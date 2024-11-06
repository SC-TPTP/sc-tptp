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

pub fn as_string(v: &Vec<FlatTerm<egg::SymbolLang>>) -> String {
  let strs: Vec<String> = v.iter()
  .map(|e| format!("{:?}", e))
  .collect();
  strs.join("\n")
}

pub fn get_flat_string(v: &Vec<FlatTerm<egg::SymbolLang>>) -> String {
  v.iter()
  .map(|e| e.to_string())
  .collect::<Vec<String>>()
  .join("\n")
}

pub fn expr_to_tptp_res(expr: &FlatTerm<egg::SymbolLang>) -> String {
  let head = expr.node.op;
  if expr.children.is_empty() {
    format!("{}", head)
  } else {
    let args = expr.children.iter().map(|e| expr_to_tptp_res(e)).collect::<Vec<String>>().join(", ");
    format!("{}({})", head, args)
  }
}
  /*
pub fn expr_to_tptp_hole(expr: &FlatTerm<egg::SymbolLang>) -> (String, Option<String>) {
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

pub fn instantiate(expr: &FlatTerm<egg::SymbolLang>, v: &str, t: &FlatTerm<egg::SymbolLang>) -> FlatTerm<egg::SymbolLang> {
  if expr.node.op.as_str() == v && expr.children.is_empty() {
    return t.clone();
  }
  else {FlatTerm { 
    node: expr.node.clone(),
    backward_rule: None,
    forward_rule: None,
    children: expr.children.iter().map(|e| instantiate(e, v, t)).collect() } 
  } 
}

pub fn matching(expr: &FlatTerm<egg::SymbolLang>, expr2: &FlatTerm<egg::SymbolLang>, map: &mut HashMap<&str, FlatTerm<egg::SymbolLang>>) -> bool {
  let e_head = expr.node.op.as_str();
  if expr.node.op == expr2.node.op && expr.children.len() == expr2.children.len() {
    let res = expr.children.iter().zip(expr2.children.iter())
      .all(|(e1, e2)| matching(e1, e2, map));
    res
  }
  else if expr.children.is_empty() {
    if map.contains_key(e_head) {
      return map[e_head] == *expr2;
    }
    else {
      map.insert(expr.node.op.as_str(), expr2.clone());
      return true;
    }
  }
  else {
    false
  }
}
pub struct HolesRes {with_holes: FlatTerm<egg::SymbolLang>, rule: Option<(FlatTerm<egg::SymbolLang>, bool, String)>}

pub fn rewrites_to_holes(expr: &FlatTerm<egg::SymbolLang>, hole: Symbol) -> HolesRes {
  if expr.backward_rule.is_some() {
    let res = FlatTerm::<egg::SymbolLang> {
      node: SymbolLang::leaf(hole),
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
    let res = FlatTerm::<egg::SymbolLang> {
      node: SymbolLang::leaf(hole),
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
    let with_holes = (FlatTerm::<egg::SymbolLang> {
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

pub fn line_to_tptp<F>(line: &FlatTerm<egg::SymbolLang>, i: &mut i32, base: &String, left: &String, map_rule: F) -> String where F: Fn(String) -> (Vec<String>, FlatTerm<egg::SymbolLang>, FlatTerm<egg::SymbolLang>) {
  let line_to_holes = rewrites_to_holes(line, "HOLE".into());
  let with_hole = line_to_holes.with_holes;
  let _rule = line_to_holes.rule;
  let (inner, backward, rule_name) = _rule.unwrap();
  let res = line.clone().remove_rewrites();
  let (variables, rule_left, rule_right) = map_rule(rule_name.clone());
  let res_string = if backward {
    let mut match_map = HashMap::new();
    if matching(&rule_left, &inner, &mut match_map) {
      let newleft = format!("{} = {}", rule_left, rule_right) + (if left.is_empty() {""} else {", "}) + left;
      let rule_repr = format!("{} = {}", rule_left, rule_right);  
      let mut vars = "".to_owned();
      //let left_with_quantified = rule_repr + (if left.is_empty() {""} else {", "}) + left;
      format!("fof(f{i}, plain, [{newleft}] --> [{base} = {res}], inference(rightSubstEq, param(0, $fof({base} = {with_hole}), $fot(HOLE)), [f{}])).\n", *i-1) + 
      variables.iter().map(|v| {
        let inst_term = match_map.get(v as &str).map(|t| t.to_string()).unwrap_or(v.to_owned());
        vars.insert_str(0, (v.to_owned() + if vars.is_empty() {""} else {", "}).as_str());
        let s = format!("fof(f{}, plain, [![{vars}]: {rule_repr}] --> [{base} = {res}], inference(leftForall, param(0, $fot({inst_term})), [f{i}])).\n", *i+1); //TODO: specialize v to the result of the matching
        *i+=1;
        s
      }).collect::<Vec<String>>().join("").as_str() +
      format!("fof(f{}, plain, [{left}] --> [{base} = {res}], inference(cut, param(0, 0), [{rule_name}, f{}])).", *i+1, *i+2).as_str()
    } else {
      panic!("Error: {} does not match {}", expr_to_tptp_res(&rule_left), expr_to_tptp_res(&res));
    }
  }
  else {
    let mut match_map = HashMap::new();
    if matching(&rule_right, &inner, &mut match_map) {
      let newleft = format!("{} = {}", rule_left, rule_right) + (if left.is_empty() {""} else {", "}) + left;
      let rule_repr = format!("{} = {}", rule_left, rule_right);  
      let mut vars = "".to_owned();
      //let left_with_quantified = rule_repr + (if left.is_empty() {""} else {", "}) + left;
      format!("fof(f{i}, plain, [{newleft}] --> [{base} = {res}], inference(rightSubstEq, param(0, $fof({base} = {with_hole}), $fot(HOLE)), [f{}])).\n", *i-1) + 
      variables.iter().map(|v| {
        let inst_term = match_map.get(v as &str).map(|t| t.to_string()).unwrap_or(v.to_owned());
        vars.insert_str(0, (v.to_owned() + if vars.is_empty() {""} else {", "}).as_str());
        let s = format!("fof(f{}, plain, [![{vars}]: {rule_repr}] --> [{base} = {res}], inference(leftForall, param(0, $fot({inst_term})), [f{i}])).\n", *i+1); //TODO: specialize v to the result of the matching
        *i+=1;
        s
      }).collect::<Vec<String>>().join("").as_str() +
      format!("fof(f{}, plain, [{left}] --> [{base} = {res}], inference(cut, param(0, 0), [{rule_name}, f{}])).", *i+1, *i+2).as_str()
    } else {
      panic!("Error: {} does not match {}", expr_to_tptp_res(&rule_right), expr_to_tptp_res(&res));
    }
  };
  *i += 2;
  res_string
}

pub fn make_cut_line<F>(left: &String, res: &String, rule_name: &String, i: i32, map: F) -> String where F: Fn(String) -> i32 {
  format!("fof(f{}, plain, [{}] --> [{}], inference(cut, param(0, {}), [{}, f{}])).", i, left, res, map((&rule_name).to_string()), rule_name, i-1)
}



pub fn proof_to_tptp(header: &String, proof: &Vec<FlatTerm<SymbolLang>>, axioms: &Vec<(Vec<String>, FlatTerm<SymbolLang>, FlatTerm<SymbolLang>)>, problem: &TPTPProblem) -> String {
  let rules_names = problem.axioms.iter().map(|axiom| axiom.0.clone()).collect::<Vec<String>>();
  let map_rule = |s: String| {
    let rule = axioms.iter().zip(&rules_names).find(|axiom| *axiom.1 == s).expect(format!("Rule not found: {}", s).as_str());
    (rule.0.0.clone(), rule.0.1.clone(), rule.0.2.clone())
  };
  let left: Vec<(String, String)> = Vec::new();
  let left_string = &left.iter().map(|rule| {
    format!("{} = {}", rule.0, rule.1)
  }).collect::<Vec<String>>().join(", ");

  let base = expr_to_tptp_res(&proof[0]);
  let first = format!("fof(f0, plain, [] --> [{base} = {base}], inference(rightRefl, param(0), [])).\n");
  let mut i = 1;

  let proofstring = header.to_owned() + "\n" + &first + &proof.iter().skip(1).map( |line| {
    let res = line_to_tptp(line, &mut i, &base, &left_string, &map_rule);
    i += 1;
    res
  }).collect::<Vec<String>>().join("\n");

  /*
  let final_res = base.to_owned() + " = " + &expr_to_tptp_res(&proof[proof.len()-1]);
  let mut n = axioms.len();
  while n > 0 {
    let newrules = axioms.iter().take(n-1);
    let newleft = left_string.to_owned() + (if left.is_empty() {""} else {", "}) + 
    &newrules.map(|rule| {
      format!("{} = {}", rule.0, rule.1)
    }).collect::<Vec<String>>().join(", ");
    //let res = make_cut_line(&newleft, &final_res, &rules_names[n-1], i, &map_index);
    //proofstring = proofstring + "\n" + &res;
    n -= 1;
  }
    */
  proofstring

}

pub struct TPTPProblem {
  pub path: String,
  pub header: String,
  pub axioms: Vec<(String, RecExpr<ENodeOrVar<SymbolLang>>, RecExpr<ENodeOrVar<SymbolLang>>)>,
  pub axioms_as_roots: Vec<(Vec<String>, RecExpr<SymbolLang>, RecExpr<SymbolLang>)>,
  pub conjecture: (String, RecExpr<SymbolLang>, RecExpr<SymbolLang>),
  pub string_rules: Vec<(String, String)>,
}