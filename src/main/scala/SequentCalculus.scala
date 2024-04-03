package sctptp

import FOL.*
import scala.compiletime.ops.int
import SequentCalculus.RulesName.*

object SequentCalculus {

    /**
   * Rule's name
   */

  object RulesName extends Enumeration {
    type RulesName = Value
    val HypRuleName = "hyp"
    val LeftHypRuleName = "leftHyp"
    val LeftWeakeningRuleName = "leftWeaken"
    val RightWeakeningRuleName = "rightWeaken"
    val CutRuleName = "cut"
    val LeftAndRuleName = "leftAnd"
    val LeftOrRuleName = "leftOr"
    val LeftImp1RuleName = "leftImp1"
    val LeftImp2RuleName = "leftImp2"
    val LeftIffRuleName = "leftIff"
    val LeftNotRuleName = "leftNot"
    val LeftExRuleName = "leftEx"
    val LeftAllRuleName = "leftAll"
    val RightAndRuleName = "rightAnd"
    val RightOrRuleName = "rightOr"
    val RightImpRuleName = "rightImp"
    val RightIffRuleName = "rightIff"
    val RightNotRuleName = "rightNot"
    val RightExRuleName = "rightEx"
    val RightAllRuleName = "rightAll"
    val LeftNotAndRuleName = "leftNotAnd"
    val LeftNotOrRuleName = "leftNotOr"
    val LeftNotImpRuleName = "leftNotImp"
    val LeftNotIffRuleName = "leftNotIff"
    val LeftNotNotRuleName = "leftNotNot"
    val LeftNotExRuleName = "leftNotEx"
    val LeftNotAllRuleName = "leftNotAll"
    val RightReflRuleName = "rightRefl"
    val LeftSubstRuleName = "leftSubst"
    val RightSubstRuleName = "rightSubst"
  }

  case class Sequent(left: Seq[Formula], right: Seq[Formula]) {
    override def toString: String =
      s"[${left.mkString(",")}] --> [${right.mkString(",")}]"
  }

  trait SCProofStep {
    val name: String
    val bot: Sequent
    val premises: Seq[String]
  }

