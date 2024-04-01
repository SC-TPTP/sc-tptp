package sctptp

import SequentCalculus.*
import SequentCalculus.RulesName.*

object CoqOutput {

  // Path to SCTPTP.v
  def pathToSCTPTPCoq(): String = {
    "path/to/SCTPTP.v"
  }

  def contextPreamble(): String = {
    "Add LoadPath \""
      + pathToSCTPTPCoq()
      + "\" as SCTPTP.\n"
      + "Require Import SCTPTP.sctptp.\n"
      + "Parameter sctptp_U : Set. (* universe *)\n"
      + "Parameter sctptp_I :  sctptp_U. (* an individual in the universe. *)\n\n"
  }

  // Todo : retrieve variables and predicates

  case class CoqProofStep(step: SCProofStep) {
    def makeAlphaStep(ruleName: String, i: Int, indexMax: Int): String = {
      s"apply ${ruleName} H${i}. intros ${Seq.range(i + 1, i + indexMax + 1).foldLeft("", i + 1)((acc, e) => (acc._1 + "H" + e.toString() + (if (acc._2 != i + indexMax) then " " else ""), acc._2 + 1))._1}."
    }

    def makeBetaStep(ruleName: String, i: Int, indexMax: Int): String = {
      s"apply ${ruleName} H${i}; [${Seq
          .range(i + 1, i + indexMax + 1)
          .foldLeft("", i + 1)((acc, e) => (acc._1 + "intros H" + e.toString() + (if (acc._2 != i + indexMax) then " | " else ""), acc._2 + 1))
          ._1}]."
    }

    def makeDeltaStep(ruleName: String, i: Int, indexMax: Int): String = {
      s"apply ${ruleName} H${i}; [${Seq.range(i + 1, i + indexMax + 1).foldLeft("", i + 1)((acc, e) => (acc._1 + "intros H" + e.toString() + (if (acc._2 != i + indexMax) then " | " else ""), acc._2 + 1))._1}]."
    }
    // "apply %s. intros %s. apply NNPP. intros %s." 

    override def toString(): String = step match {
      case Axiom(name, bot) => ""
      case Hyp(name, bot, i, j) => s"[${name}] " + "auto."
      case LeftHyp(name, bot, i, j) => s"[${name}] " + "auto."
      case LeftWeakening(name, bot, i, t1) => s"[${name}] " + s"clear H${i}."
      case RightWeakening(name, bot, i, t1) => s"[${name}] " + s"clear ${bot.right(i).toString()}."
      case Cut(name, bot, i, j, t1, t2) => ""

      case LeftAnd(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(LeftAndRuleName, i, 2)
      case LeftOr(name, bot, i, t1, t2) => s"[${name}] " + makeBetaStep(LeftOrRuleName, i, 2)
      case LeftImp1(name, bot, i, t1, t2) => s"[${name}] " + makeBetaStep(LeftImp1RuleName, i, 2)
      case LeftImp2(name, bot, i, t1, t2) => s"[${name}] " + makeBetaStep(LeftImp2RuleName, i, 2)
      case LeftIff(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(LeftIffRuleName, i, 1)
      case LeftNot(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(LeftNotRuleName, i, 1)
      case LeftEx(name, bot, i, y, t1) => ""
      case LeftAll(name, bot, i, t, t1) => ""

      case RightAnd(name, bot, i, t1, t2) => s"[${name}] " + makeBetaStep(RightAndRuleName, i, 2)
      case RightOr(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(RightOrRuleName, i, 1)
      case RightImp(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(RightImpRuleName, i, 1)
      case RightIff(name, bot, i, t1, t2) => s"[${name}] " + makeBetaStep(RightIffRuleName, i, 2)
      case RightNot(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(RightNotRuleName, i, 1)
      case RightEx(name, bot, i, y, t1) => ""
      case RightAll(name, bot, i, t, t1) => ""

      case LeftNotAnd(name, bot, i, t1, t2) => s"[${name}] " + makeBetaStep(LeftNotAndRuleName, i, 2)
      case LeftNotOr(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(LeftNotOrRuleName, i, 1)
      case LeftNotImp(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(LeftNotImpRuleName, i, 1)
      case LeftNotIff(name, bot, i, t1, t2) => s"[${name}] " + makeBetaStep(LeftNotIffRuleName, i, 2)
      case LeftNotNot(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(LeftNotNotRuleName, i, 1)
      case LeftNotEx(name, bot, i, y, t1) => ""
      case LeftNotAll(name, bot, i, t, t1) => ""

      case _ => ""
    }

    // rightnot = intro.
    // rightAnd, rightImp = intro.
  }


  case class CoqProof(ScProof: SCProof[?]) {

    // def makeAxiomn

    def makeTheorem(FirstStep: SCProofStep): String =
      s"Theorem ${ FirstStep.name} ${FirstStep.bot.toString()}"

    def toCoqSteps(ScProof: SCProof[?]): IndexedSeq[CoqProofStep] = {
      ScProof.steps.foldLeft(IndexedSeq())((acc, e) => acc :+ CoqProofStep(e))
    }

    override def toString(): String = contextPreamble() + makeTheorem(ScProof(ScProof.steps.length-1)) + "\nProof." + toCoqSteps(ScProof).foldLeft("")((acc, e) =>  e.toString() + "\n" +  acc) + "\nQed."
  }

// companion object - constructeur

//   case class CoqMapConnector
//   btps.AndConn:        "/\\",
// 		btps.OrConn:         "\\/",
// 		btps.ImpConn:        "->",
// 		btps.EquConn:        "<->",
// 		btps.NotConn:        "~",
// 		btps.TopType:        "True",
// 		btps.BotType:        "False",
// 		btps.AllQuant:       "forall",
// 		btps.ExQuant:        "exists",
// 		btps.AllTypeQuant:   "forall",
// 		btps.QuantVarOpen:   "(",
// 		btps.QuantVarClose:  ")",
// 		btps.QuantVarSep:    ",",
// 		btps.PredEmpty:      "",
// 		btps.PredTypeVarSep: ",",
// 		btps.TypeVarType:    "Type",

}
