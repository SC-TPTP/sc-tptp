import FOL.*
import SequentCalculus.*

object Printer {
  def main(args: Array[String]) = {

    // Terms
    val x_id = Identifier("X")
    val x_label = VariableLabel(x_id)
    val x = Variable(x_label)

    val a_id = Identifier("a")
    val a_term_label = FunctionLabel(a_id, 0)
    val a_term = Term(a_term_label, Seq())

    val f_id = Identifier("f")
    val f_label = FunctionLabel(f_id, 3)
    val fx = Term(f_label, Seq(x, a_term, x))

    println("x.toString: " + x.toString())
    println("a_term.toString: " + a_term.toString())
    println("fx.toString: " + fx.toString())

    // Atomic Formulas
    val a_atomic_label = AtomicLabel(a_id, 0)
    val a = AtomicFormula(a_atomic_label, Seq())
    val af_top = AtomicFormula(top, Seq())
    val af_bot = AtomicFormula(bot, Seq())
    val eq_a_a = AtomicFormula(equality, Seq(a_term, a_term))
    val p_id = Identifier("p")
    val p_atomic_label = AtomicLabel(p_id, 2)
    val p_a_x = AtomicFormula(p_atomic_label, Seq(a_term, x))

    println("a.toString: " + a.toString())
    println("p_a_x.toString: " + p_a_x.toString())
    println("af_top.toString: " + af_top.toString())
    println("af_bot.toString: " + af_bot.toString())
    println("eq_a_a.toString: " + eq_a_a.toString())
    
    // Connector formulas
    val neg_a = ConnectorFormula(Neg, Seq(a))
    val imp_a_a = ConnectorFormula(Implies, Seq(a, a))
    val iff_a_a = ConnectorFormula(Iff, Seq(a, a))
    val and_a_a_a = ConnectorFormula(And, Seq(a, a, a))
    val or_a_a_a = ConnectorFormula(Or, Seq(a, a, a))
   
    println("neg_a.toString: " + neg_a.toString())
    println("imp_a.toString: " + imp_a_a.toString())
    println("iff_a.toString: " + iff_a_a.toString())
    println("and_a.toString: " + and_a_a_a.toString())
    println("or_a.toString: " + or_a_a_a.toString())

    // Binder formulas
    val forall_x = BinderFormula(Forall, x_label, a)
    val exists_x = BinderFormula(Exists, x_label, a)

    println("forall_x.toString: " + forall_x.toString())
    println("exists_x.toString: " + exists_x.toString())


    // Sequents
    val s1 = Sequent(Seq(a, a, a), Seq(a))

    println("s1: " + s1.toString())

    // Proof step
    val ax1 = Axiom("ax1", s1)
    val hyp = Hyp("hyp", s1, 0, 1)
    val leftHyp = LeftHyp("leftHyp", s1, 0, 1)
    val leftWeaken = LeftWeakening("leftWeaken", s1, 0, "p1")
    val rightWeaken = RightWeakening("rightWeaken", s1, 0, "p1")
    val cut = Cut("cut", s1, 0, 1, "p1", "p2")
    val leftAnd = LeftAnd("leftAnd", s1, 0, "p1")
    val leftOr = LeftOr("leftOr", s1, 0, "p1", "p2")
    val leftImp1 = LeftImp1("leftImp1", s1, 0, "p1", "p2")
    val leftImp2 = LeftImp2("leftImp2", s1, 0, "p1", "p2")
    val leftIff = LeftIff("leftIff", s1, 0, "p1")
    val leftNot = LeftNot("leftNot", s1, 0, "p1")
    val leftEx = LeftEx("leftEx", s1, 0, x_label, "p1")


    println("ax1: " + ax1.toString())
    println("hyp: " + hyp.toString())
    println("leftHyp: " + leftHyp.toString())
    println("leftWeakening: " + leftWeaken.toString())
    println("rightWeakening: " + rightWeaken.toString())
    println("cut: " + cut.toString())



    // val myseq = SCProof

    // for (n <- nums)
    //     println(n)
  }
}
