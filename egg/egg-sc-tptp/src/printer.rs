use crate::translator::*;
use egg::{*, rewrite as rw};
use std::io::Read;
use std::iter::Map;
use std::ops::Index;
use tptp::TPTPIterator;
use tptp::fof;
use tptp::top;
use tptp::common::*;
use std::collections::HashMap;
use crate::fol;
use crate::fol::FOLLang;

pub fn as_string(v: &Vec<FlatTerm<FOLLang>>) -> String {
  let strs: Vec<String> = v.iter()
  .map(|e| format!("{:?}", e))
  .collect();
  strs.join("\n")
}

pub fn get_flat_string(v: &Vec<FlatTerm<FOLLang>>) -> String {
  v.iter()
  .map(|e| e.to_string())
  .collect::<Vec<String>>()
  .join("\n")
}

pub fn expr_to_tptp_res(expr: &FlatTerm<FOLLang>) -> String {
  expr.to_string()
}
  /*
pub fn expr_to_tptp_hole(expr: &FlatTerm<FOLLang>) -> (String, Option<String>) {
  rewrites_to_holes(expr, "HOLE".into()).0.to_string();
  if expr.backward_rule.is_some() {
    ("HOLE".to_owned(), Some(expr.backward_rule.unwrap().to_string().to_owned()))
  } else if expr.forward_rule.is_some() {
    ("HOLE".to_owned(), Some(expr.forward_rule.unwrap().to_string().to_owned()))
  } else {
    let head = expr.node.op;
    if expr.children.is_empty() {
      (format!("{}", head), None)
    } else {
      let args = expr.children.iter().skip(1).fold(expr_to_tptp_hole(&expr.children[0]), |acc, e| {
        let (res, rule) = expr_to_tptp_hole(e);
        (format!("{}, {}", acc.0, res), rule.or(acc.1))
      });
      (format!("{}({})", head, args.0), args.1)
    }
  }
}  */

pub fn flat_term_to_term(expr: &FlatTerm<FOLLang>) -> fol::Term {
  match expr.node {
    FOLLang::Variable(op) => fol::Term::Variable(op.to_string()),
    FOLLang::Function(op, _) => 
      fol::Term::Function(op.to_string(), expr.children.iter().map(|e| Box::new(flat_term_to_term(e))).collect()),
    _ => panic!("{} is not a term", expr.to_string())
  }                           
}
pub fn flat_term_to_formula(expr: &FlatTerm<FOLLang>) -> fol::Formula {
  match expr.node {
    FOLLang::Predicate(op, _) => 
      fol::Formula::Predicate(op.to_string(), expr.children.iter().map(|e| Box::new(flat_term_to_term(e))).collect()),
    FOLLang::Not(_) => fol::Formula::Not(Box::new(flat_term_to_formula(&expr.children[0]))),
    FOLLang::And(_) => fol::Formula::And(expr.children.iter().map(|e| Box::new(flat_term_to_formula(e))).collect()),
    FOLLang::Or(_) => fol::Formula::Or(expr.children.iter().map(|e| Box::new(flat_term_to_formula(e))).collect()),
    FOLLang::Implies(_) => fol::Formula::Implies(Box::new(flat_term_to_formula(&expr.children[0])), Box::new(flat_term_to_formula(&expr.children[1]))),
    FOLLang::Iff(_) => fol::Formula::Iff(Box::new(flat_term_to_formula(&expr.children[0])), Box::new(flat_term_to_formula(&expr.children[1]))),
    _ => panic!("{} is not a formula", expr.to_string())
  }

}

pub fn flat_term_to_term_hole(expr: &FlatTerm<FOLLang>) -> (fol::Term, Option<(fol::Term, bool, String)>) {
  if expr.backward_rule.is_some() {
    (fol::Term::Variable("HOLE".to_owned()), 
     Some((flat_term_to_term(&expr.remove_rewrites()), true, expr.backward_rule.unwrap().to_string().to_owned()))
    )
  } else if expr.forward_rule.is_some() {
    (fol::Term::Variable("HOLE".to_owned()),
     Some((flat_term_to_term(&expr.remove_rewrites()), false, expr.forward_rule.unwrap().to_string().to_owned())))
  } else {
    match expr.node {
      FOLLang::Variable(op) => (fol::Term::Variable(op.to_string()), None),
      FOLLang::Function(op, _) => {
        if expr.children.is_empty() {
          (fol::Term::Function(op.to_string(), vec![]), None)
        } else {
          let first = flat_term_to_term_hole(&expr.children[0]);
          let mut res_vec = vec![Box::new(first.0)];
          let res_rule = expr.children.iter().skip(1).fold(first.1, |acc, e| {
            let res = flat_term_to_term_hole(e);
            res_vec.push(Box::new(res.0));
            res.1.or(acc)
          });
          (fol::Term::Function(op.to_string(), res_vec), res_rule)
        }
      },
      _ => panic!("{} is not a term", expr.to_string())
    }
  }
}

