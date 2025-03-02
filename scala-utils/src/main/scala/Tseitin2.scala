package sctptp
import SequentCalculus.*
import Helpers.{*, given}
import LVL2.*
import sctptp.Parser.parseProblem
import java.io.File
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import sctptp.FOL.*

object Tseitin2 {

  def main(args: Array[String]): Unit = {    
    val file = args(0)
    val problem = parseProblem(File(file))
    val proof = certify_tseitin(problem, Prover9.proveProblem)
    println("proof: ")
    println(proof)

    val flattenedProof = flattenProof(proof)
    println("flattened proof: ")
    println(flattenedProof)

    val target = args(1)
    //print to file target
    Files.write(Paths.get(target), flattenedProof.toString.getBytes(StandardCharsets.UTF_8))
  }




  
  
  def certify_clausal(problem: Problem, prover: Problem => SCProof[?]): SCProof[?] = {
    ???
  }

  def certify_negated(problem: Problem, prover: Problem => SCProof[?]): SCProof[?] = {
    val conj = problem.conjecture.goal
    conj match
      case Sequent(Seq(), Seq(c)) =>
        val neg_c = ~(c)
        val ax = Axiom("neg_conjecture", neg_c)
        println(problem.axioms)

        val axiomsWithNegConj = problem.axioms.map(ax => LeftWeaken(ax.name + "_nc", ax.bot +<< neg_c, ax.bot.left.size, ax.name))
        val newProblem = Problem(problem.axioms :+ ax, Conjecture("prove_false", Sequent(Seq(), Seq())))
        val proof = prover(newProblem)

        val axiomsMap = problem.axioms.map {
          case Axiom(name, f) => name -> (name + "_nc")
        }.toMap

        val hyp = Hyp("neg_conjecture", neg_c |- neg_c, 0)
        val sp_neg_conj = Subproof("sp_neg_conj", proof, Seq(neg_c), axiomsMap)
        val nc_elim_1 = Hyp("nc_elim_1", c |- c, 0)
        val nc_elim_2 = RightNot("nc_elim_2", () |- (c, neg_c), 1, nc_elim_1)
        val nc_elim_3 = Cut("nc_elim_3", (sp_neg_conj.proof.steps.last.bot.left) |- c, sp_neg_conj.bot.left.size-1, nc_elim_2, sp_neg_conj)

        val finalsteps = (problem.axioms.toIndexedSeq :+ problem.conjecture ) ++ 
          axiomsWithNegConj ++ 
          Vector(hyp, sp_neg_conj, nc_elim_1, nc_elim_2, nc_elim_3)

        LVL2Proof(finalsteps.asInstanceOf, "test")

      case _ =>
        throw new Exception("Conjecture must be a single formula to apply negated conjecture")
    
  }

  def certify_nnf(problem: Problem, prover: Problem => SCProof[?]): SCProof[?] = {
    ???
  }

  private def isLiteral(f: Formula): Boolean = f match
    case AtomicFormula(_, _) => true
    case ConnectorFormula(Neg, Seq(_)) => true
    case _ => false




  private val HOLE = AtomicSymbol("HOLE", 0)

