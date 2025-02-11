package sctptp

object FOL {


  /**
   * A labelled node for tree-like structures.
   */
  trait Symbol {
    val arity: Int
    val id: Identifier
  }

  /**
    * An identifier for variables, functions, predicates and connector.
    * It is a more efficient way, in practice, to represent the identifier s"$name_$no".
    * The symbol "X" is represented by Identifier("X", -1)
    * Note also that "X_n" for n > 0 will be parsed as Identifier("X", n).
    * Note also that for any number n Identifier("X_" + n, -1) is invalid.
    *
    * @param name
    * @param no
    */
  sealed case class Identifier(val name: String, val no: Int) {
    require(no >= -1, "Variable index must be positive")
    require(Identifier.isValidIdentifier(name), "Identifier " + name + "is not valid.")
    override def toString(): String = if (no == 0) name else name + Identifier.counterSeparator + no

    def isUpper: Boolean = name(0).isUpper


  }
  object Identifier {
    def unapply(i: Identifier): Option[(String, Int)] = Some((i.name, i.no))
    def apply(name: String): Identifier = name.split("_") match {
      case a if a.last.forall(_.isDigit)=> Identifier(a.take(a.length-1).mkString(""), a.last.toInt)
      case _ => Identifier(name, 0)
    }
    def apply(name: String, no: Int): Identifier = new Identifier(name, no)

    val counterSeparator: Char = '_'
    def isValidIdentifier(s: String): Boolean = s.nonEmpty // && s.forall(_.isLetterOrDigit) && s.head.isUpper
  }
  given Conversion[String, Identifier] = Identifier(_)

  /**
    * Generates a fresh identifier based on the given base identifier and a set of already used identifiers.
    */
  def freshId(used: Iterable[Identifier], base: Identifier): Identifier = {
    new Identifier(
      base.name,
      used.foldLeft(base.no){ 
        case (no, Identifier(base.name, no2)) =>
          Math.max(no, no2)
        case (no, _) => no
      } + 1
    )
  }

  /** The parent class of function symbols and variables */
  abstract class TermSymbol extends Symbol {
    require(arity >= 0)
  }

  /** Function symbols. If the arity is 0, a constant symbol*/
  case class FunctionSymbol(id: Identifier, arity: Int) extends TermSymbol {
    def apply(args: Seq[Term]): Term = Term(this, args)
    override def toString(): String = id.toString()
  }

  /** Variable symbols. To be TPTP-compliant, must start with a capital letter. Variables with empty names are not TSPT-valid, and are used internally to compute alpha-equivalence. */
  case class VariableSymbol(id: Identifier) extends TermSymbol {
    val name: Identifier = id
    val arity = 0

    override def toString(): String = id.toString()
  }

  /** Terms of first order logic, made of a Function of Variable symbol, and a possibly empty list of arguments */
  sealed case class Term(label: TermSymbol, args: Seq[Term]) {
    require(label.arity == args.size)

    def freeVariables: Set[VariableSymbol] = label match {
      case l: VariableSymbol => Set(l)
      case _ => args.foldLeft(Set.empty[VariableSymbol])((prev, next) => prev union next.freeVariables)
    }

    override def toString(): String = label match {
      case vl: VariableSymbol => vl.name.toString()
      case fl: FunctionSymbol =>
        if (args.length == 0) then fl.id.name
        else s"${fl.toString()}(${args.mkString(",")})"
    }
  }

  /** Helper functions to create Variables**/
  object Variable {

    def apply(label: VariableSymbol): Term = Term(label, Seq())
    def apply(name: String): Term = Term(VariableSymbol(Identifier(name)), Seq())
    def apply(id: Identifier): Term = Term(VariableSymbol(id), Seq())
    def unapply(t: Term): Option[VariableSymbol] = t.label match {
      case l: VariableSymbol => Some(l)
      case _ => None
    }
  }

  //////////////////////////
  /////// Formulas /////////
  //////////////////////////

  /** Atomic (predicate) symbols. If the arity is 0, the symbol of a proposition */
  sealed case class AtomicSymbol(id: Identifier, arity: Int) extends Symbol {
    def apply(args: Seq[Term]): AtomicFormula = AtomicFormula(this, args)
    override def toString(): String = id.name
  }

  /** The equality symbol "=" */
  val equality: AtomicSymbol = AtomicSymbol(Identifier("="), 2)
  /** The "$true" proposition */
  val top: AtomicSymbol = AtomicSymbol(Identifier("$true"), 0)
  /** The "$false" proposition */
  val bot: AtomicSymbol = AtomicSymbol(Identifier("$false"), 0)

