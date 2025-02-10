package sctptp

import leo.datastructures.TPTP.AnnotatedFormula
import leo.datastructures.TPTP.FOF
import leo.datastructures.TPTP.FOFAnnotated
import leo.datastructures.TPTP.FOTAnnotated
import leo.modules.input.{TPTPParser}
import FOL.{*, given}
import SequentCalculus.*
import SequentCalculus as SC
import LVL2.*

import java.io.File
import leo.datastructures.TPTP.TFF.Logical


object Parser {

  type DefContext = (String => Option[Formula], String => Option[Term])


  def convertTermToFOL(term: FOF.Term)(using context: DefContext): Term = term match {

    case FOF.AtomicTerm(f, args) =>
      context._2(f) match 
        case Some(t) => t
        case None => Term(FunctionSymbol(f, args.size), args map convertTermToFOL)
    case FOF.Variable(name) => Variable(name)
    case FOF.DistinctObject(name) => throw new Exception("Distinct objects are not supported in pure FOL")
    case FOF.NumberTerm(value) => throw new Exception("Numbers are not supported in pure FOL")

  }
  

  def convertFormulaToFol(formula: FOF.Formula)(using context: DefContext): Formula = {
    formula match {
      case FOF.AtomicFormula(f, args) =>
        if f == "$true" then AtomicFormula(top, Nil)
        else if f == "$false" then AtomicFormula(bot, Nil)
        else context._1(f) match 
          case Some(f) => f
          case None => AtomicFormula(AtomicSymbol(f, args.size), args map convertTermToFOL)
      case FOF.QuantifiedFormula(quantifier, variableList, body) =>
        quantifier match {
          case FOF.! => variableList.foldRight(convertFormulaToFol(body))((s, f) => BinderFormula(Forall, VariableSymbol(s), f))
          case FOF.? => variableList.foldRight(convertFormulaToFol(body))((s, f) => BinderFormula(Exists, VariableSymbol(s), f))
          case FOF.Epsilon => throw new Exception("Epsilon quantifier at formula levvel is non-sensical")
        }
      case FOF.UnaryFormula(connective, body) =>
        connective match {
          case FOF.~ => !convertFormulaToFol(body)
        }
      case FOF.BinaryFormula(connective, left, right) =>
        connective match {
          case FOF.<=> => convertFormulaToFol(left) <=> convertFormulaToFol(right)
          case FOF.Impl => convertFormulaToFol(left) ==> convertFormulaToFol(right)
          case FOF.<= => convertFormulaToFol(right) ==> convertFormulaToFol(left)
          case FOF.<~> => !(convertFormulaToFol(left) <=> convertFormulaToFol(right))
          case FOF.~| => !(convertFormulaToFol(left) \/ convertFormulaToFol(right))
          case FOF.~& => !(convertFormulaToFol(left) /\ convertFormulaToFol(right))
          case FOF.| => convertFormulaToFol(left) \/ convertFormulaToFol(right)
          case FOF.& => convertFormulaToFol(left) /\ convertFormulaToFol(right)
        }
      case FOF.Equality(left, right) => AtomicFormula(equality, Seq(convertTermToFOL(left), convertTermToFOL(right)))
      case FOF.Inequality(left, right) => !AtomicFormula(equality, Seq(convertTermToFOL(left), convertTermToFOL(right)))
    }
  }

  def convertSequentToFol(sequent: FOF.Sequent)(using context: DefContext): Sequent = {
    Sequent(sequent.lhs.map(convertFormulaToFol), sequent.rhs.map(convertFormulaToFol))
  }




