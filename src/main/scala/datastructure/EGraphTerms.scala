package datastructure.mutable
import sctptp.FOL.{*, given}
import sctptp.SequentCalculus.*
import sctptp.LVL2

import scala.collection.mutable

class EGraphTerms() {

  // type EClassId = Int
  type ENode = Term



  val map = mutable.Map[ENode, Set[ENode]]()
  val parents = mutable.Map[ENode, mutable.Set[ENode]]()
  var worklist = List[ENode]()

  val uf : UnionFindWithExplain[ENode] = new UnionFindWithExplain[ENode]()
  export uf.find

  trait Step
  case class External(between: (ENode, ENode)) extends Step
  case class Congruence(between: (ENode, ENode)) extends Step

  val proofMap = mutable.Map[(ENode, ENode), Step]()

  def explain(id1: ENode, id2: ENode): Option[List[Step]] = {
    val steps = uf.explain(id1, id2)
    steps.map(_.foldLeft((id1, List[Step]())) {
      case ((prev, acc), step) =>
      proofMap(step) match
        case s @ External((l, r)) => 
          if l == prev then
            (r, s :: acc)
          else if r == prev then
            (l, External(r, l) :: acc)
          else throw new Exception("Invalid proof recovered: It is not a chain")
        case s @ Congruence((l, r)) => 
          if l == prev then
            (r, s :: acc)
          else if r == prev then
            (l, Congruence(r, l) :: acc)
          else throw new Exception("Invalid proof recovered: It is not a chain")

    }._2.reverse)
  }



  def makeSingletonEClass(node:ENode): ENode = {
    uf.add(node)
    map(node) = Set(node)
    parents(node) = mutable.Set()
    node
  }

  def classOf(id: ENode): Set[ENode] = map(id)

  def idEq(id1: ENode, id2: ENode): Boolean = find(id1) == find(id2)

  def isCanonical(node: ENode): Boolean = node == canonicalize(node)

  def canonicalize(node: ENode): ENode = {
    node match
      case Term(label, children) => Term(label, children.map(find.asInstanceOf))
  }

  def lookup(node: ENode): ENode = canonicalize(node)


  def add(node: Term): Term =
    if map.contains(node) then return node
    makeSingletonEClass(node)
    node.args.foreach(child => 
      parents(child).add(node)
      add(child)
    )
    node


  def merge(id1: ENode, id2: ENode): Unit = {
    mergeWithStep(id1, id2, External((id1, id2)))
  }

  protected def mergeWithStep(id1: ENode, id2: ENode, step: Step): Unit = {
    if find(id1) == find(id2) then ()
    else
      proofMap((id1, id2)) = step
      val newSet = map(find(id1)) ++ map(find(id2))
      val newparents = parents(find(id1)) ++ parents(find(id2))
      uf.union(id1, id2)
      val newId1 = find(id1)
      val newId2 = find(id2)
      map(newId1) = newSet
      map(newId2) = newSet
      parents(newId1) = newparents
      parents(newId2) = newparents
      worklist = id2 :: worklist
      val cause = (id1, id2)
      val id = find(id2)
      val seen = mutable.Map[ENode, ENode]()
      newparents.map { pNode =>
        val canonicalPNode = canonicalize(pNode) //Canonicalize doesn't work: Need to do 1-step proofs
        if seen.contains(canonicalPNode) then 
          val qNode = seen(canonicalPNode)

          Some((pNode, qNode, cause))
        else
          seen(canonicalPNode) = pNode
          None
      } .foreach{ 
        case Some(pNode, qNode, cause) =>
          mergeWithStep(pNode, qNode, Congruence((pNode, qNode)))
        case None => ()
      }
      parents(id) = seen.values.to(mutable.Set)
  }


  def addAllTerms(f:Formula): Unit = {
    f match
      case AtomicFormula(_, args) => args.foreach(add)
      case ConnectorFormula(_, args) => args.foreach(addAllTerms)
      case BinderFormula(_, _, inner) => ()
  }

  import sctptp.SequentCalculus as SC

  def prove(id1: ENode, id2:ENode, base: SC.Sequent, stepString: String, thmName: String = "Congruence Proof"): Option[LVL2.LVL2Proof] =
    val steps = proveInner(id1, id2, 0, base.left, base.right, stepString)
    steps.map(steps => LVL2.LVL2Proof(steps.reverse.toIndexedSeq, thmName)) 