  /** Connector symbols ~, =>, <=>, & and | */
  sealed abstract class ConnectorSymbol(val id: Identifier, val arity: Int) extends Symbol {
    def apply(args: Seq[Formula]): ConnectorFormula = ConnectorFormula(this, args)
  }

  /** The negation symbol "~" */
  case object Neg extends ConnectorSymbol(Identifier("~"), 1)
  /** The implication symbol "=>" */
  case object Implies extends ConnectorSymbol(Identifier("=>"), 2)
  /** The equivalence symbol "<=>" */
  case object Iff extends ConnectorSymbol(Identifier("<=>"), 2)
  /** The conjunction symbol "&". It can take arbitrarily many arguments */
  case object And extends ConnectorSymbol(Identifier("&"), -1)
  /** The disjunction symbol "|". It can take arbitrarily many arguments */
  case object Or extends ConnectorSymbol(Identifier("|"), -1)

  /** Binder symbols ! and ? */
  sealed abstract class BinderSymbol(val id: Identifier) extends Symbol {
    val arity = 1
    def apply(bound: VariableSymbol, inner: Formula): BinderFormula = BinderFormula(this, bound, inner)
  }

  /** The universal quantifier symbol "!" */
  case object Forall extends BinderSymbol(Identifier("!"))
  /** The existential quantifier symbol "?" */
  case object Exists extends BinderSymbol(Identifier("?"))

  /** Formulas of first order logic, which can be either Quantified formulas, connector formulas or atomic formulas */
  sealed trait Formula {
    def freeVariables: Set[VariableSymbol]

    def getFreeVariables(): Seq[Term] = {
      freeVariables.foldLeft(Seq())((acc, x) => acc :+ Variable(x))
    }
  }

  /** Formulas made of an atomic symbol and a (possibly empty) list of terms */
  sealed case class AtomicFormula(label: AtomicSymbol, args: Seq[Term]) extends Formula {
    require(label.arity == args.size)

    def freeVariables: Set[VariableSymbol] =
      args.foldLeft(Set.empty[VariableSymbol])((prev, next) => prev union next.freeVariables)

    override def toString(): String = label match {
      case `equality` => s"${args(0).toString()} ${equality.id.name} ${args(1).toString()}"
      case `top` => top.id.name
      case `bot` => bot.id.name
      case al: AtomicSymbol =>
        if (al.arity == 0) then al.id.name
        else s"${al.toString()}(${args.mkString(",")})"
    }

  }

  /**
   * Formulas made of a connector symbol and a list of subformulas.
   */
  sealed case class ConnectorFormula(label: ConnectorSymbol, args: Seq[Formula]) extends Formula {
    require(label.arity == args.size || label.arity == -1)
    require(label.arity != 0)

    override def freeVariables: Set[VariableSymbol] =
      args.foldLeft(Set.empty[VariableSymbol])((prev, next) => prev union next.freeVariables)

    override def toString(): String = label match {
      case Neg => s"${Neg.id.name}${args(0).toString()}"
      case Implies => s"(${args(0).toString()} ${Implies.id.name} ${args(1).toString()})"
      case Iff => s"(${args(0).toString()} ${Iff.id.name} ${args(1).toString()})"
      case And => s"(${args.mkString(s" ${and.id.name} ")})"
      case Or => s"(${args.mkString(s" ${or.id.name} ")})"
    }
  }

  /**
   * Formulas quantified by either ? or !
   */
  sealed case class BinderFormula(label: BinderSymbol, bound: VariableSymbol, inner: Formula) extends Formula {
    override def freeVariables: Set[VariableSymbol] = inner.freeVariables - bound

    override def toString(): String = label match {
      case Forall => s"${Forall.id.name} [${bound.toString()}]: ${inner.toString()}"
      case Exists => s"${Exists.id.name} [${bound.toString()}]: ${inner.toString()}"
    }
  }

  /** Computes the (simultaneous) substitution of some variables by some terms, in a term */
  def substituteVariablesInTerm(t: Term, m: Map[VariableSymbol, Term]): Term = t match {
    case Term(label: VariableSymbol, _) => m.getOrElse(label, t)
    case Term(label, args) => Term(label, args.map(substituteVariablesInTerm(_, m)))
  }

