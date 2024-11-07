#![allow(unused_imports)]
#![allow(dead_code)]

mod translator;
mod printer;
use printer::*;
use translator::*;

use egg::{*, rewrite as rw};
use tptp::TPTPIterator;
use tptp::fof;
use tptp::top;
use tptp::common::*;


use std::io::Read;
use std::ops::Index;
use std::env;

use clap::Parser;

#[derive(Parser)]
struct Cli {
  input_path: std::path::PathBuf,
  output_path: std::path::PathBuf,
  #[clap(long="level1", short, action)]
  level1: bool,
}

fn main() {
  let cli = Cli::parse();
  tptp_problem_to_tptp_solution(&cli.input_path, &cli.output_path, cli.level1);

}











fn testing() {
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

  
  
}


