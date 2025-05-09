package sctptp

import SequentCalculus.*
import SequentCalculus.RulesName.*
import sctptp.FOL.*
import sctptp.LVL2.LVL2ProofStep
import sctptp.LVL2.*
import scala.annotation.switch

object CoqOutput {

  // Add lemmas stored in SCTPTP.v
  def getSCTPTPLemmas(): String = {
    val source = scala.io.Source.fromFile("src/SCTPTP.v")
    val lines =
      try source.mkString
      finally source.close()

    lines + "\n"
  }

  def retrieveUninstantiatedVariables(steps: IndexedSeq[SCProofStep]): (Set[VariableSymbol]) = {
    steps.foldLeft(Set())((acc, e) => {
      e match
        case LeftExists(name, bot, i, y, t1) => acc - y
        case LeftForall(name, bot, i, t, t1) => acc ++ t.freeVariables

        case RightExists(name, bot, i, y, t1) => acc ++ y.freeVariables
        case RightForall(name, bot, i, t, t1) => acc - t

        case LeftNotEx(name, bot, i, y, t1) => acc ++ y.freeVariables
        case LeftNotAll(name, bot, i, t, t1) => acc - t
        case _ => acc
    })
  }

  def computeUninstantiatedVariables(variables: Set[VariableSymbol]): String = {
    variables.foldLeft("") { (acc, e) => acc + s"Parameter ${e.name}: sctptp_U.\n" }
  }

  def associateSignaturePredicates(context: Map[String, Int]): String = {
    context.foldLeft("") {
      case (acc, (k, v)) => {
        v match
          case 0 => acc + s"Parameter ${k}: Prop.\n"  
          case 1 => acc + s"Parameter ${k}: sctptp_U -> Prop.\n"  
          case _ => acc + s"Parameter ${k}: ((${Seq.range(0, v).foldLeft(List())((acc, _) => ("sctptp_U" :: acc)).mkString(" * ")}) -> Prop).\n"
      }
    }
  }

  def associateSignatureTerms(context: Map[String, Int]): String = {
    context.foldLeft("") {
      case (acc, (k, v)) => {
        v match
          case 0 => acc + s"Parameter ${k}: sctptp_U.\n"  
          case 1 => acc + s"Parameter ${k}: sctptp_U -> sctptp_U.\n"  
          case _ => acc + s"Parameter ${k}: ((${Seq.range(0, v).foldLeft(List())((acc, _) => ("sctptp_U" :: acc)).mkString(" * ")}) -> sctptp_U).\n"
      }
    }
  }

  def contextPreamble(ScProof: SCProof[?], context_p: Map[String, Int], context_t: Map[String, Int]): String = {
    getSCTPTPLemmas()
      + "Parameter sctptp_U : Set. (* universe *)\n"
      + "Parameter sctptp_I :  sctptp_U. (* an individual in the universe. *)\n\n"
      + associateSignaturePredicates(context_p) + "\n"
      + associateSignatureTerms(context_t) + "\n"
      + computeUninstantiatedVariables(retrieveUninstantiatedVariables(ScProof.steps)) + "\n"
  }

