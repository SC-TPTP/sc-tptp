package sctptp

import org.scalatest.compatible.Assertion
import org.scalatest.funsuite.AnyFunSuite
import scala.io.Source
import java.io.File
import SequentCalculus.checkProof

import leo.modules.input.TPTPParser
import sctptp.SequentCalculus.StepCheckError
import sctptp.SequentCalculus.StepCheckOK
import sctptp.SequentCalculus.StepCheckUnknown

class LVL1Test extends AnyFunSuite {



  private val sources = getClass.getResource("/steps_tests").getPath
  println(s"Sources: $sources")


  private val problems = Seq(
    "cut.p" -> "cut rule tests",
    "hyp.p" -> "hyp rule tests",
    "instFun.p" -> "instFun rule tests",
    "instPred.p" -> "instPred rule tests",
    "leftForall.p" -> "leftForall rule tests",
    "leftAnd.p" -> "leftAnd rule tests",
    "leftExists.p" -> "leftExists rule tests",
    "leftIff.p" -> "leftIff rule tests",
    "leftImplies.p" -> "leftImplies rule tests",
    "leftNot.p" -> "leftNot rule tests",
    "leftOr.p" -> "leftOr rule tests",
    "leftSubst.p" -> "leftSubst rule tests",
    "leftSubstIff.p" -> "leftSubstIff rule tests",
    "leftWeaken.p" -> "leftWeaken rule tests",
    "rightForall.p" -> "rightForall rule tests",
    "rightAnd.p" -> "rightAnd rule tests",
    "rightExists.p" -> "rightExists rule tests",
    "rightIff.p" -> "rightIff rule tests",
    "rightImplies.p" -> "rightImplies rule tests",
    "rightNot.p" -> "rightNot rule tests",
    "rightOr.p" -> "rightOr rule tests",
    "rightRefl.p" -> "rightRefl rule tests",
    "rightSubst.p" -> "rightSubst rule tests",
    "rightSubstIff.p" -> "rightSubstIff rule tests",
    "rightWeaken.p" -> "RightWeaken rule tests",
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
