
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
  let mut conjecture: (String, RecExpr<fol::FOLLang>) = ("".to_string(), RecExpr::default());
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
                let mut expr: RecExpr<fol::FOLLang> = RecExpr::default();
                fol::formula_to_recexpr(formula, &mut expr);
                conjecture = (name, expr);
                
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

  let mut top_expr: RecExpr<FOLLang> = RecExpr::default();
  fol::formula_to_recexpr(&fol::Formula::True, &mut top_expr);
    
  let start = &top_expr;
  let end = &problem.conjecture.1;
  
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