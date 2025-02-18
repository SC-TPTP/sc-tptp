
import org.scalatest.compatible.Assertion
import org.scalatest.funsuite.AnyFunSuite
import sctptp.FOL.{*, given}
import sctptp.SequentCalculus.* 
import sctptp.LVL2.*
import sctptp.CongruenceClosure.*

class TestCongruence extends AnyFunSuite {
  import Constants.*


  val P = AtomicSymbol("P", 1)
  

  test("15 elements no congruence egraph test with redundant merges") {
    val id1 = FunctionTerm(a, List())
    val id2 = FunctionTerm(b, List())
    val id3 = FunctionTerm(c, List())
    val id4 = FunctionTerm(d, List())
    val id5 = FunctionTerm(e, List())
    val id6 = FunctionTerm(f, List())
    val id7 = FunctionTerm(g, List())
    val id8 = FunctionTerm(h, List())
    val id9 = FunctionTerm(i, List())
    val id10 = FunctionTerm(j, List())
    val id11 = FunctionTerm(k, List())
    val id12 = FunctionTerm(l, List())
    val id13 = FunctionTerm(m, List())
    val id14 = FunctionTerm(n, List())
    val id15 = FunctionTerm(o, List())
    val base = Seq((id1, id3), (id5, id6), (id9, id11), (id13, id14), (id1, id2), (id15, id13), (id9, id13), (id7, id8), (id12, id11), (id2, id3), (id6, id5), (id15, id9), (id7, id5), (id9, id10))


    val proof = Seq(Congruence("congruence", Sequent(base.map(_ === _) :+ P(List(id2)) :+ !P(List(id3)), List()) ))
    val tran = eliminateCongruence(proof)




  }

  test("4 elements with congruence egraph test with explain") {
    val id1 = FunctionTerm(a, List())
    val id2 = FunctionTerm(b, List())
    val id3 = FunctionTerm(F, List(id1))
    val id4 = FunctionTerm(F, List(id2))

    val proof = Seq(Congruence("congruence", Sequent(List(id1 === id2, P(List(id3)), !P(List(id4))), List()) ))
    val tran = eliminateCongruence(proof)
    
  }
/*
  test("divide-mult-shift by 2 egraph test with explain") {

    val egraph = new EGraphTerms()
    val one = FunctionTerm("one", List()))
    val two = FunctionTerm("two", List()))
    val a = FunctionTerm("a", List()))
    val ax2 = FunctionTerm("*", List(a, two)))
    val ax2_d2 = FunctionTerm("/", List(ax2, two)))
    val `2d2` = FunctionTerm("/", List(two, two)))
    val ax_2d2 = FunctionTerm("*", List(a, `2d2`)))
    val ax1 = FunctionTerm("*", List(a, one)))

    val as1 = FunctionTerm("<<", List(a, one)))
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

    println(s"\n Proving ${one} === ${`2d2`} \n")
    println(egraph.prove(one, `2d2`, SC.Sequent(ctx, List()), "s"))

    println(s"\n Proving ${ax2} === ${as1} \n")
    println(egraph.prove(ax2, as1, SC.Sequent(ctx, List()), "s"))

    println(s"\n Proving ${ax2_d2} === ${ax_2d2} \n")
    println(egraph.prove(ax2_d2, ax_2d2, SC.Sequent(ctx, List()), "s"))

    println(s"\n Proving ${ax_2d2} === ${ax1} \n")
    println(egraph.prove(ax_2d2, ax1, SC.Sequent(ctx, List()), "s"))

    println(s"\n Proving ${ax_2d2} === ${a} \n")
    println(egraph.prove(ax_2d2, a, SC.Sequent(ctx, List()), "s"))

    
  }


  test("long chain congruence eGraph with explain") {
    val egraph = new EGraphTerms()
    val x = FunctionTerm("x", List()))
    val fx = FunctionTerm(F, List(x)))
    val ffx = FunctionTerm(F, List(fx)))
    val fffx = FunctionTerm(F, List(ffx)))
    val ffffx = FunctionTerm(F, List(fffx)))
    val fffffx = FunctionTerm(F, List(ffffx)))
    val ffffffx = FunctionTerm(F, List(fffffx)))
    val fffffffx = FunctionTerm(F, List(ffffffx)))
    val ffffffffx = FunctionTerm(F, List(fffffffx)))

  
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

    println(s"\n Proving ${fffx} === ${x} \n")
    println(shorten(egraph.prove(fffx, x, SC.Sequent(ctx, List()), "s").get.toString()))

    println(s"\n Proving ${ffx} === ${x} \n")
    println(shorten(egraph.prove(ffx, x, SC.Sequent(ctx, List()), "s").get.toString()))

    println(s"\n Proving ${fx} === ${x} \n")
    println(shorten(egraph.prove(fx, x, SC.Sequent(ctx, List()), "s").get.toString()))
    
  }
  */
}
