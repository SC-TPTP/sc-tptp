package sctptp

import FOL.{*, given}
import SequentCalculus.*
import org.scalatest.funsuite.AnyFunSuite

class RightSubstIffForallTest extends AnyFunSuite {
  private val x = VariableSymbol("X")
  private val y = VariableSymbol("Y")
  private val z = VariableSymbol("Z")
  private val t = FunctionSymbol("t", 0)()
  private val u = FunctionSymbol("u", 0)()
  private val p = AtomicSymbol("p", 1)
  private val q = AtomicSymbol("q", 1)
  private val rel = AtomicSymbol("r", 2)
  private val hole = AtomicSymbol("H", 0)

  private def check(shape: Formula, before: Formula, after: Formula, axiom: Formula): StepCheck = {
    val premises = Map("src" -> Sequent(Nil, Seq(before)), "equiv" -> Sequent(Nil, Seq(axiom)))
    RightSubstIffForall("out", Sequent(Nil, Seq(after)), 0, shape, hole, "src", "equiv").checkCorrectness(premises)
  }

  test("infer a single instance across both sides of the equivalence, including repeated variables") {
    val axiom = Forall(x, rel(Variable(x), Variable(x)) <=> q(Variable(x)))
    assert(check(hole(), rel(t, t), q(t), axiom) == StepCheckOK)
    assert(check(hole(), rel(t, u), q(t), axiom).isInstanceOf[StepCheckError])
    assert(check(hole(), rel(t, t), q(u), axiom).isInstanceOf[StepCheckError])
    assert(check(hole(), rel(t, t), q(t), Exists(x, rel(Variable(x), Variable(x)) <=> q(Variable(x)))).isInstanceOf[StepCheckError])
  }

  test("a context binder may supply an argument but cannot capture a free variable of the equivalence") {
    val shape = Exists(y, hole())
    val before = Exists(z, p(Variable(z)))
    val axiom = Forall(x, p(Variable(x)) <=> rel(Variable(x), Variable(y)))
    assert(check(shape, before, Exists(z, rel(Variable(z), Variable(y))), axiom) == StepCheckOK)
    assert(check(shape, before, Exists(z, rel(Variable(z), Variable(z))), axiom).isInstanceOf[StepCheckError])
    val inner = Forall(x, Exists(y, rel(Variable(x), Variable(y))) <=> q(Variable(x)))
    assert(check(hole(), Exists(y, rel(Variable(y), Variable(y))), q(t), inner).isInstanceOf[StepCheckError])
  }

  test("check the selected equivalence and preserve side formulas from both premises") {
    val eq = Forall(x, p(Variable(x)) <=> q(Variable(x)))
    val source = Sequent(Seq(p(u)), Seq(p(t), p(u)))
    val comparison = Sequent(Seq(q(u)), Seq(q(u), eq))
    val premises = Map("src" -> source, "equiv" -> comparison)
    val goal = Sequent(Seq(p(u), q(u)), Seq(p(u), q(u), q(t)))
    val step = RightSubstIffForall("out", goal, 1, hole(), hole, "src", "equiv")
    assert(step.checkCorrectness(premises) == StepCheckOK)
    for i <- Seq(-1, 0, 2) do assert(step.copy(i = i).checkCorrectness(premises).isInstanceOf[StepCheckError])
    for f <- Seq(p(u), q(u)) do {
      assert(step.copy(bot = goal -<< f).checkCorrectness(premises).isInstanceOf[StepCheckError])
      assert(step.copy(bot = goal ->> f).checkCorrectness(premises).isInstanceOf[StepCheckError])
    }
    assert(step.copy(context = p(t)).checkCorrectness(premises).isInstanceOf[StepCheckError])
  }
  test("alpha-equivalence renames binders and preserves indexed predicates") {
    val first = AtomicSymbol(Identifier("pred", 1), 1)
    val second = AtomicSymbol(Identifier("pred", 2), 1)
    val original = Forall(x, first(Variable(x)))
    assert(isSame(original, Forall(y, first(Variable(y)))))
    assert(!isSame(original, Forall(y, second(Variable(y)))))
  }

  test("printed indexed predicates and a marked context round-trip through the parser") {
    val first = AtomicSymbol(Identifier("pred", 1), 1)
    val second = AtomicSymbol(Identifier("pred", 2), 1)
    val marker = AtomicSymbol(Identifier("H", 3), 0)
    val assumption = AtomicSymbol(Identifier("a", 4), 0)()
    val source = Axiom("src", Sequent(Seq(assumption), Seq(first(t))))
    val definition = Axiom("defn", Sequent(Nil, Seq(Forall(x, first(Variable(x)) <=> second(Variable(x))))))
    val rewrite = RightSubstIffForall("out", Sequent(Seq(assumption), Seq(second(t))), 0, marker(), marker, source.name, definition.name)
    val proof = LVL3Proof(Vector(source, definition, rewrite), "indexed")
    assert(checkProof(proof) == StepCheckOK)
    val file = java.nio.file.Files.createTempFile("quantified-rewrite", ".p")
    try {
      java.nio.file.Files.writeString(file, ProofToString(proof))
      val parsed = Parser.reconstructProof(file.toFile)
      assert(parsed.steps == proof.steps)
      assert(checkProof(parsed) == StepCheckOK)
    } finally java.nio.file.Files.delete(file)
  }
}
