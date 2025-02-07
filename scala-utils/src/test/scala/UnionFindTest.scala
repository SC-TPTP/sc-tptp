import datastructure.mutable.*
import org.scalatest.compatible.Assertion
import org.scalatest.funsuite.AnyFunSuite

class UnionFindTest extends AnyFunSuite {
  def testAll[T](ops: UnionFind[T] => Unit): Unit = {
    val suf = new StrictUnionFind[T]
    ops(suf)
    val uf = new UnionFindWithExplain[T]
    ops(uf)
  }


  test("3 elements UnionFind test") {
    testAll[Int](uf => {
      uf.addAll(1 to 3)
      uf.union(1, 2)
      assert(uf.find(1) == uf.find(2))
      assert(uf.find(1) != uf.find(3))
      assert(uf.getClasses.size == 2)
    })
  }

  test("8 elements UnionFind test") {
    testAll[Int](uf => {
      uf.addAll(1 to 8)
      uf.union(1, 2)
      uf.union(3, 4)
      uf.union(5, 6)
      uf.union(7, 8)
      uf.union(1, 3)
      uf.union(6, 8)
      uf.union(1, 6)
      assert(uf.find(1) == uf.find(8))

      assert(uf.getClasses.size == 1)
    })
  }

  test("15 elements UnionFind test") {
    testAll[Int](uf => {
      uf.addAll(1 to 15)
      uf.union(1, 3)
      uf.union(5, 6)
      uf.union(9, 11)
      uf.union(13, 14)
      uf.union(1, 2)
      uf.union(15, 13)
      uf.union(9, 13)
      uf.union(7, 8)
      uf.union(12, 11)
      uf.union(3, 4)
      uf.union(1, 5)
      uf.union(1, 9)
      uf.union(7, 5)
      uf.union(9, 10)
      assert(uf.find(1) == uf.find(15))
      assert(uf.getClasses.size == 1)
    })
  }

  test("15 elements UnionFind test with redundant unions") {
    testAll[Int](uf => {
      uf.addAll(1 to 15)
      uf.union(1, 3)
      uf.union(5, 6)
      uf.union(9, 11)
      uf.union(13, 14)
      uf.union(1, 2)
      uf.union(15, 13)
      uf.union(9, 13)
      uf.union(7, 8)
      uf.union(12, 11)
      uf.union(2, 3)
      uf.union(6, 5)
      uf.union(15, 9)
      uf.union(7, 5)
      uf.union(9, 10)
      assert(uf.find(2) == uf.find(3))
      assert(uf.find(6) == uf.find(8))
      assert(uf.find(9) == uf.find(15))
      (1 to 15).foreach(i => assert(uf.find(i) != 4 || i == 4))
      assert(uf.getClasses.size == 4)
    })
  }


  test("3 elements UnionFind with explain") {
    val uf = new UnionFindWithExplain[Int]
    uf.addAll(1 to 3)
    uf.union(1, 2)
    uf.union(1, 3)
    assert(uf.explain(1, 2) == Some(Seq((1, 2))))
    assert(uf.explain(1, 3) == Some(Seq((1, 3))))
    assert(uf.explain(2, 3) == Some(Seq((1, 2), (1, 3))))
  }

  test("14 elements UnionFind with explain") {
    val uf = new UnionFindWithExplain[Int]
    uf.addAll(1 to 14)
    uf.union(1, 8)
    uf.union(7, 2)
    uf.union(3, 13)
    uf.union(7, 1)
    uf.union(6, 7)
    uf.union(9, 5)
    uf.union(9, 3)
    uf.union(14, 11)
    uf.union(10, 4)
    uf.union(12, 9)
    uf.union(4, 11)
    uf.union(10, 7)
    assert(uf.explain(1, 4) == Some(Seq( (7, 1), (10, 7), (10, 4) )))
    assert(uf.explain(5, 3) == Some(Seq( (9, 5), (9, 3) )))
    assert(uf.explain(6, 13) == None)
    assert(uf.explain(14, 2) == Some(Seq( (14, 11), (4, 11), (10, 4), (10, 7), (7, 2))))
    assert(uf.explain(2, 14) == Some(Seq((7, 2), (10, 7), (10, 4), (4, 11), (14, 11))))
  }
}