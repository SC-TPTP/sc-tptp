
use egg::{*, rewrite as rw};
use core::panic;
use std::io::Read;
use tptp::fof;
use tptp::top;
use tptp::common::*;
use tptp::TPTPIterator;
use ENodeOrVar::*;

use crate::printer::*;

//function that ready translate a file with path 'path' and then calls TPTPIterator::<()>::new(bytes) on it
pub fn take_input(path: &str) -> Vec<u8> {
  let mut file = std::fs::File::open(path).unwrap();
  let mut bytes = Vec::new();
  file.read_to_end(&mut bytes).unwrap();
  bytes
  
}



pub struct TPTPProblem {
  pub path: String,
  pub header: String,
  pub axioms: Vec<(String, RecExpr<ENodeOrVar<SymbolLang>>, RecExpr<ENodeOrVar<SymbolLang>>)>,
  pub axioms_as_roots: Vec<RecExpr<SymbolLang>>,
  pub conjecture: (String, RecExpr<SymbolLang>, RecExpr<SymbolLang>),
  pub string_rules: Vec<(String, String)>,
}

pub fn parse_tptp_problem(path: &str) -> TPTPProblem {
  let bytes = take_input(path);
  let header = String::from_utf8(bytes.clone()).unwrap();
  let mut parser = TPTPIterator::<()>::new(bytes.as_slice());
  let mut rules: Vec<(String, RecExpr<ENodeOrVar<SymbolLang>>, RecExpr<ENodeOrVar<SymbolLang>>)> = Vec::new();
  let mut string_rules: Vec<(String, String)> = Vec::new();
  let mut conjecture: (String, RecExpr<SymbolLang>, RecExpr<SymbolLang>) = ("".to_string(), RecExpr::default(), RecExpr::default());
  let mut axioms_as_roots: Vec<RecExpr<SymbolLang>> = Vec::new();
  for result in &mut parser {
    match result {
      Ok(r) => {
        match r {
          top::TPTPInput::Annotated(annotated) => {
            match &*annotated {
              top::AnnotatedFormula::Fof(fof_annotated) => {
                let name = fof_annotated.0.name.to_string();
                let role = fof_annotated.0.role.to_string();
                let main_formula = match &*fof_annotated.0.formula {
                  fof::Formula::Logic(f) => f,
                  _ => todo!()
                };
                let formula = &mut main_formula.clone();
                get_head_logic(&main_formula, formula);
                //let annotations = &fof_annotated.0.annotations;
                match role.as_str() {
                  "conjecture" => {                    
                    match formula {
                      fof::LogicFormula::Unitary(u) => {
                        match u {
                          fof::UnitaryFormula::Atomic(a) => {
                            match &**a {
                              fof::AtomicFormula::Defined(d) => {
                                match d {
                                  fof::DefinedAtomicFormula::Infix(i) => {
                                    if i.op.0.to_string() == "=" {
                                      use non_patern_translator::TranslatorSL;
                                      let mut expr_left: RecExpr<SymbolLang> = RecExpr::default();
                                      let mut expr_right: RecExpr<SymbolLang> = RecExpr::default();
                                      Id::translate(&*i.left, &mut expr_left);
                                      Id::translate(&*i.right, &mut expr_right);
                                      conjecture = (name, expr_left, expr_right);

                                    } else {panic!("formulas must be equalities")}
                                    
                                  }
                                  _ => panic!("formulas must be equalities")
                                }
                              }
                              _ => panic!("formulas must be equalities")
                            }
                          },
                          _ => panic!("formulas must be equalities")
                        }
                      }
                      _ => panic!("formulas must be equalities,")
                    }
                    
                  }
                  "axiom" => {
                    match formula {
                      fof::LogicFormula::Unitary(u) => {
                        match u {
                          fof::UnitaryFormula::Atomic(a) => {
                            match &**a {
                              fof::AtomicFormula::Defined(d) => {
                                match d {
                                  fof::DefinedAtomicFormula::Infix(i) => {
                                    if i.op.0.to_string() == "=" {
                                      {
                                        use patern_translator::TranslatorSL;
                                        let mut expr_left: RecExpr<ENodeOrVar<SymbolLang>> = RecExpr::default();
                                        let mut expr_right: RecExpr<ENodeOrVar<SymbolLang>> = RecExpr::default();
                                        Id::translate(&*i.left, &mut expr_left);
                                        Id::translate(&*i.right, &mut expr_right);
                                        rules.push((name, expr_left, expr_right));
                                      }
                                      {
                                        use non_patern_translator::TranslatorSL;
                                        let mut expr_left: RecExpr<SymbolLang> = RecExpr::default();
                                        let mut expr_right: RecExpr<SymbolLang> = RecExpr::default();
                                        Id::translate(&*i.left, &mut expr_left);
                                        Id::translate(&*i.right, &mut expr_right);
                                        axioms_as_roots.push(expr_left);
                                        axioms_as_roots.push(expr_right);
                                      }
                                      string_rules.push((i.left.to_string(), i.right.to_string()));
                                    } else {panic!("formulas must be equalities")}
                                    
                                  }
                                  _ => panic!("formulas must be equalities")
                                }
                              }
                              _ => panic!("formulas must be equalities")
                            }
                          },
                          _ => panic!("formulas must be equalities")
                        }
                      }
                      _ => panic!("formulas must be equalities,")
                    }
                  }
                  _ => ()
                }
              }
              _ => ()
            }
          }
          _ => ()
        }
        
      }
      Err(e) => {
        println!("Error: {:?}", e);
        break;
      }
    }
  }

  return TPTPProblem {
    path: path.to_string(),
    header: header,
    axioms: rules,
    conjecture: conjecture,
    string_rules: string_rules,
    axioms_as_roots: axioms_as_roots,
  }
}


