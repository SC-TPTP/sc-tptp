
use egg::{*, rewrite as rw};
use nom::InputLength;
use tptp::top::AnnotatedFormula;
use core::panic;
use core::time;
use std::fmt::format;
use std::io::Read;
use tptp::fof;
use tptp::top;
use tptp::common::*;
use tptp::TPTPIterator;
use ENodeOrVar::*;

use crate::fol;
use fol::FOLLang;

use crate::printer::*;

//function that ready translate a file with path 'path' and then calls TPTPIterator::<()>::new(bytes) on it
pub fn take_input(path: &std::path::PathBuf) -> Vec<u8> {
  let mut file = std::fs::File::open(path).unwrap();
  let mut bytes = Vec::new();
  file.read_to_end(&mut bytes).unwrap();
  bytes
  
}


use tptp::*;
use nom::branch::alt;
use nom::bytes::streaming::{
  escaped, tag, take_until, take_while, take_while1,
};
use nom::character::complete::{multispace1, space0, line_ending, not_line_ending, one_of,
};
use nom::combinator::{map, opt, recognize, value, eof, complete};
use nom::multi::fold_many0;
use nom::sequence::{delimited, pair, preceded, terminated,  tuple};

//derive Display 
impl std::fmt::Display for HeaderLine {
  fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
    match self {
      HeaderLine::Comment(tag, value) => write!(f, "% {tag} : {value}\n"),
      HeaderLine::Whiteline => write!(f, "\n"),
      HeaderLine::Other(o) => write!(f, "%{}", o),
    }
  }
}

fn fmt_with_spaces(line: &HeaderLine, f: &mut std::fmt::Formatter, n: usize) -> std::fmt::Result {
  match line {
    HeaderLine::Comment(tag, value) => {
      // add spaces to tag to  reach len n:
      let new_tag = format!("{tag:n$}");
      write!(f, "% {new_tag} : {value}\n")
    },
    HeaderLine::Whiteline => write!(f, "\n"),
    HeaderLine::Other(o) => write!(f, "{}\n", o),
  }
}

#[derive(Clone)]
#[derive(Debug)]
pub enum HeaderLine {
  Comment(String, String),
  Whiteline,
  Other(String),
}

