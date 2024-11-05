%--------------------------------------------------------------------------
% File     : lisa.maths.Tests.testThm : TPTP v8.0.0.
% Domain   : None
% Problem  : question0
% Version  : None
% English  :

% Refs     : https://github.com/epfl-lara/lisa
%          : lisa.utils.tptp.ProofParser
% Source   : [Lisa, lisa.maths.Tests.testThm]
% Names    : 

% Status   : Unknown
% Rating   : ?
% Syntax   : ?
% SPC      : FOF_UNK_RFO_SEQ

% Comments : This problem, was printed from a statement in a proof of a theorem by the Lisa theorem prover for submission to proof-producing ATPs.
%--------------------------------------------------------------------------


fof(a1, axiom, cx = sf(sf(cx))).
fof(a2, axiom, cx = sf(sf(sf(cx)))).
fof(c3, conjecture, cx = sf(cx)).
fof(f0, plain, [] --> [cx = cx], inference(rightRefl, param(0), [])).
fof(f1, plain, [cx = (sf (sf (sf cx)))] --> [cx = (sf (sf (sf cx)))], inference(rightSubstEq, param(0, $fof(cx = HOLE), $fot(HOLE)), [f0])).
fof(f2, plain, [] --> [cx = (sf (sf (sf cx)))], inference(cut, param(0, 0), [a2, f3])).
fof(f4, plain, [cx = (sf (sf cx))] --> [cx = (sf cx)], inference(rightSubstEq, param(0, $fof(cx = (sf HOLE)), $fot(HOLE)), [f3])).
fof(f5, plain, [] --> [cx = (sf cx)], inference(cut, param(0, 0), [a1, f4])).