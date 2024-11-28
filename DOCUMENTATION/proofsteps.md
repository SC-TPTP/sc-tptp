

# Level 1 Proof Steps
| Rule Name | Premises | Rule       | Parameters   | Comments | 
| :--       | :--      | :--        | :--          |  :--     |
| `rightTrue` | 0      | $$\frac{}{\Gamma \vdash \top, \Delta}$$ | `i:Int`: Index of $\top$ on the right | |
| `leftFalse` | 0      | $$\frac{}{\Gamma, \bot \vdash \Delta}$$ | `i:Int`: Index of $\bot$ on the left | |
| `hyp`    | 0        | $$\frac{}{\Gamma, A \vdash A, \Delta}$$ | `i:Int`: Index of $A$ on the left   <br> `j:Int`: Index of $A$ on the right | |
| `leftHyp` | 0       | $$\frac{}{\Gamma, A, \neg A \vdash \Delta}$$ | `i:Int`: Index of $A$ on the left   <br> `j:Int`: Index of $\neg A$ on the left. | |
| `leftWeaken` | 1    | $$\frac{\Gamma \vdash \Delta}{\Gamma, A \vdash \Delta}$$ | `i:Int`: Index of $A$ on the left | |
| `rightWeaken` | 1   | $$\frac{\Gamma \vdash \Delta}{\Gamma \vdash A, \Delta}$$ | `i:Int`: Index of $A$ on the right | |
| `cut` | 2           | $$\frac{\Gamma \vdash A, \Delta \quad \Sigma, A \vdash \Pi}{\Gamma, \Sigma \vdash \Delta, \Pi}$$ |`i:Int`: Index of the cut formula on the right of the first premise | |
| `leftAnd` | 1       | $$\frac{\Gamma, A, B \vdash \Delta}{\Gamma, A \land B \vdash \Delta}$$ | `i:Int`: Index of $A \land B$ on the left | |
| `leftOr` | 2        | $$\frac{\Gamma, A \vdash \Delta \quad \Sigma, B \vdash \Pi}{\Gamma, \Sigma, A \lor B \vdash \Delta, \Pi}$$ | `i:Int`: Index of $A \lor B$ on the left | |
| `leftImplies` | 2      | $$\frac{\Gamma \vdash A, \Delta \quad \Sigma, B \vdash \Pi}{\Gamma, \Sigma, A \Rightarrow B \vdash \Delta, \Pi}$$ | `i:Int`: Index of $A \Rightarrow B$ on the left | |
| `leftIff` | 1       | $$\frac{\Gamma, A \Rightarrow B, B \Rightarrow A \vdash \Delta}{\Gamma, A \Leftrightarrow B \vdash \Delta}$$ | `i:Int`: Index of $A \Leftrightarrow B$ on the left | |
| `leftNot` | 1       | $$\frac{\Gamma \vdash A, \Delta}{\Gamma, \neg A \vdash \Delta}$$ | `i:Int`: Index of $\neg A$ on the left | |
| `leftEx` | 1        | $$\frac{\Gamma, A \vdash \Delta}{\Gamma, \exists x. A \vdash \Delta}$$ | `i:Int`: Index of $\exists x. A$ on the left <br> `y:Var`: Variable in place of $x$ in the premise | |
| `leftAll` | 1       | $$\frac{\Gamma, A[x := t] \vdash \Delta}{\Gamma, \forall x. A  \vdash \Delta}$$ | `i:Int`: Index of $\forall x. A$ on the left <br> `t:Term`: Term in place of $x$ in the premise | |
| `rightAnd` | 2      | $$\frac{\Gamma \vdash A, B, \Delta}{\Gamma \vdash A \land B, \Delta}$$ | `i:Int`: Index of $A \land B$ on the right | |
| `rightOr` | 1       | $$\frac{\Gamma \vdash A, \Delta}{\Gamma \vdash A \lor B, \Delta}$$ | `i:Int`: Index of $A \lor B$ on the right | |
| `rightImplies` | 1  | $$\frac{\Gamma, A \vdash B, \Delta}{\Gamma \vdash A \Rightarrow B, \Delta}$$ | `i:Int`: Index of $A \Rightarrow B$ on the right | |
| `rightIff` | 1      | $$\frac{\Gamma \vdash A \Rightarrow B, B \Rightarrow A, \Delta}{\Gamma \vdash A \Leftrightarrow B, \Delta}$$ | `i:Int`: Index of $A \Leftrightarrow B$ on the right | |
| `rightNot` | 1      | $$\frac{\Gamma \vdash A, \Delta}{\Gamma \vdash \neg A, \Delta}$$ | `i:Int`: Index of $\neg A$ on the right | |
| `rightEx` | 1       | $$\frac{\Gamma \vdash A, \Delta}{\Gamma \vdash \exists x. A, \Delta}$$ | `i:Int`: Index of $\exists x. A$ on the right <br> `y:Var`: Variable in place of $x$ in the premise | |
| `rightAll` | 1      | $$\frac{\Gamma \vdash A[x := y], \Delta}{\Gamma \vdash \forall x. A, \Delta}$$ | `i:Int`: Index of $\forall x. A$ on the right <br> `y:Var`: Variable in place of $x$ in the premise | |
| `rightRefl` | 0     | $$\frac{}{\Gamma \vdash t = t, \Delta}$$ | `i:Int`: Index of $ t = t $ on the right | |
| `rightSubst` | 1    | $$\frac{\Gamma, t = u \vdash P(t), \Delta}{\Gamma, t = u \vdash P(u), \Delta}$$ | `i:Int`: Index of $t = u$ on the left <br> `P(Z):Var`: Shape of the predicate on the right <br> `Z:Var`: unifiable sub-term in the predicate | |
| `leftSubst` | 1     | $$\frac{\Gamma, t = u, P(t) \vdash \Delta}{\Gamma, t = u,  P(u) \vdash \Delta}$$ | `i:Int`: Index of $t = u$ on the left <br> `P(Z):Term`: Shape of the predicate on the left <br> `Z:Var`: variable indicating where to substitute | |