  def makeAlphaStep(ruleName: String, i: Int, indexNextIntro: Int, indexMax: Int): String = {
    s"apply (${ruleName} ${Seq.range(0, indexMax).foldLeft("")((acc, _) => (acc + " _ "))} H${i}). intros ${Seq
        .range(indexNextIntro, indexNextIntro + indexMax)
        .foldLeft("", i)((acc, e) => (acc._1 + "H" + e.toString() + (if (acc._2 != i + indexMax - 1) then " " else ""), acc._2 + 1))
        ._1}."
  }

  // starting index, number of branches (all with the same x for Hx)
  def makeBetaStep(ruleName: String, i: Int, indexNextIntro: Int, indexMax: Int): String = {
    s"apply (${ruleName} _ _ H${i}); [${Seq
        .range(indexNextIntro, indexNextIntro + indexMax)
        .foldLeft("", i + 1)((acc, _) => (acc._1 + s"intros H${indexNextIntro}" + (if (acc._2 != i + indexMax) then " | " else ""), acc._2 + 1))
        ._1}]."
  }

  def computeNextStep(step: SCProofStep, indexNextIntro: Int): (String, Int) = step match {
    case Axiom(name, bot) => ("", 0)
    case Hyp(name, bot, i) => (s"(* [${name}] *) " + "auto.", 0)
    case LeftHyp(name, bot, i) => (s"(* [${name}] *) " + "auto.", 0)
    case LeftWeaken(name, bot, i, t1) => (s"(* [${name}] *) " + s"clear H${i}.", 0)
    case RightWeaken(name, bot, i, t1) => (s"(* [${name}] *) " + s"clear ${bot.right(i).toString()}.", 0)
    //case LeftWeaken(name, bot, t1) => ???
    case Cut(name, bot, i, t1, t2) => ("", 1)
    case LVL2.Congruence(name, bot) => (s"(* [${name}] *) congruence.", 0)

    case LeftAnd(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(LeftAndRuleName, i, indexNextIntro, 2), 2)
    case LeftOr(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(LeftOrRuleName, i, indexNextIntro, 2), 1)
    case LeftImplies(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(LeftImpliesRuleName, i, indexNextIntro, 2), 1)
    case LeftImp2(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(LeftImp2RuleName, i, indexNextIntro, 2), 1)
    case LeftIff(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(LeftIffRuleName, i, indexNextIntro, 2), 2)
    case LeftNot(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(LeftNotRuleName, i, indexNextIntro, 1), 2)
    case LeftExists(name, bot, i, y, t1) => ( s"(* [${name}] *) elim H${i}. intros ${y}. intros  H${indexNextIntro}. ", 1)
    case LeftForall(name, bot, i, t, t1) => (s"(* [${name}] *) generalize (H${i} (${t})). intros H${indexNextIntro}. ", 1)

    case RightAnd(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(RightAndRuleName, i, indexNextIntro, 2), 1)
    case RightOr(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(RightOrRuleName, i, indexNextIntro, 2), 2)
    case RightImplies(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(RightImpliesRuleName, i, indexNextIntro, 2), 2)
    case RightIff(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(RightIffRuleName, i, indexNextIntro, 1), 1)
    case RightNot(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(RightNotRuleName, i, indexNextIntro, 1), 2)
    case RightExists(name, bot, i, y, t1) => (s"(* [${name}] *) apply H${i}. exists ${y}. apply NNPP. intros H${indexNextIntro}. ", 1)
    case RightForall(name, bot, i, t, t1) => (s"(* [${name}] *) apply H${i}. intros ${t}. apply NNPP. intros  H${indexNextIntro}. ", 1)

    case LeftNotAnd(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(LeftNotAndRuleName, i, indexNextIntro, 2), 1)
    case LeftNotOr(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(LeftNotOrRuleName, i, indexNextIntro, 2), 2)
    case LeftNotImp(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(LeftNotImpRuleName, i, indexNextIntro, 2), 2)
    case LeftNotIff(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(LeftNotIffRuleName, i, indexNextIntro, 2), 1)
    case LeftNotNot(name, bot, i, t1) => (s"(* [${name}] *) apply H${i}. intro H${indexNextIntro}.", 1)
    case LeftNotEx(name, bot, i, y, t1) => (s"(* [${name}] *) apply H${i}. exists ${y}. apply NNPP. intros H${indexNextIntro}. ", 1)
    case LeftNotAll(name, bot, i, t, t1) => (s"(* [${name}] *) apply H${i}. intros ${t}. apply NNPP. intros  H${indexNextIntro}. ", 1)

    case _ => ("", 0)

    // rightnot = intro.
    // rightAnd, rightImp = intro.
  }

  case class CoqProof(ScProof: SCProof[?], context_p: Map[String, Int], context_t: Map[String, Int]) {

    def mapTheoremToCoqSymbols(f: Formula, context_p: Map[String, Int], context_t: Map[String, Int]): (String, Map[String, Int], Map[String, Int]) = {
      f match
        case AtomicFormula(label, args) => {
          label match
            case `equality` => (s"(${args(0).toString()} = ${args(1).toString()})", context_p, context_t)
            case `top` => ("True", context_p, context_t)
            case `bot` => ("False", context_p, context_t)
            case s =>
              if (label.arity == 0) then (s.id.name, context_p + (s.id.name -> 0), context_t)
              else {
                (s"${s}(${args.mkString(",")})", context_p + (s.id.name -> label.arity), context_t ++ makeMapTermListToArity(args)) 
              }
              
        }
        case ConnectorFormula(label, args) => {
          label match
            case Neg => {
              val (res_form, res_ctx, res_var) = mapTheoremToCoqSymbols(args(0), context_p, context_t)
              (s"~(${res_form})", res_ctx, res_var)
            }
            case Implies => {
              val (res_form0, res_ctx0, res_var0) = mapTheoremToCoqSymbols(args(0), context_p, context_t)
              val (res_form1, res_ctx1, res_var1) = mapTheoremToCoqSymbols(args(1), context_p, context_t)
              (s"((${res_form0}) -> (${res_form1}))", res_ctx0 ++ res_ctx1, res_var0 ++ res_var1)
            }
            case Iff => {
              val (res_form0, res_ctx0, res_var0) = mapTheoremToCoqSymbols(args(0), context_p, context_t)
              val (res_form1, res_ctx1, res_var1) = mapTheoremToCoqSymbols(args(1), context_p, context_t)
              (s"((${res_form0}) <-> (${res_form1}))", res_ctx0 ++ res_ctx1,  res_var0 ++ res_var1)
            }
            case And => {
              val res = args.foldLeft("", context_p, context_t, 0)((acc, e) => {
                val res2 = mapTheoremToCoqSymbols(e, context_p, context_t)
                (acc._1 + res2._1 + (if (acc._4 != args.length - 1) then " /\\ " else ""), acc._2 ++ res2._2, acc._3 ++ res2._3, acc._4 + 1)
              })
              ("(" + res._1 + ")", res._2, res._3)
            }
            case Or => {
              val res = args.foldLeft("", context_p, context_t, 0)((acc, e) => {
                val res2 = mapTheoremToCoqSymbols(e, context_p, context_t)
                (acc._1 + res2._1 + (if (acc._4 != args.length - 1) then " \\/ " else ""), acc._2 ++ res2._2, acc._3 ++ res2._3, acc._4 + 1)
              })
              ("(" + res._1 + ")", res._2, res._3)
            }
        }
        case BinderFormula(label, bound, inner) => {
          label match
            case Forall => {
              val (res_form, res_ctx, res_var) = mapTheoremToCoqSymbols(inner, context_p, context_t)
              (s"(forall (${bound.toString()}: sctptp_U), ${res_form})", res_ctx, res_var)
            }
            case Exists => {
              val (res_form, res_ctx, res_var) = mapTheoremToCoqSymbols(inner, context_p, context_t)
              (s"(exists (${bound.toString()}: sctptp_U), ${res_form})", res_ctx, res_var)
            }
        }
    }

    def makeMapTermListToArity(tl: Seq[Term]): Map[String, Int] = {
      tl.foldLeft(Map.empty[String, Int])((acc, e) => 
        e match 
          case FunctionTerm(FunctionSymbol(id, arity), args) => acc + (id.name -> arity)
          case FunctionTerm(VariableSymbol(id), args) => acc
          case _ => throw new Exception("Error: Coq printing does not support epsilon terms")
      )
    }

    def introAxioms(axioms: Seq[Formula]) : (String, Int) = {
      axioms.foldLeft(("", 0))((acc, e) => {
        (acc._1 + s"intro H${acc._2}. " , acc._2 + 1)
      })
    }

    def makeTheoremAndContext(firstStep: SCProofStep): (String, Map[String, Int], Map[String, Int], Int) = {
      val axiomsList = firstStep.bot.left.slice(0, firstStep.bot.left.length - 1)
      val theorem = !(firstStep.bot.left(firstStep.bot.left.length - 1))

      val (axioms, nextIndex) = introAxioms(axiomsList)

      val initialFormula = axiomsList.foldRight(theorem: Formula)((e, acc) => { e ==> acc }) 

      val res = mapTheoremToCoqSymbols(initialFormula, Map.empty[String, Int], Map.empty[String, Int])
      (s"Theorem ${ScProof.thmName}:  ${res._1}." + "\nProof.\n" + axioms + s"intro H${nextIndex}.\n", res._2, res._3, nextIndex + 1)
    }

    def getStepFromName(Steps: IndexedSeq[SCProofStep], name: String): SCProofStep = {
      Steps.filter(e => e.name == name)(0)
    }

    def toCoqSteps(Steps: IndexedSeq[SCProofStep], name: String, Index: Int): (List[String]) = {
      val currentStep = getStepFromName(Steps, name)
      val (currentStepString, nextIndex) = computeNextStep(currentStep, Index)
      
      currentStepString :: currentStep.premises.foldRight((List()))((e, acc) => {
         toCoqSteps(Steps, e, Index + nextIndex) ++ acc
      })
    }

    override def toString(): String = {
      val firstStep = getStepFromName(ScProof.steps, "f3")
      val (initialStep, context_p, context_t, nextIndex) = makeTheoremAndContext(firstStep)
      contextPreamble(ScProof, context_p, context_t)
        + initialStep
        + toCoqSteps(ScProof.steps, "f3", nextIndex).foldRight("")((e, acc) => e + "\n" + acc)
        + "Qed."
    }
  }
}