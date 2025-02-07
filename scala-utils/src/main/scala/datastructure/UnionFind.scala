package datastructure.mutable

trait UnionFind[T] {

  def add(x: T): Unit

  def find(x: T): T

  def union(x: T, y: T): Unit

  def addAll(xs: Iterable[T]): Unit = xs.foreach(add)

  def getClasses: Set[T]
  
}


class StrictUnionFind[T] extends UnionFind[T] {
  private val parent = scala.collection.mutable.Map[T, T]()
  private val rank = scala.collection.mutable.Map[T, Int]()

  def add(x: T): Unit = {
    parent(x) = x
    rank(x) = 0
  }

  /**
    * find with path compression
    *
    * @param x the element whose parent we want to find
    * @return the root of x
    */
  def find(x: T): T = {
    if parent(x) == x then
      x
    else
      var root = x
      while parent(root) != root do
        root = parent(root)
      var y = x
      while parent(y) != root do
        parent(y) = root
        y = parent(y)
      root
  }

  /**
    * Merges the classes of x and y
    */
  def union(x: T, y: T): Unit = {
    val xRoot = find(x)
    val yRoot = find(y)
    if (xRoot == yRoot) return
    if (rank(xRoot) < rank(yRoot)) {
      parent(xRoot) = yRoot
    } else if (rank(xRoot) > rank(yRoot)) {
      parent(yRoot) = xRoot
    } else {
      parent(yRoot) = xRoot
      rank(xRoot) = rank(xRoot) + 1
    }
  }

  def getClasses: Set[T] = parent.keys.map(find).toSet
}


class UnionFindWithExplain[T] extends UnionFind[T] {
  val parent = scala.collection.mutable.Map[T, T]()
  val realParent = scala.collection.mutable.Map[T, (T, ((T, T), Boolean, Int))]()
  val rank = scala.collection.mutable.Map[T, Int]()
  var unionCounter = 0

  def add(x: T): Unit = {
    parent(x) = x
    realParent(x) = (x, ((x, x), true, 0))
    rank(x) = 0
  }

  /**
    * find without path compression
    *
    * @param x the element whose parent we want to find
    * @return the root of x
    */
  def find(x: T): T = {
    if parent(x) == x then
      x
    else
      var root = x
      while parent(root) != root do
        root = parent(root)
      var y = x
      while parent(y) != root do
        parent(y) = root
        y = parent(y)
      root
  }

  /**
    * Merges the classes of x and y
    */
  def union(x: T, y: T): Unit = {
    unionCounter += 1
    val xRoot = find(x)
    val yRoot = find(y)
    if (xRoot == yRoot) return
    if (rank(xRoot) < rank(yRoot)) {
      parent(xRoot) = yRoot
      realParent(xRoot) = (yRoot, ((x, y), true, unionCounter))
    } else if (rank(xRoot) > rank(yRoot)) {
      parent(yRoot) = xRoot
      realParent(yRoot) = (xRoot, ((x, y), false, unionCounter))
    } else {
      parent(yRoot) = xRoot
      realParent(yRoot) = (xRoot, ((x, y), false, unionCounter))
      rank(xRoot) = rank(xRoot) + 1
    }
  }

  private def getPathToRoot(x: T): List[T] = {
    if x == find(x) then
      List(x)
    else
      val next = realParent(x)
      x :: getPathToRoot(next._1)
    
  }

  private def getExplanationFromTo(x:T, c: T): List[(T, ((T, T), Boolean, Int))] = {
    if x == c then
      List()
    else
      val next = realParent(x)
      next :: getExplanationFromTo(next._1, c)}

  private def lowestCommonAncestor(x: T, y: T): Option[T] = {
    val pathX = getPathToRoot(x)
    val pathY = getPathToRoot(y)
    pathX.find(pathY.contains)
  }

  /**
    * Returns a path from x to y made of pairs of elements (u, v)
    * such that union(u, v) was called.
    */
  def explain(x:T, y:T): Option[List[(T, T)]]= {

    if (x == y) then return Some(List())
    val lca = lowestCommonAncestor(x, y)
    lca match
      case None => None
      case Some(lca) =>
        var max :((T, T), Boolean, Int) = ((x, x), true, 0)
        var itX = x
        while itX != lca do
          val (next, ((u1, u2), b, c)) = realParent(itX)
          if c > max._3 then
            max = ((u1, u2), b, c)
          itX = next

        var itY = y
        while itY != lca do
          val (next, ((u1, u2), b, c)) = realParent(itY)
          if c > max._3 then
            max = ((u1, u2), !b, c)
          itY = next

        val u1 = max._1._1
        val u2 = max._1._2
        if max._2 then
          Some(explain(x, u1).get ++ List((u1, u2)) ++ explain(u2, y).get)
        else
          Some(explain(x, u2).get ++ List((u1, u2)) ++ explain(u1, y).get)
  }


  def getClasses: Set[T] = parent.keys.map(find).toSet
}