# Level 2 Proof Steps
Proof steps for which there is an available elimination algorithm implemented in the library.


| Rule Name | Premises | Rule       | Parameters   | Comments  |
| :--       | :--      | :--        | :--          | :--       |
| `rightReflIff` | 0 | $$\frac{}{\Gamma \vdash A \Leftrightarrow A, \Delta}$$ | `i:Int`: Index of $A \Leftrightarrow A$ on the right | |

| `rightSubstMulti` | 1 | $$\frac{\Gamma \vdash P(t_1,...,t_n), \Delta}{\Gamma \vdash P(u_1,...,u_n), \Delta}$$ | `i_1`, ..., `i_n`: Index of formula $t_j = u_j$ on the left <br> `P(Z_1, ..., Z_n):Term`: Shape of the formula on the right <br> `Z_1`, ..., `Z_n`: Var: variables indicating where to substitute | |
| `leftSubstMulti` | 1  | $$\frac{\Gamma, P(t_1,...,t_n) \vdash \Delta}{\Gamma, P(u_1,...,u_n) \vdash \Delta}$$ | `i_1`, ..., `i_n`: Index of formula $t_j = u_j$ on the left <br> `P(Z_1, ..., Z_n):Term`: Shape of the formula on the left <br> `Z_1`, ..., `Z_n`: Var: variables indicating where to substitute | |
| `rightSubstIff` | 1 | $$\frac{\Gamma, \phi \Leftrightarrow \psi \vdash R(\phi), \Delta}{\Gamma, \phi \Leftrightarrow \psi \vdash R(\psi), \Delta}$$ | `i:Int`: Index of $\phi \Leftrightarrow \psi$ on the left <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:FormVar`: unifiable sub-term in the predicate | Requires `Schematic` enabled. Lvl 1 if `PropExt` is enabled|
| `rightSubstEqForallLocal` | 1 | $$\frac{\Gamma, \forall x_1,...,x_n. \phi(x_1,...,x_n) = \psi(x_1,...,x_n) \vdash R(\phi(t_1,...,t_n)), \Delta}{\Gamma, \forall x_1,...,x_n. \phi(x_1,...,x_n) = \psi(x_1,...,x_n) \vdash R(\psi(t_1,...,t_n)), \Delta}$$ | `i:Int`: Index of $\forall x_1,...,x_n. \phi(x_1,...,x_n) = \psi(x_1,...,x_n)$ on the left <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:Form`: unifiable sub-term in the predicate | |
| `rightSubstEqForall` | 2 | $$\frac{\Gamma \vdash R(\phi(t)), \Delta \quad \Sigma \vdash \forall x. \phi(x) = \psi(x), \Pi}{\Gamma, \Sigma \vdash  R(\psi(t)), \Delta, \Pi}$$ | `i:Int`: Index of $\forall x. \phi(x) = \psi(x)$ on the right of the second premisce <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:Var`: unifiable sub-term in the predicate | |
| `rightSubstIffForallLocal` | 1 | $$\frac{\Gamma, \forall x_1,...,x_n. \phi(x_1,...,x_n) \Leftrightarrow \psi(x_1,...,x_n) \vdash R(\phi(t_1,...,t_n)), \Delta}{\Gamma, \forall x_1,...,x_n. \phi(x_1,...,x_n) \Leftrightarrow \psi(x_1,...,x_n) \vdash R(\psi(t_1,...,t_n)), \Delta}$$ | `i:Int`: Index of $\forall x_1,...,x_n. \phi(x_1,...,x_n) \Leftrightarrow \psi(x_1,...,x_n)$ on the left <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:FormVar`: unifiable sub-term in the predicate | |
| `rightSubstIffForall` | 2 | $$\frac{\Gamma \vdash R(\phi(t)), \Delta \quad \Sigma \vdash \forall x. \phi(x) \Leftrightarrow \psi(x), \Pi}{\Gamma, \Sigma \vdash  R(\psi(t)), \Delta, \Pi}$$ | `i:Int`: Index of $\forall x. \phi(x) \Leftrightarrow \psi(x)$ on the right of the second premisce <br> `R(Z):Var`: Shape of the predicate on the right <br> `Z:Var`: unifiable sub-term in the predicate | |
| `NNF` | 1           | $$\frac{\Gamma \vdash \Delta}{\Gamma' \vdash \Delta'}$$ | No parameters <br> The premise and conclusion are equal up to negation normal form | |
| `congruence` | 0    | $$\frac{}{\Gamma, P \vdash Q, \Delta}$$ $$\frac{}{\Gamma, P, \neg Q \vdash \Delta}$$ | No parameter <br> $\Gamma$ contains a set of ground equalities such that P and Q are congruents | |
| `leftNotAnd` | 2    | $$\frac{\Gamma, \neg A \vdash \Delta \quad \Sigma, \neg B \vdash \Pi}{\Gamma, \Sigma, \neg(A \land B) \vdash \Delta, \Pi}$$ | `i:Int`: Index of $\neg(A \land B)$ on the left | |
| `leftNotOr` | 1     | $$\frac{\Gamma, \neg A, \neg B \vdash \Delta}{\Gamma, \neg(A \lor B) \vdash \Delta}$$ | `i:Int`: Index of $\neg(A \lor B)$ on the left | |
| `leftNotImplies` | 1 | $$\frac{\Gamma, A, \neg B \vdash \Delta}{\Gamma, \neg(A \Rightarrow B) \vdash \Delta}$$ | `i:Int`: Index of $\neg(A \Rightarrow B)$ on the left | |
| `leftNotIff` | 2    | $$\frac{\Gamma, \neg (A \Rightarrow B) \vdash \Delta \quad \Sigma, \neg (B \Rightarrow A) \vdash \Pi}{\Gamma, \Sigma, \neg(A \Leftrightarrow B) \vdash \Delta, \Pi}$$ | `i:Int`: Index of $\neg(A \Leftrightarrow B)$ on the left | |
| `leftNotNot` | 1    | $$\frac{\Gamma, A \vdash \Delta}{\Gamma, \neg \neg A \vdash \Delta}$$ | `i:Int`: Index of $\neg \neg A$ on the left | |
| `leftNotEx` | 1     | $$\frac{\Gamma, \neg A[x := t] \vdash \Delta}{\Gamma, \neg\exists x. A  \vdash \Delta}$$ | `i:Int`: Index of $\neg \exists x. A$ on the right <br> `t:Term`: Term in place of $x$ in the premise | |
| `leftNotAll` | 1    | $$\frac{\Gamma, \neg A \vdash \Delta}{\Gamma, \neg \forall x. A \vdash \Delta}$$ | `i:Int`: Index of $\neg \forall x. A$ on the right <br> `y:Var`: Variable in place of $x$ in the premise | |




  RightTrue {name: String, bot: fol::Sequent},
  LeftFalse {name: String, bot: fol::Sequent},
  RightRefl {name: String, bot: fol::Sequent, i: i32},
  RightReflIff {name: String, bot: fol::Sequent, i: i32},
  //format!("fof(f{i}, plain, [{newleft}] --> [{base} = {res}], inference(rightSubstEq, param(0, $fof({base} = {with_hole}), $fot(HOLE)), [f{}])).\n", *i-1) 
  RightSubstEq {name: String, bot: fol::Sequent, premise: String, i: i32, phi: fol::Formula, v: String},
  RightSubstIff {name: String, bot: fol::Sequent, premise: String, i: i32, phi: fol::Formula, v: String},
  RightSubstEqForallLocal {name: String, bot: fol::Sequent, premise: String, i: i32, phi: fol::Formula, v: String},
  RightSubstEqForall {name: String, bot: fol::Sequent, premise1: String, premise2: String, phi: fol::Formula, v: String},
  RightSubstIffForallLocal {name: String, bot: fol::Sequent, premise: String, i: i32, phi: fol::Formula, v: String},
  RightSubstIffForall {name: String, bot: fol::Sequent, premise1: String, premise2: String, phi: fol::Formula, v: String},