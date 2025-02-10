package sctptp
import SequentCalculus.LVL1ProofStep
import SequentCalculus as SC
import LVL2.*
import datastructure.mutable.EGraphTerms
import FOL.*
import sctptp.FOL.AtomicFormula
import java.text.Normalizer.Form
import sctptp.FOL.Forall
import sctptp.FOL.Variable

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

  // Transform a formula into nnf
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
                  case Neg => args2(0)
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
              case BinderFormula(label, bound, inner) => {
                label match
                  case Forall => sctptp.FOL.BinderFormula(Exists, bound, toNNF(ConnectorFormula(Neg, Seq(inner))))
                  case Exists => sctptp.FOL.BinderFormula(Forall, bound, toNNF(ConnectorFormula(Neg, Seq(inner))))
                
              }
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
      case BinderFormula(label, bound, inner) => sctptp.FOL.BinderFormula(label, bound, toNNF(inner))
  }

  // Transform a formula in Prenex Normal form (move quantifier to the top)
  def toPrenex(f: sctptp.FOL.Formula): sctptp.FOL.Formula = {

    def retrieveQT(f2 : sctptp.FOL.Formula): (List[(BinderSymbol, VariableSymbol)], sctptp.FOL.Formula) = {
      f2 match 
         case AtomicFormula(label, args) => (List(), f) 
         case BinderFormula(label, bound, inner) => {
          val (accQT2, accF2) = retrieveQT(inner)
          ((label, bound) +: accQT2, accF2)
         } 
         // TODO
        //  case ... 
    }

    def reInsertQT(accQT: List[(BinderSymbol, VariableSymbol)], accF2 : sctptp.FOL.Formula) : sctptp.FOL.Formula = {
      accQT.foldLeft(accF2)((acc, x) => {
        x match 
          case (Forall, x2) => sctptp.FOL.BinderFormula(Forall, x2, acc)
          case (Exists, x2) => sctptp.FOL.BinderFormula(Exists, x2, acc)
      })
    }

    val (accQT, accF) = retrieveQT(f)
    reInsertQT(accQT, accF)
  }

  // Instantiate universal quantifiers and returns a map [oldName, newName]
  def toInstantiated(f: sctptp.FOL.Formula): sctptp.FOL.Formula = ???


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

  // Transform a formula in Prenex Negation Normal form into Tseitin Normal Form with variable stored in tseitinVarTerm
  def toTseitin(f: sctptp.FOL.Formula): sctptp.FOL.Formula =  ???



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

  // replace exists x by #x + the formula it satisfy (let too?)
  // But what about the prover? How will the epsilon term will be managed?
  // Reconstruct at the end --- wrapper?
  def addEpsilonTerms(f: sctptp.FOL.Formula): sctptp.FOL.Formula = ??? 



  // Theorie : search equivalence of formula (/\, \/, =>, <=>) into tseitin => done for /\, ~ et \/, fpr the rest, juste simplify

  /** Schéma global de la preuve :
  * Formule f en entrée, fof
  * [x] Mise en NFF
  * [ ] Mise sous forme prénexe
  * [ ] Renomer variable forall
  * [ ] Instanciation forall
  * [x] récupération des variable schématiques
  * [ ] Ajout des variable tseitin level 0 (pour avoir une formule en CNF) et de leur traduction en forme étendue (i.e., j'introduis X <=> (a /\ b), et je rajoute dans la formule (¬X ∨ a), (¬X ∨ b), (X ∨ ¬a ∨ ¬b)) -> Faire ça récursivement (si la subformula n'est pas atomic)
  * [ ] donner la formule finale P9
  * [ ] Récupérer preuve
  * [ ] Derenommer les variables
  * [ ] Epslion-termes 
  * [ ] Introduce a routine to elaborate the rule for prenex and nnf
  * 
  * Annex function 
  * Generation of scheme equivalent rule directly as strings (i.e., fof(axiom, tseq1, a <=>))
  * Generation of ts equivalences itself (\psi <=> ....)
  * return proof step, together with the transformation
  * after the proof by P9: Epsilon rule catcher? Rewrite instances of x into #x (let #x1, #x1 P(x1)
  */
}
