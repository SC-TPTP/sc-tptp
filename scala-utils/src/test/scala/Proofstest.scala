package sctptp

import org.scalatest.compatible.Assertion
import org.scalatest.funsuite.AnyFunSuite
import scala.io.Source
import java.io.File
import SequentCalculus.*

import leo.modules.input.TPTPParser

class Proofstest extends AnyFunSuite {



  private val sources = getClass.getResource("/tests").getPath
  println(s"Sources: $sources")


  private val problems = Seq(
    "testiff_sol.p" -> "egg test testiff_sol"
    /**/
  )


  for (p <- problems) {
    test(p._2) {
      println("###################################")
      print(s"Parsing ${p._1} ...")
      try {
        val res = Parser.reconstructProof(File(s"$sources/${p._1}"))
        checkProof(res) match
          case StepCheckError(msg) =>
            println(s"Error: $msg")
            fail()
          case StepCheckOK =>
            println("Proof is correct.")
          case StepCheckUnknown =>
            println("Proof is probably correct, but some steps could not be checked (missing step checking implementation).")
        

        println(s"Parsed ${p._1}")
      } catch {
        case e: TPTPParser.TPTPParseException =>
          println(s"Parse error at line ${e.line}:${e.offset}: ${e.getMessage}")
          fail()
      }
    }
  }

}