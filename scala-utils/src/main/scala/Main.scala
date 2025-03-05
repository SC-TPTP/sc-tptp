package sctptp


import mainargs.{main, arg, ParserForMethods, Flag}

import java.io.File
import Parser.*
import FOL.*
import SequentCalculus.*
import sctptp.SequentCalculus.SCProofStep
import sctptp.FOL.iff
import sctptp.LVL2.LVL2ProofStep
import java.io.File
import scala.io.Source
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import sys.process._
import Prover9 as P9

object Main {

  @main
  def p9( @arg(doc = "Input File")
          input: String,
          @arg(doc = "Output File")
          output: String,
          @arg(doc = "runs P9 without any preprocessing")
          raw: Flag) = {
    if raw.value then
      println("Debug mode")
      val proof = P9._proveFile(input)
      val target = output
      Files.write(Paths.get(target), proof.toString.getBytes(StandardCharsets.UTF_8))
    else
      val file = input
      val problem = parseProblem(File(file))
      val proof = P9.fullP9Prover(problem)
      val target = output
      Files.write(Paths.get(target), proof.toString.getBytes(StandardCharsets.UTF_8))
  }

  @main
  def check( @arg(doc = "Input File")
             input: String) = {
    val proof = reconstructProof(File(input))
    val res = checkProof(proof)
      res match {
        case Some((msg, step)) =>
          println(s"Error: $msg")
          println(s"Step: $step")
        case None =>
          println(s"Proof is correct")
      }
  }


  def main(args: Array[String]): Unit = ParserForMethods(this).runOrThrow(args)

}
