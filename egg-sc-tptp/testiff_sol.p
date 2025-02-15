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


fof(e0, plain, [p(sf(cemptySet))] --> [p(sf(cemptySet))], inference(hyp, [status(thm), 0], [])).
fof(e1, plain, [] --> [(p(sf(cemptySet)) => p(sf(cemptySet)))], inference(rightImplies, [status(thm), 0], [e0])).
fof(f0, plain, [] --> [(p(sf(cemptySet)) <=> p(sf(cemptySet)))], inference(rightIff, [status(thm), 0], [e1, e1])).
fof(f1, plain, [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(cemptySet))))))))] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(cemptySet))))))))], inference(rightSubstIff, [status(thm), 0, $fof((p(sf(cemptySet)) <=> HOLE)), 'HOLE'], [f0])).
fof(f2, plain, [![Xx] : (p(Xx) <=> p(sf(sf(sf(sf(sf(Xx)))))))] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(cemptySet))))))))], inference(leftForall, [status(thm), 0, $fot(sf(cemptySet))], [f1])).
fof(f3, plain, [] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(cemptySet))))))))], inference(cut, [status(thm), 0, 0], [a2, f2])).
fof(f4, plain, [(p(sf(sf(sf(sf(sf(sf(cemptySet))))))) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet)))))))))))))] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet)))))))))))))], inference(rightSubstIff, [status(thm), 0, $fof((p(sf(cemptySet)) <=> HOLE)), 'HOLE'], [f3])).
fof(f5, plain, [![Xx] : (p(Xx) <=> p(sf(sf(sf(sf(sf(Xx)))))))] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet)))))))))))))], inference(leftForall, [status(thm), 0, $fot(sf(sf(sf(sf(sf(sf(cemptySet)))))))], [f4])).
fof(f6, plain, [] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet)))))))))))))], inference(cut, [status(thm), 0, 0], [a2, f5])).
fof(f7, plain, [(p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet)))))))))))) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))))))))))] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))))))))))], inference(rightSubstIff, [status(thm), 0, $fof((p(sf(cemptySet)) <=> HOLE)), 'HOLE'], [f6])).
fof(f8, plain, [![Xx] : (p(Xx) <=> p(sf(sf(sf(sf(sf(Xx)))))))] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))))))))))], inference(leftForall, [status(thm), 0, $fot(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))))], [f7])).
fof(f9, plain, [] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))))))))))], inference(cut, [status(thm), 0, 0], [a2, f8])).
fof(f10, plain, [(p(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))))))))))] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))], inference(rightSubstIff, [status(thm), 0, $fof((p(sf(cemptySet)) <=> HOLE)), 'HOLE'], [f9])).
fof(f11, plain, [![Xx] : (p(Xx) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(Xx))))))))))] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))], inference(leftForall, [status(thm), 0, $fot(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet)))))))))], [f10])).
fof(f12, plain, [] --> [(p(sf(cemptySet)) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))], inference(cut, [status(thm), 0, 0], [a1, f11])).
fof(f13, plain, [(p(cemptySet) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(cemptySet))))))))))] --> [(p(sf(cemptySet)) <=> p(cemptySet))], inference(rightSubstIff, [status(thm), 0, $fof((p(sf(cemptySet)) <=> HOLE)), 'HOLE'], [f12])).
fof(f14, plain, [![Xx] : (p(Xx) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(Xx))))))))))] --> [(p(sf(cemptySet)) <=> p(cemptySet))], inference(leftForall, [status(thm), 0, $fot(cemptySet)], [f13])).
fof(f15, plain, [] --> [(p(sf(cemptySet)) <=> p(cemptySet))], inference(cut, [status(thm), 0, 0], [a1, f14])).