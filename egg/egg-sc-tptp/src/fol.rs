use std::fmt;

// hierarchy of classes for first order logic with variables, constants, functions, predicates and all and exists quantifiers

// terms:

#[derive(Debug, Clone)]
enum Term {
  Variable(String),
  Function(String, Vec<Box<Term>>),
}

impl fmt::Display for Term {
  fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
    match self {
      Term::Variable(name) => write!(f, "{}", name),
      Term::Function(name, args) => write!(f, "{}({})", name, args.iter().map(|x| x.to_string()).collect::<Vec<String>>().join(", "))
    }
  }
}

// formulas:

#[derive(Debug, Clone)]
enum Formula {
  Predicate(String, Vec<Box<Term>>),
  Not(Box<Formula>),
  And(Box<Formula>, Box<Formula>),
  Or(Box<Formula>, Box<Formula>),
  Implies(Box<Formula>, Box<Formula>),
  Iff(Box<Formula>, Box<Formula>),
  Forall(Vec<String>, Box<Formula>),
  Exists(Vec<String>, Box<Formula>),
}

impl fmt::Display for Formula {
  fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
    match self {
      Formula::Predicate(name, args) => write!(f, "{}({})", name, args.iter().map(|x| x.to_string()).collect::<Vec<String>>().join(", ")),
      Formula::Not(formula) => write!(f, "¬{}", formula),
      Formula::And(formula1, formula2) => write!(f, "({} && {})", formula1, formula2),
      Formula::Or(formula1, formula2) => write!(f, "({} || {})", formula1, formula2),
      Formula::Implies(formula1, formula2) => write!(f, "({} => {})", formula1, formula2),
      Formula::Iff(formula1, formula2) => write!(f, "({} <=> {})", formula1, formula2),
      Formula::Forall(vars, formula) => write!(f, "![{}] : {}", vars.join(", "), formula),
      Formula::Exists(vars, formula) => write!(f, "?[{}] : {}", vars.join(", "), formula),
    }
  }
}

// sequents:

#[derive(Debug, Clone)]
struct Sequent {
  antecedent: Vec<Formula>,
  succedent: Vec<Formula>,
}

impl fmt::Display for Sequent {
  fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
    write!(f, "[{}] --> [{}]", self.antecedent.iter().map(|x| x.to_string()).collect::<Vec<String>>().join(", "), self.succedent.iter().map(|x| x.to_string()).collect::<Vec<String>>().join(", "))
  }
}


use egg::define_language;
use egg::Id;

define_language! {
  enum FOLLang {
    Variable(String),
    Function(String, Vec<Id>),
  }
}