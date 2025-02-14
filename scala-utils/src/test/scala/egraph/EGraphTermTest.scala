
import datastructure.mutable.*
import org.scalatest.compatible.Assertion
import org.scalatest.funsuite.AnyFunSuite

import sctptp.FOL.{*, given}
import sctptp.FOL
import sctptp.Parser.{given}
import sctptp.SequentCalculus as SC


private object Constants {

  def Term(lab: String, args: List[Term]): Term = FOL.Term(FunctionSymbol(lab, args.length), args)
  def Term(lab: TermSymbol, args: List[Term]): Term = FOL.Term(lab, args)
  def AtomicFormula(lab: String, args: List[Term]): AtomicFormula = AtomicFormula(AtomicSymbol(lab, args.length), args)
  def AtomicFormula(lab: AtomicSymbol, args: List[Term]): AtomicFormula = FOL.AtomicFormula(lab, args)

  val a = FunctionSymbol("a", 0)
  val b = FunctionSymbol("b", 0)
  val c = FunctionSymbol("c", 0)
  val d = FunctionSymbol("d", 0)
  val e = FunctionSymbol("e", 0)
  val f = FunctionSymbol("f", 0)
  val g = FunctionSymbol("g", 0)
  val h = FunctionSymbol("h", 0)
  val i = FunctionSymbol("i", 0)
  val j = FunctionSymbol("j", 0)
  val k = FunctionSymbol("k", 0)
  val l = FunctionSymbol("l", 0)
  val m = FunctionSymbol("m", 0)
  val n = FunctionSymbol("n", 0)
  val o = FunctionSymbol("o", 0)
  val x = FunctionSymbol("x", 0)


  val F = FunctionSymbol("F", 1)
}

class EGraphTermTest extends AnyFunSuite {
  import Constants.*


  test("3 elements no congruence egraph test") {
    val egraph = new EGraphTerms()
    val id1 = egraph.add(Term(a, List()))
    val id2 = egraph.add(Term(b, List()))
    val id3 = egraph.add(Term(c, List()))
    egraph.merge(id1, id2)
    assert(egraph.idEq(id1, id2))
    assert(!egraph.idEq(id1, id3))
    
  }
  test("8 elements no congruence egraph test") {
    val egraph = new EGraphTerms()
    val id1 = egraph.add(Term(a, List()))
    val id2 = egraph.add(Term(b, List()))
    val id3 = egraph.add(Term(c, List()))
    val id4 = egraph.add(Term(d, List()))
    val id5 = egraph.add(Term(e, List()))
    val id6 = egraph.add(Term(f, List()))
    val id7 = egraph.add(Term(g, List()))
    val id8 = egraph.add(Term(h, List()))
    egraph.merge(id1, id2)
    egraph.merge(id3, id4)
    egraph.merge(id5, id6)
    egraph.merge(id7, id8)
    egraph.merge(id1, id3)
    egraph.merge(id6, id8)
    egraph.merge(id1, id6)
    assert(egraph.idEq(id1, id8))
    
  }

  test("15 elements no congruence egraph test") {
    val egraph = new EGraphTerms()
    val id1 = egraph.add(Term(a, List()))
    val id2 = egraph.add(Term(b, List()))
    val id3 = egraph.add(Term(c, List()))
    val id4 = egraph.add(Term(d, List()))
    val id5 = egraph.add(Term(e, List()))
    val id6 = egraph.add(Term(f, List()))
    val id7 = egraph.add(Term(g, List()))
    val id8 = egraph.add(Term(h, List()))
    val id9 = egraph.add(Term(i, List()))
    val id10 = egraph.add(Term(j, List()))
    val id11 = egraph.add(Term(k, List()))
    val id12 = egraph.add(Term(l, List()))
    val id13 = egraph.add(Term(m, List()))
    val id14 = egraph.add(Term(n, List()))
    val id15 = egraph.add(Term(o, List()))
    egraph.merge(id1, id3)
    egraph.merge(id5, id6)
    egraph.merge(id9, id11)
    egraph.merge(id13, id14)
    egraph.merge(id1, id2)
    egraph.merge(id15, id13)
    egraph.merge(id9, id13)
    egraph.merge(id7, id8)
    egraph.merge(id12, id11)
    egraph.merge(id3, id4)
    egraph.merge(id1, id5)
    egraph.merge(id1, id9)
    egraph.merge(id7, id5)
    egraph.merge(id9, id10)
    assert(egraph.idEq(id1, id15))
    
  }

