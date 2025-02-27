use core::panic;
use std::fmt;
use std::collections::HashMap;
use std::fmt::write;
use std::str::FromStr;
use egg::Symbol;
use egg::ENodeOrVar;


// hierarchy of classes for first order logic with variables, constants, functions, predicates and all and exists quantifiers

// terms:

#[derive(Debug, Clone)]
#[derive(PartialEq)]
pub enum Term {
  Function(String, Vec<Box<Term>>),
}

impl fmt::Display for Term {
  fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
    match self {
      Term::Function(name, args) => {
        if args.len() > 0 {write!(f, "{}({})", name, args.iter().map(|x| x.to_string()).collect::<Vec<String>>().join(", "))}
        else {write!(f, "{}", name)}
      }
    }
  }
}

pub fn is_variable(s: &str) -> bool {
  s.chars().next().unwrap().is_uppercase()
}

// formulas:

#[derive(Debug, Clone)]
#[derive(PartialEq)]
pub enum Formula {
  True,
  False,
  Predicate(String, Vec<Box<Term>>),
  Not(Box<Formula>),
  And(Vec<Box<Formula>>),
  Or(Vec<Box<Formula>>),
  Implies(Box<Formula>, Box<Formula>),
  Iff(Box<Formula>, Box<Formula>),
  Forall(Vec<String>, Box<Formula>),
  Exists(Vec<String>, Box<Formula>),
}

impl fmt::Display for Formula {
  fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
    match self {
      Formula::True => write!(f, "$true"),
      Formula::False => write!(f, "$false"),
      Formula::Predicate(op, args) => 
        if op == "=" {write!(f, "({} = {})", args[0], args[1])}
        else if args.len() > 0 {write!(f, "{}({})", op, args.iter().map(|x| x.to_string()).collect::<Vec<String>>().join(", "))}
        else {write!(f, "{}", op)}
      Formula::Not(formula) => write!(f, "¬{}", formula),
      Formula::And(formulas) => write!(f, "({})", formulas.iter().map(|x| x.to_string()).collect::<Vec<String>>().join(" && ")),
      Formula::Or(formulas) => write!(f, "({})", formulas.iter().map(|x| x.to_string()).collect::<Vec<String>>().join(" || ")),
      Formula::Implies(formula1, formula2) => write!(f, "({} => {})", formula1, formula2),
      Formula::Iff(formula1, formula2) => write!(f, "({} <=> {})", formula1, formula2),
      Formula::Forall(vars, formula) => write!(f, "![{}] : {}", vars.join(", "), formula),
      Formula::Exists(vars, formula) => write!(f, "?[{}] : {}", vars.join(", "), formula),
    }
  }
}

// sequents:

#[derive(Debug, Clone)]
#[derive(PartialEq)]
pub struct Sequent {
  pub left: Vec<Formula>,
  pub right: Vec<Formula>,
}

impl fmt::Display for Sequent {
  fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
    write!(f, "[{}] --> [{}]", self.left.iter().map(|x| x.to_string()).collect::<Vec<String>>().join(", "), self.right.iter().map(|x| x.to_string()).collect::<Vec<String>>().join(", "))
  }
}

#[derive(Debug, Clone)]
#[derive(PartialEq)]
pub enum Statement {
  Sequent(Sequent),
  Formula(Formula),
}

#[derive(Debug, Clone)]
#[derive(PartialEq)]
pub struct AnnotatedStatement {
  pub name: String,
  pub role: String,
  pub statement: Statement,
}


//functions

pub fn instantiate_term(expr: &Term, map: &HashMap<String, Term>) -> Term {
  match expr {
    Term::Function(name, args) => {
      if is_variable(name) && args.is_empty() && map.contains_key(name.as_str()) {
        map[name.as_str()].clone()
      } else {
        let new_args = args.iter().map(|x| Box::new(instantiate_term(x, map))).collect();
        Term::Function(name.clone(), new_args)
      }
    }
  }
}

