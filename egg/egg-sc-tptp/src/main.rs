#![allow(unused_imports)]
#![allow(dead_code)]

mod translator;

use crate::translator::*;
use egg::{*, rewrite as rw};
use std::io::Read;
use tptp::TPTPIterator;
use tptp::fof;
use tptp::top;
use tptp::common::*;




///////////////////
///    Output   ///
///////////////////
fn as_string(v: &Vec<FlatTerm<egg::SymbolLang>>) -> String {
  let strs: Vec<String> = v.iter()
  .map(|e| format!("{:?}", e))
  .collect();
  strs.join("\n")
}

fn get_flat_string(v: &Vec<FlatTerm<egg::SymbolLang>>) -> String {
  v.iter()
  .map(|e| e.to_string())
  .collect::<Vec<String>>()
  .join("\n")
}

fn expr_to_tptp_res(expr: &FlatTerm<egg::SymbolLang>) -> String {
  let head = expr.node.op;
  if expr.children.is_empty() {
    format!("{}", head)
  } else {
    let args = expr.children.iter().map(|e| expr_to_tptp_res(e)).collect::<Vec<String>>().join(", ");
    format!("{}({})", head, args)
  }
}

fn expr_to_tptp_hole(expr: &FlatTerm<egg::SymbolLang>) -> (String, Option<String>) {
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

fn line_to_tptp<F>(line: &FlatTerm<egg::SymbolLang>, i: i32, base: &String, left: &String, map: F) -> String where F: Fn(String) -> i32 {
  let (with_hole, _rule) = expr_to_tptp_hole(line);
  let full = expr_to_tptp_res(line);
  format!("fof(f{}, plain, [{}] --> [{} = {}], inference(rightSubstEq, param({}, $fof({} = {}), $fot(HOLE)), [f{}])).", i, left, base, full, map(_rule.unwrap()), base, with_hole, i-1)
}

fn make_cut_line<F>(left: &String, res: &String, rule_name: &String, i: i32, map: F) -> String where F: Fn(String) -> i32 {
  format!("fof(f{}, plain, [{}] --> [{}], inference(cut, param(0, {}), [{}, f{}])).", i, left, res, map((&rule_name).to_string()), rule_name, i-1)
}



fn proof_to_tptp<F>(header: String, proof: &Vec<FlatTerm<egg::SymbolLang>>, left: Vec<(String, String)>, rules: Vec<(String, String)>, rules_names: Vec<String>, map: F) -> String where F: Fn(String) -> i32 {
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
  let mut proofstring = header + "\n" + &first + &proof.iter().skip(1).map( |line| {
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

///////////////////
///    Input    ///
///////////////////

fn main() {
  println!("Hello, world!");
  
  let rules: &[Rewrite<SymbolLang, ()>] = &[
  rw!("rule8"; "(sf (sf (sf (sf (sf (sf (sf (sf cc))))))))" => "cc"),
  rw!("rule5"; "(sf (sf (sf (sf (sf cc)))))" => "cc"),
  
  ];
  
  
  let expr = "(sf (sf (sf (sf (sf (sf (sf (sf cc))))))))".parse().unwrap();
  let start = "(sf cc)".parse().unwrap();
  let end = "cc".parse().unwrap();
  let mut runner = Runner::default().with_explanations_enabled().with_expr(&expr).run(rules);
  let mut e1 = runner.explain_equivalence(&start, &end);
  let _e1 = e1.make_flat_explanation();
  //println!("{}", as_string(&_e1));
  println!("\n");
  
  
  let rules2: &[Rewrite<SymbolLang, ()>] = &[
  rw!("rule2"; "(sf cc)" => "dd"),
  ];
  
  
  let expr2 = "(sf (sf cc))".parse().unwrap();
  let start2 = "(sf (sf cc))".parse().unwrap();
  let end2 = "(sf dd)".parse().unwrap();
  let mut runner2 = Runner::default().with_explanations_enabled().with_expr(&expr2).run(rules2);
  let mut e2 = runner2.explain_equivalence(&start2, &end2);
  let _e2 = e2.make_flat_explanation();
  println!("{}", as_string(&_e2));
  
  //implement display for Vec<FlatTerm<egg::SymbolLang>>
  
  
  println!("");
  
  
  
  // This is the respresentation "sexpr" of type FlatTerm<egg::SymbolLang
  let _sexpr_proof = vec![
  "(sf cc)",
  "(sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc)))))))",
  "(sf (sf (sf (sf (sf (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc))))))))))))",
  "(sf (sf (sf (Rewrite=> rule8 cc))))",
  "(sf (sf (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc)))))))))",
  "(Rewrite=> rule8 cc)"
  ];
  
  // This is the respresentation "tptp"
  let _tptp_proof = vec![
  "fof(f1, plain, [(sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc), (sf(sf(sf(sf(sf(cc))))) = cc)] --> [sf(cc) = sf(cc)], inference(RightRefl, param(0), [])).", // (sf cc)
  "fof(f2, plain, [(sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc), (sf(sf(sf(sf(sf(cc))))) = cc)] --> [sf(cc) = sf(sf(sf(sf(sf(sf(cc))))))], inference(RightSubstEq, param(1, sf(cc) = sf(Y), Y), [f1])).", // (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc)))))))
  "fof(f3, plain, [(sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc), (sf(sf(sf(sf(sf(cc))))) = cc)] --> [sf(cc) = sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))))))], inference(RightSubstEq, param(1, sf(cc) = sf(sf(sf(sf(sf(sf(Y)))))), Y), [f2])).", // (sf (sf (sf (sf (sf (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc)))))))))))
  "fof(f4, plain, [(sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc), (sf(sf(sf(sf(sf(cc))))) = cc)] --> [sf(cc) = sf(sf(sf(cc)))], inference(RightSubstEq, param(0, sf(cc) = sf(sf(sf(Y))), Y), [f3])).", // (sf (sf (sf (Rewrite=> rule8 cc))))
  "fof(f5, plain, [(sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc), (sf(sf(sf(sf(sf(cc))))) = cc)] --> [sf(cc) = sf(sf(sf(sf(sf(sf(sf(cc)))))))], inference(RightSubstEq, param(1, sf(cc) = sf(sf(sf(Y))), Y), [f4])).", // (sf (sf (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc))))))))
  "fof(f6, plain, [(sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc), (sf(sf(sf(sf(sf(cc))))) = cc)] --> [sf(cc) = cc], inference(RightSubstEq, param(0, sf(cc) = Y, Y), [f5])).", // (Rewrite=> rule8 cc)
  ];
  
  // sexpr term: (sf (sf (sf (sf (sf (sf cc))))))
  // tptp term: sf(sf(sf(sf(sf(sf(cc)))))
  // function that transforms expressions of type FlatTerm<egg::SymbolLang> to tptp format:
  #[allow(unused_variables)]
  
  
  
  
  let rules_ = vec![
  ("sf(sf(sf(sf(sf(cc)))))".to_owned(), "cc".to_owned()),
  ("sf(sf(sf(sf(sf(sf(sf(sf(cc))))))))".to_owned(), "cc".to_owned())
  ];
  let rules_names = vec!["rule5".to_owned(), "rule8".to_owned()];
  let header = 
  "fof(rule5, axiom, [] --> [sf(sf(sf(sf(sf(cc))))) = cc]).
fof(rule8, axiom, [] --> [sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc]).".to_owned();
  println!("{}", get_flat_string(_e1));
  println!("\n");
  println!("{}", proof_to_tptp(header, &_e1, vec![], rules_, rules_names, |s| if s == "rule5" { 0 } else { 1 }));
  

  
  println!("\n ---- \n");
  solve_tptp_problem("test.p");
}
