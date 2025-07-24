# SC-TPTP utils

This repository contains various utilities to handle SC-TPTP proofs, including a parser, pretty printer and proof checker.

## Check a Proof
To check a proof, use for example
```bash
sbt run check --input proof_file.p
```
Or directly run the `jar`:
```bash
java -jar sctptpUtils.jar check --input proof_file.p
```

## Run Prover9 with proof production

To run Prover9 with proof production, use (with `sbt`)
```bash
sbt run p9  --input input.p --output output.p
```
Or directly run the `jar`:
```bash
java -jar sctptpUtils.jar p9 --input input.p --output output.p
```
