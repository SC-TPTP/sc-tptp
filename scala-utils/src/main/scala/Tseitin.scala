package sctptp
import SequentCalculus.LVL1ProofStep
import SequentCalculus as SC
import LVL2.*
import datastructure.mutable.EGraphTerms
import FOL.*
import sctptp.FOL.AtomicFormula

class Tseitin {
  var tseitinVarTerm: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula]()
  var tseitinTermVar: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula]()

  def getTseitinVarTerm(): Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = tseitinVarTerm

  def getTseitinTermVar(): Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = tseitinTermVar

  def printTseitinVarTerm() = {
    for ((k, v) <- getTseitinVarTerm()) {
      println(k.toString() + " : " + v.toString())
    }
  }

  def makeTseitinMaps(TermVar: Map[sctptp.FOL.Formula, sctptp.FOL.Formula]) = {
    tseitinTermVar = TermVar
    tseitinVarTerm = TermVar.map(_.swap)
  }

  // Go through the formula and associate each subformula to a variable
  def createTseitinVariables(
      f: sctptp.FOL.Formula,
      acc: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula](),
      next_index: Int = 0
  ): (Map[sctptp.FOL.Formula, sctptp.FOL.Formula], Int) = {
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
          (acc + (f -> AtomicFormula(AtomicSymbol(Identifier("ts" + next_index, next_index), f.freeVariables.size), f.getFreeVariables())), next_index + 1)
        }
      }
      case ConnectorFormula(label, args) => {
        if (acc.contains(f)) {
          args.foldLeft((acc, next_index))((acc2, c) => (createTseitinVariables(c, acc2._1, next_index)._1, acc2._2))
        } else {
          args.foldLeft((acc, next_index))((acc2, c) => {
            val (newAcc, newCpt) = createTseitinVariables(c, acc2._1 + (f -> AtomicFormula(AtomicSymbol(Identifier("ts" + acc2._2, acc2._2), f.freeVariables.size), f.getFreeVariables())), acc2._2 + 1)
            (newAcc, newCpt)
          })
        }

      }
      case BinderFormula(label, bound, inner) => {
        if (acc.contains(f)) {
          createTseitinVariables(inner, acc, next_index)
        } else {
          createTseitinVariables(inner, acc + (f -> AtomicFormula(AtomicSymbol(Identifier("ts" + next_index, next_index), f.freeVariables.size), f.getFreeVariables())), next_index + 1)
        }
      }
  }

  // Transform a formula in Prenex Form into nnf
  def toNNF(f: sctptp.FOL.Formula): sctptp.FOL.Formula = {
    f match
      case AtomicFormula(label, args) => f
      case ConnectorFormula(label, args) => {
        label match
          case Neg => {
            args(0) match
              case AtomicFormula(_, _) => f
              case ConnectorFormula(label2, args2) => {
                label2 match
                  case Neg => f
                  case Or => sctptp.FOL.ConnectorFormula(And, args2.map(x => toNNF(ConnectorFormula(Neg, Seq(x)))))
                  case And => sctptp.FOL.ConnectorFormula(Or, args2.map(x => toNNF(ConnectorFormula(Neg, Seq(x)))))
                  case Implies => sctptp.FOL.ConnectorFormula(And, Seq(toNNF(args2(0)), toNNF(ConnectorFormula(Neg, Seq(args2(1))))))
                  case Iff =>
                    sctptp.FOL.ConnectorFormula(
                      Or,
                      Seq(
                        sctptp.FOL.ConnectorFormula(And, Seq(toNNF(args2(0)), toNNF(args2(1)))),
                        sctptp.FOL.ConnectorFormula(And, Seq(toNNF(ConnectorFormula(Neg, Seq(args2(0)))), toNNF(ConnectorFormula(Neg, Seq(args2(1))))))
                      )
                    )
              }
              case BinderFormula(label, bound, inner) => throw new Exception("Formula not in Prenex form")
          }
          case Or => sctptp.FOL.ConnectorFormula(Or, args.map(x => toNNF(x)))
          case And => sctptp.FOL.ConnectorFormula(And, args.map(x => toNNF(x)))
          case Implies => sctptp.FOL.ConnectorFormula(And, Seq(ConnectorFormula(Neg, Seq(args(0))), toNNF(args(1))))
          case Iff =>
            sctptp.FOL.ConnectorFormula(
              Or,
              Seq(
                sctptp.FOL.ConnectorFormula(And, Seq(toNNF(args(0)), toNNF(args(1)))),
                sctptp.FOL.ConnectorFormula(And, Seq(toNNF(ConnectorFormula(Neg, Seq(args(0)))), toNNF(ConnectorFormula(Neg, Seq(args(1))))))
              )
            )
      }
      case BinderFormula(label, bound, inner) => throw new Exception("Formula not in Prenex form")
  }

  // add equivalences into the formula
  // Input : a formula f with a subformula sf
  // Output : a formula f in which the subformula sf have be changed to sf <=> its equivalence
  def addEquivalences(f: sctptp.FOL.Formula, sf: sctptp.FOL.Formula): sctptp.FOL.Formula = {
    f match
      case AtomicFormula(label, args) => ConnectorFormula(Iff, Seq(getTseitinTermVar()(f), f))
      case ConnectorFormula(label, args) => {
        label match {
          // en fold
          case Or => ConnectorFormula(Iff, Seq(getTseitinTermVar()(f), f))
          // recreate a big OR
          // ConnectorFormla, OR, result)
        }
      }
      case BinderFormula(label, bound, inner) => ???
  }

  // case (ConnectorFormula(Neg, Seq(x)), AtomicFormula(_, _))

  // Create TSpredicates

  // List of steps
  // Input : formula in PNF, Skolemized
}
