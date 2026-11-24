package sctptp

import FOL.{*, given}
import SequentCalculus.*
import LVL2.RightSubstFun
import org.scalatest.funsuite.AnyFunSuite

class RightSubstFunTest extends AnyFunSuite {
  private val x = VariableSymbol("X")
  private val y = VariableSymbol("Y")
  private val z = VariableSymbol("Z")
  private val f = FunctionSymbol("f", 2)
  private val g = FunctionSymbol("g", 2)
  private val h = FunctionSymbol("H", 2)
  private val p = AtomicSymbol("p", 1)
  private val eq = Forall(x, Forall(y, equality(f(Variable(x), Variable(y)), g(Variable(y), Variable(x)))))
  private def check(source: Sequent, step: RightSubstFun) = step.checkCorrectness(Map("src" -> source))

  test("pointwise function substitution respects argument order, nested applications, direction and side contexts") {
    def shape(fun: FunctionSymbol, swap: Boolean): Formula = {
      def app(a: Term, b: Term) = if swap then fun(b, a) else fun(a, b)
      Forall(z, p(app(Variable(y), app(Variable(z), Variable(x)))))
    }
    val before = shape(f, false)
    val after = shape(g, true)
    val extra = p(Variable(z))
    val source = Sequent(Seq(extra), Seq(before, extra))
    val goal = Sequent(Seq(extra, eq), Seq(after, extra))
    val step = RightSubstFun("out", goal, 1, false, shape(h, false), h, "src")
    assert(check(source, step) == StepCheckOK)
    assert(check(source.copy(right = goal.right), step.copy(bot = goal.copy(right = source.right), flip = true)) == StepCheckOK)
    for invalid <- Seq(step.copy(flip = true), step.copy(i = -1), step.copy(i = 0), step.copy(i = 2),
        step.copy(bot = goal.copy(right = Seq(shape(g, false), extra))), step.copy(bot = goal.copy(right = Seq(after))),
        step.copy(bot = Sequent(Seq(eq), goal.right), i = 0), step.copy(F = FunctionSymbol("H", 1))) do
      assert(check(source, invalid).isInstanceOf[StepCheckError])
  }

  test("binders in the context cannot capture free variables of a replacement") {
    val hole = FunctionSymbol("H", 1)
    val comparison = Forall(x, equality(Variable(x), Variable(y)))
    val shape = p(EpsilonTerm(y, p(hole(Variable(y)))))
    val before = p(EpsilonTerm(y, p(Variable(y))))
    val after = p(EpsilonTerm(z, p(Variable(y))))
    val source = Sequent(Nil, Seq(before))
    val step = RightSubstFun("out", Sequent(Seq(comparison), Seq(after)), 0, false, shape, hole, "src")
    assert(check(source, step) == StepCheckOK)
    assert(check(source, step.copy(bot = Sequent(Seq(comparison), Seq(before)))).isInstanceOf[StepCheckError])
  }
}
