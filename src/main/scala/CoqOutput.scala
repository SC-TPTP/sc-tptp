package sctptp

import SequentCalculus.*
import SequentCalculus.RulesName.*
import sctptp.FOL.Formula
import sctptp.FOL.Identifier
import sctptp.FOL.AtomicFormula
import sctptp.FOL.ConnectorFormula
import sctptp.FOL.BinderFormula
import sctptp.FOL.Neg
import sctptp.FOL.Implies
import sctptp.FOL.Iff
import sctptp.FOL.And
import sctptp.FOL.Or
import sctptp.FOL.Forall
import sctptp.FOL.Exists
import sctptp.LVL2.LVL2ProofStep

object CoqOutput {

  // Add lemmas stored in SCTPTP.v
  def getSCTPTPLemmas(): String = {
    val source = scala.io.Source.fromFile("src/SCTPTP.v")
    val lines =
      try source.mkString
      finally source.close()

    lines + "\n"
  }

  def retrieveVariables(steps: IndexedSeq[SCProofStep]): (Set[Identifier], Set[Identifier]) = {
    steps.foldLeft(Set(), Set())((acc, e) => {
      e match
        case LeftEx(name, bot, i, y, t1) =>  (acc._1 + y.id, acc._2 + y.id)
        case LeftAll(name, bot, i, t, t1) => (acc._1 + t.label.id, acc._2)

        case RightEx(name, bot, i, y, t1) => (acc._1 + y.label.id, acc._2)
        case RightAll(name, bot, i, t, t1) => (acc._1 + t.id, acc._2 + t.id)

        case LeftNotEx(name, bot, i, y, t1) => (acc._1 + y.label.id, acc._2)
        case LeftNotAll(name, bot, i, t, t1) => (acc._1 + t.id, acc._2 + t.id)

        case _ => acc
    })
  }

  def computeUninstantiatedVariables(allVar: Set[Identifier], instantiatedVar: Set[Identifier]): String = {
    val uninstantiatedVariables = allVar -- instantiatedVar
    uninstantiatedVariables.foldLeft("") { (acc, e) => acc + s"Parameter ${e.name}: sctptp_U.\n" }
  }

  def associatePredicate(context: Map[String, Int]): String = {
    context.foldLeft("") {
      case (acc, (k, v)) => {
        if v == 0
        then acc + s"Parameter ${k}: Prop.\n"
        else acc + s"Parameter ${k}: (${Seq.range(0, v).foldLeft("")((acc, _) => (acc + "sctptp_U -> "))} Prop).\n"
      }
    }
  }

  def contextPreamble(ScProof: SCProof[?], context: Map[String, Int]): String = {
    val (allVar, instantiatedVar) = retrieveVariables(ScProof.steps)
    getSCTPTPLemmas()
      + "Parameter sctptp_U : Set. (* universe *)\n"
      + "Parameter sctptp_I :  sctptp_U. (* an individual in the universe. *)\n\n"
      + associatePredicate(context) + "\n"
      + computeUninstantiatedVariables(allVar, instantiatedVar) + "\n"
  }

