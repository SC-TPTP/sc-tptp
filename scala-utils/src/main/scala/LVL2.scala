package sctptp

import FOL.*
import SequentCalculus.*

object LVL2 {
  sealed trait StrictLVL2ProofStep extends SCProofStep
  type LVL2ProofStep = StrictLVL2ProofStep | LVL1ProofStep

  case class LVL2Proof(steps: IndexedSeq[LVL2ProofStep], thmName: String) extends SCProof[LVL2ProofStep]

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

    def checkCorrectness(premises: String => Sequent): Boolean =
      val (ls, rs) = is.foldLeft(None: Option[(List[Term], List[Term])]) {
        case (Some((list1, list2)), i) =>
          premises(t1).left(i) match
            case AtomicFormula(`equality`, Seq(l, r)) => Some(l :: list1, r :: list2)
            case _ => None
        case (None, _) => None
      } match
        case Some((ls, rs)) => (ls.reverse, rs.reverse)
        case None => return false
      val P_t = substituteVariablesInFormula(P, (xs zip ls).toMap)
      val P_u = substituteVariablesInFormula(P, (xs zip rs).toMap)
      isSameSet(bot.left, premises(t1).left ++ (ls zip rs).map(_ === _)) &&
      isSameSet(P_t +: bot.right, P_u +: premises(t1).right)
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

    def checkCorrectness(premises: String => Sequent): Boolean =
      val (ls, rs) = is.foldLeft(None: Option[(List[Term], List[Term])]) {
        case (Some((list1, list2)), i) =>
          premises(t1).left(i) match
            case AtomicFormula(`equality`, Seq(l, r)) => Some(l :: list1, r :: list2)
            case _ => None
        case (None, _) => None
      } match
        case Some((ls, rs)) => (ls.reverse, rs.reverse)
        case None => return false
      val P_t = substituteVariablesInFormula(P, (xs zip ls).toMap)
      val P_u = substituteVariablesInFormula(P, (xs zip rs).toMap)
      isSameSet(P_t +: bot.left, (P_u +: premises(t1).left) ++ (ls zip rs).map(_ === _)) &&
      isSameSet(bot.right, premises(t1).right)

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
    def checkCorrectness(premises: String => Sequent): Boolean =
      try {
        CongruenceClosure.eliminateCongruence(Seq(this))
        return true
      } catch
        case e: Exception => {
          return false
        }
  }

    /**
   * -----------------
   *   Γ, A |- A, Δ
   *
   * @param bot Resulting formula
   **/
  // Resolution rule --- between t1:i1 and t2:i2
  case class Res(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputDoubleIndexes(name, "plain", "res", bot, i1, i2, premises)
    def checkCorrectness(premises: String => Sequent): Boolean =
      val A = premises(t1).left(i1)
      val B = premises(t2).left(i2)

      (A, B) match 
        case (AtomicFormula(_, _), ConnectorFormula(Neg,Seq(x))) =>
          (x == A) &&
          (isSameSet(A +: bot.left, premises(t1).left) &&
            isSameSet(bot.right, premises(t1).right) &&
            isSameSet(B +: bot.left, premises(t2).left) &&
            isSameSet(bot.right, premises(t2).right)) ||
          (isSameSet(B +: bot.left, premises(t1).left) &&
            isSameSet(bot.right, premises(t1).right) &&
            isSameSet(A +: bot.left, premises(t2).left) &&
            isSameSet(bot.right, premises(t2).right)) || 
            bot.left(0) == AtomicFormula(AtomicSymbol("$false", 0), Nil)
        case (ConnectorFormula(Neg, Seq(x)), AtomicFormula(_, _)) =>
          x == B &&
          (isSameSet(A +: bot.left, premises(t1).left) &&
            isSameSet(bot.right, premises(t1).right) &&
            isSameSet(B +: bot.left, premises(t2).left) &&
            isSameSet(bot.right, premises(t2).right)) ||
          (isSameSet(B +: bot.left, premises(t1).left) &&
            isSameSet(bot.right, premises(t1).right) &&
            isSameSet(A +: bot.left, premises(t2).left) &&
            isSameSet(bot.right, premises(t2).right)) ||
            bot.left(0) == AtomicFormula(AtomicSymbol("$false", 0), Nil)
        case _ => false
  }

  /**
   *    Γ |-
   * -------------
   *    Δ |-
   *  And Δ is a subset of Γ
   * @param bot Resulting formula
   */
  case class NegatedConjecture(name: String, bot: Sequent, t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = s"fof(${name}, assumption, ${bot}, inference(clausify, [status(thm)], [])).";
    def checkCorrectness(premises: String => Sequent): Boolean = true
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
    override def toString: String = s"fof(${name}, plain, ${bot}, inference(instantiate_l, [status(thm), ${i}, $$fot(${x.toString()}), $$fot(${t.toString()})], [${parent}])).";
    def checkCorrectness(premises: String => Sequent): Boolean = 
      bot.left(i) match
        case AtomicFormula(p, args) => {
          val new_p = substituteVariablesInFormula(bot.left(i), Map(x -> t))
          isSameSet(bot.left(i) +: bot.left, new_p +: premises(parent).left) && isSameSet(bot.right, premises(parent).right)
        }
        case ConnectorFormula(Neg, Seq(a)) => {
          val new_p = substituteVariablesInFormula(bot.left(i), Map(x -> t))
          isSameSet(bot.left(i) +: bot.left, new_p +: premises(parent).left) && isSameSet(bot.right, premises(parent).right)
        }
        case _ => false 
  }
}
