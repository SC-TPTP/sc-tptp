package sctptp
import SequentCalculus.*
import FOL.*
import Helpers.{*, given}
import LVL2.*
import scala.io.Source
import Parser.*
import java.io.File
import java.nio.file.{Files, Paths}
import java.io.InputStream
import java.nio.file.StandardCopyOption
import sys.process._
import java.nio.charset.StandardCharsets
import scala.collection.mutable.Map as mutMap
import leo.modules.input.TPTPParser.problem

object Prover9 {

  def fullP9Prover(problem: Problem): LVL2Proof = {
    flattenProof(Tseitin2.certify_clausal(problem, Prover9.proveProblem))
  }

  def toSafe(f: Formula): Formula = f match {
    case AtomicFormula(pred, sqf) => 
      if pred.id.name(0) == '$' then f 
      else AtomicFormula(pred.copy(id = pred.id.copy(name = "p"+pred.id.name)), sqf)
    case ConnectorFormula(cn, seqf) => ConnectorFormula(cn, seqf.map(toSafe))
    case _ => throw new Exception("quantifiers not supported")
  }
  def fromSafe(f: Formula): Formula = f match {
    case AtomicFormula(pred, sqf) => 
      if pred.id.name(0) == '$' then f 
      else AtomicFormula(pred.copy(id = pred.id.copy(name = pred.id.name.tail)), sqf)
    case ConnectorFormula(cn, seqf) => ConnectorFormula(cn, seqf.map(fromSafe))
    case _ => throw new Exception("quantifiers not supported")
  }

  val foldername = "p9proof/"
  val p9Exec = getClass.getResource

  def proveProblem(problem: Problem) : LVL2Proof = {
    certify_P9_variables(problem)
  }
  

  def proveProblem_no_rename(problem: Problem): LVL2Proof = {
    val problem2 = Problem(
      problem.axioms.map(_.mapBot(bot => bot.left |- bot.right.map(toSafe))),
      problem.conjecture
    )
    val pathdir = Paths.get(foldername)
    if (!(Files.exists(pathdir) && Files.isDirectory(pathdir)))
      Files.createDirectory(pathdir)

    // Create the file if it doesn't exist
    val pathfilepname = foldername + "/p9abc.p"
    val pathfilep = Paths.get(pathfilepname)
    if (!Files.exists(pathfilep)) {
      Files.createFile(pathfilep)
    }

    val newProblemAxioms = problem2.axioms.map(_.mapBot(bot => bot.left |- (if bot.right.size == 1 then bot.right.head else Or(bot.right))))
    val newProblem = Problem(newProblemAxioms, Conjecture("cjctr", () |- bot()))
    // Write the problem to the file
    Files.write(pathfilep, newProblem.toStringNoSeq.getBytes(StandardCharsets.UTF_8))

    val proof = _proveFile(pathfilepname)
    val newsteps = proof.steps.map(step =>
      step.mapBot(bot => bot.left.map(fromSafe) |- bot.right.map(fromSafe))
    )

    def removeOr(f: Formula): Seq[Formula] = f match {
      case AtomicFormula(`bot`, Seq()) => Seq()
      case ConnectorFormula(Or, seqf) => seqf.flatMap(removeOr)
      case _ => Seq(f)
    }


    val seqmap: Map[Sequent, String] = problem.axioms.map {
      case Axiom(name, f) => f -> name
    }.toMap
    val namemap = mutMap[String, String]()
    println(seqmap)

    newsteps.foreach {
      case Axiom(name, bot) => 
        namemap += (name -> seqmap(bot.left |- removeOr(bot.right.head)))
      case _ => ()
    }
    
    val newsteps_originalnames = newsteps.map(step =>
      val newbot = step.bot.left |- removeOr(step.bot.right(0))
      step.mapBot(bot => newbot) match
        case Axiom(name, bot) => Axiom(namemap(name), bot)
        case LeftWeakenRes(name, bot, i, t1) => RightWeaken(name, bot, i, namemap.getOrElse(t1, t1))
        case step => step.renamePremises(namemap.toMap)
    )


    val premises2 = mutMap[String, Sequent]()
    val newsteps_clause = newsteps_originalnames.map( step =>
      premises2 += (step.name -> step.bot)
      step match 
        case r:Res2 => 
          r.castToRes(premises2.apply)
        case step => step
    )

    LVL2Proof(newsteps_clause.asInstanceOf, proof.thmName)
  }

  /**
    * Prove the problem without any pre or post processing
    *
    */
  def proveFile(filename: String): LVL2Proof = {
    val problem = parseProblem(File(filename))
    val proof = proveProblem(problem)
    proof
  }

