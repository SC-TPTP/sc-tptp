import FOL.*
import SequentCalculus.Sequent

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

    println("a.toString: " + a.toString())
    println("af_top.toString: " + af_top.toString())
    println("af_bot.toString: " + af_top.toString())
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
    val exists_one_x = BinderFormula(ExistsOne, x_label, a)

    println("forall_x.toString: " + forall_x.toString())
    println("exists_x.toString: " + exists_x.toString())
    println("exists_one_x.toString: " + exists_one_x.toString())


    // Sequents
    val s1 = Sequent(Seq(a, a, a), Seq(a))

    println("s1: " + s1.toString())

    // Proof step
    // val ps1 = SCProofStep()

    // val myseq = SCProof

    // for (n <- nums)
    //     println(n)
  }
}