pub fn solve_tptp_problem(problem: &TPTPProblem) -> Explanation<egg::SymbolLang> {
  let rules: Vec<Rewrite<SymbolLang, ()>> = problem.axioms.iter().map(|(name, l, r)| {
    Rewrite::<egg::SymbolLang, ()>::new(name, egg::Pattern::new(l.clone()), egg::Pattern::new(r.clone())).expect("failed to create rewrite rule")
  }).collect::<Vec<_>>();
    
  let start = &problem.conjecture.1;
  let end = &problem.conjecture.2;
  let mut runner = Runner::default().with_explanations_enabled()
    .with_expr(&start)
    .with_expr(&end);
  runner = problem.axioms_as_roots.iter().fold(runner, |r, e| r.with_expr(e));
  runner = runner.run(rules.iter().collect::<Vec<_>>());
  let e = runner.explain_equivalence(&start, &end);
  e
}

pub fn tptp_problem_to_tptp_solution(path: &str, output: &str) -> () {
  let problem = parse_tptp_problem(path);
  let mut proof = solve_tptp_problem(&problem);
  let expl = proof.make_flat_explanation();

  let rules_names = problem.axioms.iter().map(|axiom| axiom.0.clone()).collect::<Vec<String>>();
  let map = |s: String| rules_names.iter().position(|r| *r == s).expect(format!("Rule not found: {}", s).as_str()) as i32;

  let res = proof_to_tptp(&problem.header, expl, Vec::new(), &problem.string_rules, &rules_names, map);
  let mut file = std::fs::File::create(output).unwrap();
  use std::io::Write;
  file.write_all(res.as_bytes()).unwrap();
}


fn get_head_logic<'a>(frm: &fof::LogicFormula<'a>, res: &mut fof::LogicFormula<'a> ) -> () {
  use fof::LogicFormula::*;
  match frm {
    Binary(_) => *res = frm.clone(),
    Unary(_) => *res = frm.clone(),
    Unitary(u) => get_head_unitary(u, res),
  }
}


fn get_head_unitary<'a>(frm: &fof::UnitaryFormula<'a>, res: &mut fof::LogicFormula<'a>) -> () {
  use fof::UnitaryFormula::*;
  match frm {
    Parenthesised(flf) => get_head_logic(&*flf, res),
    Quantified(_) => *res = fof::LogicFormula::Unitary(frm.clone()),
    Atomic(_) => *res = fof::LogicFormula::Unitary(frm.clone()),
  }
}



mod non_patern_translator {
  use egg::*;
  use tptp::fof;
  use tptp::top;
  use tptp::common::*;
  use tptp::TPTPIterator;

  pub trait TranslatorSL<T> {
    fn translate(tm: &T, expr: &mut RecExpr<SymbolLang>) -> Self;
  }


