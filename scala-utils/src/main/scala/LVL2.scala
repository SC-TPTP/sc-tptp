package sctptp

import FOL.*
import SequentCalculus.*
import java.text.Normalizer.Form

object LVL2 {
  sealed trait StrictLVL2ProofStep extends SCProofStep
  type LVL2ProofStep = StrictLVL2ProofStep | LVL1ProofStep

  // Flattern a formula with Or connector
  def toFlatternOr(f: sctptp.FOL.Formula): Seq[sctptp.FOL.Formula] = {
      f match
        case ConnectorFormula(Or, args) => args.foldLeft(Seq())((acc, x) => acc ++ toFlatternOr(x))
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
    * @param j Index of ¬A on the left
    */
  case class LeftHyp(name: String, bot: Sequent, i: Int, j: Int) extends StrictLVL2ProofStep {
    val premises = Seq()
    override def toString: String = SCProofStep.outputDoubleIndexes(name, LeftHypRuleName, "assumption", bot, i, j, premises)
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val fi = premises(name).left(i)
      val fj = premises(name).left(j)
      if isSame(fi, ~fj) then None
      else Some(s"${fi} and ${~fj} are not the same")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val AB = bot.left(i)
      AB match
        case ConnectorFormula(Or, Seq(a, b)) =>
          if (isSameSet(bot.right, premises(t1).right.concat(premises(t2).right)))
            if (isSameSet(bot.left :+ ~a :+ b, premises(t1).left.concat(premises(t2).left) :+ AB))
              None
            else Some(s"Left-hand side of conclusion must be identical to union of left-hand sides of premisces + φ⇒ψ.")
          else Some(s"Right-hand side of conclusion + A  + B must be identical to union of right-hand sides of premisces.")
        case _ => Some(s"$AB is not an implication")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val nAB = bot.left(i)
      nAB match
        case na_b @ ConnectorFormula(Neg, Seq(ConnectorFormula(And, Seq(a, b)))) => 
          if (isSameSet(bot.right, premises(t1).right `concat` premises(t2).right))
            if (isSameSet(bot.left :+ a :+ b, premises(t1).left `concat` premises(t2).left :+ nAB))
              None
            else Some(s"Left-hand side of conclusion must be identical to union of left-hand sides of premisces + ~(A ∧ B).")
          else Some(s"Right-hand side of conclusion + A + B must be identical to union of right-hand sides of premisces.")
        case _ => Some(s"$nAB is not a negated conjunction")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val nAB = bot.left(i)
      bot.left(i) match
        case na_b @ ConnectorFormula(Neg, Seq(ConnectorFormula(Or, Seq(a, b)))) => 
          if isSameSet(bot.right, premises(t1).right) then
            if isSameSet(bot.left :+ ~a :+ ~b, premises(t1).left :+ nAB) then
              None
            else Some("Left-hand side of conclusion :+ φ∧ψ must be same as left-hand side of premise :+ A, B")
          else Some("Right-hand sides of the premise and the conclusion must be the same.")
        case _ => Some(s"$nAB is not a negated disjunction")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val nAB = bot.left(i)
      nAB match
        case ConnectorFormula(Neg, Seq(ConnectorFormula(Implies, Seq(a, b)))) => 
          if (isSameSet(bot.right, premises(t1).right))
            if (isSameSet(bot.left :+ a :+ ~b, premises(t1).left :+ nAB))
              None
            else Some(s"Left-hand side of conclusion must be identical to union of left-hand sides of premisces + ~(A ⇒ B).")
          else Some(s"Right-hand side of conclusion + A + ~B must be identical to union of right-hand sides of premisces.")
        case _ => Some(s"$nAB is not a negated implication")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val nAB = bot.left(i)
      bot.left(i) match
        case ConnectorFormula(Neg, Seq(ConnectorFormula(Iff, Seq(a, b)))) => 
          val nAimpB = ~(a ==> b)
          val nBimpA = ~(b ==> a)
          if (isSameSet(bot.right, premises(t1).right `concat` premises(t2).right))
            if (isSameSet(bot.left :+ nAimpB :+ nBimpA, premises(t1).left `concat` premises(t2).left :+ nAB))
              None
            else Some(s"Left-hand side of conclusion must be identical to union of left-hand sides of premisces + ~(A ⇔ B).")
          else Some(s"Right-hand side of conclusion + ~(A ⇒ B) + ~(B ⇒ A) must be identical to union of right-hand sides of premisces.")
        case _ => Some(s"$nAB is not a negated biconditional")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val nnA = bot.left(i)
      bot.left(i) match
        case ConnectorFormula(Neg, Seq(ConnectorFormula(Neg, Seq(a)))) => 
          if (isSameSet(bot.right, premises(t1).right))
            if (isSameSet(bot.left :+ a, premises(t1).left :+ nnA))
              None
            else Some(s"Left-hand side of conclusion must be identical to union of left-hand sides of premisces + ¬¬A.")
          else Some(s"Right-hand side of conclusion + A must be identical to union of right-hand sides of premisces.")
        case _ => Some(s"$nnA is not a double negation")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val neA = bot.left(i)
      neA match
        case ConnectorFormula(Neg, Seq(BinderFormula(Exists, x, a))) => 
          val inst = substituteVariablesInFormula(a, Map(x -> t))
          if (isSameSet(bot.right, premises(t1).right))
            if (isSameSet(bot.left :+ ~inst, premises(t1).left :+ neA))
              None
            else Some("Left-hand side of conclusion + A[t/x] must be the same as left-hand side of premise + ∀x. A")
          else Some("Right-hand side of conclusion must be the same as right-hand side of premise")
        case _ => Some("The formula is not a negated existential quantification")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val nfA = bot.left(i)
      bot.left(i) match
        case nall @ ConnectorFormula(Neg, Seq(BinderFormula(Forall, x, a))) => 
          val inst = substituteVariablesInFormula(a, Map(x -> y()))
          if (isSameSet(bot.right, premises(t1).right))
            if (isSameSet(bot.left :+ inst, premises(t1).left :+ nfA))
              if ((bot.left `concat` bot.right).forall(f => !f.freeVariables.contains(x)))
                None
              else Some("The variable x must not be free in the resulting sequent.")
            else Some("Left-hand side of conclusion + A must be the same as left-hand side of premise + ∃x. A")
          else Some("Right-hand side of conclusion must be the same as right-hand side of premise")
        case _ => Some("The formula is not a negated universal quantification")
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

    def checkCorrectness(premises: String => Sequent): Option[String] =
      val (ls, rs) = is.foldLeft(None: Option[(List[Term], List[Term])]) {
        case (Some((list1, list2)), i) =>
          premises(t1).left(i) match
            case AtomicFormula(`equality`, Seq(l, r)) => Some(l :: list1, r :: list2)
            case _ => None
        case (None, _) => None
      } match
        case Some((ls, rs)) => (ls.reverse, rs.reverse)
        case None => return Some("Right substitution failed: equality not found")
      val P_t = substituteVariablesInFormula(P, (xs zip ls).toMap)
      val P_u = substituteVariablesInFormula(P, (xs zip rs).toMap)
      if isSameSet(bot.left, premises(t1).left ++ (ls zip rs).map(_ === _)) then
        if isSameSet(P_t +: bot.right, P_u +: premises(t1).right) then
          None
        else
          Some(s"Right substitution failed: left sides are not correct")
      else
        Some(s"Right substitution failed: right sides are not correct")
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

    def checkCorrectness(premises: String => Sequent): Option[String] =
      val (ls, rs) = is.foldLeft(None: Option[(List[Term], List[Term])]) {
        case (Some((list1, list2)), i) =>
          premises(t1).left(i) match
            case AtomicFormula(`equality`, Seq(l, r)) => Some(l :: list1, r :: list2)
            case _ => None
        case (None, _) => None
      } match
        case Some((ls, rs)) => (ls.reverse, rs.reverse)
        case None => return Some("Left substitution failed: equality not found")
      val P_t = substituteVariablesInFormula(P, (xs zip ls).toMap)
      val P_u = substituteVariablesInFormula(P, (xs zip rs).toMap)
      if isSameSet(P_t +: bot.left, (P_u +: premises(t1).left) ++ (ls zip rs).map(_ === _)) then
        if isSameSet(bot.right, premises(t1).right) then
          None
        else
          Some(s"Left substitution failed: right sides are not correct")
      else
        Some(s"Left substitution failed: left sides are not correct")

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
    def checkCorrectness(premises: String => Sequent): Option[String] =
      try {
        CongruenceClosure.eliminateCongruence(Seq(this))
        return None
      } catch
        case e: Exception => {
          return Some(e.getMessage)
        }
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
  case class Res(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputDoubleIndexes(name, "plain", "res", bot, i1, i2, premises)
    def checkCorrectness(premises: String => Sequent): Option[String] =

      def computeLit(f : Formula, index: Int): (Formula, Seq[Formula]) = {
        f match
          case AtomicFormula(_, _) => (f, Seq())
          case ConnectorFormula(label, args) => {
            label match
              case Neg =>  (f,  Seq())
              case Or => {
                val args_flat = toFlatternOr(f)
                (args_flat(index), args_flat.filterNot(x => x == args_flat(index)))
              } 
              case _ => throw Exception(s"Resolution literal is not correct") 
          }
          case _ => throw Exception(s"Resolution literal is not correct") 
      }    


      val A_Pair = computeLit(premises(t1).left(0), i1)
      
      val B_Pair = computeLit(premises(t2).left(0), i2)

      val Res = {
        bot.left(0) match
          case AtomicFormula(_, _) =>  bot.left
          case ConnectorFormula(label, args) => {
            label match
              case Neg =>  bot.left
              case Or =>  toFlatternOr(bot.left(0))
              case _ => throw Exception(s"Resolution literal is not correct") 
          }
          case _ => throw Exception(s"Resolution literal is not correct") 
      }

      val A = A_Pair._1
      val A_Rest = A_Pair._2
      val B = B_Pair._1
      val B_Rest = B_Pair._2

      // println(s"A = ${A}")
      // println(s"A_rest = ${A_Rest}")
      // println("----------------------")
      // println(s"B = ${B}")
      // println(s"B_rest = ${B_Rest}")
      // println("----------------------")
      // println(s"Res = ${bot.left(0)}")
      // println(s"Res == $$False : ${bot.left(0) == AtomicFormula(AtomicSymbol("$false",0), Seq())}")

      val res = (A, B) match 
        case (AtomicFormula(_, _), ConnectorFormula(Neg,Seq(x))) =>
          (x == A) &&         
          (isSameSet(A_Rest ++ B_Rest, Res) || bot.left(0) == AtomicFormula(AtomicSymbol("$false",0), Seq()))

        case (ConnectorFormula(Neg, Seq(x)), AtomicFormula(_, _)) =>
          x == B &&
          (isSameSet(A_Rest ++ B_Rest, Res) || bot.left(0) == AtomicFormula(AtomicSymbol("$false",0), Seq()))
        case _ => false

      if res then None else Some("Resolution failed")
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
    def checkCorrectness(premises: String => Sequent): Option[String] = None
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
  case class Clausify(name: String, bot: Sequent, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = s"fof(${name}, plain, ${bot}, inference(clausify, [status(thm)], [${t1}])).";
    def checkCorrectness(premises: String => Sequent): Option[String] = None
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
  case class Prenex(name: String, bot: Sequent, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = s"fof(${name}, assumption, ${bot}, inference(prenex_step, [status(thm)], [${t1}])).";
    def checkCorrectness(premises: String => Sequent): Option[String] = None
      // isSubset(bot.left, premises(t1).left) &&
      //   isSubset(bot.right, premises(t1).right)
  }

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
  case class Instantiate_L(name: String, bot: Sequent, i: Int, x: VariableSymbol, t: Term, parent: String) extends StrictLVL2ProofStep {
    val premises = Seq(parent)
    override def toString: String = s"fof(${name}, plain, ${bot}, inference(instantiate, [status(thm), ${i}, $$fot(${x.toString()}), $$fot(${t.toString()})], [${parent}])).";
    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val new_p = substituteVariablesInFormula(premises(parent).left(i), Map(x -> t))

      // println("##################################")
      // println(premises(parent).left(i))
      // println(bot.left)
      // println("----------------------------")
      // println(new_p)
      // println(premises(parent).left)
      // println("----------------------------")
      // println(premises(parent).left(i) +: bot.left)
      // println(new_p +: premises(parent).left)
      // println(isSameSet(premises(parent).left(i) +: bot.left, new_p +: premises(parent).left))
      // println("##################################")

      if isSameSet(premises(parent).left(i) +: bot.left, new_p +: premises(parent).left) then
        if isSameSet(bot.right, premises(parent).right) then
          None
        else
          Some("right sides is not the same")
      else
        Some("left sides is not correct")
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
  case class InstantiateMult(name: String, bot: Sequent, i: Int, terms: Seq[(VariableSymbol, Term)], parent: String) extends StrictLVL2ProofStep {
    val premises = Seq(parent)

    override def toString: String = s"fof(${name}, plain, ${bot}, inference(instantiateMult, [status(thm), ${i}, [${terms.foldLeft(("", 0))((acc, x) => {(acc._1 ++ s"[$$fot(${x(0).toString()}), $$fot(${x(1).toString()})]" ++ (if (acc._2 != terms.length - 1) then ", " else ""), acc._2 + 1)})._1}]], [${parent}])).";

    def checkCorrectness(premises: String => Sequent): Option[String] = 
      val map = terms.foldLeft(Map[sctptp.FOL.VariableSymbol, sctptp.FOL.Term]())((acc, x) => acc + (x._1 -> x._2))
      val new_p =  substituteVariablesInFormula(premises(parent).left(i), map)

      // println("############## INST MULT ####################")
      // println(s"Terms : ${terms}")
      // println("----------------------------")
      // println(premises(parent).left(i))
      // println(bot.left)
      // println("----------------------------")
      // println(new_p)
      // println(premises(parent).left)
      // println("----------------------------")
      // println(premises(parent).left(i) +: bot.left)
      // println(new_p +: premises(parent).left)
      // println(isSameSet(premises(parent).left(i) +: bot.left, new_p +: premises(parent).left))
      // println("##################################")

      if isSameSet(premises(parent).left(i) +: bot.left, new_p +: premises(parent).left) then
        if isSameSet(bot.right, premises(parent).right) then
          None
        else
          Some("right sides is not the same")
      else
        Some("left sides is not correct")
  }


     /**
   *    Γ |-
   * -------------
   *    Δ |-
   *  And Δ is the nnf of Γ
   * @param bot Resulting formula
   */
  case class NNF(name: String, bot: Sequent, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = s"fof(${name}, plain, ${bot}, inference(nnf, [status(thm)], [${t1}])).";
    def checkCorrectness(premises: String => Sequent): Option[String] = None
  }

  case class Let(name: String, bot: Sequent) extends StrictLVL2ProofStep {
    val premises = Seq()
    override def toString: String = s"fof(${name}, let, ${bot.right(0)}).";
    def checkCorrectness(premises: String => Sequent): Option[String] = None
  }
}
