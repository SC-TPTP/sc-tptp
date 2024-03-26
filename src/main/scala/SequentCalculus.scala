
import FOL.*

object SequentCalculus {
  
  case class Sequent(left: Seq[Formula], right: Seq[Formula])

  sealed trait SCProofStep {
    val name: String
    val bot: Sequent
    val premises: Seq[String]
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
  case class Axiom(name: String, bot:Sequent) extends SCProofStep {
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
  case class Hyp(name: String, bot:Sequent, i:Int, j:Int) extends SCProofStep {
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
  case class LeftHyp(name: String, bot:Sequent, i:Int, j:Int) extends SCProofStep {
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
  case class LeftWeakening(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }


  /**
    *    Γ |- Δ
    * -------------
    *   Γ |- A, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A on the right
    */
  case class RightWeakening(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
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
  case class Cut(name: String, bot:Sequent, i:Int, j:Int, t1:String, t2:String) extends SCProofStep {
    val premises = Seq(t1, t2)
  }


  /**
    *    Γ, A, B |- Δ
    * ----------------
    *   Γ, A ∧ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ∧ B on the left
    */  
  case class LeftAnd(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }


  /**
    *    Γ, A |- Δ    Γ, B |- Δ
    * -------------------------
    *          Γ, A ∨ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ∨ B on the left
    */
  case class LeftOr(name: String, bot:Sequent, i:Int, t1:String, t2:String) extends SCProofStep {
    val premises = Seq(t1, t2)
  }


  /**
    *    Γ |- B, Δ    Γ, A |- Δ
    * -----------------------------------
    *         Γ, A ⇒ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇒ B on the left
    */
  case class LeftImp1(name: String, bot:Sequent, i:Int, t1:String, t2:String) extends SCProofStep {
    val premises = Seq(t1, t2)
  }


  /**
    *    Γ, A |-  Δ     Γ ¬B |-  Δ
    * -------------------------------
    *          Γ, A ⇒ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇒ B on the left
    */
  case class LeftImp2(name: String, bot:Sequent, i:Int, t1:String, t2:String) extends SCProofStep {
    val premises = Seq(t1, t2)
  }


  /**
    *    Γ, A ⇒ B, B ⇒ A |- Δ 
    * -----------------------------------
    *   Γ, A ⇔ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇔ B on the left
    */
  case class LeftIff(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }


  /**
    *    Γ |- A, Δ
    * ----------------
    *   Γ, ¬A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬A on the left
    */
  case class LeftNot(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
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
  case class LeftEx(name: String, bot:Sequent, i:Int, y:VariableLabel, t1:String) extends SCProofStep {
    val premises = Seq(t1)
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
  case class LeftAll(name: String, bot:Sequent, i:Int, t:Term, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }


  /**
    *    Γ |- A, Δ      Γ |- A, Δ
    * ------------------------------
    *        Γ |- A ∧ B, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ∧ B on the right
    */
  case class RightAnd(name: String, bot:Sequent, i:Int, t1:String, t2:String) extends SCProofStep {
    val premises = Seq(t1, t2)
  }


  /**
    *    Γ |- A, B Δ  
    * ----------------
    *  Γ |- A ∨ B, Δ
    * @param bot Resulting formula
    * @param i Index of A ∨ B on the right
    */
  case class RightOr(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }


  /**
    *    Γ, A |- B, Δ
    * ----------------
    *   Γ |- A ⇒ B, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇒ B on the right
    */
  case class RightImp(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }


  /**
    *    Γ |- A ⇒ B, Δ     Γ |- B ⇒ A, Δ
    * ------------------------------------
    *              Γ |- A ⇔ B, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇔ B on the right
    */
  case class RightIff(name: String, bot:Sequent, i:Int, t1:String, t2:String) extends SCProofStep {
    val premises = Seq(t1, t2)
  }


  /**
    *    Γ, A |- Δ
    * ----------------
    *   Γ |- ¬A, Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬A on the right
    */
  case class RightNot(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
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
  case class RightEx(name: String, bot:Sequent, i:Int, t:Term, t1:String) extends SCProofStep {
    val premises = Seq(t1)
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
  case class RightAll(name: String, bot:Sequent, i:Int, y:VariableLabel, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }


  /**
    *    Γ, ¬A |- Δ     Γ, ¬B |- Δ
    * -------------------------------
    *     Γ, ¬(A ∧ B), Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ∧ B) on the left
    */
  case class LeftNotAnd(name: String, bot:Sequent, i:Int, t1:String, t2:String) extends SCProofStep {
    val premises = Seq(t1, t2)
  }


  /**
    *    Γ, ¬A, ¬B |- Δ
    * ----------------
    *   Γ, ¬(A ∨ B) |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ∨ B) on the left
    */
  case class LeftNotOr(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }


  /**
    *    Γ, A, ¬B |- Δ
    * ----------------
    *   Γ, ¬(A ⇒ B) |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ⇒ B) on the left
    */
  case class LeftNotImp(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }


  /**
    *    Γ, ¬(A ⇒ B)  |- Δ     Γ, ¬(B ⇒ A)  |- Δ
    * ---------------------------------------------
    *               Γ, ¬(A ⇔ B) |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ⇔ B) on the left
    */
  case class LeftNotIff(name: String, bot:Sequent, i:Int, t1:String, t2:String) extends SCProofStep {
    val premises = Seq(t1, t2)
  }


  /**
    *    Γ, A |- Δ
    * ----------------
    *   Γ, ¬¬A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬¬A on the left
    */
  case class LeftNotNot(name: String, bot:Sequent, i:Int, t1:String) extends SCProofStep {
    val premises = Seq(t1)
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
  case class LeftNotEx(name: String, bot:Sequent, i:Int, t:Term, t1:String) extends SCProofStep {
    val premises = Seq(t1)
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
  case class LeftNotAll(name: String, bot:Sequent, i:Int, y:VariableLabel, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }


  /**
    * ----------------
    *   Γ |- t = t, Δ
    *
    * @param bot Resulting formula
    * @param i Index of t = t on the right
    */
  case class RightRefl(name: String, bot:Sequent, i:Int) extends SCProofStep {
    val premises = Seq()
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
  case class LeftSubst(name: String, bot:Sequent, i:Int, j:Int, P:Formula, x:VariableLabel, t1:String) extends SCProofStep {
    val premises = Seq(t1)
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
  case class RightSubst(name: String, bot:Sequent, i:Int, j:Int, P:Formula, x:VariableLabel, t1:String) extends SCProofStep {
    val premises = Seq(t1)
  }

}
