

# Level 1 Proof Steps
| Rule Name | Premises | Rule       | Parameters   | Comments | 
| :--       | :--      | :--        | :--          |  :--     |
| `leftFalse` | 0      | $$\frac{}{\Gamma, \bot \vdash \Delta}$$ | `i:Int`: Index of $\bot$ on the left | |
| `rightTrue` | 0      | $$\frac{}{\Gamma \vdash \top, \Delta}$$ | `i:Int`: Index of $\top$ on the right | |
| `hyp`    | 0         | $$\frac{}{\Gamma, A \vdash A, \Delta}$$ | `i:Int`: Index of $A$ on the left | |
| `leftWeaken` | 1     | $$\frac{\Gamma \vdash \Delta}{\Gamma, A \vdash \Delta}$$ | `i:Int`: Index of $A$ on the left | |
| `rightWeaken` | 1    | $$\frac{\Gamma \vdash \Delta}{\Gamma \vdash A, \Delta}$$ | `i:Int`: Index of $A$ on the right | |
| `cut` | 2            | $$\frac{\Gamma \vdash A, \Delta \quad \Sigma, A \vdash \Pi}{\Gamma, \Sigma \vdash \Delta, \Pi}$$ |`i:Int`: Index of the cut formula on the right of the first premise <br> | |
| `leftAnd` | 1        | $$\frac{\Gamma, A, B \vdash \Delta}{\Gamma, A \land B \vdash \Delta}$$ | `i:Int`: Index of $A \land B$ on the left | |
| `leftOr` | 2         | $$\frac{\Gamma, A \vdash \Delta \quad \Sigma, B \vdash \Pi}{\Gamma, \Sigma, A \lor B \vdash \Delta, \Pi}$$ | `i:Int`: Index of $A \lor B$ on the left | |
| `leftImplies` | 2    | $$\frac{\Gamma \vdash A, \Delta \quad \Sigma, B \vdash \Pi}{\Gamma, \Sigma, A \Rightarrow B \vdash \Delta, \Pi}$$ | `i:Int`: Index of $A \Rightarrow B$ on the left | |
| `leftIff` | 1        | $$\frac{\Gamma, A \Rightarrow B, B \Rightarrow A \vdash \Delta}{\Gamma, A \Leftrightarrow B \vdash \Delta}$$ | `i:Int`: Index of $A \Leftrightarrow B$ on the left | |
| `leftNot` | 1        | $$\frac{\Gamma \vdash A, \Delta}{\Gamma, \neg A \vdash \Delta}$$ | `i:Int`: Index of $\neg A$ on the left | |
| `leftExists` | 1     | $$\frac{\Gamma, A[x := y] \vdash \Delta}{\Gamma, \exists x. A \vdash \Delta}$$ | `i:Int`: Index of $\exists x. A$ on the left <br> `y:String`: Name of the variable in place of $x$ in the premise | $y$ should not be free in the resulting sequent. |
| `leftForall` | 1     | $$\frac{\Gamma, A[x := t] \vdash \Delta}{\Gamma, \forall x. A  \vdash \Delta}$$ | `i:Int`: Index of $\forall x. A$ on the left <br> `t:Term`: Term in place of $x$ in the premise | |
| `rightAnd` | 2       | $$\frac{\Gamma \vdash A, \Delta \quad \Sigma \vdash B, \Pi}{\Gamma, \Sigma \vdash A \land B, \Delta, \Pi}$$ | `i:Int`: Index of $A \land B$ on the right | |
| `rightOr` | 1        | $$\frac{\Gamma \vdash A, \Delta}{\Gamma \vdash A \lor B, \Delta}$$ | `i:Int`: Index of $A \lor B$ on the right | |
| `rightImplies` | 1   | $$\frac{\Gamma, A \vdash B, \Delta}{\Gamma \vdash A \Rightarrow B, \Delta}$$ | `i:Int`: Index of $A \Rightarrow B$ on the right | |
| `rightIff` | 2       | $$\frac{\Gamma \vdash A \Rightarrow B, \Delta \quad \Sigma \vdash B \Rightarrow A, \Pi}{\Gamma \vdash A \Leftrightarrow B, \Delta}$$ | `i:Int`: Index of $A \Leftrightarrow B$ on the right | |
| `rightNot` | 1       | $$\frac{\Gamma, A \vdash \Delta}{\Gamma \vdash \neg A, \Delta}$$ | `i:Int`: Index of $\neg A$ on the right | |
| `rightExists` | 1    | $$\frac{\Gamma \vdash A[x := t], \Delta}{\Gamma \vdash \exists x. A, \Delta}$$ | `i:Int`: Index of $\exists x. A$ on the right <br> `t:Term`: Term in place of $x$ in the premise | |
| `rightForall` | 1    | $$\frac{\Gamma \vdash A[x := y], \Delta}{\Gamma \vdash \forall x. A, \Delta}$$ | `i:Int`: Index of $\forall x. A$ on the right <br> `y:String`: Name of the variable in place of $x$ in the premise. | $y$ should not be free in the resulting sequent. |
| `rightRefl` | 0      | $$\frac{}{\Gamma \vdash t = t, \Delta}$$ | `i:Int`: Index of $ t = t $ on the right | |
| `rightSubst` | 1     | $$\frac{\Gamma \vdash P(t), \Delta}{\Gamma, t = u \vdash P(u), \Delta}$$ | `i:Int`: Index of $t = u$ on the left <br> `backward:Int`: If non-zero, the substitution is done backward <br> `P(Z):Term`: Shape of the predicate on the right <br> `Z:String`: Name of the variable indication where the substitution takes place. | |
| `leftSubst` | 1      | $$\frac{\Gamma, P(t) \vdash \Delta}{\Gamma, t = u,  P(u) \vdash \Delta}$$ | `i:Int`: Index of $t = u$ on the left <br> `backward:Int`: If non-zero, the substitution is done backward <br> `P(Z):Term`: Shape of the predicate on the left <br> `Z:String`: Name of the variable indication where the substitution takes place. | |
| `rightSubstIff` | 1  | $$\frac{\Gamma\vdash R(\phi), \Delta}{\Gamma, \phi \Leftrightarrow \psi \vdash R(\psi), \Delta}$$ | `i:Int`: Index of $\phi \Leftrightarrow \psi$ on the left <br> `backward:Int`: If non-zero, the substitution is done backward <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:String`: Name of the variable indication where in $P$ the substitution takes place. | |
| `leftSubstIff` | 1   | $$\frac{\Gamma, R(\phi),  \vdash \Delta}{\Gamma, \phi \Leftrightarrow \psi, R(\psi) \vdash \Delta}$$ | `i:Int`: Index of $\phi \Leftrightarrow \psi$ on the left <br> `backward:Int`: If non-zero, the substitution is done backward <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:String`: Name of the schematic variable indication where in $P$ the substitution takes place. | |
| `instFun` | 1        | $$\frac{\Gamma[F_X] \vdash \Delta[F_X]}{\Gamma[F_X := t_X] \vdash \Delta[F_X := t_X]}$$ | `'F': String`: Schematic function to substitute.  <br> `t:Term`: Term, possibly containing $X_1, ..., X_n$, to instantiate $F$ with.  <br> `Xs: Seq[String]`: Name of the variables parametrizing $t$. The length gives the arity of $F$. | If $n$ is 0, $F$ is a variable. Otherwise it is a function of arity $n$, starting with a capital letter.|
| `instPred` | 1       | $$\frac{\Gamma[P_X] \vdash \Delta[P_X]}{\Gamma[P_X := \phi_X] \vdash \Delta[P_X := \phi_X]}$$ | `'P': String`: Schematic predicate to substitute. `n`can be $0$. <br> `\phi: Formula`: Formula, possibly containing $X_1, ..., X_n$, to instantiate $F$ with. <br> `Xs: Seq[String]`: Name of the variables parametrizing $\phi$. The length gives the arity of $P$. | Requires `Schematic` enabled. If $n$ is 0, $F$ is a formula variable. Otherwise it is a predicate of arity $n$, starting with a capital letter.|
| `rightEpsilon` | 1   | $$\frac{\Gamma \vdash A[x := t], \Delta}{\Gamma \vdash A[x := \epsilon x. A], \Delta}$$ | `A:Formula`: Formula defining the epsilon-term <br> `X:String`: Name of the variable being substituted in $A$.  <br> `t:Term`: Term in place of $x$ in the premise  | |
| `leftEpsilon` | 1     | $$\frac{\Gamma, A[x := y] \vdash \Delta}{\Gamma, A[x := \epsilon x. A] \vdash \Delta}$$ | `i:Int`: Index of $A[x := y]$ on the left of the premise<br> `y:String`: Name of the variable in place of $x$ in the premise | $y$ should not be free in the resulting sequent. |

