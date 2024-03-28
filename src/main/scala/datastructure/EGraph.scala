package datastructure.mutable

import scala.collection.*

class EGraph[Label] {

  type EClassId = Int
  var maxId = 0
  def nextId(): EClassId = {
    maxId += 1
    maxId
  }

  val uf = UnionFindWithExplain[EClassId]()
  import uf.{union}
  export uf.find

  type ENode = (Label, List[EClassId])

  val map = mutable.Map[EClassId, Set[ENode]]()
  val parents = mutable.Map[EClassId, mutable.Map[ENode, EClassId]]()
  val hashcons = mutable.Map[ENode, EClassId]()
  var worklist = List[EClassId]()

  def isCanonical(id: EClassId): Boolean = id == find(id)

  def isCanonical(node: ENode): Boolean = node == canonicalize(node)

  def canonicalize(node: ENode): ENode = {
    val (label, children) = node
    val canonicalChildren = children.map(find)
    (label, canonicalChildren)
  }

  def idEq(id1: EClassId, id2: EClassId): Boolean = find(id1) == find(id2)

  def nodeEq(node1: ENode, node2: ENode): Boolean = ???

  private def nodeCongruence(node1: ENode, node2: ENode): Boolean = 
    node1._1 == node2._2 && node1._2.length == node2._2.length &&
      node1._2.zip(node2._2).forall((id1, id2) => idEq(id1, id2))
  

  def lookup(node: ENode): Option[EClassId] = hashcons.get(canonicalize(node))

  def add(node: ENode): EClassId = lookup(node) match {
    case Some(id) => id
    case None =>
      val id = nextId()
      map(id) = Set(node)
      node._2.foreach(child => parents(child)(node) = id)
      hashcons(node) = id
      id
  }

  def merge(id1: EClassId, id2: EClassId): Unit = {
    if find(id1) == find(id2) then ()
    else
      union(id1, id2)
      worklist = find(id1) :: worklist
      rebuild()
  }


  def rebuild(): Unit = {
    while worklist.nonEmpty do
      val todo = worklist.map(find)
      worklist = List()
      todo.foreach(repair)
  }

  def repair(id: EClassId): Unit = {
    parents(id).foreach {
      case (pNode, pClassId) =>
        hashcons.remove(pNode)
        val newPNode = canonicalize(pNode)
        hashcons(newPNode) = find(pClassId)
    }

    val newParents = mutable.Map[ENode, EClassId]()
    parents(id).foreach {
      case (pNode, pClassId) =>
        val newPNode = canonicalize(pNode)
        if newParents.contains(newPNode) then
          merge(pClassId, newParents(newPNode))
        newParents(newPNode) = find(pClassId)
      parents(id) = newParents
    }
  }

  
}
