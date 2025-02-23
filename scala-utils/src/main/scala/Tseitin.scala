package sctptp
import SequentCalculus as SC
import LVL2.*
import SequentCalculus.*
import sctptp.FOL.*
import sctptp.LVL2.*
import FOL.*
import datastructure.mutable.EGraphTerms
import sctptp.FOL.AtomicFormula
import java.text.Normalizer.Form
import sctptp.FOL.Forall
import sctptp.FOL.Variable
import javax.imageio.plugins.tiff.ExifGPSTagSet
import scala.util.boundary
import scala.annotation.switch
import javax.swing.text.StyledEditorKit.ForegroundAction
import java.util.concurrent.atomic.AtomicLongFieldUpdater
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import sys.process._
import scala.util.matching.Regex
import scala.io.Source
import scala.util.matching.Regex
import Parser.*
import java.io.File
import sctptp.SequentCalculus.RulesName.LeftFalseRuleName



class Tseitin {

  val varName = "V"
  val tseitinName = "Ts"
  var varCpt = 0
  val skoName = "sko"
  var skoCpt = 0 
  val ladrName = "ts"
  var ladrCpt = -1
  val instantiateName = "i"
  val tseitinStepName = "$tsStep"
  val tseitinStepNameExpl = "tsStepExpl"
  val NoneFormula : Formula = AtomicFormula(AtomicSymbol("None", 0), Seq())
  var phi = NoneFormula
  val psi = AtomicFormula(AtomicSymbol("$psi", 0), Seq())
  var originalFormula = NoneFormula
  var cptTSExpSteps = -1