//TODO : flat_term_to_formula_hole




pub struct HolesRes {with_holes: FlatTerm<FOLLang>, rule: Option<(FlatTerm<FOLLang>, bool, String)>}

pub fn rewrites_to_holes(expr: &FlatTerm<FOLLang>, hole: Symbol) -> HolesRes {
  if expr.backward_rule.is_some() {
    let res = FlatTerm::<FOLLang> {
      node: FOLLang::leaf(hole),
      backward_rule: None,
      forward_rule: None,
      children: vec![]
    };
    let expr_without_rewrites = expr.remove_rewrites();
    HolesRes {
      with_holes: res,
      rule: Some((expr_without_rewrites, true, expr.backward_rule.unwrap().to_string().to_owned()))
    }
  } else if expr.forward_rule.is_some() {
    let res = FlatTerm::<FOLLang> {
      node: FOLLang::leaf(hole),
      backward_rule: None,
      forward_rule: None,
      children: vec![]
    };
    let expr_without_rewrites = expr.remove_rewrites();
    HolesRes {
      with_holes: res,
      rule: Some((expr_without_rewrites, false, expr.forward_rule.unwrap().to_string().to_owned()))
    }
  } else {
    let first = rewrites_to_holes(&expr.children[0], hole);
    let mut res_vec = vec![first.with_holes];
    let res_rule = expr.children.iter().skip(1).fold(first.rule, |acc, e| {
      let res = rewrites_to_holes(e, hole);
      res_vec.push(res.with_holes);
      res.rule.or(acc)
    });
    let with_holes = (FlatTerm::<FOLLang> {
      node: expr.node.clone(),
      backward_rule: None,
      forward_rule: None,
      children: res_vec
    }, res_rule);
    HolesRes {
      with_holes: with_holes.0,
      rule: with_holes.1
    }
  }
}

pub fn line_to_tptp_level1<F>(line: &FlatTerm<FOLLang>, i: &mut i32, base: &String, left: &String, map_rule: F) -> String where F: Fn(String) -> (Vec<String>, FlatTerm<FOLLang>, FlatTerm<FOLLang>) {
  let line_to_holes = rewrites_to_holes(line, "HOLE".into());
  let with_hole = expr_to_tptp_res(&line_to_holes.with_holes);
  let _rule = line_to_holes.rule;
  let (inner, backward, rule_name) = _rule.unwrap();
  let is_local_rule: bool = rule_name.starts_with("$");
  let res = expr_to_tptp_res(&line.clone().remove_rewrites());
  let (variables, rule_left, rule_right) = map_rule(rule_name.clone());
  let mut match_map = HashMap::new();
  let has_matched = if backward { matching(&rule_left, &inner, &mut match_map) } else { matching(&rule_right, &inner, &mut match_map) };
  let res_string: String = if has_matched {
    let newleft = format!("{} = {}", expr_to_tptp_res(&instantiate(&rule_left, &match_map)), expr_to_tptp_res(&instantiate(&rule_right, &match_map))) + (if left.is_empty() {""} else {", "}) + left;
    let mut vars = "".to_owned();
    *i+=1; 
    format!("fof(f{i}, plain, [{newleft}] --> [{base} = {res}], inference(rightSubstEq, param(0, $fof({base} = {with_hole}), $fot(HOLE)), [f{}])).\n", *i-1) + 
    variables.iter().enumerate().rev().map(|(nth, v)| {
      let inst_term = match_map.get(v as &str).map(expr_to_tptp_res).unwrap_or(v.to_owned());
      vars.insert_str(0, (v.to_owned() + if vars.is_empty() {""} else {", "}).as_str());
      match_map.remove(&v as &str);
      let new_inner = format!("{} = {}", expr_to_tptp_res(&instantiate(&rule_left, &match_map)), expr_to_tptp_res(&instantiate(&rule_right, &match_map)));
      *i+=1; 
      let new_quant_formula = if is_local_rule && nth == 0 {"".to_owned()} else {format!("![{vars}]: {new_inner}")};
      let forall_no = if is_local_rule && nth == 0 {
        let mut no = rule_name.clone();
        no.remove(0);
        no
      } else {"0".to_owned()};
      let comma = if left.is_empty() || new_quant_formula.is_empty() {"".to_owned()} else {", ".to_owned()};
      let s = format!("fof(f{i}, plain, [{new_quant_formula}{comma}{left}] --> [{base} = {res}], inference(leftForall, param({forall_no}, $fot({inst_term})), [f{}])).\n", *i-1);
      s
    }).collect::<Vec<String>>().join("").as_str() +
    (if !is_local_rule {
      *i+=1; format!("fof(f{i}, plain, [{left}] --> [{base} = {res}], inference(cut, param(0, 0), [{rule_name}, f{}])).", *i-1)
    } else {"".to_string()}).as_str()
  } else {
    panic!("Error: neither {} nor {} does not match {}", expr_to_tptp_res(&rule_left), expr_to_tptp_res(&rule_right), &res);
  };
  res_string
}