  test("15 elements no congruence egraph test with redundant merges") {
    val egraph = new EGraphTerms()
    val id1 = egraph.add(Term(a, List()))
    val id2 = egraph.add(Term(b, List()))
    val id3 = egraph.add(Term(c, List()))
    val id4 = egraph.add(Term(d, List()))
    val id5 = egraph.add(Term(e, List()))
    val id6 = egraph.add(Term(f, List()))
    val id7 = egraph.add(Term(g, List()))
    val id8 = egraph.add(Term(h, List()))
    val id9 = egraph.add(Term(i, List()))
    val id10 = egraph.add(Term(j, List()))
    val id11 = egraph.add(Term(k, List()))
    val id12 = egraph.add(Term(l, List()))
    val id13 = egraph.add(Term(m, List()))
    val id14 = egraph.add(Term(n, List()))
    val id15 = egraph.add(Term(o, List()))
    egraph.merge(id1, id3)
    egraph.merge(id5, id6)
    egraph.merge(id9, id11)
    egraph.merge(id13, id14)
    egraph.merge(id1, id2)
    egraph.merge(id15, id13)
    egraph.merge(id9, id13)
    egraph.merge(id7, id8)
    egraph.merge(id12, id11)
    egraph.merge(id2, id3)
    egraph.merge(id6, id5)
    egraph.merge(id15, id9)
    egraph.merge(id7, id5)
    egraph.merge(id9, id10)
    assert(egraph.idEq(id2, id3))
    assert(egraph.idEq(id6, id8))
    assert(egraph.idEq(id9, id15))
    
  }

  test("4 elements withcongruence egraph test") {
    val egraph = new EGraphTerms()
    val id1 = egraph.add(Term(a, List()))
    val id2 = egraph.add(Term(b, List()))
    val id3 = egraph.add(Term(F, List(id1)))
    val id4 = egraph.add(Term(F, List(id2)))
    egraph.merge(id1, id2)
    assert(egraph.idEq(id1, id2))
    assert(egraph.idEq(id3, id4))
    assert(!egraph.idEq(id1, id3))
    assert(!egraph.idEq(id1, id4))
    assert(!egraph.idEq(id2, id3))
    assert(!egraph.idEq(id2, id4))
    
  }

  
  test("divide-mult-shift by 2 egraph test") {

    val egraph = new EGraphTerms()
    val one = egraph.add(Term("one", List()))
    val two = egraph.add(Term("two", List()))
    val a = egraph.add(Term("a", List()))
    val ax2 = egraph.add(Term("*", List(a, two)))
    val ax2_d2 = egraph.add(Term("/", List(ax2, two)))
    val `2d2` = egraph.add(Term("/", List(two, two)))
    val ax_2d2 = egraph.add(Term("*", List(a, `2d2`)))
    val ax1 = egraph.add(Term("*", List(a, one)))

    val as1 = egraph.add(Term("<<", List(a, one)))

    egraph.merge(ax2, as1)
    egraph.merge(ax2_d2, ax_2d2)
    egraph.merge(`2d2`, one)
    egraph.merge(ax1, a)
    

    assert(egraph.idEq(one, `2d2`))
    assert(egraph.idEq(ax2, as1))
    assert(egraph.idEq(ax2_d2, ax_2d2))
    assert(egraph.idEq(`ax_2d2`, ax1))
    assert(egraph.idEq(`ax_2d2`, a))

    assert(!egraph.idEq(ax2, ax2_d2))
    assert(!egraph.idEq(ax2, `2d2`))
    assert(!egraph.idEq(ax2, ax_2d2))
    assert(!egraph.idEq(ax2, ax1))
    assert(!egraph.idEq(ax2, a))
    assert(!egraph.idEq(ax2_d2, `2d2`))
    
  }

  test("long chain congruence eGraph") {
    val egraph = new EGraphTerms()
    val x = egraph.add(Term("x", List()))
    val fx = egraph.add(Term(F, List(x)))
    val ffx = egraph.add(Term(F, List(fx)))
    val fffx = egraph.add(Term(F, List(ffx)))
    val ffffx = egraph.add(Term(F, List(fffx)))
    val fffffx = egraph.add(Term(F, List(ffffx)))
    val ffffffx = egraph.add(Term(F, List(fffffx)))
    val fffffffx = egraph.add(Term(F, List(ffffffx)))
    val ffffffffx = egraph.add(Term(F, List(fffffffx)))

  
    egraph.merge(ffffffffx, x)
    egraph.merge(fffffx, x)
    assert(egraph.idEq(fffx, x))
    assert(egraph.idEq(ffx, x))
    assert(egraph.idEq(fx, x))
    assert(egraph.idEq(x, fx))
    
  }

}

