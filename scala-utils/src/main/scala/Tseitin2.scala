package sctptp
import SequentCalculus.*
import Helpers.{*, given}
import LVL2.*
object Tseitin2 {




  
  
  def certify_clausal(problem: Problem, prover: Problem => SCProof[?]): SCProof[?] = {
    ???
  }

  def certify_negated(problem: Problem, prover: Problem => SCProof[?]): SCProof[?] = {
    val conj = problem.conjecture.goal
    conj match
      case Sequent(Seq(), Seq(c)) =>
        val neg_c = ~(c)
        val ax = Axiom("neg_conjecture", neg_c)
        val newAxioms = problem.axioms.map {
          case Axiom(name, f) => Axiom("nc_" + name, f)
        }
        val newProblem = Problem(problem.axioms :+ ax, Conjecture("prove_false", Sequent(Seq(), Seq())))
        val proof = prover(newProblem)

        val axiomsMap = newAxioms.map {
          case Axiom(name, f) => name -> name
        }.toMap

        val hyp = Hyp("neg_conjecture", neg_c |- neg_c, 0)
        val sp_neg_conj = Subproof("sp_neg_conj", proof, Seq(neg_c), axiomsMap)
        val nc_elim_1 = Hyp("nc_elim_1", c |- c, 0)
        val nc_elim_2 = RightNot("nc_elim_2", () |- (c, neg_c), 1, nc_elim_1)
        val nc_elim_3 = Cut("nc_elim_3", (sp_neg_conj.proof.steps.last.bot.left) |- c, sp_neg_conj.bot.left.size-1, sp_neg_conj, nc_elim_2)

        LVL2Proof(Vector(hyp, sp_neg_conj, nc_elim_1, nc_elim_2, nc_elim_3), "test")

      case _ =>
        throw new Exception("Conjecture must be a single formula to apply negated conjecture")
    
  }

  def certify_nnf(problem: Problem, prover: Problem => SCProof[?]): SCProof[?] = {
    ???
  }

  def certify_prenex(problem: Problem, prover: Problem => SCProof[?]): SCProof[?] = {
    ???
  }






}