pub fn instantiate_formula(formula: &Formula, map_t: &HashMap<String, Term>, map_f: &HashMap<String, Formula>) -> Formula {
  match formula {
    
    Formula::True => Formula::True,
    Formula::False => Formula::False,
    Formula::Predicate(name, args) => {
      if args.len() == 0 && map_f.contains_key(name.as_str()) {
        map_f[name.as_str()].clone()
      } else {
        let new_args = args.iter().map(|x| Box::new(instantiate_term(x, map_t))).collect();
        Formula::Predicate(name.clone(), new_args)
      }
    },
    Formula::Not(formula) => Formula::Not(Box::new(instantiate_formula(formula, map_t, map_f))),
    Formula::And(formulas) => {
      let new_formulas = formulas.iter().map(|x| Box::new(instantiate_formula(x, map_t, map_f))).collect();
      Formula::And(new_formulas)
    },
    Formula::Or(formulas) => {
      let new_formulas = formulas.iter().map(|x| Box::new(instantiate_formula(x, map_t, map_f))).collect();
      Formula::Or(new_formulas)
    },
    Formula::Implies(formula1, formula2) => {
      let new_formula1 = instantiate_formula(formula1, map_t, map_f);
      let new_formula2 = instantiate_formula(formula2, map_t, map_f);
      Formula::Implies(Box::new(new_formula1), Box::new(new_formula2))
    },
    Formula::Iff(formula1, formula2) => {
      let new_formula1 = instantiate_formula(formula1, map_t, map_f);
      let new_formula2 = instantiate_formula(formula2, map_t, map_f);
      Formula::Iff(Box::new(new_formula1), Box::new(new_formula2))
    },
    Formula::Forall(vars, formula) => {
      let new_map = vars.iter().map(|x| (x.clone(), Term::Function(x.clone(), Vec::new()))).collect();
      let new_formula = instantiate_formula(formula, &new_map, map_f);
      Formula::Forall(vars.clone(), Box::new(new_formula))
    },
    Formula::Exists(vars, formula) => {
      let new_map = vars.iter().map(|x| (x.clone(), Term::Function(x.clone(), Vec::new()))).collect();
      let new_formula = instantiate_formula(formula, &new_map, map_f);
      Formula::Exists(vars.clone(), Box::new(new_formula))
    },
  }
}



pub fn matching_term(expr: &Term, expr2: &Term, map: &mut HashMap<String, Term>) -> bool {
  match (expr, expr2) {
    (Term::Function(name, args), Term::Function(name2, args2)) => {
      if is_variable(name) && args.is_empty() {
        if map.contains_key(name.as_str()) {
        return map[name.as_str()] == *expr2;
        } else {
          map.insert(name.to_owned(), expr2.clone());
          return true;
        }
      } else if name == name2 && args.len() == args2.len() {
        let res = args.iter().zip(args2.iter())
          .all(|(e1, e2)| matching_term(e1, e2, map));
        res
      } else {
        false
      }
    }
  }
}
pub fn matching_formula(formula: &Formula, formula2: &Formula, map: &mut HashMap<String, Term>) -> bool {
  match (formula, formula2) {
    (Formula::True, Formula::True) => true,
    (Formula::False, Formula::False) => true,
    (Formula::Predicate(name, args), Formula::Predicate(name2, args2)) => {
      if name == name2 && args.len() == args2.len() {
        let res = args.iter().zip(args2.iter())
          .all(|(e1, e2)| matching_term(e1, e2, map));
        res
      } else {
        false
      }
    },
    (Formula::Not(formula), Formula::Not(formula2)) => matching_formula(formula, formula2, map),
    (Formula::And(formulas), Formula::And(formulas2)) => {
      if formulas.len() == formulas2.len() {
        let res = formulas.iter().zip(formulas2.iter())
          .all(|(e1, e2)| matching_formula(e1, e2, map));
        res
      } else {
        false
      }
    },
    (Formula::Or(formulas), Formula::Or(formulas2)) => {
      if formulas.len() == formulas2.len() {
        let res = formulas.iter().zip(formulas2.iter())
          .all(|(e1, e2)| matching_formula(e1, e2, map));
        res
      } else {
        false
      }
    },
    (Formula::Implies(formula1, formula2), Formula::Implies(formula1_2, formula2_2)) => {
      matching_formula(formula1, formula1_2, map) && matching_formula(formula2, formula2_2, map)
    },
    (Formula::Iff(formula1, formula2), Formula::Iff(formula1_2, formula2_2)) => {
      matching_formula(formula1, formula1_2, map) && matching_formula(formula2, formula2_2, map)
    },
    _ => false
  }
}


