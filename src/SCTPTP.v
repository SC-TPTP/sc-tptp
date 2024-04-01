Require Export Classical.

(* Left Rules *)

Lemma LeftNot : forall P : Prop, (~P -> False) -> P.
Proof. tauto. Qed.

Lemma leftAnd_s : forall P Q : Prop, (P -> (Q -> False)) -> (P /\ Q -> False).
Proof. tauto. Qed.

Definition leftAnd := fun P Q c h => leftAnd_s P Q h c.

Lemma leftOr_s : forall P Q : Prop,
  (P -> False) -> (Q -> False) -> (P \/ Q -> False).
Proof. tauto. Qed.

Definition leftOr := fun P Q c h i => leftOr_s P Q h i c.

Lemma leftImply_s : forall P Q : Prop,
  (~P -> False) -> (Q -> False) -> ((P -> Q) -> False).
Proof. tauto. Qed.

Definition leftImply := fun P Q c h i => leftImply_s P Q h i c.

Lemma leftIff_s : forall P Q : Prop,
  (~P -> ~Q -> False) -> (P -> Q -> False) -> ((P <-> Q) -> False).
Proof. tauto. Qed.

Definition leftIff := fun P Q c h i => leftIff_s P Q h i c.

Lemma leftEx_s : forall (T : Type) (P : T -> Prop),
  (forall z : T, ((P z) -> False)) -> ((exists x : T, (P x)) -> False).
Proof. firstorder. Qed.

Definition leftEx := fun T P c h => leftEx_s T P h c.

Lemma leftAll : forall (T : Type) (P : T -> Prop) (t : T),
  ((P t) -> False) -> ((forall x : T, (P x)) -> False).
Proof. firstorder. Qed.


(* Left Not Rules *)

Lemma leftNotNot : forall P : Prop,
  P -> (~ P -> False).
Proof. tauto. Qed.

Lemma leftNotTrue :
  (~True -> False).
Proof. tauto. Qed.

Lemma leftNotAnd_s : forall P Q : Prop,
  (~P -> False) -> (~Q -> False) -> (~(P /\ Q) -> False).
Proof. tauto. Qed.

Definition leftNotAnd := fun P Q c h i => leftNotAnd_s P Q h i c.

Lemma leftNotImply_s : forall P Q : Prop,
  (P -> ~Q -> False) -> (~(P -> Q) -> False).
Proof. tauto. Qed.

Definition leftNotImply := fun P Q c h => leftNotImply_s P Q h c.

Lemma leftNotOr_s : forall P Q : Prop,
  (~P -> ~Q -> False) -> (~(P \/ Q) -> False).
Proof. tauto. Qed.

Definition leftNotOr := fun P Q c h => leftNotOr_s P Q h c.

Lemma leftNotIff_s : forall P Q : Prop,
  (~P -> Q -> False) -> (P -> ~Q -> False) -> (~(P <-> Q) -> False).
Proof. tauto. Qed.

Definition leftNotIff := fun P Q c h i => leftNotIff_s P Q h i c.

Lemma leftNotEx: forall (T : Type) (P : T -> Prop) (t : T),
  (~(P t) -> False) -> (~(exists x : T, (P x)) -> False).
Proof. firstorder. Qed.

Lemma leftNotAll_s : forall (T : Type) (P : T -> Prop),
  (forall z : T, (~(P z) -> False)) -> (~(forall x : T, (P x)) -> False).
Proof. intros T P Ha Hb. apply Hb. intro. apply NNPP. exact (Ha x). Qed.

Definition leftNotAll := fun T P c h => leftNotAll_s T P h c.


(* Right Rules *)

Lemma RightNot : forall P : Prop, (P -> False) -> ~P.
Proof. tauto. Qed.

Lemma rightAnd : forall P Q : Prop, (P) -> (Q) -> (P /\ Q).
Proof. intros P Q H. split. auto. auto. Qed.
(* intro *)

Lemma rightOr : forall P Q : Prop, ~(~P /\ ~Q) -> (P \/ Q).
Proof. intros P Q H. apply NNPP. intro H1. apply H. split. auto. auto. Qed.

Lemma rightImp : forall P Q : Prop, P -> Q -> (P -> Q).
Proof. intros P Q H0 H1 H2. auto. Qed.
(* intro *)

Lemma rightIff : forall P Q : Prop, (P -> Q) -> (Q -> P) -> (P <-> Q).
Proof. intros P Q H0 H1. split. auto. auto. Qed.

Lemma RightEx: forall (T : Type) (P : T -> Prop) (t : T), (P t) -> (exists x : T, (P x)).
Proof. firstorder. Qed.

Lemma RightAll : forall (T : Type) (P : T -> Prop),
  (forall x : T, (P x)) -> (forall x : T, (P x)).
Proof. firstorder. Qed.

