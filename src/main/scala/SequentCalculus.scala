
import FOL.*

object SequentCalculus {
  
  case class Sequent(left: Seq[Formula], right: Seq[Formula])

  sealed trait SCProofStep {
      val bot: Sequent
      val premises: Seq[Int]
    }


  /**
    * -----------------
    *   Γ, A |- A, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A on the left
    * @param j Index of A on the right
    */
  case class Hyp(bot:Sequent, i:Int, j:Int) extends SCProofStep {
    val premises = Seq()
  }


  /**
    * -------------------
    *   Γ, A, ¬A, |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A on the left
    * @param j Index of ¬A on the left
    */
  case class LeftHyp(bot:Sequent, i:Int, j:Int) extends SCProofStep {
    val premises = Seq()
  }


  /**
    *    Γ |- Δ
    * -------------
    *   Γ, A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A on the left
    */
  case class LeftWeakening(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- Δ
    * -------------
    *   Γ |- A, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A on the right
    */
  case class RightWeakening(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- A, Δ    Γ, A |- Δ
    * -------------------------
    *          Γ |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of cut on the right of first premise
    * @param j Index of cut on the left of second premise
    */
  case class Cut(bot:Sequent, i:Int, j:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, A, B |- Δ
    * ----------------
    *   Γ, A ∧ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ∧ B on the left
    */  
  case class LeftAnd(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, A |- Δ    Γ, B |- Δ
    * -------------------------
    *          Γ, A ∨ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ∨ B on the left
    */
  case class LeftOr(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ |- B, Δ    Γ, A |- Δ
    * -----------------------------------
    *         Γ, A ⇒ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇒ B on the left
    */
  case class LeftImp1(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, A |-  Δ     Γ ¬B |-  Δ
    * -------------------------------
    *          Γ, A ⇒ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇒ B on the left
    */
  case class LeftImp2(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, A ⇒ B, B ⇒ A |- Δ 
    * -----------------------------------
    *   Γ, A ⇔ B |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇔ B on the left
    */
  case class LeftIff(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- A, Δ
    * ----------------
    *   Γ, ¬A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬A on the left
    */
  case class LeftNot(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, A[x:=y] |- Δ
    * ---------------- Where y is not free in Γ and Δ
    *   Γ, ∃x. A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ∃x. A on the left
    * @param y Variable in place of x in the premise
    */
  case class LeftEx(bot:Sequent, i:Int, y:VariableLabel, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, A[x:=t] |- Δ
    * ----------------
    *   Γ, ∀x. A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ∀x. A on the left
    * @param t Term in in place of x in the premise
    */
  case class LeftAll(bot:Sequent, i:Int, t:Term, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- A, Δ      Γ |- A, Δ
    * ------------------------------
    *        Γ |- A ∧ B, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ∧ B on the right
    */
  case class RightAnd(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ |- A, B Δ  
    * ----------------
    *  Γ |- A ∨ B, Δ
    * @param bot Resulting formula
    * @param i Index of A ∨ B on the right
    */
  case class RightOr(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, A |- B, Δ
    * ----------------
    *   Γ |- A ⇒ B, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇒ B on the right
    */
  case class RightImp(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- A ⇒ B, Δ     Γ |- B ⇒ A, Δ
    * ------------------------------------
    *              Γ |- A ⇔ B, Δ
    *
    * @param bot Resulting formula
    * @param i Index of A ⇔ B on the right
    */
  case class RightIff(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, A |- Δ
    * ----------------
    *   Γ |- ¬A, Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬A on the right
    */
  case class RightNot(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- A[x:=t], Δ
    * -------------------
    *     Γ |- ∃x. A, Δ
    *
    * @param bot Resulting formula
    * @param i Index of ∃x. A on the right
    * @param t Term in place of x in the premise
    */
  case class RightEx(bot:Sequent, i:Int, t:Term, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ |- A[x:=y], Δ
    * -------------------
    *     Γ |- ∀x. A, Δ
    *
    * @param bot Resulting formula
    * @param i Index of ∀x. A on the right
    * @param y Variable in place of x in the premise
    */
  case class RightAll(bot:Sequent, i:Int, y:VariableLabel, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, ¬A |- Δ     Γ, ¬B |- Δ
    * -------------------------------
    *     Γ, ¬(A ∧ B), Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ∧ B) on the left
    */
  case class LeftNotAnd(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, ¬A, ¬B |- Δ
    * ----------------
    *   Γ, ¬(A ∨ B) |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ∨ B) on the left
    */
  case class LeftNotOr(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, A, ¬B |- Δ
    * ----------------
    *   Γ, ¬(A ⇒ B) |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ⇒ B) on the left
    */
  case class LeftNotImp(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, ¬(A ⇒ B)  |- Δ     Γ, ¬(B ⇒ A)  |- Δ
    * ---------------------------------------------
    *               Γ, ¬(A ⇔ B) |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬(A ⇔ B) on the left
    */
  case class LeftNotIff(bot:Sequent, i:Int, p1:Int, p2:Int) extends SCProofStep {
    val premises = Seq(p1, p2)
  }


  /**
    *    Γ, A |- Δ
    * ----------------
    *   Γ, ¬¬A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬¬A on the left
    */
  case class LeftNotNot(bot:Sequent, i:Int, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, ¬A[x:=t] |- Δ
    * ----------------
    *   Γ, ¬∃x. A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬∃x. A on the left
    * @param t Term in place of x in the premise
    */
  case class LeftNotEx(bot:Sequent, i:Int, t:Term, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *    Γ, ¬A[x:=y] |- Δ
    * ----------------
    *   Γ, ¬∀x. A |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of ¬∀x. A on the left
    * @param y Variable in place of x in the premise
    */
  case class LeftNotAll(bot:Sequent, i:Int, y:VariableLabel, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    * ----------------
    *   Γ |- t = t, Δ
    *
    * @param bot Resulting formula
    * @param i Index of t = t on the right
    */
  case class RightRefl(bot:Sequent, i:Int) extends SCProofStep {
    val premises = Seq()
  }


  /**
    *   Γ |- P[x:=t], Δ
    * ----------------
    *   Γ, t = u |- P[x:=u], Δ
    *
    * @param bot Resulting formula
    * @param i Index of t = u on the left
    * @param j Index of P(t) on the right
    * @param P Shape of the formula in which the substitution occurs
    * @param x Variable indicating where in P the substitution occurs
    */
  case class RightSubst(bot:Sequent, i:Int, j:Int, P:Formula, x:VariableLabel, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }


  /**
    *   Γ, P[x:=t] |- Δ
    * ----------------
    *   Γ, t = u, P[x:=u] |- Δ
    *
    * @param bot Resulting formula
    * @param i Index of t = u on the left
    * @param j Index of P(t) on the left
    * @param P Shape of the formula in which the substitution occurs
    * @param x Variable indicating where in P the substitution occurs
    */
  case class LeftSubst(bot:Sequent, i:Int, j:Int, P:Formula, x:VariableLabel, p1:Int) extends SCProofStep {
    val premises = Seq(p1)
  }






}


/*
\begin{table}
    \centering

    \begin{tabular}{|c|c|r@{\hskip3pt}c@{\hskip3pt}l@{\hskip3pt}|l|}
         \hline
        Rule name & Premises & \multicolumn{3}{c}{Shape of conclusion} & Parameters \\ \hline    
        \texttt{hyp}       &  0 & $ \Gamma, A $ & $ \vdash $&$ A, \Delta $        & \makecell[l]{\texttt{i:Int}: Index of $A$ on the left  \\ \texttt{j:Int}: Index of $A$ on the right} \\ \hline
        \texttt{leftHyp}  & 0 & $ \Gamma, A, \neg A $&$ \vdash $&$ \Delta $    & \makecell[l]{\texttt{i:Int}: Index of $A$ on the left  \\ \texttt{j:Int}: Index of $\neg A$ on the left.} \\ \hline
        \texttt{leftWeaken}  & 1 & $ \Gamma, A $&$ \vdash $&$ \Delta $            & \texttt{i:Int}: Index of $A$ on the left  \\ \hline 
        \texttt{rightWeaken} & 1 & $ \Gamma $&$ \vdash $&$ A, \Delta $            & \texttt{i:Int}: Index of $A$ on the right \\ \hline 
        \texttt{cut}         & 2 & $ \Gamma $&$ \vdash $&$ \Delta $               & \makecell[l]{\texttt{i:Int}: Index of cut on the right of first premise \\ \texttt{j:Int}: Index of cut on the left of second premise} \\ \hline \hline 
        \texttt{leftAnd}     & 1 & $ \Gamma, A \land B $&$ \vdash $&$ \Delta $    & \texttt{i:Int}: Index of $A \land B$ on the left    \\ \hline
        \texttt{leftOr}      & 2 & $ \Gamma, A \lor B $&$ \vdash $&$ \Delta $     & \texttt{i:Int}: Index of $A \lor B$ on the left     \\ \hline
        \texttt{leftImp1}     & 2 & $ \Gamma, A \implies B $&$ \vdash $&$ \Delta $ & \texttt{i:Int}: Index of $A \implies B$ on the left \\ \hline
        \texttt{leftImp2}     & 2 & $ \Gamma, A \implies B $&$ \vdash $&$ \Delta $ & \texttt{i:Int}: Index of $A \implies B$ on the left \\ \hline
        \texttt{leftIff}     & 1 & $ \Gamma, A \iff B $&$ \vdash $&$ \Delta $     & \texttt{i:Int}: Index of $A \iff B$ on the left     \\ \hline
        \texttt{leftNot}     & 1 & $ \Gamma, \neg A $&$ \vdash $&$ \Delta $       & \texttt{i:Int}: Index of $\neg A$ on the left       \\ \hline
        \texttt{leftEx}      & 1 & $ \Gamma, \exists x. A $&$ \vdash $&$ \Delta $ & \makecell[l]{\texttt{i:Int}: Index of $\exists x. A$ on the left      \\ \texttt{y:Var}: Variable in place of $x$ in the premise} \\ \hline
        \texttt{leftAll}     & 1 & $ \Gamma, \forall x. A $&$ \vdash $&$ \Delta $ & \makecell[l]{\texttt{i:Int}: Index of $\forall x. A$ on the left      \\ \texttt{t:Term}: Term in in place of $x$ in the premise} \\ \hline \hline
        \texttt{rightAnd}    & 2 & $ \Gamma $&$ \vdash $&$ A \land B, \Delta $    & \texttt{i:Int}: Index of $A \land B$ on the right    \\ \hline
        \texttt{rightOr}     & 1 & $ \Gamma $&$ \vdash $&$ A \lor B, \Delta $     & \texttt{i:Int}: Index of $A \lor B$ on the right     \\ \hline
        \texttt{rightImp}    & 1 & $ \Gamma $&$ \vdash $&$ A \implies B, \Delta $ & \texttt{i:Int}: Index of $A \implies B$ on the right \\ \hline
        \texttt{rightIff}    & 2 & $ \Gamma $&$ \vdash $&$ A \iff B, \Delta $     & \texttt{i:Int}: Index of $A \iff B$ on the right     \\ \hline
        \texttt{rightNot}    & 1 & $ \Gamma $&$ \vdash $&$ \neg A, \Delta $       & \texttt{i:Int}: Index of $\neg A$ on the right       \\ \hline
        \texttt{rightEx}     & 1 & $ \Gamma $&$ \vdash $&$ \exists x. A, \Delta $ & \makecell[l]{\texttt{i:Int}: Index of $\exists x. A$ on the right     \\ \texttt{t:Term}: Term in place of $x$ in the premise}    \\ \hline
        \texttt{rightAll}    & 1 & $ \Gamma $&$ \vdash $&$ \forall x. A, \Delta $ & \makecell[l]{\texttt{i:Int}: Index of $\forall x. A$ on the right     \\ \texttt{y:Var}: Variable in place of $x$ in the premise} \\ \hline \hline
        \texttt{leftNotAnd}  & 2 & $ \Gamma, \neg(A \land B) $&$ \vdash $&$ \Delta $    & \texttt{i:Int}: Index of $\neg(A \land B)$ on the left     \\ \hline
        \texttt{leftNotOr}   & 1 & $ \Gamma, \neg(A \lor B) $&$ \vdash $&$ \Delta $     & \texttt{i:Int}: Index of $\neg(A \lor B)$ on the left      \\ \hline
        \texttt{leftNotImp}  & 1 & $ \Gamma, \neg(A \implies B) $&$ \vdash $&$ \Delta $ & \texttt{i:Int}: Index of $\neg(A \implies B)$ on the left  \\ \hline
        \texttt{leftNotIff}  & 2 & $ \Gamma, \neg(A \iff B) $&$ \vdash $&$ \Delta $     & \texttt{i:Int}: Index of $\neg(A \iff B)$ on the left      \\ \hline
        \texttt{leftNotNot}  & 1 & $ \Gamma, \neg \neg A $&$ \vdash $&$ \Delta $        & \texttt{i:Int}: Index of $\neg \neg A$ on the left         \\ \hline
        \texttt{leftNotEx}   & 1 & $ \Gamma, \neg \exists x. A $&$ \vdash $&$ \Delta $  & \makecell[l]{\texttt{i:Int}: Index of $\neg \exists x. A$ on the right     \\  \texttt{t:Term}: Term in place of $x$ in the premise}   \\ \hline
        \texttt{leftNotAll}  & 1 & $ \Gamma, \neg \exists x. A $&$ \vdash $&$ \Delta $  & \texttt{i:Int}: Index of $\neg \forall x. A$ on the right     \\
                             &   &              &          &                            & \texttt{y:Var}: Variable in place of $x$ in the premise \\ \hline \hline
        \texttt{rightRefl}  & 1 & $ \Gamma $&$ \vdash $&$ t = t, \Delta$  & \texttt{i:Int}: Index of $ t = t $ on the right. \\ \hline
        \texttt{rightSubst}  & 2 & $ \Gamma, t = u $&$ \vdash $&$ P(t), \Delta$  & \makecell[l]{\texttt{i:Int}: Index of $t = u$ on the left  \\ \texttt{j:Int}: Index of $P(t)$ on the right \\ \texttt{P(Z):Var}: Shape of the predicate on the right \\ \texttt{Z:Var}: unifiable sub-term in the predicate} \\ \hline                   
        \texttt{leftSubst}  & 2 & $ \Gamma, t = u,  P(t) $&$ \vdash $&$\Delta$  & \makecell[l]{\texttt{i:Int}: Index of $t = u$ on the left  \\ \texttt{j:Int}: Index of $P(t)$ on the left  \\ \texttt{P(Z):Var}: Shape of the predicate on the left \\ 
        \texttt{Z:Var}: unifiable subterm in the predicate } \\\hline    

    \end{tabular}
    \caption{Level 1 rules of SC-TPTP, for one-sided and two-sided sequent calculus.}
    \label{tab:SCTPTP_rules_lvl_1}
\end{table}
*/