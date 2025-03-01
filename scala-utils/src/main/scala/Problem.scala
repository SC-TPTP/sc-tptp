package sctptp
import SequentCalculus.*
import FOL.*
import LVL2.*


case class Subproof(name: String, proof: SCProof[?], assumptions: Seq[Formula], axioms: Map[String, String]) extends StrictLVL2ProofStep {
  val bot = proof.steps.last.bot ++<< assumptions
  val premises = axioms.values.toSeq

  override def toString: String = {
    val axiomsString = axioms.map { case (k, v) => s"$k: $v" }.mkString(", ")
    val assumprionsString = assumptions.mkString(", ")
    s"fof($name, plain, $bot, inference(subproof, [$assumptions], {\n $proof\n}, [$axiomsString]))."
  }

  def checkCorrectness(premises: String => Sequent): Option[String] = {
    checkProof(proof).map(_._1)
  }
}

case class Problem(axioms: Seq[Axiom], conjecture: Conjecture) {
  override def toString: String = {
    val axiomsString = axioms.map(_.toString).mkString("\n")
    s"$axiomsString\n${conjecture}"
  }
  
}

