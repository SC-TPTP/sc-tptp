%--------------------------------------------------------------------------
% File     : lisa.maths.Tests.buveurs : TPTP v8.0.0.
% Domain   : None
% Problem  : question0
% Version  : None
% English  :

% Refs     : https://github.com/epfl-lara/lisa
%          : lisa.utils.tptp.ProofParser
% Source   : [Lisa, lisa.maths.Tests.buveurs]
% Names    : 

% Status   : Unknown
% Rating   : ?
% Syntax   : ?
% SPC      : FOF_UNK_RFO_SEQ

% Comments : This problem, was printed from a statement in a proof of a theorem by the Lisa theorem prover for submission to proof-producing ATPs.
%--------------------------------------------------------------------------
fof(rule5, axiom, sf(sf(sf(sf(sf(cc))))) = cc).
fof(rule8, axiom, sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc).
fof(c1, conjecture, sf(cc) = cc).
fof(f0, plain, [sf(sf(sf(sf(sf(cc))))) = cc, sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc] --> [sf(cc) = sf(cc)], inference(rightRefl, param(0), [])).
fof(f1, plain, [sf(sf(sf(sf(sf(cc))))) = cc, sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc] --> [sf(cc) = sf(sf(sf(sf(sf(sf(cc))))))], inference(rightSubstEq, param(0, $fof(sf(cc) = sf(HOLE)), $fot(HOLE)), [f0])).
fof(f2, plain, [sf(sf(sf(sf(sf(cc))))) = cc, sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc] --> [sf(cc) = sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))))))], inference(rightSubstEq, param(0, $fof(sf(cc) = sf(sf(sf(sf(sf(sf(HOLE))))))), $fot(HOLE)), [f1])).
fof(f3, plain, [sf(sf(sf(sf(sf(cc))))) = cc, sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc] --> [sf(cc) = sf(sf(sf(cc)))], inference(rightSubstEq, param(1, $fof(sf(cc) = sf(sf(sf(HOLE)))), $fot(HOLE)), [f2])).
fof(f4, plain, [sf(sf(sf(sf(sf(cc))))) = cc, sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc] --> [sf(cc) = sf(sf(sf(sf(sf(sf(sf(sf(cc))))))))], inference(rightSubstEq, param(0, $fof(sf(cc) = sf(sf(sf(HOLE)))), $fot(HOLE)), [f3])).
fof(f5, plain, [sf(sf(sf(sf(sf(cc))))) = cc, sf(sf(sf(sf(sf(sf(sf(sf(cc)))))))) = cc] --> [sf(cc) = cc], inference(rightSubstEq, param(1, $fof(sf(cc) = HOLE), $fot(HOLE)), [f4])).
fof(f6, plain, [sf(sf(sf(sf(sf(cc))))) = cc] --> [sf(cc) = cc], inference(cut, param(0, 1), [rule8, f5])).
fof(f7, plain, [] --> [sf(cc) = cc], inference(cut, param(0, 0), [rule5, f6])).