  def substituteFunctionsInTerm(t: Term, m: Map[FunctionSymbol, (Term, Seq[VariableSymbol])]): Term = t match {
    case Term(label: FunctionSymbol, args) => 
      if m.contains(label) then 
        val (newTerm, vars) = m(label)
        val newSubst = vars.zip(args).toMap
        substituteVariablesInTerm(newTerm, newSubst)
      else Term(label, args.map(substituteFunctionsInTerm(_, m)))
    case Term(label, args) => Term(label, args.map(substituteFunctionsInTerm(_, m)))
  }

  /** Computes the (simultaneous) substitution of some variables by some terms, in a formula */
  def substituteVariablesInFormula(phi: Formula, m: Map[VariableSymbol, Term], takenIds: Seq[Identifier] = Seq[Identifier]()): Formula = phi match {
    case AtomicFormula(label, args) => AtomicFormula(label, args.map(substituteVariablesInTerm(_, m)))
    case ConnectorFormula(label, args) => ConnectorFormula(label, args.map(substituteVariablesInFormula(_, m)))
    case BinderFormula(label, bound, inner) =>
      val newSubst = m - bound
      val newTaken = takenIds :+ bound.id
      val fv = m.values.flatMap(_.freeVariables).toSet
      if (fv.contains(bound)) {
        val newBoundVariable = VariableSymbol(freshId(fv.map(_.name) ++ m.keys.map(_.id) ++ newTaken, bound.name))
        val newInner = substituteVariablesInFormula(inner, Map(bound -> Variable(newBoundVariable)), newTaken)
        BinderFormula(label, newBoundVariable, substituteVariablesInFormula(newInner, newSubst, newTaken))
      } else BinderFormula(label, bound, substituteVariablesInFormula(inner, newSubst, newTaken))
  }

  /** Computes the simultaneous substitution of some schematics function symbols by some terms (with holes), in a formula */
  def substituteFunctionsInFormula(phi: Formula, m: Map[FunctionSymbol, (Term, Seq[VariableSymbol])], takenIds: Seq[Identifier] = Seq[Identifier]()): Formula = phi match {
    case AtomicFormula(label, args) => AtomicFormula(label, args.map(substituteFunctionsInTerm(_, m)))
    case ConnectorFormula(label, args) => ConnectorFormula(label, args.map(substituteFunctionsInFormula(_, m))
    )
    case BinderFormula(label, bound, inner) => 
      val newTaken = takenIds :+ bound.id
      val fv = m.values.flatMap((t, lv) => t.freeVariables -- lv.toSet).toSet
      if (fv.contains(bound)) {
        val newBoundVariable = VariableSymbol(freshId(fv.map(_.name) ++ m.keys.map(_.id) ++ newTaken, bound.name))
        val newInner = substituteVariablesInFormula(inner, Map(bound -> Variable(newBoundVariable)), newTaken)
        BinderFormula(label, newBoundVariable, substituteFunctionsInFormula(newInner, m, newTaken))
      } else BinderFormula(label, bound, substituteFunctionsInFormula(inner, m, newTaken))
  }


  def substituteAtomicsInFormula(phi: Formula, m: Map[AtomicSymbol, Formula], takenIds: Seq[Identifier] = Seq[Identifier]()): Formula = phi match {
    case AtomicFormula(label, args) => if args.size == 0 then m.getOrElse(label, phi) else phi
    case ConnectorFormula(label, args) => ConnectorFormula(label, args.map(substituteAtomicsInFormula(_, m))
    )
    case BinderFormula(label, bound, inner) => 
      val newTaken = takenIds :+ bound.id
      val fv = m.values.flatMap(_.freeVariables).toSet
      if (fv.contains(bound)) {
        val newBoundVariable = VariableSymbol(freshId(fv.map(_.name) ++ m.keys.map(_.id) ++ newTaken, bound.name))
        val newInner = substituteVariablesInFormula(inner, Map(bound -> Variable(newBoundVariable)), newTaken)
        BinderFormula(label, newBoundVariable, substituteAtomicsInFormula(newInner, m, newTaken))
      } else BinderFormula(label, bound, substituteAtomicsInFormula(inner, m, newTaken))
  }