  def reconstructProof(file: File): SCProof[?] = {
    val problem = TPTPParser.problem(io.Source.fromFile(file)) 
    val nameMap = scala.collection.mutable.Map[String, Sequent]()
    var steps = List[SCProofStep]()
    var contextFormula = scala.collection.mutable.Map[String, Formula]()
    var contextTerm = scala.collection.mutable.Map[String, Term]()
    val defcontext: DefContext = (contextFormula.get, contextTerm.get)
    problem.formulas.foreach {
      case ft: FOTAnnotated =>
        if ft.role == "let" then
          val term = ft.formula
          val defedcst = "$" + ft.name
          contextTerm(defedcst) = convertTermToFOL(term)(using defcontext)
       
      case fa: FOFAnnotated =>
        if fa.role == "conjecture" then ()
        if fa.role == "let" then
          val formula = fa.formula match {
            case FOF.Logical(formula) => formula
            case s: FOF.Sequent => throw new Exception("Sequent in let statement is incorrect")
          }
          val defedcst = "$" + fa.name
          contextFormula(defedcst) = convertFormulaToFol(formula)(using defcontext)
        else
          val fofsequent = fa.formula match {
            case FOF.Logical(formula) => FOF.Sequent(Seq(), Seq(formula))
            case s: FOF.Sequent => s
          }
          if fa.role == "axiom" then
            val sequent = Sequent(fofsequent.lhs.map(convertFormulaToFol(_)(using defcontext)), fofsequent.rhs.map(convertFormulaToFol(_)(using defcontext)))
            nameMap(fa.name) = sequent
            steps = Axiom(fa.name, sequent) :: steps
          else
            annotatedStatementToProofStep(fa, e => nameMap(e), defcontext) match {
              case Some(step) =>
                nameMap(fa.name) = step.bot
                steps = step :: steps
              case None => throw new Exception(s"Proof step could not be reconstructed from ${fa.pretty}")
            }
      case _ => throw new Exception("Only FOF statements are supported")
    }
    if steps.forall(_.isInstanceOf[LVL1ProofStep]) then LVL1Proof(steps.reverse.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], file.getName().replace(".", "_"))
    if steps.forall(_.isInstanceOf[LVL2ProofStep]) then LVL2Proof(steps.reverse.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], file.getName().replace(".", "_"))
     else throw new Exception("Some proof steps could not be reconstructed")
  }

  def annotatedStatementToProofStep(ann: FOFAnnotated, sequentmap: String => Sequent, context : DefContext): Option[SCProofStep] = {
    given (String => Sequent) = sequentmap
    given DefContext = context
    val r: Option[SequentCalculus.SCProofStep] = ann match {
      case Inference.Hyp(step) => Some(step)
      case Inference.LeftWeakening(step) => Some(step)
      case Inference.RightWeakening(step) => Some(step)
      case Inference.Cut(step) => Some(step)
      case Inference.LeftHyp(step) => Some(step)
      case Inference.LeftNotNot(step) => Some(step)
      case Inference.LeftAnd(step) => Some(step)
      case Inference.LeftNotOr(step) => Some(step)
      case Inference.LeftNotImp(step) => Some(step)
      case Inference.LeftNotAnd(step) => Some(step)
      case Inference.LeftOr(step) => Some(step)
      case Inference.LeftImp1(step) => Some(step)
      case Inference.LeftImp2(step) => Some(step)
      case Inference.LeftNotAll(step) => Some(step)
      case Inference.LeftEx(step) => Some(step)
      case Inference.LeftAll(step) => Some(step)
      case Inference.LeftNotEx(step) => Some(step)
      case Inference.RightNot(step) => Some(step)
      case Inference.RightAnd(step) => Some(step)
      case Inference.RightOr(step) => Some(step)
      case Inference.RightImp(step) => Some(step)
      case Inference.RightIff(step) => Some(step)
      case Inference.RightEx(step) => Some(step)
      case Inference.RightAll(step) => Some(step)
      case Inference.Congruence(step) => Some(step)
      case Inference.Res(step) => Some(step)
      case Inference.NegatedConjecture(step) => Some(step)
      case Inference.Instantiate_L(step) => Some(step)
      case _ => None
    }
    r
  }

  object Inference {
    import leo.datastructures.TPTP.{Annotations, GeneralTerm, MetaFunctionData, NumberData, Integer, FOF, GeneralFormulaData, FOTData, FOFData}

    object Number {
      def unapply(ann_seq: GeneralTerm): Option[BigInt] =
        ann_seq match {
          case GeneralTerm(List(NumberData(Integer(n))), None) => Some(n)
          case _ => None
        }
    }
    object GenTerm {
      def unapply(ann_seq: GeneralTerm)(using context: DefContext): Option[Term] =
        ann_seq match {
          case GeneralTerm(List(GeneralFormulaData(FOTData(term))), None) => Some(convertTermToFOL(term))
          case _ => None
        }
    }
    object GenFormula {
      def unapply(ann_seq: GeneralTerm)(using context: DefContext): Option[Formula] =
        ann_seq match {
          case GeneralTerm(List(GeneralFormulaData(FOFData(FOF.Logical(formula)))), None) => Some(convertFormulaToFol(formula))
          case _ => None
        }
    }
    object String {
      def unapply(ann_seq: GeneralTerm): Option[String] =
        ann_seq match {
          case GeneralTerm(List(MetaFunctionData(string, List())), None) => Some(string)
          case _ => None
        }
    }
    object StrOrNum {
      def unapply(ann_seq: GeneralTerm): Option[String] =
        ann_seq match {
          case String(s) => Some(s)
          case Number(n) => Some(n.toString)
          case _ => None
        }
    }
    def unapply(ann_seq: Annotations): Option[(String, Seq[GeneralTerm], Seq[String])] =
      ann_seq match {
        case Some(
              (
                GeneralTerm(
                  List(
                    MetaFunctionData(
                      "inference",
                      List(
                        GeneralTerm(List(MetaFunctionData(stepName, List())), None), // stepnames
                        GeneralTerm(List(), Some(parameters)), // params
                        GeneralTerm(List(), Some(numberTerms))
                      ) // numbers
                    )
                  ),
                  None
                ),
                None
              )
            ) =>
          Some(
            (
              stepName,
              parameters,
              numberTerms.map {
                case StrOrNum(n) => n.toString
                case String(n) => n
                case _ => throw new Exception(s"Expected a list of number as last parameter of inference, but got $numberTerms")
              }
            )
          )
        case _ => None
      }

    object Hyp {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("hyp", Seq(_, StrOrNum(n), StrOrNum(m)), Seq()), _) =>
            if (sequent.lhs(n.toInt) == sequent.rhs(m.toInt)) then
              val left = sequent.lhs.map(convertFormulaToFol)
              val right = sequent.rhs.map(convertFormulaToFol)
              Some(SC.Hyp(name, Sequent(left, right), n.toInt, m.toInt))
            else None
          case _ => None
        }
    }
    
    object LeftHyp {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftHyp", Seq(_, StrOrNum(n), StrOrNum(m)), Seq()), _) =>
            Some(SC.LeftHyp(name, convertSequentToFol(sequent), n.toInt, m.toInt))
          case _ =>
            None
        }
    }

    object LeftWeakening {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        println("Test")
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftWeaken", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.LeftWeakening(name, convertSequentToFol(sequent), t1))
          case _ => None
        }
    }

    object RightWeakening {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightWeaken", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.RightWeakening(name, convertSequentToFol(sequent), t1))
          case _ => None
        }
    }



    object Cut {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("cut", Seq(_, StrOrNum(n), StrOrNum(m)), Seq(t1, t2)), _) =>
            Some(SC.Cut(name, convertSequentToFol(sequent), n.toInt, m.toInt, t1, t2))
          case _ =>
            None
        }

    }


    object LeftAnd {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftAnd", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.LeftAnd(name, convertSequentToFol(sequent), n.toInt, t1))
          case _ => None
        }
    }

    object LeftOr {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftOr", Seq(_, StrOrNum(n)), Seq(t1, t2)), _) =>
            val f = sequent.lhs(n.toInt)
            val (a, b) = convertFormulaToFol(f) match {
              case ConnectorFormula(Or, Seq(x, y)) => (x, y)
              case _ => throw new Exception(s"Expected a disjunction, but got $f")
            }
            Some(SC.LeftOr(name, convertSequentToFol(sequent), n.toInt, t1, t2))
          case _ => None
        }
    }

    object LeftImp1 {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftImp1", Seq(_, StrOrNum(n)), Seq(t1, t2)), _) =>
            val f = sequent.lhs(n.toInt)
            val (a, b) = convertFormulaToFol(f) match {
              case ConnectorFormula(Implies, Seq(x, y)) => (x, y)
              case _ => throw new Exception(s"Expected an implication, but got $f")
            }
            Some(SC.LeftImplies(name, convertSequentToFol(sequent), n.toInt, t1, t2))
          case _ => None
        }
    }

    object LeftImp2 {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftImp2", Seq(_, StrOrNum(n)), Seq(t1, t2)), _) =>
            
            Some(SC.LeftImp2(name, convertSequentToFol(sequent), n.toInt, t1, t2))
          case _ => None
        }
    }

    object LeftIff {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftIff", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.LeftIff(name, convertSequentToFol(sequent), n.toInt, t1))
          case _ => None
        }
    }

    object LeftNot {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftNot", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.LeftNot(name, convertSequentToFol(sequent), n.toInt, t1))
          case _ => None
        }
    }

    object LeftEx {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftEx", Seq(_, StrOrNum(n), GenTerm(xl)), Seq(t1)), _) => // x has to be a GeneralTerm representinf a variable, i.e. $fot(x)
            val x = xl match
              case Term(x: VariableSymbol, Seq()) => x
              case _ => throw new Exception(s"Expected a variable, but got $xl")
            Some(SC.LeftEx(name, convertSequentToFol(sequent), n.toInt, x, t1))
          case _ => None
        }
    }

    object LeftAll {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftForall", Seq(_, StrOrNum(n), GenTerm(t)), Seq(t1)), _) =>
            val f = sequent.lhs(n.toInt)
            Some(SC.LeftAll(name, convertSequentToFol(sequent), n.toInt, t, t1))
          case _ => None
        }
    }

    object RightAnd {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightAnd", Seq(_, StrOrNum(n)), Seq(t1, t2)), _) =>
            Some(SC.RightAnd(name, convertSequentToFol(sequent), n.toInt, t1, t2))
          case _ => None
        }
    }

    object RightOr {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightOr", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.RightOr(name, convertSequentToFol(sequent), n.toInt, t1))
          case _ => None
        }
    }

    object RightImp {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightImp", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.RightImplies(name, convertSequentToFol(sequent), n.toInt, t1))
          case _ => None
        }
    }

    object RightIff {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightIff", Seq(_, StrOrNum(n)), Seq(t1, t2)), _) =>
            Some(SC.RightIff(name, convertSequentToFol(sequent), n.toInt, t1, t2))
          case _ => None
        }
    }

    object RightNot {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightNot", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.RightNot(name, convertSequentToFol(sequent), n.toInt, t1))
          case _ => None
        }
    }

    object RightEx {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightEx", Seq(_, StrOrNum(n), GenTerm(t)), Seq(t1)), _) =>
            
            Some(SC.RightEx(name, convertSequentToFol(sequent), n.toInt, t, t1))
          case _ => None
        }
    }

    object RightAll {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightForall", Seq(_, StrOrNum(n), GenTerm(xl)), Seq(t1)), _) =>
            val x = xl match
              case Term(x: VariableSymbol, Seq()) => x
              case _ => throw new Exception(s"Expected a variable, but got $xl")
            Some(SC.RightAll(name, convertSequentToFol(sequent), n.toInt, x, t1))
          case _ => None
        }
    }

    object LeftNotAnd {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftNotAnd", Seq(_, StrOrNum(n)), Seq(t1, t2)), _) =>
            val f = sequent.lhs(n.toInt)
            val (a, b) = convertFormulaToFol(f) match {
              case ConnectorFormula(Neg, Seq(ConnectorFormula(And, Seq(x, y)))) => (x, y)
              case _ => throw new Exception(s"Expected a negated conjunction, but got $f")
            }
            Some(SC.LeftNotAnd(name, convertSequentToFol(sequent), n.toInt, t1, t2))
          case _ => None
        }
    }
    
    object LeftNotOr {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftNotOr", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.LeftNotOr(name, convertSequentToFol(sequent), n.toInt, t1))
          case _ => None
        }
    }

    object LeftNotImp {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftNotImp", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.LeftNotImp(name, convertSequentToFol(sequent), n.toInt, t1))
          case _ => None
        }
    }

    object LeftNotIff {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftNotIff", Seq(_, StrOrNum(n)), Seq(t1, t2)), _) =>
            Some(SC.LeftNotIff(name, convertSequentToFol(sequent), n.toInt, t1, t2))
          case _ => None
        }
    }

    object LeftNotNot {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftNotNot", Seq(_, StrOrNum(n)), Seq(t1)), _) =>
            Some(SC.LeftNotNot(name, convertSequentToFol(sequent), n.toInt, t1))
          case _ => None
        }
    }

    object LeftNotEx {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftNotEx", Seq(_, StrOrNum(n), GenTerm(t)), Seq(t1)), _) =>
            val f = sequent.lhs(n.toInt)
            val (x, phi) = convertFormulaToFol(f) match {
              case ConnectorFormula(Neg, Seq(BinderFormula(Exists, x, phi))) => (x, phi)
              case _ => throw new Exception(s"Expected a negated existential quantification, but got $f")
            }
            Some(SC.LeftNotEx(name, convertSequentToFol(sequent), n.toInt, t, t1))
          case _ => None
        }
    }

    object LeftNotAll {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] =
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("leftNotForall", Seq(_, StrOrNum(n), GenTerm(xl)), Seq(t1)), _) => // x has to be a GeneralTerm representinf a variable, i.e. $fot(x)
            val x = xl match
              case Term(x: VariableSymbol, Seq()) => x
              case _ => throw new Exception(s"Expected a variable, but got $xl")
            Some(SC.LeftNotAll(name, convertSequentToFol(sequent), n.toInt, x, t1))
          case _ => None
        }
    }

    object RightRefl {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightRefl", Seq(_, StrOrNum(n)), Seq()), _) =>
            Some(SC.RightRefl(name, convertSequentToFol(sequent), n.toInt))
          case _ => None
        }
    }

    object LeftSubst {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightSubst", Seq(_, StrOrNum(n), GenTerm(xl), GenFormula(p)), Seq(t1)), _) =>
            val x = xl match
              case Term(x: VariableSymbol, Seq()) => x
              case _ => throw new Exception(s"Expected a variable, but got $xl")
            Some(SC.LeftSubst(name, convertSequentToFol(sequent), n.toInt, p, x, t1))
          case _ => None
        }
    }

    object RightSubst {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("rightSubst", Seq(_, StrOrNum(n), GenTerm(xl), GenFormula(p)), Seq(t1)), _) =>
            val x = xl match
              case Term(x: VariableSymbol, Seq()) => x
              case _ => throw new Exception(s"Expected a variable, but got $xl")
            Some(SC.RightSubst(name, convertSequentToFol(sequent), n.toInt, p, x, t1))
          case _ => None
        }
    }

    object Congruence {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("congruence", Seq(), Seq()), _) =>
            Some(LVL2.Congruence(name, convertSequentToFol(sequent)))
          case _ => None
        }
    }

    object Res {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("res", Seq(_, StrOrNum(i), StrOrNum(j)), Seq(t1, t2)), _) =>
            Some(LVL2.Res(name, convertSequentToFol(sequent), i.toInt, j.toInt, t1, t2))
          case _ => None
        }
    }

    object NegatedConjecture {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("negated_conjecture", Seq(_), Seq(t1)), _) =>
            Some(LVL2.NegatedConjecture(name, convertSequentToFol(sequent), t1))
          case _ => None
        }
    }

    object Instantiate_L {
      def unapply(ann_seq: FOFAnnotated)(using sequentmap: String => Sequent, context: DefContext): Option[SCProofStep] = 
        ann_seq match {
          case FOFAnnotated(name, role, sequent: FOF.Sequent, Inference("instantiate_l", Seq(_, StrOrNum(i), GenTerm(x), GenTerm(t)), Seq(t1)), _) =>
            val x2 = x match 
              case Term(xs: VariableSymbol, Seq()) => xs
              case _ => throw new Exception(s"Expected a variable, but got $x")
            Some(LVL2.Instantiate_L(name, convertSequentToFol(sequent), i.toInt, x2, t, t1))
          case _ => None
        }
    }
  }
}
