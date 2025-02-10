package sctptp

import java.io.File
import Parser.*
import FOL.*
import SequentCalculus.*
import CoqOutput.CoqProof
import sctptp.SequentCalculus.SCProofStep
import sctptp.Tseitin
import sctptp.FOL.iff

object Test {

  def main(args: Array[String]): Unit = {
    // println("\n First proof:")
    // println(reconstructProof(new File("proofs/Test.gothm0.p")).toString())
    // println("\n Second proof:")
    // println(reconstructProof(new File("proofs/Test.gothm1.p")).toString())

    // val problem = reconstructProof(new File("proofs/Test.gothm0.p"))
    // val problem = reconstructProof(new File("proofs/Test.gothm1.p"))
    // val problem = reconstructProof(new File("proofs/Test.gothm2.p"))
    // val problem = reconstructProof(new File("proofs/Test.gothm3.p"))
    // val problem = reconstructProof(new File("proofs/Test.gothm4.p"))
    // val problem = reconstructProof(new File("proofs/Test.gothm5.p"))
    // val problem = reconstructProof(new File("proofs/Test.gothm6.p"))
    // val problem = reconstructProof(new File("proofs/Test.gothm7.p"))
    // val problem = reconstructProof(new File("proofs/Test.gothm8.p"))
    // val problem = reconstructProof(new File("proofs/Test.gothm9.p"))
    // val problem = reconstructProof(new File("proofs/Test.gothm10.p"))
    // val problem = reconstructProof(new File("proofs/example.out"))
    // val problem = reconstructProof(new File("proofs/clausified.p"))
    // val problem = reconstructProof(new File("proofs/big.p"))
    
    // println("\nProblem TPTP:")
    // println(problem.toString())
    // println(s"CheckProof : ${checkProof(problem)}")


    // println("\nProblem Coq:")
    // val problemCoq = CoqProof(problem, Map.empty[String, Int], Map.empty[String, Int])
    // println(problemCoq.toString())

    testSC()
  }