  def substitutePredicatesInFormula(phi: Formula, m: Map[AtomicSymbol, (Formula, Seq[VariableSymbol])], takenIds: Seq[Identifier] = Seq[Identifier]()): Formula = phi match {
    case AtomicFormula(label, args) => 
      if m.contains(label) then
        val (newFormula, vars) = m(label)
        val newSubst = vars.zip(args).toMap
        substituteVariablesInFormula(newFormula, newSubst)
      else phi
    case ConnectorFormula(label, args) => ConnectorFormula(label, args.map(substitutePredicatesInFormula(_, m)))
    case BinderFormula(label, bound, inner) => 
      val newTaken = takenIds :+ bound.id
      val fv = m.values.flatMap((t, lv) => t.freeVariables -- lv.toSet).toSet
      if (fv.contains(bound)) {
        val newBoundVariable = VariableSymbol(freshId(fv.map(_.name) ++ m.keys.map(_.id) ++ newTaken, bound.name))
        val newInner = substituteVariablesInFormula(inner, Map(bound -> Variable(newBoundVariable)), newTaken)
        BinderFormula(label, newBoundVariable, substitutePredicatesInFormula(newInner, m, newTaken))
      } else BinderFormula(label, bound, substitutePredicatesInFormula(inner, m, newTaken))
  }



  extension (tl: TermSymbol) {
    def apply(args: Term*): Term = Term(tl, args)
  }

  extension (al: AtomicSymbol) {
    def apply(args: Term*): AtomicFormula = AtomicFormula(al, args)
  }

  extension (cl: ConnectorSymbol) {
    def apply(args: Formula*): ConnectorFormula = ConnectorFormula(cl, args)
  }


  //Syntactic sugar to allow writing formulas in the usual way

  val === = equality
  extension (t: Term) {
    infix def ===(u: Term): Formula = equality(List(t, u))
  }

  val ⊤ : top.type = top

  val ⊥ : bot.type = bot

  val neg: Neg.type = Neg
  val ~ : Neg.type = Neg

  val and: And.type = And
  val /\ : And.type = And

  val or: Or.type = Or
  val \/ : Or.type = Or

  val implies: Implies.type = Implies
  val ==> : Implies.type = Implies

  val iff: Iff.type = Iff
  val <=> : Iff.type = Iff

  val forall: Forall.type = Forall
  val ∀ : Forall.type = forall
  val ? : Forall.type = forall

  val exists: Exists.type = Exists
  val ∃ : Exists.type = exists
  val ! : Exists.type = exists

  extension (f: Formula) {
    def unary_! = Neg(Seq(f))
    def unary_~ = Neg(Seq(f))
    infix inline def ==>(g: Formula): Formula = Implies(Seq(f, g))
    infix inline def <=>(g: Formula): Formula = Iff(Seq(f, g))
    infix inline def /\(g: Formula): Formula = And(Seq(f, g))
    infix inline def &(g: Formula): Formula = And(Seq(f, g))
    infix inline def \/(g: Formula): Formula = Or(Seq(f, g))
    infix inline def |(g: Formula): Formula = Or(Seq(f, g))
  }

  def toLocallyNameless(phi:Formula): Formula = {
    val default_bound = VariableSymbol("%")
    def toLocallyNamelessTerm(t: Term, subst: Map[Identifier, Int], i: Int): Term = {
      t match {
        case Term(label: VariableSymbol, _) =>
          if (subst.contains(label.id)) Variable(Identifier("%", i - subst(label.id)))
          else if label.id.name(0) == '%' then Variable(Identifier("%" + label.id.name, i - subst(label.id)))
          else t
        case Term(label, args) => Term(label, args.map(c => toLocallyNamelessTerm(c, subst, i)))
      }
    }
    def toLocallyNamelessInner(phi: Formula, subst: Map[Identifier, Int], i: Int): Formula = {
      phi match {
        case AtomicFormula(id, args) => AtomicFormula(id, args.map(c => toLocallyNamelessTerm(c, subst, i)))
        case ConnectorFormula(id, args) => ConnectorFormula(id, args.map(f => toLocallyNamelessInner(f, subst, i)))
        case BinderFormula(id, bound, inner) => BinderFormula(id, default_bound, toLocallyNamelessInner(inner, subst + (bound.id -> i), i + 1))

      }
    }
    toLocallyNamelessInner(phi, Map(), 0)
  }

  def areAlphaEquivalent(phi: Formula, psi: Formula) = (phi eq psi) || (toLocallyNameless(phi) == toLocallyNameless(psi))
  def isSame (phi: Formula, psi: Formula) = areAlphaEquivalent(phi, psi)
  def isSubset(s1: Iterable[Formula], s2: Iterable[Formula]): Boolean = 
    val s2Local = s2.map(toLocallyNameless).toSet
    s1.map(toLocallyNameless).forall(s2Local.contains)
  
  def isSameSet(s1: Iterable[Formula], s2: Iterable[Formula]): Boolean = 
    val s1Local = s1.map(toLocallyNameless).toSet
    val s2Local = s2.map(toLocallyNameless).toSet
    (s1.size == s2.size) && s1Local.forall(s2Local.contains)
}
