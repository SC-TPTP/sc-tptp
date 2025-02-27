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

% Status   : Unknown
% Rating   : ?
% Syntax   : ?
% SPC      : FOF_UNK_RFO_SEQ

% Comments : This problem, was printed from a statement in a proof of a theorem by the Lisa theorem prover for submission to proof-producing ATPs.
%--------------------------------------------------------------------------
fof(a1, axiom, (! [Xx]: ((p(Xx) <=> p(sf(sf(sf(sf(sf(sf(sf(sf(Xx))))))))))))).
fof(a2, axiom, (! [Xx]: ((p(Xx) <=> p(sf(sf(sf(sf(sf(Xx)))))))))).
fof(c3, conjecture, (p(sf(cemptySet)) <=> p(cemptySet))).
%fof(c3, simplify, (p(sf(cemptySet)))).

