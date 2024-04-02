package sctptp

import FOL.* 
import SequentCalculus.*

object LVL2 {
  sealed trait StrictLVL2ProofStep extends SCProofStep
  type LVL2ProofStep = StrictLVL2ProofStep | LVL1ProofStep

  case class LVL2Proof(steps: IndexedSeq[LVL2ProofStep]) extends SCProof[LVL2ProofStep] 

  /**
   *   Γ |- P[x:=t], Δ
   * ----------------
   *   Γ, t = u |- P[x:=u], Δ
   *
   * @param bot Resulting formula
   * @param i Index of t = u on the left
   * @param j Index of P(t) on the right
   * @param P Shape of the formula in which the substitution occurs
   * @param x Variable indicating where in P the substitution occurs
   */
  case class RightSubstMulti(name: String, bot: Sequent, is: List[(Int, Boolean)], j: Int, P: Formula, xs: List[VariableLabel], t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubstMany(name, "rightSubstMulti", bot, is, j, P.toString(), xs.map(_.toString()), premises)
  }

  /**
   * -----------------
   *   Γ, A |- A, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   * @param j Index of A on the right
   */
  case class Congruence(name: String, bot: Sequent) extends StrictLVL2ProofStep {
    val premises = Seq()
    override def toString: String = SCProofStep.outputNIndexes(name, "congruence", bot, Seq(), Seq())
  }


}
