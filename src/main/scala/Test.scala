import java.io.File
import Parser.* 

object Test {
  
  def main(args: Array[String]): Unit = {
    println("\n First proof")
    reconstructProof(new File("proofs/Test.gothm0.p")).steps.foreach(println)
    println("\n Second proof")
    reconstructProof(new File("proofs/Test.gothm1.p")).steps.foreach(println)
  }
}