  def proveInner(id1: ENode, id2: ENode, step: Int, ctx1: Seq[Formula], ctx2: Seq[Formula], stepString: String): Option[List[LVL2.LVL2ProofStep]] = {
    import sctptp.SequentCalculus as SC
    val steps = explain(id1, id2)
    steps match
      case None => None
      case Some(value) =>

        var no = step
        if value.isEmpty then return Some(List[LVL2.LVL2ProofStep](SC.RightRefl(s"$stepString$no", SC.Sequent(ctx1, (id1 === id1) +: ctx2), 0) ))
        

        var proof = List[LVL2.LVL2ProofStep]()
        var first = true

        value.foreach {
          case External((left, right)) => 
            if first then
              val leftIndex = ctx1.indexWhere(f => f == (left === right) || f == (right === left))
              val newstep = SC.Hyp(s"$stepString$no", SC.Sequent(ctx1, (id1 === right) +: ctx2), leftIndex, 0)
              proof = newstep :: proof
              first = false
              no += 1

            // sno is a step proving id1 === left
            // needs to output a proof of id1 === right
            else
              val x = VariableSymbol(freshId(id1.freeVariables.map(_.name), "x"))
              val leftIndex = ctx1.indexWhere(f => f == (left === right) || f == (right === left))
              val newstep = SC.RightSubst(s"$stepString$no", SC.Sequent(ctx1, (id1 === right) +: ctx2), leftIndex, 0, id1 === x(), x, s"$stepString${no-1}")
              proof = newstep :: proof
              no += 1

          case Congruence((left, right)) => 

            // sno is a step proving id1 === left
            // need to output a proof of id1 === right
            // ** 1. output a proof of left === right |- id1 === right (transitivity)
            // ** 2. output a proof of |- left === right (congruence)
            // ** ** a) gather proofs of equality between children of left and right
            // ** ** b) use reflexivity to obtain a proof of left === left
            // ** ** c) apply substitution to obtain a proof of (a1===b1, ..., an===bn) |- left === right
            // ** ** d) apply cut n times to obtain a proof of id1 === right
            // ** 3. apply cut to obtain a proof of id1 === right


            val name1 = s"$stepString$no"
            if !first then {
              val x = VariableSymbol(freshId(id1.freeVariables.map(_.name), "X"))
              val leftIndex = ctx1.find(_ == (left === right))
              val cond_id1_eq_r = SC.RightSubst(name1, SC.Sequent((left === right) +: ctx1, (id1 === right) +: ctx2), 0, 0, id1 === x(), x, s"$stepString${no-1}") // ** 1.
              proof = cond_id1_eq_r :: proof
              no += 1
              assert(left.label == right.label, s"Congruence: left and right should have the same label but obtained\n left: $left\n right: $right")
              assert(left.args.size == right.args.size, s"Congruence: left and right should have the same number of children but obtained\n left: $left\n right: $right")
            }

            val eqs = left.args.zip(right.args).toList
            val childrenProofs = eqs.map { (l, r) => 
              val chpr = proveInner(l, r, no, ctx1, ctx2, stepString).get
              no += chpr.size
              proof = chpr ++ proof
              (chpr, no-1, l, r)
            } // ** ** a)

            val name2 = s"$stepString$no" // ** ** b)
            val step_refl = SC.RightRefl(name2, SC.Sequent(ctx1, (left === left) +: ctx2), 0) 
            proof = step_refl :: proof
            no += 1
            
            
            val name3 = s"$stepString$no" // ** ** c)
            var freeCount: Int = eqs.flatMap(pair => (pair._1.freeVariables ++ pair._2.freeVariables).map(_.name)).foldLeft(0)((acc, x) => if x.name.no > acc then x.name.no else acc)
            val freeVars = eqs.map(i => 
              freeCount += 1
              VariableSymbol(Identifier("XX", freeCount))
            )
            val P = left === right.label(freeVars.map(_())*)
            val left_eq_right = sctptp.LVL2.RightSubstMulti(
              name3, 
              SC.Sequent(eqs.map(_ === _) ++ ctx1, (left === right) +: ctx2),
              (0 until eqs.size).map((_, true)).toList, 
              0, 
              P, 
              freeVars.toList, 
              name2
            ) // ** ** c)
            proof = left_eq_right :: proof
            no += 1
            val cuts = childrenProofs.foldLeft(left_eq_right.bot) {
              case (acc, (_, n, l, r)) => 
                val namen = s"$stepString$no"
                val newbot = SC.Sequent(acc.left.tail, acc.right)
                val cut = SC.Cut(
                  namen,
                  newbot,
                  0,
                  0,
                  s"$stepString$n",
                  s"$stepString${no-1}"
                )
                no += 1
                proof = cut :: proof
                newbot
            } // ** ** d)

            if !first then {
              val name4 = s"$stepString${no-1}"
              val name5 = s"$stepString$no"

              val lastStep = SC.Cut(
                name5,
                SC.Sequent(ctx1, (id1 === right) +: ctx2),
                0,
                0,
                name4,
                name1
              ) // ** 3.
              proof = lastStep :: proof
            }
        }

        Some(proof)
    

  }


}

