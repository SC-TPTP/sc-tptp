package sctptp

import FOL.*
import scala.compiletime.ops.int
import SequentCalculus.RulesName.*
import sctptp.LVL2.*

object SequentCalculus {

    /**
   * Rule's name
   */

  object RulesName extends Enumeration {
    type RulesName = Value
    val RightTrueRuleName = "rightTrue"
    val LeftFalseRuleName = "leftFalse"
    val HypRuleName = "hyp"
    val LeftWeakenRuleName = "leftWeaken"
    val LeftWeakenResRuleName = "leftWeakenRes"
    val RightWeakenRuleName = "rightWeaken"
    val CutRuleName = "cut"
    val LeftAndRuleName = "leftAnd"
    val LeftOrRuleName = "leftOr"
    val LeftImpliesRuleName = "leftImplies"
    val LeftIffRuleName = "leftIff"
    val LeftNotRuleName = "leftNot"
    val LeftExistsRuleName = "leftExists"
    val LeftForallRuleName = "leftForall"
    val RightAndRuleName = "rightAnd"
    val RightOrRuleName = "rightOr"
    val RightImpliesRuleName = "rightImplies"
    val RightIffRuleName = "rightIff"
    val RightNotRuleName = "rightNot"
    val RightExistsRuleName = "rightExists"
    val RightForallRuleName = "rightForall"
    val RightReflRuleName = "rightRefl"
    val LeftSubstRuleName = "leftSubst"
    val RightSubstRuleName = "rightSubst"
    val LeftSubstIffRuleName = "leftSubstIff"
    val RightSubstIffRuleName = "rightSubstIff"
    val InstFunRuleName = "instFun"
    val InstPredRuleName = "instPred"


    //move to level 2?

  }

  case class Sequent(left: Seq[Formula], right: Seq[Formula]) {
    override def toString: String =
      s"[${left.mkString(",")}] --> [${right.mkString(",")}]"
  }

  trait SCProofStep {
    val name: String
    val bot: Sequent
    val premises: Seq[String]

