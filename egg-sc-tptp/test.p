%--------------------------------------------------------------------------
% File     : lisa.maths.Tests.divide_mult_shift : TPTP v8.0.0.
% Domain   : None
% Problem  : question0
% Version  : None
% English  :

% Refs     : https://github.com/epfl-lara/lisa
%          : lisa.utils.tptp.ProofParser
% Source   : [Lisa, lisa.maths.Tests.divide_mult_shift]
% Names    : 

% Status   : Unknown
% Rating   : ?
% Syntax   : ?
% SPC      : FOF_UNK_RFO_SEQ

% Comments : This problem, was printed from a statement in a proof of a theorem by the Lisa theorem prover for submission to proof-producing ATPs.
%--------------------------------------------------------------------------
fof(div_one, axiom, ! [X]: d(X, t1) = X).
fof(cancel_denominator, axiom, ! [X, Y]: (m(d(X, Y), Y) = X)).
fof(invert_div, axiom, ! [X, Y]: d(X, Y) = d(t1, d(Y, X))).

fof(c, conjecture, d(m(d(t2, t3), d(t3, t2)), t1) = t1).

