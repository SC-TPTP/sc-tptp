%--------------------------------------------------------------------------
% File     : lisa.maths.Tests.saturation : TPTP v8.0.0.
% Domain   : None
% Problem  : question0
% Version  : None
% English  : 

% Refs     : https://github.com/epfl-lara/lisa
%          : lisa.utils.tptp.ProofParser
% Source   : [Lisa, lisa.maths.Tests.saturation]
% Names    : 

% Status   : Theorem
% Rating   : ?
% Syntax   : ?
% SPC      : FOF_UNK_RFO_SEQ

% Comments : This problem, was printed from a statement in a proof of a theorem by the Lisa theorem prover for submission to proof-producing ATPs.
% Solver   : egg v0.9.5
%          : egg-sc-tptp v0.1.0
%--------------------------------------------------------------------------
fof(a1, axiom, (sf(sf(sf(cemptySet))) = cemptySet)).
fof(a2, axiom, (sf(sf(cemptySet)) = cemptySet)).
fof(c3, conjecture, (cemptySet = sf(cemptySet))).

fof(f0, plain, [] --> [(cemptySet = cemptySet)], inference(rightRefl, [status(thm), 0], [])).
fof(f1, plain, [(sf(sf(sf(cemptySet))) = cemptySet)] --> [(cemptySet = sf(sf(sf(cemptySet))))], inference(rightSubstEq, [status(thm), 0, $fof((cemptySet = HOLE)), $fot(HOLE)], [f0])).
fof(f2, plain, [] --> [(cemptySet = sf(sf(sf(cemptySet))))], inference(cut, [status(thm), 0, 0], [a1, f1])).
fof(f3, plain, [(sf(sf(cemptySet)) = cemptySet)] --> [(cemptySet = sf(cemptySet))], inference(rightSubstEq, [status(thm), 0, $fof((cemptySet = sf(HOLE))), $fot(HOLE)], [f2])).
fof(f4, plain, [] --> [(cemptySet = sf(cemptySet))], inference(cut, [status(thm), 0, 0], [a2, f3])).