  def testSC(): Unit = {
    // // Terms
    // val x_id = Identifier("X")
    // val x_label = VariableSymbol(x_id)
    // val x = Variable(x_label)

    // val a_id = Identifier("a")
    // val a_term_label = FunctionSymbol(a_id, 0)
    // val a_term = Term(a_term_label, Seq())

    // val f_id = Identifier("f")
    // val f_label = FunctionSymbol(f_id, 3)
    // val fx = Term(f_label, Seq(x, a_term, x))

    // println("x.toString: " + x.toString())
    // println("a_term.toString: " + a_term.toString())
    // println("fx.toString: " + fx.toString())

    // // Atomic Formulas
    // val a_atomic_label = AtomicSymbol(a_id, 0)
    // val a = AtomicFormula(a_atomic_label, Seq())
    // val af_top = AtomicFormula(top, Seq())
    // val af_bot = AtomicFormula(bot, Seq())
    // val eq_a_a = AtomicFormula(equality, Seq(a_term, a_term))
    // val p_id = Identifier("p")
    // val p_atomic_label = AtomicSymbol(p_id, 2)
    // val p_a_x = AtomicFormula(p_atomic_label, Seq(a_term, x))

    // println("a.toString: " + a.toString())
    // println("p_a_x.toString: " + p_a_x.toString())
    // println("af_top.toString: " + af_top.toString())
    // println("af_bot.toString: " + af_bot.toString())
    // println("eq_a_a.toString: " + eq_a_a.toString())

    // // Connector formulas
    // val neg_a = ConnectorFormula(Neg, Seq(a))
    // val imp_a_a = ConnectorFormula(Implies, Seq(a, a))
    // val iff_a_a = ConnectorFormula(Iff, Seq(a, a))
    // val and_a_a_a = ConnectorFormula(And, Seq(a, a, a))
    // val or_a_a_a = ConnectorFormula(Or, Seq(a, a, a))

    // println("neg_a.toString: " + neg_a.toString())
    // println("imp_a.toString: " + imp_a_a.toString())
    // println("iff_a.toString: " + iff_a_a.toString())
    // println("and_a.toString: " + and_a_a_a.toString())
    // println("or_a.toString: " + or_a_a_a.toString())

    // // Binder formulas
    // val forall_x = BinderFormula(Forall, x_label, a)
    // val exists_x = BinderFormula(Exists, x_label, a)

    // println("forall_x.toString: " + forall_x.toString())
    // println("exists_x.toString: " + exists_x.toString())

    // // Sequents
    // val s1 = Sequent(Seq(a, a, a), Seq(a))
    
    // println("s1: " + s1.toString())


  //   // Proof step
  //   val ax1 = Axiom("ax1", s1)
  //   val hyp = Hyp("hyp", s1, 0, 1)
  //   val leftHyp = LeftHyp("leftHyp", s1, 0, 1)
  //   val weakening = LeftWeakening("weakening", s1,"p1")
  //   val cut = Cut("cut", s1, 0, 1, "p1", "p2")
  //   val leftAnd = LeftAnd("leftAnd", s1, 0, "p1")
  //   val leftOr = LeftOr("leftOr", s1, 0, "p1", "p2")
  //   val leftImp1 = LeftImp1("leftImp1", s1, 0, "p1", "p2")
  //   val leftImp2 = LeftImp2("leftImp2", s1, 0, "p1", "p2")
  //   val leftIff = LeftIff("leftIff", s1, 0, "p1")
  //   val leftNot = LeftNot("leftNot", s1, 0, "p1")
  //   val leftEx = LeftEx("leftEx", s1, 0, x_label, "p1")
  //   val leftAll = LeftAll("leftAll", s1, 0, a_term, "p1")
  //   val rightAnd = RightAnd("rightAnd", s1, 0, "p1", "p2")
  //   val rightOr = RightOr("rightOr", s1, 0, "p1")
  //   val rightImp = RightImp("rightImp", s1, 0, "p1")
  //   val rightIff = RightIff("rightIff", s1, 0, "p1", "p2")
  //   val rightNot = RightNot("rightNot", s1, 0, "p1")
  //   val rightEx = RightEx("rightEx", s1, 0, a_term, "p1")
  //   val rightAll = RightAll("rightAll", s1, 0, x_label, "p1")
  //   val leftNotAnd = LeftNotAnd("leftNotAnd", s1, 0, "p1", "p2")
  //   val leftNotOr = LeftNotOr("leftNotOr", s1, 0, "p1")
  //   val leftNotImp = LeftNotImp("leftNotImp", s1, 0, "p1")
  //   val leftNotIff = LeftNotIff("leftNotIff", s1, 0, "p1", "p2")
  //   val leftNotNot = LeftNotNot("leftNotNot", s1, 0, "p1")
  //   val leftNotEx = LeftNotEx("leftNotEx", s1, 0, a_term, "p1")
  //   val leftNotAll = LeftNotAll("leftNotAll", s1, 0, x_label, "p1")

  //   val rightRefl = RightRefl("rightRefl", s1, 0)
  //   val leftSubst = LeftSubst("leftSubst", s1, 0, 1, p_a_x, x_label, "p1")
  //   val rightSubst = RightSubst("rightSubst", s1, 0, 1, p_a_x, x_label, "p1")

  //   println("ax1: " + ax1.toString())
  //   println("hyp: " + hyp.toString())
  //   println("leftHyp: " + leftHyp.toString())
  //   println("leftWeakening: " + weakening.toString())
  //   println("cut: " + cut.toString())
  //   println("leftAnd: " + leftAnd.toString())
  //   println("leftOr: " + leftOr.toString())
  //   println("leftImp1: " + leftImp1.toString())
  //   println("leftImp2: " + leftImp2.toString())
  //   println("leftIff: " + leftIff.toString())
  //   println("leftNot: " + leftNot.toString())
  //   println("leftEx: " + leftEx.toString())
  //   println("leftAll: " + leftAll.toString())
  //   println("rightAnd: " + rightAnd.toString())
  //   println("rightOr: " + rightOr.toString())
  //   println("rightImp: " + rightImp.toString())
  //   println("rightIff: " + rightIff.toString())
  //   println("rightNot: " + rightNot.toString())
  //   println("rightEx: " + rightEx.toString())
  //   println("rightAll: " + rightAll.toString())
  //   println("leftNotAnd: " + leftNotAnd.toString())
  //   println("leftNotOr: " + leftNotOr.toString())
  //   println("leftNotImp: " + leftNotImp.toString())
  //   println("leftNotIff: " + leftNotIff.toString())
  //   println("leftNotNot: " + leftNotNot.toString())
  //   println("leftNotEx: " + leftNotEx.toString())
  //   println("leftNotAll: " + leftNotAll.toString())
  //   println("rightRefl: " + rightRefl.toString())
  //   println("leftSubst: " + leftSubst.toString())
  //   println("rightSubst: " + rightSubst.toString())




  val problem = reconstructProof(new File("../proofs/clausification/clause_parse.p"))
  val parsedProblem = problem.getSequent(0).right(0)
  println("Formula : " + parsedProblem)

  
  val myTseitin = new Tseitin()
  // Creation of tseitin terms
  val premap2 = myTseitin.createTseitinVariables(parsedProblem)
  myTseitin.makeTseitinMaps(premap2._1)
  myTseitin.printTseitinVarTerm()

  // Take the negation (for tests)
  val parsedProblem2 = ConnectorFormula(Neg , Seq(parsedProblem))
  println("Formula : " + parsedProblem2)

  // NNF
  val parsedProblem3 = myTseitin.toNNF(parsedProblem2)
  println("Formula in NNF Form : " + parsedProblem3)

  // Prenex
    val parsedProblem4 = myTseitin.toPrenex(parsedProblem3)
  println("Formula in Prenex Form : " + parsedProblem4)

  }
}
