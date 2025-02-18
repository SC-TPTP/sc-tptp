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


class Tseitin {

  val varName = "V"
  val tseitinName = "X"
  var varCpt = 0
  val ladrName = "ladr"
  var ladrCpt = -1
  val NoneFormula = AtomicFormula(AtomicSymbol("None", 0), Seq())

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
  def toNegatedFormula(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, LVL2ProofStep) = {
    val new_f = ConnectorFormula(Neg , Seq(f)) 
    (new_f, NegatedConjecture("nc_step", Sequent(Seq(new_f), Seq()), "0"))
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
    (new_f, NNF("nnf_step", Sequent(Seq(new_f), Seq()), "nc_step"))
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
    (new_f, Prenex("prenex_step", Sequent(Seq(new_f), Seq()) ,"nnf_step"))
  }

  // Instantiate universal quantifiers and returns a map [oldName, newName], the new formula, and has renamed them according to P9 standard
  def toInstantiated(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, Map[VariableSymbol, VariableSymbol]) = {
      def toInstantiatedAux(f2: sctptp.FOL.Formula, accVS: Map[VariableSymbol, VariableSymbol], accT: Map[VariableSymbol, Term]): (sctptp.FOL.Formula, Map[VariableSymbol, VariableSymbol]) = {
        f2 match 
          case BinderFormula(label, bound, inner) => {
            label match
              case Forall => {
                val new_symbol =VariableSymbol(varName+varCpt)
                val new_accVS = accVS + (bound -> new_symbol)
                val new_accT = accT + (bound -> Variable(new_symbol))
                varCpt = varCpt+1
                val (new_f, new_acc_next_steps) = toInstantiatedAux(substituteVariablesInFormula(inner, new_accT), new_accVS, new_accT)
                (new_f, new_acc_next_steps)
              }
              case Exists => {
                
                val new_symbol = VariableSymbol(varName+varCpt)
                val new_accVS = accVS + (bound -> new_symbol)
                val new_accT = accT + (bound -> Variable(new_symbol))
                varCpt = varCpt+1
                val (new_f, new_acc_next_steps) = toInstantiatedAux(substituteVariablesInFormula(inner, new_accT), new_accVS, new_accT)
                (BinderFormula(Exists, new_symbol, new_f), new_acc_next_steps)
              }
          }
          case _ => (f2, accVS)
      }

      toInstantiatedAux(f,  Map[VariableSymbol, VariableSymbol](), Map[VariableSymbol, Term]())

      // sequence of forall left
  }

