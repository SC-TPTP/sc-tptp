package datastructure.mutable

import scala.collection.*

trait EGraph[Symbol]() {

  type EClassId = Int
  type ENode = (Symbol, List[EClassId])

  var maxId = 0
  def nextId(): EClassId = {
    maxId += 1
    maxId
  }


  val uf : UnionFind[EClassId]
  export uf.find

  def makeSingletonEClass(node:ENode): EClassId 

  def classOf(id: EClassId): Set[ENode] 


  def isCanonical(id: EClassId): Boolean = id == find(id)

  def isCanonical(node: ENode): Boolean = node == canonicalize(node)

  def canonicalize(node: ENode): ENode = {
    val (label, children) = node
    val canonicalChildren = children.map(find)
    (label, canonicalChildren)
  }

  def idEq(id1: EClassId, id2: EClassId): Boolean = find(id1) == find(id2)

  def add(node: ENode): EClassId

  def merge(id1: EClassId, id2: EClassId): Unit

}






class StrictEGraph[Symbol]() extends EGraph[Symbol] {

  type EClassId = Int

  val uf = UnionFindWithExplain[EClassId]()
  import uf.{union}

  type ENode = (Symbol, List[EClassId])


  def makeSingletonEClass(node:ENode): EClassId = {
    val id = nextId()
    uf.add(id)
    map(id) = Set(node)
    parents(id) = mutable.Map()
    id
  }

  def classOf(id: EClassId): Set[ENode] = map(id)



  val map = mutable.Map[EClassId, Set[ENode]]()
  val parents = mutable.Map[EClassId, mutable.Map[ENode, EClassId]]()
  val hashcons = mutable.Map[ENode, EClassId]()
  var worklist = List[EClassId]()



  def nodeEq(node1: ENode, node2: ENode): Boolean = ???

  def lookup(node: ENode): Option[EClassId] = hashcons.get(canonicalize(node))

  def add(node: ENode): EClassId = lookup(node) match {
    case Some(id) => id
    case None =>
      val id = makeSingletonEClass(node)
      node._2.foreach(child => parents(child)(node) = id)
      hashcons(node) = id
      id
  }

  def merge(id1: EClassId, id2: EClassId): Unit = {
    if find(id1) == find(id2) then ()
    else
      val newSet = map(find(id1)) ++ map(find(id2))
      val newparents = parents(find(id1)) ++ parents(find(id2))
      union(id1, id2)
      val newId = find(id1)
      map(newId) = newSet
      parents(newId) = newparents
      worklist = id2 :: worklist
      rebuild()
  }


  protected def rebuild(): Unit = {
    while worklist.nonEmpty do
      val todo = worklist.map(find)
      worklist = List()
      todo.foreach(repair)
  }

  protected def repair(id: EClassId): Unit = {
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



class EGraphWithProof[Symbol]() extends StrictEGraph[Symbol] {
  override val uf: UnionFindWithExplain[EClassId] = UnionFindWithExplain[EClassId]()
  import uf.{union}

  trait Step
  case class External(cause: (EClassId, EClassId)) extends Step
  case class Congruence(cause: (EClassId, EClassId)) extends Step

  val proofMap = mutable.Map[(EClassId, EClassId), Step]()

  def explain(id1: EClassId, id2: EClassId): Option[List[Step]] = {
    val steps = uf.explain(id1, id2)
    steps.map(_.map(proofMap))
  }

  override def merge(id1: EClassId, id2: EClassId): Unit = {
    mergeWithStep(id1, id2, External((id1, id2)))
  }
  
  protected def mergeWithStep(id1: EClassId, id2: EClassId, step: Step): Unit = {
    if find(id1) == find(id2) then ()
    else
      proofMap((id1, id2)) = step
      val newSet = map(find(id1)) ++ map(find(id2))
      val newparents = parents(find(id1)) ++ parents(find(id2))
      union(id1, id2)
      val newId = find(id1)
      map(newId) = newSet
      parents(newId) = newparents
      worklist = id2 :: worklist
      rebuildCause((id1, id2))
  }

  protected def rebuildCause(cause: (EClassId, EClassId)): Unit = {
    while worklist.nonEmpty do
      val todo = worklist.map(find)
      worklist = List()
      todo.foreach(repairCause(_, cause))
  }

  protected def repairCause(id: EClassId, cause: (EClassId, EClassId)): Unit = {
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
          mergeWithStep(pClassId, newParents(newPNode), Congruence(cause))
        newParents(newPNode) = find(pClassId)
    }
    parents(id) = newParents
  }

  

}
