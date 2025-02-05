package sctptp
import SequentCalculus.LVL1ProofStep
import SequentCalculus as SC
import LVL2.*
import datastructure.mutable.EGraphTerms
import FOL.*

class Tseitin {

  val fvTseitin: Set[VariableSymbol] = Set[VariableSymbol]()
  var tseitinVarTerm: Map[VariableSymbol, sctptp.FOL.Formula] = Map[VariableSymbol, sctptp.FOL.Formula]()
  var tseitinTermVar: Map[sctptp.FOL.Formula, VariableSymbol] = Map[sctptp.FOL.Formula, VariableSymbol]()


  def getTseitinVarTerm(): Map[VariableSymbol, sctptp.FOL.Formula] = tseitinVarTerm

  def getTseitinTermVar(): Map[sctptp.FOL.Formula, VariableSymbol] = tseitinTermVar


  def printTseitinVarTerm() = {
    for ((k, v) <- getTseitinVarTerm()) {
      println(k.toString() + " : " + v.toString())
    }
  }

  def makeTseitinMaps(TermVar: Map[sctptp.FOL.Formula, VariableSymbol]) = {
    tseitinTermVar = TermVar
    tseitinVarTerm = TermVar.map(_.swap)
  }

  // Go through the formula and associate each subformula to a variable
  def createTseitinVariables(
      f: sctptp.FOL.Formula,
      acc: Map[sctptp.FOL.Formula, VariableSymbol] = Map[sctptp.FOL.Formula, VariableSymbol](),
      next_index: Int = 0
  ): (Map[sctptp.FOL.Formula, VariableSymbol], Int) = {
    // println("Next id = " + next_index)
    // println("acc = " + acc.toString())
    // println("f = " + f.toString())
    // println("Contains : " + acc.contains(f))
    // println("------------------------------")
    f match
      case AtomicFormula(label, args) => {
        if (acc.contains(f)) {
          (acc, next_index)
        } else {
          (acc + (f -> VariableSymbol(freshId(fvTseitin.map(_.name), "X" + next_index))), next_index + 1)
        }
      }
      case ConnectorFormula(label, args) => {
        if (acc.contains(f)) {
          args.foldLeft((acc, next_index))((acc2, c) => (createTseitinVariables(c, acc2._1, next_index)._1, acc2._2))
        } else {
          args.foldLeft((acc, next_index))((acc2, c) => {
            val (newAcc, newCpt) =  createTseitinVariables(c, acc2._1 + (f -> VariableSymbol(freshId(fvTseitin.map(_.name), "X" + acc2._2))), acc2._2 + 1)
            (newAcc, newCpt)
          })
        }

      }
      case BinderFormula(label, bound, inner) => {
        if (acc.contains(f)) {
          createTseitinVariables(inner, acc, next_index)
        } else {
          createTseitinVariables(inner, acc + (f -> VariableSymbol(freshId(fvTseitin.map(_.name), "X" + next_index))), next_index + 1)
        }
      }
  }

  // add equivalences into the formula
  // Input : a formula 
  // Output : a formula
  def addEquivalences(f: sctptp.FOL.Formula): sctptp.FOL.Formula = {
    f match
      case AtomicFormula(label, args) => ConnectorFormula(Iff, Seq(AtomicFormula(AtomicSymbol(getTseitinTermVar()(f).id, 0), Seq()), f))
      case ConnectorFormula(label, args) => {
        label match {
          // en fold
          case Or => ConnectorFormula(Iff, Seq(AtomicFormula(AtomicSymbol(getTseitinTermVar()(f).id, 0), Seq()), f)) 
          // recreate a big OR
          // ConnectorFormla, OR, result)
        }
      }
      case BinderFormula(label, bound, inner) => ???
  }

  // case (ConnectorFormula(Neg, Seq(x)), AtomicFormula(_, _))

 

 
  // Rewrite every equivalences into CNF
  // Rules for \/, /\, =>, <=>, !

  // List of steps
  // Input : formula in PNF, Skolemized

  // Return a list of disjunctive formula
  // def toCNF(f: sctptp.FOL.Formula): Seq[sctptp.FOL.Formula] = {
  //     f match
  //       case AtomicFormula | FOF.Equality | FOF.Inequality => Seq(f)
  //       case FOF.UnaryFormula(connective, body) =>
  //         connective match {
  //           case FOF.~~ => toCNF(f)
  //           case FOF.~ => body match
  //             case FOF.AtomicFormula | FOF.Equality | FOF.Inequality => Seq(unary_!(f))
  //         }

  //       case ConnectorFormula(connective, args: Seq[Formula]) =>
  //         connective match {
  //           case FOF.& => toCNF(left) ++ toCNF(right)
  //           case FOF.| => {
  //             val newL= toCNF(left)
  //             val newR= toCNF(right)
  //             newL.foldLeft(Seq())( (acc, x) => acc ++ newR.foldLeft(Seq())((acc2, y) => acc2 ++ Seq(x, y)))
  //           }
  //           case FOF.~& => toCNF(unary_!(left) \/ unary_!(right))
  //           case FOF.~| => toCNF(unary_!(left)) ++ toCNF(unary_!(right))
  //           case FOF.Impl => toCNF(!left \/ right)
  //           case FOF.<= => toCNF(!right \/ left)
  //           case FOF.<=> => toCNF((left /\ right) \/ (!left /\ !right))
  //           case FOF.<~> => toCNF((!left /\ right) \/ (left /\ !right))
  //         }
  // Binder formulas
  // }
}