  impl TranslatorSL<fof::FunctionTerm<'_>> for Id {
    fn translate(tm: &fof::FunctionTerm, expr: &mut RecExpr<SymbolLang>) -> Id {
        use fof::FunctionTerm::*;
        match tm {
            Plain(p) => Self::translate(p, expr),
            Defined(d) => Self::translate(d, expr),
            System(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::DefinedTerm<'_>> for Id {
    fn translate(tm: &fof::DefinedTerm, expr: &mut RecExpr<SymbolLang>) -> Id {
        use fof::DefinedTerm::*;
        match tm {
            Defined(d) => Self::translate(d, expr),
            Atomic(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<tptp::common::DefinedTerm<'_>> for Id {
    fn translate(tm: &tptp::common::DefinedTerm, expr: &mut RecExpr<SymbolLang>) -> Id {
        use tptp::common::DefinedTerm::*;
        match tm {
            Number(n) => expr.add(SymbolLang::leaf(n.to_string())),
            Distinct(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::Term<'_>> for Id {
    fn translate(tm: &fof::Term, expr: &mut RecExpr<SymbolLang>) -> Id {
        use fof::Term::*;
        match tm {
            Variable(v) => expr.add(SymbolLang::leaf(v.to_string())),
            Function(f) => Id::translate(&**f, expr),
        }
    }
  }

  impl TranslatorSL<fof::Arguments<'_>> for Vec<Id> {
    fn translate(args: &fof::Arguments, expr: &mut RecExpr<SymbolLang>) -> Vec<Id> {
      args.0.clone().into_iter().map(move |a: fof::Term<'_>| Id::translate(&a, expr)).collect()
    }
  }

  impl TranslatorSL<fof::PlainTerm<'_>> for Id {
    fn translate(tm: &fof::PlainTerm, expr: &mut RecExpr<SymbolLang>) -> Id {
        use fof::PlainTerm::*;
        match tm {
            Constant(c) => expr.add(SymbolLang::leaf(c.to_string())), //Self::C(c.to_string(), Id::new()),
            Function(f, args) => {
              let ids = Vec::translate(args, expr);
              expr.add(SymbolLang::new(f.to_string(), ids))
            }
        }
    }
  }



  impl TranslatorSL<fof::LogicFormula<'_>> for Id {
    fn translate(frm: &fof::LogicFormula, expr: &mut RecExpr<SymbolLang>) -> Id {
      use fof::LogicFormula::*;
      match frm {
        Binary(b) => Self::translate(b, expr),
        Unary(u) => Self::translate(u, expr),
        Unitary(u) => Self::translate(u, expr),
      }
    }
  }

  impl TranslatorSL<fof::QuantifiedFormula<'_>> for Id {
    fn translate(_frm: &fof::QuantifiedFormula, _expr: &mut RecExpr<SymbolLang>) -> Id {
      todo!();
      /*
      let q = Quantifier::translate(frm.quantifier);
      let vs = frm.bound.0.iter().rev().map(|v| v.to_string());
      vs.fold(Self::translate(*frm.formula), |fm, v| {
          Self::Quant(q, v, Box::new(fm))
      })
      */
    }
  }

  impl TranslatorSL<fof::UnitFormula<'_>> for Id {
    fn translate(frm: &fof::UnitFormula, expr: &mut RecExpr<SymbolLang>) -> Id {
        use fof::UnitFormula::*;
        match frm {
            Unitary(u) => Self::translate(u, expr),
            Unary(u) => Self::translate(u, expr),
        }
    }
  }

  impl TranslatorSL<fof::InfixUnary<'_>> for Id {
    fn translate(frm: &fof::InfixUnary, expr: &mut RecExpr<SymbolLang>) -> Id {
        let lid = Id::translate(&*frm.left, expr);
        let rid = Id::translate(&*frm.right, expr);
        expr.add(SymbolLang::new(frm.op.to_string(), vec![lid, rid]))
    }
  }

  impl TranslatorSL<fof::UnaryFormula<'_>> for Id {
    fn translate(frm: &fof::UnaryFormula, expr: &mut RecExpr<SymbolLang>) -> Id {
      use fof::UnaryFormula::*;
      match frm {
        Unary(op, fuf) => {
          let child = Id::translate(&**fuf, expr);
          expr.add(SymbolLang::new(op.to_string(), vec![child]))
        } InfixUnary(i) => Self::translate(i, expr),
      }
    }
  }

  impl TranslatorSL<fof::BinaryFormula<'_>> for Id {
    fn translate(frm: &fof::BinaryFormula, expr: &mut RecExpr<SymbolLang>) -> Id {
        use fof::BinaryFormula::*;
        match frm {
            Nonassoc(fbn) => Self::translate(fbn, expr),
            Assoc(fba) => Self::translate(fba, expr),
        }
    }
  }

  impl TranslatorSL<fof::BinaryNonassoc<'_>> for Id {
    fn translate(frm: &fof::BinaryNonassoc, expr: &mut RecExpr<SymbolLang>) -> Id {
        let lid = Id::translate(&*frm.left, expr);
        let rid = Id::translate(&*frm.right, expr);
        expr.add(SymbolLang::new(frm.op.to_string(), vec![lid, rid]))
    }
  }