  // Var = Tseitin Var, Term = Original formula Term
  var tseitinVarTerm: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula]()
  var tseitinTermVar: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula]()
  var tseitinVarTermUp: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula]()
  var tseitinTermVarUp: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula]()

  def getTseitinVarTerm(): Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = tseitinVarTerm

  def getTseitinTermVar(): Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = tseitinTermVar

  def getTseitinVarTermUp(): Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = tseitinVarTermUp

  def getTseitinTermVarUp(): Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = tseitinTermVarUp

  def printTseitinVarTerm() = {
    for ((k, v) <- getTseitinVarTerm()) {
      println(k.toString() + " : " + v.toString())
    }
  }

  def printTseitinVarTermUp() = {
    for ((k, v) <- getTseitinVarTermUp()) {
      println(k.toString() + " : " + v.toString())
    }
  }

  def makeTseitinMaps(TermVar: Map[sctptp.FOL.Formula, sctptp.FOL.Formula]) = {
    tseitinTermVar = tseitinTermVar ++ TermVar
    tseitinVarTerm = tseitinVarTerm ++ TermVar.map(_.swap)
  }

  def makeTseitinMapsUp(TermVar: Map[sctptp.FOL.Formula, sctptp.FOL.Formula]) = {
    tseitinTermVarUp = tseitinTermVarUp ++ TermVar
    tseitinVarTermUp = tseitinVarTermUp ++ TermVar.map(_.swap)
  }

  // ----------------------------------------------------
  // ------------------ Pre Processing ------------------
  //-----------------------------------------------------

    // Transform a formula into nnf
  def toNegatedFormula(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, AtomicFormula, Seq[LVL2ProofStep]) = {
    val phi_name = "phi"
    phi = AtomicFormula(AtomicSymbol(Identifier("$"+phi_name), 0), Seq())
    originalFormula = f
    val new_f = ConnectorFormula(Neg , Seq(f)) 
    val initial_let = Let(phi_name, Sequent(Seq(phi), Seq(new_f)))

    (new_f, phi.asInstanceOf[AtomicFormula], Seq(
      Hyp("negated_conjecture", Sequent(Seq(new_f), Seq(new_f)), 0), 
      initial_let))
  }

  // Transform a formula into nnf
  def toNNF(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, LVL2ProofStep) = {
    
    def toNNFForm(f: sctptp.FOL.Formula): sctptp.FOL.Formula = {
      f match {
        case AtomicFormula(label, args) => f
        case ConnectorFormula(label, args) => {
          label match {
            case Neg => {
              args(0) match {
                case AtomicFormula(_, _) => f
                case ConnectorFormula(label2, args2) => {
                  label2 match {
                    case Neg => args2(0)
                    case Or => sctptp.FOL.ConnectorFormula(And, args2.map(x => toNNFForm(ConnectorFormula(Neg, Seq(x)))))
                    case And => sctptp.FOL.ConnectorFormula(Or, args2.map(x => toNNFForm(ConnectorFormula(Neg, Seq(x)))))
                    case Implies => sctptp.FOL.ConnectorFormula(And, Seq(toNNFForm(args2(0)), toNNFForm(ConnectorFormula(Neg, Seq(args2(1))))))
                    case Iff =>
                      sctptp.FOL.ConnectorFormula(
                        Or,
                        Seq(
                          sctptp.FOL.ConnectorFormula(And, Seq(toNNFForm(args2(0)), toNNFForm(args2(1)))),
                          sctptp.FOL.ConnectorFormula(And, Seq(toNNFForm(ConnectorFormula(Neg, Seq(args2(0)))), toNNFForm(ConnectorFormula(Neg, Seq(args2(1))))))
                        )
                      )
                  }
                }
                case BinderFormula(label, bound, inner) => {
                  label match {
                    case Forall => sctptp.FOL.BinderFormula(Exists, bound, toNNFForm(ConnectorFormula(Neg, Seq(inner))))
                    case Exists => sctptp.FOL.BinderFormula(Forall, bound, toNNFForm(ConnectorFormula(Neg, Seq(inner))))
                  }
                }
              }
            }
            case Or => sctptp.FOL.ConnectorFormula(Or, args.map(x => toNNFForm(x)))
            case And => sctptp.FOL.ConnectorFormula(And, args.map(x => toNNFForm(x)))
            case Implies => sctptp.FOL.ConnectorFormula(And, Seq(ConnectorFormula(Neg, Seq(args(0))), toNNFForm(args(1))))
            case Iff =>
              sctptp.FOL.ConnectorFormula(
                Or,
                Seq(
                  sctptp.FOL.ConnectorFormula(And, Seq(toNNFForm(args(0)), toNNFForm(args(1)))),
                  sctptp.FOL.ConnectorFormula(And, Seq(toNNFForm(ConnectorFormula(Neg, Seq(args(0)))), toNNFForm(ConnectorFormula(Neg, Seq(args(1))))))
                )
              )
          }
        }
        case BinderFormula(label, bound, inner) => sctptp.FOL.BinderFormula(label, bound, toNNFForm(inner))
      }
    }
    val new_f = toNNFForm(f)
    (new_f, NNF("nnf_step", Sequent(Seq(phi), Seq(new_f)), 0, 0, "negated_conjecture"))
  }

  // Transform a formula in Prenex Normal form (move quantifier to the top)
  def retrieveQT(f2 : sctptp.FOL.Formula): (List[(BinderSymbol, VariableSymbol)], sctptp.FOL.Formula) = {
    f2 match 
      case AtomicFormula(label, args) => (List(), f2) 
      case BinderFormula(label, bound, inner) => {
        val (accQT2, accF2) = retrieveQT(inner)
        (accQT2 :+ (label, bound), accF2)
      } 
      case ConnectorFormula(label, args) => {
        val (accQT2, accF2) = args.foldLeft((List[(BinderSymbol, VariableSymbol)](), List[Formula]()))((acc, x) => {
            val (accQT3, accF3) = retrieveQT(x)
            (accQT3 ++ acc._1,  acc._2 :+ accF3)
        })
        (accQT2, ConnectorFormula(label, accF2))
      }
  }

  def reInsertQT(accQT: List[(BinderSymbol, VariableSymbol)], accF2 : sctptp.FOL.Formula) : sctptp.FOL.Formula = {
    accQT.foldLeft(accF2)((acc, x) => {
      x match 
        case (Forall, x2) => sctptp.FOL.BinderFormula(Forall, x2, acc)
        case (Exists, x2) => sctptp.FOL.BinderFormula(Exists, x2, acc)
    })
  }

  def toPrenex(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, LVL2ProofStep) = {
    val (accQT, accF) = retrieveQT(f)
    val new_f = reInsertQT(accQT, accF)
    (new_f, Prenex("prenex_step", Sequent(Seq(phi), Seq(new_f)) ,"nnf_step"))
  }

  // Instantiate universal quantifiers and returns a map [oldName, newName], the new formula, and has renamed them according to P9 standard
  // 0 = forall, 1 = exists
  def toInstantiated(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, Map[VariableSymbol, VariableSymbol], Seq[LVL2ProofStep]) = {
      def toInstantiatedAux(f2: sctptp.FOL.Formula, accVS: Map[VariableSymbol, VariableSymbol], accT: Map[VariableSymbol, Term], accProof: Seq[LVL2ProofStep], cpt: Int): (sctptp.FOL.Formula, Map[VariableSymbol, VariableSymbol], Seq[LVL2ProofStep]) = {
        f2 match 
          case BinderFormula(label, bound, inner) => {
            label match
              case Forall => {
                val new_symbol = VariableSymbol(varName+varCpt)
                val new_accVS = accVS + (bound -> new_symbol)
                val new_term = Variable(new_symbol)
                val new_accT = accT + (bound -> new_term)
                varCpt = varCpt+1
                val new_form = substituteVariablesInFormula(inner, new_accT)
                val ps = InstForall(instantiateName+cpt, Sequent(Seq(phi), Seq(new_form)), 0, new_symbol, if cpt > 0 then s"${instantiateName}${cpt-1}" else "prenex_step")
                val (new_f, new_acc_next_steps, new_accProof) = toInstantiatedAux(new_form, new_accVS, new_accT, ps +: accProof , cpt+1)
                (new_f, new_acc_next_steps, new_accProof)
              }
              case Exists => {
                val previousVarForall = accVS.foldLeft(Seq[VariableSymbol]())((acc, x) => acc :+ x._2)
                val new_symbol = FunctionSymbol(skoName+varCpt, previousVarForall.size)
                val new_term = FunctionTerm(new_symbol, previousVarForall.map(x => Variable(x)))
                val new_accT = accT + (bound -> new_term)
                skoCpt = skoCpt+1
                val new_form = substituteVariablesInFormula(inner, new_accT)
                val ps = RightExists(instantiateName+cpt, Sequent(Seq(phi),Seq(new_form)), 0, new_term, if cpt > 0 then s"${instantiateName}${cpt-1}" else "prenex_step")
                val (new_f, new_acc_next_steps, new_accProof) = toInstantiatedAux(new_form, accVS, new_accT, ps +: accProof , cpt+1)
                (new_f, new_acc_next_steps, new_accProof)
              }
          }
          case _ => (f2, accVS, accProof)
      }

      toInstantiatedAux(f,  Map[VariableSymbol, VariableSymbol](), Map[VariableSymbol, Term](), Seq[LVL1ProofStep](), 0)
  }

  // Go through the formula and associate each subformula to a variable
  def createTseitinVariables(f: sctptp.FOL.Formula, acc: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula](), next_index: Int = 0): (Map[sctptp.FOL.Formula, sctptp.FOL.Formula], Int) = {
    // println("Next id = " + next_index)
    // println("acc = " + acc.toString())
    // println("f = " + f.toString())
    // println("Contains : " + acc.contains(f))
    // println("------------------------------")
    f match
      case AtomicFormula(label, args) => (acc, next_index)
      case ConnectorFormula(label, args) => {
        if (acc.contains(f)) {
          args.foldLeft((acc, next_index))((acc2, c) => (createTseitinVariables(c, acc2._1, next_index)._1, acc2._2))
        } else {
          label match
            case Neg => (acc, next_index)
            case _ => {
              args.foldLeft((acc, next_index))((acc2, c) => {
                ladrCpt = ladrCpt+1
                val (newAcc, newCpt) = createTseitinVariables(c, acc2._1 + (f -> AtomicFormula(AtomicSymbol( (if (f.freeVariables.size == 0) then Identifier(ladrName + ladrCpt, acc2._2) else Identifier(tseitinName + acc2._2, acc2._2) ), f.freeVariables.size), f.getFreeVariables())), acc2._2 + 1)
                (newAcc, newCpt)
                })    
              }
        }

      }
      case BinderFormula(label, bound, inner) => {
        if (acc.contains(f)) {
          createTseitinVariables(inner, acc, next_index)
        } else {
          ladrCpt = ladrCpt+1
          createTseitinVariables(inner, acc + (f -> AtomicFormula(AtomicSymbol((if (f.freeVariables.size == 0) then Identifier(ladrName + ladrCpt, next_index) else Identifier(tseitinName + next_index, next_index)), f.freeVariables.size), f.getFreeVariables())), next_index + 1)
        }
      }
  }

  // Take tseitinTermVar et return tseitinTermVar updated
  def updateTseitinVariables(map: Map[sctptp.FOL.Formula, sctptp.FOL.Formula]) : Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = {
    map.foldLeft(Map[sctptp.FOL.Formula, sctptp.FOL.Formula]())((acc, x) =>
      val (termForm, varTseitin) = x
      termForm match {
        case AtomicFormula(label, args) => acc + (termForm -> varTseitin)
        case ConnectorFormula(label, args) => acc + (ConnectorFormula(label, args.map(x => if (map contains x) then map(x) else x)) -> varTseitin)
        case  BinderFormula(label, bound, inner) => acc + (BinderFormula(label, bound, map(inner)) -> varTseitin)
      }
    )
  }

  // Transform a formula in Prenex Negatted Normal form into Tseitin Normal Form with variable stored in tseitinVarTerm
  def toTseitin(f2: sctptp.FOL.Formula): sctptp.FOL.Formula =  {

    def toTseitinAux(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, sctptp.FOL.Formula) = {
    val varT = if (getTseitinTermVar() contains f) then getTseitinTermVar()(f) else f
    val varTneg = ConnectorFormula(Neg, Seq(varT))
    f match
      case AtomicFormula(label, args) => (f, NoneFormula) 
      case ConnectorFormula(label, args) => {
        // println(s"La variable introduite pour ${f.toString()} est ${varT.toString()}")
        label match {
          case Neg => {
            val (p, c) = toTseitinAux(args(0))
            (ConnectorFormula(Neg, Seq(p)), c)
          }
          case And => {
            val (p1, c1) = toTseitinAux(args(0))
            val (p2, c2) = toTseitinAux(args(1))
            val formTokeep = 
              (c1, c2) match
                case (NoneFormula, NoneFormula) => Seq()
                case (_, NoneFormula) => Seq(c1)
                case (NoneFormula, _) => Seq(c2)
                case (_, _) => Seq(c1, c2)
            val new_c = ConnectorFormula(And, formTokeep ++ Seq(ConnectorFormula(Or, Seq(varT, ConnectorFormula(Neg, Seq(p1)), ConnectorFormula(Neg, Seq(p2))))) :+ ConnectorFormula(Or, Seq(varTneg, p1)) :+ ConnectorFormula(Or, Seq(varTneg, p2)))
            (varT, new_c)
          } 

          case Or => {
            val (p1, c1) = toTseitinAux(args(0))
            val (p2, c2) = toTseitinAux(args(1))
            val formTokeep = 
              (c1, c2) match
                case (NoneFormula, NoneFormula) => Seq()
                case (_, NoneFormula) => Seq(c1)
                case (NoneFormula, _) => Seq(c2)
                case (_, _) => Seq(c1, c2)
            val new_c = ConnectorFormula(And, formTokeep ++ Seq(ConnectorFormula(Or, Seq(varTneg, p1, p2))) :+ ConnectorFormula(Or, Seq(varT, ConnectorFormula(Neg, Seq(p1)))) :+ ConnectorFormula(Or, Seq(varT, ConnectorFormula(Neg, Seq(p2)))))
            (varT, new_c)
          } 
          case _ => throw new Exception("toTseitin failed")
        }
      }
      case BinderFormula(label, bound, inner) => (varT, BinderFormula(label, bound, toTseitin(inner)))
    }

    val (l: sctptp.FOL.Formula, c: sctptp.FOL.Formula) = toTseitinAux(f2) 
    // println(s"c = ${c.toString()}")
    // println(s"l = ${l.toString()}")
    if (c != NoneFormula) then toPrenex(ConnectorFormula(And, Seq(l, c)))._1
    else toPrenex(l)._1
  }

  // Flattern  a formula in CNF
  def toFlatternAnd(f: sctptp.FOL.Formula): sctptp.FOL.Formula = {
    def toFlatternAndAux(f2: sctptp.FOL.Formula): Seq[sctptp.FOL.Formula] = {
      f2 match
        case ConnectorFormula(And, args) => args.foldLeft(Seq())((acc, x) => acc ++ toFlatternAndAux(x))
        case _ => Seq(f2)
    }

    f match 
      case ConnectorFormula(And, args) =>  ConnectorFormula(And, args.foldLeft(Seq())((acc, x) => acc ++ toFlatternAndAux(x)))
      case BinderFormula(label, bound, inner) => BinderFormula(label, bound, toFlatternAnd(inner))
      case _ => throw new Exception("toFlatternAnd failed")
  }

  // Flattern  a formula in DNF
  def toFlatternOr(f: sctptp.FOL.Formula): sctptp.FOL.Formula = {
    def toFlatternOrAux(f2: sctptp.FOL.Formula): Seq[sctptp.FOL.Formula] = {
      f2 match
        case ConnectorFormula(Or, args) => args.foldLeft(Seq())((acc, x) => acc ++ toFlatternOrAux(x))
        case _ => Seq(f2)
    }

    f match 
      case ConnectorFormula(Or, args) =>  ConnectorFormula(Or, args.foldLeft(Seq())((acc, x) => acc ++ toFlatternOrAux(x)))
      case BinderFormula(label, bound, inner) => BinderFormula(label, bound, toFlatternOr(inner))
      case _ => f
  }

  // Input : a formula f and a map an generate the let that connect he variable to the formula
  def generateLet(f: sctptp.FOL.Formula, sf: sctptp.FOL.Formula, cpt: Int): (LVL2ProofStep, AtomicFormula) = {
    val atomicForm = AtomicFormula(AtomicSymbol(Identifier(tseitinStepName + cpt), 0), Seq())
    val new_f = sf.freeVariables.foldLeft(ConnectorFormula(Iff, Seq(f, sf)).asInstanceOf[Formula])((acc, x) => BinderFormula(Forall, x, acc))
    (Let(atomicForm.label.id.name.drop(1), Sequent(Seq(), Seq(new_f))), atomicForm)
  }

  // Input : a formula f and a map an generate the let that connect he variable to the formula
  def generateTseitin(): (Seq[LVL2ProofStep], Map[AtomicFormula, AtomicFormula], Seq[AtomicFormula]) = {
    val res = tseitinVarTermUp.foldLeft((Seq[LVL2ProofStep](), Map[AtomicFormula, AtomicFormula](), Seq[AtomicFormula](), tseitinVarTermUp.size-1))((acc, x) => {
      val (ps, psname) = generateLet(x._1, x._2, acc._4)
      (acc._1 :+ ps, acc._2 + (psname -> x._1.asInstanceOf[AtomicFormula]), acc._3 :+ psname, acc._4-1)
      })
      
    (res._1, res._2, res._3)
    }
  
  // Compute tsStep steps, that replace a tseitin formula
  def computeTseitinReplacementSteps(s: SCProofStep, tsNames: Seq[AtomicFormula], map: Map[AtomicFormula, AtomicFormula], tsSteps: Seq[LVL2ProofStep], original_parent: String): Seq[LVL2ProofStep] = {

      // if there is a forall in the corresponding axiom, first instantiate it 
      def computeTseitinReplacementStepsAux(
        f: Formula, 
        f_right: Formula,
        previousForms: Seq[Formula],
        fSubstituted: Formula,
        fSubstituted2: Formula,
        new_var: AtomicSymbol,
        parent: String,
        ): Seq[LVL2ProofStep] = {
        val f_with_qt = f

        f_with_qt match
          case AtomicFormula(_, _) |  ConnectorFormula(_, _) => {
              cptTSExpSteps = cptTSExpSteps + 1
              val final_f = RightSubstIff(tseitinStepNameExpl+cptTSExpSteps,
              Sequent( 
              previousForms, // les formules d'avant, avec un acc
              Seq(fSubstituted)), 
              previousForms.size-1, 
              fSubstituted2,
              new_var, 
              parent)
              Seq(final_f)
          }
          case BinderFormula(label, bound, inner) => {}
            label match
              case Forall => {
                cptTSExpSteps = cptTSExpSteps+1
                val f_next_step = LeftForall(tseitinStepNameExpl+cptTSExpSteps, Sequent(phi +: tsNames.take(previousForms.size+1), Seq(f_right)), previousForms.size, Variable(bound), tseitinStepNameExpl+(cptTSExpSteps+2))
                computeTseitinReplacementStepsAux(inner, f_right, previousForms, fSubstituted, fSubstituted2, new_var, parent) :+ f_next_step// same with inner
              }
              case Exists => throw Exception("Compute Tseitin replacement step: existential quantifier not allowed")
      }
      
      def computeInnerFromStep(s: LVL2ProofStep): Formula = {
        def computeInnerFromStepAux(f: Formula): Formula = {
          f match 
            case AtomicFormula(label, args) => f
            case ConnectorFormula(label, args) => f 
            case BinderFormula(label, bound, inner) => computeInnerFromStepAux(inner)
        }

        s.bot.right(0) match 
          case AtomicFormula(label, args) => AtomicFormula(AtomicSymbol("$"+s.name, 0), Seq())
          case ConnectorFormula(label, args) => AtomicFormula(AtomicSymbol("$"+s.name, 0), Seq())
          case BinderFormula(label, bound, inner) => computeInnerFromStepAux(inner)
      }
      // fof(i0, plain, [$phi] --> [(p(V0) & (~p(a) | ~p(b)))], inference(instForall, [status(thm), 0, $fot(V0)], [prenex_step])).
      // fof(tsStep0, let, (Ts3 <=> (~p(a) | ~p(b)))).
      // fof(tsStep1, let, ! [V0]: (Ts1(V0) <=> (p(V0) & Ts3))).

      // TS1
      // fof(tsStepExpl0, plain, [$phi,$tsStep0] --> [(p(V0) & Ts3)], inference(rightSubstIff, [status(thm), 1, $fof(p(V0) & A), 'A'], [i0])).
      
      
      // fof(tsStepExpl1, plain, [$phi,$tsStep0,(Ts1(V0) <=> (p(V0) & Ts3))] --> [Ts1(V0)], inference(rightSubstIff, [status(thm), 2, $fof(A), 'A'], [tsStepExpl0])).
      
      // TS0
      // fof(4, plain, [$phi,$tsStep0,$tsStep1] --> [Ts1(V0)], inference(leftForall, [status(thm), 2, $fot(V0)], [tsStepExpl1])).


      // println("#####################")
      // tsSteps.map(x => println(x))
      // println("-------------------")
      // println(tsNames)
      // println("-------------------")
      // println(map)
      // println("-------------------")
      // println(tseitinTermVarUp)
      // println("#####################")


      val final_res = tsSteps.foldLeft(
        (Seq[LVL2ProofStep](), 0, Map[Formula, Formula](), s.bot.right(0), Seq[LVL2ProofStep]()))(
          (acc, x) => {

            val new_acc3 = acc._3 + (tseitinVarTermUp(map(tsNames(acc._2))) -> map(tsNames(acc._2)))
            val previousForms = tsNames.take(acc._2+1) // acc + courant
            val fSubstituted = substituteFormulaInFormula(
              acc._4, 
              new_acc3 
              )

            val new_var = AtomicSymbol(Identifier("A"), 0)
            val fSubstituted2 = substituteFormulaInFormula(
              acc._4, 
              acc._3 + (tseitinVarTermUp(map(tsNames(acc._2))) -> AtomicFormula(new_var, Seq())) 
            )
            val parent = if (acc._2 == 0) then original_parent else tseitinStepNameExpl+cptTSExpSteps
            

            // Compute the formula with free variables
            val aux_steps = computeTseitinReplacementStepsAux(
              x.bot.right(0), 
              substituteFormulaInFormula(
                fSubstituted, 
                if (acc._2 < tsSteps.size -1) 
                  then new_acc3 + (tseitinVarTermUp(map(tsNames(acc._2 + 1))) -> map(tsNames(acc._2 + 1)))
                  else new_acc3
              ), 
              phi +: previousForms,
              fSubstituted,
              fSubstituted2,
              new_var,
              parent
              ).reverse

            // cptTSExpSteps = cptTSExpSteps + 1
            val final_f = aux_steps.last

            val old_x = if (acc._5.size == 0) then Seq() else (Seq(computeInnerFromStep(acc._5(0))))

            val final_f2 = RightSubstIff(tseitinStepNameExpl+cptTSExpSteps,
            Sequent( 
            phi +: (previousForms.dropRight(1) ++old_x), // les formules d'avant, avec un acc
            Seq(fSubstituted)), 
            previousForms.size, 
            fSubstituted2,
            new_var, 
            parent)

            
            val final_f3 = if aux_steps.size > 1 then final_f else final_f2
            val final_aux = aux_steps.dropRight(1) // if aux_steps.size == 0 then Seq() else aux_steps.dropRight(1)

            // println(s"Je traite : ${x.name}")
            // println(s"fSubst = " + fSubstituted)
            // println(s"final_f = " + final_f)
            // println(s"fSubst2 = " + fSubstituted)
            // println(AtomicFormula(AtomicSymbol("$"+x.name, 0), Seq()))
            // println(map(AtomicFormula(AtomicSymbol("$"+x.name, 0), Seq())))
            // println(substituteFormulaInFormula(fSubstituted, 
            // Map[Formula, Formula]() + (AtomicFormula(AtomicSymbol(map(tsNames(acc._2)).toString(), 0), Seq()) -> AtomicFormula(new_var, Seq()))))
            // println(s"Aux steps = ")
            // aux_steps.map(x => println(x))
            // println(s"acc._1 steps = ")
            // acc._1.map(x => println(x))
            // println(s"final_aux = ")
            // final_aux.map(x => println(x))
            // println(s"final_2 = " + final_f2)
            // println(s"final_3 = " + final_f3)

            // println("----------------------")



            (
            (final_f3 +: final_aux).reverse ++ acc._1, 
            acc._2+1, 
            new_acc3, 
            fSubstituted,
            Seq(x)
        )})._1

      // TS1
      // fof(tsStepExpl0, plain, [$phi,$tsStep0] --> [(p(V0) & Ts3)], inference(rightSubstIff, [status(thm), 1, $fof(p(V0) & A), 'A'], [i0])).
      
      
      // fof(tsStepExpl1, plain, [$phi,$tsStep0,(Ts1(V0) <=> (p(V0) & Ts3))] --> [Ts1(V0)], inference(rightSubstIff, [status(thm), 2, $fof(A), 'A'], [tsStepExpl0])).
      
      // TS0
      // fof(4, plain, [$phi,$tsStep0,$tsStep1] --> [Ts1(V0)], inference(leftForall, [status(thm), 2, $fot(V0)], [tsStepExpl1])).

        final_res(1) +: final_res(0) +: final_res(2) +: Seq()
  }
  
  // Generate Tseitin Step
  def generateTseitinStep(f: Formula, context: Seq[Formula], parent: Seq[LVL2ProofStep], scproof: SCProof[?]): (SCProof[?], String) = {

    val clausalSteps = f.asInstanceOf[ConnectorFormula].args.foldLeft((Seq[LVL2ProofStep](), 0))((acc, x) => {
      (Clausify(acc._2.toString(),Sequent(phi +: context, Seq(x)), "") +: acc._1, acc._2+1)
    })._1 

    var last_step = "-1"

    val new_steps = scproof.steps.filter(x => {
      if !(areAlphaEquivalent(x.bot.right(0), parent(0).bot.right(0)))
      then true 
      else {last_step = x.name; false}
      }
      ).flatMap(x => {
      x match {
        case Axiom(name: String, bot: Sequent) => {
          val flat_formula = toFlatternOr(bot.right(0))
          val originalStep = clausalSteps.filter(x => areAlphaEquivalent(x.bot.right(0), flat_formula))
          if (originalStep.size > 0) 
            then {
              if flat_formula.isInstanceOf[ConnectorFormula]
                then Some(Clausify(name, Sequent(Seq(), flat_formula.asInstanceOf[ConnectorFormula].args), ""))
                else Some(Clausify(name, Sequent(Seq(), Seq(flat_formula)), ""))
            }
            else None
        }
        case _ => Some(x)
      }})
  
    if new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then (LVL1Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName), last_step)
    if new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then (LVL2Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName), last_step)
    else throw new Exception("Some proof steps could not be unrenamed")
  }

  // generate axioms to give to p9, writ them on a file, launch p9 on the file, retrieve the result as a list of proof steps
  def toP9(f: Formula): SCProof[?] = {
    val path = Paths.get("../proofs/p9/p9.p")

    // Create the file if it doesn't exist
    if (!Files.exists(path)) {
      Files.createFile(path)
    }

    // Write some content into the file
    val content = f.asInstanceOf[ConnectorFormula].args.foldLeft(("", 0))((acc, x) => {
      (acc._1 + s"fof(${acc._2}, axiom, ${x.toString()}).\n\n", acc._2+1)
    })._1

    Files.write(path, content.getBytes(StandardCharsets.UTF_8))

    println(s"Content written to: ${path.toAbsolutePath}")

    // Command to execute with shell
    val command = "sh -c \"./../p9-sc-tptp/tptp_to_ladr < ../proofs/p9/p9.p | ./../p9-sc-tptp/prover9 > ../proofs/p9/p9.in && ./../p9-sc-tptp/prooftrans ivy -f ../proofs/p9/p9.in > ../proofs/p9/p9.out\""

    // Run the command and redirect both stdout and stderr to /dev/null
    val exitCode = command.!

    val filePath = "../proofs/p9/p9.out"
    val fileContent = Source.fromFile(filePath).getLines().mkString("\n")

    // Debug: Check the positions of the start and end markers
    val startPos = fileContent.indexOf(";; BEGINNING OF PROOF OBJECT")
    val endPos = fileContent.indexOf(";; END OF PROOF OBJECT")

    // Extract content between the markers
    val proofContent = fileContent.substring(startPos + ";; BEGINNING OF PROOF OBJECT".length, endPos).trim

    // Print the extracted proof content
    // println("Extracted Proof Content:")
    // println(proofContent)

    val path2 = Paths.get("../proofs/p9/p9_proof.p")

    // Create the file if it doesn't exist
    if (!Files.exists(path2)) {
      Files.createFile(path2)
    }

    Files.write(path2, proofContent.getBytes(StandardCharsets.UTF_8))

    Parser.reconstructProof(new File("../proofs/p9/p9_proof.p"))
  }

  def updateId(proof: Seq[LVL2ProofStep], s: String): Seq[LVL2ProofStep] = {

    def updateIdStep(step: LVL2ProofStep, s: String): LVL2ProofStep = {
      step match {
        case Axiom(name: String, bot: Sequent) => Axiom(s, bot)
        case Hyp(name: String, bot: Sequent, i: Int) => Hyp(s, bot, i)
        case LeftFalse(name: String, bot: Sequent) =>  LeftFalse(s, bot)
        case LeftWeaken(name: String, bot: Sequent, i: Int, t1: String) => LeftWeaken(s, bot, i, t1)
        case LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) => LeftWeakenRes(s, bot, i, t1)
        case RightWeaken(name: String, bot: Sequent, i: Int, t1: String) => RightWeaken(s, bot, i, t1)
        case ElimIffRefl(name: String, bot: Sequent, i: Int, t1: String) => ElimIffRefl(s, bot, i, t1)
        case Cut(name: String, bot: Sequent, i: Int, j: Int, t1: String, t2: String) => Cut(s, bot, i, j, t1, t2)
        case LeftAnd(name: String, bot: Sequent, i: Int, t1: String) => LeftAnd(s, bot, i, t1)
        case LeftOr(name: String, bot: Sequent, i: Int, t1: String, t2: String) =>  LeftOr(s, bot, i, t1, t2)
        case LeftImplies(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImplies(s, bot, i, t1, t2)
        case LeftIff(name: String, bot: Sequent, i: Int, t1: String) => LeftIff(s, bot, i, t1)
        case LeftNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNot(s, bot, i, t1)
        case LeftExists(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftExists(s, bot, i, y, t1)
        case LeftForall(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftForall(s, bot, i, t, t1)
        case RightAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightAnd(s, bot, i, t1, t2)
        case RightOr(name: String, bot: Sequent, i: Int, t1: String) => RightOr(s, bot, i, t1)
        case RightImplies(name: String, bot: Sequent, i: Int, t1: String) => RightImplies(s, bot, i, t1)
        case RightIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightIff(s, bot, i, t1, t2)
        case RightNot(name: String, bot: Sequent, i: Int, t1: String) => RightNot(s, bot, i, t1)
        case RightExists(name: String, bot: Sequent, i: Int, t: Term, t1: String) => RightExists(s, bot, i, t, t1)
        case RightForall(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => RightForall(s, bot, i, y, t1)
        case RightRefl(name: String, bot: Sequent, i: Int) => RightRefl(s, bot, i)
        case LeftSubst(name: String, bot: Sequent, i: Int, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(s, bot, i,  p, x, t1)
        case RightSubst(name: String, bot: Sequent, i: Int, p: Formula, x: VariableSymbol, t1: String) => RightSubst(s, bot, i,  p, x, t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(s, bot, i,  r, a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, r: Formula, a: AtomicSymbol, t1: String) => RightSubstIff(s, bot, i,  r, a, t1) // TODO : check that
        case InstFun(name: String, bot: Sequent, f: FunctionSymbol, t: (Term, Seq[VariableSymbol]), t1: String) => InstFun(s, bot, f, t, t1)
        case InstPred(name: String, bot: Sequent, p: AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), t1: String) => InstPred(s, bot, p, phi, t1)
        case LeftHyp(name: String, bot: Sequent, i: Int, j: Int) =>  LeftHyp(s, bot, i, j)
        case LeftImp2(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImp2(s, bot, i, t1, t2)
        case LeftNotAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotAnd(s, bot, i, t1, t2)
        case LeftNotOr(name: String, bot: Sequent, i: Int, t1: String) => LeftNotOr(s, bot, i, t1)
        case LeftNotImp(name: String, bot: Sequent, i: Int, t1: String) => LeftNotImp(s, bot, i, t1)
        case LeftNotIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotIff(s, bot, i, t1, t2)
        case LeftNotNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNotNot(s, bot, i, t1)
        case LeftNotEx(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftNotEx(s, bot, i, t, t1)
        case LeftNotAll(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftNotAll(s, bot, i, y, t1)
        case RightSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => RightSubstMulti(s, bot, is: List[Int],  p, xs, t1)
        case LeftSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => LeftSubstMulti(s, bot, is: List[Int],  p, xs, t1)
        case Congruence(name: String, bot: Sequent) => Congruence(s, bot)
        case Res(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) => Res(s, bot, i1, i2, t1, t2)
        case NegatedConjecture(name: String, bot: Sequent, t1: String) => NegatedConjecture(s, bot, t1)
        case Clausify(name: String, bot: Sequent, t1: String) => Clausify(s, bot, t1)
        case NNF(name: String, bot: Sequent, i:Int, j: Int, t1: String) => NNF(s, bot, i, j, t1)
        case Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) =>  Instantiate_L(s, bot, i, x, t, parent)
        case InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) => InstantiateMult(s, bot, i, terms, parent)
        case Prenex(name: String, bot: Sequent, t1: String) => Prenex(s, bot, t1)
        case Let(name: String, bot: Sequent) => Let(s, bot)
        case TseitinStep(name: String, bot: Sequent, t1: String) => TseitinStep(s, bot, t1)
        case _ => throw Exception("Proof step not found")
      }
    }

    updateIdStep(proof(0), s) +: proof.drop(1)
  }

  // -----------------------------------------------------
  // ------------------ Post Processing ------------------
  //------------------------------------------------------

  // Retrieve the original name of variables


  def UnRenameVariablesInTerm(t: sctptp.FOL.Term, reverseMap: Map[VariableSymbol, VariableSymbol] ) : sctptp.FOL.Term = {
    // println(reverseMap)
    // println(t)
    t match
      case FunctionTerm(label: VariableSymbol, _) if (reverseMap contains label) => Variable(reverseMap(label))
      case FunctionTerm(label, args) => FunctionTerm(label, args.map(UnRenameVariablesInTerm(_, reverseMap)))
      case EpsilonTerm(bound, inner) => {
            if (reverseMap contains bound)
                then ??? //EpsilonTerm(reverseMap(bound), UnRenameVariablesAux(substituteVariablesInFormula(inner, /*accT + */Map.empty +(bound -> Variable(reverseMap(bound))))))
                else ??? //EpsilonTerm(bound, UnRenameVariablesAux(inner))
            }
  }


  def UnRenameVariables(f: sctptp.FOL.Formula,  reverseMap: Map[VariableSymbol, VariableSymbol]): sctptp.FOL.Formula = {
    def UnRenameVariablesAux(f2: sctptp.FOL.Formula): sctptp.FOL.Formula = {
      f2 match 
        case AtomicFormula(label, args) => AtomicFormula(label, args.map(UnRenameVariablesInTerm(_, reverseMap)))
        case BinderFormula(label, bound, inner) => {
            if (reverseMap contains bound)
                then BinderFormula(label, bound, UnRenameVariablesAux(substituteVariablesInFormula(inner, Map[VariableSymbol, Term](bound -> Variable(reverseMap(bound))))))
                else BinderFormula(label, bound, UnRenameVariablesAux(inner))
            }
        case ConnectorFormula(label, args) => ConnectorFormula(label, args.map(UnRenameVariablesAux(_)))
    }

    UnRenameVariablesAux(f)
  }

  // Input : a formula f and a map an generate the let that connect the variable to the formula
  def unrenameProof(scproof: SCProof[?], mapVar: Map[VariableSymbol, VariableSymbol]): SCProof[?] = {

    def unrenameListForm(seq: Seq[Formula]): Seq[Formula] = {
      seq.foldLeft(Seq[Formula]())((acc, e) => {acc :+ UnRenameVariables(e, mapVar)})
    }

    def unrenameSequent(seq: Sequent): Sequent = {
      Sequent(unrenameListForm(seq.left), unrenameListForm(seq.right))
    }
    
    val new_steps = scproof.steps.map(x => {
      x match {
        case Axiom(name: String, bot: Sequent) => Axiom(name, unrenameSequent(bot))
        case Hyp(name: String, bot: Sequent, i: Int) => Hyp(name, unrenameSequent(bot), i)
        case LeftWeaken(name: String, bot: Sequent, i: Int, t1: String) => LeftWeaken(name, unrenameSequent(bot), i, t1)
        case LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) => LeftWeakenRes(name, unrenameSequent(bot), i, t1)
        case RightWeaken(name: String, bot: Sequent, i: Int, t1: String) => RightWeaken(name, unrenameSequent(bot), i, t1)
        case Cut(name: String, bot: Sequent, i: Int, j: Int, t1: String, t2: String) => Cut(name, unrenameSequent(bot), i, j, t1, t2)
        case LeftAnd(name: String, bot: Sequent, i: Int, t1: String) => LeftAnd(name, unrenameSequent(bot), i, t1)
        case LeftOr(name: String, bot: Sequent, i: Int, t1: String, t2: String) =>  LeftOr(name, unrenameSequent(bot), i, t1, t2)
        case LeftImplies(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImplies(name, unrenameSequent(bot), i, t1, t2)
        case LeftIff(name: String, bot: Sequent, i: Int, t1: String) => LeftIff(name, unrenameSequent(bot), i, t1)
        case LeftNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNot(name, unrenameSequent(bot), i, t1)
        case LeftExists(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftExists(name, unrenameSequent(bot), i, y, t1)
        case LeftForall(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftForall(name, unrenameSequent(bot), i, t, t1)
        case RightAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightAnd(name, unrenameSequent(bot), i, t1, t2)
        case RightOr(name: String, bot: Sequent, i: Int, t1: String) => RightOr(name, unrenameSequent(bot), i, t1)
        case RightImplies(name: String, bot: Sequent, i: Int, t1: String) => RightImplies(name, unrenameSequent(bot), i, t1)
        case RightIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightIff(name, unrenameSequent(bot), i, t1, t2)
        case RightNot(name: String, bot: Sequent, i: Int, t1: String) => RightNot(name, unrenameSequent(bot), i, t1)
        case RightExists(name: String, bot: Sequent, i: Int, t: Term, t1: String) => RightExists(name, unrenameSequent(bot), i, t, t1)
        case RightForall(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => RightForall(name, unrenameSequent(bot), i, y, t1)
        case RightRefl(name: String, bot: Sequent, i: Int) => RightRefl(name, unrenameSequent(bot), i)
        case LeftSubst(name: String, bot: Sequent, i: Int, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(name, unrenameSequent(bot), i, UnRenameVariables(p, mapVar), (if (mapVar contains x) then mapVar(x) else x), t1)
        case RightSubst(name: String, bot: Sequent, i: Int, p: Formula, x: VariableSymbol, t1: String) => RightSubst(name, unrenameSequent(bot), i, UnRenameVariables(p, mapVar), (if (mapVar contains x) then mapVar(x) else x), t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(name, unrenameSequent(bot), i, UnRenameVariables(r, mapVar), a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, r: Formula, a: AtomicSymbol, t1: String) => RightSubstIff(name, unrenameSequent(bot), i, UnRenameVariables(r, mapVar), a, t1) // TODO : check that
        case InstFun(name: String, bot: Sequent, f: FunctionSymbol, t: (Term, Seq[VariableSymbol]), t1: String) => InstFun(name, unrenameSequent(bot), f, (UnRenameVariablesInTerm(t._1, mapVar), t._2.map(x => (if (mapVar contains x) then mapVar(x) else x))), t1)
        case InstPred(name: String, bot: Sequent, p: AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), t1: String) => InstPred(name, unrenameSequent(bot), p, (UnRenameVariables(phi._1, mapVar), phi._2.map(x => (if (mapVar contains x) then mapVar(x) else x))), t1)
        case LeftHyp(name: String, bot: Sequent, i: Int, j: Int) =>  LeftHyp(name, unrenameSequent(bot), i, j)
        case LeftImp2(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImp2(name, unrenameSequent(bot), i, t1, t2)
        case LeftNotAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotAnd(name, unrenameSequent(bot), i, t1, t2)
        case LeftNotOr(name: String, bot: Sequent, i: Int, t1: String) => LeftNotOr(name, unrenameSequent(bot), i, t1)
        case LeftNotImp(name: String, bot: Sequent, i: Int, t1: String) => LeftNotImp(name, unrenameSequent(bot), i, t1)
        case LeftNotIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotIff(name, unrenameSequent(bot), i, t1, t2)
        case LeftNotNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNotNot(name, unrenameSequent(bot), i, t1)
        case LeftNotEx(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftNotEx(name, unrenameSequent(bot), i, t, t1)
        case LeftNotAll(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftNotAll(name, unrenameSequent(bot), i, y, t1)
        case RightSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => RightSubstMulti(name, unrenameSequent(bot), is: List[Int], UnRenameVariables(p, mapVar), xs.map(x => (if (mapVar contains x) then mapVar(x) else x)), t1)
        case LeftSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => LeftSubstMulti(name, unrenameSequent(bot), is: List[Int], UnRenameVariables(p, mapVar), xs.map(x => (if (mapVar contains x) then mapVar(x) else x)), t1)
        case Congruence(name: String, bot: Sequent) => Congruence(name, unrenameSequent(bot))
        case Res(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) => Res(name, unrenameSequent(bot), i1, i2, t1, t2)
        case NegatedConjecture(name: String, bot: Sequent, t1: String) => NegatedConjecture(name, unrenameSequent(bot), t1)
        case Clausify(name: String, bot: Sequent, t1: String) => Clausify(name, unrenameSequent(bot), t1)
        case NNF(name: String, bot: Sequent, i:Int, j: Int, t1: String) => NNF(name, unrenameSequent(bot), i, j, t1)
        case Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) =>  Instantiate_L(name, unrenameSequent(bot), i, (if (mapVar contains x) then mapVar(x) else x), UnRenameVariablesInTerm(t, mapVar), parent)
        case InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) => InstantiateMult(name, unrenameSequent(bot), i: Int, terms.map((x, y) => { ((if (mapVar contains x) then mapVar(x) else x), UnRenameVariablesInTerm(y, mapVar))}), parent)
        case _ => throw Exception("Proof step not found")
      }

    })

    if new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then LVL1Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName)
    if new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then LVL2Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName)
    else throw new Exception("Some proof steps could not be unrenamed")

    
  }

    // Input : a formula f and a map an generate the let that connect the variable to the formula
  def renameTseitinConstant(scproof: SCProof[?]): SCProof[?] = {



    val mapTseitinConstAux = tseitinVarTerm.flatMap((k, v) => {
      k match 
        case AtomicFormula(label, args) if (label.arity == 0) => Some(label)
        case _ => None
    })

    val mapTseitinConst: Map[AtomicSymbol, AtomicFormula] = mapTseitinConstAux.foldLeft(Map[AtomicSymbol, AtomicFormula]())((acc, x) => (acc + (x -> AtomicFormula(AtomicSymbol(x.id.name.capitalize, 0), Seq()))))

    def renameTseitinConstantForm(seq: Seq[Formula]): Seq[Formula] = {
      seq.foldLeft(Seq[Formula]())((acc, e) => {acc :+ substituteAtomicsInFormula(e, mapTseitinConst)})
    }

    def renameTseitinConstantSequent(seq: Sequent): Sequent = {
      Sequent(renameTseitinConstantForm(seq.left), renameTseitinConstantForm(seq.right))
    }
    
    val new_steps = scproof.steps.map(x => {
      x match {
        case Axiom(name: String, bot: Sequent) => Axiom(name, renameTseitinConstantSequent(bot))
        case Hyp(name: String, bot: Sequent, i: Int) => Hyp(name, renameTseitinConstantSequent(bot), i)
        case LeftFalse(name: String, bot: Sequent) =>  LeftFalse(name, renameTseitinConstantSequent(bot))
        case LeftWeaken(name: String, bot: Sequent, i: Int, t1: String) => LeftWeaken(name, renameTseitinConstantSequent(bot), i, t1)
        case LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) => LeftWeakenRes(name, renameTseitinConstantSequent(bot), i, t1)
        case RightWeaken(name: String, bot: Sequent, i: Int, t1: String) => RightWeaken(name, renameTseitinConstantSequent(bot), i, t1)
        case ElimIffRefl(name: String, bot: Sequent, i: Int, t1: String) => ElimIffRefl(name, renameTseitinConstantSequent(bot), i, t1)
        case Cut(name: String, bot: Sequent, i: Int, j: Int, t1: String, t2: String) => Cut(name, renameTseitinConstantSequent(bot), i, j, t1, t2)
        case LeftAnd(name: String, bot: Sequent, i: Int, t1: String) => LeftAnd(name, renameTseitinConstantSequent(bot), i, t1)
        case LeftOr(name: String, bot: Sequent, i: Int, t1: String, t2: String) =>  LeftOr(name, renameTseitinConstantSequent(bot), i, t1, t2)
        case LeftImplies(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImplies(name, renameTseitinConstantSequent(bot), i, t1, t2)
        case LeftIff(name: String, bot: Sequent, i: Int, t1: String) => LeftIff(name, renameTseitinConstantSequent(bot), i, t1)
        case LeftNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNot(name, renameTseitinConstantSequent(bot), i, t1)
        case LeftExists(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftExists(name, renameTseitinConstantSequent(bot), i, y, t1)
        case LeftForall(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftForall(name, renameTseitinConstantSequent(bot), i, t, t1)
        case RightAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightAnd(name, renameTseitinConstantSequent(bot), i, t1, t2)
        case RightOr(name: String, bot: Sequent, i: Int, t1: String) => RightOr(name, renameTseitinConstantSequent(bot), i, t1)
        case RightImplies(name: String, bot: Sequent, i: Int, t1: String) => RightImplies(name, renameTseitinConstantSequent(bot), i, t1)
        case RightIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightIff(name, renameTseitinConstantSequent(bot), i, t1, t2)
        case RightNot(name: String, bot: Sequent, i: Int, t1: String) => RightNot(name, renameTseitinConstantSequent(bot), i, t1)
        case RightExists(name: String, bot: Sequent, i: Int, t: Term, t1: String) => RightExists(name, renameTseitinConstantSequent(bot), i, t, t1)
        case RightForall(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => RightForall(name, renameTseitinConstantSequent(bot), i, y, t1)
        case InstForall(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => InstForall(name, renameTseitinConstantSequent(bot), i, y, t1)
        case RightRefl(name: String, bot: Sequent, i: Int) => RightRefl(name, renameTseitinConstantSequent(bot), i)
        case LeftSubst(name: String, bot: Sequent, i: Int, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(name, renameTseitinConstantSequent(bot), i,  substituteAtomicsInFormula(p, mapTseitinConst), x, t1)
        case RightSubst(name: String, bot: Sequent, i: Int, p: Formula, x: VariableSymbol, t1: String) => RightSubst(name, renameTseitinConstantSequent(bot), i,  substituteAtomicsInFormula(p, mapTseitinConst), x, t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(name, renameTseitinConstantSequent(bot), i,  substituteAtomicsInFormula(r, mapTseitinConst),  if (mapTseitinConst contains a) then mapTseitinConst(a).label else a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, r: Formula, a: AtomicSymbol, t1: String) => 
          RightSubstIff(name, renameTseitinConstantSequent(bot), i,  substituteAtomicsInFormula(r, mapTseitinConst), 
          {
          val res = mapTseitinConst.filter(x2 => { a.id.name == x2._1.id.name})
          if res.size > 0 
          then res.head._2.label
          else a 
          },          
          t1) // TODO : check that
        case InstFun(name: String, bot: Sequent, f: FunctionSymbol, t: (Term, Seq[VariableSymbol]), t1: String) => InstFun(name, renameTseitinConstantSequent(bot), f, t, t1)
        case InstPred(name: String, bot: Sequent, p: AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), t1: String) => InstPred(name, renameTseitinConstantSequent(bot), { if (mapTseitinConst contains p) then mapTseitinConst(p).label else p}, 
        (substituteAtomicsInFormula(phi._1, mapTseitinConst), phi._2.map(x => {
          val res = mapTseitinConst.filter(x2 => { x.name.name == x2._1.id.name})
          if res.size > 0 
          then VariableSymbol(res.head._2.label.id)
          else x 
          })), t1)
        case LeftHyp(name: String, bot: Sequent, i: Int, j: Int) =>  LeftHyp(name, renameTseitinConstantSequent(bot), i, j)
        case LeftImp2(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImp2(name, renameTseitinConstantSequent(bot), i, t1, t2)
        case LeftNotAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotAnd(name, renameTseitinConstantSequent(bot), i, t1, t2)
        case LeftNotOr(name: String, bot: Sequent, i: Int, t1: String) => LeftNotOr(name, renameTseitinConstantSequent(bot), i, t1)
        case LeftNotImp(name: String, bot: Sequent, i: Int, t1: String) => LeftNotImp(name, renameTseitinConstantSequent(bot), i, t1)
        case LeftNotIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotIff(name, renameTseitinConstantSequent(bot), i, t1, t2)
        case LeftNotNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNotNot(name, renameTseitinConstantSequent(bot), i, t1)
        case LeftNotEx(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftNotEx(name, renameTseitinConstantSequent(bot), i, t, t1)
        case LeftNotAll(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftNotAll(name, renameTseitinConstantSequent(bot), i, y, t1)
        case RightSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => RightSubstMulti(name, renameTseitinConstantSequent(bot), is: List[Int],  substituteAtomicsInFormula(p, mapTseitinConst), xs, t1)
        case LeftSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => LeftSubstMulti(name, renameTseitinConstantSequent(bot), is: List[Int],  substituteAtomicsInFormula(p, mapTseitinConst), xs, t1)
        case Congruence(name: String, bot: Sequent) => Congruence(name, renameTseitinConstantSequent(bot))
        case Res(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) => Res(name, renameTseitinConstantSequent(bot), i1, i2, t1, t2)
        case NegatedConjecture(name: String, bot: Sequent, t1: String) => NegatedConjecture(name, renameTseitinConstantSequent(bot), t1)
        case Clausify(name: String, bot: Sequent, t1: String) => Clausify(name, renameTseitinConstantSequent(bot), t1)
        case NNF(name: String, bot: Sequent, i:Int, j: Int, t1: String) => NNF(name, renameTseitinConstantSequent(bot), i, j, t1)
        case Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) =>  Instantiate_L(name, renameTseitinConstantSequent(bot), i, x, t, parent)
        case InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) => InstantiateMult(name, renameTseitinConstantSequent(bot), i, terms, parent)
        case Prenex(name: String, bot: Sequent, t1: String) => Prenex(name, renameTseitinConstantSequent(bot), t1)
        case Let(name: String, bot: Sequent) => Let(name, renameTseitinConstantSequent(bot))
        case TseitinStep(name: String, bot: Sequent, t1: String) => TseitinStep(name, renameTseitinConstantSequent(bot), t1)
        case _ => throw Exception("Proof step not found")
      }

    })

    if new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then LVL1Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName)
    if new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then LVL2Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName)
    else throw new Exception("Some proof steps could not be unrenamed")

    
  }

  def modifyOrSteps(scproof: SCProof[?]): SCProof[?] = {

    def modifyOrStepsForm(f: Formula): Seq[Formula] = {
      f match {
        case ConnectorFormula(label, args) => 
            label match { 
              case Or => toFlatternOr(f).asInstanceOf[ConnectorFormula].args
              case _ => Seq(f)
            }
        case _ => Seq(f)
      }
    }

    def modifyOrStepsFormList(seq: Seq[Formula]): Seq[Formula] = {
      seq.foldLeft(Seq[Formula]())((acc, e) => {acc ++ modifyOrStepsForm(e)})
    }

    def modifyOrStepsSequent(seq: Sequent): Sequent = {
      Sequent(modifyOrStepsFormList(seq.left), modifyOrStepsFormList(seq.right))
    }

    val new_steps = scproof.steps.map(x => {
      x match {
        case Axiom(name: String, bot: Sequent) => Axiom(name, modifyOrStepsSequent(bot))
        case Hyp(name: String, bot: Sequent, i: Int) => Hyp(name, modifyOrStepsSequent(bot), i)
        case LeftFalse(name: String, bot: Sequent) =>  LeftFalse(name, modifyOrStepsSequent(bot))
        case LeftWeaken(name: String, bot: Sequent, i: Int, t1: String) => LeftWeaken(name, modifyOrStepsSequent(bot), i, t1)
        case LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) => LeftWeakenRes(name, modifyOrStepsSequent(bot), i, t1)
        case RightWeaken(name: String, bot: Sequent, i: Int, t1: String) => RightWeaken(name, modifyOrStepsSequent(bot), i, t1)
        case ElimIffRefl(name: String, bot: Sequent, i: Int, t1: String) => ElimIffRefl(name, modifyOrStepsSequent(bot), i, t1)
        case Cut(name: String, bot: Sequent, i: Int, j: Int, t1: String, t2: String) => Cut(name, modifyOrStepsSequent(bot), i, j, t1, t2)
        case LeftAnd(name: String, bot: Sequent, i: Int, t1: String) => LeftAnd(name, modifyOrStepsSequent(bot), i, t1)
        case LeftOr(name: String, bot: Sequent, i: Int, t1: String, t2: String) =>  LeftOr(name, modifyOrStepsSequent(bot), i, t1, t2)
        case LeftImplies(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImplies(name, modifyOrStepsSequent(bot), i, t1, t2)
        case LeftIff(name: String, bot: Sequent, i: Int, t1: String) => LeftIff(name, modifyOrStepsSequent(bot), i, t1)
        case LeftNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNot(name, modifyOrStepsSequent(bot), i, t1)
        case LeftExists(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftExists(name, modifyOrStepsSequent(bot), i, y, t1)
        case LeftForall(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftForall(name, modifyOrStepsSequent(bot), i, t, t1)
        case RightAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightAnd(name, modifyOrStepsSequent(bot), i, t1, t2)
        case RightOr(name: String, bot: Sequent, i: Int, t1: String) => RightOr(name, modifyOrStepsSequent(bot), i, t1)
        case RightImplies(name: String, bot: Sequent, i: Int, t1: String) => RightImplies(name, modifyOrStepsSequent(bot), i, t1)
        case RightIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightIff(name, modifyOrStepsSequent(bot), i, t1, t2)
        case RightNot(name: String, bot: Sequent, i: Int, t1: String) => RightNot(name, modifyOrStepsSequent(bot), i, t1)
        case RightExists(name: String, bot: Sequent, i: Int, t: Term, t1: String) => RightExists(name, modifyOrStepsSequent(bot), i, t, t1)
        case RightForall(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => RightForall(name, modifyOrStepsSequent(bot), i, y, t1)
        case RightRefl(name: String, bot: Sequent, i: Int) => RightRefl(name, modifyOrStepsSequent(bot), i)
        case LeftSubst(name: String, bot: Sequent, i: Int, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(name, modifyOrStepsSequent(bot), i,  p, x, t1)
        case RightSubst(name: String, bot: Sequent, i: Int, p: Formula, x: VariableSymbol, t1: String) => RightSubst(name, modifyOrStepsSequent(bot), i,  p, x, t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(name, modifyOrStepsSequent(bot), i,  r, a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, r: Formula, a: AtomicSymbol, t1: String) => RightSubstIff(name, modifyOrStepsSequent(bot), i,  r, a, t1) // TODO : check that
        case InstFun(name: String, bot: Sequent, f: FunctionSymbol, t: (Term, Seq[VariableSymbol]), t1: String) => InstFun(name, modifyOrStepsSequent(bot), f, t, t1)
        case InstPred(name: String, bot: Sequent, p: AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), t1: String) => InstPred(name, modifyOrStepsSequent(bot), p, phi, t1)
        case LeftHyp(name: String, bot: Sequent, i: Int, j: Int) =>  LeftHyp(name, modifyOrStepsSequent(bot), i, j)
        case LeftImp2(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImp2(name, modifyOrStepsSequent(bot), i, t1, t2)
        case LeftNotAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotAnd(name, modifyOrStepsSequent(bot), i, t1, t2)
        case LeftNotOr(name: String, bot: Sequent, i: Int, t1: String) => LeftNotOr(name, modifyOrStepsSequent(bot), i, t1)
        case LeftNotImp(name: String, bot: Sequent, i: Int, t1: String) => LeftNotImp(name, modifyOrStepsSequent(bot), i, t1)
        case LeftNotIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotIff(name, modifyOrStepsSequent(bot), i, t1, t2)
        case LeftNotNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNotNot(name, modifyOrStepsSequent(bot), i, t1)
        case LeftNotEx(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftNotEx(name, modifyOrStepsSequent(bot), i, t, t1)
        case LeftNotAll(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftNotAll(name, modifyOrStepsSequent(bot), i, y, t1)
        case RightSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => RightSubstMulti(name, modifyOrStepsSequent(bot), is: List[Int],  p, xs, t1)
        case LeftSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => LeftSubstMulti(name, modifyOrStepsSequent(bot), is: List[Int],  p, xs, t1)
        case Congruence(name: String, bot: Sequent) => Congruence(name, modifyOrStepsSequent(bot))
        case Res(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) => Res(name, modifyOrStepsSequent(bot), i1, i2, t1, t2)
        case NegatedConjecture(name: String, bot: Sequent, t1: String) => NegatedConjecture(name, modifyOrStepsSequent(bot), t1)
        case Clausify(name: String, bot: Sequent, t1: String) => Clausify(name, modifyOrStepsSequent(bot), t1)
        case NNF(name: String, bot: Sequent, i:Int, j: Int, t1: String) => NNF(name, modifyOrStepsSequent(bot), i, j,t1)
        case Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) =>  Instantiate_L(name, modifyOrStepsSequent(bot), i, x, t, parent)
        case InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) => InstantiateMult(name, modifyOrStepsSequent(bot), i, terms, parent)
        case Prenex(name: String, bot: Sequent, t1: String) => Prenex(name, modifyOrStepsSequent(bot), t1)
        case Let(name: String, bot: Sequent) => Let(name, modifyOrStepsSequent(bot))
        case TseitinStep(name: String, bot: Sequent, t1: String) => TseitinStep(name, modifyOrStepsSequent(bot), t1)
        case _ => throw Exception("Proof step not found")
      }
    })

    if new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then LVL1Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName)
    if new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then LVL2Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName)
    else throw new Exception("Some proof steps could not be unrenamed")
  }

  // Add context 
  def addContextProof(scproof: SCProof[?], context: Seq[AtomicFormula]): SCProof[?] = {    
    val new_steps = scproof.steps.map(x => {
      x match {
        case Axiom(name: String, bot: Sequent) => Axiom(name, Sequent(bot.left ++ context, bot.right))
        case Hyp(name: String, bot: Sequent, i: Int) => Hyp(name, Sequent(bot.left ++ context, bot.right), i)
        case LeftFalse(name: String, bot: Sequent) =>  LeftFalse(name, Sequent(bot.left ++ context, bot.right))
        case LeftWeaken(name: String, bot: Sequent, i: Int, t1: String) => LeftWeaken(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) => LeftWeakenRes(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case RightWeaken(name: String, bot: Sequent, i: Int, t1: String) => RightWeaken(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case ElimIffRefl(name: String, bot: Sequent, i: Int, t1: String) => ElimIffRefl(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case Cut(name: String, bot: Sequent, i: Int, j: Int, t1: String, t2: String) => Cut(name, Sequent(bot.left ++ context, bot.right), i, j, t1, t2)
        case LeftAnd(name: String, bot: Sequent, i: Int, t1: String) => LeftAnd(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case LeftOr(name: String, bot: Sequent, i: Int, t1: String, t2: String) =>  LeftOr(name, Sequent(bot.left ++ context, bot.right), i, t1, t2)
        case LeftImplies(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImplies(name, Sequent(bot.left ++ context, bot.right), i, t1, t2)
        case LeftIff(name: String, bot: Sequent, i: Int, t1: String) => LeftIff(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case LeftNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNot(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case LeftExists(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftExists(name, Sequent(bot.left ++ context, bot.right), i, y, t1)
        case LeftForall(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftForall(name, Sequent(bot.left ++ context, bot.right), i, t, t1)
        case RightAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightAnd(name, Sequent(bot.left ++ context, bot.right), i, t1, t2)
        case RightOr(name: String, bot: Sequent, i: Int, t1: String) => RightOr(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case RightImplies(name: String, bot: Sequent, i: Int, t1: String) => RightImplies(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case RightIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightIff(name, Sequent(bot.left ++ context, bot.right), i, t1, t2)
        case RightNot(name: String, bot: Sequent, i: Int, t1: String) => RightNot(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case RightExists(name: String, bot: Sequent, i: Int, t: Term, t1: String) => RightExists(name, Sequent(bot.left ++ context, bot.right), i, t, t1)
        case RightForall(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => RightForall(name, Sequent(bot.left ++ context, bot.right), i, y, t1)
        case RightRefl(name: String, bot: Sequent, i: Int) => RightRefl(name, Sequent(bot.left ++ context, bot.right), i)
        case LeftSubst(name: String, bot: Sequent, i: Int, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(name, Sequent(bot.left ++ context, bot.right), i, p, x, t1)
        case RightSubst(name: String, bot: Sequent, i: Int, p: Formula, x: VariableSymbol, t1: String) => RightSubst(name, Sequent(bot.left ++ context, bot.right), i, p, x, t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(name, Sequent(bot.left ++ context, bot.right), i, r, a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, r: Formula, a: AtomicSymbol, t1: String) => RightSubstIff(name, Sequent(bot.left ++ context, bot.right), i, r, a, t1) // TODO : check that
        case InstFun(name: String, bot: Sequent, f: FunctionSymbol, t: (Term, Seq[VariableSymbol]), t1: String) => InstFun(name, Sequent(bot.left ++ context, bot.right), f, t, t1)
        case InstPred(name: String, bot: Sequent, p: AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), t1: String) => InstPred(name, Sequent(bot.left ++ context, bot.right), p, phi, t1)
        case LeftHyp(name: String, bot: Sequent, i: Int, j: Int) =>  LeftHyp(name, Sequent(bot.left ++ context, bot.right), i, j)
        case LeftImp2(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImp2(name, Sequent(bot.left ++ context, bot.right), i, t1, t2)
        case LeftNotAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotAnd(name, Sequent(bot.left ++ context, bot.right), i, t1, t2)
        case LeftNotOr(name: String, bot: Sequent, i: Int, t1: String) => LeftNotOr(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case LeftNotImp(name: String, bot: Sequent, i: Int, t1: String) => LeftNotImp(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case LeftNotIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotIff(name, Sequent(bot.left ++ context, bot.right), i, t1, t2)
        case LeftNotNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNotNot(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case LeftNotEx(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftNotEx(name, Sequent(bot.left ++ context, bot.right), i, t, t1)
        case LeftNotAll(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftNotAll(name, Sequent(bot.left ++ context, bot.right), i, y, t1)
        case RightSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => RightSubstMulti(name, Sequent(bot.left ++ context, bot.right), is: List[Int], p, xs, t1)
        case LeftSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => LeftSubstMulti(name, Sequent(bot.left ++ context, bot.right), is: List[Int], p, xs, t1)
        case Congruence(name: String, bot: Sequent) => Congruence(name, Sequent(bot.left ++ context, bot.right))
        case Res(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) => Res(name, Sequent(bot.left ++ context, bot.right), i1, i2, t1, t2)
        case NegatedConjecture(name: String, bot: Sequent, t1: String) => NegatedConjecture(name, Sequent(bot.left ++ context, bot.right), t1)
        case Clausify(name: String, bot: Sequent, t1: String) => Clausify(name, Sequent(bot.left ++ context, bot.right), t1)
        case NNF(name: String, bot: Sequent, i:Int, j: Int, t1: String) => NNF(name, Sequent(bot.left ++ context, bot.right), i, j, t1)
        case Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) =>  Instantiate_L(name, Sequent(bot.left ++ context, bot.right), i, x, t, parent)
        case InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) => InstantiateMult(name, Sequent(bot.left ++ context, bot.right), i, terms, parent)
        case _ => throw Exception("Proof step not found")
      }

    })

    if new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then LVL1Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName)
    if new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then LVL2Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName)
    else throw new Exception("Some proof steps could not be unrenamed")

    
  }

  // Remove $false
  def removeFalse(context: Seq[AtomicFormula], lastId: String): Seq[LVL1ProofStep] = {
    Seq(
      LeftFalse("stepFalse0", Sequent(Seq(), Seq())),
      Cut("stepFalse1", Sequent(context, Seq()), 0, 0, lastId , "stepFalse0")
    )
  }

  // Add psi
  def addPsi(context: Seq[AtomicFormula]): Seq[LVL2ProofStep] = {
    Seq(
      Let("psi", Sequent(Seq(psi), Seq(originalFormula))),
      Hyp("addPsi0", Sequent(Seq(psi), Seq(psi)), 0),
      RightNot("addPsi1", Sequent(Seq(), Seq(psi, phi)), 0, "addPsi0"),
      Cut("addPsi2", Sequent(context.drop(1), Seq(psi)), 1, 0, "addPsi1", "stepFalse1")
    )
  }

  def removeTseitin(tsNames: Seq[AtomicFormula], map: Map[AtomicFormula, AtomicFormula]): Seq[LVL2ProofStep] = { 
    // println(tsNames)
    // println("------------")
    // println(map)
    // println("------------")
    // println(tseitinVarTermUp)

    val steps = tsNames.foldLeft((Seq[LVL2ProofStep](), 0, 0))((acc, x) => {
      val new_context = if (acc._3+1 < tsNames.size) then tsNames.reverse.dropRight(acc._3+1) else Seq()
      val parent = if (acc._2 == 0) then "addPsi2" else "removeTseitin"+(acc._2-1) 
      val parent2 = "removeTseitin"+(acc._2) 
      val f = tseitinVarTermUp(map(x))
      val f1 = f.freeVariables.foldLeft(ConnectorFormula(Iff, Seq(map(x), f)).asInstanceOf[Formula])((acc, x) => BinderFormula(Forall, x, acc))
      val f2 = f.freeVariables.foldLeft(ConnectorFormula(Iff, Seq(f, f)).asInstanceOf[Formula])((acc, x) => BinderFormula(Forall, x, acc))
      
      (acc._1 ++ Seq(
        InstPred(
        "removeTseitin"+(acc._2), 
        Sequent(new_context :+ f2, Seq(psi)), 
        map(x).label, 
        (f, f.freeVariables.toSeq), 
        parent
        ),
        ElimIffRefl(
          "removeTseitin"+(acc._2+1), 
          Sequent(new_context, Seq(psi)), 
          new_context.size, 
          parent2
        )), 
        acc._2+2, acc._3+1)
    }
    )

    val last_parent = if (steps._2 == 0) then "addPsi2" else "removeTseitin"+(steps._2-1) 

    steps._1 // :+ ElimIffRefl("removeTseitin"+steps._2, Sequent(Seq(), Seq(phi)), 0, last_parent)

  }

  // replace exists x by #x + the formula it satisfy (let too?)
  // But what about the prover? How will the epsilon term will be managed?
  // Reconstruct at the end --- wrapper?
  def addEpsilonTerms(f: sctptp.FOL.Formula): sctptp.FOL.Formula = ??? 
}