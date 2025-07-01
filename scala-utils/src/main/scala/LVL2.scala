package sctptp

import FOL.*
import SequentCalculus.*
import java.text.Normalizer.Form

object LVL2 {
  trait StrictLVL2ProofStep extends SCProofStep
  type LVL2ProofStep = StrictLVL2ProofStep | LVL1ProofStep

  // Flattern a formula with Or connector
  def toFlatternOrSeq(f: sctptp.FOL.Formula): Seq[sctptp.FOL.Formula] = {
      f match
        case ConnectorFormula(Or, args) => args.foldLeft(Seq())((acc, x) => acc ++ toFlatternOrSeq(x))
        case _ => Seq(f)
  }

  case class LVL2Proof(steps: IndexedSeq[LVL2ProofStep], thmName: String) extends SCProof[LVL2ProofStep] {
    override def addStepLVL1After(scps: LVL1ProofStep): SCProof[LVL2ProofStep] = {
      LVL2Proof(steps :+ scps, thmName)
    }

    override def addStepsLVL1After(scps: Seq[LVL1ProofStep]): SCProof[LVL2ProofStep] = {
      LVL2Proof(scps.foldLeft(steps)((acc, x) => acc :+ x), thmName)
    }

    override def addStepLVL2After(scps: LVL2ProofStep): SCProof[LVL2ProofStep] = {
      LVL2Proof(steps :+ scps, thmName)
    }

    override def addStepsLVL2After(scps: Seq[LVL2ProofStep]): SCProof[LVL2ProofStep] = {
      LVL2Proof(scps.foldLeft(steps)((acc, x) => acc :+ x), thmName)
    }

    override def addStepLVL1Before(scps: LVL1ProofStep): SCProof[LVL2ProofStep] = {
      LVL2Proof(scps +: steps, thmName)
    }

    override def addStepsLVL1Before(scps: Seq[LVL1ProofStep]): SCProof[LVL2ProofStep] = {
      LVL2Proof(scps.foldLeft(steps)((acc, x) => x +: acc), thmName)
    }

    override def addStepLVL2Before(scps: LVL2ProofStep): SCProof[LVL2ProofStep] = {
      LVL2Proof(scps +: steps, thmName)
    }

    override def addStepsLVL2Before(scps: Seq[LVL2ProofStep]): SCProof[LVL2ProofStep] = {
      LVL2Proof(scps.foldLeft(steps)((acc, x) => x +: acc), thmName)
    }
  }

  val LeftImp2RuleName = "leftImp2"
  val LeftHypRuleName = "leftHyp"
  val LeftNotAndRuleName = "leftNotAnd"
  val LeftNotOrRuleName = "leftNotOr"
  val LeftNotImpRuleName = "leftNotImp"
  val LeftNotIffRuleName = "leftNotIff"
  val LeftNotNotRuleName = "leftNotNot"
  val LeftNotExRuleName = "leftNotEx"
  val LeftNotAllRuleName = "leftNotAll"