impl std::fmt::Display for Header {
  fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
    let n = self.comments.iter().map(|l| match l {
      HeaderLine::Comment(tag, _) => tag.len(),
      _ => 0,
    }).max().unwrap_or(0);
    for line in &self.comments {
      fmt_with_spaces(line, f, n)?;
    }
    Ok(())
  }
}
#[derive(Clone)]
pub struct Header {
  comments: Vec<HeaderLine>,
}
pub fn comment_tag<'a, E: Error<'a>>(x: &'a [u8]) -> Result<'a, String, E> {
  use HeaderLine::*;
  map(delimited(preceded(tag("%"), space0), alphanumeric, delimited(space0, tag(":"), space0 )), |s|
  String::from_utf8(s.to_vec()).unwrap())(x)
}

pub fn line_or_file_ending<'a, E: Error<'a>>(x: &'a [u8]) -> Result<'a, (), E> {
  alt((value((), line_ending), value((), eof)))(x)
}


pub fn comment_line<'a, E: Error<'a>>(x: &'a [u8]) -> Result<'a, HeaderLine, E> {
  use HeaderLine::*;
  alt((value(Whiteline, terminated(space0, line_or_file_ending)),
      complete(map(pair(comment_tag,
         delimited(space0,  not_line_ending, line_or_file_ending)), |(tag, s)| {
          Comment(tag.to_string(), String::from_utf8(s.to_vec()).unwrap())
      })),
      map(terminated( not_line_ending, line_or_file_ending), |s: &[u8]|Other(String::from_utf8(s.to_vec()).unwrap())),

    ))(x)
}

pub fn parse_header(mut bytes: &[u8]) -> Header {
  let mut header: Vec<HeaderLine> = Vec::new();
  loop {
    let r    = comment_line::<'_, ()>(bytes);
    
    match r {
      Ok((reminder, comment)) => {
        header.push(comment);
        bytes = reminder;
        if reminder.input_len() == 0 {
          break;
        }
      }
      Err(_) => panic!("Error: parsing header failed")
    }
  }
  let header2 = Header { comments: header };
  header2
}


pub fn parse_tptp_problem(path: &std::path::PathBuf) -> TPTPProblem {
  let bytes = take_input(path);
  let header = parse_header(&bytes.clone());
  let mut parser = TPTPIterator::<()>::new(bytes.as_slice());
  let mut rules: Vec<(String, RecExpr<ENodeOrVar<fol::FOLLang>>, RecExpr<ENodeOrVar<fol::FOLLang>>)> = Vec::new();
  let mut string_rules: Vec<(String, String)> = Vec::new();
  let mut conjecture: (String, RecExpr<fol::FOLLang>, RecExpr<fol::FOLLang>) = ("".to_string(), RecExpr::default(), RecExpr::default());
  let mut axioms_as_roots: Vec<(Vec<String>, RecExpr<fol::FOLLang>, RecExpr<fol::FOLLang>)> = Vec::new();
  let mut left_string: Vec<String> = Vec::new();
  for result in &mut parser {
    match result {
      Ok(r) => {
        match r {
          top::TPTPInput::Annotated(annotated) => {
            use crate::fol::tptp_fol_translator::*;
            let anot_form = fol::AnnotatedStatement::translate(&*annotated);
            let name = anot_form.name;
            let role = anot_form.role;
            let (conditions, main_formula) = match anot_form.statement {
              fol::Statement::Formula(f) => (Vec::<fol::Formula>::new(), f),
              fol::Statement::Sequent(sequent) => {
                let left = &sequent.left;
                let right = &sequent.right;
                if right.len() != 1 {
                  panic!("Axioms and Conjectures must have exactly one formula on the right hand side")
                }
                let f = &right[0];
                (left.clone(), f.clone())
              }
            };
            //let annotations = &anot_form.0.annotations;
            match role.as_str() {
              "conjecture" => {     

                //Handles rewrite rules on the left
                conditions.iter().enumerate().for_each(|(no, c)| {
                  left_string.push(c.to_string());
                  let formula = &mut c.clone();
                  let mut vars =  Vec::<String>::new();
                  get_head_vars_logic(&c, formula, &mut vars);

                  match formula {
                    fol::Formula::Predicate(op, args ) => 
                      if op == "=" && args.len() == 2 {
                        {
                          let mut expr_left: RecExpr<ENodeOrVar<fol::FOLLang>> = RecExpr::default();
                          let mut expr_right: RecExpr<ENodeOrVar<fol::FOLLang>> = RecExpr::default();
                          fol::term_to_recexpr_pattern(&*args[0], &vars, &mut expr_left);
                          fol::term_to_recexpr_pattern(&*args[1], &vars, &mut expr_right);
                          rules.push((format!("${no}"), expr_left, expr_right));
                        }
                        {
                          let mut expr_left: RecExpr<fol::FOLLang> = RecExpr::default();
                          let mut expr_right: RecExpr<fol::FOLLang> = RecExpr::default();
                          fol::term_to_recexpr(&*args[0], &mut expr_left);
                          fol::term_to_recexpr(&*args[1], &mut expr_right);
                          axioms_as_roots.push((vars, expr_left, expr_right));
                        }
                        string_rules.push((args[0].to_string(), args[1].to_string()));
                      } else {},
                    _ => panic!("formulas must be equalities")
                  }
                });



                //Handles the conjecture
                let formula = &mut main_formula.clone();
                get_head_logic(&main_formula, formula);
                match formula {
                  fol::Formula::Predicate(op, args ) => {
                    if op == "=" {
                      let mut expr_left: RecExpr<fol::FOLLang> = RecExpr::default();
                      let mut expr_right: RecExpr<fol::FOLLang> = RecExpr::default();
                      fol::term_to_recexpr(&*args[0], &mut expr_left);
                      fol::term_to_recexpr(&*args[1], &mut expr_right);
                      conjecture = (name, expr_left, expr_right);

                    } else {panic!("formulas must be equalities")}
                  }
                  _ => panic!("formulas must be equalities,")
                }
                
              }
              "axiom" => {
                let formula = &mut main_formula.clone();
                let mut vars =  Vec::<String>::new();
                get_head_vars_logic(&main_formula, formula, &mut vars);
                match formula {
                  fol::Formula::Predicate(op, args ) => {
                    if op == "=" {
                      {
                        let mut expr_left: RecExpr<ENodeOrVar<fol::FOLLang>> = RecExpr::default();
                        let mut expr_right: RecExpr<ENodeOrVar<fol::FOLLang>> = RecExpr::default();
                        fol::term_to_recexpr_pattern(&*args[0], &vars, &mut expr_left);
                        fol::term_to_recexpr_pattern(&*args[1], &vars, &mut expr_right);
                        rules.push((name, expr_left, expr_right));
                      }
                      {
                        let mut expr_left: RecExpr<fol::FOLLang> = RecExpr::default();
                        let mut expr_right: RecExpr<fol::FOLLang> = RecExpr::default();
                        fol::term_to_recexpr(&*args[0], &mut expr_left);
                        fol::term_to_recexpr(&*args[1], &mut expr_right);
                        axioms_as_roots.push((vars, expr_left, expr_right));
                      }
                      string_rules.push((args[0].to_string(), args[1].to_string()));
                    }
                  }
                  _ => panic!("formulas must be equalities unitary")
                }
              }
              _ => ()
            }
          }
          _ => ()
        }
        
      }
      Err(_) => {
        panic!("Error: parsing failed")
      }
    }
  }


  return TPTPProblem {
    path: path.clone(),
    header: header,
    axioms: rules,
    left_string: left_string,
    conjecture: conjecture,
    string_rules: string_rules,
    axioms_as_roots: axioms_as_roots,
    options: Vec::new(),
  }
}


pub fn solve_tptp_problem(problem: &TPTPProblem) -> Explanation<FOLLang> {
  let rules: Vec<Rewrite<FOLLang, ()>> = problem.axioms.iter().map(|(name, l, r)| {
    Rewrite::<FOLLang, ()>::new(name, egg::Pattern::new(l.clone()), egg::Pattern::new(r.clone())).expect("failed to create rewrite rule")
  }).collect::<Vec<_>>();
    
  let start = &problem.conjecture.1;
  let end = &problem.conjecture.2;
  
  let mut runner: Runner<FOLLang, ()> = Runner::default().with_explanations_enabled()
    .with_expr(&start)
    .with_expr(&end);
  if problem.options.len() >= 2 && problem.options[0] == "--time-limit" {
    let time_limit = problem.options[1].parse::<u64>().expect("time limit must be a number");
    runner = runner.with_time_limit(std::time::Duration::from_secs(time_limit));
    println!("Time limit set to {} seconds", time_limit); 
  }
  runner = problem.axioms_as_roots.iter().fold(runner, |r, e| r.with_expr(&e.1).with_expr(&e.2));
  runner = runner.run(&rules);
  let e = runner.explain_equivalence(&start, &end);
  e
}

pub fn tptp_problem_to_tptp_solution(path: &std::path::PathBuf, output: &std::path::PathBuf, level1:bool) -> () {
  let mut problem: TPTPProblem = parse_tptp_problem(path);
  let mut newcomments = Vec::<HeaderLine>::new();
  let contains_solver = problem.header.comments.iter().any(|l| match l {
    HeaderLine::Comment(tag, _) => tag == "Solver",
    _ => false,
  });
  problem.header.comments.iter().for_each(|l| match l {
    HeaderLine::Comment(tag, value) => {
      if tag == "EggOptions" {
        newcomments.push(l.clone());
          let mut opts: Vec<String> = value.split_whitespace().map(|v| v.to_string()).collect(); 
          problem.options.append(&mut opts);
      } else if tag == "Status" {
        newcomments.push(HeaderLine::Comment(tag.clone(), "Theorem".to_string()));
      } else if tag == "Comments" && !contains_solver {
        newcomments.push(l.clone());
        newcomments.push(HeaderLine::Comment("Solver".to_string(), format!("egg v0.9.5, egg-sc-tptp v{}", env!("CARGO_PKG_VERSION"))));
      } else if tag == "Solver" {
        newcomments.push(HeaderLine::Comment("Solver".to_string(), format!("egg v0.9.5, egg-sc-tptp v{}", env!("CARGO_PKG_VERSION"))));
      } else {
        newcomments.push(HeaderLine::Comment(tag.clone(), value.clone()));
      }
    },
    _ => newcomments.push(l.clone()),
  });
  let newheader = Header { comments: newcomments };

  let init = format!("{}", newheader);
  let mut proof = solve_tptp_problem(&problem);
  let expl = proof.make_flat_explanation();
  let axioms: Vec<(Vec<String>, FlatTerm<FOLLang>, FlatTerm<FOLLang>)> = problem.axioms_as_roots.iter().map(|(vars, l, r)| {
    let mut runner1: Runner<FOLLang, ()> = Runner::default().with_explanations_enabled().with_expr(&l).run(&[]);
    let mut runner2: Runner<FOLLang, ()> = Runner::default().with_explanations_enabled().with_expr(&l).run(&[]);
    let left_ft = runner1.explain_equivalence(&l, &l).make_flat_explanation()[0].clone();
    let right_ft = runner2.explain_equivalence(&r, &r).make_flat_explanation()[0].clone();
  (vars.clone(), left_ft, right_ft)
  }).collect();

  let res = proof_to_tptp(&init, expl, &axioms, &problem, level1);
  let mut file = std::fs::File::create(output).unwrap();
  use std::io::Write;
  file.write_all(res.as_bytes()).unwrap();
}


fn get_head_logic<'a>(frm: &fol::Formula, res: &mut fol::Formula ) -> () {
  use fol::Formula::*;
  match frm {
    Forall(_, inner) => {
      get_head_logic(&*inner, res)
    },
    _ => *res = frm.clone(),
  }
}

fn get_head_vars_logic<'a>(frm: &fol::Formula, res_f: &mut fol::Formula, res_v: &mut Vec<String> ) -> () {
  use fol::Formula::*;
  match frm {
    Forall(variables, inner) => {
      res_v.append(&mut variables.clone());
      get_head_vars_logic(&*inner, res_f, res_v)
    },
    _ => *res_f = frm.clone(),
  }
}



/*
mod non_patern_translator {
  use egg::*;
  use tptp::fof;
  use tptp::top;
  use tptp::common::*;
  use tptp::TPTPIterator;

  pub trait TranslatorSL<T> {
    fn translate(tm: &T, expr: &mut RecExpr<FOLLang>) -> Self;
  }


  impl TranslatorSL<fof::FunctionTerm<'_>> for Id {
    fn translate(tm: &fof::FunctionTerm, expr: &mut RecExpr<FOLLang>) -> Id {
        use fof::FunctionTerm::*;
        match tm {
            Plain(p) => Self::translate(p, expr),
            Defined(d) => Self::translate(d, expr),
            System(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::DefinedTerm<'_>> for Id {
    fn translate(tm: &fof::DefinedTerm, expr: &mut RecExpr<FOLLang>) -> Id {
        use fof::DefinedTerm::*;
        match tm {
            Defined(d) => Self::translate(d, expr),
            Atomic(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<tptp::common::DefinedTerm<'_>> for Id {
    fn translate(tm: &tptp::common::DefinedTerm, expr: &mut RecExpr<FOLLang>) -> Id {
        use tptp::common::DefinedTerm::*;
        match tm {
            Number(n) => expr.add(FOLLang::leaf(n.to_string())),
            Distinct(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::Term<'_>> for Id {
    fn translate(tm: &fof::Term, expr: &mut RecExpr<FOLLang>) -> Id {
        use fof::Term::*;
        match tm {
            Variable(v) => expr.add(FOLLang::leaf(v.to_string())),
            Function(f) => Id::translate(&**f, expr),
        }
    }
  }

  impl TranslatorSL<fof::Arguments<'_>> for Vec<Id> {
    fn translate(args: &fof::Arguments, expr: &mut RecExpr<FOLLang>) -> Vec<Id> {
      args.0.clone().into_iter().map(move |a: fof::Term<'_>| Id::translate(&a, expr)).collect()
    }
  }

  impl TranslatorSL<fof::PlainTerm<'_>> for Id {
    fn translate(tm: &fof::PlainTerm, expr: &mut RecExpr<FOLLang>) -> Id {
        use fof::PlainTerm::*;
        match tm {
            Constant(c) => expr.add(FOLLang::leaf(c.to_string())), //Self::C(c.to_string(), Id::new()),
            Function(f, args) => {
              let ids = Vec::translate(args, expr);
              expr.add(FOLLang::new(f.to_string(), ids))
            }
        }
    }
  }



  impl TranslatorSL<fof::LogicFormula<'_>> for Id {
    fn translate(frm: &fof::LogicFormula, expr: &mut RecExpr<FOLLang>) -> Id {
      use fof::LogicFormula::*;
      match frm {
        Binary(b) => Self::translate(b, expr),
        Unary(u) => Self::translate(u, expr),
        Unitary(u) => Self::translate(u, expr),
      }
    }
  }

  impl TranslatorSL<fof::QuantifiedFormula<'_>> for Id {
    fn translate(_frm: &fof::QuantifiedFormula, _expr: &mut RecExpr<FOLLang>) -> Id {
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
    fn translate(frm: &fof::UnitFormula, expr: &mut RecExpr<FOLLang>) -> Id {
        use fof::UnitFormula::*;
        match frm {
            Unitary(u) => Self::translate(u, expr),
            Unary(u) => Self::translate(u, expr),
        }
    }
  }

  impl TranslatorSL<fof::InfixUnary<'_>> for Id {
    fn translate(frm: &fof::InfixUnary, expr: &mut RecExpr<FOLLang>) -> Id {
        let lid = Id::translate(&*frm.left, expr);
        let rid = Id::translate(&*frm.right, expr);
        expr.add(FOLLang::new(frm.op.to_string(), vec![lid, rid]))
    }
  }

  impl TranslatorSL<fof::UnaryFormula<'_>> for Id {
    fn translate(frm: &fof::UnaryFormula, expr: &mut RecExpr<FOLLang>) -> Id {
      use fof::UnaryFormula::*;
      match frm {
        Unary(op, fuf) => {
          let child = Id::translate(&**fuf, expr);
          expr.add(FOLLang::new(op.to_string(), vec![child]))
        } InfixUnary(i) => Self::translate(i, expr),
      }
    }
  }

  impl TranslatorSL<fof::BinaryFormula<'_>> for Id {
    fn translate(frm: &fof::BinaryFormula, expr: &mut RecExpr<FOLLang>) -> Id {
        use fof::BinaryFormula::*;
        match frm {
            Nonassoc(fbn) => Self::translate(fbn, expr),
            Assoc(fba) => Self::translate(fba, expr),
        }
    }
  }

  impl TranslatorSL<fof::BinaryNonassoc<'_>> for Id {
    fn translate(frm: &fof::BinaryNonassoc, expr: &mut RecExpr<FOLLang>) -> Id {
        let lid = Id::translate(&*frm.left, expr);
        let rid = Id::translate(&*frm.right, expr);
        expr.add(FOLLang::new(frm.op.to_string(), vec![lid, rid]))
    }
  }

  impl TranslatorSL<fof::BinaryAssoc<'_>> for Id {
    fn translate(fm: &fof::BinaryAssoc, expr: &mut RecExpr<FOLLang>) -> Id {
        use fof::BinaryAssoc::*;
      match fm {
        Or(fms) => {
          let ids = fms.0.clone().into_iter().map(|a| Id::translate(&a, expr)).collect();
          expr.add(FOLLang::new("or".to_string(), ids))
        }
        And(fms) => {
          let ids = fms.0.clone().into_iter().map(|a| Id::translate(&a, expr)).collect();
          expr.add(FOLLang::new("and".to_string(), ids))
        }
      }
    }
  }

  impl TranslatorSL<fof::Quantifier> for Id {
    fn translate(_q: &fof::Quantifier, _expr: &mut RecExpr<FOLLang>) -> Id {
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
    fn translate(frm: &fof::UnitaryFormula, expr: &mut RecExpr<FOLLang>) -> Id {
      use fof::UnitaryFormula::*;
      match frm {
        Parenthesised(flf) => Self::translate(&**flf, expr),
        Quantified(fqf) => Self::translate(fqf, expr),
        Atomic(a) => Self::translate(&**a, expr),
      }
    }
  }

  impl TranslatorSL<fof::PlainAtomicFormula<'_>> for Id {
    fn translate(frm: &fof::PlainAtomicFormula, expr: &mut RecExpr<FOLLang>) -> Id {
      use fof::PlainTerm::*;
      match &frm.0 {
        Constant(c) => expr.add(FOLLang::leaf(c.to_string())),
        Function(f, args) => {
          let ids = Vec::translate(&*args, expr);
          expr.add(FOLLang::new(f.to_string(), ids))
        }
      }
    }
  }
  

  impl TranslatorSL<fof::DefinedAtomicFormula<'_>> for Id {
    fn translate(frm: &fof::DefinedAtomicFormula, expr: &mut RecExpr<FOLLang>) -> Id {
        use fof::DefinedAtomicFormula::*;
        match frm {
            Plain(p) => Self::translate(p, expr),
            Infix(i) => {
              let left = Id::translate(&*i.left, expr);
              let right = Id::translate(&*i.right, expr);
              expr.add(FOLLang::new(i.op.to_string(), vec![left, right]))
            }
        }
        
    }
  }

  impl TranslatorSL<fof::DefinedPlainFormula<'_>> for Id {
    fn translate(fm: &fof::DefinedPlainFormula, expr: &mut RecExpr<FOLLang>) -> Id {
        use fof::DefinedPlainTerm::Constant;
        match &fm.0 {
            Constant(c) if c.0 .0 .0 .0 .0 == "true" => 
              expr.add(FOLLang::leaf("$true".to_string())),
            Constant(c) if c.0 .0 .0 .0 .0 == "false" => 
              expr.add(FOLLang::leaf("$false".to_string())),
            _ => todo!("only supported defined formulas are $true and $false"),
        }
    }
  }

  impl TranslatorSL<fof::AtomicFormula<'_>> for Id {
    fn translate(frm: &fof::AtomicFormula, expr: &mut RecExpr<FOLLang>) -> Id {
        use fof::AtomicFormula::*;
        match frm {
            Plain(p) => Self::translate(p, expr),
            Defined(d) => Self::translate(d, expr),
            System(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::Formula<'_>> for Id {
    fn translate(frm: &fof::Formula, expr: &mut RecExpr<FOLLang>) -> Id {
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
    fn translate(tm: &T, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Self;
  }


  impl TranslatorSL<fof::FunctionTerm<'_>> for Id {
    fn translate(tm: &fof::FunctionTerm, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        use fof::FunctionTerm::*;
        match tm {
            Plain(p) => Self::translate(p, allowed_vars, expr),
            Defined(d) => Self::translate(d,  allowed_vars, expr),
            System(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::DefinedTerm<'_>> for Id {
    fn translate(tm: &fof::DefinedTerm, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        use fof::DefinedTerm::*;
        match tm {
            Defined(d) => Self::translate(d, allowed_vars, expr),
            Atomic(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<tptp::common::DefinedTerm<'_>> for Id {
    fn translate(tm: &tptp::common::DefinedTerm, _allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
      use tptp::common::DefinedTerm::*;
      match tm {
        Number(n) => expr.add(ENode(FOLLang::leaf(n.to_string()))),
        Distinct(_) => todo!(),
      }
    }
  }

  impl TranslatorSL<fof::Term<'_>> for Id {
    fn translate(tm: &fof::Term, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        use fof::Term::*;
        match tm {
          Variable(v) => {
            if allowed_vars.contains(&v.to_string()) {
              use std::str::FromStr;
              expr.add(ENodeOrVar::Var(egg::Var::from_str(&format!("?{}", v.to_string())).expect(&format!("incorrect variable name: {}", v.to_string())) ))
            } else {
              expr.add(ENode(FOLLang::leaf(v.to_string())))
            }
          },
          Function(f) => Id::translate(&**f, allowed_vars, expr),
        }
    }
  }

  impl TranslatorSL<fof::Arguments<'_>> for Vec<Id> {
    fn translate(args: &fof::Arguments, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Vec<Id> {
      args.0.clone().into_iter().map(move |a: fof::Term<'_>| Id::translate(&a, allowed_vars, expr)).collect()
    }
  }

  impl TranslatorSL<fof::PlainTerm<'_>> for Id {
    fn translate(tm: &fof::PlainTerm, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        use fof::PlainTerm::*;
        match tm {
            Constant(c) => expr.add(ENode(FOLLang::leaf(c.to_string()))), 
            Function(f, args) => {
              let ids = Vec::translate(args, allowed_vars, expr);
              expr.add(ENode(FOLLang::new(f.to_string(), ids)))
            }
        }
    }
  }



  impl TranslatorSL<fof::LogicFormula<'_>> for Id {
    fn translate(frm: &fof::LogicFormula, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
      use fof::LogicFormula::*;
      match frm {
        Binary(b) => Self::translate(b, allowed_vars, expr),
        Unary(u) => Self::translate(u, allowed_vars, expr),
        Unitary(u) => Self::translate(u, allowed_vars, expr),
      }
    }
  }

  impl TranslatorSL<fof::QuantifiedFormula<'_>> for Id {
    fn translate(_frm: &fof::QuantifiedFormula, _allowed_vars: &Vec<String>, _expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
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
    fn translate(frm: &fof::UnitFormula, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        use fof::UnitFormula::*;
        match frm {
            Unitary(u) => Self::translate(u, allowed_vars, expr),
            Unary(u) => Self::translate(u, allowed_vars, expr),
        }
    }
  }

  impl TranslatorSL<fof::InfixUnary<'_>> for Id {
    fn translate(frm: &fof::InfixUnary, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        let lid = Id::translate(&*frm.left, allowed_vars, expr);
        let rid = Id::translate(&*frm.right, allowed_vars, expr);
        expr.add(ENode(FOLLang::new(frm.op.to_string(), vec![lid, rid])))
    }
  }

  impl TranslatorSL<fof::UnaryFormula<'_>> for Id {
    fn translate(frm: &fof::UnaryFormula, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
      use fof::UnaryFormula::*;
      match frm {
        Unary(op, fuf) => {
          let child = Id::translate(&**fuf, allowed_vars, expr);
          expr.add(ENode(FOLLang::new(op.to_string(), vec![child])))
        } InfixUnary(i) => Self::translate(i, allowed_vars, expr),
      }
    }
  }

  impl TranslatorSL<fof::BinaryFormula<'_>> for Id {
    fn translate(frm: &fof::BinaryFormula, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        use fof::BinaryFormula::*;
        match frm {
            Nonassoc(fbn) => Self::translate(fbn, allowed_vars, expr),
            Assoc(fba) => Self::translate(fba, allowed_vars, expr),
        }
    }
  }

  impl TranslatorSL<fof::BinaryNonassoc<'_>> for Id {
    fn translate(frm: &fof::BinaryNonassoc, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        let lid = Id::translate(&*frm.left, allowed_vars, expr);
        let rid = Id::translate(&*frm.right, allowed_vars, expr);
        expr.add(ENode(FOLLang::new(frm.op.to_string(), vec![lid, rid])))
    }
  }

  impl TranslatorSL<fof::BinaryAssoc<'_>> for Id {
    fn translate(fm: &fof::BinaryAssoc, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        use fof::BinaryAssoc::*;
      match fm {
        Or(fms) => {
          let ids = fms.0.clone().into_iter().map(|a| Id::translate(&a, allowed_vars, expr)).collect();
          expr.add(ENode(FOLLang::new("or".to_string(), ids)))
        }
        And(fms) => {
          let ids = fms.0.clone().into_iter().map(|a| Id::translate(&a, allowed_vars, expr)).collect();
          expr.add(ENode(FOLLang::new("and".to_string(), ids)))
        }
      }
    }
  }

  impl TranslatorSL<fof::Quantifier> for Id {
    fn translate(_q: &fof::Quantifier, _allowed_vars: &Vec<String>, _expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
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
    fn translate(frm: &fof::UnitaryFormula, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
      use fof::UnitaryFormula::*;
      match frm {
        Parenthesised(flf) => Self::translate(&**flf, allowed_vars, expr),
        Quantified(fqf) => Self::translate(fqf, allowed_vars, expr),
        Atomic(a) => Self::translate(&**a, allowed_vars, expr),
      }
    }
  }

  impl TranslatorSL<fof::PlainAtomicFormula<'_>> for Id {
    fn translate(frm: &fof::PlainAtomicFormula, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
      use fof::PlainTerm::*;
      match &frm.0 {
        Constant(c) => expr.add(ENode(FOLLang::leaf(c.to_string()))),
        Function(f, args) => {
          let ids = Vec::translate(&*args, allowed_vars, expr);
          expr.add(ENode(FOLLang::new(f.to_string(), ids)))
        }
      }
    }
  }

  impl TranslatorSL<fof::DefinedAtomicFormula<'_>> for Id {
    fn translate(frm: &fof::DefinedAtomicFormula, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        use fof::DefinedAtomicFormula::*;
        match frm {
            Plain(p) => Self::translate(p, allowed_vars, expr),
            Infix(i) => {
              let left = Id::translate(&*i.left, allowed_vars, expr);
              let right = Id::translate(&*i.right, allowed_vars, expr);
              expr.add(ENode(FOLLang::new(i.op.to_string(), vec![left, right])))
            }
        }
        
    }
  }

  impl TranslatorSL<fof::DefinedPlainFormula<'_>> for Id {
    fn translate(fm: &fof::DefinedPlainFormula, _allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        use fof::DefinedPlainTerm::Constant;
        match &fm.0 {
            Constant(c) if c.0 .0 .0 .0 .0 == "true" => 
              expr.add(ENode(FOLLang::leaf("$true".to_string()))),
            Constant(c) if c.0 .0 .0 .0 .0 == "false" => 
              expr.add(ENode(FOLLang::leaf("$false".to_string()))),
            _ => todo!("only supported defined formulas are $true and $false"),
        }
    }
  }

  impl TranslatorSL<fof::AtomicFormula<'_>> for Id {
    fn translate(frm: &fof::AtomicFormula, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
        use fof::AtomicFormula::*;
        match frm {
            Plain(p) => Self::translate(p, allowed_vars, expr),
            Defined(d) => Self::translate(d, allowed_vars, expr),
            System(_) => todo!(),
        }
    }
  }

  impl TranslatorSL<fof::Formula<'_>> for Id {
    fn translate(frm: &fof::Formula, allowed_vars: &Vec<String>, expr: &mut RecExpr<ENodeOrVar<FOLLang>>) -> Id {
      match frm {
        fof::Formula::Logic(l) => Self::translate(l, allowed_vars, expr),
        fof::Formula::Sequent(_) => todo!(),
      }
    }
  }
}

  */