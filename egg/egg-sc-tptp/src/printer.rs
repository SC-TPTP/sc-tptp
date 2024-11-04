use crate::translator::*;
use egg::{*, rewrite as rw};
use std::io::Read;
use std::ops::Index;
use tptp::TPTPIterator;
use tptp::fof;
use tptp::top;
use tptp::common::*;

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

pub fn expr_to_tptp_hole(expr: &FlatTerm<egg::SymbolLang>) -> (String, Option<String>) {
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
}

pub fn line_to_tptp<F>(line: &FlatTerm<egg::SymbolLang>, i: i32, base: &String, left: &String, map: F) -> String where F: Fn(String) -> i32 {
  let (with_hole, _rule) = expr_to_tptp_hole(line);
  let full = expr_to_tptp_res(line);
  format!("fof(f{}, plain, [{}] --> [{} = {}], inference(rightSubstEq, param({}, $fof({} = {}), $fot(HOLE)), [f{}])).", i, left, base, full, map(_rule.unwrap()), base, with_hole, i-1)
}

pub fn make_cut_line<F>(left: &String, res: &String, rule_name: &String, i: i32, map: F) -> String where F: Fn(String) -> i32 {
  format!("fof(f{}, plain, [{}] --> [{}], inference(cut, param(0, {}), [{}, f{}])).", i, left, res, map((&rule_name).to_string()), rule_name, i-1)
}



pub fn proof_to_tptp<F>(header: &String, proof: &Vec<FlatTerm<egg::SymbolLang>>, left: Vec<(String, String)>, rules: &Vec<(String, String)>, rules_names: &Vec<String>, map: F) -> String where F: Fn(String) -> i32 {
  assert!(rules.len() == rules_names.len());
  let left_string = &left.iter().map(|rule| {
    format!("{} = {}", rule.0, rule.1)
  }).collect::<Vec<String>>().join(", ");
  let newleft = left_string.to_owned() + (if left.is_empty() {""} else {", "}) + 
  &rules.iter().map(|rule| {
    format!("{} = {}", rule.0, rule.1)
  }).collect::<Vec<String>>().join(", ");
  let base = expr_to_tptp_res(&proof[0]);
  let first = format!("fof(f0, plain, [{}] --> [{} = {}], inference(rightRefl, param(0), [])).\n", newleft, base, base);
  
  let mut i = 1;
  let mut proofstring = header.to_owned() + "\n" + &first + &proof.iter().skip(1).map( |line| {
    let res = line_to_tptp(line, i, &base, &newleft, &map);
    i += 1;
    res
  }
).collect::<Vec<String>>().join("\n");

let final_res = base.to_owned() + " = " + &expr_to_tptp_res(&proof[proof.len()-1]);
let mut n = rules.len();
while n > 0 {
  let newrules = rules.iter().take(n-1);
  let newleft = left_string.to_owned() + (if left.is_empty() {""} else {", "}) + 
  &newrules.map(|rule| {
    format!("{} = {}", rule.0, rule.1)
  }).collect::<Vec<String>>().join(", ");
  
  let res = make_cut_line(&newleft, &final_res, &rules_names[n-1], i, &map);
  proofstring = proofstring + "\n" + &res;
  i += 1;
  n -= 1;
}
proofstring

}