  /**
    * -------------------
    *   Γ, A, ¬A, |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A on the left
    */
  case class LeftHyp(name: String, bot: Sequent, i: Int) extends StrictLVL2ProofStep {
    val premises = Seq()
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftHypRuleName, "assumption", bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) = 
      val fi = bot.left(i)
      if bot.left.contains(~fi) then StepCheckOK
      else StepCheckError(s"${~fi} is not present in the left hand side")
  }
  
  /**
  *    Γ, ¬A |-  Δ     Γ, B |-  Δ
  * -------------------------------
  *          Γ, A ⇒ B |- Δ
  *
  * @param bot Resulting formula
  * @param i Index of A ⇒ B on the left
  */
  case class LeftImp2(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftImp2RuleName, "plain", bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1), t2 = map.getOrElse(t2, t2))
    def checkCorrectness(premises: String => Sequent) = 
      val AB = bot.left(i)
      AB match
        case ConnectorFormula(Implies, Seq(a, b)) =>
          if (isSameSet(bot.right, premises(t1).right.concat(premises(t2).right)))
            if (isSameSet(bot.left :+ ~a, premises(t1).left :+ AB) && isSameSet(bot.left :+ b, premises(t2).left :+ AB))
              StepCheckOK
            else StepCheckError(s"Left-hand side of conclusion must be identical to union of left-hand sides of premisces + φ⇒ψ.")
          else StepCheckError(s"Right-hand side of conclusion + A  + B must be identical to union of right-hand sides of premisces.")
        case _ => StepCheckError(s"$AB is not an implication")
  }






  /**
   *    Γ, ¬A |- Δ     Γ, ¬B |- Δ
   * -------------------------------
   *     Γ, ¬(A ∧ B), Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬(A ∧ B) on the left
   */
  case class LeftNotAnd(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotAndRuleName, "plain", bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1), t2 = map.getOrElse(t2, t2))
    def checkCorrectness(premises: String => Sequent) = 
      val nAB = bot.left(i)
      nAB match
        case na_b @ ConnectorFormula(Neg, Seq(ConnectorFormula(And, Seq(a, b)))) => 
          if (isSameSet(bot.right, premises(t1).right `concat` premises(t2).right))
            if (isSameSet(bot.left :+ ~a, premises(t1).left :+ nAB) && isSameSet(bot.left :+ ~b, premises(t2).left :+ nAB))
              StepCheckOK
            else StepCheckError(s"Left-hand side of conclusion must be identical to union of left-hand sides of premisces + ~(A ∧ B).")
          else StepCheckError(s"Right-hand side of conclusion + A + B must be identical to union of right-hand sides of premisces.")
        case _ => StepCheckError(s"$nAB is not a negated conjunction")
  }

  /**
   *    Γ, ¬A, ¬B |- Δ
   * ----------------
   *   Γ, ¬(A ∨ B) |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬(A ∨ B) on the left
   */
  case class LeftNotOr(name: String, bot: Sequent, i: Int, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotOrRuleName, "plain", bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val nAB = bot.left(i)
      bot.left(i) match
        case na_b @ ConnectorFormula(Neg, Seq(ConnectorFormula(Or, Seq(a, b)))) => 
          if isSameSet(bot.right, premises(t1).right) then
            if isSameSet(bot.left :+ ~a :+ ~b, premises(t1).left :+ nAB) then
              StepCheckOK
            else StepCheckError("Left-hand side of conclusion :+ φ∧ψ must be same as left-hand side of premise :+ A, B")
          else StepCheckError("Right-hand sides of the premise and the conclusion must be the same.")
        case _ => StepCheckError(s"$nAB is not a negated disjunction")
  }

  /**
   *    Γ, A, ¬B |- Δ
   * ----------------
   *   Γ, ¬(A ⇒ B) |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬(A ⇒ B) on the left
   */
  case class LeftNotImp(name: String, bot: Sequent, i: Int, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotImpRuleName, "plain", bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val nAB = bot.left(i)
      nAB match
        case ConnectorFormula(Neg, Seq(ConnectorFormula(Implies, Seq(a, b)))) => 
          if (isSameSet(bot.right, premises(t1).right))
            if (isSameSet(bot.left :+ a :+ ~b, premises(t1).left :+ nAB))
              StepCheckOK
            else StepCheckError(s"Left-hand side of conclusion must be identical to union of left-hand sides of premisces + ~(A ⇒ B).")
          else StepCheckError(s"Right-hand side of conclusion + A + ~B must be identical to union of right-hand sides of premisces.")
        case _ => StepCheckError(s"$nAB is not a negated implication")
  }

  /**
   *    Γ, ¬(A ⇒ B)  |- Δ     Γ, ¬(B ⇒ A)  |- Δ
   * ---------------------------------------------
   *               Γ, ¬(A ⇔ B) |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬(A ⇔ B) on the left
   */
  case class LeftNotIff(name: String, bot: Sequent, i: Int, t1: String, t2: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotIffRuleName, "plain", bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1), t2 = map.getOrElse(t2, t2))
    def checkCorrectness(premises: String => Sequent) = 
      val nAB = bot.left(i)
      bot.left(i) match
        case ConnectorFormula(Neg, Seq(ConnectorFormula(Iff, Seq(a, b)))) => 
          val nAimpB = ~(a ==> b)
          val nBimpA = ~(b ==> a)
          if (isSameSet(bot.right, premises(t1).right `concat` premises(t2).right))
            if (isSameSet(bot.left :+ nAimpB, premises(t1).left :+ nAB) && isSameSet(bot.left :+ nBimpA, premises(t2).left :+ nAB))
              StepCheckOK
            else StepCheckError(s"Left-hand side of conclusion must be identical to union of left-hand sides of premisces + ~(A ⇔ B).")
          else StepCheckError(s"Right-hand side of conclusion + ~(A ⇒ B) + ~(B ⇒ A) must be identical to union of right-hand sides of premisces.")
        case _ => StepCheckError(s"$nAB is not a negated biconditional")
  }

  /**
   *    Γ, A |- Δ
   * ----------------
   *   Γ, ¬¬A |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of ¬¬A on the left
   */
  case class LeftNotNot(name: String, bot: Sequent, i: Int, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, LeftNotNotRuleName, "plain", bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val nnA = bot.left(i)
      bot.left(i) match
        case ConnectorFormula(Neg, Seq(ConnectorFormula(Neg, Seq(a)))) => 
          if (isSameSet(bot.right, premises(t1).right))
            if (isSameSet(bot.left :+ a, premises(t1).left :+ nnA))
              StepCheckOK
            else StepCheckError(s"Left-hand side of conclusion must be identical to union of left-hand sides of premisces + ¬¬A.")
          else StepCheckError(s"Right-hand side of conclusion + A must be identical to union of right-hand sides of premisces.")
        case _ => StepCheckError(s"$nnA is not a double negation")
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
  case class LeftNotEx(name: String, bot: Sequent, i: Int, t: Term, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftNotExRuleName, bot, i, t.toString(), premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val neA = bot.left(i)
      neA match
        case ConnectorFormula(Neg, Seq(BinderFormula(Exists, x, a))) => 
          val inst = substituteVariablesInFormula(a, Map(x -> t))
          if (isSameSet(bot.right, premises(t1).right))
            if (isSameSet(bot.left :+ ~inst, premises(t1).left :+ neA))
              StepCheckOK
            else StepCheckError("Left-hand side of conclusion + A[t/x] must be the same as left-hand side of premise + ∀x. A")
          else StepCheckError("Right-hand side of conclusion must be the same as right-hand side of premise")
        case _ => StepCheckError("The formula is not a negated existential quantification")
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
  case class LeftNotAll(name: String, bot: Sequent, i: Int, y: VariableSymbol, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, LeftNotAllRuleName, bot, i, y.toString(), premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val nfA = bot.left(i)
      bot.left(i) match
        case nall @ ConnectorFormula(Neg, Seq(BinderFormula(Forall, x, a))) => 
          val inst = substituteVariablesInFormula(a, Map(x -> y()))
          if (isSameSet(bot.right, premises(t1).right))
            if (isSameSet(bot.left :+ inst, premises(t1).left :+ nfA))
              if ((bot.left `concat` bot.right).forall(f => !f.freeVariables.contains(x)))
                StepCheckOK
              else StepCheckError("The variable x must not be free in the resulting sequent.")
            else StepCheckError("Left-hand side of conclusion + A must be the same as left-hand side of premise + ∃x. A")
          else StepCheckError("Right-hand side of conclusion must be the same as right-hand side of premise")
        case _ => StepCheckError("The formula is not a negated universal quantification")
  }





  /**
   *       Γ |- P[x:=t], Δ
   * ---------------------------
   *   Γ, t = u |- P[x:=u], Δ
   *
   * @param bot Resulting formula
   * @param i Index of t = u on the left
   * @param j Index of P(t) on the right
   * @param P Shape of the formula in which the substitution occurs
   * @param x Variable indicating where in P the substitution occurs
   */
  case class RightSubstMulti(name: String, bot: Sequent, is: List[Int], P: Formula, xs: List[VariableSymbol], t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubstMany(name, "rightSubstMulti", bot, is, P.toString(), xs.map(_.toString()), premises)

    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) =
      val (ls, rs) = is.foldLeft(None: Option[(List[Term], List[Term])]) {
        case (Some((list1, list2)), i) =>
          premises(t1).left(i) match
            case AtomicFormula(`equality`, Seq(l, r)) => Some(l :: list1, r :: list2)
            case _ => None
        case (None, _) => None
      } match
        case Some((ls, rs)) => (ls.reverse, rs.reverse)
        case None => return StepCheckError("Right substitution failed: equality not found")
      val P_t = substituteVariablesInFormula(P, (xs zip ls).toMap)
      val P_u = substituteVariablesInFormula(P, (xs zip rs).toMap)
      if isSameSet(bot.left, premises(t1).left ++ (ls zip rs).map(_ === _)) then
        if isSameSet(P_t +: bot.right, P_u +: premises(t1).right) then
          StepCheckOK
        else
          StepCheckError(s"Right substitution failed: left sides are not correct")
      else
        StepCheckError(s"Right substitution failed: right sides are not correct")
  }

  /**
   *       Γ, P[x:=t] |- Δ
   * ---------------------------
   *   Γ, t = u, P[x:=u] |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of t = u on the left
   * @param j Index of P(t) on the left
   * @param P Shape of the formula in which the substitution occurs
   * @param x Variable indicating where in P the substitution occurs
   */
  case class LeftSubstMulti(name: String, bot: Sequent, is: List[Int], P: Formula, xs: List[VariableSymbol], t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubstMany(name, "leftSubstMulti", bot, is, P.toString(), xs.map(_.toString()), premises)

    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) =
      val (ls, rs) = is.foldLeft(None: Option[(List[Term], List[Term])]) {
        case (Some((list1, list2)), i) =>
          premises(t1).left(i) match
            case AtomicFormula(`equality`, Seq(l, r)) => Some(l :: list1, r :: list2)
            case _ => None
        case (None, _) => None
      } match
        case Some((ls, rs)) => (ls.reverse, rs.reverse)
        case None => return StepCheckError("Left substitution failed: equality not found")
      val P_t = substituteVariablesInFormula(P, (xs zip ls).toMap)
      val P_u = substituteVariablesInFormula(P, (xs zip rs).toMap)
      if isSameSet(P_t +: bot.left, (P_u +: premises(t1).left) ++ (ls zip rs).map(_ === _)) then
        if isSameSet(bot.right, premises(t1).right) then
          StepCheckOK
        else
          StepCheckError(s"Left substitution failed: right sides are not correct")
      else
        StepCheckError(s"Left substitution failed: left sides are not correct")

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
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) =
      try {
        CongruenceClosure.eliminateCongruence(Seq(this))
        return StepCheckOK
      } catch
        case e: Exception => {
          return StepCheckError(e.getMessage)
        }
  }

  case class Res(name: String, bot: Sequent, i1: Int, t1: String, t2: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1, t2)

    override val toString: String = SCProofStep.outputSingleIndex(name, "plain", "res", bot, i1, premises)

    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1), t2 = map.getOrElse(t2, t2))
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown //todo
  }



    /** A \/ C     A \/ ~C
   * -----------------------
   *   Γ |- A \/ B, Δ
   *
   * @param name name of the formula
   * @param bot Resulting formula
   * @param i1 index of C in t1
   * @param t1 Parent clause 1
   * @param i2 index of ~C in t2
   * @param t2 Parent clause 2
   **/
  case class Res2(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1, t2)

    def computeLit(f : Formula, index: Int): (Formula, Seq[Formula]) = {
      f match
        case AtomicFormula(_, _) => (f, Seq())
        case ConnectorFormula(label, args) => {
          label match
            case Neg =>  (f,  Seq())
            case Or => {
              val args_flat = toFlatternOrSeq(f)
              (args_flat(index), args_flat.filterNot(x => x == args_flat(index)))
            } 
            case _ => throw Exception(s"Resolution literal is not correct") 
        }
        case _ => throw Exception(s"Resolution literal is not correct") 
    }   

    override val toString: String = SCProofStep.outputDoubleIndexes(name, "plain", "res", bot, i1, i2, premises)

    def castToRes(premises2: String => Sequent): SCProofStep = {
      val a = premises2(t1).right(i1)
      if !(a.isInstanceOf[AtomicFormula]) 
        then Res(name, bot, i2, t2, t1)
        else Res(name, bot, i1, t1, t2)
    }
    override def toStringWithPremises(premises2: String => Sequent): String = {
      val A_Pair = computeLit(premises2(t1).right(0), i1)
      if A_Pair._1.isInstanceOf[AtomicFormula] 
        then SCProofStep.outputSingleIndex(name, "plain", "res", bot, i1, premises)
        else SCProofStep.outputSingleIndex(name, "plain", "res", bot, i2, premises.reverse)
    }

    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1), t2 = map.getOrElse(t2, t2))
    def checkCorrectness(premises: String => Sequent) =

      val A_Pair = computeLit(premises(t1).left(0), i1)
      val B_Pair = computeLit(premises(t2).left(0), i2)

      val Res2 = {
        bot.left(0) match
          case AtomicFormula(_, _) =>  bot.left
          case ConnectorFormula(label, args) => {
            label match
              case Neg =>  bot.left
              case Or =>  toFlatternOrSeq(bot.left(0))
              case _ => throw Exception(s"Resolution literal is not correct") 
          }
          case _ => throw Exception(s"Resolution literal is not correct") 
      }

      val A = A_Pair._1
      val A_Rest = A_Pair._2
      val B = B_Pair._1
      val B_Rest = B_Pair._2


      val res = (A, B) match 
        case (AtomicFormula(_, _), ConnectorFormula(Neg,Seq(x))) =>
          (x == A) &&         
          (isSameSet(A_Rest ++ B_Rest, Res2) || bot.left(0) == AtomicFormula(AtomicSymbol("$false",0), Seq()))

        case (ConnectorFormula(Neg, Seq(x)), AtomicFormula(_, _)) =>
          x == B &&
          (isSameSet(A_Rest ++ B_Rest, Res2) || bot.left(0) == AtomicFormula(AtomicSymbol("$false",0), Seq()))
        case _ => false

      if res then StepCheckOK else StepCheckError("Resolution failed")
  }

  /**
   *    Γ |-
   * -------------
   *    Δ |-
   *  And Δ is the negation of Γ
   * @param bot Resulting formula
   */
  case class NegatedConjecture(name: String, bot: Sequent, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = s"fof(${name}, assumption, ${bot}, inference(negated_conjecture, [status(thm)], [${t1}])).";
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown //TODO
  }

    /**
   *    Γ |-
   * -------------
   *    Δ |-
   *  And Δ is the negation of Γ
   * @param bot Resulting formula
   */
  case class Clausify(name: String, bot: Sequent, i:Int) extends StrictLVL2ProofStep {
    val premises = Seq()
    override def toString: String = s"fof(${name}, plain, ${bot}, inference(clausify, [status(thm), $i], [])).";
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
      // isSubset(bot.left, premises(t1).left) &&
      //   isSubset(bot.right, premises(t1).right)
  }

      /**
   *    Γ |-
   * -------------
   *    Δ |-
   *  And Δ is the negation of Γ
   * @param bot Resulting formula
   */
  case class RightPrenex(name: String, bot: Sequent, i:Int, j: Int, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = s"fof(${name}, plain, ${bot}, inference(rightPrenex, [status(thm), $i, $j], [${t1}])).";
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
      // isSubset(bot.left, premises(t1).left) &&
      //   isSubset(bot.right, premises(t1).right)
  }

  /*
    /**
   *   Γ, P[x:=t] |- Δ
   * ----------------
   *   Γ, P[x:=u] |- Δ
   *
   * @param bot Resulting formula
   * @param i Index of P(t) on the left
   * @param x Variable indicating where in P the substitution occurs
   * @param t term in place of x
   */
  case class Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = s"fof(${name}, plain, ${bot}, inference(instantiate, [status(thm), ${i}, $$fot(${x.toString()}), $$fot(${t.toString()})], [${t1}])).";
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val new_p = substituteVariablesInFormula(premises(t1).left(i), Map(x -> t))

      if isSameSet(premises(t1).left(i) +: bot.left, new_p +: premises(t1).left) then
        if isSameSet(bot.right, premises(t1).right) then
          StepCheckOK
        else
          StepCheckError("right sides is not the same")
      else
        StepCheckError("left sides is not correct")
  }