// Language 

use egg::{define_language, Id, RecExpr};

define_language! {
  pub enum FOLLang {
    "$true" = True,
    "$false" = False,
    "~" = Not(Id),
    "&&" = And([Id; 2]),
    "or" = Or([Id; 2]),
    "=>" = Implies([Id; 2]),
    "<=>" = Iff([Id; 2]),
    Function(Symbol, Vec<Id>),
    Predicate(Symbol, Vec<Id>),
  }
}

pub fn term_to_recexpr(term: &Term, expr: &mut RecExpr<FOLLang>) -> Id {
  match term {
    Term::Function(name, args) => {
      let args_ids = args.iter().map(|x| term_to_recexpr(x, expr)).collect::<Vec<Id>>();
      expr.add(FOLLang::Function(Symbol::from(name.clone()), args_ids))
    }
  }
}

pub fn formula_to_recexpr(formula: &Formula, expr: &mut RecExpr<FOLLang>) -> Id {
  match formula {
    Formula::True => expr.add(FOLLang::True),
    Formula::False => expr.add(FOLLang::False),
    Formula::Predicate(name, args) => {
      let args_ids = args.iter().map(|x| term_to_recexpr(x, expr)).collect::<Vec<Id>>();
      expr.add(FOLLang::Predicate(Symbol::from(name.clone()), args_ids))
    },
    Formula::Not(formula) => {
      let inner_id = formula_to_recexpr(formula, expr);
      expr.add(FOLLang::Not(inner_id))
    },
    Formula::And(formulas) => {
      let formulas_ids = formulas.iter().map(|x| formula_to_recexpr(x, expr)).collect::<Vec<Id>>();
      expr.add(FOLLang::And([formulas_ids[0], formulas_ids[1]]))
    },
    Formula::Or(formulas) => {
      let formulas_ids = formulas.iter().map(|x| formula_to_recexpr(x, expr)).collect::<Vec<Id>>();
      expr.add(FOLLang::Or([formulas_ids[0], formulas_ids[1]]))
    },
    Formula::Implies(formula1, formula2) => {
      let formula1_id = formula_to_recexpr(formula1, expr);
      let formula2_id = formula_to_recexpr(formula2, expr);
      expr.add(FOLLang::Implies([formula1_id, formula2_id]))
    },
    Formula::Iff(formula1, formula2) => {
      let formula1_id = formula_to_recexpr(formula1, expr);
      let formula2_id = formula_to_recexpr(formula2, expr);
      expr.add(FOLLang::Iff([formula1_id, formula2_id]))
    },
    Formula::Forall(_vars, _formula) => {
      panic!("Forall not implemented yet")
    },
    Formula::Exists(_vars, _formula) => {
      panic!("Exists not implemented yet")
    },
  }
}

pub fn term_to_recexpr_pattern(term: &Term, vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
  match term {
    Term::Function(name, args) => {
      if is_variable(name) && args.is_empty() {
        expr.add(ENodeOrVar::Var(egg::Var::from_str(&format!("?{}", name)).expect(&format!("incorrect variable name: {}", name)) ))
      } else {
        let args_ids = args.iter().map(|x| term_to_recexpr_pattern(x, vars, expr)).collect::<Vec<Id>>();
        expr.add(ENodeOrVar::ENode(FOLLang::Function(Symbol::from(name.clone()), args_ids)))
      }
    }
  }
}


