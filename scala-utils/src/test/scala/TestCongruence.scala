
import org.scalatest.compatible.Assertion
import org.scalatest.funsuite.AnyFunSuite
import sctptp.FOL.{*, given}
import sctptp.SequentCalculus.* 
import sctptp.LVL2.*
import sctptp.CongruenceClosure.*

class TestCongruence extends AnyFunSuite {
  import Constants.*


  println("\n\n With Proofs \n\n")
  val P = AtomicSymbol("P", 1)
  

  test("15 elements no congruence egraph test with redundant merges") {
    val id1 = Term(a, List())
    val id2 = Term(b, List())
    val id3 = Term(c, List())
    val id4 = Term(d, List())
    val id5 = Term(e, List())
    val id6 = Term(f, List())
    val id7 = Term(g, List())
    val id8 = Term(h, List())
    val id9 = Term(i, List())
    val id10 = Term(j, List())
    val id11 = Term(k, List())
    val id12 = Term(l, List())
    val id13 = Term(m, List())
    val id14 = Term(n, List())
    val id15 = Term(o, List())
    val base = Seq((id1, id3), (id5, id6), (id9, id11), (id13, id14), (id1, id2), (id15, id13), (id9, id13), (id7, id8), (id12, id11), (id2, id3), (id6, id5), (id15, id9), (id7, id5), (id9, id10))


    println(s"\n Proving ${P(id2)} === ${P(id3)} \n")
    val proof = Seq(Congruence("congruence", Sequent(base.map(_ === _) :+ P(List(id2)) :+ !P(List(id3)), List()) ))
    val tran = eliminateCongruence(proof)
    tran.foreach(println)




  }

  test("4 elements with congruence egraph test with explain") {
    val id1 = Term(a, List())
    val id2 = Term(b, List())
    val id3 = Term(F, List(id1))
    val id4 = Term(F, List(id2))

    println(s"\n Proving ${id3} === ${id4} \n")
    val proof = Seq(Congruence("congruence", Sequent(List(id1 === id2, P(List(id3)), !P(List(id4))), List()) ))
    val tran = eliminateCongruence(proof)
    tran.foreach(println)
    
  }
/*
  test("divide-mult-shift by 2 egraph test with explain") {

    val egraph = new EGraphTerms()
    val one = Term("one", List()))
    val two = Term("two", List()))
    val a = Term("a", List()))
    val ax2 = Term("*", List(a, two)))
    val ax2_d2 = Term("/", List(ax2, two)))
    val `2d2` = Term("/", List(two, two)))
    val ax_2d2 = Term("*", List(a, `2d2`)))
    val ax1 = Term("*", List(a, one)))

    val as1 = Term("<<", List(a, one)))
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
    val x = Term("x", List()))
    val fx = Term(F, List(x)))
    val ffx = Term(F, List(fx)))
    val fffx = Term(F, List(ffx)))
    val ffffx = Term(F, List(fffx)))
    val fffffx = Term(F, List(ffffx)))
    val ffffffx = Term(F, List(fffffx)))
    val fffffffx = Term(F, List(ffffffx)))
    val ffffffffx = Term(F, List(fffffffx)))

  
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
