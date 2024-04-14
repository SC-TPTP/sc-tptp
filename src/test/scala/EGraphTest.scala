
import datastructure.mutable.*
import org.scalatest.compatible.Assertion
import org.scalatest.funsuite.AnyFunSuite

class EGraphTest extends AnyFunSuite {
  def testAll[T](ops: EGraph[T] => Unit): Unit = {
    val suf = new StrictEGraph[T]
    ops(suf)
    val uf = new EGraphWithProof[T]
    ops(uf)
  }

  test("3 elements no congruence egraph test") {
    testAll {egraph =>
      val id1 = egraph.add(("a", List()))
      val id2 = egraph.add(("b", List()))
      val id3 = egraph.add(("c", List()))
      egraph.merge(id1, id2)
      assert(egraph.idEq(id1, id2))
      assert(!egraph.idEq(id1, id3))
    }
  }

  test("8 elements no congruence egraph test") {
    testAll {egraph =>
      val id1 = egraph.add(("a", List()))
      val id2 = egraph.add(("b", List()))
      val id3 = egraph.add(("c", List()))
      val id4 = egraph.add(("d", List()))
      val id5 = egraph.add(("e", List()))
      val id6 = egraph.add(("f", List()))
      val id7 = egraph.add(("g", List()))
      val id8 = egraph.add(("h", List()))
      egraph.merge(id1, id2)
      egraph.merge(id3, id4)
      egraph.merge(id5, id6)
      egraph.merge(id7, id8)
      egraph.merge(id1, id3)
      egraph.merge(id6, id8)
      egraph.merge(id1, id6)
      assert(egraph.idEq(id1, id8))
    }
  }

  test("15 elements no congruence egraph test") {
    testAll {egraph =>
      val egraph = new StrictEGraph[String]()
      val id1 = egraph.add(("a", List()))
      val id2 = egraph.add(("b", List()))
      val id3 = egraph.add(("c", List()))
      val id4 = egraph.add(("d", List()))
      val id5 = egraph.add(("e", List()))
      val id6 = egraph.add(("f", List()))
      val id7 = egraph.add(("g", List()))
      val id8 = egraph.add(("h", List()))
      val id9 = egraph.add(("i", List()))
      val id10 = egraph.add(("j", List()))
      val id11 = egraph.add(("k", List()))
      val id12 = egraph.add(("l", List()))
      val id13 = egraph.add(("m", List()))
      val id14 = egraph.add(("n", List()))
      val id15 = egraph.add(("o", List()))
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
  }

  test("15 elements no congruence egraph test with redundant merges") {
    testAll {egraph =>
      val id1 = egraph.add(("a", List()))
      val id2 = egraph.add(("b", List()))
      val id3 = egraph.add(("c", List()))
      val id4 = egraph.add(("d", List()))
      val id5 = egraph.add(("e", List()))
      val id6 = egraph.add(("f", List()))
      val id7 = egraph.add(("g", List()))
      val id8 = egraph.add(("h", List()))
      val id9 = egraph.add(("i", List()))
      val id10 = egraph.add(("j", List()))
      val id11 = egraph.add(("k", List()))
      val id12 = egraph.add(("l", List()))
      val id13 = egraph.add(("m", List()))
      val id14 = egraph.add(("n", List()))
      val id15 = egraph.add(("o", List()))
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
  }

  test("4 elements withcongruence egraph test") {
    testAll {egraph =>
      val id1 = egraph.add(("a", List()))
      val id2 = egraph.add(("b", List()))
      val id3 = egraph.add(("f", List(id1)))
      val id4 = egraph.add(("f", List(id2)))
      egraph.merge(id1, id2)
      assert(egraph.idEq(id1, id2))
      assert(egraph.idEq(id3, id4))
      assert(!egraph.idEq(id1, id3))
      assert(!egraph.idEq(id1, id4))
      assert(!egraph.idEq(id2, id3))
      assert(!egraph.idEq(id2, id4))
    }
  }

  test("divide-mult-shift by 2 egraph test") {
    testAll {egraph =>
      val one = egraph.add(("one", List()))
      val two = egraph.add(("two", List()))
      val a = egraph.add(("a", List(one)))
      val ax2 = egraph.add(("*", List(a, two)))
      val ax2_d2 = egraph.add(("/", List(ax2, two)))
      val `2d2` = egraph.add(("/", List(two, two)))
      val ax_2d2 = egraph.add(("*", List(a, `2d2`)))
      val ax1 = egraph.add(("*", List(a, one)))

      val as1 = egraph.add(("<<", List(a, one)))

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
  }

  test("long chain congruence eGraph") {
    testAll {egraph =>
      val x = egraph.add(("x", List()))
      val fx = egraph.add(("f", List(x)))
      val ffx = egraph.add(("f", List(fx)))
      val fffx = egraph.add(("f", List(ffx)))
      val ffffx = egraph.add(("f", List(fffx)))
      val fffffx = egraph.add(("f", List(fffx)))
      val ffffffx = egraph.add(("f", List(fffffx)))
      val fffffffx = egraph.add(("f", List(ffffffx)))
      val ffffffffx = egraph.add(("f", List(fffffffx)))

      
      egraph.merge(ffffffffx, x)
      egraph.merge(fffffx, x)
      assert(egraph.idEq(fffx, x))
      assert(egraph.idEq(ffx, x))
      assert(egraph.idEq(fx, x))
      assert(egraph.idEq(x, fx))
    }
  }


  // Tests with Proofs
  println("\n\n With Proofs \n\n")

  test("4 elements with congruence egraph test with proofs") {
    val egraph = new EGraphWithProof[String]()
    val id1 = egraph.add(("a", List()))
    val id2 = egraph.add(("b", List()))
    val id3 = egraph.add(("f", List(id1)))
    val id4 = egraph.add(("f", List(id2)))
    egraph.merge(id1, id2)
    println(egraph.proofMap.toList)
    println(egraph.explain(id3, id4))
  }


  
}
