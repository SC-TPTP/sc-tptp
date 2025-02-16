package sctptp
import SequentCalculus.LVL1ProofStep
import SequentCalculus as SC
import LVL2.*
import datastructure.mutable.EGraphTerms
import FOL.*


object CongruenceClosure {

  

  def eliminateCongruence(steps: Seq[LVL2ProofStep]): Seq[LVL2ProofStep] = {
    var stringSize = 1
    val stepString = steps.foreach { step =>
      val Pattern = "\\A(s*)".r
      step.name match
        case Pattern(c)  if c.size > stringSize => stringSize = c.size
        case _ => ()
    }
    val sname = "s"*stringSize
    var unfoldCounter = 0
    val result: Seq[Seq[LVL2ProofStep]] = steps map {
      case Congruence(name, bot) => 
        var subProof = List[LVL2ProofStep]()
        val egraph = EGraphTerms()
        (bot.left ++ bot.right).foreach(egraph.addAllTerms)
        
        bot.left.collect { 
          case eq @ AtomicFormula(`equality`, Seq(l, r)) => 
            (l, r)
            egraph.merge(l, r)
        }

        val leftOnly = bot.left.view.zipWithIndex.map { (la, i) => la match
          case la @ AtomicFormula(label, largs) => 
            val matching = bot.left.view.zipWithIndex.map { (ra, j) => ra match
              case ConnectorFormula(`neg`, List(raa @ AtomicFormula(`label`, rargs))) =>
                if largs.zip(rargs).forall { (l, r) =>
                  egraph.idEq(l, r)
                } then Some((raa, j)) else None 
              case _ => None
            }.collectFirst({ case Some(e) => e })
            if matching.isEmpty then None
            else 
              val hypSequent = SC.Sequent(bot.left.updated(i, matching.get._1), bot.right)
              val hypName = s"${sname}_${unfoldCounter}_h"
              val hypStep = LeftHyp(hypName, hypSequent, i, matching.get._2)
              subProof = hypStep :: subProof
              Some((la, i, matching.get._1, matching.get._2))
          case _ => None
        }.collectFirst({ case Some(e) => e })
        val hypOrHypLeft = leftOnly.orElse{
          bot.left.view.zipWithIndex.map { (la, i) => la match
            case la @ AtomicFormula(label, largs) => 
              val matching = bot.right.view.zipWithIndex.map { (ra, j) => ra match
                case ra @ AtomicFormula(`label`, rargs) =>
                  if largs.zip(rargs).forall { (l, r) =>
                    egraph.idEq(l, r)
                  } then Some((ra, j)) else None 
                case _ => None
              }.collectFirst({ case Some(e) => e })
              if matching.isEmpty then None
              else 
                val hypSequent = SC.Sequent(bot.left.updated(i, matching.get._1), bot.right)
                val hypName = s"${sname}_${unfoldCounter}_h"
                val hypStep = SC.Hyp(hypName, hypSequent, i)
                subProof = hypStep :: subProof
                Some((la, i, matching.get._1, matching.get._2))
            case _ => None
          }.collectFirst({ case Some(e) => e })
        }

        hypOrHypLeft match
          case Some((la, i, ra, j)) =>
            val childrenPairs = la.args.zip(ra.args)
            val childrenProofs = childrenPairs.zipWithIndex.map{ case ((l, r), n) =>
              val chpr = egraph.proveInner(l, r, 0, bot.left, bot.right, s"${sname}_${unfoldCounter}_${n}_").get
              subProof = chpr.toList ++ subProof
              (chpr, chpr.head.name, l, r)

            }//all the proofs of the form a1=b1,...,an=bn
            // Congruence Step
            val congName = s"${sname}_${unfoldCounter}_="
            val xs = (1 to la.args.size).map(k => VariableSymbol(Identifier("XX", k))).toList
            val substStep = LVL2.RightSubstMulti(
              congName, 
              SC.Sequent(childrenPairs.map(_ === _) ++ bot.left, bot.right),
              (0 until childrenPairs.size).toList,
              la.label(xs map (_())),
              xs,
              s"${sname}_${unfoldCounter}_h"
            )
            subProof = substStep :: subProof

            // Cuts steps
            var cutCounter = 0
            val cuts = childrenProofs.foldLeft(substStep.bot) {
              case (acc, (_, name, l, r)) => 
                val namen = s"${sname}_${unfoldCounter}_c$cutCounter"
                val newbot = SC.Sequent(acc.left.tail, acc.right)
                val cut = SC.Cut(
                  namen,
                  newbot,
                  0,
                  0,
                  name,
                  if cutCounter == 0 then congName else s"${sname}_${unfoldCounter}_c${cutCounter-1}"
                )
                cutCounter += 1
                subProof = cut :: subProof
                newbot
            } // ** ** d)

            subProof.reverse


          case None => 
            //try refl
            val refl = bot.right.view.zipWithIndex.map { (la, i) => la match
              case la @ AtomicFormula(`equality`, Seq(a, b)) => 
                if egraph.idEq(a, b) then Some(((a, b, i))) else None
              case _ => None
            }.collectFirst({ case Some(e) => e })
            refl match
              case Some((a, b, i)) =>
                val chpr = egraph.proveInner(a, b, 0, bot.left, bot.right, s"${sname}_${unfoldCounter}_").get
                subProof = chpr.toList ++ subProof
                //val weakStep = SC.LeftWeaken(s"${sname}_${unfoldCounter}_w", bot, chpr.head.name)
                subProof.reverse
                

              case None => 
                throw new Exception("Congruence closure failed")
            
            
        
        
      case step => List(step)
    }

    result.flatten

  }
}
