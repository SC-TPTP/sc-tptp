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
fof(c1, conjecture, [(! [Xx]: ((! [Xy]: ((smult(sdiv(Xx,Xy),Xy) = Xx))))),(! [Xx]: ((! [Xy]: ((sdiv(Xx,Xy) = sdiv(ct1,sdiv(Xy,Xx))))))),(! [Xx]: ((sdiv(Xx,ct1) = Xx)))] --> [(sdiv(smult(sdiv(ct2,ct3),sdiv(ct3,ct2)),ct1) = ct1)]).

