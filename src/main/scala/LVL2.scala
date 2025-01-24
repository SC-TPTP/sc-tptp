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
  case class RightSubstMulti(name: String, bot: Sequent, is: List[Int], j: Int, P: Formula, xs: List[VariableSymbol], t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubstMany(name, "rightSubstMulti", bot, is, j, P.toString(), xs.map(_.toString()), premises)

    def checkCorrectness(premises: String => Sequent): Boolean =       
      val (ls, rs) = is.foldLeft(None: Option[(List[Term], List[Term])]){
        case (Some((list1, list2)), i) => premises(t1).left(i) match 
          case AtomicFormula(`equality`, Seq(l, r)) => Some(l::list1, r::list2)
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
  case class LeftSubstMulti(name: String, bot: Sequent, is: List[Int], j: Int, P: Formula, xs: List[VariableSymbol], t1: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1)
    override def toString: String = SCProofStep.outputWithSubstMany(name, "leftSubstMulti", bot, is, j, P.toString(), xs.map(_.toString()), premises)

    def checkCorrectness(premises: String => Sequent): Boolean = 
      val (ls, rs) = is.foldLeft(None: Option[(List[Term], List[Term])]){
        case (Some((list1, list2)), i) => premises(t1).left(i) match 
          case AtomicFormula(`equality`, Seq(l, r)) => Some(l::list1, r::list2)
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
      try{
        CongruenceClosure.eliminateCongruence(Seq(this))
        return true
      } catch case e: Exception => {
        return false
      }
  }

  case class Res(name: String, bot: Sequent, i1: Int, i2: Int, t1: String, t2: String) extends StrictLVL2ProofStep {
    val premises = Seq(t1, t2)
    override def toString: String = SCProofStep.outputDoubleIndexes(name, "res", "plain", bot, i1, i2, premises)
    def checkCorrectness(premises: String => Sequent): Boolean = 
      val A = premises(t1).left(i1)
      val negA = premises(t2).left(i2)

      (negA == ~A) &&
      (isSameSet(A +: bot.left, premises(t1).left) && 
      isSameSet(bot.right, premises(t1).right) &&
      isSameSet(negA +: bot.left, premises(t2).left) && 
      isSameSet(bot.right, premises(t2).right)) ||
      (isSameSet(negA +: bot.left, premises(t1).left) && 
      isSameSet(bot.right, premises(t1).right) &&
      isSameSet(A +: bot.left, premises(t2).left) && 
      isSameSet(bot.right, premises(t2).right))
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
    override def toString: String = s"fof(${name}, negated_conjecture, ${bot}, inference(negated_conjecture, [status(thm)], [1])).";
    def checkCorrectness(premises: String => Sequent): Boolean = 
      isSubset(bot.left, premises(t1).left) && 
      isSubset(bot.right, premises(t1).right)
      
  }

}