  object SCProofStep {
    def outputNIndexes(name: String, rule: String, bot: Sequent, indexes: Seq[Int], premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [${indexes.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != indexes.length - 1) then ", " else ""), acc._2 + 1))._1}], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"
    def outputSingleIndex(name: String, rule: String, bot: Sequent, i: Int, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [${i}], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"
    
    def outputDoubleIndexes(name: String, rule: String, bot: Sequent, i: Int, j: Int, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [${i}, ${j}], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"

    def outputWithTerm(name: String, rule: String, bot: Sequent, i: Int, term: String, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [${i}, $$fot(${term})], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"

    def outputWithSubst(name: String, rule: String, bot: Sequent, i: Int, j: Int, term: String, subterm: String, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [${i}, ${j}, $$fof(${term})) $$fot(${subterm})], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"

    def outputWithSubstMany(name: String, rule: String, bot: Sequent, is: List[(Int, Boolean)], j: Int, term: String, subterms: List[String], premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [${is}, ${j}, $$fof(${term}), [${subterms.map(st => s"$$fot(${st})").mkString(",")}]], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"


  }

  trait SCProof[Steps<:SCProofStep] {
    val thmName: String
    val steps: IndexedSeq[Steps]

    /**
     * Fetches the <code>i</code>th step of the proof.
     * @param i the index
     * @return a step
     */
    def apply(i: Int): Steps =
      if (i >= 0 && i < steps.length) then steps(i)
      else throw new IndexOutOfBoundsException(s"index $i is out of bounds of the steps Seq")

    /**
     * Get the ith sequent of the proof. If the index is positive, give the bottom sequent of proof step number i.
     * If the index is negative, return the <code>(-i-1)</code>th imported sequent.
     *
     * @param i The reference number of a sequent in the proof
     * @return A sequent, either imported or reached during the proof.
     */
    def getSequent(i: Int): Sequent =
      apply(i).bot

    /**
     * The length of the proof in terms of top-level steps, without including the imports.
     */
    def length: Int = steps.length

    /**
     * The conclusion of the proof, namely the bottom sequent of the last proof step.
     * Throw an error if the proof is empty.
     */
    def conclusion: Sequent = {
      if steps.isEmpty then throw new NoSuchElementException("conclusion of an empty proof")
      else this.getSequent(length - 1)
    }

    override def toString(): String = steps.foldLeft("")((acc, e) => acc + "\n" + e.toString())
  }


  sealed trait LVL1ProofStep extends SCProofStep

  case class LVL1Proof(steps: IndexedSeq[LVL1ProofStep], thmName: String) extends SCProof[LVL1ProofStep] 


  /**
   * --------------
   *    Γ |- Δ
   *
   * @param bot Resulting formula
   */
  case class Axiom(name: String, bot: Sequent) extends LVL1ProofStep {
    val premises = Seq()
    override def toString: String =
      s"fof(${name}, axiom, ${bot})"
  }

  /**
   * -----------------
   *   Γ, A |- A, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   * @param j Index of A on the right
   */
  case class Hyp(name: String, bot: Sequent, i: Int, j: Int) extends LVL1ProofStep {
    val premises = Seq()
    override def toString: String = SCProofStep.outputDoubleIndexes(name, HypRuleName, bot, i, j, premises)
  }

  /**
   * -------------------
   *   Γ, A, ¬A, |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   * @param j Index of ¬A on the left
   */
  case class LeftHyp(name: String, bot: Sequent, i: Int, j: Int) extends LVL1ProofStep {
    val premises = Seq()
    override def toString: String = SCProofStep.outputDoubleIndexes(name, LeftHypRuleName, bot, i, j, premises)
  }

  /**
   *    Γ |- Δ
   * -------------
   *   Γ, A |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   */
  case class LeftWeakening(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftWeakeningRuleName, bot, i, premises)
  }

  /**
   *    Γ |- Δ
   * -------------
   *   Γ |- A, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A on the right
   */
  case class RightWeakening(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, RightWeakeningRuleName, bot, i, premises)
  }

  /**
   *    Γ |- A, Δ    Γ, A |- Δ
   * -------------------------
   *          Γ |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of cut on the right of first premise
   * @param j Index of cut on the left of second premise
   */
  case class Cut(name: String, bot: Sequent, i: Int, j: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputDoubleIndexes(name, CutRuleName, bot, i, j, premises)
  }

  /**
   *    Γ, A, B |- Δ
   * ----------------
   *   Γ, A ∧ B |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ∧ B on the left
   */
  case class LeftAnd(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftAndRuleName, bot, i, premises)
  }

  /**
   *    Γ, A |- Δ    Γ, B |- Δ
   * -------------------------
   *          Γ, A ∨ B |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ∨ B on the left
   */
  case class LeftOr(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftOrRuleName, bot, i, premises)
  }

  /**
   *    Γ |- B, Δ    Γ, A |- Δ
   * -----------------------------------
   *         Γ, A ⇒ B |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ⇒ B on the left
   */
  case class LeftImp1(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftImp1RuleName, bot, i, premises)
  }

  /**
   *    Γ, A |-  Δ     Γ ¬B |-  Δ
   * -------------------------------
   *          Γ, A ⇒ B |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ⇒ B on the left
   */
  case class LeftImp2(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftImp2RuleName, bot, i, premises)
  }

  /**
   *    Γ, A ⇒ B, B ⇒ A |- Δ
   * -----------------------------------
   *   Γ, A ⇔ B |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ⇔ B on the left
   */
  case class LeftIff(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftIffRuleName, bot, i, premises)
  }

  /**
   *    Γ |- A, Δ
   * ----------------
   *   Γ, ¬A |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬A on the left
   */
  case class LeftNot(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotRuleName, bot, i, premises)
  }

  /**
   *    Γ, A[x:=y] |- Δ
   * ---------------- Where y is not free in Γ and Δ
   *   Γ, ∃x. A |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ∃x. A on the left
   * @param y Variable in place of x in the premise
   */
  case class LeftEx(name: String, bot: Sequent, i: Int, y: VariableLabel, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftExRuleName, bot, i, y.toString(), premises)
  }

  /**
   *    Γ, A[x:=t] |- Δ
   * ----------------
   *   Γ, ∀x. A |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ∀x. A on the left
   * @param t Term in in place of x in the premise
   */
  case class LeftAll(name: String, bot: Sequent, i: Int, t: Term, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftAllRuleName, bot, i, t.toString(), premises)
  }

  /**
   *    Γ |- A, Δ      Γ |- A, Δ
   * ------------------------------
   *        Γ |- A ∧ B, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ∧ B on the right
   */
  case class RightAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name,RightAndRuleName, bot, i, premises)
  }

  /**
   *    Γ |- A, B Δ
   * ----------------
   *  Γ |- A ∨ B, Δ
   * @param bot Resulting formula
   * @param i Index of A ∨ B on the right
   */
  case class RightOr(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, RightOrRuleName, bot, i, premises)
  }

  /**
   *    Γ, A |- B, Δ
   * ----------------
   *   Γ |- A ⇒ B, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ⇒ B on the right
   */
  case class RightImp(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, RightImpRuleName, bot, i, premises)
  }

  /**
   *    Γ |- A ⇒ B, Δ     Γ |- B ⇒ A, Δ
   * ------------------------------------
   *              Γ |- A ⇔ B, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ⇔ B on the right
   */
  case class RightIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, RightIffRuleName, bot, i, premises)
  }

  /**
   *    Γ, A |- Δ
   * ----------------
   *   Γ |- ¬A, Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬A on the right
   */
  case class RightNot(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, RightNotRuleName, bot, i, premises)
  }

  /**
   *    Γ |- A[x:=t], Δ
   * -------------------
   *     Γ |- ∃x. A, Δ
   *
   * @param bot Resulting formula
   * @param i Index of ∃x. A on the right
   * @param t Term in place of x in the premise
   */
  case class RightEx(name: String, bot: Sequent, i: Int, t: Term, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, RightExRuleName, bot, i, t.toString(), premises)
  }

  /**
   *    Γ |- A[x:=y], Δ
   * -------------------
   *     Γ |- ∀x. A, Δ
   *
   * @param bot Resulting formula
   * @param i Index of ∀x. A on the right
   * @param y Variable in place of x in the premise
   */
  case class RightAll(name: String, bot: Sequent, i: Int, y: VariableLabel, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, RightAllRuleName, bot, i, y.toString(), premises)
  }

  /**
   *    Γ, ¬A |- Δ     Γ, ¬B |- Δ
   * -------------------------------
   *     Γ, ¬(A ∧ B), Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬(A ∧ B) on the left
   */
  case class LeftNotAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotAndRuleName, bot, i, premises)
}

  /**
   *    Γ, ¬A, ¬B |- Δ
   * ----------------
   *   Γ, ¬(A ∨ B) |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬(A ∨ B) on the left
   */
  case class LeftNotOr(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotOrRuleName, bot, i, premises)
  }

  /**
   *    Γ, A, ¬B |- Δ
   * ----------------
   *   Γ, ¬(A ⇒ B) |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬(A ⇒ B) on the left
   */
  case class LeftNotImp(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotImpRuleName, bot, i, premises)
  }

  /**
   *    Γ, ¬(A ⇒ B)  |- Δ     Γ, ¬(B ⇒ A)  |- Δ
   * ---------------------------------------------
   *               Γ, ¬(A ⇔ B) |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬(A ⇔ B) on the left
   */
  case class LeftNotIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotIffRuleName, bot, i, premises)
  }

  /**
   *    Γ, A |- Δ
   * ----------------
   *   Γ, ¬¬A |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬¬A on the left
   */
  case class LeftNotNot(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotNotRuleName, bot, i, premises)
  }

  /**
   *    Γ, ¬A[x:=t] |- Δ
   * ----------------
   *   Γ, ¬∃x. A |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬∃x. A on the left
   * @param t Term in place of x in the premise
   */
  case class LeftNotEx(name: String, bot: Sequent, i: Int, t: Term, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftNotExRuleName, bot, i, t.toString(), premises)
  }

  /**
   *    Γ, ¬A[x:=y] |- Δ
   * ----------------
   *   Γ, ¬∀x. A |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬∀x. A on the left
   * @param y Variable in place of x in the premise
   */
  case class LeftNotAll(name: String, bot: Sequent, i: Int, y: VariableLabel, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftNotAllRuleName, bot, i, y.toString(), premises)
  }

  /**
   * ----------------
   *   Γ |- t = t, Δ
   *
   * @param bot Resulting formula
   * @param i Index of t = t on the right
   */
  case class RightRefl(name: String, bot: Sequent, i: Int) extends LVL1ProofStep {
    val premises = Seq()
    override def toString: String = SCProofStep.outputSingleIndex(name, RightReflRuleName, bot, i, premises)
  }

  /**
   *   Γ, P[x:=t] |- Δ
   * ----------------
   *   Γ, t = u, P[x:=u] |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of t = u on the left
   * @param j Index of P(t) on the left
   * @param P Shape of the formula in which the substitution occurs
   * @param x Variable indicating where in P the substitution occurs
   */
  case class LeftSubst(name: String, bot: Sequent, i: Int, j: Int, P: Formula, x: VariableLabel, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubst(name, LeftSubstRuleName, bot, i, j, P.toString(), x.toString(), premises)
  }

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
  case class RightSubst(name: String, bot: Sequent, i: Int, j: Int, P: Formula, x: VariableLabel, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubst(name, RightSubstRuleName, bot, i, j, P.toString(), x.toString(), premises)
  }


}

