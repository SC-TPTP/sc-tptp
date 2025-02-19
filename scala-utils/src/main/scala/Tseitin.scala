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



class Tseitin {

  val varName = "V"
  val tseitinName = "Ts"
  var varCpt = 0
  val skoName = "sko"
  var skoCpt = 0 
  val ladrName = "ts"
  var ladrCpt = -1
  val instantiateName = "i"
  val tseitinStepName = "tsStep"
  val tseitinStepNameExpl = "tsStepExpl"
  val NoneFormula = AtomicFormula(AtomicSymbol("None", 0), Seq())
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
    val phi = "phi"
    originalFormula = AtomicFormula(AtomicSymbol(Identifier("phi"), 0), Seq())
    val new_f = ConnectorFormula(Neg , Seq(f)) 
    val initial_let = Let(phi, Sequent(Seq(originalFormula), Seq(new_f)))

    (new_f, originalFormula, Seq(
      LeftSubstIff("sc_step1", Sequent(Seq(originalFormula), Seq(new_f)), 0, new_f, originalFormula.label, "nc_step0"),
      NegatedConjecture("nc_step0", Sequent(Seq(new_f), Seq(new_f)), ""), 
      initial_let))

    //todo : add right iff 
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
    (new_f, NNF("nnf_step", Sequent(Seq(originalFormula), Seq(new_f)), "nc_step1"))
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
    (new_f, Prenex("prenex_step", Sequent(Seq(originalFormula), Seq(new_f)) ,"nnf_step"))
  }

  // Instantiate universal quantifiers and returns a map [oldName, newName], the new formula, and has renamed them according to P9 standard
  // 0 = forall, 1 = exists
  def toInstantiated(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, Map[VariableSymbol, VariableSymbol], Seq[LVL1ProofStep]) = {
      def toInstantiatedAux(f2: sctptp.FOL.Formula, accVS: Map[VariableSymbol, VariableSymbol], accT: Map[VariableSymbol, Term], accProof: Seq[LVL1ProofStep], cpt: Int): (sctptp.FOL.Formula, Map[VariableSymbol, VariableSymbol], Seq[LVL1ProofStep]) = {
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
                val ps = RightForall(instantiateName+cpt, Sequent(Seq(originalFormula), Seq(new_form)), 0, bound, if cpt > 0 then s"${instantiateName}${cpt-1}" else "prenex_step")
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
                val ps = RightExists(instantiateName+cpt, Sequent(Seq(originalFormula),Seq(new_form)), 0, new_term, if cpt > 0 then s"${instantiateName}${cpt-1}" else "prenex_step")
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
    (Let(atomicForm.label.id.name, Sequent(Seq(), Seq(new_f))), atomicForm)
  }

  // Input : a formula f and a map an generate the let that connect he variable to the formula
  def generateTseitin(): (Seq[LVL2ProofStep], Map[AtomicFormula, AtomicFormula], Seq[AtomicFormula]) = {
    val res = tseitinVarTermUp.foldLeft((Seq[LVL2ProofStep](), Map[AtomicFormula, AtomicFormula](), Seq[AtomicFormula](), tseitinVarTermUp.size-1))((acc, x) => {
      val (ps, psname) = generateLet(x._1, x._2, acc._4)
      (acc._1 :+ ps, acc._2 + (psname -> x._1.asInstanceOf[AtomicFormula]), acc._3 :+ psname, acc._4-1)
      })
      
    (res._1, res._2, res._3)
    }
  

  /**
    * fof(i1, plain, [] --> [((p(V0) | (p(f(V1)) & p(c))) & (~p(f(f(a))) | ~p(c)))], inference(....))
    * fof(tsStep3, let, (ladr7 <=> (~p(f(f(a))) | ~p(c)))).
    * fof(tsStep3Exp, plain, [ladr7] --> [((p(V0) | (p(f(V1)) & p(c))) & ladr7)], inference( rightSubstIff, [status(thm), 0, ((p(V0) | (p(f(V1)) & p(c))) & HOLE), 'HOLE'], i1))
    */
  def computeTseitinReplacementSteps(s: SCProofStep, tsNames: Seq[AtomicFormula], map: Map[AtomicFormula, AtomicFormula], tsSteps: Seq[LVL2ProofStep], original_parent: String): Seq[LVL2ProofStep] = {

      tsSteps.foldLeft(
        (Seq[LVL2ProofStep](), 0, Map[Formula, Formula](), s.bot.right(0)))(
          (acc, x) => {
            val new_acc3 = acc._3 + (tseitinVarTermUp(map(tsNames(acc._2))) -> map(tsNames(acc._2)))
            val previousForms = tsNames.take(acc._2+1) // acc + courant
            val fSubstituted = substituteFormulaInFormula(
              acc._4, 
              new_acc3 
              )
            val parent = if (acc._2 == 0) then original_parent else tseitinStepNameExpl+(acc._2-1)
            
            (
            acc._1 :+ RightSubstIff(tseitinStepNameExpl+acc._2,
            Sequent( 
            originalFormula +: previousForms, // les formules d'avant, avec un acc
            Seq(fSubstituted)), 
            0, 
            tseitinVarTermUp(map(tsNames(acc._2))),
            AtomicSymbol(map(tsNames(acc._2)).toString(), 0), 
            parent), 
            acc._2+1, 
            new_acc3, 
            fSubstituted
        )})._1.reverse
  }
  
  // Generate Tseitin Step
  def generateTseitinStep(f: Formula, context: Seq[Formula], parent: Int, scproof: SCProof[?]): SCProof[?] = {
    val firstStep = TseitinStep("tseitinStep", Sequent(context, Seq(f)), tseitinStepNameExpl+parent)

    val clausalSteps = f.asInstanceOf[ConnectorFormula].args.foldLeft((Seq[LVL2ProofStep](), 0))((acc, x) => {
      (Clausify(acc._2.toString(),Sequent(originalFormula +: context, Seq(x)), "") +: acc._1, acc._2+1)
    })._1 // :+ firstStep

    val new_steps = scproof.steps.flatMap(x => {
      x match {
        case Axiom(name: String, bot: Sequent) => {
          println(x)
          val originalStep = clausalSteps.filter(x => areAlphaEquivalent(x.bot.right(0), toFlatternOr(bot.right(0))))
          if (originalStep.size > 0) 
            then Some(Clausify(name, Sequent(originalStep(0).bot.left, Seq(toFlatternOr(originalStep(0).bot.right(0)))), ""))
            else None
        }
        case _ => Some(x)
      }})
  
    if new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then LVL1Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName)
    if new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then LVL2Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName)
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

    // println(s"Content written to: ${path.toAbsolutePath}")

    // Command to execute with shell
    val command = "sh -c \"./../p9-sc-tptp/tptp_to_ladr < ../proofs/p9/p9.p | ./../p9-sc-tptp/prover9 > ../proofs/p9/p9.in && ./../p9-sc-tptp/prooftrans ivy -f ../proofs/p9/p9.in > ../proofs/p9/p9.out\""

    // Run the command and redirect both stdout and stderr to /dev/null
    val exitCode = command #> new java.io.File("/dev/null") #>> new java.io.File("/dev/null")


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

  // Add context 
  def addContextProof(scproof: SCProof[?], context: Seq[AtomicFormula]): SCProof[?] = {    
    val new_steps = scproof.steps.map(x => {
      x match {
        case Axiom(name: String, bot: Sequent) => Axiom(name, Sequent(bot.left ++ context, bot.right))
        case Hyp(name: String, bot: Sequent, i: Int) => Hyp(name, Sequent(bot.left ++ context, bot.right), i)
        case LeftWeaken(name: String, bot: Sequent, i: Int, t1: String) => LeftWeaken(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) => LeftWeakenRes(name, Sequent(bot.left ++ context, bot.right), i, t1)
        case RightWeaken(name: String, bot: Sequent, i: Int, t1: String) => RightWeaken(name, Sequent(bot.left ++ context, bot.right), i, t1)
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
        case NNF(name: String, bot: Sequent, t1: String) => NNF(name, Sequent(bot.left ++ context, bot.right), t1)
        case Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) =>  Instantiate_L(name, Sequent(bot.left ++ context, bot.right), i, x, t, parent)
        case InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) => InstantiateMult(name, Sequent(bot.left ++ context, bot.right), i, terms, parent)
        case _ => throw Exception("Proof step not found")
      }

    })

    if new_steps.forall(_.isInstanceOf[LVL1ProofStep]) then LVL1Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL1ProofStep]], scproof.thmName)
    if new_steps.forall(_.isInstanceOf[LVL2ProofStep]) then LVL2Proof(new_steps.toIndexedSeq.asInstanceOf[IndexedSeq[LVL2ProofStep]], scproof.thmName)
    else throw new Exception("Some proof steps could not be unrenamed")

    
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
  * [x] donner la formule finale P9 (sous forme axiom et sans quantifier universel)
  * [x] Derenommer les variables dans la preuve 
  * [x] Ajouter les let
  * 
  * TODO : 
  * [x] Exemple plus simple   
  * [x] FOrmule sprécédentes à garder ?
  * [ ] Que faire des forall ?
  * 
  * [ ] Instantiate à droite ?
  * [ ] Status des steps
  * [ ] Ex sans exists
  * [ ] Ex avec exists
  * [ ] Tseitin 
  * [ ] Pipeline + executable
  * [ ] Post traitement epsilon-Term
  * 
  * TODO++:
  * unfold resolution --- leftnot et cut
  * unfold inst multi
  * neg à droite, pos à gauche + tracking des listes -> relou ?
  * unfold nnf
  * 
  * TODO : reprendre unraname variable
  * Later : 
  * Update gog sans param + status thm
  **/
}