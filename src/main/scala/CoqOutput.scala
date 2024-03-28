import SequentCalculus.*
import SequentCalculus.RulesName.*

object CoqOutput {

    def toCoqProof(scProof: SCProof[?]): IndexedSeq[CoqProofStep] = {
        scProof.steps.foldLeft(IndexedSeq())((acc, e) =>  acc :+ CoqProofStep(e))
    }

    def toCoqProofStep(scProof: SCProof[?]): IndexedSeq[CoqProofStep] = {
        scProof.steps.foldLeft(IndexedSeq())((acc, e) =>  acc :+ CoqProofStep(e))
    }


  case class CoqContext() {
    // Path to SCTPTP.v
    def pathToSCTPTPCoq(): String = {
      "path/to/SCTPTP.v"
    }

    def contextPreamble(): String = {
      "Add LoadPath \""
        + pathToSCTPTPCoq()
        + "\" as Goeland.\n"
        + "Require Import Goeland.goeland.\n"
        + "Parameter goeland_U : Set. (* universe *)\n"
        + "Parameter goeland_I : goeland_U. (* an individual in the universe. *)\n\n"
    }

    // Todo : retrieve variables and predicates

  }

  case class CoqProofStep(step: SCProofStep) {
    def makeAlphaStep(ruleName: String, i : Int, indexMax: Int) : String = {
        s"apply ${ruleName} H${i}. intros ${Seq.range(i+1, i+indexMax+1).foldLeft("", 0)((acc, e) => (acc._1 + "H" + e.toString() + (if (acc._2 != i + indexMax) then " " else ""), acc._2 + 1))._1}."
    }

    def makeBetaStep(ruleName: String, i : Int, indexMax: Int) : String = {
        s"apply ${ruleName} H${i}; [${Seq.range(i+1, i+indexMax+1).foldLeft("", 0)((acc, e) => (acc._1 + "intros H" + e.toString() + (if (acc._2 != i + indexMax) then " | " else ""), acc._2 + 1))._1}]."
    }

    def makeDeltaStep(ruleName: String, i : Int, indexMax: Int) : String = {
        s"apply ${ruleName} H${i}; [${Seq.range(i+1, i+indexMax+1).foldLeft("", 0)((acc, e) => (acc._1 + "intros H" + e.toString() + (if (acc._2 != i + indexMax) then " | " else ""), acc._2 + 1))._1}]."
    }


    override def toString() : String = step match {
        case Axiom(name, bot) => ???
        case Hyp(name, bot, i, j) => s"[${name}] " + "auto."
        case LeftHyp(name, bot, i, j) => s"[${name}] " + "auto."
        case LeftWeakening(name, bot, i, t1) => s"[${name}] " + s"clear H${i}."
        case RightWeakening(name, bot, i, t1) => s"[${name}] " + s"clear ${bot.right(i).toString()}."
        // case Cut(name, bot, i, j, t1, t2) => ???
        case LeftAnd(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(LeftAndRuleName, i, 2)
        case LeftOr(name, bot, i, t1, t2) =>  s"[${name}] " + makeBetaStep(LeftOrRuleName, i, 2)
        case LeftImp1(name, bot, i, t1, t2) => s"[${name}] " + makeBetaStep(LeftImp1RuleName, i, 2)
        case LeftImp2(name, bot, i, t1, t2) => s"[${name}] " + makeBetaStep(LeftImp2RuleName, i, 2)
        case LeftIff(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(LeftIffRuleName, i, 1)
        case LeftNot(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(LeftNotRuleName, i, 1)
        case LeftNotImp(name, bot, i, t1) => s"[${name}] " + makeAlphaStep(LeftNotImpRuleName, i, 2)
        case _ => ""
    }
  }

  case class CoqProof(steps: IndexedSeq[SCProofStep]) {
    override def toString(): String = steps.foldLeft("")((acc, e) => acc + "\n" + e.toString())
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