  def _proveFile(filename: String): LVL2Proof = {
    val ladr2TPTPPathName = "tptp_to_ladr"
    val p9PathName = "prover9"
    val prooftransName = "prooftrans"

    val ladr2TPTPPath: InputStream = Option(
      getClass.getClassLoader.getResourceAsStream(ladr2TPTPPathName)
    ) match {
      case Some(stream) => stream
      case None =>
        throw new RuntimeException(
          s"Executable '$ladr2TPTPPathName' not found in the JAR"
        )
    }

    val p9PathPath: InputStream = Option(
      getClass.getClassLoader.getResourceAsStream(p9PathName)
    ) match {
      case Some(stream) => stream
      case None =>
        throw new RuntimeException(
          s"Executable '$p9PathName' not found in the JAR"
        )
    }

    val prooftransPath: InputStream = Option(
      getClass.getClassLoader.getResourceAsStream(prooftransName)
    ) match {
      case Some(stream) => stream
      case None =>
        throw new RuntimeException(
          s"Executable '$prooftransName' not found in the JAR"
        )
    }

    // Create the file if it doesn't exist
    val pathname2 = "./tmp/"
    val pathdir2 = Paths.get(pathname2)
    if (!(Files.exists(pathdir2) && Files.isDirectory(pathdir2)))
      Files.createDirectory(pathdir2)

    val ladr2TPTPTempExecutablePath = "/tmp/tptp_to_ladr"
    val p9TempExecutablePath = "/tmp/prouver9"
    val prooftransTempExecutablePath = "/tmp/prooftrans"

    Files.copy(
      ladr2TPTPPath,
      Paths.get(ladr2TPTPTempExecutablePath),
      StandardCopyOption.REPLACE_EXISTING
    )
    Files.copy(
      p9PathPath,
      Paths.get(p9TempExecutablePath),
      StandardCopyOption.REPLACE_EXISTING
    )
    Files.copy(
      prooftransPath,
      Paths.get(prooftransTempExecutablePath),
      StandardCopyOption.REPLACE_EXISTING
    )

    s"sh -c \" chmod +x $ladr2TPTPTempExecutablePath && chmod +x $p9TempExecutablePath && chmod +x $prooftransTempExecutablePath\"".!

    // Launch

    val pathfileinname = foldername + "/p9.in"
    val pathfileout = foldername + "/p9.out"

    val command =
      s"sh -c \"${ladr2TPTPTempExecutablePath} < ${filename} | ${p9TempExecutablePath} > ${pathfileinname} && ${prooftransTempExecutablePath} ivy -f ${pathfileinname} >  ${pathfileout}\""

    try {
      val exitCode = command.!
      if (exitCode == 0) {
        println("Executable ran successfully.")
      } else {
        println(s"Executable failed with exit code: $exitCode")
      }
    } catch {
      case e: Exception =>
        println(s"Error running the executable: ${e.getMessage}")
    }

    val fileContent = Source.fromFile(pathfileout).getLines().mkString("\n")

    // Debug: Check the positions of the start and end markers
    val startPos = fileContent.indexOf(";; BEGINNING OF PROOF OBJECT")
    val endPos = fileContent.indexOf(";; END OF PROOF OBJECT")

    // Extract content between the markers
    val proofContent = fileContent
      .substring(startPos + ";; BEGINNING OF PROOF OBJECT".length, endPos)
      .trim

    val pathfileoutname = foldername + "/p9proof.p"
    val pathproof = Paths.get(pathfileoutname)

    // Create the file if it doesn't exist
    if (!Files.exists(pathproof)) {
      Files.createFile(pathproof)
    }

    Files.write(pathproof, proofContent.getBytes(StandardCharsets.UTF_8))

    val proof = Parser.reconstructProof(new File(pathfileoutname)).asInstanceOf[LVL2Proof]
    proof
    
  }


  def certify_P9_variables(problem: Problem): LVL2Proof = {
    val insts = problem.axioms.map( ax =>
      val clause = ax.bot.right
      val mmap = mutMap[VariableSymbol, Term]()
      var i = 0
      clause.foreach (lit =>
        val fvs = lit.freeVariables
        fvs.foreach(fv =>
          if !mmap.contains(fv) then
            mmap += (fv -> Variable("V"+i))
          else
            ()
          end if
        )
      )
      val newclause = clause.map(lit => substituteVariablesInFormula(lit, mmap.toMap))
      val inst = InstantiateMult(ax.name+"_p9r", () |- newclause, 0, mmap.toSeq, ax.name)
      inst
    )
    val newAxs = insts.map(inst => Axiom(inst.t1, inst.bot))
    val renamed = insts.map(inst => inst.t1 -> inst.name).toMap
    val innerproof = proveProblem_no_rename(Problem(newAxs, problem.conjecture))
    val subproof = Subproof("p9_rename_sp", innerproof, Seq(), renamed)
    val newsteps = (insts :+ subproof).toIndexedSeq
    LVL2Proof(newsteps, "p9_rename_sp")

  }


}
