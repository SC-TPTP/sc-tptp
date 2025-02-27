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
fof(a1, axiom, (! [Xx]: ((p(Xx) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(Xx))))))))))))).
fof(a2, axiom, (! [Xx]: ((p(Xx) <=> p(sf(sf(sf(sf(sf(Xx)))))))))).
fof(c3, conjecture, (p(sf(cemptySet)) <=> p(cemptySet))).
%fof(c3, simplify, (p(sf(cemptySet)))).


fof(f0, plain, [] --> [(p(sf(cemptySet)) <=> p(sf(cemptySet)))], inference(rightReflIff, [status(thm), 0], [])).
fof(f1, plain, [] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(cemptySet))))))))], inference(rightSubstIffForall, [status(thm), $fof((p(sf(cemptySet)) <=> HOLE)), 'HOLE'], [a2, f0])).
fof(f2, plain, [] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet)))))))))))))], inference(rightSubstIffForall, [status(thm), $fof((p(sf(cemptySet)) <=> HOLE)), 'HOLE'], [a2, f1])).
fof(f3, plain, [] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))))))))))], inference(rightSubstIffForall, [status(thm), $fof((p(sf(cemptySet)) <=> HOLE)), 'HOLE'], [a2, f2])).
fof(f4, plain, [] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))], inference(rightSubstIffForall, [status(thm), $fof((p(sf(cemptySet)) <=> HOLE)), 'HOLE'], [a1, f3])).
fof(f5, plain, [] --> [(p(sf(cemptySet)) <=> p(cemptySet))], inference(rightSubstIffForall, [status(thm), $fof((p(sf(cemptySet)) <=> HOLE)), 'HOLE'], [a1, f4])).