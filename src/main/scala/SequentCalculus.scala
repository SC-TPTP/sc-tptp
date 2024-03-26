
import FOL.*

object SequentCalculus {
  
  case class Sequent(left: Seq[Formula], right: Seq[Formula])

  sealed trait SCProofStep {
      val bot: Sequent
      val premises: Seq[Int]
    }

  case class SCProof(steps: IndexedSeq[SCProofStep]) {

    /**
     * Fetches the <code>i</code>th step of the proof.
     * @param i the index
     * @return a step
     */
    def apply(i: Int): SCProofStep = 
      if (i >= 0 && i < steps.length) then
        steps(i)
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

  }



  /**
    * --------------
    *    Γ |- Δ
    *
    * @param bot Resulting formula
    */
  case class Axiom(bot:Sequent) extends SCProofStep {
    val premises = Seq()
  }


  /**
    * -----------------
    *   Γ, A |- A, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A on the left
    * @param j Index of A on the right
    */
  case class Hyp(bot:Sequent, i:Int, j:Int) extends SCProofStep {
    val premises = Seq()
  }


  /**
    * -------------------
    *   Γ, A, ¬A, |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A on the left
    * @param j Index of ¬A on the left
    */
  case class LeftHyp(bot:Sequent, i:Int, j:Int) extends SCProofStep {
    val premises = Seq()
  }


  /**
    *    Γ |- Δ
    * -------------
    *   Γ, A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A on the left
    */
  case class LeftWeakening(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- Δ
    * -------------
    *   Γ |- A, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A on the right
    */
  case class RightWeakening(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
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
  case class Cut(bot:Sequent, i:Int, j:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, A, B |- Δ
    * ----------------
    *   Γ, A ∧ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ∧ B on the left
    */  
  case class LeftAnd(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, A |- Δ    Γ, B |- Δ
    * -------------------------
    *          Γ, A ∨ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ∨ B on the left
    */
  case class LeftOr(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ |- B, Δ    Γ, A |- Δ
    * -----------------------------------
    *         Γ, A ⇒ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇒ B on the left
    */
  case class LeftImp1(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, A |-  Δ     Γ ¬B |-  Δ
    * -------------------------------
    *          Γ, A ⇒ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇒ B on the left
    */
  case class LeftImp2(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, A ⇒ B, B ⇒ A |- Δ 
    * -----------------------------------
    *   Γ, A ⇔ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇔ B on the left
    */
  case class LeftIff(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- A, Δ
    * ----------------
    *   Γ, ¬A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬A on the left
    */
  case class LeftNot(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
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
  case class LeftEx(bot:Sequent, i:Int, y:VariableLabel, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
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
  case class LeftAll(bot:Sequent, i:Int, t:Term, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- A, Δ      Γ |- A, Δ
    * ------------------------------
    *        Γ |- A ∧ B, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ∧ B on the right
    */
  case class RightAnd(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ |- A, B Δ  
    * ----------------
    *  Γ |- A ∨ B, Δ
    * @param bot Resulting formula
    * @param i Index of A ∨ B on the right
    */
  case class RightOr(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, A |- B, Δ
    * ----------------
    *   Γ |- A ⇒ B, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇒ B on the right
    */
  case class RightImp(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- A ⇒ B, Δ     Γ |- B ⇒ A, Δ
    * ------------------------------------
    *              Γ |- A ⇔ B, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇔ B on the right
    */
  case class RightIff(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, A |- Δ
    * ----------------
    *   Γ |- ¬A, Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬A on the right
    */
  case class RightNot(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
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
  case class RightEx(bot:Sequent, i:Int, t:Term, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
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
  case class RightAll(bot:Sequent, i:Int, y:VariableLabel, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, ¬A |- Δ     Γ, ¬B |- Δ
    * -------------------------------
    *     Γ, ¬(A ∧ B), Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ∧ B) on the left
    */
  case class LeftNotAnd(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, ¬A, ¬B |- Δ
    * ----------------
    *   Γ, ¬(A ∨ B) |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ∨ B) on the left
    */
  case class LeftNotOr(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, A, ¬B |- Δ
    * ----------------
    *   Γ, ¬(A ⇒ B) |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ⇒ B) on the left
    */
  case class LeftNotImp(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, ¬(A ⇒ B)  |- Δ     Γ, ¬(B ⇒ A)  |- Δ
    * ---------------------------------------------
    *               Γ, ¬(A ⇔ B) |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ⇔ B) on the left
    */
  case class LeftNotIff(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, A |- Δ
    * ----------------
    *   Γ, ¬¬A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬¬A on the left
    */
  case class LeftNotNot(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
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
  case class LeftNotEx(bot:Sequent, i:Int, t:Term, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
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
  case class LeftNotAll(bot:Sequent, i:Int, y:VariableLabel, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    * ----------------
    *   Γ |- t = t, Δ
    *
    * @param bot Resulting formula
    * @param i Index of t = t on the right
    */
  case class RightRefl(bot:Sequent, i:Int) extends SCProofStep {
    val premises = Seq()
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
  case class RightSubst(bot:Sequent, i:Int, j:Int, P:Formula, x:VariableLabel, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
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
  case class LeftSubst(bot:Sequent, i:Int, j:Int, P:Formula, x:VariableLabel, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }

}
