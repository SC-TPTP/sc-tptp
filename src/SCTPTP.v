Require Export Classical.

Lemma leftNotNot : forall P : Prop,
  P -> (~ P -> False).
Proof. tauto. Qed.

Lemma letNotTrue :
  (~True -> False).
Proof. tauto. Qed.

Lemma leftAnd : forall P Q : Prop,
  (P -> (Q -> False)) -> (P /\ Q -> False).
Proof. tauto. Qed.

Lemma leftOr : forall P Q : Prop,
  (P -> False) -> (Q -> False) -> (P \/ Q -> False).
Proof. tauto. Qed.

Lemma leftImply : forall P Q : Prop,
  (~P -> False) -> (Q -> False) -> ((P -> Q) -> False).
Proof. tauto. Qed.

Lemma leftIff : forall P Q : Prop,
  (~P -> ~Q -> False) -> (P -> Q -> False) -> ((P <-> Q) -> False).
Proof. tauto. Qed.

Lemma leftNotAnd : forall P Q : Prop,
  (~P -> False) -> (~Q -> False) -> (~(P /\ Q) -> False).
Proof. tauto. Qed.

Lemma leftNotOr : forall P Q : Prop,
  (~P -> ~Q -> False) -> (~(P \/ Q) -> False).
Proof. tauto. Qed.

Lemma leftNotImply : forall P Q : Prop,
  (P -> ~Q -> False) -> (~(P -> Q) -> False).
Proof. tauto. Qed.

Lemma leftNotIff : forall P Q : Prop,
  (~P -> Q -> False) -> (P -> ~Q -> False) -> (~(P <-> Q) -> False).
Proof. tauto. Qed.

Lemma leftEx : forall (T : Type) (P : T -> Prop),
  (forall z : T, ((P z) -> False)) -> ((exists x : T, (P x)) -> False).
Proof. firstorder. Qed.

Lemma leftAll : forall (T : Type) (P : T -> Prop) (t : T),
  ((P t) -> False) -> ((forall x : T, (P x)) -> False).
Proof. firstorder. Qed.

Lemma leftNotEx : forall (T : Type) (P : T -> Prop) (t : T),
  (~(P t) -> False) -> (~(exists x : T, (P x)) -> False).
Proof. firstorder. Qed.

Lemma leftNotAll : forall (T : Type) (P : T -> Prop),
  (forall z : T, (~(P z) -> False)) -> (~(forall x : T, (P x)) -> False).
Proof. intros T P Ha Hb. apply Hb. intro. apply NNPP. exact (Ha x). Qed.

Definition rightAnd := fun P Q c h => leftAnd P Q h c.
Definition rightOr := fun P Q c h i => leftOr P Q h i c.
Definition rightImply := fun P Q c h i => leftImply P Q h i c.
Definition rightIff := fun P Q c h i => leftIff P Q h i c.
Definition rightNotAnd := fun P Q c h i => leftNotAnd P Q h i c.
Definition rightNotOr := fun P Q c h => leftNotOr P Q h c.
Definition rightIotImply := fun P Q c h => leftNotImply P Q h c.
Definition rightNotEquiv := fun P Q c h i => leftNotIff P Q h i c.
Definition rightEx := fun T P c h => leftEx T P h c.
Definition rightNotAll := fun T P c h => leftNotAll T P h c.