pub fn formula_to_recexpr_pattern(formula: &Formula, vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
  match formula {
    Formula::True => expr.add(ENodeOrVar::ENode(FOLLang::True)),
    Formula::False => expr.add(ENodeOrVar::ENode(FOLLang::False)),
    Formula::Predicate(name, args) => {
      let args_ids = args.iter().map(|x| term_to_recexpr_pattern(x, vars, expr)).collect::<Vec<Id>>();
      expr.add(ENodeOrVar::ENode(FOLLang::Predicate(Symbol::from(name.clone()), args_ids)))
    },
    Formula::Not(formula) => {
      let inner_id = formula_to_recexpr_pattern(formula, vars, expr);
      expr.add(ENodeOrVar::ENode(FOLLang::Not(inner_id)))
    },
    Formula::And(formulas) => {
      let formulas_ids = formulas.iter().map(|x| formula_to_recexpr_pattern(x, vars, expr)).collect::<Vec<Id>>();
      expr.add(ENodeOrVar::ENode(FOLLang::And([formulas_ids[0], formulas_ids[1]])))
    },
    Formula::Or(formulas) => {
      let formulas_ids = formulas.iter().map(|x| formula_to_recexpr_pattern(x, vars, expr)).collect::<Vec<Id>>();
      expr.add(ENodeOrVar::ENode(FOLLang::Or([formulas_ids[0], formulas_ids[1]])))
    },
    Formula::Implies(formula1, formula2) => {
      let formula1_id = formula_to_recexpr_pattern(formula1, vars, expr);
      let formula2_id = formula_to_recexpr_pattern(formula2, vars, expr);
      expr.add(ENodeOrVar::ENode(FOLLang::Implies([formula1_id, formula2_id])))
    },
    Formula::Iff(formula1, formula2) => {
      let formula1_id = formula_to_recexpr_pattern(formula1, vars, expr);
      let formula2_id = formula_to_recexpr_pattern(formula2, vars, expr);
      expr.add(ENodeOrVar::ENode(FOLLang::Iff([formula1_id, formula2_id])))
    },
    Formula::Forall(_vars, _formula) => {
      panic!("Forall not implemented yet")
    },
    Formula::Exists(_vars, _formula) => {
      panic!("Exists not implemented yet")
    },
  }
}


// Translator from tptp parser

pub mod tptp_fol_translator {

  use egg::*;
  use tptp::fof;
  use tptp::top;
  use tptp::common::*;
  use tptp::TPTPIterator;
  use crate::fol::*;

  pub trait FOLTranslator<T> {
    fn translate(tm: &T) -> Self;
  }


