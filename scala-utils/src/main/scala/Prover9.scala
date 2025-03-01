package sctptp
import SequentCalculus.*
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

object Prover9 {

  def main(args: Array[String]): Unit = {

    val proof = proveFile(args(0))
    println(proof)
  }

  val foldername = "p9proof/"
  val p9Exec = getClass.getResource
  

  def proveProblem(problem: Problem): SCProof[?] = {
    val pathdir = Paths.get(foldername)
    if (!(Files.exists(pathdir) && Files.isDirectory(pathdir)))
      Files.createDirectory(pathdir)

    // Create the file if it doesn't exist
    val pathfilepname = foldername + "/p9abc.p"
    val pathfilep = Paths.get(pathfilepname)
    if (!Files.exists(pathfilep)) {
      Files.createFile(pathfilep)
    }
    // Write the problem to the file
    Files.write(pathfilep, problem.toString.getBytes(StandardCharsets.UTF_8))

    val proof = proveFile(pathfilepname)
    proof
  }

  def proveFile(filename: String): SCProof[?] = {
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

    Parser.reconstructProof(new File(pathfileoutname))
    
  }


}