  def makeAlphaStep(ruleName: String, i: Int, indexNextIntro: Int, indexMax: Int): String = {
    s"apply (${ruleName} ${Seq.range(0, indexMax).foldLeft("")((acc, _) => (acc + " _ "))} H${i}). intros ${Seq
        .range(indexNextIntro, indexNextIntro + indexMax)
        .foldLeft("", i)((acc, e) => (acc._1 + "H" + e.toString() + (if (acc._2 != i + indexMax) then " " else ""), acc._2 + 1))
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
    case Hyp(name, bot, i, j) => (s"(* [${name}] *) " + "auto.", 0)
    case LeftHyp(name, bot, i, j) => (s"(* [${name}] *) " + "auto.", 0)
    case LeftWeakening(name, bot, i, t1) => (s"(* [${name}] *) " + s"clear H${i}.", 0)
    case RightWeakening(name, bot, i, t1) => (s"(* [${name}] *) " + s"clear ${bot.right(i).toString()}.", 0)
    case Cut(name, bot, i, j, t1, t2) => ("", 0)

    case LeftAnd(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(LeftAndRuleName, i, indexNextIntro, 2), 2)
    case LeftOr(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(LeftOrRuleName, i, indexNextIntro, 2), 1)
    case LeftImp1(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(LeftImp1RuleName, i, indexNextIntro, 2), 1)
    case LeftImp2(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(LeftImp2RuleName, i, indexNextIntro, 2), 1)
    case LeftIff(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(LeftIffRuleName, i, indexNextIntro, 2), 2)
    case LeftNot(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(LeftNotRuleName, i, indexNextIntro, 1), 2)
    case LeftEx(name, bot, i, y, t1) => ("", 0)
    case LeftAll(name, bot, i, t, t1) => ("", 0)

    case RightAnd(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(RightAndRuleName, i, indexNextIntro, 2), 1)
    case RightOr(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(RightOrRuleName, i, indexNextIntro, 2), 2)
    case RightImp(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(RightImpRuleName, i, indexNextIntro, 2), 2)
    case RightIff(name, bot, i, t1, t2) => (s"(* [${name}] *) " + makeBetaStep(RightIffRuleName, i, indexNextIntro, 1), 1)
    case RightNot(name, bot, i, t1) => (s"(* [${name}] *) " + makeAlphaStep(RightNotRuleName, i, indexNextIntro, 1), 2)
    case RightEx(name, bot, i, y, t1) => ("", 0)
    case RightAll(name, bot, i, t, t1) => ("", 0)

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

  case class CoqProof(ScProof: SCProof[?], context: Map[String, Int]) {

    def mapTheoremToCoqSymbols(f: Formula, context: Map[String, Int]): (String, Map[String, Int]) = {
      f match
        case AtomicFormula(label, args) => {
          label.id.name match
            case "equality" => (s"(${args(0).toString()} = ${args(1).toString()})", context)
            case "$true" => ("True", context)
            case "$false" => ("False", context)
            case s =>
              if (label.arity == 0) then (s, context + (s -> 0))
              else (s"${s}(${args.mkString(",")})", context + (s -> label.arity))
        }
        case ConnectorFormula(label, args) => {
          label match
            case Neg => {
              val (res_form, res_ctx) = mapTheoremToCoqSymbols(args(0), context)
              (s"~${res_form}", res_ctx)
            }
            case Implies => {
              val (res_form0, res_ctx0) = mapTheoremToCoqSymbols(args(0), context)
              val (res_form1, res_ctx1) = mapTheoremToCoqSymbols(args(1), context)
              (s"(${res_form0} -> ${res_form1})", res_ctx0 ++ res_ctx1)
            }
            case Iff => {
              val (res_form0, res_ctx0) = mapTheoremToCoqSymbols(args(0), context)
              val (res_form1, res_ctx1) = mapTheoremToCoqSymbols(args(1), context)
              (s"(${res_form0} <-> ${res_form1})", res_ctx0 ++ res_ctx1)
            }
            case And => {
              val res = args.foldLeft("", context, 0)((acc, e) => {
                val res2 = mapTheoremToCoqSymbols(e, context)
                (acc._1 + res2._1 + (if (acc._3 != args.length - 1) then " /\\ " else ""), acc._2 ++ res2._2, acc._3 + 1)
              })
              ("(" + res._1 + ")", res._2)
            }
            case Or => {
              val res = args.foldLeft("", context, 0)((acc, e) => {
                val res2 = mapTheoremToCoqSymbols(e, context)
                (acc._1 + res2._1 + (if (acc._3 != args.length - 1) then " \\/ " else ""), acc._2 ++ res2._2, acc._3 + 1)
              })
              ("(" + res._1 + ")", res._2)
            }
        }
        case BinderFormula(label, bound, inner) => {
          label match
            case Forall => {
              val (res_form, res_ctx) = mapTheoremToCoqSymbols(inner, context)
              (s"forall (${bound.toString()}: sctptp_U), ${res_form}", res_ctx)
            }
            case Exists => {
              val (res_form, res_ctx) = mapTheoremToCoqSymbols(inner, context)
              (s"exists (${bound.toString()}: sctptp_U), ${res_form}", res_ctx)
            }
        }
    }

    def makeTheoremAndContext(FirstStep: SCProofStep): (String, Map[String, Int]) = {
      val res = mapTheoremToCoqSymbols(FirstStep.bot.right(0), Map.empty[String, Int])
      (s"Theorem ${ScProof.thmName}:  ${res._1}." + "\nProof.\n" + "apply NNPP. intro H0.\n", res._2)
    }

    def getInitialStepIndex(Steps: IndexedSeq[SCProofStep]): Int = {
      Steps.indexWhere(e => e.name == "f0") - 2
    }

    def toCoqSteps(Steps: IndexedSeq[SCProofStep]): Seq[String] = {
      Steps.foldRight((Seq(), 1))((e, acc) => (acc._1 :+ computeNextStep(e, acc._2)._1 + "\n", acc._2 + computeNextStep(e, acc._2)._2))._1
    }

    override def toString(): String = {
      val (thm, ctx) = makeTheoremAndContext(ScProof(ScProof.steps.length - 1))
      contextPreamble(ScProof, ctx)
        + thm
        + toCoqSteps(ScProof.steps.slice(0, getInitialStepIndex(ScProof.steps))).foldRight("")((e, acc) => e + acc)
        + "\nQed."
    }
  }
}