*/

  /**
   *   Γ, P[x:=t] |- Δ
   * ----------------
   *   Γ, P[x:=u] |- Δ
   *
   * @param bot Resulting formula
   * @param (i, x) List of pair (index, term)
   * @param t term in place of x
   */
  case class InstMult(name: String, bot: Sequent, terms: Seq[(FunctionSymbol|AtomicSymbol|VariableSymbol, Term|Formula, Seq[VariableSymbol])], t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)

    override def toString: String = 
      s"fof(${name}, plain, ${bot}, inference(instMult, [status(thm), " +
      s"[" +
        terms.map((label, t, varsl) => s"tuple3('${label.toString()}', $$fot(${t.toString()}), [${varsl.mkString(", ")}])").mkString(", ") +
      s"]], [${t1}]))."

    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
  }


     /**
   *    Γ |-
   * -------------
   *    Δ |-
   *  And Δ is the nnf of Γ
   * @param bot Resulting formula
   */
  case class RightNNF(name: String, bot: Sequent, i: Int, j: Int, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = s"fof(${name}, plain, ${bot}, inference(rightNnf, [status(thm), ${i}, ${j}], [${t1}])).";
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
  }

  case class LetFormula(name: String, defin: Formula) extends StrictLVL2ProofStep {
    val premises = Seq()
    val bot =  Sequent(Seq(), Seq(top()))
    override def toString: String = s"fof(${name}, let, $defin).";
    def addAssumptions(fs: Seq[Formula]) = this
    def mapBot(f: Sequent => Sequent) = this
    def rename(newName: String) = this
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) = StepCheckOK
  }

  case class LetTerm(name: String, defin: Term) extends StrictLVL2ProofStep {
    val premises = Seq()
    val bot =  Sequent(Seq(), Seq(top()))
    override def toString: String = s"fof(${name}, let, $defin).";
    def addAssumptions(fs: Seq[Formula]) = this
    def mapBot(f: Sequent => Sequent) = this
    def rename(newName: String) = this
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) = StepCheckOK
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
  case class InstForall(name: String, bot: Sequent, i: Int, t: Term, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithTerm(name, "instForall", bot, i, t.toString(), premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
  }


  case class RightEpsilonSubst(name: String, bot: Sequent, i: Int, flip: Boolean,
                               P:AtomicSymbol, phi: (Formula, Seq[VariableSymbol]), exi: BinderFormula,
                               t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = 
      s"fof(${name}, plain, ${bot}, inference(rightEpsilonSubst, [status(thm), ${i}, ${if flip then 1 else 0}, '${P}', $$fof(${phi._1}), " +
      s"[${phi._2.map(st => s"'${st.toString()}'").mkString(",")}], $$fof(${exi})], [${t1}]))."
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
  }

  case class ExistsIffEpsilon(name: String, bot: Sequent, i:Int) extends StrictLVL2ProofStep {
    val premises = Seq()
    override def toString: String = SCProofStep.outputSingleIndex(name, "plain", "existsIffEpsilon", bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = this
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
  }



  case class RightSubstFun(name: String, bot: Sequent, i: Int, flip:Boolean, phi: Formula, F:FunctionSymbol,  t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = 
      s"fof(${name}, plain, ${bot}, inference(rightSubstFun, [status(thm), ${i}, ${if flip then 1 else 0}, $$fof(${phi}), '${F}'], [${t1}]))."
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): RightSubstFun = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
  }

  case class RightSubstPred(name:String, bot: Sequent, i: Int, flip:Boolean, phi: Formula, P:AtomicSymbol,  t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = 
      s"fof(${name}, plain, ${bot}, inference(rightSubstPred, [status(thm), ${i}, ${if flip then 1 else 0}, $$fof(${phi}), '${P}'], [${t1}]))."
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): RightSubstPred = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
  }

  /**
   * -----------------
   *   Γ, A |- A, Δ
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   */
  case class ElimIffRefl(name: String, bot: Sequent, i: Int, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, "plain", "elimIffRefl", bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
  }

  case class ElimEqRefl(name: String, bot: Sequent, i: Int, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, "plain", "elimEqRefl", bot, i, premises)
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = this
    def checkCorrectness(premises: String => Sequent) = StepCheckUnknown
  }


  /**
   * This step is rather ill-named (it's a rightContract) but that's what the Prover 9 proouf output uses.
   * Please don't use it.
   *    Γ |- Δ, a, a
   * -------------
   *   Γ |- Δ, a
   *
   * @param bot Resulting formula
   * @param i Index of A on the left
   */
  case class LeftWeakenRes(name: String, bot: Sequent, i: Int, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputSingleIndex(name, "plain", "leftWeakenRes", bot, i, Seq(t1))
    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 

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

      if isSameSet(Seq(new_bot), premises(t1).left) then
        if isSameSet(premises(t1).right, bot.right) then
          StepCheckOK
        else StepCheckError(s"Right-hand side of premise is not the same as the right-hand side of the conclusion.")
      else StepCheckError(s"Left-hand side of premise is not the same as the left-hand side of the conclusion.")
  }

  /**
   *   Γ, P[x:=t] |- Δ
   * ----------------
   *   Γ, P[x:=u] |- Δ
   *
   * @param bot Resulting formula
   * @param (i, x) List of pair (index, term)
   * @param t term in place of x
   */
  case class InstantiateMultP9(name: String, bot: Sequent, terms: Seq[(VariableSymbol, Term)], t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)

    override def toString: String = s"fof(${name}, plain, ${bot}, inference(instantiateMult, [status(thm), " +
      s"[${terms.foldLeft(("", 0))((acc, x) => {(acc._1 ++ ("[$fot" + s"(${x(0).toString()}), $$fot(${x(1).toString()})]") ++ (if (acc._2 != terms.length - 1) then ", " else ""), acc._2 + 1)})._1}]], [${t1}])).";

    def addAssumptions(fs: Seq[Formula]) = copy(bot = bot ++<< fs)
    def mapBot(f: Sequent => Sequent) = copy(bot = f(bot))
    def rename(newName: String) = copy(name = newName)
    def renamePremises(map: Map[String, String]): SCProofStep = copy(t1 = map.getOrElse(t1, t1))
    def checkCorrectness(premises: String => Sequent) = 
      val map = terms.foldLeft(Map[VariableSymbol, Term]())((acc, x) => acc + (x._1 -> x._2))
      val new_right = premises(t1).right.map(f => substituteVariablesInFormula(f, map))
      if isSameSet(bot.right, new_right) then
        if isSameSet(bot.left, premises(t1).left) then
          StepCheckOK
        else
          StepCheckError("left sides are not the same")
      else
        StepCheckError("right side (with instantiation) is not correct")
  }


}
