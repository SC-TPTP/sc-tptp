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

    val tseitin_prover = certify_tseitin(_, Prover9.proveProblem)
    val prenex_prover = certify_prenex(_, tseitin_prover)
    val skolem_prover = certify_skolem(_, prenex_prover)
    val proof = skolem_prover(problem)
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

  def certify_skolem(problem: Problem, prover: Problem => SCProof[?]): SCProof[?] = {
    /**
      * (
      *   formula replaced exists(v, ...),   Formula
      *   phi(hole(fv)),                         Formula
      *   phi(inner(eps(v, inner))),
      *   phi(inner(Sk(fv))),
      *   Sk
      *   eps(v, inner),
      *   fv
      *   phi(inner(thole(v)))
      *   forall(fv, eps(v, inner) = Sk(fv))
      * )
      */
    def find_epsilon(f:Formula): Option[(Formula, Formula, Formula, Formula, FunctionSymbol, Term, Seq[VariableSymbol], Formula)] = {
      f match
        case BinderFormula(binder, v, inner) =>
          find_epsilon(inner) match
            case Some((f, phi_hole, phi_eps, phi_sko, sko, epsterm, fv, phi_thole)) =>
              def $(inner: Formula) = BinderFormula(binder, v, inner)
              Some((f, $(phi_hole), $(phi_eps), $(phi_sko), sko, epsterm, fv, $(phi_thole)))
            case None if binder == Exists =>
              val freevars = (inner.freeVariables - v).toSeq
              val HOLE = AtomicSymbol("HOLE", freevars.size)
              val HOLEterm = AtomicFormula(HOLE, freevars.map(_()))
              val epsterm = EpsilonTerm(v, inner)
              val sko = FunctionSymbol(s"Sk${v.name}", freevars.size)
              val skoterm = sko(freevars.map(_()))
              val THOLE = FunctionSymbol(s"THOLE", freevars.size)
              val THOLEterm = THOLE(freevars.map(_()))
              val phi_eps = substituteVariablesInFormula(inner, Map(v -> epsterm))
              val phi_sko = substituteVariablesInFormula(inner, Map(v -> skoterm))
              val phi_thole = substituteVariablesInFormula(inner, Map(v -> THOLEterm))
              Some((f, HOLEterm, phi_eps, phi_sko, sko, epsterm, freevars, phi_thole))
            case _ => None
        case AtomicFormula(label, args) => None
        case ConnectorFormula(label, Seq(f1, f2)) => 
          find_epsilon(f1) match
            case None => find_epsilon(f2) match
              case None => None
              case Some((f, phi_hole, phi_eps, phi_sko, sko, epsterm, fv, phi_thole)) =>
                def $(f2: Formula) = ConnectorFormula(label, Seq(f1, f2))
                Some((f, $(phi_hole), $(phi_eps), $(phi_sko), sko, epsterm, fv, $(phi_thole)))
            case Some((f, phi_hole, phi_eps, phi_sko, sko, epsterm, fv, phi_thole)) =>
              def $(f1: Formula) = ConnectorFormula(label, Seq(f1, f2))
              Some((f, $(phi_hole), $(phi_eps), $(phi_sko), sko, epsterm, fv, $(phi_thole)))
        case ConnectorFormula(label, _) => None


          
          
    }
    var i = 0
    def inner_certify_skolem(notdone: List[Axiom], done: List[Axiom]): IndexedSeq[LVL2ProofStep] = {
      notdone match
        case ax :: next => 
          find_epsilon(ax.bot.right.head) match
            case None => 
              inner_certify_skolem(next, done :+ ax)
            case Some((f, phi_hole, phi_eps, phi_sko, sko, epsterm, fv, phi_thole)) =>
              val skoterm = sko(fv.map(_()))
              val equa = AtomicFormula(equality, Seq(epsterm, skoterm))
              val quant_eq = fv.foldRight(equa: Formula)((v, f) => Forall(v, f))
              var j = 0
              val miniproofaxioms = (next ++ done).map(ax =>
                j+=1
                LeftWeaken(ax.name+s"weak${i}_$j", ax.bot +<< quant_eq, ax.bot.left.size, ax.name)
              )
              val HOLE = AtomicSymbol("HOLE", fv.size)
              val epsistep = RightEpsilonSubst("eps_subst"+i, () |- phi_eps, 0, false,
                                               HOLE, (phi_hole, fv), f.asInstanceOf, ax.name)
              val THOLE = FunctionSymbol("THOLE", fv.size)
              val skosubst_step = RightSubstFun("sko_subst"+i, quant_eq |- phi_sko, 0, false, THOLE, (phi_thole, fv), epsistep)
              val skoax = Axiom(ax.name, () |- phi_sko)
              val innerproof = inner_certify_skolem(skoax :: next, done)
              val skoaxmap = Map(skoax.name -> skosubst_step.name) ++ miniproofaxioms.map(ax => ax.t1 -> ax.name).toMap
              val sub = Subproof("sko_sp"+i, LVL2Proof(innerproof, "thing"), Seq(quant_eq), skoaxmap)
              val quantRefl = substituteFunctionsInFormula(quant_eq, Map(sko -> (epsterm, fv)))
              val instStep = InstFun("sko_inst"+i, quantRefl |- (), sko, (epsterm, fv), sub)
              val elimStep = ElimEqRefl("sko_elim"+i, () |- (), 0, instStep)
              val steps:Seq[LVL2ProofStep] = notdone ++ done ++ miniproofaxioms ++ Vector(epsistep, skosubst_step, sub, instStep, elimStep)
              steps.toIndexedSeq
        case Nil =>
          val newProblem = Problem(done, problem.conjecture)
          prover(newProblem).steps.asInstanceOf[IndexedSeq[LVL2ProofStep]]

    }
    LVL2Proof(inner_certify_skolem(problem.axioms.toList, List()).toIndexedSeq, "name")
  }

  private def isLiteral(f: Formula): Boolean = f match
    case AtomicFormula(_, _) => true
    case ConnectorFormula(Neg, Seq(_)) => true
    case _ => false

  

  def matrix(f: Formula, i: Int, map: Map[VariableSymbol, Term]): (Formula, Int) = {
    f match
      case AtomicFormula(label, args) => (AtomicFormula(label, args.map(t => substituteVariablesInTerm(t, map))), i)
      case ConnectorFormula(label, Seq(f1, f2)) => 
        val (p2, i1) = matrix(f2, i, map)
        val (p1, i2) = matrix(f1, i1, map)
        (ConnectorFormula(label, Seq(p1, p2)), i2)
      case ConnectorFormula(Neg, args) => 
        val (p, i1) = matrix(args.head, i, map)
        (ConnectorFormula(Neg, Seq(p)), i1)
      case BinderFormula(Forall, bound, inner) => 
        val newmap = map + (bound -> FunctionTerm(VariableSymbol(s"V$i"), Seq()))
        val (p, i1) = matrix(inner, i+1, newmap)
        (p, i1)
      case _ => ???
  }

  def prenex(f: Formula, i: Int) : (Formula, Int) = 
    val (m, i1) = matrix(f, i, Map())
    val p = m.freeVariables.foldRight(m: Formula)((v, f) => Forall(v, f))
    (p, i1)


  def certify_prenex(problem: Problem, prover: Problem => SCProof[?]): SCProof[?] = {
    assert(problem.conjecture.goal. == (()|-()))
    var i = 0
    val prenex_steps = problem.axioms.map(ax => 
      val (prenexax, i1) = prenex(ax.bot.right.head, i)
      i = i1
      val s1 = Prenex(ax.name+"_pre", prenexax, ax.name)
      def inst_once(f: Formula, prem : String): (LVL2ProofStep, Formula) = f match
        case BinderFormula(Forall, v, inner) => 
          i += 1
          val s2 = InstForall(s"${ax.name}_$v", () |- inner, 0, v, prem)
          (s2, inner)
        case _ => throw new Exception("prenex should only return a formula with foralls")
      var instSteps = List[LVL2ProofStep](s1)
      var next = prenexax
      while next.isInstanceOf[BinderFormula] do
        val (step, inner) = inst_once(next, instSteps.head.name)
        next = inner
        instSteps = step :: instSteps
      val newax = Axiom(ax.name, () |- next)
      val axrefname = if instSteps.length > 0 then instSteps.head.name else s1.name
      (instSteps.reverse, newax, ax.name -> axrefname)
    )
    val newAxioms = prenex_steps.map(_._2)
    val innerproof = prover(Problem(newAxioms, problem.conjecture))
    val axiommap = prenex_steps.map(_._3).toMap
    val sub = Subproof("prenex_sp", innerproof, Seq(), axiommap)
    val steps = problem.axioms ++ prenex_steps.flatMap(_._1) :+ sub
    LVL2Proof(steps.toIndexedSeq, "name")
  }



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
      println(s"clausal: $clausal_axioms")
      axioms match
        case axiom :: next => 
          if isLiteral(axiom.bot.right.head) then 
            (axiom +: inner_cert_tseitin(next, clausal_axioms :+ axiom))
          else
            i += 1
            val (ts_subst, hole_f, ts_f, tsi, fv) = find_pair(axiom.bot.right.head)
            val tsiapp = tsi(fv.map(_()))
            val equiv = Iff(Seq(tsiapp, ts_subst))
            val quantified = fv.foldRight(equiv: Formula)((v, f) => Forall(v, f))
            val ts_ax_just = RightSubstIff("ts_ax_just"+i, equiv |- ts_f, 0, false, hole_f, HOLE, axiom.name)
            var j = 0
            val miniproof = fv.scanRight(ts_ax_just: LVL2ProofStep)((v, f) => 
              j+=1
              LeftForall(s"ts_axQ${i}_$j", Forall(v, f.bot.left.head) |- f.bot.right, 0, v(), f.name)
            ).reverse
            var j3 = 0
            val miniproofaxioms = (next ++ clausal_axioms).map(ax => //TODO : remove next
              j3+=1
              LeftWeaken(ax.name+s"weak${i}_$j3", ax.bot +<< quantified, ax.bot.left.size, ax.name)
            )
            val ts_axiom = Axiom(miniproof.last.name, () |- ts_f)
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
              case ConnectorFormula(Or, Seq(a, b)) =>
                val ts_claci = "ts_clac"+i
                val step1: LVL2ProofStep = Clausify(ts_claci, equiv |- (~tsiapp, a, b), 0)
                var j1 = 0
                val miniproof1 = fv.scanRight(step1: LVL2ProofStep)((v, f) => 
                  j1+=1
                  LeftForall(s"ts_claQ${i}_$j1", Forall(v, f.bot.left.head) |- f.bot.right, 0, v(), f.name)
                ).reverse
                val ax1 = Axiom(miniproof1.last, () |- (~tsiapp, a, b))

                val innerproof = inner_cert_tseitin(ts_axiom :: next, clausal_axioms :+ ax1)
                val axiommap = (ax1 :: ts_axiom :: Nil).map(ax => ax.name -> ax.name).toMap ++ miniproofaxioms.map(ax => ax.t1 -> ax.name).toMap
                val sub = Subproof("ts_sp"+i, LVL2Proof(innerproof, "thing"), Seq(quantified), axiommap)
                val quant_refl = substitutePredicatesInFormula(quantified, Map(tsi -> (ts_subst, fv)))
                val instStep = InstPred("ts_inst"+i, quant_refl |- (), tsi, (ts_subst, fv), sub)
                val elimStep = ElimIffRefl("ts_elim"+i, () |- (), 0, instStep)

                val steps:Seq[LVL2ProofStep] = axioms ++ clausal_axioms ++ miniproofaxioms ++ miniproof1 ++ miniproof ++ Vector(sub, instStep, elimStep)
                steps.toIndexedSeq

              case _ => ???
          
        case Nil => 
          val newProblem = Problem(clausal_axioms, problem.conjecture)
          prover(newProblem).steps.asInstanceOf[IndexedSeq[LVL2ProofStep]]
      
    }
    LVL2Proof((inner_cert_tseitin(problem.axioms.toList, List())).toIndexedSeq, "name")
  }






}
