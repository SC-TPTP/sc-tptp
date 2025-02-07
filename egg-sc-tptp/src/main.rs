#![allow(unused_imports)]
#![allow(dead_code)]

mod translator;
mod printer;
mod fol;
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
  env::set_var("RUST_BACKTRACE", "1");
  let cli = Cli::parse();
  tptp_problem_to_tptp_solution(&cli.input_path, &cli.output_path, cli.level1);

}