# Level 2 Proof Steps
Proof steps for which there is an available elimination algorithm implemented in the library.


| Rule Name | Premises | Rule       | Parameters   | Comments  |
| :--       | :--      | :--        | :--          | :--       |
| `congruence` | 0    | $$\frac{}{\Gamma, P \vdash Q, \Delta}$$ $$\frac{}{\Gamma, P, \neg Q \vdash \Delta}$$ | No parameter <br> $\Gamma$ contains a set of ground equalities such that P and Q are congruents | |
| `leftHyp` | 0        | $$\frac{}{\Gamma, A, \neg A \vdash \Delta}$$ | `i:Int`: Index of $A$ on the left   <br> `j:Int`: Index of $\neg A$ on the left. | |
| `leftNotAnd` | 2    | $$\frac{\Gamma, \neg A \vdash \Delta \quad \Sigma, \neg B \vdash \Pi}{\Gamma, \Sigma, \neg(A \land B) \vdash \Delta, \Pi}$$ | `i:Int`: Index of $\neg(A \land B)$ on the left | |
| `leftNotOr` | 1     | $$\frac{\Gamma, \neg A, \neg B \vdash \Delta}{\Gamma, \neg(A \lor B) \vdash \Delta}$$ | `i:Int`: Index of $\neg(A \lor B)$ on the left | |
| `leftNotImplies` | 1 | $$\frac{\Gamma, A, \neg B \vdash \Delta}{\Gamma, \neg(A \Rightarrow B) \vdash \Delta}$$ | `i:Int`: Index of $\neg(A \Rightarrow B)$ on the left | |
| `leftNotIff` | 2    | $$\frac{\Gamma, \neg (A \Rightarrow B) \vdash \Delta \quad \Sigma, \neg (B \Rightarrow A) \vdash \Pi}{\Gamma, \Sigma, \neg(A \Leftrightarrow B) \vdash \Delta, \Pi}$$ | `i:Int`: Index of $\neg(A \Leftrightarrow B)$ on the left | |
| `leftNotNot` | 1    | $$\frac{\Gamma, A \vdash \Delta}{\Gamma, \neg \neg A \vdash \Delta}$$ | `i:Int`: Index of $\neg \neg A$ on the left | |
| `leftNotEx` | 1     | $$\frac{\Gamma, \neg A[x := t] \vdash \Delta}{\Gamma, \neg\exists x. A  \vdash \Delta}$$ | `i:Int`: Index of $\neg \exists x. A$ on the right <br> `t:Term`: Term in place of $x$ in the premise | |
| `leftNotAll` | 1    | $$\frac{\Gamma, \neg A \vdash \Delta}{\Gamma, \neg \forall x. A \vdash \Delta}$$ | `i:Int`: Index of $\neg \forall x. A$ on the right <br> `y:String`: Variable in place of $x$ in the premise | |


