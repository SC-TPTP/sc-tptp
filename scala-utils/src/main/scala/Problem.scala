package sctptp
import SequentCalculus.*
import FOL.*
import LVL2.*


case class Subproof(name: String, proof: SCProof[?], assumptions: Seq[Formula], axioms: Map[String, String]) extends StrictLVL2ProofStep {
  val bot = proof.steps.last.bot ++<< assumptions
  val premises = axioms.values.toSeq

  def addAssumptions(fs: Seq[Formula]) = this.copy(assumptions = assumptions ++ fs)

  def mapBot(f: Sequent => Sequent) = throw new Exception("can't change the bot of a subproof")
  def rename(newName: String) = copy(name = newName)
  def renamePremises(map: Map[String, String]): SCProofStep = 
    val newax = axioms.view.mapValues(c => map.getOrElse(c, c)).toMap
    copy(axioms = newax)
  override def toString: String = {
    val axiomsString = axioms.map { case (k, v) => s"$k: $v" }.mkString(", ")
    val assumprionsString = assumptions.mkString(", ")
    s"fof($name, plain, $bot, inference(subproof, [$assumptions], {\n $proof\n}, [$axiomsString]))."
  }

  def checkCorrectness(premises: String => Sequent) = {
    checkProof(proof)
  }
}

case class Problem(axioms: Seq[Axiom], conjecture: Conjecture) {
  override def toString: String = {
    val axiomsString = axioms.map(_.toString).mkString("\n")
    s"$axiomsString\n${conjecture}"
  }

  def toStringNoSeq = {
    val axiomsString = axioms.map {
      case Axiom(name, Sequent(Seq(), Seq(f))) => 
        s"fof($name, axiom, $f)."
    }.mkString("\n")
    val conjectureString = conjecture match
      case Conjecture(name, Sequent(Seq(), Seq(f))) => 
        s"fof($name, conjecture, $f)."
      case Conjecture(name, Sequent(Seq(), Seq())) => 
        s"fof($name, conjecture, " + "$false)."
    s"$axiomsString\n$conjectureString"
  }
  
}


def flattenProof(proof: LVL2Proof): LVL2Proof = {
  var i = 0
  def innerFlattenProof(steps: Seq[SCProofStep], in: Boolean, assums: Seq[Formula], renamed: Map[String, String]): Seq[SCProofStep] = {
    steps.flatMap {
      case Subproof(name, proof, assumptions, axioms) =>
        var phisteps : List[LetFormula] = Nil
        val phis = assumptions.map(phi => {
          i += 1
          phisteps = LetFormula("r"+i, phi) :: phisteps
          AtomicFormula(AtomicSymbol(Identifier("$r"+i, 0), 0), Seq())
        })
        val newsteps = innerFlattenProof(proof.steps, true, phis ++ assums, renamed ++ axioms)
        val last = newsteps.last
        val newlast = last.rename(name)
        newsteps.dropRight(1).prependedAll(phisteps.toIndexedSeq) :+ newlast

      case ax: Axiom  =>
        if !in then
          Seq(ax)
        else Seq()
      case s => 
        Seq(s.addAssumptions(assums).renamePremises(renamed))
    }
  }
  val newsteps = innerFlattenProof(proof.steps, false, Seq(), Map())
  LVL2Proof(newsteps.toIndexedSeq.asInstanceOf, proof.thmName)
}