  impl TranslatorSL<fof::BinaryAssoc<'_>> for Id {
    fn translate(fm: &fof::BinaryAssoc, expr: &mut RecExpr<SymbolLang>) -> Id {
        use fof::BinaryAssoc::*;
      match fm {
        Or(fms) => {
          let ids = fms.0.clone().into_iter().map(|a| Id::translate(&a, expr)).collect();
          expr.add(SymbolLang::new("or".to_string(), ids))
        }
        And(fms) => {
          let ids = fms.0.clone().into_iter().map(|a| Id::translate(&a, expr)).collect();
          expr.add(SymbolLang::new("and".to_string(), ids))
        }
      }
    }
  }

  impl TranslatorSL<fof::Quantifier> for Id {
    fn translate(_q: &fof::Quantifier, _expr: &mut RecExpr<SymbolLang>) -> Id {
      todo!();
      /*
      use fof::Quantifier::*;
      match q {
        Forall => Self::Forall,
        Exists => Self::Exists,
      }
      */
    }
  }

  impl TranslatorSL<fof::UnitaryFormula<'_>> for Id {
    fn translate(frm: &fof::UnitaryFormula, expr: &mut RecExpr<SymbolLang>) -> Id {
      use fof::UnitaryFormula::*;
      match frm {
        Parenthesised(flf) => Self::translate(&**flf, expr),
        Quantified(fqf) => Self::translate(fqf, expr),
        Atomic(a) => Self::translate(&**a, expr),
      }
    }
  }

  impl TranslatorSL<fof::PlainAtomicFormula<'_>> for Id {
    fn translate(frm: &fof::PlainAtomicFormula, expr: &mut RecExpr<SymbolLang>) -> Id {
      use fof::PlainTerm::*;
      match &frm.0 {
        Constant(c) => expr.add(SymbolLang::leaf(c.to_string())),
        Function(f, args) => {
          let ids = Vec::translate(&*args, expr);
          expr.add(SymbolLang::new(f.to_string(), ids))
        }
      }
    }
  }
  

  impl TranslatorSL<fof::DefinedAtomicFormula<'_>> for Id {
    fn translate(frm: &fof::DefinedAtomicFormula, expr: &mut RecExpr<SymbolLang>) -> Id {
        use fof::DefinedAtomicFormula::*;
        match frm {
            Plain(p) => Self::translate(p, expr),
            Infix(i) => {
              let left = Id::translate(&*i.left, expr);
              let right = Id::translate(&*i.right, expr);
              expr.add(SymbolLang::new(i.op.to_string(), vec![left, right]))
            }
        }
        
    }
  }

  impl TranslatorSL<fof::DefinedPlainFormula<'_>> for Id {
    fn translate(fm: &fof::DefinedPlainFormula, expr: &mut RecExpr<SymbolLang>) -> Id {
        use fof::DefinedPlainTerm::Constant;
        match &fm.0 {
            Constant(c) if c.0 .0 .0 .0 .0 == "true" => 
              expr.add(SymbolLang::leaf("$true".to_string())),
            Constant(c) if c.0 .0 .0 .0 .0 == "false" => 
              expr.add(SymbolLang::leaf("$false".to_string())),
            _ => todo!("only supported defined formulas are $true and $false"),
        }
    }
  }

  impl TranslatorSL<fof::AtomicFormula<'_>> for Id {
    fn translate(frm: &fof::AtomicFormula, expr: &mut RecExpr<SymbolLang>) -> Id {
        use fof::AtomicFormula::*;
        match frm {
            Plain(p) => Self::translate(p, expr),
            Defined(d) => Self::translate(d, expr),
            System(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::Formula<'_>> for Id {
    fn translate(frm: &fof::Formula, expr: &mut RecExpr<SymbolLang>) -> Id {
      match frm {
        fof::Formula::Logic(l) => Self::translate(l, expr),
        fof::Formula::Sequent(_) => todo!(),
      }
    }
  }
}




mod patern_translator {
  use egg::*;
  use tptp::fof;
  use tptp::top;
  use tptp::common::*;
  use tptp::TPTPIterator;
  use ENodeOrVar::*;

  pub trait TranslatorSL<T> {
    fn translate(tm: &T, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Self;
  }


  impl TranslatorSL<fof::FunctionTerm<'_>> for Id {
    fn translate(tm: &fof::FunctionTerm, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use fof::FunctionTerm::*;
        match tm {
            Plain(p) => Self::translate(p, expr),
            Defined(d) => Self::translate(d, expr),
            System(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::DefinedTerm<'_>> for Id {
    fn translate(tm: &fof::DefinedTerm, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use fof::DefinedTerm::*;
        match tm {
            Defined(d) => Self::translate(d, expr),
            Atomic(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<tptp::common::DefinedTerm<'_>> for Id {
    fn translate(tm: &tptp::common::DefinedTerm, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use tptp::common::DefinedTerm::*;
        match tm {
            Number(n) => expr.add(ENode(SymbolLang::leaf(n.to_string()))),
            Distinct(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::Term<'_>> for Id {
    fn translate(tm: &fof::Term, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use fof::Term::*;
        match tm {
            Variable(v) => expr.add(ENode(SymbolLang::leaf(v.to_string()))),
            Function(f) => Id::translate(&**f, expr),
        }
    }
  }

  impl TranslatorSL<fof::Arguments<'_>> for Vec<Id> {
    fn translate(args: &fof::Arguments, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Vec<Id> {
      args.0.clone().into_iter().map(move |a: fof::Term<'_>| Id::translate(&a, expr)).collect()
    }
  }

  impl TranslatorSL<fof::PlainTerm<'_>> for Id {
    fn translate(tm: &fof::PlainTerm, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use fof::PlainTerm::*;
        match tm {
            Constant(c) => expr.add(ENode(SymbolLang::leaf(c.to_string()))), 
            Function(f, args) => {
              let ids = Vec::translate(args, expr);
              expr.add(ENode(SymbolLang::new(f.to_string(), ids)))
            }
        }
    }
  }



  impl TranslatorSL<fof::LogicFormula<'_>> for Id {
    fn translate(frm: &fof::LogicFormula, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
      use fof::LogicFormula::*;
      match frm {
        Binary(b) => Self::translate(b, expr),
        Unary(u) => Self::translate(u, expr),
        Unitary(u) => Self::translate(u, expr),
      }
    }
  }

  impl TranslatorSL<fof::QuantifiedFormula<'_>> for Id {
    fn translate(_frm: &fof::QuantifiedFormula, _expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
      todo!();
      /*
      let q = Quantifier::translate(frm.quantifier);
      let vs = frm.bound.0.iter().rev().map(|v| v.to_string());
      vs.fold(Self::translate(*frm.formula), |fm, v| {
          Self::Quant(q, v, Box::new(fm))
      })
      */
    }
  }

  impl TranslatorSL<fof::UnitFormula<'_>> for Id {
    fn translate(frm: &fof::UnitFormula, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use fof::UnitFormula::*;
        match frm {
            Unitary(u) => Self::translate(u, expr),
            Unary(u) => Self::translate(u, expr),
        }
    }
  }

  impl TranslatorSL<fof::InfixUnary<'_>> for Id {
    fn translate(frm: &fof::InfixUnary, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        let lid = Id::translate(&*frm.left, expr);
        let rid = Id::translate(&*frm.right, expr);
        expr.add(ENode(SymbolLang::new(frm.op.to_string(), vec![lid, rid])))
    }
  }

  impl TranslatorSL<fof::UnaryFormula<'_>> for Id {
    fn translate(frm: &fof::UnaryFormula, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
      use fof::UnaryFormula::*;
      match frm {
        Unary(op, fuf) => {
          let child = Id::translate(&**fuf, expr);
          expr.add(ENode(SymbolLang::new(op.to_string(), vec![child])))
        } InfixUnary(i) => Self::translate(i, expr),
      }
    }
  }

  impl TranslatorSL<fof::BinaryFormula<'_>> for Id {
    fn translate(frm: &fof::BinaryFormula, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use fof::BinaryFormula::*;
        match frm {
            Nonassoc(fbn) => Self::translate(fbn, expr),
            Assoc(fba) => Self::translate(fba, expr),
        }
    }
  }

  impl TranslatorSL<fof::BinaryNonassoc<'_>> for Id {
    fn translate(frm: &fof::BinaryNonassoc, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        let lid = Id::translate(&*frm.left, expr);
        let rid = Id::translate(&*frm.right, expr);
        expr.add(ENode(SymbolLang::new(frm.op.to_string(), vec![lid, rid])))
    }
  }

  impl TranslatorSL<fof::BinaryAssoc<'_>> for Id {
    fn translate(fm: &fof::BinaryAssoc, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use fof::BinaryAssoc::*;
      match fm {
        Or(fms) => {
          let ids = fms.0.clone().into_iter().map(|a| Id::translate(&a, expr)).collect();
          expr.add(ENode(SymbolLang::new("or".to_string(), ids)))
        }
        And(fms) => {
          let ids = fms.0.clone().into_iter().map(|a| Id::translate(&a, expr)).collect();
          expr.add(ENode(SymbolLang::new("and".to_string(), ids)))
        }
      }
    }
  }

  impl TranslatorSL<fof::Quantifier> for Id {
    fn translate(_q: &fof::Quantifier, _expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
      todo!();
      /*
      use fof::Quantifier::*;
      match q {
        Forall => Self::Forall,
        Exists => Self::Exists,
      }
      */
    }
  }

  impl TranslatorSL<fof::UnitaryFormula<'_>> for Id {
    fn translate(frm: &fof::UnitaryFormula, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
      use fof::UnitaryFormula::*;
      match frm {
        Parenthesised(flf) => Self::translate(&**flf, expr),
        Quantified(fqf) => Self::translate(fqf, expr),
        Atomic(a) => Self::translate(&**a, expr),
      }
    }
  }

  impl TranslatorSL<fof::PlainAtomicFormula<'_>> for Id {
    fn translate(frm: &fof::PlainAtomicFormula, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
      use fof::PlainTerm::*;
      match &frm.0 {
        Constant(c) => expr.add(ENode(SymbolLang::leaf(c.to_string()))),
        Function(f, args) => {
          let ids = Vec::translate(&*args, expr);
          expr.add(ENode(SymbolLang::new(f.to_string(), ids)))
        }
      }
    }
  }

  impl TranslatorSL<fof::DefinedAtomicFormula<'_>> for Id {
    fn translate(frm: &fof::DefinedAtomicFormula, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use fof::DefinedAtomicFormula::*;
        match frm {
            Plain(p) => Self::translate(p, expr),
            Infix(i) => {
              let left = Id::translate(&*i.left, expr);
              let right = Id::translate(&*i.right, expr);
              expr.add(ENode(SymbolLang::new(i.op.to_string(), vec![left, right])))
            }
        }
        
    }
  }

  impl TranslatorSL<fof::DefinedPlainFormula<'_>> for Id {
    fn translate(fm: &fof::DefinedPlainFormula, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use fof::DefinedPlainTerm::Constant;
        match &fm.0 {
            Constant(c) if c.0 .0 .0 .0 .0 == "true" => 
              expr.add(ENode(SymbolLang::leaf("$true".to_string()))),
            Constant(c) if c.0 .0 .0 .0 .0 == "false" => 
              expr.add(ENode(SymbolLang::leaf("$false".to_string()))),
            _ => todo!("only supported defined formulas are $true and $false"),
        }
    }
  }

  impl TranslatorSL<fof::AtomicFormula<'_>> for Id {
    fn translate(frm: &fof::AtomicFormula, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
        use fof::AtomicFormula::*;
        match frm {
            Plain(p) => Self::translate(p, expr),
            Defined(d) => Self::translate(d, expr),
            System(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::Formula<'_>> for Id {
    fn translate(frm: &fof::Formula, expr: &mut RecExpr<ENodeOrVar<SymbolLang>>) -> Id {
      match frm {
        fof::Formula::Logic(l) => Self::translate(l, expr),
        fof::Formula::Sequent(_) => todo!(),
      }
    }
  }
}



