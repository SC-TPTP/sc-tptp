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
% Solver   : egg v0.9.5, egg-sc-tptp v0.1.0
%--------------------------------------------------------------------------
fof(a1, axiom, (! [Xx]: ((Xx = sf(sf(sf(sf(sf(sf(sf(sf(Xx)))))))))))).
fof(a2, axiom, (! [Xx]: ((Xx = sf(sf(sf(sf(sf(Xx))))))))).
fof(c3, conjecture, (cemptySet = sf(cemptySet))).


fof(f0, plain, [] --> [(cemptySet = cemptySet)], inference(rightRefl, param(0), [])).
fof(f1, plain, [] --> [(cemptySet = sf(sf(sf(sf(sf(sf(sf(sf(cemptySet)))))))))], inference(rightSubstEqForall, param($fof((cemptySet = HOLE)), $fot(HOLE)), [a1, f0])).
fof(f2, plain, [] --> [(cemptySet = sf(sf(sf(cemptySet))))], inference(rightSubstEqForall, param($fof((cemptySet = sf(sf(HOLE)))), $fot(HOLE)), [a2, f1])).
fof(f3, plain, [] --> [(cemptySet = sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))))], inference(rightSubstEqForall, param($fof((cemptySet = sf(sf(sf(HOLE))))), $fot(HOLE)), [a1, f2])).
fof(f4, plain, [] --> [(cemptySet = sf(sf(sf(sf(sf(sf(cemptySet)))))))], inference(rightSubstEqForall, param($fof((cemptySet = sf(sf(sf(sf(sf(HOLE))))))), $fot(HOLE)), [a2, f3])).
fof(f5, plain, [] --> [(cemptySet = sf(cemptySet))], inference(rightSubstEqForall, param($fof((cemptySet = sf(HOLE))), $fot(HOLE)), [a2, f4])).