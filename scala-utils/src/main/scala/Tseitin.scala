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
import java.io.InputStream
import java.nio.file.StandardCopyOption
import java.net.URL
import java.util.Enumeration



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
  val NoneSymbol : AtomicSymbol = AtomicSymbol("None", 0)
  val NoneFormula : Formula = AtomicFormula(NoneSymbol, Seq())
  var phi = NoneFormula
  val psi = AtomicFormula(AtomicSymbol("$psi", 0), Seq())
  var originalFormula = NoneFormula

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
    f2 match {
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
  }

  def reInsertQT(accQT: List[(BinderSymbol, VariableSymbol)], accF2 : sctptp.FOL.Formula) : sctptp.FOL.Formula = {
    accQT.foldLeft(accF2)((acc, x) => {
      x match {
        case (Forall, x2) => sctptp.FOL.BinderFormula(Forall, x2, acc)
        case (Exists, x2) => sctptp.FOL.BinderFormula(Exists, x2, acc)
      }
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
        f2 match {
          case BinderFormula(label, bound, inner) => {
            label match {
              case Forall => {
                val new_symbol = VariableSymbol(varName+varCpt)
                val new_accVS = accVS + (bound -> new_symbol)
                val new_term = Variable(new_symbol)
                val new_accT = accT + (bound -> new_term)
                varCpt = varCpt+1
                val new_form = substituteVariablesInFormula(inner, new_accT)
                val ps = InstForall(instantiateName+cpt, Sequent(Seq(phi), Seq(new_form)), 0, new_symbol, if (cpt > 0) then (s"${instantiateName}${cpt-1}") else "prenex_step")
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
                val ps = RightExists(instantiateName+cpt, Sequent(Seq(phi),Seq(new_form)), 0, new_term, if (cpt > 0) then (s"${instantiateName}${cpt-1}") else "prenex_step")
                val (new_f, new_acc_next_steps, new_accProof) = toInstantiatedAux(new_form, accVS, new_accT, ps +: accProof , cpt+1)
                (new_f, new_acc_next_steps, new_accProof)
              }
            }
          }
          case _ => (f2, accVS, accProof)
        }
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
    f match {
      case AtomicFormula(label, args) => (acc, next_index)
      case ConnectorFormula(label, args) => {
        if (acc.contains(f)) {
          args.foldLeft((acc, next_index))((acc2, c) => (createTseitinVariables(c, acc2._1, next_index)._1, acc2._2))
        } else {
          label match {
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
  }

  // Take tseitinTermVar et return tseitinTermVar updated
  def updateTseitinVariables(map: Map[sctptp.FOL.Formula, sctptp.FOL.Formula]) : Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = {
    map.foldLeft(Map[sctptp.FOL.Formula, sctptp.FOL.Formula]())((acc, x) => {
      val (termForm, varTseitin) = x
      termForm match {
        case AtomicFormula(label, args) => acc + (termForm -> varTseitin)
        case ConnectorFormula(label, args) => acc + (ConnectorFormula(label, args.map(x => if (map contains x) then map(x) else x)) -> varTseitin)
        case  BinderFormula(label, bound, inner) => acc + (BinderFormula(label, bound, map(inner)) -> varTseitin)
      }
    })
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

    // println("---------------------")
    // println("tsName: "+tsNames)
    // println("tsSteps: ")
    // tsSteps.map(x => println(x))
    // println("map: "+map)
    // println("original parent: "+original_parent)
    // println("TseitinVarTermUp :"+tseitinVarTermUp)
    // println("TseitinTermVapUp :"+tseitinTermVarUp)

    tsSteps.foldLeft((Seq[LVL2ProofStep](), 0, s.bot.right(0), 0)){(acc, x) => 
      val currentStep = x
      val index = acc._2
      val cptStep = acc._4
      val currentFormula = acc._3
      val parent = if (acc._2 == 0) then original_parent else tseitinStepNameExpl+(cptStep-1)

      // Compute new Formula
      val currentFormulaSubstituted = substituteFormulaInFormula(
        currentFormula, 
        tseitinTermVarUp
      )
        
      // Compute Scheme
      val new_var = AtomicSymbol(Identifier("A"), 0)
      val formulaScheme = substituteFormulaInFormula(
        currentFormula, 
        tseitinTermVarUp + (tseitinVarTermUp(map(tsNames(acc._2))) -> AtomicFormula(new_var, Seq())) 
      )


      // Compute the last hypothesis
      val lastHyp = 
        if (currentStep.bot.right(0).isInstanceOf[BinderFormula] && (currentStep.bot.right(0).asInstanceOf[BinderFormula].label == Forall))
          then retrieveQT(currentStep.bot.right(0))._2
          else tsNames(index)

      // Annex function to compute the forall chain
      def computeTseitinReplacementStepsAux(hyp: Formula, f: Formula, old_parent: String, cpt: Int) : (Seq[LVL2ProofStep], String, Int) = {
        val hypName = if areAlphaEquivalent(hyp, currentStep.bot.right(0)) 
          then tsNames(index)
          else hyp 

        if (hyp.isInstanceOf[BinderFormula] && hyp.asInstanceOf[BinderFormula].label == Forall)
          then (LeftForall(tseitinStepNameExpl+cpt, Sequent((phi +: tsNames.take(index)) :+ hypName, Seq(f)), index+1, Variable(hyp.asInstanceOf[BinderFormula].bound), tseitinStepNameExpl+(cpt-1)) 
                +: computeTseitinReplacementStepsAux(hyp.asInstanceOf[BinderFormula].inner, f, tseitinStepNameExpl+(cpt-1), index+1)._1, tseitinStepNameExpl+cpt, index+1)
          else (Seq(), old_parent, cpt)
      }



      val finalStep = RightSubstIff(tseitinStepNameExpl+cptStep,
            Sequent( 
            phi +: (tsNames.take(index)) :+ lastHyp, // les formules d'avant, avec un acc
            Seq(currentFormulaSubstituted)), 
            index+1, 
            true,
            formulaScheme,
            new_var, 
            parent)


      val res = computeTseitinReplacementStepsAux(currentStep.bot.right(0), currentFormulaSubstituted, tseitinStepNameExpl+cptStep, cptStep+1)

      val finalList = acc._1 ++ (finalStep +: res._1.reverse)

      // println("#####################################")
      // finalList.map(x => println(x))
      // println("#####################################")

      (finalList, acc._2 + 1, currentFormulaSubstituted, res._3)
    }._1
  }
  
  // Generate Tseitin Step
  def generateTseitinStep(f: Formula, context: Seq[Formula], parent: Seq[LVL2ProofStep], scproof: SCProof[?], map: Map[AtomicFormula, AtomicFormula], tsSteps: Seq[LVL2ProofStep]): (SCProof[?], String) = {

    val new_map = map.map(_.swap)
    val new_tsSteps = tsSteps.foldLeft(Map[String, Formula]())((acc, x) => {
      acc + (x.name -> x.bot.right(0))
    })

    // println(new_map)
    // println(tseitinVarTermUp)
    // println(tseitinTermVarUp)
    // tsSteps.map(x => println(x))
    // new_tsSteps.map(x => println(x))
    // println("------------")

    val clausalSteps = f.asInstanceOf[ConnectorFormula].args.foldLeft((Seq[LVL2ProofStep](), 0))((acc, x) => {
      val first_elem = {
        x match
          case AtomicFormula(label, args) => x
          case ConnectorFormula(label, args) => args(0)
          case BinderFormula(label, bound, inner) => throw Exception("Forall in clausal step")
      } 
      val first_elem_neg = {
        first_elem match
          case AtomicFormula(label, args) => ConnectorFormula(Neg, Seq(first_elem))
          case ConnectorFormula(label, args) => {
            label match
              case Neg => args(0)
              case _ => first_elem
          }
          case BinderFormula(label, bound, inner) => throw Exception("Forall in clausal step")
        
      }
      val associated_step = new_map.toSeq.foldLeft(NoneFormula)((acc, x) => {
        if (areAlphaEquivalent(x._1, first_elem) || areAlphaEquivalent(x._1, first_elem_neg)) 
          then (x._2) 
          else (acc)
      })

      val clausify_step = Clausify(acc._2.toString(),Sequent(phi +: context, Seq(x)), context.indexOf(associated_step)+1, "")
      (clausify_step +: acc._1, acc._2+1)
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
                then Some(Clausify(name, Sequent(phi +: context, flat_formula.asInstanceOf[ConnectorFormula].args), originalStep(0).asInstanceOf[Clausify].i, ""))
                else Some(Clausify(name, Sequent(phi +: context, Seq(flat_formula)), originalStep(0).asInstanceOf[Clausify].i, ""))
            }
            else None
        }
        case _ => Some(x)
      }})

    val new_new_steps = new_steps.foldLeft(Seq[SCProofStep]())((acc, x) => {
      x match
        case Clausify(name, bot, i, t1) => acc ++ {
          // println(x)
          val linked_formula = new_tsSteps(bot.left(i).asInstanceOf[AtomicFormula].label.id.name.drop(1)) 
          if linked_formula.isInstanceOf[BinderFormula]

            then {
              val linked_form_binder = linked_formula.asInstanceOf[BinderFormula]
              val inst_form = linked_form_binder.inner

              // modify x
              val new_x = Clausify("a"+name, Sequent(bot.left.dropRight(1) :+ inst_form, bot.right), i, t1)

              // create forall step
              val forallstep = LeftForall(name, bot, i, Variable(linked_form_binder.bound), "a"+name)

              Seq(new_x, forallstep)
            }
            else Seq(x)
        }
        case _ => acc :+ addContextStep(x, phi +: context)
      
    })

    // val new_new_new_steps = removeContextProof(new_new_steps)
    // println("------------")
    // new_new_steps.map(x => println(x))
  
    if new_new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then (LVL1Proof(new_new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName), last_step)
    if new_new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then (LVL2Proof(new_new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName), last_step)
    else throw new Exception("Some proof steps could not be unrenamed")
  }

  // generate axioms to give to p9, writ them on a file, launch p9 on the file, retrieve the result as a list of proof steps
  def toP9(f: Formula): SCProof[?] = {

    val pathname = "./p9proof"
    val pathdir = Paths.get(pathname)
    if(!(Files.exists(pathdir) && Files.isDirectory(pathdir)))
      Files.createDirectory(pathdir)

    // Create the file if it doesn't exist
    val pathfilepname = pathname+"/p9.p"
    val pathfilep = Paths.get(pathfilepname)
    if (!Files.exists(pathfilep)) {
      Files.createFile(pathfilep)
    }

    // Write some content into the file
    val content = f.asInstanceOf[ConnectorFormula].args.foldLeft(("", 0))((acc, x) => {
      (acc._1 + s"fof(${acc._2}, axiom, ${x.toString()}).\n\n", acc._2+1)
    })._1

    Files.write(pathfilep, content.getBytes(StandardCharsets.UTF_8))

    println(s"Content written to: ${pathfilep.toAbsolutePath}")

    // Executable 




    // val ladr2TPTPPath = Paths.get("classes/tptp_to_ladr"
    // val p9Path =  Paths.get("classes/prover9")
    // val prooftrans =  Paths.get("classes/prooftrans")


    val ladr2TPTPPathName = "tptp_to_ladr"
    val p9PathName =  "prover9"
    val prooftransName =  "prooftrans"

    val ladr2TPTPPath: InputStream = Option(getClass.getClassLoader.getResourceAsStream(ladr2TPTPPathName)) match {
      case Some(stream) => stream
      case None => throw new RuntimeException(s"Executable '$ladr2TPTPPathName' not found in the JAR")
    }

    val p9PathPath: InputStream = Option(getClass.getClassLoader.getResourceAsStream(p9PathName)) match {
      case Some(stream) => stream
      case None => throw new RuntimeException(s"Executable '$p9PathName' not found in the JAR")
    }

    val prooftransPath: InputStream = Option(getClass.getClassLoader.getResourceAsStream(prooftransName)) match {
      case Some(stream) => stream
      case None => throw new RuntimeException(s"Executable '$prooftransName' not found in the JAR")
    }

    // Create the file if it doesn't exist
    val pathname2 = "./tmp/"
    val pathdir2 = Paths.get(pathname2)
    if(!(Files.exists(pathdir2) && Files.isDirectory(pathdir2)))
      Files.createDirectory(pathdir2)

    val ladr2TPTPTempExecutablePath = "/tmp/tptp_to_ladr"
    val p9TempExecutablePath = "/tmp/prouver9"
    val prooftransTempExecutablePath = "/tmp/prooftrans"

    Files.copy(ladr2TPTPPath, Paths.get(ladr2TPTPTempExecutablePath), StandardCopyOption.REPLACE_EXISTING)
    Files.copy(p9PathPath, Paths.get(p9TempExecutablePath), StandardCopyOption.REPLACE_EXISTING)
    Files.copy(prooftransPath, Paths.get(prooftransTempExecutablePath), StandardCopyOption.REPLACE_EXISTING)

    s"sh -c \" chmod +x $ladr2TPTPTempExecutablePath && chmod +x $p9TempExecutablePath && chmod +x $prooftransTempExecutablePath\"".!

    // Launch 

    val pathfileinname = pathname+"/p9.in"
    val pathfileout = pathname+"/p9.out"

    val command = s"sh -c \"${ladr2TPTPTempExecutablePath} < ${pathfilepname} | ${p9TempExecutablePath} > ${pathfileinname} && ${prooftransTempExecutablePath} ivy -f ${pathfileinname} >  ${pathfileout}\""

    try {
      val exitCode = command.!
      if (exitCode == 0) {
        println("Executable ran successfully.")
      } else {
        println(s"Executable failed with exit code: $exitCode")
      }
    } catch {
      case e: Exception => println(s"Error running the executable: ${e.getMessage}")
    }

    val fileContent = Source.fromFile(pathfileout).getLines().mkString("\n")

    // Debug: Check the positions of the start and end markers
    val startPos = fileContent.indexOf(";; BEGINNING OF PROOF OBJECT")
    val endPos = fileContent.indexOf(";; END OF PROOF OBJECT")

    // Extract content between the markers
    val proofContent = fileContent.substring(startPos + ";; BEGINNING OF PROOF OBJECT".length, endPos).trim

    val pathfileoutname = pathname+"/p9proof.p"
    val pathproof = Paths.get(pathfileoutname)

    // Create the file if it doesn't exist
    if (!Files.exists(pathproof)) {
      Files.createFile(pathproof)
    }

    Files.write(pathproof, proofContent.getBytes(StandardCharsets.UTF_8))

    Parser.reconstructProof(new File(pathfileoutname))
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
        case Cut(name: String, bot: Sequent, i: Int, t1: String, t2: String) => Cut(s, bot, i, t1, t2)
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
        case LeftSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(s, bot, i, flip,  p, x, t1)
        case RightSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => RightSubst(s, bot, i, flip,  p, x, t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(s, bot, i, flip,  r, a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => RightSubstIff(s, bot, i, flip,  r, a, t1) // TODO : check that
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
        case Clausify(name: String, bot: Sequent, i: Int, t1: String) => Clausify(s, bot, i, t1)
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
        case Cut(name: String, bot: Sequent, i: Int, t1: String, t2: String) => Cut(name, unrenameSequent(bot), i, t1, t2)
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
        case LeftSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(name, unrenameSequent(bot), i, flip, UnRenameVariables(p, mapVar), (if (mapVar contains x) then mapVar(x) else x), t1)
        case RightSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => RightSubst(name, unrenameSequent(bot), i, flip, UnRenameVariables(p, mapVar), (if (mapVar contains x) then mapVar(x) else x), t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(name, unrenameSequent(bot), i, flip, UnRenameVariables(r, mapVar), a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => RightSubstIff(name, unrenameSequent(bot), i, flip, UnRenameVariables(r, mapVar), a, t1) // TODO : check that
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
        case Clausify(name: String, bot: Sequent, i: Int, t1: String) => Clausify(name, unrenameSequent(bot), i, t1)
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
        case Cut(name: String, bot: Sequent, i: Int, t1: String, t2: String) => Cut(name, renameTseitinConstantSequent(bot), i, t1, t2)
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
        case LeftSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(name, renameTseitinConstantSequent(bot), i, flip,  substituteAtomicsInFormula(p, mapTseitinConst), x, t1)
        case RightSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => RightSubst(name, renameTseitinConstantSequent(bot), i, flip, substituteAtomicsInFormula(p, mapTseitinConst), x, t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(name, renameTseitinConstantSequent(bot), i, flip, substituteAtomicsInFormula(r, mapTseitinConst),  if (mapTseitinConst contains a) then mapTseitinConst(a).label else a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => 
          RightSubstIff(name, renameTseitinConstantSequent(bot), i, flip,  substituteAtomicsInFormula(r, mapTseitinConst), 
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
        case Clausify(name: String, bot: Sequent,  i: Int, t1: String) => Clausify(name, renameTseitinConstantSequent(bot), i, t1)
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
        case Cut(name: String, bot: Sequent, i: Int, t1: String, t2: String) => Cut(name, modifyOrStepsSequent(bot), i, t1, t2)
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
        case LeftSubst(name: String, bot: Sequent, i: Int, flip: Boolean, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(name, modifyOrStepsSequent(bot), i, flip,  p, x, t1)
        case RightSubst(name: String, bot: Sequent, i: Int, flip: Boolean, p: Formula, x: VariableSymbol, t1: String) => RightSubst(name, modifyOrStepsSequent(bot), i, flip,  p, x, t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, flip: Boolean, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(name, modifyOrStepsSequent(bot), i, flip,  r, a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, flip: Boolean, r: Formula, a: AtomicSymbol, t1: String) => RightSubstIff(name, modifyOrStepsSequent(bot), i, flip,  r, a, t1) // TODO : check that
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
        case Clausify(name: String, bot: Sequent, i: Int, t1: String) => Clausify(name, modifyOrStepsSequent(bot), i, t1)
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

  def addContextStep(x: SCProofStep, context: Seq[Formula]): SCProofStep = {
      x match {
        case Axiom(name: String, bot: Sequent) => Axiom(name, Sequent(bot.left ++ context, bot.right))
        case Hyp(name: String, bot: Sequent, i: Int) => Hyp(name, Sequent(bot.left ++ context, bot.right), i)
        case LeftFalse(name: String, bot: Sequent) =>  LeftFalse(name, Sequent(bot.left ++ context, bot.right))
        case LeftWeaken(name: String, bot: Sequent, i: Int, t1: String) => LeftWeaken(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) => LeftWeakenRes(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case RightWeaken(name: String, bot: Sequent, i: Int, t1: String) => RightWeaken(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case ElimIffRefl(name: String, bot: Sequent, i: Int, t1: String) => ElimIffRefl(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case Cut(name: String, bot: Sequent, i: Int, t1: String, t2: String) => Cut(name, Sequent(bot.left ++ context, bot.right), i, t1, t2)
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
        case LeftSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(name, Sequent(bot.left ++ context, bot.right), i, flip, p, x, t1)
        case RightSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => RightSubst(name, Sequent(bot.left ++ context, bot.right), i, flip, p, x, t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(name, Sequent(bot.left ++ context, bot.right), i, flip, r, a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => RightSubstIff(name, Sequent(bot.left ++ context, bot.right), i, flip, r, a, t1) // TODO : check that
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
        case Clausify(name: String, bot: Sequent, i: Int, t1: String) => Clausify(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case NNF(name: String, bot: Sequent, i:Int, j: Int, t1: String) => NNF(name, Sequent(bot.left ++ context, bot.right), i, j, t1)
        case Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) =>  Instantiate_L(name, Sequent(bot.left ++ context, bot.right), i, x, t, parent)
        case InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) => InstantiateMult(name, Sequent(bot.left ++ context, bot.right), i, terms, parent)
        case _ => throw Exception("Proof step not found")
      }
  }

  def addContextProof(scproof: SCProof[?], context: Seq[Formula]): SCProof[?] = {    
    val new_steps = scproof.steps.map(x => addContextStep(x, context))
    if new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then LVL1Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName)
    if new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then LVL2Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName)
    else throw new Exception("Some proof steps could not be unrenamed")

    
  }

    // Add context 
  def removeContextProof(steps: Seq[SCProofStep]): Seq[SCProofStep] = {    
    steps.map(x => {
      x match {
        case Axiom(name: String, bot: Sequent) => Axiom(name, Sequent(Seq(), bot.right))
        case Hyp(name: String, bot: Sequent, i: Int) => Hyp(name, Sequent(Seq(), bot.right), i)
        case LeftFalse(name: String, bot: Sequent) =>  LeftFalse(name, Sequent(Seq(), bot.right))
        case LeftWeaken(name: String, bot: Sequent, i: Int, t1: String) => LeftWeaken(name, Sequent(Seq(), bot.right), i, t1)
        case LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) => LeftWeakenRes(name, Sequent(Seq(), bot.right), i, t1)
        case RightWeaken(name: String, bot: Sequent, i: Int, t1: String) => RightWeaken(name, Sequent(Seq(), bot.right), i, t1)
        case ElimIffRefl(name: String, bot: Sequent, i: Int, t1: String) => ElimIffRefl(name, Sequent(Seq(), bot.right), i, t1)
        case Cut(name: String, bot: Sequent, i: Int, t1: String, t2: String) => Cut(name, Sequent(Seq(), bot.right), i, t1, t2)
        case LeftAnd(name: String, bot: Sequent, i: Int, t1: String) => LeftAnd(name, Sequent(Seq(), bot.right), i, t1)
        case LeftOr(name: String, bot: Sequent, i: Int, t1: String, t2: String) =>  LeftOr(name, Sequent(Seq(), bot.right), i, t1, t2)
        case LeftImplies(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImplies(name, Sequent(Seq(), bot.right), i, t1, t2)
        case LeftIff(name: String, bot: Sequent, i: Int, t1: String) => LeftIff(name, Sequent(Seq(), bot.right), i, t1)
        case LeftNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNot(name, Sequent(Seq(), bot.right), i, t1)
        case LeftExists(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftExists(name, Sequent(Seq(), bot.right), i, y, t1)
        case LeftForall(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftForall(name, Sequent(Seq(), bot.right), i, t, t1)
        case RightAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightAnd(name, Sequent(Seq(), bot.right), i, t1, t2)
        case RightOr(name: String, bot: Sequent, i: Int, t1: String) => RightOr(name, Sequent(Seq(), bot.right), i, t1)
        case RightImplies(name: String, bot: Sequent, i: Int, t1: String) => RightImplies(name, Sequent(Seq(), bot.right), i, t1)
        case RightIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightIff(name, Sequent(Seq(), bot.right), i, t1, t2)
        case RightNot(name: String, bot: Sequent, i: Int, t1: String) => RightNot(name, Sequent(Seq(), bot.right), i, t1)
        case RightExists(name: String, bot: Sequent, i: Int, t: Term, t1: String) => RightExists(name, Sequent(Seq(), bot.right), i, t, t1)
        case RightForall(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => RightForall(name, Sequent(Seq(), bot.right), i, y, t1)
        case RightRefl(name: String, bot: Sequent, i: Int) => RightRefl(name, Sequent(Seq(), bot.right), i)
        case LeftSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(name, Sequent(Seq(), bot.right), i, flip, p, x, t1)
        case RightSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => RightSubst(name, Sequent(Seq(), bot.right), i, flip, p, x, t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(name, Sequent(Seq(), bot.right), i, flip, r, a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => RightSubstIff(name, Sequent(Seq(), bot.right), i, flip, r, a, t1) // TODO : check that
        case InstFun(name: String, bot: Sequent, f: FunctionSymbol, t: (Term, Seq[VariableSymbol]), t1: String) => InstFun(name, Sequent(Seq(), bot.right), f, t, t1)
        case InstPred(name: String, bot: Sequent, p: AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), t1: String) => InstPred(name, Sequent(Seq(), bot.right), p, phi, t1)
        case LeftHyp(name: String, bot: Sequent, i: Int, j: Int) =>  LeftHyp(name, Sequent(Seq(), bot.right), i, j)
        case LeftImp2(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImp2(name, Sequent(Seq(), bot.right), i, t1, t2)
        case LeftNotAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotAnd(name, Sequent(Seq(), bot.right), i, t1, t2)
        case LeftNotOr(name: String, bot: Sequent, i: Int, t1: String) => LeftNotOr(name, Sequent(Seq(), bot.right), i, t1)
        case LeftNotImp(name: String, bot: Sequent, i: Int, t1: String) => LeftNotImp(name, Sequent(Seq(), bot.right), i, t1)
        case LeftNotIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotIff(name, Sequent(Seq(), bot.right), i, t1, t2)
        case LeftNotNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNotNot(name, Sequent(Seq(), bot.right), i, t1)
        case LeftNotEx(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftNotEx(name, Sequent(Seq(), bot.right), i, t, t1)
        case LeftNotAll(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftNotAll(name, Sequent(Seq(), bot.right), i, y, t1)
        case RightSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => RightSubstMulti(name, Sequent(Seq(), bot.right), is: List[Int], p, xs, t1)
        case LeftSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => LeftSubstMulti(name, Sequent(Seq(), bot.right), is: List[Int], p, xs, t1)
        case Congruence(name: String, bot: Sequent) => Congruence(name, Sequent(Seq(), bot.right))
        case Res(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) => Res(name, Sequent(Seq(), bot.right), i1, i2, t1, t2)
        case NegatedConjecture(name: String, bot: Sequent, t1: String) => NegatedConjecture(name, Sequent(Seq(), bot.right), t1)
        case Clausify(name: String, bot: Sequent, i: Int, t1: String) => Clausify(name, Sequent(Seq(), bot.right), i, t1)
        case NNF(name: String, bot: Sequent, i:Int, j: Int, t1: String) => NNF(name, Sequent(Seq(), bot.right), i, j, t1)
        case Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) =>  Instantiate_L(name, Sequent(Seq(), bot.right), i, x, t, parent)
        case InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) => InstantiateMult(name, Sequent(Seq(), bot.right), i, terms, parent)
        case _ => throw Exception("Proof step not found")
      }

    })
  }

  // Remove $false
  def removeFalse(context: Seq[AtomicFormula], lastId: String): Seq[LVL1ProofStep] = {
    Seq(
      LeftFalse("stepFalse0", Sequent(Seq(), Seq())),
      Cut("stepFalse1", Sequent(context, Seq()), 0, lastId , "stepFalse0")
    )
  }

    // Remove $false
  def removeFalse2(scproof: SCProof[?]): SCProof[?] = {

    def removeFalse2Aux(l:  Seq[Formula]): Seq[Formula] = {
      l.filter(x => {
        !(x.isInstanceOf[AtomicFormula]) || x.asInstanceOf[AtomicFormula].label.id.name != "$false"
      })
    }

    val new_steps = scproof.steps.map(x => {
      x match {
        case Axiom(name: String, bot: Sequent) => Axiom(name, Sequent(bot.left, removeFalse2Aux(bot.right)))
        case Hyp(name: String, bot: Sequent, i: Int) => Hyp(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i)
        case LeftFalse(name: String, bot: Sequent) =>  LeftFalse(name, Sequent(bot.left, removeFalse2Aux(bot.right)))
        case LeftWeaken(name: String, bot: Sequent, i: Int, t1: String) => LeftWeaken(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) => LeftWeakenRes(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case RightWeaken(name: String, bot: Sequent, i: Int, t1: String) => RightWeaken(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case ElimIffRefl(name: String, bot: Sequent, i: Int, t1: String) => ElimIffRefl(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case Cut(name: String, bot: Sequent, i: Int, t1: String, t2: String) => Cut(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1, t2)
        case LeftAnd(name: String, bot: Sequent, i: Int, t1: String) => LeftAnd(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case LeftOr(name: String, bot: Sequent, i: Int, t1: String, t2: String) =>  LeftOr(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1, t2)
        case LeftImplies(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImplies(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1, t2)
        case LeftIff(name: String, bot: Sequent, i: Int, t1: String) => LeftIff(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case LeftNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNot(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case LeftExists(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftExists(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, y, t1)
        case LeftForall(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftForall(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t, t1)
        case RightAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightAnd(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1, t2)
        case RightOr(name: String, bot: Sequent, i: Int, t1: String) => RightOr(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case RightImplies(name: String, bot: Sequent, i: Int, t1: String) => RightImplies(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case RightIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => RightIff(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1, t2)
        case RightNot(name: String, bot: Sequent, i: Int, t1: String) => RightNot(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case RightExists(name: String, bot: Sequent, i: Int, t: Term, t1: String) => RightExists(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t, t1)
        case RightForall(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => RightForall(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, y, t1)
        case RightRefl(name: String, bot: Sequent, i: Int) => RightRefl(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i)
        case LeftSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => LeftSubst(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, flip, p, x, t1)
        case RightSubst(name: String, bot: Sequent, i: Int, flip, p: Formula, x: VariableSymbol, t1: String) => RightSubst(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, flip, p, x, t1)
        case LeftSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => LeftSubstIff(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, flip, r, a, t1) // TODO : check that
        case RightSubstIff(name: String, bot: Sequent, i: Int, flip, r: Formula, a: AtomicSymbol, t1: String) => RightSubstIff(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, flip, r, a, t1) // TODO : check that
        case InstFun(name: String, bot: Sequent, f: FunctionSymbol, t: (Term, Seq[VariableSymbol]), t1: String) => InstFun(name, Sequent(bot.left, removeFalse2Aux(bot.right)), f, t, t1)
        case InstPred(name: String, bot: Sequent, p: AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), t1: String) => InstPred(name, Sequent(bot.left, removeFalse2Aux(bot.right)), p, phi, t1)
        case LeftHyp(name: String, bot: Sequent, i: Int, j: Int) =>  LeftHyp(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, j)
        case LeftImp2(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftImp2(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1, t2)
        case LeftNotAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotAnd(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1, t2)
        case LeftNotOr(name: String, bot: Sequent, i: Int, t1: String) => LeftNotOr(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case LeftNotImp(name: String, bot: Sequent, i: Int, t1: String) => LeftNotImp(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case LeftNotIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) => LeftNotIff(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1, t2)
        case LeftNotNot(name: String, bot: Sequent, i: Int, t1: String) => LeftNotNot(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case LeftNotEx(name: String, bot: Sequent, i: Int, t: Term, t1: String) => LeftNotEx(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t, t1)
        case LeftNotAll(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => LeftNotAll(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, y, t1)
        case RightSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => RightSubstMulti(name, Sequent(bot.left, removeFalse2Aux(bot.right)), is: List[Int], p, xs, t1)
        case LeftSubstMulti(name: String, bot: Sequent, is: List[Int], p: Formula, xs: List[VariableSymbol], t1: String) => LeftSubstMulti(name, Sequent(bot.left, removeFalse2Aux(bot.right)), is: List[Int], p, xs, t1)
        case Congruence(name: String, bot: Sequent) => Congruence(name, Sequent(bot.left, removeFalse2Aux(bot.right)))
        case Res(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) => Res(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i1, i2, t1, t2)
        case NegatedConjecture(name: String, bot: Sequent, t1: String) => NegatedConjecture(name, Sequent(bot.left, removeFalse2Aux(bot.right)), t1)
        case Clausify(name: String, bot: Sequent, i: Int, t1: String) => Clausify(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, t1)
        case NNF(name: String, bot: Sequent, i:Int, j: Int, t1: String) => NNF(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, j, t1)
        case Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) =>  Instantiate_L(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, x, t, parent)
        case InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) => InstantiateMult(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, terms, parent)
        case Let(name: String, bot: Sequent) => Let(name, Sequent(bot.left, removeFalse2Aux(bot.right)))
        case Prenex(name: String, bot: Sequent, t1: String) => Prenex(name, Sequent(bot.left, removeFalse2Aux(bot.right)), t1)
        case InstForall(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) => InstForall(name, Sequent(bot.left, removeFalse2Aux(bot.right)), i, y, t1)
        case _ => throw Exception("Proof step not found")
      }

    })

    if new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then LVL1Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName)
    if new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then LVL2Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName)
    else throw new Exception("Some proof steps could not be unrenamed")
  }

  // Add psi
  def addPsi(context: Seq[AtomicFormula], parent: String): Seq[LVL2ProofStep] = {
    Seq(
      Let("psi", Sequent(Seq(psi), Seq(originalFormula))),
      Hyp("addPsi0", Sequent(Seq(psi), Seq(psi)), 0),
      RightNot("addPsi1", Sequent(Seq(), Seq(psi, phi)), 0, "addPsi0"),
      Cut("addPsi2", Sequent(context.drop(1), Seq(psi)), 1, "addPsi1", parent)
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