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
    val WeakeningRuleName = "Weakening"
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

    def checkCorrectness(premises: String => Sequent): Boolean
  }

  object SCProofStep {
    def outputNIndexes(name: String, rule: String, bot: Sequent, indexes: Seq[Int], premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), ${indexes.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != indexes.length - 1) then ", " else ""), acc._2 + 1))._1}], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"

    def outputSingleIndex(name: String, role: String, rule: String, bot: Sequent, i: Int, premises: Seq[String]): String = 
      s"fof(${name}, ${role}, ${bot}, inference(${rule}, [status(thm), ${i}], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"
    
    def outputDoubleIndexes(name: String, role: String, rule: String, bot: Sequent, i: Int, j: Int, premises: Seq[String]): String = 
      s"fof(${name}, ${role}, ${bot}, inference(${rule}, [status(thm), ${i}, ${j}], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"

    def outputWithTerm(name: String, rule: String, bot: Sequent, i: Int, term: String, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), ${i}, $$fot(${term})], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"

    def outputWithSubst(name: String, rule: String, bot: Sequent, i: Int, j: Int, term: String, subterm: String, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), ${i}, ${j}, $$fof(${term})) $$fot(${subterm})], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"

    def outputWithSubstMany(name: String, rule: String, bot: Sequent, is: List[Int], j: Int, term: String, subterms: List[String], premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), [${is.mkString(", ")}], ${j}, $$fof(${term}), [${subterms.map(st => s"$$fot(${st})").mkString(",")}]], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))"


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
  def checkProof[Steps<:SCProofStep](p: SCProof[Steps]): Boolean = {
    val steps = p.steps
    val premises: scala.collection.mutable.Map[String, Sequent] = scala.collection.mutable.Map()
    steps.forall(step =>
      premises.update(step.name, step.bot)
      step.checkCorrectness(premises)
    )
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
    def checkCorrectness(premises: String => Sequent): Boolean = true
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
    override def toString: String = SCProofStep.outputDoubleIndexes(name, HypRuleName, "assumption", bot, i, j, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = bot.left(i) == bot.left(j)
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
    override def toString: String = SCProofStep.outputDoubleIndexes(name, LeftHypRuleName, "assumption", bot, i, j, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = bot.left(j) == ~bot.left(i)
  }

  /**
   *    Γ1 |- Δ1
   * -------------
   *   Γ1, Γ2 |- Δ1, Δ2
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   */
  case class Weakening(name: String, bot: Sequent, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputNIndexes(name, WeakeningRuleName, bot, List(), Seq(t1))
    def checkCorrectness(premises: String => Sequent): Boolean = 
      isSubset(premises(t1).left, bot.left) && isSubset(premises(t1).right, bot.right)
      
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
    override def toString: String = SCProofStep.outputDoubleIndexes(name, CutRuleName, "plain", bot, i, j, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      val A = premises(t1).right(i)
      isSameSet(bot.left, premises(t1).left) && 
      isSameSet(A +: bot.right, premises(t1).right) &&
      isSameSet(A +: bot.left, premises(t2).left) && 
      isSameSet(bot.right, premises(t2).right)
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
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftAndRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case ConnectorFormula(And, Seq(a, b)) =>
          isSameSet(a +: b +: bot.left, premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right)
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftOrRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case a_b @ ConnectorFormula(Or, Seq(a, b)) =>
          isSameSet(a +: bot.left, a_b +: premises(t1).left) &&
          isSameSet(b +: bot.left, a_b +: premises(t2).left) &&
          isSameSet(bot.right, premises(t1).right) &&
          isSameSet(bot.right, premises(t2).right)
        case _ => false
  }


  /**
   *    Γ |- A, Δ    Γ, B |- Δ
   * -----------------------------------
   *         Γ, A ⇒ B |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ⇒ B on the left
   */
  case class LeftImp1(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftImp1RuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case a_b @ ConnectorFormula(Implies, Seq(a, b)) =>
          isSameSet(bot.left, a_b +: premises(t1).left) &&
          isSameSet(a +: bot.right, premises(t1).right) &&
          isSameSet(b +: bot.left, a_b +: premises(t2).left) &&
          isSameSet(bot.right, premises(t2).right)
        case _ => false
  }

  /**
   *    Γ, ¬A |-  Δ     Γ, B |-  Δ
   * -------------------------------
   *          Γ, A ⇒ B |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ⇒ B on the left
   */
  case class LeftImp2(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftImp2RuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case a_b @ ConnectorFormula(Implies, Seq(a, b)) =>
          isSameSet(~a +: bot.left, a_b +: premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right) &&
          isSameSet(b +: bot.left, a_b +: premises(t2).left) &&
          isSameSet(bot.right, premises(t2).right)
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftIffRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case a_b @ ConnectorFormula(Iff, Seq(a, b)) =>
          isSameSet((a ==> b) +: (b ==> a) +: bot.left, a_b +: premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right)
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case na @ ConnectorFormula(Neg, Seq(a)) => 
          isSameSet(bot.left, na +: premises(t1).left) &&
          isSameSet(a +: bot.right, premises(t1).right)
        case _ => false
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
  case class LeftEx(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftExRuleName, bot, i, y.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case ex @ BinderFormula(Exists, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> y()))
          isSameSet(inst +: bot.left, ex +:premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right) &&
          (bot.right ++ bot.left).forall(_.freeVariables.contains(y))
        case _ => false
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
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case all @ BinderFormula(Forall, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> t))
          isSameSet(inst +: bot.left, all +:premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right)
        case _ => false
  }

  /**
   *    Γ |- A, Δ      Γ |- A, Δ
   * ------------------------------
   *        Γ |- A ∧ B, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ∧ B on the right
   */
  case class RightAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep { //inverse of LeftOr
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name,RightAndRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.right(i) match
        case a_b @ ConnectorFormula(And, Seq(a, b)) => 
          isSameSet(bot.left, premises(t1).left) &&
          isSameSet(a +: bot.right, a_b +: premises(t1).right) &&
          isSameSet(bot.left, premises(t2).left) &&
          isSameSet(b +: bot.right, a_b +: premises(t2).right)
        case _ => false

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
    override def toString: String = SCProofStep.outputSingleIndex(name, RightOrRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.right(i) match
        case a_b @ ConnectorFormula(Or, Seq(a, b)) => 
          isSameSet(bot.left, premises(t1).left) &&
          isSameSet(a +: b +: bot.right, a_b +: premises(t1).right)
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, RightImpRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.right(i) match
        case a_b @ ConnectorFormula(Implies, Seq(a, b)) => 
          isSameSet(a +: bot.left, premises(t1).left) &&
          isSameSet(b +: bot.right, a_b +: premises(t1).right)
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, RightIffRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.right(i) match
        case a_b @ ConnectorFormula(Iff, Seq(a, b)) => 
          isSameSet(bot.left, premises(t1).left) &&
          isSameSet((a ==> b) +: bot.right, a_b +: premises(t1).right) &&
          isSameSet(bot.left, premises(t2).left) &&
          isSameSet((b ==> a) +: bot.right, a_b +: premises(t2).right)
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, RightNotRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.right(i) match
        case na @ ConnectorFormula(Neg, Seq(a)) => 
          isSameSet(a +: bot.left, premises(t1).left) &&
          isSameSet(bot.right, na +: premises(t1).right)
        case _ => false
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
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.right(i) match
        case ex @ BinderFormula(Exists, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> t))
          isSameSet(bot.left, inst +: premises(t1).left) &&
          isSameSet(bot.right, ex +: premises(t1).right)
        case _ => false
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
  case class RightAll(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, RightAllRuleName, bot, i, y.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.right(i) match
        case all @ BinderFormula(Forall, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> y()))
          isSameSet(bot.left, inst +: premises(t1).left) &&
          isSameSet(bot.right, all +: premises(t1).right)&&
          (bot.right ++ bot.left).forall(_.freeVariables.contains(y))
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotAndRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case na_b @ ConnectorFormula(Neg, Seq(ConnectorFormula(And, Seq(a, b)))) => 
          isSameSet(~a +: bot.left, na_b +: premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right) &&
          isSameSet(~b +: bot.left, na_b +: premises(t2).left) &&
          isSameSet(bot.right, premises(t2).right)
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotOrRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case na_b @ ConnectorFormula(Neg, Seq(ConnectorFormula(Or, Seq(a, b)))) => 
          isSameSet(~a +: ~b +: bot.left, na_b +: premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right)
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotImpRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case na_b @ ConnectorFormula(Neg, Seq(ConnectorFormula(Implies, Seq(a, b)))) => 
          isSameSet(a +: ~b +: bot.left, na_b +: premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right)
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotIffRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case na_b @ ConnectorFormula(Neg, Seq(ConnectorFormula(Iff, Seq(a, b)))) => 
          isSameSet(~(a ==> b) +: bot.left, na_b +: premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right) &&
          isSameSet(~(b ==> a) +: bot.left, na_b +: premises(t2).left) &&
          isSameSet(bot.right, premises(t2).right)
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotNotRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case na @ ConnectorFormula(Neg, Seq(ConnectorFormula(Neg, Seq(a)))) => 
          isSameSet(a+: bot.left, na +: premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right)
        case _ => false
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
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case nex @ ConnectorFormula(Neg, Seq(BinderFormula(Exists, x, a))) => 
          val inst = substituteVariablesInFormula(a, Map(x -> t))
          isSameSet(~inst +: bot.left, nex +: premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right)
        case _ => false
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
  case class LeftNotAll(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftNotAllRuleName, bot, i, y.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case nall @ ConnectorFormula(Neg, Seq(BinderFormula(Forall, x, a))) => 
          val inst = substituteVariablesInFormula(a, Map(x -> y()))
          isSameSet(~inst +: bot.left, nall +: premises(t1).left) &&
          isSameSet(bot.right, premises(t1).right) &&
          (bot.right ++ bot.left).forall(_.freeVariables.contains(y))
        case _ => false
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
    override def toString: String = SCProofStep.outputSingleIndex(name, RightReflRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.right(i) match
        case AtomicFormula(`equality`, Seq(t1, t2)) => t1 == t2
        case _ => false
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
  case class LeftSubst(name: String, bot: Sequent, i: Int, j: Int, P: Formula, x: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubst(name, LeftSubstRuleName, bot, i, j, P.toString(), x.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      val (t, u) = bot.right(i) match
        case AtomicFormula(`equality`, Seq(t1, t2)) => (t1, t2)
        case _ => return false
      val P_t = substituteVariablesInFormula(P, Map(x -> t))
      val P_u = substituteVariablesInFormula(P, Map(x -> u))
      isSameSet(P_t +: bot.left, P_u +: bot.right(i) +: premises(t1).left) &&
      isSameSet(bot.right, premises(t1).right)
      
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
  case class RightSubst(name: String, bot: Sequent, i: Int, j: Int, P: Formula, x: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubst(name, RightSubstRuleName, bot, i, j, P.toString(), x.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      val (t, u) = bot.left(i) match
        case AtomicFormula(`equality`, Seq(t1, t2)) => (t1, t2)
        case _ => return false
      val P_t = substituteVariablesInFormula(P, Map(x -> t))
      val P_u = substituteVariablesInFormula(P, Map(x -> u))
      isSameSet(bot.left, bot.left(i) +: premises(t1).left) &&
      isSameSet(P_t +: bot.right, P_u +: premises(t1).right)
  }




}