  impl FOLTranslator<fof::FunctionTerm<'_>> for Term {
    fn translate(tm: &fof::FunctionTerm) -> Self {
        use fof::FunctionTerm::*;
        match tm {
            Plain(p) => Self::translate(p),
            Defined(d) => Self::translate(d),
            System(_) => todo!(),
        }
    }
  }

  impl FOLTranslator<fof::DefinedTerm<'_>> for Term {
    fn translate(tm: &fof::DefinedTerm) -> Self {
        use fof::DefinedTerm::*;
        match tm {
            Defined(d) => Self::translate(d),
            Atomic(_) => todo!(),
        }
    }
  }

  impl FOLTranslator<tptp::common::DefinedTerm<'_>> for Term {
    fn translate(tm: &tptp::common::DefinedTerm) -> Self {
        use tptp::common::DefinedTerm::*;
        match tm {
            Number(n) => Term::Function(n.to_string(), Vec::new()),
            Distinct(_) => todo!(),
        }
    }
  }

  impl FOLTranslator<fof::Term<'_>> for Term {
    fn translate(tm: &fof::Term) -> Self {
        use fof::Term::*;
        match tm {
            Variable(v) => Term::Function(v.to_string(), Vec::new()),
            Function(f) => Self::translate(&**f),
        }
    }
  }

  impl FOLTranslator<fof::Arguments<'_>> for Vec<Box<Term>> {
    fn translate(args: &fof::Arguments) -> Self {
      args.0.clone().into_iter().map(move |a: fof::Term<'_>| Box::new(Term::translate(&a))).collect()
    }
  }

  impl FOLTranslator<fof::PlainTerm<'_>> for Term {
    fn translate(tm: &fof::PlainTerm) -> Self {
        use fof::PlainTerm::*;
        match tm {
            Constant(c) => Term::Function(c.to_string(), Vec::new()),
            Function(f, args) => {
              let ids = Vec::translate(args);
              Term::Function(f.to_string(), ids)
            }
        }
    }
  }



  impl FOLTranslator<fof::LogicFormula<'_>> for Formula {
    fn translate(frm: &fof::LogicFormula) -> Formula {
      use fof::LogicFormula::*;
      match frm {
        Binary(b) => Self::translate(b),
        Unary(u) => Self::translate(u),
        Unitary(u) => Self::translate(u),
      }
    }
  }

  impl FOLTranslator<fof::QuantifiedFormula<'_>> for Formula {
    fn translate(_frm: &fof::QuantifiedFormula) -> Self {
      match _frm.quantifier {
        fof::Quantifier::Forall => Formula::Forall(_frm.bound.0.iter().map(|x| x.to_string()).collect(), Box::new(Formula::translate(&*_frm.formula))),
        fof::Quantifier::Exists => Formula::Exists(_frm.bound.0.iter().map(|x| x.to_string()).collect(), Box::new(Formula::translate(&*_frm.formula))),
          
      }
    }
  }

  impl FOLTranslator<fof::UnitFormula<'_>> for Formula {
    fn translate(frm: &fof::UnitFormula) -> Formula {
        use fof::UnitFormula::*;
        match frm {
            Unitary(u) => Self::translate(u),
            Unary(u) => Self::translate(u),
        }
    }
  }

  impl FOLTranslator<fof::InfixUnary<'_>> for Formula {
    fn translate(frm: &fof::InfixUnary) -> Self {
        let lid = Term::translate(&*frm.left);
        let rid = Term::translate(&*frm.right);
        Formula::Predicate(frm.op.to_string(), vec![Box::new(lid), Box::new(rid)])
    }
  }

  impl FOLTranslator<fof::UnaryFormula<'_>> for Formula {
    fn translate(frm: &fof::UnaryFormula) -> Formula {
      use fof::UnaryFormula::*;
      match frm {
        Unary(op, fuf) => {
          let child = Formula::translate(&**fuf);
          if op.to_string() == "~" {
            Formula::Not(Box::new(child))
          } else {
            std::panic!("Only ~ is supported as unary operator")
          }
        },
        InfixUnary(i) => Self::translate(i),
      }
    }
  }

  impl FOLTranslator<fof::BinaryFormula<'_>> for Formula {
    fn translate(frm: &fof::BinaryFormula) -> Formula {
        use fof::BinaryFormula::*;
        match frm {
            Nonassoc(fbn) => Self::translate(fbn),
            Assoc(fba) => Self::translate(fba),
        }
    }
  }

  impl FOLTranslator<fof::BinaryNonassoc<'_>> for Formula {
    fn translate(frm: &fof::BinaryNonassoc) -> Formula {
        let lid = Formula::translate(&*frm.left);
        let rid = Formula::translate(&*frm.right);
        match frm.op.to_string().as_str() {
            "=>" => Formula::Implies(Box::new(lid), Box::new(rid)),
            "<=>" => Formula::Iff(Box::new(lid), Box::new(rid)),
            _ => std::panic!("Only => and <=> are supported as binary nonassoc operator")
        }
    }
  }

  impl FOLTranslator<fof::BinaryAssoc<'_>> for Formula {
    fn translate(fm: &fof::BinaryAssoc) -> Formula {
        use fof::BinaryAssoc::*;
      match fm {
        Or(fms) => {
          let ids = fms.0.clone().into_iter().map(|a| Box::new(Formula::translate(&a))).collect();
          Formula::Or(ids)
        }
        And(fms) => {
          let ids = fms.0.clone().into_iter().map(|a| Box::new(Formula::translate(&a))).collect();
          Formula::And(ids)
        }
      }
    }
  }

  impl FOLTranslator<fof::UnitaryFormula<'_>> for Formula {
    fn translate(frm: &fof::UnitaryFormula) -> Formula {
      use fof::UnitaryFormula::*;
      match frm {
        Parenthesised(flf) => Self::translate(&**flf),
        Quantified(fqf) => Self::translate(fqf),
        Atomic(a) => Self::translate(&**a),
      }
    }
  }

  impl FOLTranslator<fof::PlainAtomicFormula<'_>> for Formula {
    fn translate(frm: &fof::PlainAtomicFormula) -> Formula {
      use fof::PlainTerm::*;
      match &frm.0 {
        Constant(c) => Formula::Predicate(c.to_string(), Vec::new()),
        Function(f, args) => {
          let ids = Vec::translate(&*args);
          Formula::Predicate(f.to_string(), ids)
        }
      }
    }
  }
  

  impl FOLTranslator<fof::DefinedAtomicFormula<'_>> for Formula {
    fn translate(frm: &fof::DefinedAtomicFormula) -> Formula {
        use fof::DefinedAtomicFormula::*;
        match frm {
            Plain(p) => Self::translate(p),
            Infix(i) => {
              let left = Term::translate(&*i.left);
              let right = Term::translate(&*i.right);
              Formula::Predicate(i.op.to_string(), vec![Box::new(left), Box::new(right)])
            }
        }
        
    }
  }

  impl FOLTranslator<fof::DefinedPlainFormula<'_>> for Formula {
    fn translate(fm: &fof::DefinedPlainFormula) -> Formula {
        use fof::DefinedPlainTerm::*;
        match &fm.0 {
            Constant(c) if c.0 .0 .0 .0 .0 == "true" => 
              Formula::True,
            Constant(c) if c.0 .0 .0 .0 .0 == "false" => 
              Formula::False,
            Constant(c) => Formula::Predicate(c.to_string(), Vec::new()),
            Function(f, args) => {
              let ids = Vec::translate(&*args);
              Formula::Predicate(f.to_string(), ids)
              }
        }
    }
  }

  impl FOLTranslator<fof::AtomicFormula<'_>> for Formula {
    fn translate(frm: &fof::AtomicFormula) -> Formula {
        use fof::AtomicFormula::*;
        match frm {
            Plain(p) => Self::translate(p),
            Defined(d) => Self::translate(d),
            System(_) => todo!(),
        }
    }
  }

  impl FOLTranslator<fof::Formula<'_>> for Formula {
    fn translate(frm: &fof::Formula) -> Formula {
      match frm {
        fof::Formula::Logic(l) => Self::translate(l),
        fof::Formula::Sequent(_) => todo!(),
      }
    }
  }

  impl FOLTranslator<fof::LogicSequent<'_>> for Sequent {
    fn translate(frm: &fof::LogicSequent) -> Sequent {
      Sequent {
        left: frm.left.0.iter().map(|x| Formula::translate(&*x)).collect(),
        right: frm.right.0.iter().map(|x| Formula::translate(&*x)).collect()
      }
    }
  }

  impl FOLTranslator<top::AnnotatedFormula<'_>> for AnnotatedStatement {
    fn translate(frm: &top::AnnotatedFormula) -> AnnotatedStatement {
      match frm {
        top::AnnotatedFormula::Fof(f) => 
          Self::translate(&**f),
        _ => std::panic!("Only Fof is supported")
      }
    }
  }

  impl FOLTranslator<top::FofAnnotated<'_>> for AnnotatedStatement {
    fn translate(frm: &top::FofAnnotated) -> AnnotatedStatement {
      AnnotatedStatement {
        name: frm.0.name.to_string(),
        role: frm.0.role.to_string(),
        statement: Statement::translate(&*frm.0.formula)
      }
    }
  }

  impl FOLTranslator<fof::Formula<'_>> for Statement {
    fn translate(frm: &fof::Formula) -> Statement {
      match frm {
        fof::Formula::Logic(l) => Statement::Formula(Formula::translate(l)),
        fof::Formula::Sequent(s) => Statement::Sequent(Sequent::translate(s)),
      }
    }
  }
}

