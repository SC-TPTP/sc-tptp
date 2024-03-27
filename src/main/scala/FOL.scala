object FOL {

  /**
   * A labelled node for tree-like structures.
   */
  trait Label {
    val arity: Int
    val id: Identifier
  }

  sealed case class Identifier(val name: String, val no: Int) {
    require(no >= 0, "Variable index must be positive")
    require(Identifier.isValidIdentifier(name), "Variable name " + name + "is not valid.")
    override def toString: String = if (no == 0) name else name + Identifier.counterSeparator + no
  }
  object Identifier {
    def unapply(i: Identifier): Option[(String, Int)] = Some((i.name, i.no))
    def apply(name: String): Identifier = new Identifier(name, 0)
    def apply(name: String, no: Int): Identifier = new Identifier(name, no)

    val counterSeparator: Char = '_'
    def isValidIdentifier(s: String): Boolean = s.nonEmpty // && s.forall(_.isLetterOrDigit) && s.head.isUpper
  }

  def freshId(taken: Iterable[Identifier], base: Identifier): Identifier = {
    new Identifier(
      base.name,
      (taken.collect({ case Identifier(base.name, no) =>
        no
      }) ++ Iterable(base.no)).max + 1
    )
  }

  abstract class TermLabel extends Label {
    require(arity >= 0)
  }

  case class FunctionLabel(id: Identifier, arity: Int) extends TermLabel {
    def apply(args: Seq[Term]): Term = Term(this, args)
    override def toString(): String = id.name
  }

  case class VariableLabel(id: Identifier) extends TermLabel {
    val name: Identifier = id
    val arity = 0

    override def toString(): String = name.name
  }

  sealed case class Term(label: TermLabel, args: Seq[Term]) {
    require(label.arity == args.size)

    def freeVariables: Set[VariableLabel] = label match {
      case l: VariableLabel => Set(l)
      case _ => args.foldLeft(Set.empty[VariableLabel])((prev, next) => prev union next.freeVariables)
    }

    override def toString(): String = label match {
      case vl: VariableLabel => vl.name.toString()
      case fl: FunctionLabel =>
        if (args.length == 0) then fl.id.name
        else s"${fl.toString()}(${args.mkString(",")})"
    }
  }

  object Variable {

    def apply(label: VariableLabel): Term = Term(label, Seq())
    def apply(name: String): Term = Term(VariableLabel(Identifier(name)), Seq())
    def unapply(t: Term): Option[VariableLabel] = t.label match {
      case l: VariableLabel => Some(l)
      case _ => None
    }
  }

  //////////////////////////
  /////// Formulas /////////
  //////////////////////////




  sealed case class AtomicLabel(id: Identifier, arity: Int) extends Label{
    def apply(args: Seq[Term]): AtomicFormula = AtomicFormula(this, args)
     override def toString(): String = id.name
  }

  val equality: AtomicLabel = AtomicLabel(Identifier("="), 2)
  val top: AtomicLabel = AtomicLabel(Identifier("$true"), 0)
  val bot: AtomicLabel = AtomicLabel(Identifier("$false"), 0)


  sealed abstract class ConnectorLabel(val id: Identifier, val arity: Int) extends Label{
    def apply(args: Seq[Formula]): ConnectorFormula = ConnectorFormula(this, args)
  }

  case object Neg extends ConnectorLabel(Identifier("~"), 1)

  case object Implies extends ConnectorLabel(Identifier("=>"), 2)

  case object Iff extends ConnectorLabel(Identifier("<=>"), 2)

  case object And extends ConnectorLabel(Identifier("&"), -1)

  case object Or extends ConnectorLabel(Identifier("|"), -1)

  sealed abstract class BinderLabel(val id: Identifier) extends Label {
    val arity = 1
    def apply(bound: VariableLabel, inner: Formula): BinderFormula = BinderFormula(this, bound, inner)
  }

  case object Forall extends BinderLabel(Identifier("!"))

  case object Exists extends BinderLabel(Identifier("?"))

  sealed trait Formula {
    def freeVariables: Set[VariableLabel]
  }

  sealed case class AtomicFormula(label: AtomicLabel, args: Seq[Term]) extends Formula {
    require(label.arity == args.size)

    def freeVariables: Set[VariableLabel] =
      args.foldLeft(Set.empty[VariableLabel])((prev, next) => prev union next.freeVariables)

    override def toString(): String = label match {
      case `equality` => s"(${args(0).toString()} ${equality.id.name} ${args(1).toString()})"
      case `top` => top.id.name
      case `bot` => bot.id.name
      case al: AtomicLabel => 
        if (al.arity == 0) then al.id.name
        else s"${al.toString()}(${args.mkString(",")})"
    }

  }

  /**
   * The formula counterpart of [[ConnectorLabel]].
   */
  sealed case class ConnectorFormula(label: ConnectorLabel, args: Seq[Formula]) extends Formula {
    require(label.arity == args.size || label.arity == -1)
    require(label.arity != 0)

    override def freeVariables: Set[VariableLabel] =
      args.foldLeft(Set.empty[VariableLabel])((prev, next) => prev union next.freeVariables)

    override def toString(): String = label match {
      case Neg => s"${Neg.id.name}${args(0).toString()}"
      case Implies => s"(${args(0).toString()} ${Implies.id.name} ${args(1).toString()})"
      case Iff => s"(${args(0).toString()} ${Iff.id.name} ${args(1).toString()})"
      case And => s"(${args.mkString(s" ${and.id.name} ")})"
      case Or => s"(${args.mkString(s" ${or.id.name} ")})"
    }
  }

  /**
   * The formula counterpart of [[BinderLabel]].
   */
  sealed case class BinderFormula(label: BinderLabel, bound: VariableLabel, inner: Formula) extends Formula {
    override def freeVariables: Set[VariableLabel] = inner.freeVariables - bound

    override def toString(): String = label match {
      case Forall => s"${Forall.id.name} [${bound.toString()}]: ${inner.toString()}" 
      case Exists => s"${Exists.id.name} [${bound.toString()}]: ${inner.toString()}" 
    }
  }

  def substituteVariablesInTerm(t: Term, m: Map[VariableLabel, Term]): Term = t match {
    case Term(label: VariableLabel, _) => m.getOrElse(label, t)
    case Term(label, args) => Term(label, args.map(substituteVariablesInTerm(_, m)))
  }

  def substituteVariablesInFormula(phi: Formula, m: Map[VariableLabel, Term], takenIds: Seq[Identifier] = Seq[Identifier]()): Formula = phi match {
    case AtomicFormula(label, args) => AtomicFormula(label, args.map(substituteVariablesInTerm(_, m)))
    case ConnectorFormula(label, args) => ConnectorFormula(label, args.map(substituteVariablesInFormula(_, m)))
    case BinderFormula(label, bound, inner) =>
      val newSubst = m - bound
      val newTaken = takenIds :+ bound.id
      val fv = m.values.flatMap(_.freeVariables).toSet
      if (fv.contains(bound)) {
        val newBoundVariable = VariableLabel(freshId(fv.map(_.name) ++ m.keys.map(_.id) ++ newTaken, bound.name))
        val newInner = substituteVariablesInFormula(inner, Map(bound -> Variable(newBoundVariable)), newTaken)
        BinderFormula(label, newBoundVariable, substituteVariablesInFormula(newInner, newSubst, newTaken))
      } else BinderFormula(label, bound, substituteVariablesInFormula(inner, newSubst, newTaken))
  }




  val === = equality
  extension (t: Term) {
    infix def ===(u: Term): Formula = equality(List(t, u))
  }

  val ⊤ : top.type = top

  val ⊥ : bot.type = bot

  val neg: Neg.type = Neg
  val ¬ : Neg.type = Neg
  val ! : Neg.type = Neg

  val and: And.type = And
  val /\ : And.type = And
  val ∧ : And.type = And

  val or: Or.type = Or
  val \/ : Or.type = Or
  val ∨ : Or.type = Or

  val implies: Implies.type = Implies
  val ==> : Implies.type = Implies

  val iff: Iff.type = Iff
  val <=> : Iff.type = Iff

  val forall: Forall.type = Forall
  val ∀ : Forall.type = forall

  val exists: Exists.type = Exists
  val ∃ : Exists.type = exists

  extension (f: Formula) {
    def unary_! = Neg(Seq(f))
    infix inline def ==>(g: Formula): Formula = Implies(Seq(f, g))
    infix inline def <=>(g: Formula): Formula = Iff(Seq(f, g))
    infix inline def /\(g: Formula): Formula = And(Seq(f, g))
    infix inline def ∧(g: Formula): Formula = And(Seq(f, g))
    infix inline def \/(g: Formula): Formula = Or(Seq(f, g))
    infix inline def ∨(g: Formula): Formula = Or(Seq(f, g))
  }



}