# Level 3 Proof Steps
Proof steps for which there is no elimination procedure, but which are precisely defined and checkable, and usually implemented in some way or another in proof systems.

| Rule Name | Premises | Rule       | Parameters   | Comments  |
| :--       | :--      | :--        | :--          | :--       |
| `rightReflIff` | 0 | $$\frac{}{\Gamma \vdash A \Leftrightarrow A, \Delta}$$ | `i:Int`: Index of $A \Leftrightarrow A$ on the right | |
| `rightSubstMulti` | 1 | $$\frac{\Gamma \vdash P(t_1,...,t_n), \Delta}{\Gamma \vdash P(u_1,...,u_n), \Delta}$$ | `i_1`, ..., `i_n`: Index of formulas $t_j = u_j$ on the left <br> `P(Z_1, ..., Z_n):Term`: Shape of the formula on the right <br> `Z_1`, ..., `Z_n`: Var: variables indicating where to substitute | |
| `leftSubstMulti` | 1  | $$\frac{\Gamma, P(t_1,...,t_n) \vdash \Delta}{\Gamma, P(u_1,...,u_n) \vdash \Delta}$$ | `i_1`, ..., `i_n`: Index of formula $t_j = u_j$ on the left <br> `P(Z_1, ..., Z_n):Term`: Shape of the formula on the left <br> `Z_1`, ..., `Z_n`: Var: variables indicating where to substitute | |
| `rightSubstEqForallLocal` | 1 | $$\frac{\Gamma, \forall x_1,...,x_n. \phi(x_1,...,x_n) = \psi(x_1,...,x_n) \vdash R(\phi(t_1,...,t_n)), \Delta}{\Gamma, \forall x_1,...,x_n. \phi(x_1,...,x_n) = \psi(x_1,...,x_n) \vdash R(\psi(t_1,...,t_n)), \Delta}$$ | `i:Int`: Index of $\forall x_1,...,x_n. \phi(x_1,...,x_n) = \psi(x_1,...,x_n)$ on the left <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:Form`: unifiable sub-term in the predicate | |
| `rightSubstEqForall` | 2 | $$\frac{\Gamma \vdash R(\phi(t)), \Delta \quad \Sigma \vdash \forall x. \phi(x) = \psi(x), \Pi}{\Gamma, \Sigma \vdash  R(\psi(t)), \Delta, \Pi}$$ | `i:Int`: Index of $\forall x. \phi(x) = \psi(x)$ on the right of the second premisce <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:Var`: unifiable sub-term in the predicate | |
| `rightSubstIffForallLocal` | 1 | $$\frac{\Gamma, \forall x_1,...,x_n. \phi(x_1,...,x_n) \Leftrightarrow \psi(x_1,...,x_n) \vdash R(\phi(t_1,...,t_n)), \Delta}{\Gamma, \forall x_1,...,x_n. \phi(x_1,...,x_n) \Leftrightarrow \psi(x_1,...,x_n) \vdash R(\psi(t_1,...,t_n)), \Delta}$$ | `i:Int`: Index of $\forall x_1,...,x_n. \phi(x_1,...,x_n) \Leftrightarrow \psi(x_1,...,x_n)$ on the left <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:FormVar`: unifiable sub-term in the predicate | |
| `rightSubstIffForall` | 2 | $$\frac{\Gamma \vdash R(\phi(t)), \Delta \quad \Sigma \vdash \forall x. \phi(x) \Leftrightarrow \psi(x), \Pi}{\Gamma, \Sigma \vdash  R(\psi(t)), \Delta, \Pi}$$ | `i:Int`: Index of $\forall x. \phi(x) \Leftrightarrow \psi(x)$ on the right of the second premisce <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:Var`: unifiable sub-term in the predicate | |
| `rightNnf` | 1           | $$\frac{\Gamma \vdash \phi, \Delta}{\Gamma \vdash \phi', \Delta}$$ | `i:Int`: Index of $\phi'$ on the right of the premise <br> `j:Int`: Index of $\phi'$ on the right of the conclusion | $\phi$ and $\phi'$ have the same negation normal form |
| `rightPrenex` | 1        | $$\frac{\Gamma \vdash \phi, \Delta}{\Gamma \vdash \phi', \Delta}$$ | `i:Int`: Index of $\phi'$ on the right of the premise <br> `j:Int`: Index of $\phi'$ on the right of the conclusion| $\phi$ and $\phi'$ have the same prenex normal form |
| `clausify` | 0      | $$\frac{}{\Gamma, a \iff b \circ c \vdash \Delta}$$ | `i:Int`: Index of $a \iff b \circ c$ on the left | $\Gamma$ is a clause resulting from the inequality $a \iff b \circ c$|
| `elimIffRefl` | 1      | $$\frac{\Gamma, \forall x_1, ..., x_n. \phi \iff \phi \vdash \Delta}{\Gamma \vdash\Delta}$$ | `i:Int`: Index of $a \iff b \circ c$ on the left <br> `j:Int`: Index of $\phi$ on the right | |
| `instMult` | 1      | $$\frac{\Gamma[F_1, ..., F_n],  \vdash \Delta[F_1, ..., F_n]}{\Gamma[G_1, ..., G_n] \vdash\Delta[G_1, ..., G_n]}$$ | Sequence of triplets of the form: <br> `'F': String, t: Term\|Formula, Xs: Seq[String]`. Each triplet has the same construction as arguments of `instFun`and `instPred`, but the substitution is carried simultaneously. | Simultaneous substitution of function and predicate schemas, including variables and formula variables. |