  def certify_tseitin(problem: Problem, prover: Problem => SCProof[?]): SCProof[?] = {
    assert(problem.conjecture.goal. == (()|-()))
    assert(problem.axioms.forall(ax => ax.bot.right.size == 1 && ax.bot.left.size == 0))

    var i = 0
    def find_pair(f: Formula): (Formula, Formula, Formula, AtomicSymbol, Seq[VariableSymbol]) = f match
      case ConnectorFormula(cn, Seq(f1, f2)) => 
        if isLiteral(f1) then
          if isLiteral(f2) then 
            val freevars = (f1.freeVariables ++ f2.freeVariables).toSeq
            val tsi = AtomicSymbol(s"Ts$i", freevars.size)
            val ts_app = AtomicFormula(tsi, freevars.map(v => FunctionTerm(v, Seq())))
            (f, HOLE(), ts_app, tsi, freevars)
          else 
            val (ts_subst, hole_f, ts_f, tsi, fv) = find_pair(f2)
            (ts_subst, cn(f1, hole_f), cn(f1, ts_f), tsi, fv)
        else
          val (ts_subst, hole_f, ts_f, tsi, fv) = find_pair(f1)
          (ts_subst, cn(hole_f, f2), cn(ts_f, f2), tsi, fv)
      case _ => throw new Exception(s"find_pair only works on connector formulas, not $f")



    def inner_cert_tseitin(axioms: List[Axiom], clausal_axioms:List[Axiom]): IndexedSeq[LVL2ProofStep] = {
      axioms match
        case axiom :: next => 
          if isLiteral(axiom.bot.right.head) then 
            (axiom +: inner_cert_tseitin(next, clausal_axioms :+ axiom))
          else
            i += 1
            val (ts_subst, hole_f, ts_f, tsi, fv) = find_pair(axiom.bot.right.head)
            val tsiapp = tsi(fv.map(_()))
            val ts_axiom = Axiom(axiom.name, ts_f)
            val equiv = Iff(Seq(tsiapp, ts_subst))
            val quantified = fv.foldRight(equiv: Formula)((v, f) => Forall(v, f))
            ts_subst match
              case ConnectorFormula(And, Seq(a, b)) =>
                val ts_clai = "ts_cla"+i
                val ts_clbi = "ts_clb"+i 
                val step1: LVL2ProofStep = Clausify(ts_clai, equiv |- (~tsiapp, a), 0)
                val step2: LVL2ProofStep = Clausify(ts_clbi, equiv |- (~tsiapp, b), 0)
                var j1 = 0
                val miniproof1 = fv.scanRight(step1: LVL2ProofStep)((v, f) => 
                  j1+=1
                  LeftForall(s"ts_claQ${i}_$j1", Forall(v, f.bot.left.head) |- f.bot.right, 0, v(), f.name)
                ).reverse
                var j2 = 0
                val miniproof2 = fv.scanRight(step2: LVL2ProofStep)((v, f) => 
                  j2+=1
                  LeftForall(s"ts_clbQ${i}_$j2", Forall(v, f.bot.left.head) |- f.bot.right, 0, v(), f.name)
                ).reverse
                val ts_ax_just = RightSubstIff("ts_ax_just"+i, equiv |- ts_f, 0, false, hole_f, HOLE, axiom.name)
                var j = 0
                val miniproof = fv.scanRight(ts_ax_just: LVL2ProofStep)((v, f) => 
                  j+=1
                  LeftForall(s"ts_axQ${i}_$j", Forall(v, f.bot.left.head) |- f.bot.right, 0, v(), f.name)
                ).reverse

                var j3 = 0
                val miniproofaxioms = (next ++ clausal_axioms).map(ax =>
                  j3+=1
                  LeftWeaken(ax.name+s"weak${i}_$j", ax.bot +<< quantified, ax.bot.left.size, ax.name)
                )

                val ts_axiom = Axiom(miniproof.last.name, () |- ts_f)
                val ax1 = Axiom(miniproof1.last, () |- (~tsiapp, a))
                val ax2 = Axiom(miniproof2.last, () |- (~tsiapp, b))
                val innerproof = inner_cert_tseitin(ts_axiom :: next, clausal_axioms :+ ax1 :+ ax2)
                val axiommap = (ax1 :: ax2 :: ts_axiom :: Nil).map(ax => ax.name -> ax.name).toMap ++ miniproofaxioms.map(ax => ax.t1 -> ax.name).toMap
                val sub = Subproof("ts_sp"+i, LVL2Proof(innerproof, "thing"), Seq(quantified), axiommap)
                val quant_refl = substitutePredicatesInFormula(quantified, Map(tsi -> (ts_subst, fv)))
                val instStep = InstPred("ts_inst"+i, quant_refl |- (), tsi, (ts_subst, fv), sub)
                val elimStep = ElimIffRefl("ts_elim"+i, () |- (), 0, instStep)

                val steps:Seq[LVL2ProofStep] = axioms ++ clausal_axioms ++ miniproofaxioms ++ miniproof1 ++ miniproof2 ++ miniproof ++ Vector(sub, instStep, elimStep)
                steps.toIndexedSeq
              case _ => ???
          
        case Nil => 
          val newProblem = Problem(clausal_axioms, problem.conjecture)
          prover(newProblem).steps.asInstanceOf[IndexedSeq[LVL2ProofStep]]
      
    }
    LVL2Proof((inner_cert_tseitin(problem.axioms.toList, List())).toIndexedSeq, "name")
  }






}
