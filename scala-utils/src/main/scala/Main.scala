package sctptp

import java.io.File
import Parser.*
import FOL.*
import SequentCalculus.*
import CoqOutput.CoqProof
import sctptp.SequentCalculus.SCProofStep
import sctptp.Tseitin
import sctptp.FOL.iff
import sctptp.LVL2.LVL2ProofStep
import java.io.File
import scala.io.Source
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import sys.process._

object Main {

  def main(args: Array[String]): Unit = {

    // if (args.size < 1){
    //   println("You need to provide a file")
    //   System.exit(0)
    // }

    // Problem
    val problem_file = "../proofs/clausification/simple2.p"
    val problem = reconstructProof(new File(problem_file))

    // parse conjecture
    val conjecture = problem.steps(0).asInstanceOf[LVL2ProofStep]
    val parsedProblem = problem.getSequent(0).right(0)
    val parsedProblemName = problem.thmName
    val myTseitin = new Tseitin()
    val (parsedProblem1, originalFormula, stepNC) =
      myTseitin.toNegatedFormula(parsedProblem)

    // NNF
    val (parsedProblem2, stepNNF) = myTseitin.toNNF(parsedProblem1)

    // Prenex
    val (parsedProblem3, stepPrenex) = myTseitin.toPrenex(parsedProblem2)

    // Instantiated and renamed
    val (parsedProblem4, mapVar, stepInst) =
      myTseitin.toInstantiated(parsedProblem3)
    val reverseMap = for ((k, v) <- mapVar) yield (v, k)

    // Unrenamed formula
    val parsedProblem5 = myTseitin.UnRenameVariables(parsedProblem4, reverseMap)

    // Creation of tseitin terms beforme renaming
    val premap = myTseitin.createTseitinVariables(parsedProblem4)
    myTseitin.makeTseitinMaps(premap._1)
    myTseitin.makeTseitinMapsUp(
      myTseitin.updateTseitinVariables(myTseitin.getTseitinTermVar())
    )

    // Tseitin Normal Form
    val parsedProblem6 = myTseitin.toTseitin(parsedProblem4)

    // Tseitin Normal Form
    val parsedProblem7 = myTseitin.toFlatternAnd(parsedProblem6)

    // Create let steps
    val (tseitinForms, tseitinStepMap, tseitinStepNames) =
      myTseitin.generateTseitin()

    // Generate tseistin replacement
    val lastInstForm = stepInst.last
    val tseitinReplacementStep = myTseitin.computeTseitinReplacementSteps(
      lastInstForm,
      tseitinStepNames.reverse,
      tseitinStepMap,
      tseitinForms.reverse,
      "i" + (stepInst.size - 1)
    )

    // Problem file
    val problem8 = myTseitin.toP9(parsedProblem7)

    // Create tseitin step
    val (problem9, last_step) = myTseitin.generateTseitinStep(
      parsedProblem7,
      tseitinStepNames.reverse,
      tseitinReplacementStep.reverse,
      problem8,
      tseitinStepMap,
      tseitinForms
    )
    val problem9_flat = myTseitin.modifyOrSteps(problem9)

    // Update ID

    val tseitinReplacementStep_up =
      myTseitin.updateId(tseitinReplacementStep.reverse, last_step)

    // Compute Context
    val context = originalFormula +: tseitinStepNames.reverse

    // Add step to the proof
    val newProof = problem9_flat
    val newProof3 =
      newProof.addStepsLVL2Before(tseitinReplacementStep_up)
    val newProof4 = newProof3.addStepsLVL2Before(tseitinForms)
    val newProof5 = newProof4.addStepsLVL2Before(stepInst)
    val newProof6 = newProof5.addStepLVL2Before(stepPrenex)
    val newProof7 = newProof6.addStepLVL2Before(stepNNF)
    val newProof8 = newProof7.addStepsLVL2Before(stepNC)
    val newProof9 = myTseitin.removeFalse2(newProof8)
    val newProof10 = newProof9.addStepsLVL2After(
      myTseitin.addPsi(context, newProof9.steps.last.name)
    )
    val newProof11 = newProof10.addStepsLVL2After(
      myTseitin.removeTseitin(tseitinStepNames, tseitinStepMap)
    )
    val newProof12 = myTseitin.renameTseitinConstant(newProof11)
    val newProof13 = newProof12.addStepLVL2Before(conjecture)

    println("\nProof :")
    printProof(newProof13)
    // newProof13.steps.drop(5).dropRight(25).map(x => println(x))
    println("\n")

    val final_proof = ProofToString(newProof13)

    // Write in a file
    val output_file = {
      if (args.size == 2)
      then args(1)
      else problem_file.dropRight(2).+("_output.p")
    }

    val path = Paths.get(output_file)

    // Create the file if it doesn't exist
    if (!Files.exists(path)) {
      Files.createFile(path)
    }

    Files.write(path, final_proof.getBytes(StandardCharsets.UTF_8))
  }
}