pub fn line_to_tptp_level2(line: &FlatTerm<FOLLang>, i: &mut i32, base: &String, left: &String) -> String {
  let line_to_holes = rewrites_to_holes(line, "HOLE".into());
  let with_hole = expr_to_tptp_res(&line_to_holes.with_holes);
  let _rule = line_to_holes.rule;
  let (_, _, rule_name) = _rule.unwrap();
  let is_local_rule: bool = rule_name.starts_with("$");
  let res = expr_to_tptp_res(&line.clone().remove_rewrites());
  *i+=1; 
  if is_local_rule {
    let forall_no = if is_local_rule {
      let mut no = rule_name.clone();
      no.remove(0);
      no
    } else {"0".to_owned()};
    format!("fof(f{i}, plain, [{left}] --> [{base} = {res}], inference(substWithMatchingLocal, param({forall_no}, $fof({base} = {with_hole}), $fot(HOLE)), [f{}])).", *i-1)
  } else {
    format!("fof(f{i}, plain, [{left}] --> [{base} = {res}], inference(substWithMatching, param($fof({base} = {with_hole}), $fot(HOLE)), [{rule_name}, f{}])).", *i-1)
  }
}



pub fn make_cut_line<F>(left: &String, res: &String, rule_name: &String, i: i32, map: F) -> String where F: Fn(String) -> i32 {
  format!("fof(f{}, plain, [{}] --> [{}], inference(cut, param(0, {}), [{}, f{}])).", i, left, res, map((&rule_name).to_string()), rule_name, i-1)
}




pub fn proof_to_tptp(header: &String, proof: &Vec<FlatTerm<FOLLang>>, axioms: &Vec<(Vec<String>, FlatTerm<FOLLang>, FlatTerm<FOLLang>)>, problem: &TPTPProblem, level1:bool) -> String {
  let rules_names = problem.axioms.iter().map(|axiom| axiom.0.clone()).collect::<Vec<String>>();
  let map_rule = |s: String| {

    let rule = 
      axioms.iter().zip(&rules_names).find(|axiom| *axiom.1 == s).expect(format!("Rule not found: {}", s).as_str());
    
    (rule.0.0.clone(), rule.0.1.clone(), rule.0.2.clone())
  };
  let left = &problem.left_string.join(", ");

  let base = expr_to_tptp_res(&proof[0]);
  let first = format!("fof(f0, plain, [{left}] --> [{base} = {base}], inference(rightRefl, param(0), [])).\n");
  let mut i = 0;

  let proofstring = header.to_owned() + "\n" + &first + &proof.iter().skip(1).map( |line| {
    let res =if level1 {
      line_to_tptp_level1(line, &mut i, &base, &left, &map_rule)
    } else {
      line_to_tptp_level2(line, &mut i, &base, &left)
    };
    res
  }).collect::<Vec<String>>().join("\n");
  proofstring

}

pub struct TPTPProblem {
  pub path: std::path::PathBuf,
  pub header: Header,
  pub axioms: Vec<(String, RecExpr<ENodeOrVar<FOLLang>>, RecExpr<ENodeOrVar<FOLLang>>)>,
  pub left_string: Vec<String>,
  pub axioms_as_roots: Vec<(Vec<String>, RecExpr<FOLLang>, RecExpr<FOLLang>)>,
  pub conjecture: (String, RecExpr<FOLLang>, RecExpr<FOLLang>),
  pub string_rules: Vec<(String, String)>,
  pub options: Vec<String>
}