class EGraphTermTestWithExplain extends AnyFunSuite {
  import Constants.*


  // Tests with Proofs

  test("4 elements with congruence egraph test with explain") {
    val egraph = new EGraphTerms()
    val id1 = egraph.add(Term(a, List()))
    val id2 = egraph.add(Term(b, List()))
    val id3 = egraph.add(Term(F, List(id1)))
    val id4 = egraph.add(Term(F, List(id2)))
    egraph.merge(id1, id2)
    assert(egraph.explain(id3, id4) == Some(List(egraph.Congruence((id3, id4)))) )
  }

  test("divide-mult-shift by 2 egraph test with explain") {

    val egraph = new EGraphTerms()
    val one = egraph.add(Term("one", List()))
    val two = egraph.add(Term("two", List()))
    val a = egraph.add(Term("a", List()))
    val ax2 = egraph.add(Term("*", List(a, two)))
    val ax2_d2 = egraph.add(Term("/", List(ax2, two)))
    val `2d2` = egraph.add(Term("/", List(two, two)))
    val ax_2d2 = egraph.add(Term("*", List(a, `2d2`)))
    val ax1 = egraph.add(Term("*", List(a, one)))

    val as1 = egraph.add(Term("<<", List(a, one)))
    val * : TermSymbol = FunctionSymbol("*", 2)

    egraph.merge(ax2, as1)
    egraph.merge(ax2_d2, ax_2d2)
    egraph.merge(`2d2`, one)
    egraph.merge(ax1, a)

    assert(egraph.explain(one, `2d2`) == Some(List(egraph.External((one, `2d2`)))) )
    assert(egraph.explain(ax2, as1) == Some(List(egraph.External((ax2, as1)))) )
    assert(egraph.explain(ax2_d2, ax_2d2) == Some(List(egraph.External((ax2_d2, ax_2d2)))) )

    assert(egraph.explain(ax_2d2, ax1) == Some(List(egraph.Congruence((ax_2d2, ax1)))) )
    assert(egraph.explain(ax_2d2, a) == Some(List(egraph.Congruence((ax_2d2, ax1)), egraph.External((ax1, a))) ))

    
  }

  test("long chain congruence eGraph with explain") {
    val egraph = new EGraphTerms()
    val x = egraph.add(Term("x", List()))
    val fx = egraph.add(Term(F, List(x)))
    val ffx = egraph.add(Term(F, List(fx)))
    val fffx = egraph.add(Term(F, List(ffx)))
    val ffffx = egraph.add(Term(F, List(fffx)))
    val fffffx = egraph.add(Term(F, List(ffffx)))
    val ffffffx = egraph.add(Term(F, List(fffffx)))
    val fffffffx = egraph.add(Term(F, List(ffffffx)))
    val ffffffffx = egraph.add(Term(F, List(fffffffx)))

  
    egraph.merge(ffffffffx, x)
    egraph.merge(fffffx, x)
    assert(egraph.idEq(fffx, x))
    assert(egraph.idEq(ffx, x))
    assert(egraph.idEq(fx, x))
    assert(egraph.idEq(x, fx))
    assert(egraph.explain(fx, x) == Some(List(egraph.Congruence(fx, ffffffffx), egraph.External(ffffffffx, x))))
    
  }
}

class EGraphTermTestWithProofs extends AnyFunSuite {
  import Constants.*



  test("15 elements no congruence egraph test with redundant merges") {
    val egraph = new EGraphTerms()
    val id1 = egraph.add(Term(a, List()))
    val id2 = egraph.add(Term(b, List()))
    val id3 = egraph.add(Term(c, List()))
    val id4 = egraph.add(Term(d, List()))
    val id5 = egraph.add(Term(e, List()))
    val id6 = egraph.add(Term(f, List()))
    val id7 = egraph.add(Term(g, List()))
    val id8 = egraph.add(Term(h, List()))
    val id9 = egraph.add(Term(i, List()))
    val id10 = egraph.add(Term(j, List()))
    val id11 = egraph.add(Term(k, List()))
    val id12 = egraph.add(Term(l, List()))
    val id13 = egraph.add(Term(m, List()))
    val id14 = egraph.add(Term(n, List()))
    val id15 = egraph.add(Term(o, List()))
    val base = Seq((id1, id3), (id5, id6), (id9, id11), (id13, id14), (id1, id2), (id15, id13), (id9, id13), (id7, id8), (id12, id11), (id2, id3), (id6, id5), (id15, id9), (id7, id5), (id9, id10))
    egraph.merge(id1, id3)
    egraph.merge(id5, id6)
    egraph.merge(id9, id11)
    egraph.merge(id13, id14)
    egraph.merge(id1, id2)
    egraph.merge(id15, id13)
    egraph.merge(id9, id13)
    egraph.merge(id7, id8)
    egraph.merge(id12, id11)
    egraph.merge(id2, id3)
    egraph.merge(id6, id5)
    egraph.merge(id15, id9)
    egraph.merge(id7, id5)
    egraph.merge(id9, id10)
    assert(egraph.idEq(id2, id3))
    assert(egraph.idEq(id6, id8))
    assert(egraph.idEq(id9, id15))


  }