    def checkCorrectness(premises: String => Sequent): Option[String]
  }


  
  object SCProofStep {
    def outputNIndexes(name: String, rule: String, bot: Sequent, indexes: Seq[Int], premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), ${indexes.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != indexes.length - 1) then ", " else ""), acc._2 + 1))._1}], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputSingleIndex(name: String, role: String, rule: String, bot: Sequent, i: Int, premises: Seq[String]): String = 
      s"fof(${name}, ${role}, ${bot}, inference(${rule}, [status(thm), ${i}], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."
    
    def outputDoubleIndexes(name: String, role: String, rule: String, bot: Sequent, i: Int, j: Int, premises: Seq[String]): String = 
      s"fof(${name}, ${role}, ${bot}, inference(${rule}, [status(thm), ${i}, ${j}], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputWithTerm(name: String, rule: String, bot: Sequent, i: Int, term: String, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), ${i}, $$fot(${term})], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputWithSubst(name: String, rule: String, bot: Sequent, i: Int, term: String, subterm: String, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), ${i}, $$fof(${term}), $$fot(${subterm})], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputWithSubstIff(name: String, rule: String, bot: Sequent, i: Int, term: String, subterm: String, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), ${i}, $$fof(${term})) $$fof(${subterm})], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputWithSubstMany(name: String, rule: String, bot: Sequent, is: Seq[Int], term: String, subterms: Seq[String], premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), [${is.mkString(", ")}], $$fof(${term}), [${subterms.map(st => s"$$fot(${st})").mkString(",")}]], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputWithInstFun(name: String, rule: String, bot: Sequent, F: FunctionSymbol, t: (Term, Seq[VariableSymbol]), premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), $$fot(${F.toString()}), $$fot(${t._1.toString()}), [${t._2.map(st => s"$$fot(${st.toString()})").mkString(",")}]], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputWithInstPred(name: String, rule: String, bot: Sequent, P: AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), $$fof(${P.toString()}), $$fot(${phi._1.toString()}), [${phi._2.map(st => s"$$fot(${st.toString()})").mkString(",")}]], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

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

    def addStepLVL1After(scps: LVL1ProofStep): SCProof[?] = this
    def addStepsLVL1After(scps: Seq[LVL1ProofStep]): SCProof[?] = this
    def addStepLVL2After(scps: LVL2ProofStep): SCProof[?] = this
    def addStepsLVL2After(scps: Seq[LVL2ProofStep]): SCProof[?] = this
    def addStepLVL1Before(scps: LVL1ProofStep): SCProof[?] = this
    def addStepsLVL1Before(scps: Seq[LVL1ProofStep]): SCProof[?] = this
    def addStepLVL2Before(scps: LVL2ProofStep): SCProof[?] = this
    def addStepsLVL2Before(scps: Seq[LVL2ProofStep]): SCProof[?] = this


    override def toString(): String = steps.foldLeft("")((acc, e) => acc + "\n" + e.toString())
  }

  def checkProof[Steps<:SCProofStep](p: SCProof[Steps]): Option[(String, Steps)] = {
    val steps = p.steps
    val premises: scala.collection.mutable.Map[String, Sequent] = scala.collection.mutable.Map()
    def inner( steps: Iterator[Steps]): Option[(String, Steps)] =
      if steps.hasNext then
        val step = steps.next()
        step.checkCorrectness(premises) match
        case None =>
          premises.update(step.name, step.bot)
          inner(steps)
        case Some(msg) =>
          Some((s"${step.name}:  $msg", step))
      else
        None
    inner(steps.iterator)
  }


  sealed trait LVL1ProofStep extends SCProofStep

  case class LVL1Proof(steps: IndexedSeq[LVL1ProofStep], thmName: String) extends SCProof[LVL1ProofStep] {
    override def addStepLVL1After(scps: LVL1ProofStep): SCProof[LVL1ProofStep] = {
      LVL1Proof(steps :+ scps, thmName)
    }

    override def addStepsLVL1After(scps: Seq[LVL1ProofStep]): SCProof[LVL1ProofStep] = {
      LVL1Proof(scps.foldLeft(steps)((acc, x) => acc :+ x), thmName)
    }

    override def addStepLVL2After(scps: LVL2ProofStep): SCProof[LVL2ProofStep] = throw Exception("Cannot convert a LVL1 proof into a LVL2 proof")

    override def addStepsLVL2After(scps: Seq[LVL2ProofStep]): SCProof[LVL2ProofStep] = throw Exception("Cannot convert a LVL1 proof into a LVL2 proof")

    override def addStepLVL1Before(scps: LVL1ProofStep): SCProof[LVL1ProofStep] = {
      LVL1Proof(scps +: steps, thmName)
    }

    override def addStepsLVL1Before(scps: Seq[LVL1ProofStep]): SCProof[LVL1ProofStep] = {
      LVL1Proof(scps.foldLeft(steps)((acc, x) => x +: acc), thmName)
    }

    override def addStepLVL2Before(scps: LVL2ProofStep): SCProof[LVL2ProofStep] = throw Exception("Cannot convert a LVL1 proof into a LVL2 proof")

    override def addStepsLVL2Before(scps: Seq[LVL2ProofStep]): SCProof[LVL2ProofStep] = throw Exception("Cannot convert a LVL1 proof into a LVL2 proof")
  }


  private def contains(seq: Seq[Formula], f: Formula): Boolean = seq.contains(f)

  /**
   * --------------
   *    Γ |- Δ
   *
   * @param bot Resulting formula
   */
  case class Axiom(name: String, bot: Sequent) extends LVL1ProofStep {
    val premises = Seq()
    override def toString: String =
      s"fof(${name}, axiom, ${bot})."
    def checkCorrectness(premises: String => Sequent): Option[String] = None
  }

    /**
   * --------------
   *    Γ |- Δ
   *
   * @param bot Resulting formula
   */
  case class LeftFalse(name: String, bot: Sequent) extends LVL1ProofStep {
    val premises = Seq()
    override def toString: String =
      s"fof(${name}, assumption, [${"$"}false] --> [], inference(${LeftFalseRuleName}, [status(thm)], []))."
    def checkCorrectness(premises: String => Sequent): Option[String] = None
  }


  /**
   * -----------------
   *   Γ, A |- A, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   */
  case class Hyp(name: String, bot: Sequent, i: Int) extends LVL1ProofStep {
    val premises = Seq()
    override def toString: String = SCProofStep.outputSingleIndex(name, HypRuleName, "assumption", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val fi = bot.left(i)
      if bot.right.exists(isSame(_, fi)) then None else Some(s"${fi} is not in the right-hand side of the conclusion.")
  }

    /**
   * -----------------
   *   Γ, A |- A, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   */
  case class ElimIffRefl(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, "plain", "elimIffRefl", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = None
 
  }
  /**
   *    Γ |- Δ
   * -------------
   *   Γ, A |- Δ1
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   */
  case class LeftWeaken(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, "plain", LeftWeakenRuleName, bot, i, Seq(t1))
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val A = bot.left(i)
      if isSameSet(A +: premises(t1).left, bot.left) then
        if isSameSet(premises(t1).right, bot.right) then
          None
        else Some(s"Right-hand side of premise is not the same as the right-hand side of the conclusion.")
      else Some(s"Left-hand side of premise is not the same as the left-hand side of the conclusion.")
      
  }

    /**
   *    Γ |- Δ
   * -------------
   *   Γ, A |- Δ1
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   */
  case class LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, "plain", LeftWeakenResRuleName, bot, i, Seq(t1))
    def checkCorrectness(premises: String => Sequent): Option[String] = 

      def computeLit(f : Formula, index: Int): (Formula, Seq[Formula]) = {
        f match
          case AtomicFormula(_, _) => (f, Seq())
          case ConnectorFormula(label, args) => {
            label match
              case Neg =>  (f,  Seq())
              case Or => {
                val args_flat = LVL2.toFlatternOrSeq(f)
                (args_flat(index), args_flat.zipWithIndex.filter(_._2 != index).map(_._1))
              } 
              case _ => throw Exception(s"Resolution literal is not correct") 
          }
          case _ => throw Exception(s"Resolution literal is not correct") 
      }

      val A_Pair = computeLit(bot.left(0), i)  
      val A = A_Pair._1
      val new_bot = ConnectorFormula(Or, A +: bot.left)

      // println(s"A = ${A}")
      // println(s"new_bot = ${new_bot}")
      // println("----------------------")
      // println(s"premises(t1).left = ${premises(t1).left}")
      // println(s"bot.left = ${bot.left}")
      // println("----------------------")

      if isSameSet(Seq(new_bot), premises(t1).left) then
        if isSameSet(premises(t1).right, bot.right) then
          None
        else Some(s"Right-hand side of premise is not the same as the right-hand side of the conclusion.")
      else Some(s"Left-hand side of premise is not the same as the left-hand side of the conclusion.")
      
  }



   /**
   *    Γ |- Δ
   * -------------
   *   Γ |- Δ, A
   *
   * @param bot Resulting formula
   * @param i Index of A on the right
   */
  case class RightWeaken(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, "plain", RightWeakenRuleName, bot, i, Seq(t1))
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val A = bot.right(i)
      if isSameSet(A +: premises(t1).right, bot.right) then
        if isSameSet(premises(t1).left, bot.left) then
          None
        else Some(s"Left-hand side of premise is not the same as the left-hand side of the conclusion.")
      else Some(s"Right-hand side of premise is not the same as the right-hand side of the conclusion.")
      
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
    override def toString: String = SCProofStep.outputDoubleIndexes(name, "plain", CutRuleName, bot, i, j, premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val A = premises(t1).right(i)
      if (isSameSet(bot.left :+ A, premises(t1).left `concat` premises(t2).left) && (!contains(premises(t1).left, A) || contains(bot.left, A))) then
        if (isSameSet(bot.right :+ A, premises(t2).right `concat` premises(t1).right) && (!contains(premises(t2).right, A) || contains(bot.right, A))) then
          if (contains(premises(t2).left, A)) then
            if (contains(premises(t1).right, A)) then
              None
            else Some(s"Right-hand side of first premise does not contain A as claimed.")
          else Some(s"Left-hand side of second premise does not contain A as claimed.")
        else Some(s"Right-hand side of conclusion :+ A is not the union of the right-hand sides of the premises.")
      else Some(s"Left-hand side of conclusion :+ A is not the union of the left-hand sides of the premises.")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val AB = bot.left(i)
      AB match
        case ConnectorFormula(And, Seq(a, b)) =>
          if isSameSet(bot.right, premises(t1).right) then
            if isSameSet(bot.left :+ a :+ b, premises(t1).left :+ AB) then
              None
            else 
              println()
              println("bot + a + b: " + (bot.left :+ a :+ b))
              println("premises(t1).left + AB: " + (premises(t1).left :+ AB))
              Some("Left-hand side of premise :+ A∧B must be same as left-hand side of premise :+ A, B")
          else Some("Right-hand sides of the premise and the conclusion must be the same.")
        case _ => Some(s"$AB is not a conjunction")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val AB = bot.left(i)
      AB match
        case ConnectorFormula(Or, Seq(a, b)) =>
          if (isSameSet(bot.right, premises(t1).right `concat` premises(t2).right))
            if (isSameSet(bot.left :+ a :+ b, premises(t1).left `concat` premises(t2).left :+ AB))
              None
            else Some(s"Left-hand side of conclusion must be identical to `concat` of left-hand sides of premisces :+ A ∨ B.")
          else Some(s"Right-hand side of conclusion :+ A :+ B must be identical to `concat` of right-hand sides of premisces.")
        case _ => Some(s"$AB is not a disjunction")
  }


  /**
   *    Γ |- A, Δ    Γ, B |- Δ
   * -----------------------------------
   *         Γ, A ⇒ B |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ⇒ B on the left
   */
  case class LeftImplies(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftImpliesRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val AB = bot.left(i)
      AB match
        case ConnectorFormula(Implies, Seq(a, b)) =>
            if (isSameSet(bot.right :+ a, premises(t1).right `concat` premises(t2).right))
              if (isSameSet(bot.left :+ b, premises(t1).left `concat` premises(t2).left :+ AB))
                None
              else Some(s"Left-hand side of conclusion :+ B must be identical to `concat` of left-hand sides of premisces :+ A⇒B.")
            else Some(s"Right-hand side of conclusion :+ A must be identical to `concat` of right-hand sides of premisces.")
        case _ => Some(s"$AB is not an implication")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val AB = bot.left(i)
      bot.left(i) match
        case a_b @ ConnectorFormula(Iff, Seq(a, b)) =>
          val AimpB = ConnectorFormula(Implies, Seq(a, b))
          val BimpA = ConnectorFormula(Implies, Seq(b, a))
          if (isSameSet(premises(t1).right, bot.right))
            if isSameSet(bot.left :+ AimpB :+ BimpA, premises(t1).left :+ AB) then
              None
            else Some("Left-hand side of conclusion :+ A⇔B must be same as left-hand side of premise :+ either A⇒B, B⇒A or both.")
          else Some("Right-hand sides of premise and conclusion must be the same.")
        case _ => Some(s"$AB is not a biconditional")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val nA = bot.left(i)
      bot.left(i) match
        case ConnectorFormula(Neg, Seq(a)) => 
          if (isSameSet(bot.left, premises(t1).left :+ nA)) then
            if (isSameSet(bot.right :+ a, premises(t1).right)) then
              None
            else Some("Right-hand side of conclusion :+ A must be the same as right-hand side of premise")
          else Some("Left-hand side of conclusion must be the same as left-hand side of premise :+ ¬A")
        case _ => Some(s"$nA is not a negation")
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
  case class LeftExists(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftExistsRuleName, bot, i, y.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val eA = bot.left(i)
      bot.left(i) match
        case  BinderFormula(Exists, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> y()))
          if (isSameSet(bot.right, premises(t1).right)) then
            if (isSameSet(bot.left :+ inst, premises(t1).left :+ eA)) then
              if ((bot.left `concat` bot.right).forall(f => !f.freeVariables.contains(y))) then
                None
              else Some(s"The variable $y must not be free in the resulting sequent.")
            else Some("Left-hand side of conclusion :+ A must be the same as left-hand side of premise :+ ∃x. A")
          else Some("Right-hand side of conclusion must be the same as right-hand side of premise")
        case _ => Some(s"$eA is not an existential quantification")
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
  case class LeftForall(name: String, bot: Sequent, i: Int, t: Term, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftForallRuleName, bot, i, t.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val fa = bot.left(i)
      bot.left(i) match
        case all @ BinderFormula(Forall, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> t))
          if (isSameSet(bot.right, premises(t1).right))
            if (isSameSet(bot.left :+ inst, premises(t1).left :+ fa))
              None
            else Some("Left-hand side of conclusion :+ A[t/x] must be the same as left-hand side of premise :+ ∀x. A")
          else Some("Right-hand side of conclusion must be the same as right-hand side of premise")
        case _ => Some(s"$fa is not a universal quantification")
  }

  /**
   *    Γ |- A, Δ      Γ |- B, Δ
   * ------------------------------
   *        Γ |- A ∧ B, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ∧ B on the right
   */
  case class RightAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep { //inverse of LeftOr
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name,RightAndRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val AB = bot.right(i)
      AB match
        case ConnectorFormula(And, Seq(a, b)) =>
          if (isSameSet(bot.left, premises(t1).left `concat` premises(t2).left)) then
            if (
              isSubset(premises(t1).right, bot.right :+ a) &&
              isSubset(premises(t2).right, bot.right :+ b) &&
              isSubset(bot.right, premises(t1).right `concat` premises(t2).right :+ AB)
              ) then 
              None
            else Some(s"Right-hand side of conclusion + a + b is not the same as the union of the right-hand sides of the premises A, B.")
          else Some("Left-hand side of conclusion must be the same as left-hand side of premise")
        case _ => Some(s"$AB is not a conjunction")

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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val AB = bot.right(i)
      AB match
        case ConnectorFormula(Or, Seq(a, b)) =>
          if (isSameSet(bot.left, premises(t1).left))
            if (isSameSet(bot.right :+ a :+ b, premises(t1).right :+ AB))
              None
            else Some("Right-hand side of conclusion  :+ A, B must be the same as right-hand side of premise :+ A ∨ B")
          else Some("Left-hand side of conclusion must be the same as left-hand side of premise")
        case _ => Some(s"$AB is not a disjunction")
  }

  /**
   *    Γ, A |- B, Δ
   * ----------------
   *   Γ |- A ⇒ B, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A ⇒ B on the right
   */
  case class RightImplies(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, RightImpliesRuleName, "plain", bot, i, premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val AB = bot.right(i)
      AB match
        case ConnectorFormula(Implies, Seq(a, b)) =>
          if (isSameSet(bot.left :+ a, premises(t1).left))
            if (isSameSet(bot.right :+ b, premises(t1).right :+ AB))
              None
            else Some("Right-hand side of conclusion :+ A=>B must be the same as right-hand side of premise + B")
          else Some("Left-hand side of conclusion :+ A must be the same as left-hand side of premise")
        case _ => Some(s"$AB is not an implication")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val AB = bot.right(i)
      AB match
        case ConnectorFormula(Iff, Seq(a, b)) =>
          val AimpB = ConnectorFormula(Implies, Seq(a, b))
          val BimpA = ConnectorFormula(Implies, Seq(b, a))
          if (isSameSet(bot.left, premises(t1).left `concat` premises(t2).left))
            if (
              isSubset(premises(t1).right, bot.right :+ AimpB) &&
              isSubset(premises(t2).right, bot.right :+ BimpA) &&
              isSubset(bot.right, premises(t1).right `concat` premises(t2).right :+ AB)
              )
              None
            else Some(s"Right-hand side of conclusion + a⇒B + B⇒A is not the same as the union of the right-hand sides of the premises A⇔b.")
          else Some(s"Left-hand side of conclusion is not the union of the left-hand sides of the premises.")
        case _ => Some(s"$AB is not a biconditional")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val nA = bot.right(i)
      nA match
        case ConnectorFormula(Neg, Seq(a)) =>
          if (isSameSet(bot.left :+ a, premises(t1).left))
            if (isSameSet(bot.right, premises(t1).right :+ nA))
              None
            else Some("Right-hand side of conclusion :+ A must be the same as right-hand side of premise")
          else Some("Left-hand side of conclusion must be the same as left-hand side of premise :+ A")
        case _ => Some(s"$nA is not a negation")
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
  case class RightExists(name: String, bot: Sequent, i: Int, t: Term, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, RightExistsRuleName, bot, i, t.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val eA = bot.right(i)
      eA match
        case BinderFormula(Exists, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> t))
          if (isSameSet(bot.left, premises(t1).left))
            if (isSameSet(bot.right :+ inst, premises(t1).right :+ eA))
              None
            else Some("Right-hand side of conclusion :+ A must be the same as right-hand side of premise")
          else Some("Left-hand side of conclusion must be the same as left-hand side of premise :+ A")
        case _ => Some("$eA is not an existential")
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
  case class RightForall(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, RightForallRuleName, bot, i, y.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val fa = bot.right(i)
      fa match
        case BinderFormula(Forall, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> Variable(y)))
          if (isSameSet(bot.left, premises(t1).left))
            if (isSameSet(bot.right :+ inst, premises(t1).right :+ fa))
              if ((bot.left `concat` bot.right).forall(f => !f.freeVariables.contains(y))) then
                None
              else Some(s"The variable $y must not be free in the resulting sequent.")
            else Some("Right-hand side of conclusion :+ A must be the same as right-hand side of premise :+ A")
          else Some("Left-hand side of conclusion must be the same as left-hand side of premise")
        case _ => Some("$fa is not a universal")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val A = bot.right(i)
      A match
        case AtomicFormula(`equality`, Seq(t1, t2)) => 
          if t1 == t2 then None else Some("t1 and t2 are not the same")
        case _ => Some("$A is not an equality")
  }

  /**
   *   Γ, P[x:=t] |- Δ
   * ----------------
   *   Γ, t = u, P[x:=u] |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of t = u on the left
   * @param P Shape of the formula in which the substitution occurs
   * @param x Variable indicating where in P the substitution occurs
   */
  case class LeftSubst(name: String, bot: Sequent, i: Int, P: Formula, x: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubst(name, LeftSubstRuleName, bot, i, P.toString(), x.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val (t, u) = bot.left(i) match
        case AtomicFormula(`equality`, Seq(t1, t2)) => (t1, t2)
        case _ => return Some(s"${bot.right(i)} is not an equality")
      val P_t = substituteVariablesInFormula(P, Map(x -> t))
      val P_u = substituteVariablesInFormula(P, Map(x -> u))
      if isSameSet(P_t +: bot.left, P_u +: bot.left(i) +: premises(t1).left) || isSameSet(P_u +: bot.left, P_t +: bot.left(i) +: premises(t1).left) then
        if isSameSet(bot.right, premises(t1).right) then
          None
        else Some("Right-hand side is not correct.")
      else Some("Left-hand side is not correct.")
      
  }

  /**
   *   Γ |- P[x:=t], Δ
   * ----------------
   *   Γ, t = u |- P[x:=u], Δ
   *
   * @param bot Resulting formula
   * @param i Index of t = u on the left
   * @param P Shape of the formula in which the substitution occurs
   * @param x Variable indicating where in P the substitution occurs
   */
  case class RightSubst(name: String, bot: Sequent, i: Int, P: Formula, x: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubstIff(name, RightSubstRuleName, bot, i, P.toString(), x.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val (t, u) = bot.left(i) match
        case AtomicFormula(`equality`, Seq(t1, t2)) => (t1, t2)
        case _ => return  Some(s"${bot.left(i)} is not an equality")
      val P_t = substituteVariablesInFormula(P, Map(x -> t))
      val P_u = substituteVariablesInFormula(P, Map(x -> u))
      if isSameSet(bot.left, bot.left(i) +: premises(t1).left) then
        if isSameSet(P_t +: bot.right, P_u +: premises(t1).right) || isSameSet(P_u +: bot.right, P_t +: premises(t1).right) then
          None
        else Some("Right-hand side is not correct.")
      else Some("Left-hand side is not correct.")
  }


  /**
   *   Γ, R[A:=A] |- Δ
   * ----------------
   *  Γ, A <=> B, R[A:=B] |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A <=> B on the left
   * @param R Shape of the formula in which the substitution occurs
   * @param A Formula Variable indicating where in P the substitution occurs
   */
  case class LeftSubstIff(name: String, bot: Sequent, i: Int, R: Formula, A: AtomicSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubstIff(name, LeftSubstIffRuleName, bot, i, R.toString(), A.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      if A.arity != 0 || !A.id.isUpper then return Some(s"$A is not a formula variable")
      val (phi, psi) = bot.left(i) match
        case ConnectorFormula(Iff, Seq(phi, psi)) => (phi, psi)
        case _ => return Some(s"${bot.right(i)} is not a biconditional")
      val R_phi = substituteAtomicsInFormula(R, Map(A -> phi))
      val R_psi = substituteAtomicsInFormula(R, Map(A -> psi))
      if isSameSet(R_phi +: bot.left, R_psi +: bot.left(i) +: premises(t1).left) || isSameSet(R_psi +: bot.left, R_phi +: bot.left(i) +: premises(t1).left) then
        if isSameSet(bot.right, premises(t1).right) then
          None
        else Some("Right-hand side is not correct.")
      else Some("Left-hand side is not correct.")
  }

  /**
   *   Γ |- R[A:=A], Δ
   * ----------------
   *  Γ, A <=> B |- R[A:=B], Δ
   *
   * @param bot Resulting formula
   * @param i Index of A <=> B on the left
   * @param R Shape of the formula in which the substitution occurs
   * @param A Formula Variable indicating where in P the substitution occurs
   */
  case class RightSubstIff(name: String, bot: Sequent, i: Int, R: Formula, A: AtomicSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubst(name, RightSubstIffRuleName, bot, i, R.toString(), A.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      if A.arity != 0 || !A.id.isUpper then return Some(s"$A is not a formula variable")
      val (phi, psi) = bot.left(i) match
        case ConnectorFormula(Iff, Seq(phi, psi)) => (phi, psi)
        case _ => return Some(s"${bot.left(i)} is not a biconditional")
      val R_phi = substituteAtomicsInFormula(R, Map(A -> phi))
      val R_psi = substituteAtomicsInFormula(R, Map(A -> psi))
      if isSameSet(bot.left, bot.left(i) +: premises(t1).left) then
        if isSameSet(R_phi +: bot.right, R_psi +: premises(t1).right) || isSameSet(R_psi +: bot.right, R_phi +: premises(t1).right) then
          None
        else Some("Right-hand side is not correct.")
      else Some("Left-hand side is not correct.")
  }

  // Γ [ F X ] ⊢ Δ [ F X ] Γ [ F X := t X ] ⊢ Δ [ F X := t X ] 

  /**
   *   Γ[F(Xi)] |- Δ[F(Xi)]
   * -------------------------------------------------------------------------------
   *  Γ[F(Xi) := t(Xi)] |- Δ[F(Xi) := t(Xi)]
   *
   */
  case class InstFun(name: String, bot: Sequent, F: FunctionSymbol, t: (Term, Seq[VariableSymbol]), t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithInstFun(name, InstFunRuleName, bot, F, t, premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val fun = 
        if t._2.isEmpty then f => substituteVariablesInFormula(f, Map(VariableSymbol(F.id) -> t._1)) 
        else f => substituteFunctionsInFormula(f, Map(F -> t))
      val newleft = premises(t1).left.map(fun)
      val newright = premises(t1).right.map(fun)
      if isSameSet(newleft, bot.left) then
        if isSameSet(newright, bot.right) then
          None
        else Some("Right-hand side is not correct. Expected: " + newright)
      else Some("Left-hand side is not correct. Expected: " + newleft)
  }   

  /**
   *   Γ[P(Xi)] |- Δ[P(Xi)]
   * -------------------------------------------------------------------------------
   *  Γ[P(Xi) := A(Xi)] |- Δ[P(Xi) := A(Xi)]
   *
   */
  case class InstPred(name: String, bot: Sequent, P: AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithInstPred(name, InstPredRuleName, bot, P, phi, premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val newleft = premises(t1).left.map(substitutePredicatesInFormula(_, Map(P -> phi)))
      val newright = premises(t1).right.map(substitutePredicatesInFormula(_, Map(P -> phi)))
      if isSameSet(newleft, bot.left) then
        if isSameSet(newright, bot.right) then
          None
        else Some("Right-hand side is not correct. Expected: " + newright)
      else Some("Left-hand side is not correct. Expected: " + newleft)
  }



  /**
   *    Γ, A[x := y] |- Δ
   * ------------------------- Where y is not free in Γ and Δ
   *   Γ, A[x := ϵx. A ] |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A[x := y] on the left of the premise
   */
  case class LeftEpsilon(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftExistsRuleName, bot, i, y.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val Ay = premises(t1).left(i)
      val Aepsi = substituteVariablesInFormula(Ay, Map(y -> EpsilonTerm(y, Ay)))
      if (isSameSet(bot.right, premises(t1).right)) then
        if (isSameSet(bot.left :+ Ay, premises(t1).left :+ Aepsi)) then
          if ((bot.left `concat` bot.right).forall(f => !f.freeVariables.contains(y))) then
            None
          else Some(s"The variable $y must not be free in the resulting sequent.")
        else Some("Left-hand side of conclusion :+ A must be the same as left-hand side of premise :+ ∃x. A")
      else Some("Right-hand side of conclusion must be the same as right-hand side of premise")
  }


   /**
   *    Γ |- A[x := t], Δ
   * -------------------
   *  Γ |- A[x := ϵx. A], Δ
   *
   * @param bot Resulting formula
   * @param i Index of ∃x. A on the right
   * @param t Term in place of x in the premise
   */
  case class RightEpsilon(name: String, bot: Sequent, i: Int, phi: Formula, x: VariableSymbol, t: Term, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, RightExistsRuleName, bot, i, t.toString(), premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val At = substituteVariablesInFormula(phi, Map(x -> t))
      val Aepsi = substituteVariablesInFormula(phi, Map(x -> EpsilonTerm(x, phi)))
      if (isSameSet(bot.left, premises(t1).left))
        if (isSameSet(bot.right :+ At, premises(t1).right :+ Aepsi))
          None
        else Some("Right-hand side of conclusion :+ A must be the same as right-hand side of premise")
      else Some("Left-hand side of conclusion must be the same as left-hand side of premise :+ A")
  }
}

