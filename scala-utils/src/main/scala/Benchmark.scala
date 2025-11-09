
package sctptp
import java.io.*
import scala.io.Source
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import sys.process._

import sctptp.SequentCalculus.checkProof

object Benchmark {
  
  val goelandExec = "../goeland/goeland_linux_release"

  val res_folder = "../proofs/benchmark/"
  val source_folder = "../proofs/SYN/"



  def goelandBenchmark(args: Array[String]): Unit = {
    val list_of_files = new File(source_folder).listFiles.filter(_.isFile).toList

    val list_proved = new File(res_folder +"goeland/").listFiles.filter(_.isFile).toList

    list_of_files.foreach { file =>
      if (list_proved.exists(_.getName == file.getName.dropRight(2) + "_sol.p")) {
        println(s"Skipping ${file.getName}")
      }
      else {
        val filename = file.getName
        val path = source_folder + filename
        val target = res_folder +"goeland/" + filename.dropRight(2) + "_sol"
        val ret = s"chmod u+x \"$goelandExec\"".!
        val cmd = (s"$goelandExec -otptp -wlogs -no_id -l 3 -quoted_pred -proof_file=$target $path")
        println(s"path: $path")
        println(s"target: $target")
        val res =
          try {
            //cmd.!!
          } catch {
            case e: Exception =>
              val writer = new PrintWriter(new File(target + ".p"))
              writer.write("Error: " + e.getMessage)
          }
        println("finished")
      }
    }

    val list_of_proofs = new File(res_folder+"goeland/").listFiles.filter(_.isFile).toList
    list_of_proofs.foreach { file =>
      val filename = file.getName
      val path = res_folder + "goeland/" + filename
      val proof = Parser.reconstructProof(new File(path))
      val res = checkProof(proof)
      res match {
        case SequentCalculus.StepCheckError(msg) =>
          println(s"Error: $msg") 
        case SequentCalculus.StepCheckOK =>
          println(s"Proof is correct")
        case SequentCalculus.StepCheckUnknown =>
          println(s"Proof seems correct but contains level 3 proof steps that have not been checked.")
      }
    }
  }
}