  test("4 elements with congruence egraph test with explain") {
    val egraph = new EGraphTerms()
    val id1 = egraph.add(Term(a, List()))
    val id2 = egraph.add(Term(b, List()))
    val id3 = egraph.add(Term(F, List(id1)))
    val id4 = egraph.add(Term(F, List(id2)))
    egraph.merge(id1, id2)
    assert(egraph.explain(id3, id4) == Some(List(egraph.Congruence((id3, id4)))) )

  }


  test("divide-mult-shift by 2 egraph test with explain") {

    val egraph = new EGraphTerms()
    val one = egraph.add(Term("one", List()))
    val two = egraph.add(Term("two", List()))
    val a = egraph.add(Term("a", List()))
    val ax2 = egraph.add(Term("*", List(a, two)))
    val ax2_d2 = egraph.add(Term("/", List(ax2, two)))
    val `2d2` = egraph.add(Term("/", List(two, two)))
    val ax_2d2 = egraph.add(Term("*", List(a, `2d2`)))
    val ax1 = egraph.add(Term("*", List(a, one)))

    val as1 = egraph.add(Term("<<", List(a, one)))
    val * : TermSymbol = FunctionSymbol("*", 2)

    egraph.merge(ax2, as1)
    egraph.merge(ax2_d2, ax_2d2)
    egraph.merge(`2d2`, one)
    egraph.merge(ax1, a)
    val ctx = List(ax2 === as1, ax2_d2 === ax_2d2, `2d2` === one, ax1 === a)

    assert(egraph.explain(one, `2d2`) == Some(List(egraph.External((one, `2d2`)))) )
    assert(egraph.explain(ax2, as1) == Some(List(egraph.External((ax2, as1)))) )
    assert(egraph.explain(ax2_d2, ax_2d2) == Some(List(egraph.External((ax2_d2, ax_2d2)))) )

    assert(egraph.explain(ax_2d2, ax1) == Some(List(egraph.Congruence((ax_2d2, ax1)))) )
    assert(egraph.explain(ax_2d2, a) == Some(List(egraph.Congruence((ax_2d2, ax1)), egraph.External((ax1, a))) ))

    
  }


  test("long chain congruence eGraph with explain") {
    val egraph = new EGraphTerms()
    val x = egraph.add(Term("x", List()))
    val fx = egraph.add(Term(F, List(x)))
    val ffx = egraph.add(Term(F, List(fx)))
    val fffx = egraph.add(Term(F, List(ffx)))
    val ffffx = egraph.add(Term(F, List(fffx)))
    val fffffx = egraph.add(Term(F, List(ffffx)))
    val ffffffx = egraph.add(Term(F, List(fffffx)))
    val fffffffx = egraph.add(Term(F, List(ffffffx)))
    val ffffffffx = egraph.add(Term(F, List(fffffffx)))

  
    egraph.merge(ffffffffx, x)
    egraph.merge(fffffx, x)
    assert(egraph.idEq(fffx, x))
    assert(egraph.idEq(ffx, x))
    assert(egraph.idEq(fx, x))
    assert(egraph.idEq(x, fx))
    
    val ctx = List(ffffffffx === x, fffffx === x)
    def shorten(s:String): String = 
      s.replace("F(F(F(F(F(F(F(F(x))))))))", "F8x")
      .replace("F(F(F(F(F(F(F(x)))))))", "F7x")
      .replace("F(F(F(F(F(F(x))))))", "F6x")
      .replace("F(F(F(F(F(x)))))", "F5x")
      .replace("F(F(F(F(x)))", "F4x")
      .replace("F(F(F(x)))", "F3x")
      .replace("F(F(x))", "F2x")
      .replace("F(x)", "Fx")


  }

  
}
