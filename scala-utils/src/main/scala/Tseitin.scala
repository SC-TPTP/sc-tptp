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
import javax.imageio.plugins.tiff.ExifGPSTagSet
import scala.util.boundary
import scala.annotation.switch

class Tseitin {

  val varName = "V"
  val tseitinName = "X"
  var varCpt = 0
  val NoneFormula = AtomicFormula(AtomicSymbol("None", 0), Seq())

  // Var = Tseitin Var, Term = Original formula Term
  var tseitinVarTerm: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula]()
  var tseitinTermVar: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula]()
  var tseitinVarTermUp: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula]()
  var tseitinTermVarUp: Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = Map[sctptp.FOL.Formula, sctptp.FOL.Formula]()

  def getTseitinVarTerm(): Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = tseitinVarTerm

  def getTseitinTermVar(): Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = tseitinTermVar

  def getTseitinVarTermUp(): Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = tseitinVarTermUp

  def getTseitinTermVarUp(): Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = tseitinTermVarUp

  def printTseitinVarTerm() = {
    for ((k, v) <- getTseitinVarTerm()) {
      println(k.toString() + " : " + v.toString())
    }
  }

  def printTseitinVarTermUp() = {
    for ((k, v) <- getTseitinVarTermUp()) {
      println(k.toString() + " : " + v.toString())
    }
  }

  def makeTseitinMaps(TermVar: Map[sctptp.FOL.Formula, sctptp.FOL.Formula]) = {
    tseitinTermVar = tseitinTermVar ++ TermVar
    tseitinVarTerm = tseitinVarTerm ++ TermVar.map(_.swap)
  }

  def makeTseitinMapsUp(TermVar: Map[sctptp.FOL.Formula, sctptp.FOL.Formula]) = {
    tseitinTermVarUp = tseitinTermVarUp ++ TermVar
    tseitinVarTermUp = tseitinVarTermUp ++ TermVar.map(_.swap)
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
        case AtomicFormula(label, args) => (List(), f2) 
        case BinderFormula(label, bound, inner) => {
          val (accQT2, accF2) = retrieveQT(inner)
          (accQT2 :+ (label, bound), accF2)
        } 
        case ConnectorFormula(label, args) => {
          val (accQT2, accF2) = args.foldLeft((List[(BinderSymbol, VariableSymbol)](), List[Formula]()))((acc, x) => {
             val (accQT3, accF3) = retrieveQT(x)
             (accQT3 ++ acc._1,  acc._2 :+ accF3)
          })
          (accQT2, ConnectorFormula(label, accF2))
        }
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

  // Instantiate universal quantifiers and returns a map [oldName, newName], the new formula, and has renamed them according to P9 standard
  def toInstantiated(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, Map[VariableSymbol, VariableSymbol]) = {
      def toInstantiatedAux(f2: sctptp.FOL.Formula, accVS: Map[VariableSymbol, VariableSymbol], accT: Map[VariableSymbol, Term]): (sctptp.FOL.Formula, Map[VariableSymbol, VariableSymbol]) = {
        f2 match 
          case BinderFormula(label, bound, inner) => {
            label match
              case Forall => {
                val new_symbol =VariableSymbol(varName+varCpt)
                val new_accVS = accVS + (bound -> new_symbol)
                val new_accT = accT + (bound -> Variable(new_symbol))
                varCpt = varCpt+1
                val (new_f, new_acc_next_steps) = toInstantiatedAux(substituteVariablesInFormula(inner, new_accT), new_accVS, new_accT)
                (new_f, new_acc_next_steps)
              }
              case Exists => {
                
                val new_symbol = VariableSymbol(varName+varCpt)
                val new_accVS = accVS + (bound -> new_symbol)
                val new_accT = accT + (bound -> Variable(new_symbol))
                varCpt = varCpt+1
                val (new_f, new_acc_next_steps) = toInstantiatedAux(substituteVariablesInFormula(inner, new_accT), new_accVS, new_accT)
                (BinderFormula(Exists, new_symbol, new_f), new_acc_next_steps)
              }
          }
          case _ => (f2, accVS)
      }

      toInstantiatedAux(f,  Map[VariableSymbol, VariableSymbol](), Map[VariableSymbol, Term]())
  }

  // Retrieve the original name of variables
  def UnRenameVariables(f: sctptp.FOL.Formula,  map: Map[VariableSymbol, VariableSymbol]): sctptp.FOL.Formula = {
    val reverseMap = for ((k, v) <- map) yield (v, k) 
    val accT = Map[VariableSymbol, Term]()

    def unRenameVariablesInTerm(t: sctptp.FOL.Term) : sctptp.FOL.Term = {
      t match
        case Term(label: VariableSymbol, _) if (reverseMap contains label) =>  Term(reverseMap(label), Seq())
        case Term(label, args) => Term(label, args.map(unRenameVariablesInTerm(_)))
    }

    def UnRenameVariablesAux(f2: sctptp.FOL.Formula): sctptp.FOL.Formula = {
      f2 match 
        case AtomicFormula(label, args) => AtomicFormula(label, args.map(unRenameVariablesInTerm(_)))
        case BinderFormula(label, bound, inner) => {
            if (reverseMap contains bound)
                then BinderFormula(label, reverseMap(bound), UnRenameVariablesAux(substituteVariablesInFormula(inner, accT + (bound -> Variable(reverseMap(bound))))))
                else BinderFormula(label, bound, UnRenameVariablesAux(inner))
            }
        case ConnectorFormula(label, args) => ConnectorFormula(label, args.map(UnRenameVariablesAux(_)))
    }

    UnRenameVariablesAux(f)
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
          (acc + (f -> AtomicFormula(AtomicSymbol(Identifier(tseitinName + next_index, next_index), f.freeVariables.size), f.getFreeVariables())), next_index + 1)
        }
      }
      case ConnectorFormula(label, args) => {
        if (acc.contains(f)) {
          args.foldLeft((acc, next_index))((acc2, c) => (createTseitinVariables(c, acc2._1, next_index)._1, acc2._2))
        } else {
          args.foldLeft((acc, next_index))((acc2, c) => {
            val (newAcc, newCpt) = createTseitinVariables(c, acc2._1 + (f -> AtomicFormula(AtomicSymbol(Identifier(tseitinName + acc2._2, acc2._2), f.freeVariables.size), f.getFreeVariables())), acc2._2 + 1)
            (newAcc, newCpt)
          })
        }

      }
      case BinderFormula(label, bound, inner) => {
        if (acc.contains(f)) {
          createTseitinVariables(inner, acc, next_index)
        } else {
          createTseitinVariables(inner, acc + (f -> AtomicFormula(AtomicSymbol(Identifier(tseitinName + next_index, next_index), f.freeVariables.size), f.getFreeVariables())), next_index + 1)
        }
      }
  }

  // Take tseitinTermVar et return tseitinTermVar updated
  def updateTseitinVariables(map: Map[sctptp.FOL.Formula, sctptp.FOL.Formula]) : Map[sctptp.FOL.Formula, sctptp.FOL.Formula] = {
    map.foldLeft(Map[sctptp.FOL.Formula, sctptp.FOL.Formula]())((acc, x) =>
      val (termForm, varTseitin) = x
      termForm match {
        case AtomicFormula(label, args) => acc + (termForm -> varTseitin)
        case ConnectorFormula(label, args) => acc + (ConnectorFormula(label, args.map(x => map(x))) -> varTseitin)
        case  BinderFormula(label, bound, inner) => acc + (BinderFormula(label, bound, map(inner)) -> varTseitin)
      }
    )
  }

  // Transform a formula in Prenex Negatted Normal form into Tseitin Normal Form with variable stored in tseitinVarTerm
  def toTseitin(f: sctptp.FOL.Formula): sctptp.FOL.Formula =  {

    def toTseitinAux(f: sctptp.FOL.Formula): (sctptp.FOL.Formula, sctptp.FOL.Formula) = {
    val varT = getTseitinTermVar()(f)
    val varTneg = ConnectorFormula(Neg, Seq(varT))
    f match
      case AtomicFormula(label, args) => (f, NoneFormula) // getTseitinTermVar()(f) //ConnectorFormula(Iff, Seq(getTseitinTermVar()(f), f))
      case ConnectorFormula(label, args) => {
        println(s"La variable introduite pour ${f.toString()} est ${varT.toString()}")
        label match {
          case Neg => {
            val (p, c) = toTseitinAux(args(0))
            (ConnectorFormula(Neg, Seq(p)), c)
          }
          case And => {
            val (p1, c1) = toTseitinAux(args(0))
            val (p2, c2) = toTseitinAux(args(1))
            val formTokeep = 
              (c1, c2) match
                case (NoneFormula, NoneFormula) => Seq()
                case (_, NoneFormula) => Seq(c1)
                case (NoneFormula, _) => Seq(c2)
                case (_, _) => Seq(c1, c2)
            val new_c = ConnectorFormula(And, formTokeep ++ Seq(ConnectorFormula(Or, Seq(varT, ConnectorFormula(Neg, Seq(p1)), ConnectorFormula(Neg, Seq(p2))))) :+ ConnectorFormula(Or, Seq(varTneg, p1)) :+ ConnectorFormula(Or, Seq(varTneg, p2)))
            (varT, new_c)
          } 

          case Or => {
            val (p1, c1) = toTseitinAux(args(0))
            val (p2, c2) = toTseitinAux(args(1))
            val formTokeep = 
              (c1, c2) match
                case (NoneFormula, NoneFormula) => Seq()
                case (_, NoneFormula) => Seq(c1)
                case (NoneFormula, _) => Seq(c2)
                case (_, _) => Seq(c1, c2)
            val new_c = ConnectorFormula(And, formTokeep ++ Seq(ConnectorFormula(Or, Seq(varTneg, p1, p2))) :+ ConnectorFormula(Or, Seq(varT, ConnectorFormula(Neg, Seq(p1)))) :+ ConnectorFormula(Or, Seq(varT, ConnectorFormula(Neg, Seq(p2)))))
            (varT, new_c)
          } 
          case _ => throw new Exception("toTseitin failed")
        }
      }
      case BinderFormula(label, bound, inner) => (varT, BinderFormula(label, bound, toTseitin(inner)))
    }

    val (l: sctptp.FOL.Formula, c: sctptp.FOL.Formula) = toTseitinAux(f) 
    println(s"c = ${c.toString()}")
    println(s"l = ${l.toString()}")
    if (c != NoneFormula) then ConnectorFormula(And, Seq(l, c))
    else l
  }

          // case Neg =>  (varT, ConnectorFormula(And, Seq(ConnectorFormula(Or, Seq(varTneg, f)), ConnectorFormula(Or, Seq(varT, args(0))))))
          // case And => (varT, ConnectorFormula(And, ConnectorFormula(Or, varT +: args.map(x => ConnectorFormula(Neg, Seq(x)))) +: args.map(x => ConnectorFormula(Or, Seq(varTneg, x)))))
          // case Or =>  (varT, ConnectorFormula(And, ConnectorFormula(Or, varTneg +: args.map(x => x)) +: args.map(x => ConnectorFormula(Or, Seq(varT, ConnectorFormula(Neg, Seq(x)))))))



  // // Flattern  a formula in CNF
  // def toFlattern(f: sctptp.FOL.Formula): sctptp.FOL.Formula = {
  //   def toFlatternAux(f: sctptp.FOL.Formula): Seq[sctptp.FOL.Formula] = {
  //     f match
  //       case ConnectorFormula(And, args) => {
  //           args
  //       }
  //       case _ => args
  //   }

  //   ConnectorFormula(And, toFlatternAux(f))
  // }


  // add equivalences into the formula
  // Input : a formula f with a subformula sf
  // Output : a formula f in which the subformula sf have be changed to sf <=> its equivalence
  def addEquivalences(f: sctptp.FOL.Formula, sf: sctptp.FOL.Formula): sctptp.FOL.Formula = {
    ???
  }

  // replace exists x by #x + the formula it satisfy (let too?)
  // But what about the prover? How will the epsilon term will be managed?
  // Reconstruct at the end --- wrapper?
  def addEpsilonTerms(f: sctptp.FOL.Formula): sctptp.FOL.Formula = ??? 


  // Prend une preuve et remplace les variable x), .. par les variables initiales
  // def desInstantiate()



  // Theorie : search equivalence of formula (/\, \/, =>, <=>) into tseitin => done for /\, ~ et \/, for the rest, juste simplify

  /** Schéma global de la preuve :
  * Formule f en entrée, fof
  * [x] Mise en NFF
  * [x] Mise sous forme prénexe
  * [x] Renommer variable forall
  * [x] Instanciation forall
  * [x] récupération des variable schématiques
  * [ ] Ajout des variable tseitin level 0 (pour avoir une formule en CNF) et de leur traduction en forme étendue (i.e., j'introduis X <=> (a /\ b), et je rajoute dans la formule (¬X ∨ a), (¬X ∨ b), (X ∨ ¬a ∨ ¬b)) -> Faire ça récursivement (si la subformula n'est pas atomic)
  * [ ] Ajouter les let
  * [ ] donner la formule finale P9 (sous forme axiom et sans quantifier universel)
  * [ ] Récupérer preuve
  * [x] Derenommer les variables
  * [ ] Epslion-termes 
  * [ ] Introduce a routine to elaborate the rule for prenex and nnf
  * [ ] Res -> coq
  * [ ] schematique : `X`
  * 
  * Annex function 
  * Generation of scheme equivalent rule directly as strings (i.e., fof(axiom, tseq1, a <=>))
  * Generation of ts equivalences itself (\psi <=> ....)
  * return proof step, together with the transformation
  * after the proof by P9: Epsilon rule catcher? Rewrite instances of x into #x (let #x1, #x1 P(x1)
  */
}