  // Go through the formula and associate each subformula to a variable
  def createTseitinVariables(f: sctptp.FOL.Formula, acc: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula](), next_index: Int = 0): (Map[sctptp.FOL.Formula, sctptp.FOL.Formula], Int) = {
    // println("Next id = " + next_index)
    // println("acc = " + acc.toString())
    // println("f = " + f.toString())
    // println("Contains : " + acc.contains(f))
    // println("------------------------------")
    f match
      case AtomicFormula(label, args) => {
        if (acc.contains(f)) {
          (acc, next_index)
        } else {
          (acc + (f -> AtomicFormula(AtomicSymbol(Identifier(tseitinName + next_index, next_index), f.freeVariables.size), f.getFreeVariables())), next_index + 1)
        }
      }
      case ConnectorFormula(label, args) => {
        if (acc.contains(f)) {
          args.foldLeft((acc, next_index))((acc2, c) => (createTseitinVariables(c, acc2._1, next_index)._1, acc2._2))
        } else {
          args.foldLeft((acc, next_index))((acc2, c) => {
            ladrCpt = ladrCpt+1
            val (newAcc, newCpt) = createTseitinVariables(c, acc2._1 + (f -> AtomicFormula(AtomicSymbol( (if (f.freeVariables.size == 0) then Identifier(ladrName + ladrCpt, acc2._2) else Identifier(tseitinName + acc2._2, acc2._2) ), f.freeVariables.size), f.getFreeVariables())), acc2._2 + 1)
            (newAcc, newCpt)
          })
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
        case ConnectorFormula(label, args) => acc + (ConnectorFormula(label, args.map(x => map(x))) -> varTseitin)
        case  BinderFormula(label, bound, inner) => acc + (BinderFormula(label, bound, map(inner)) -> varTseitin)
      }
    )
  }

  // Transform a formula in Prenex Negatted Normal form into Tseitin Normal Form with variable stored in tseitinVarTerm
  def toTseitin(f2: sctptp.FOL.Formula): sctptp.FOL.Formula =  {

    def toTseitinAux(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, sctptp.FOL.Formula) = {
    val varT = getTseitinTermVar()(f)
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


  


  // -----------------------------------------------------
  // ------------------ Post Processing ------------------
  //------------------------------------------------------

  // Retrieve the original name of variables


  def UnRenameVariablesInTerm(t: sctptp.FOL.Term, reverseMap: Map[VariableSymbol, VariableSymbol] ) : sctptp.FOL.Term = {
    // println(reverseMap)
    // println(t)
    t match
      case FunctionTerm(label: VariableSymbol, _) if (reverseMap contains label) => FunctionTerm(reverseMap(label), Seq())
      case FunctionTerm(label, args) => FunctionTerm(label, args.map(UnRenameVariablesInTerm(_, reverseMap)))
      case EpsilonTerm(bound, inner) => {
            if (reverseMap contains bound)
                then ??? //EpsilonTerm(reverseMap(bound), UnRenameVariablesAux(substituteVariablesInFormula(inner, /*accT + */Map.empty +(bound -> Variable(reverseMap(bound))))))
                else ??? //EpsilonTerm(bound, UnRenameVariablesAux(inner))
            }
  }


  def UnRenameVariables(f: sctptp.FOL.Formula,  reverseMap: Map[VariableSymbol, VariableSymbol]): sctptp.FOL.Formula = {
    val accT = Map[VariableSymbol, Term]()


    def UnRenameVariablesAux(f2: sctptp.FOL.Formula): sctptp.FOL.Formula = {
      f2 match 
        case AtomicFormula(label, args) => AtomicFormula(label, args.map(UnRenameVariablesInTerm(_, reverseMap)))
        case BinderFormula(label, bound, inner) => {
            if (reverseMap contains bound)
                then BinderFormula(label, reverseMap(bound), UnRenameVariablesAux(substituteVariablesInFormula(inner, accT + (bound -> Variable(reverseMap(bound))))))
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
        case NNF(name: String, bot: Sequent, t1: String) => NNF(name, unrenameSequent(bot), t1)
        case Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) =>  Instantiate_L(name, unrenameSequent(bot), i, (if (mapVar contains x) then mapVar(x) else x), UnRenameVariablesInTerm(t, mapVar), parent)
        case InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) => InstantiateMult(name, unrenameSequent(bot), i: Int, terms.map((x, y) => { ((if (mapVar contains x) then mapVar(x) else x), UnRenameVariablesInTerm(y, mapVar))}), parent)
        case _ => throw Exception("Proof step not found")
      }

    })

    if new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then LVL1Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName)
    if new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then LVL2Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName)
    else throw new Exception("Some proof steps could not be unrenamed")

    
  }


  // add equivalences into the formula
  // Input : a formula f and a map an generate the let that connect he variable to the formula
  def generateLet(f: sctptp.FOL.Formula, sf: sctptp.FOL.Formula): String = {
    s"let(${tseitinTermVar(f)}, ${f})"
  }

  // replace exists x by #x + the formula it satisfy (let too?)
  // But what about the prover? How will the epsilon term will be managed?
  // Reconstruct at the end --- wrapper?
  def addEpsilonTerms(f: sctptp.FOL.Formula): sctptp.FOL.Formula = ??? 

  /** Schéma global de la preuve :
  * Formule f en entrée, fof
  * [x] Mise en NFF
  * [x] Mise sous forme prénexe
  * [x] Renommer variable forall
  * [x] Instanciation forall
  * [x] récupération des variable schématiques
  * [x] Ajout des variable tseitin level 0 (pour avoir une formule en CNF) et de leur traduction en forme étendue (i.e., j'introduis X <=> (a /\ b), et je rajoute dans la formule (¬X ∨ a), (¬X ∨ b), (X ∨ ¬a ∨ ¬b)) -> Faire ça récursivement (si la subformula n'est pas atomic)
  * [x] To flatterm
  * [x] schematique : `X`autour de variables sans param
  * [ ] donner la formule finale P9 (sous forme axiom et sans quantifier universel) (auto)
  * [ ] Récupérer preuve (auto)
  * [x] Derenommer les variables dans la preuve 
  * [ ] pipeline
  * [ ] Ajouter les let
  * [ ] Epslion-termes 
  * [ ] Introduce a routine to elaborate the rule for prenex and nnf
  * [ ] Res -> coq
  * 
  * Annex function 
  * Generation of scheme equivalent rule directly as strings (i.e., fof(axiom, tseq1, a <=>))
  * Generation of ts equivalences itself (\psi <=> ....)
  * return proof step, together with the transformation
  * after the proof by P9: Epsilon rule catcher? Rewrite instances of x into #x (let #x1, #x1 P(x1))
  * 
  * TODO : 
  * Instantiate
  * Tseitin
  * unfold resolution --- leftnot et congruence
  * unfold inst multi
  * neg à droite, pos à gauche + tracking des listes
  * unfold nnf
  * Epsilon
  * Pipeline + executable
  * 
  * Later : 
  * Update gog sans param + status thm
  **/
}
