use crate::fol::instantiate_formula;
use crate::translator::*;
use egg::{*, rewrite as rw};
use tptp::fof::Formula;
use std::io::Read;
use std::iter::Map;
use std::ops::Index;
use std::path::Display;
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

pub fn flat_term_to_term(expr: &FlatTerm<FOLLang>) -> fol::Term {
  match expr.node {
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

pub fn flat_term_to_term_hole(expr: &FlatTerm<FOLLang>, hole: &String) -> (fol::Term, Option<(fol::Term, bool, String)>) {
  if expr.backward_rule.is_some() {
    (fol::Term::Function(hole.to_owned(), Vec::new()), 
     Some((flat_term_to_term(&expr.remove_rewrites()), true, expr.backward_rule.unwrap().to_string().to_owned()))
    )
  } else if expr.forward_rule.is_some() {
    (fol::Term::Function(hole.to_owned(), Vec::new()),
     Some((flat_term_to_term(&expr.remove_rewrites()), false, expr.forward_rule.unwrap().to_string().to_owned())))
  } else {
    match expr.node {
      FOLLang::Function(op, _) => {
        if expr.children.is_empty() {
          (fol::Term::Function(op.to_string(), vec![]), None)
        } else {
          let first = flat_term_to_term_hole(&expr.children[0], hole);
          let mut res_vec = vec![Box::new(first.0)];
          let res_rule = expr.children.iter().skip(1).fold(first.1, |acc, e| {
            let res = flat_term_to_term_hole(e, hole);
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

pub enum TermOrFormula {
  Term(fol::Term),
  Formula(fol::Formula)
}

pub fn flat_term_to_formula_hole(expr: &FlatTerm<FOLLang>, hole: &String) -> (fol::Formula, Option<(TermOrFormula, bool, String)>) {
  if expr.backward_rule.is_some() {
    (fol::Formula::Predicate(hole.to_owned(), vec![]), 
     Some((TermOrFormula::Formula(flat_term_to_formula(&expr.remove_rewrites())), true, expr.backward_rule.unwrap().to_string().to_owned()))
    )
  } else if expr.forward_rule.is_some() {
    (fol::Formula::Predicate(hole.to_owned(), vec![]),
     Some((TermOrFormula::Formula(flat_term_to_formula(&expr.remove_rewrites())), false, expr.forward_rule.unwrap().to_string().to_owned())))
  } else {
    match expr.node {
      FOLLang::True => (fol::Formula::True, None),
      FOLLang::False => (fol::Formula::False, None),
      FOLLang::Predicate(op, _) => {
        if expr.children.is_empty() {
          (fol::Formula::Predicate(op.to_string(), vec![]), None)
        } else {
          let first = flat_term_to_term_hole(&expr.children[0], hole);
          let mut res_vec = vec![Box::new(first.0)];
          let res_rule = expr.children.iter().skip(1).fold(first.1, |acc, e| {
            let res = flat_term_to_term_hole(e, hole);
            res_vec.push(Box::new(res.0));
            res.1.or(acc)
          });
          (fol::Formula::Predicate(op.to_string(), res_vec), res_rule.map(|(t, b, r)| (TermOrFormula::Term(t), b, r)))
        }
      },
      FOLLang::Not(_) => {
        let res = flat_term_to_formula_hole(&expr.children[0], hole);
        (fol::Formula::Not(Box::new(res.0)), res.1)
      },
      FOLLang::And(_) => {
        if expr.children.is_empty() {
          (fol::Formula::And(vec![]), None)
        } else {
          let first = flat_term_to_formula_hole(&expr.children[0], hole);
          let mut res_vec = vec![Box::new(first.0)];
          let res_rule = expr.children.iter().skip(1).fold(first.1, |acc, e| {
            let res = flat_term_to_formula_hole(e, hole);
            res_vec.push(Box::new(res.0));
            res.1.or(acc)
          });
          (fol::Formula::And(res_vec), res_rule)
        }
      },
      FOLLang::Or(_) => {
        if expr.children.is_empty() {
          (fol::Formula::Or(vec![]), None)
        } else {
          let first = flat_term_to_formula_hole(&expr.children[0], hole);
          let mut res_vec = vec![Box::new(first.0)];
          let res_rule = expr.children.iter().skip(1).fold(first.1, |acc, e| {
            let res = flat_term_to_formula_hole(e, hole);
            res_vec.push(Box::new(res.0));
            res.1.or(acc)
          });
          (fol::Formula::Or(res_vec), res_rule)
        }
      },
      FOLLang::Implies(_) => {
        let left = flat_term_to_formula_hole(&expr.children[0], hole);
        let right = flat_term_to_formula_hole(&expr.children[1], hole);
        (fol::Formula::Implies(Box::new(left.0), Box::new(right.0)), left.1.or(right.1))
      },
      FOLLang::Iff(_) => {
        let left = flat_term_to_formula_hole(&expr.children[0], hole);
        let right = flat_term_to_formula_hole(&expr.children[1], hole);
        (fol::Formula::Iff(Box::new(left.0), Box::new(right.0)), left.1.or(right.1))
      },
      _ => panic!("{} is not a formula", expr.to_string())
    }
  }
}

pub enum SCTPTPRule {
  RightTrue {name: String, bot: fol::Sequent},
  RightRefl {name: String, bot: fol::Sequent, i: i32},
  RightReflIff {name: String, bot: fol::Sequent, i: i32},
  //format!("fof(f{i}, plain, [{newleft}] --> [{base} = {res}], inference(rightSubstEq, [status(thm), 0, $fof({base} = {with_hole}), $fot(HOLE)), [f{}])).\n", *i-1) 
  RightSubstEq {name: String, bot: fol::Sequent, premise: String, i: i32, phi: fol::Formula, v: String},
  RightSubstIff {name: String, bot: fol::Sequent, premise: String, i: i32, phi: fol::Formula, v: String},
  LeftForall {name: String, bot: fol::Sequent, premise: String, i: i32, t: fol::Term},
  Cut {name: String, bot: fol::Sequent, premise1: String, premise2: String, i1: i32, i2: i32},
  RightSubstEqForallLocal {name: String, bot: fol::Sequent, premise: String, i: i32, phi: fol::Formula, v: String},
  RightSubstEqForall {name: String, bot: fol::Sequent, premise1: String, premise2: String, phi: fol::Formula, v: String},
  RightSubstIffForallLocal {name: String, bot: fol::Sequent, premise: String, i: i32, phi: fol::Formula, v: String},
  RightSubstIffForall {name: String, bot: fol::Sequent, premise1: String, premise2: String, phi: fol::Formula, v: String},
  Hypothesis {name: String, bot: fol::Sequent, i: i32},
  RightImplies {name: String, bot: fol::Sequent, premise: String, i: i32},
  RightIff {name: String, bot: fol::Sequent, premise1: String, premise2: String, i: i32}

}

impl std::fmt::Display for SCTPTPRule {
  fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
    match self {
      SCTPTPRule::RightTrue {name, bot} => 
        write!(f, "fof({}, plain, {}, inference(rightTrue, [status(thm)], [])).", name, bot),
      
      SCTPTPRule::RightRefl {name, bot, i} => 
        write!(f, "fof({}, plain, {}, inference(rightRefl, [status(thm), {}], [])).", name, bot, i),
      SCTPTPRule::RightReflIff {name, bot, i} => 
        write!(f, "fof({}, plain, {}, inference(rightReflIff, [status(thm), {}], [])).", name, bot, i),
      SCTPTPRule::RightSubstEq {name, bot, premise, i, phi, v} => 
        write!(f, "fof({}, plain, {}, inference(rightSubstEq, [status(thm), {}, $fof({}), '{}'], [{}])).", name, bot, i, phi, v, premise),
      SCTPTPRule::RightSubstIff {name, bot, premise, i, phi, v} => 
        write!(f, "fof({}, plain, {}, inference(rightSubstIff, [status(thm), {}, $fof({}), '{}'], [{}])).", name, bot, i, phi, v, premise),
      SCTPTPRule::LeftForall {name, bot, premise, i, t} => 
        write!(f, "fof({}, plain, {}, inference(leftForall, [status(thm), {}, $fot({})], [{}])).", name, bot, i, t, premise),
      SCTPTPRule::Cut {name, bot, premise1, premise2, i1, i2} => 
        write!(f, "fof({}, plain, {}, inference(cut, [status(thm), {}, {}], [{}, {}])).", name, bot, i1, i2, premise1, premise2),
      SCTPTPRule::RightSubstEqForallLocal {name, bot, premise, i, phi, v} =>
        write!(f, "fof({}, plain, {}, inference(rightSubstEqForallLocal, [status(thm), {}, $fof({}), '{}'], [{}])).", name, bot, i, phi, v, premise),
      SCTPTPRule::RightSubstEqForall {name, bot, premise1, premise2, phi, v} =>
        write!(f, "fof({}, plain, {}, inference(rightSubstEqForall, [status(thm), $fof({}), '{}'], [{}, {}])).", name, bot, phi, v, premise1, premise2),
      SCTPTPRule::RightSubstIffForallLocal {name, bot, premise, i, phi, v} =>
        write!(f, "fof({}, plain, {}, inference(rightSubstIffForallLocal, [status(thm), {}, $fof({}), '{}'], [{}])).", name, bot, i, phi, v, premise),
      SCTPTPRule::RightSubstIffForall {name, bot, premise1, premise2, phi, v} =>
        write!(f, "fof({}, plain, {}, inference(rightSubstIffForall, [status(thm), $fof({}), '{}'], [{}, {}])).", name, bot, phi, v, premise1, premise2),
      SCTPTPRule::Hypothesis {name, bot, i} =>
        write!(f, "fof({}, plain, {}, inference(hyp, [status(thm), {}], [])).", name, bot, i),
      SCTPTPRule::RightImplies {name, bot, premise, i} =>
        write!(f, "fof({}, plain, {}, inference(rightImplies, [status(thm), {}], [{}])).", name, bot, i, premise),
      SCTPTPRule::RightIff {name, bot, premise1, premise2, i} =>
        write!(f, "fof({}, plain, {}, inference(rightIff, [status(thm), {}], [{}, {}])).", name, bot, i, premise1, premise2)
    }
  }
}

#[derive(Debug, Clone)]
pub enum RewriteRule {
  FormulaRule(Vec<String>, fol::Formula, fol::Formula),
  TermRule(Vec<String>, fol::Term, fol::Term)
}

pub fn equals(a: &fol::Term, b: &fol::Term) -> fol::Formula {
  fol::Formula::Predicate("=".to_owned(), vec![Box::new(a.clone()), Box::new(b.clone())])
}

pub fn line_to_tptp_level1<F>(line: &FlatTerm<FOLLang>, i: &mut i32, left: &Vec<fol::Formula>, map_rule: F, proof: &mut Vec<SCTPTPRule>) -> () where F: Fn(String) -> RewriteRule {
  let line_to_holes = flat_term_to_formula_hole(line, &"HOLE".into());
  let with_hole = line_to_holes.0;
  let _rule = line_to_holes.1;
  let (inner, backward, rule_name) = _rule.unwrap();
  let is_local_rule: bool = rule_name.starts_with("$");
  let res = flat_term_to_formula(&line.clone());
  let rew_rule = map_rule(rule_name.clone());
  //let (variables, rule_left, rule_right) = map_rule(rule_name.clone())
  
  let mut match_map = HashMap::new();
  *i+=1;
  match (rew_rule, inner) {
    (RewriteRule::FormulaRule(variables, rule_left, rule_right), TermOrFormula::Formula(inner)) => {
      let has_matched: bool = if backward { fol::matching_formula(&rule_left, &inner, &mut match_map) } else { fol::matching_formula(&rule_right, &inner, &mut match_map) };
      if !has_matched {panic!("Error: neither {} nor {} does not match {}", rule_left, rule_right, &res);}
      let subst_form = fol::Formula::Iff(Box::new(instantiate_formula(&rule_left, &match_map)), Box::new(instantiate_formula(&rule_right, &match_map)));
      let mut newleft = vec![subst_form];
      newleft.append(&mut left.clone());
      use SCTPTPRule::*;
      let subst_step = RightSubstIff {
        name: format!("f{i}"),
        bot: fol::Sequent {left: newleft, right: vec![res.clone()]},
        premise: format!("f{}", *i-1),
        i: 0,
        phi: with_hole,
        v: "HOLE".to_owned()
      };
      let mut vars: Vec<String> = Vec::new();
      proof.push(subst_step);
      variables.iter().enumerate().rev().for_each(|(nth, v)| {
        let v_var = fol::Term::Function(v.to_owned(), Vec::new());
        let inst_term: fol::Term = match_map.get(v as &str).unwrap_or(&v_var).clone();
        match_map.remove(&v as &str);
        vars.insert(0, v.to_owned());
        let new_inner = fol::Formula::Iff(Box::new(instantiate_formula(&rule_left, &match_map)), Box::new(instantiate_formula(&rule_right, &match_map)));
        *i+=1; 
        let new_quant_formula = fol::Formula::Forall(vars.clone(), Box::new(new_inner));
        let forall_no = if is_local_rule && nth == 0 {
          let mut no = rule_name.clone();
          no.remove(0);
          no.parse().expect(&format!("Error: rule name is not a number: {}", rule_name))
        } else {0};
        let mut newleft = vec![new_quant_formula];
        newleft.append(&mut left.clone());
        let forall_rule = LeftForall {
          name: format!("f{}", *i),
          bot: fol::Sequent {left: newleft, right: vec![res.clone()]},
          premise: format!("f{}", *i-1),
          i: forall_no,
          t: inst_term
        };
        proof.push(forall_rule);
        
      });
      if !is_local_rule {
        *i+=1;
        let cut_rule = Cut {
          name: format!("f{}", *i),
          bot: fol::Sequent {left: left.clone(), right: vec![res.clone()]},
          premise1: rule_name,
          premise2: format!("f{}", *i-1),
          i1: 0,
          i2: 0
        };
        proof.push(cut_rule);
      } else {}
    },
    (RewriteRule::TermRule(variables, rule_left, rule_right), TermOrFormula::Term(inner)) => {
      let has_matched: bool = if backward { fol::matching_term(&rule_left, &inner, &mut match_map) } else { fol::matching_term(&rule_right, &inner, &mut match_map) };
      if !has_matched {panic!("Error: neither {} nor {} does not match {}", rule_left, rule_right, &res);}
      let subst_form = equals(&fol::instantiate_term(&rule_left, &match_map), &fol::instantiate_term(&rule_right, &match_map));
      let mut newleft = vec![subst_form];
      newleft.append(&mut left.clone());
      use SCTPTPRule::*;
      let subst_step = RightSubstEq {
        name: format!("f{i}"),
        bot: fol::Sequent {left: newleft, right: vec![res.clone()]},
        premise: format!("f{}", *i-1),
        i: 0,
        phi: with_hole,
        v: "HOLE".to_owned()
      };
      proof.push(subst_step);
      let mut vars: Vec<String> = Vec::new();
      variables.iter().enumerate().rev().for_each(|(nth, v)| {
        let v_var = &fol::Term::Function(v.to_owned(), Vec::new());
        let inst_term: fol::Term = match_map.get(v as &str).unwrap_or(v_var).clone();
        match_map.remove(&v as &str);
        vars.insert(0, v.to_owned());
        let new_inner = equals(&fol::instantiate_term(&rule_left, &match_map), &fol::instantiate_term(&rule_right, &match_map));
        *i+=1; 
        let new_quant_formula = fol::Formula::Forall(vars.clone(), Box::new(new_inner));
        let forall_no = if is_local_rule && nth == 0 {
          let mut no = rule_name.clone();
          no.remove(0);
          no.parse().expect(&format!("Error: rule name is not a number: {}", rule_name))
        } else {0};
        let mut newleft = vec![new_quant_formula];
        newleft.append(&mut left.clone());
        let forall_rule = LeftForall {
          name: format!("f{}", *i),
          bot: fol::Sequent {left: newleft, right: vec![res.clone()]},
          premise: format!("f{}", *i-1),
          i: forall_no,
          t: inst_term
        };
        proof.push(forall_rule);
        
      });
      if !is_local_rule {
        *i+=1;
        let cut_rule = Cut {
          name: format!("f{}", *i),
          bot: fol::Sequent {left: left.clone(), right: vec![res]},
          premise1: rule_name,
          premise2: format!("f{}", *i-1),
          i1: 0,
          i2: 0
        };
        proof.push(cut_rule);
      } else {}
    },
    _ => panic!("Should not happen")
  };
}

pub fn line_to_tptp_level2(line: &FlatTerm<FOLLang>, i: &mut i32, left: &Vec<fol::Formula>, proof: &mut Vec<SCTPTPRule>) -> () {
  use SCTPTPRule::*;
  let line_to_holes = flat_term_to_formula_hole(line, &"HOLE".into());
  let with_hole = line_to_holes.0;
  let _rule = line_to_holes.1;
  let (inner, _, rule_name) = _rule.unwrap();
  let is_local_rule: bool = rule_name.starts_with("$");
  let res = flat_term_to_formula(&line.clone());
  //let (variables, rule_left, rule_right) = map_rule(rule_name.clone())
  *i+=1;
  match inner {
    TermOrFormula::Formula(_) => {
      if is_local_rule {
        let forall_no = if is_local_rule {
          let mut no = rule_name.clone();
          no.remove(0);
          no.parse().expect(&format!("Error: rule name is not a number: {}", rule_name))
        } else {0};
        let subst_step = RightSubstIffForallLocal {
          name: format!("f{i}"),
          bot: fol::Sequent {left: left.clone(), right: vec![res.clone()]},
          premise: format!("f{}", *i-1),
          i: forall_no,
          phi: with_hole,
          v: "HOLE".to_owned()
        };
        proof.push(subst_step);
      } else {
        let subst_step = RightSubstIffForall {
          name: format!("f{i}"),
          bot: fol::Sequent {left: left.clone(), right: vec![res.clone()]},
          premise1: rule_name,
          premise2: format!("f{}", *i-1),
          phi: with_hole,
          v: "HOLE".to_owned()
        };
        proof.push(subst_step);
      }
    },
    TermOrFormula::Term(_) => {
      if is_local_rule {
        let forall_no = if is_local_rule {
          let mut no = rule_name.clone();
          no.remove(0);
          no.parse().expect(&format!("Error: rule name is not a number: {}", rule_name))
        } else {0};
        let subst_step = RightSubstEqForallLocal {
          name: format!("f{i}"),
          bot: fol::Sequent {left: left.clone(), right: vec![res.clone()]},
          premise: format!("f{}", *i-1),
          i: forall_no,
          phi: with_hole,
          v: "HOLE".to_owned()
        };
        proof.push(subst_step);
      } else {
        let subst_step = RightSubstEqForall {
          name: format!("f{i}"),
          bot: fol::Sequent {left: left.clone(), right: vec![res.clone()]},
          premise1: rule_name,
          premise2: format!("f{}", *i-1),
          phi: with_hole,
          v: "HOLE".to_owned()
        };
        proof.push(subst_step);
      }
    }
  };
}



pub fn proof_to_tptp(header: &String, proof: &Vec<FlatTerm<FOLLang>>, problem: &TPTPProblem, level1:bool) -> String {
  let map_rule = |s: String| {
    problem.axioms.iter().find(|axiom| *axiom.0 == s).expect(format!("Rule not found: {}", s).as_str()).1.clone()
  };

  let init_formula = flat_term_to_formula(&proof[0]);
  let first_seq = fol::Sequent {left: problem.left.clone(), right: vec![init_formula.clone()]};
  let first_steps: Vec<SCTPTPRule> = match init_formula {
    fol::Formula::True => vec![SCTPTPRule::RightTrue {name: "f0".to_owned(), bot: first_seq}],
    fol::Formula::Predicate(op, _) if op == "=" => vec![SCTPTPRule::RightRefl {name: "f0".to_owned(), bot: first_seq, i: 0}],
    fol::Formula::Iff(a, _) => {
      if level1 {vec![
        SCTPTPRule::Hypothesis { name: "e0".to_owned(), bot: fol::Sequent {left: vec![*a.clone()], right: vec![*a.clone()]}, i: 0},
        SCTPTPRule::RightImplies { name: "e1".to_owned(), bot: fol::Sequent {left:vec![], right: vec![fol::Formula::Implies(a.clone(), a.clone())]}, premise: "e0".to_owned(), i: 0 },
        SCTPTPRule::RightIff { name: "f0".to_owned(), bot: fol::Sequent {left:vec![], right: vec![fol::Formula::Iff(a.clone(), a)]}, premise1: "e1".to_owned(), premise2: "e1".to_owned(), i: 0 },
      ]
      } else {
        vec![SCTPTPRule::RightReflIff {name: "f0".to_owned(), bot: first_seq, i: 0}]
      }
    },
    _ if problem.simplify => {
      let first_seq = fol::Sequent {left: problem.left.clone(), right: vec![fol::Formula::Iff(Box::new(init_formula.clone()), Box::new(init_formula.clone()))]};
      vec![SCTPTPRule::RightReflIff {name: "f0".to_owned(), bot: first_seq, i: 0}]
    }
    _ => panic!("unexpected starting expression")
  };
  let mut i = 0;

  let mut proof_vec = Vec::<SCTPTPRule>::new();
  proof.iter().skip(1).for_each( |line| {
    let res = if level1 {
      line_to_tptp_level1(line, &mut i, &problem.left, &map_rule, &mut proof_vec)
    } else {
      line_to_tptp_level2(line, &mut i, &problem.left, &mut proof_vec)
    };
    res
  });
  format!("{}\n{}\n{}", header, 
    first_steps.iter().map(|step| step.to_string()).collect::<Vec<String>>().join("\n"),
    proof_vec.iter().map(|step| step.to_string()).collect::<Vec<String>>().join("\n"))

}

pub struct TPTPProblem {
  pub path: std::path::PathBuf,
  pub header: Header,
  pub axioms: Vec<(String, RewriteRule)>,
  pub left: Vec<fol::Formula>,
  pub conjecture: (String, fol::Formula),
  pub options: Vec<String>,
  pub simplify: bool
}