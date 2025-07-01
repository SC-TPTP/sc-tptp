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

    def +<<(f: Formula): Sequent = Sequent(left :+ f, right)
    def -<<(f: Formula): Sequent = Sequent(left.filterNot(_ == f), right)
    def +>>(f: Formula): Sequent = Sequent(left, right :+ f)
    def ->>(f: Formula): Sequent = Sequent(left, right.filterNot(_ == f))
    def ++<<(fs: Seq[Formula]): Sequent = Sequent(left ++ fs, right)
    def ++>>(fs: Seq[Formula]): Sequent = Sequent(left, right ++ fs)
  }

  
  
  sealed trait StepCheck
  case object StepCheckOK extends StepCheck :
    override def toString(): String = "% SZS status Verified"
  case class StepCheckError(msg: String) extends StepCheck :
    override def toString(): String = "% SZS status FailedVerified\n" + msg
  case object StepCheckUnknown extends StepCheck :
    override def toString(): String = "% SZS status NotVerified\n" +
      "% This proof contains level 3 proof steps that have not been checked, but the proof is otherwise correct.\n"

  trait SCProofStep {
    val name: String
    val bot: Sequent
    val premises: Seq[String]

    def checkCorrectness(premises: String => Sequent): StepCheck
    def toStringWithPremises(premises: String => Sequent): String = toString
    def addAssumptions(fs: Seq[Formula]): SCProofStep
    def mapBot(f: Sequent => Sequent): SCProofStep
    def rename(newName: String): SCProofStep
    def renamePremises(map: Map[String, String]): SCProofStep
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

    def outputWithSubst(name: String, rule: String, bot: Sequent, i: Int, flip: Boolean, term: String, subterm: String, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), ${i}, ${if flip then 1 else 0}, $$fof(${term}), '${subterm}'], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputWithSubstIff(name: String, rule: String, bot: Sequent, i: Int, flip: Boolean, term: String, subterm: String, premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), ${i}, ${if flip then 1 else 0}, $$fof(${term})) $$fof(${subterm})], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputWithSubstMany(name: String, rule: String, bot: Sequent, is: Seq[Int], term: String, subterms: Seq[String], premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), [${is.mkString(", ")}], $$fof(${term}), [${subterms.map(st => s"$$fot(${st})").mkString(",")}]], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputWithInstFun(name: String, rule: String, bot: Sequent, F: FunctionSymbol, t: (Term, Seq[VariableSymbol]), premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), '${F.toString()}', $$fot(${t._1.toString()}), [${t._2.map(st => s"'${st.toString()}'").mkString(",")}]], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    def outputWithInstPred(name: String, rule: String, bot: Sequent, P: AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), premises: Seq[String]): String = 
      s"fof(${name}, plain, ${bot}, inference(${rule}, [status(thm), '${P.toString()}', $$fof(${phi._1.toString()}), [${phi._2.map(st => s"'${st.toString()}'").mkString(",")}]], [${premises.foldLeft("", 0)((acc, e) => (acc._1 + e.toString() + (if (acc._2 != premises.length - 1) then ", " else ""), acc._2 + 1))._1}]))."

    }

  case class Conjecture(name: String, goal: Sequent) extends LVL1ProofStep{
    val bot = Sequent(Seq(), Seq(top()))
    val premises = Seq()
    def addAssumptions(fs: Seq[Formula]) = this
    def mapBot(f: Sequent => Sequent) = this
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) = StepCheckOK
    override def toString: String = s"fof($name, conjecture, ${goal})."

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

  def checkProof[Steps<:SCProofStep](p: SCProof[Steps]): StepCheck = {
    val steps = p.steps
    val premises: scala.collection.mutable.Map[String, Sequent] = scala.collection.mutable.Map()
    var unknown = false
    def inner( steps: Iterator[Steps]): Option[(String, Steps)] =
      if steps.hasNext then
        val step = steps.next()
        step.checkCorrectness(premises) match
        case StepCheckOK =>
          premises.update(step.name, step.bot)
          inner(steps)
        case StepCheckUnknown =>
          unknown = true
          premises.update(step.name, step.bot)
          inner(steps)
        case StepCheckError(msg) =>
          Some((s"${step.name}:  $msg", step))
      else
        None
    inner(steps.iterator) match
      case Some((msg, step)) =>
        StepCheckError(s"line $step\n $msg")
      case None =>
        if unknown then StepCheckUnknown else StepCheckOK
  }

  def printProof[Steps<:SCProofStep](p: SCProof[Steps]): Unit = {
    val steps = p.steps
    val premises: scala.collection.mutable.Map[String, Sequent] = scala.collection.mutable.Map()
    def inner(steps: Iterator[Steps]): Unit =
      if steps.hasNext then
        val step = steps.next()
        println(step.toStringWithPremises(premises))
        premises.update(step.name, step.bot)
        inner(steps)
    inner(steps.iterator)
  }

  def ProofToString[Steps<:SCProofStep](p: SCProof[Steps]): String = {
    val steps = p.steps
    val premises: scala.collection.mutable.Map[String, Sequent] = scala.collection.mutable.Map()
    def inner(step: Steps): String =
        premises.update(step.name, step.bot)
        step.toStringWithPremises(premises)
    steps.foldLeft("")((acc, x) => acc ++ inner(x) ++ "\n")
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
    def addAssumptions(fs: Seq[Formula]): Axiom = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent): Axiom = copy(bot = f(bot))
    def rename(newName: String): Axiom = copy(name = newName)
    def renamePremises(map: Map[String, String]): Axiom = this
    def checkCorrectness(premises: String => Sequent) = StepCheckOK
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) = StepCheckOK
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
    override def toString: String = SCProofStep.outputSingleIndex(name, "assumption", HypRuleName, bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) = 
      val fi = bot.left(i)
      if bot.right.exists(isSame(_, fi)) then StepCheckOK 
      else StepCheckError(s"Hyp error: Formula ${fi} is not present in the right-hand side of the conclusion.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val A = bot.left(i)
      if isSameSet(A +: premises(t1).left, bot.left) then
        if isSameSet(premises(t1).right, bot.right) then
          StepCheckOK
        else StepCheckError(s"LeftWeaken error: The right-hand side of the conclusion does not match the right-hand side of the premise.")
      else StepCheckError(s"LeftWeaken error: The left-hand side of the conclusion should match the left-hand side of the premise with formula ${A} added.")
      
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val A = bot.right(i)
      if isSameSet(A +: premises(t1).right, bot.right) then
        if isSameSet(premises(t1).left, bot.left) then
          StepCheckOK
        else StepCheckError(s"RightWeaken error: The left-hand side of the conclusion does not match the left-hand side of the premise.")
      else StepCheckError(s"RightWeaken error: The right-hand side of the conclusion should match the right-hand side of the premise with formula ${A} added.")
      
  }

  /**
   *    Γ |- A, Δ    Γ, A |- Δ
   * -------------------------
   *          Γ |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of cut on the right of first premise
   */
  case class Cut(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends LVL1ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, "plain", CutRuleName, bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1), t2 = map.getOrElse(t2, t2))
    def checkCorrectness(premises: String => Sequent) = 
      val A = premises(t1).right(i)
      if (isSameSet(bot.left :+ A, premises(t1).left `concat` premises(t2).left) && (!contains(premises(t1).left, A) || contains(bot.left, A))) then
        if (isSameSet(bot.right :+ A, premises(t2).right `concat` premises(t1).right) && (!contains(premises(t2).right, A) || contains(bot.right, A))) then
          if (contains(premises(t2).left, A)) then
            if (contains(premises(t1).right, A)) then
              StepCheckOK
            else StepCheckError(s"Cut error: The right-hand side of the first premise does not contain the formula ${A} as expected.")
          else StepCheckError(s"Cut error: The left-hand side of the second premise does not contain the formula ${A} as expected.")
        else StepCheckError(s"Cut error: The right-hand side of the conclusion with ${A} added is not the union of the right-hand sides of the premises.")
      else StepCheckError(s"Cut error: The left-hand side of the conclusion with ${A} added is not the union of the left-hand sides of the premises.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val AB = bot.left(i)
      AB match
        case ConnectorFormula(And, Seq(a, b)) =>
          if isSameSet(bot.right, premises(t1).right) then
            if isSameSet(bot.left :+ a :+ b, premises(t1).left :+ AB) then
              StepCheckOK
            else StepCheckError("LeftAnd error: The left-hand side of the conclusion with A and B added must match the left-hand side of the premise with A ∧ B.")
          else StepCheckError("LeftAnd error: The right-hand side of the conclusion must match the right-hand side of the premise.")
        case _ => StepCheckError(s"LeftAnd error: ${AB} is not a conjunction.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1), t2 = map.getOrElse(t2, t2))
    def checkCorrectness(premises: String => Sequent) = 
      val AB = bot.left(i)
      AB match
        case ConnectorFormula(Or, Seq(a, b)) =>
          if (isSameSet(bot.right, premises(t1).right `concat` premises(t2).right))
            if (isSameSet(bot.left :+ a :+ b, premises(t1).left `concat` premises(t2).left :+ AB))
              StepCheckOK
            else StepCheckError("LeftOr error: The left-hand side of the conclusion must match the concatenation of the left-hand sides of the premises with A ∨ B.")
          else StepCheckError("LeftOr error: The right-hand side of the conclusion must match the concatenation of the right-hand sides of the premises.")
        case _ => StepCheckError(s"LeftOr error: ${AB} is not a disjunction.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1), t2 = map.getOrElse(t2, t2))
    def checkCorrectness(premises: String => Sequent) = 
      val AB = bot.left(i)
      AB match
        case ConnectorFormula(Implies, Seq(a, b)) =>
          if (isSameSet(bot.right :+ a, premises(t1).right `concat` premises(t2).right))
            if (isSameSet(bot.left :+ b, premises(t1).left `concat` premises(t2).left :+ AB))
              StepCheckOK
            else StepCheckError("LeftImplies error: The left-hand side of the conclusion with B added must match the concatenation of the left-hand sides of the premises with A ⇒ B.")
          else StepCheckError("LeftImplies error: The right-hand side of the conclusion with A added must match the concatenation of the right-hand sides of the premises.")
        case _ => StepCheckError(s"LeftImplies error: ${AB} is not an implication.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val AB = bot.left(i)
      AB match
        case ConnectorFormula(Iff, Seq(a, b)) =>
          val AimpB = ConnectorFormula(Implies, Seq(a, b))
          val BimpA = ConnectorFormula(Implies, Seq(b, a))
          if (isSameSet(premises(t1).right, bot.right))
            if isSameSet(bot.left :+ AimpB :+ BimpA, premises(t1).left :+ AB) then
              StepCheckOK
            else StepCheckError("LeftIff error: The left-hand side of the conclusion with A ⇔ B must match the left-hand side of the premise with A ⇒ B and B ⇒ A.")
          else StepCheckError("LeftIff error: The right-hand sides of the premise and the conclusion must match.")
        case _ => StepCheckError(s"LeftIff error: ${AB} is not a biconditional.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val nA = bot.left(i)
      nA match
        case ConnectorFormula(Neg, Seq(a)) => 
          if (isSameSet(bot.left, premises(t1).left :+ nA)) then
            if (isSameSet(bot.right :+ a, premises(t1).right)) then
              StepCheckOK
            else StepCheckError("LeftNot error: The right-hand side of the conclusion with A must match the right-hand side of the premise.")
          else StepCheckError("LeftNot error: The left-hand side of the conclusion must match the left-hand side of the premise with ¬A.")
        case _ => StepCheckError(s"LeftNot error: ${nA} is not a negation.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val eA = bot.left(i)
      eA match
        case BinderFormula(Exists, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> y()))
          if (isSameSet(bot.right, premises(t1).right)) then
            if (isSameSet(bot.left :+ inst, premises(t1).left :+ eA)) then
              if ((bot.left `concat` bot.right).forall(f => !f.freeVariables.contains(y))) then
                StepCheckOK
              else StepCheckError(s"LeftExists error: The variable ${y} must not be free in the resulting sequent.")
            else StepCheckError("LeftExists error: The left-hand side of the conclusion with A must match the left-hand side of the premise with ∃x. A.")
          else StepCheckError("LeftExists error: The right-hand side of the conclusion must match the right-hand side of the premise.")
        case _ => StepCheckError(s"LeftExists error: ${eA} is not an existential quantification.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val fa = bot.left(i)
      fa match
        case BinderFormula(Forall, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> t))
          if (isSameSet(bot.right, premises(t1).right))
            if (isSameSet(bot.left :+ inst, premises(t1).left :+ fa))
              StepCheckOK
            else StepCheckError("LeftForall error: The left-hand side of the conclusion with A[t/x] must match the left-hand side of the premise with ∀x. A.")
          else StepCheckError("LeftForall error: The right-hand side of the conclusion must match the right-hand side of the premise.")
        case _ => StepCheckError(s"LeftForall error: ${fa} is not a universal quantification.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1), t2 = map.getOrElse(t2, t2))
    def checkCorrectness(premises: String => Sequent) = 
      val AB = bot.right(i)
      AB match
        case ConnectorFormula(And, Seq(a, b)) =>
          if (isSameSet(bot.left, premises(t1).left `concat` premises(t2).left)) then
            if (
              isSubset(premises(t1).right, bot.right :+ a) &&
              isSubset(premises(t2).right, bot.right :+ b) &&
              isSubset(bot.right, premises(t1).right `concat` premises(t2).right :+ AB)
              ) then 
              StepCheckOK
            else StepCheckError("RightAnd error: The right-hand side of the conclusion with A and B must match the union of the right-hand sides of the premises.")
          else StepCheckError("RightAnd error: The left-hand side of the conclusion must match the left-hand side of the premises.")
        case _ => StepCheckError(s"RightAnd error: ${AB} is not a conjunction.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val AB = bot.right(i)
      AB match
        case ConnectorFormula(Or, Seq(a, b)) =>
          if (isSameSet(bot.left, premises(t1).left))
            if (isSameSet(bot.right :+ a :+ b, premises(t1).right :+ AB))
              StepCheckOK
            else StepCheckError("RightOr error: The right-hand side of the conclusion with A and B must match the right-hand side of the premise with A ∨ B.")
          else StepCheckError("RightOr error: The left-hand side of the conclusion must match the left-hand side of the premise.")
        case _ => StepCheckError(s"RightOr error: ${AB} is not a disjunction.")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val AB = bot.right(i)
      AB match
        case ConnectorFormula(Implies, Seq(a, b)) =>
          if (isSameSet(bot.left :+ a, premises(t1).left))
            if (isSameSet(bot.right :+ b, premises(t1).right :+ AB))
              StepCheckOK
            else StepCheckError("Right-hand side of conclusion :+ A=>B must be the same as right-hand side of premise + B")
          else StepCheckError("Left-hand side of conclusion :+ A must be the same as left-hand side of premise")
        case _ => StepCheckError(s"$AB is not an implication")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1), t2 = map.getOrElse(t2, t2))
    def checkCorrectness(premises: String => Sequent) = 
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
              StepCheckOK
            else StepCheckError(s"Right-hand side of conclusion + a⇒B + B⇒A is not the same as the union of the right-hand sides of the premises A⇔b.")
          else StepCheckError(s"Left-hand side of conclusion is not the union of the left-hand sides of the premises.")
        case _ => StepCheckError(s"$AB is not a biconditional")
  }

  /**
   *    Γ |- A, Δ
   * ----------------
   *   Γ |- ¬A, Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬A on the right
   */
  case class RightNot(name: String, bot: Sequent, i: Int, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, "plain", RightNotRuleName, bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val nA = bot.right(i)
      nA match
        case ConnectorFormula(Neg, Seq(a)) =>
          if (isSameSet(bot.left :+ a, premises(t1).left))
            if (isSameSet(bot.right, premises(t1).right :+ nA))
              StepCheckOK
            else StepCheckError("Right-hand side of conclusion :+ A must be the same as right-hand side of premise")
          else StepCheckError("Left-hand side of conclusion :+ A must be the same as left-hand side of premise")
        case _ => StepCheckError(s"$nA is not a negation")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val eA = bot.right(i)
      eA match
        case BinderFormula(Exists, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> t))
          if (isSameSet(bot.left, premises(t1).left))
            if (isSameSet(bot.right :+ inst, premises(t1).right :+ eA))
              StepCheckOK
            else StepCheckError("Right-hand side of conclusion :+ A must be the same as right-hand side of premise")
          else StepCheckError("Left-hand side of conclusion must be the same as left-hand side of premise :+ A")
        case _ => StepCheckError("$eA is not an existential")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val fa = bot.right(i)
      fa match
        case BinderFormula(Forall, x, a) => 
          val inst = substituteVariablesInFormula(a, Map(x -> Variable(y)))
          if (isSameSet(bot.left, premises(t1).left))
            if (isSameSet(bot.right :+ inst, premises(t1).right :+ fa))
              if ((bot.left `concat` bot.right).forall(f => !f.freeVariables.contains(y))) then
                StepCheckOK
              else StepCheckError(s"The variable $y must not be free in the resulting sequent.")
            else StepCheckError("Right-hand side of conclusion :+ A must be the same as right-hand side of premise :+ A")
          else StepCheckError("Left-hand side of conclusion must be the same as left-hand side of premise")
        case _ => StepCheckError("$fa is not a universal")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent): RightRefl = this
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) =
      val A = bot.right(i)
      A match
        case AtomicFormula(`equality`, Seq(t1, t2)) => 
          if t1 == t2 then StepCheckOK else StepCheckError("t1 and t2 are not the same")
        case _ => StepCheckError("$A is not an equality")
  }

  /**
   *   Γ, R[A:=A] |- Δ
   * ----------------
   *   Γ, A <=> B, R[A:=B] |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A <=> B on the left
   * @param R Shape of the formula in which the substitution occurs
   * @param A Formula Variable indicating where in P the substitution occurs
   */
  case class LeftSubst(name: String, bot: Sequent, i: Int, flip: Boolean, P: Formula, x: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubst(name, LeftSubstRuleName, bot, i, flip, P.toString(), x.toString(), premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val (t, u) = bot.left(i) match
        case AtomicFormula(`equality`, Seq(t1, t2)) => (t1, t2)
        case _ => return StepCheckError(s"${bot.right(i)} is not an equality")
      val P_t = substituteVariablesInFormula(P, Map(x -> t))
      val P_u = substituteVariablesInFormula(P, Map(x -> u))
      val is_left_correct = 
        if flip then
          isSameSet(P_u +: bot.left, P_t +: bot.left(i) +: premises(t1).left)
        else
          isSameSet(P_t +: bot.left, P_u +: bot.left(i) +: premises(t1).left) 
      if is_left_correct then
        if isSameSet(bot.right, premises(t1).right) then
          StepCheckOK
        else StepCheckError("Right-hand side is not correct.")
      else StepCheckError("Left-hand side is not correct.")
      
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
  case class RightSubst(name: String, bot: Sequent, i: Int, flip:Boolean, P: Formula, x: VariableSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubstIff(name, RightSubstRuleName, bot, i, flip, P.toString(), x.toString(), premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val (t, u) = bot.left(i) match
        case AtomicFormula(`equality`, Seq(t1, t2)) => (t1, t2)
        case _ => return  StepCheckError(s"${bot.left(i)} is not an equality")
      val P_t = substituteVariablesInFormula(P, Map(x -> t))
      val P_u = substituteVariablesInFormula(P, Map(x -> u))
      val is_right_correct = 
        if flip then
          isSameSet(P_u +: bot.right, P_t +: premises(t1).right)
        else
          isSameSet(P_t +: bot.right, P_u +: premises(t1).right)
      if isSameSet(bot.left, bot.left(i) +: premises(t1).left) then
        if is_right_correct then
          StepCheckOK
        else StepCheckError("Right-hand side is not correct.")
      else StepCheckError("Left-hand side is not correct.")
  }


  /**
   *   Γ, R[A:=A] |- Δ
   * ----------------
   *   Γ, A <=> B, R[A:=B] |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of A <=> B on the left
   * @param R Shape of the formula in which the substitution occurs
   * @param A Formula Variable indicating where in P the substitution occurs
   */
  case class LeftSubstIff(name: String, bot: Sequent, i: Int,  flip:Boolean, R: Formula, A: AtomicSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubstIff(name, LeftSubstIffRuleName, bot, i, flip, R.toString(), A.toString(), premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      if A.arity != 0 || !A.id.isUpper then return StepCheckError(s"$A is not a formula variable")
      val (phi, psi) = bot.left(i) match
        case ConnectorFormula(Iff, Seq(phi, psi)) => (phi, psi)
        case _ => return StepCheckError(s"${bot.right(i)} is not a biconditional")
      val R_phi = substituteAtomicsInFormula(R, Map(A -> phi))
      val R_psi = substituteAtomicsInFormula(R, Map(A -> psi))
      val is_left_correct = 
        if flip then
          isSameSet(R_psi +: bot.left, R_phi +: bot.left(i) +: premises(t1).left)
        else
          isSameSet(R_phi +: bot.left, R_psi +: bot.left(i) +: premises(t1).left)
      if is_left_correct then
        if isSameSet(bot.right, premises(t1).right) then
          StepCheckOK
        else StepCheckError("Right-hand side is not correct.")
      else StepCheckError("Left-hand side is not correct.")
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
  case class RightSubstIff(name: String, bot: Sequent, i: Int, flip:Boolean, R: Formula, A: AtomicSymbol, t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubst(name, RightSubstIffRuleName, bot, i, flip, R.toString(), A.toString(), premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      if A.arity != 0 || !A.id.isUpper then return StepCheckError(s"$A is not a formula variable")
      val (phi, psi) = bot.left(i) match
        case ConnectorFormula(Iff, Seq(phi, psi)) => (phi, psi)
        case _ => return StepCheckError(s"${bot.left(i)} is not a biconditional")
      val R_phi = substituteAtomicsInFormula(R, Map(A -> phi))
      val R_psi = substituteAtomicsInFormula(R, Map(A -> psi))
      val is_right_correct = 
        if flip then
          isSameSet(R_psi +: bot.right, R_phi +: premises(t1).right)
        else
          isSameSet(R_phi +: bot.right, R_psi +: premises(t1).right)
      if isSameSet(bot.left, bot.left(i) +: premises(t1).left) then
        if is_right_correct then
          StepCheckOK
        else StepCheckError("Right-hand side is not correct.")
      else StepCheckError("Left-hand side is not correct.")
  }


  /**
   *   Γ[F(Xi)] |- Δ[F(Xi)]
   * -------------------------------------------------------------------------------
   *  Γ[F(Xi) := t(Xi)] |- Δ[F(Xi) := t(Xi)]
   *
   */
  case class InstFun(name: String, bot: Sequent, F: FunctionSymbol, t: (Term, Seq[VariableSymbol]), t1: String) extends LVL1ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithInstFun(name, InstFunRuleName, bot, F, t, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val fun = 
        if t._2.isEmpty then f => substituteVariablesInFormula(f, Map(VariableSymbol(F.id) -> t._1)) 
        else f => substituteFunctionsInFormula(f, Map(F -> t))
      val newleft = premises(t1).left.map(fun)
      val newright = premises(t1).right.map(fun)
      if isSameSet(newleft, bot.left) then
        if isSameSet(newright, bot.right) then
          StepCheckOK
        else StepCheckError("Right-hand side is not correct. Expected: " + newright)
      else StepCheckError("Left-hand side is not correct. Expected: " + newleft)
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val newleft = premises(t1).left.map(substitutePredicatesInFormula(_, Map(P -> phi)))
      val newright = premises(t1).right.map(substitutePredicatesInFormula(_, Map(P -> phi)))
      if isSameSet(newleft, bot.left) then
        if isSameSet(newright, bot.right) then
          StepCheckOK
        else StepCheckError("Right-hand side is not correct. Expected: " + newright)
      else StepCheckError("Left-hand side is not correct. Expected: " + newleft)
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val Ay = premises(t1).left(i)
      val Aepsi = substituteVariablesInFormula(Ay, Map(y -> EpsilonTerm(y, Ay)))
      if (isSameSet(bot.right, premises(t1).right)) then
        if (isSameSet(bot.left :+ Ay, premises(t1).left :+ Aepsi)) then
          if ((bot.left `concat` bot.right).forall(f => !f.freeVariables.contains(y))) then
            StepCheckOK
          else StepCheckError(s"The variable $y must not be free in the resulting sequent.")
        else StepCheckError("Left-hand side of conclusion :+ A must be the same as left-hand side of premise :+ ∃x. A")
      else StepCheckError("Right-hand side of conclusion must be the same as right-hand side of premise")
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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val At = substituteVariablesInFormula(phi, Map(x -> t))
      val Aepsi = substituteVariablesInFormula(phi, Map(x -> EpsilonTerm(x, phi)))
      if (isSameSet(bot.left, premises(t1).left))
        if (isSameSet(bot.right :+ At, premises(t1).right :+ Aepsi))
          StepCheckOK
        else StepCheckError("Right-hand side of conclusion :+ A must be the same as right-hand side of premise")
      else StepCheckError("Left-hand side of conclusion must be the same as left-hand side of premise :+ A")
  }
}

