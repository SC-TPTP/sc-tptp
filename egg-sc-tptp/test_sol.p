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

% Status   : Theorem
% Rating   : ?
% Syntax   : ?
% SPC      : FOF_UNK_RFO_SEQ

% Solver   : egg v0.9.5
%          : egg-sc-tptp v0.1.0
% Logic    : schem
% Comments : This problem, was printed from a statement in a proof of a theorem by the Lisa theorem prover for submission to proof-producing ATPs.
%--------------------------------------------------------------------------
fof(c1, conjecture, [! [Xx]: (Xx = sf(Xx))] --> [(c = sf(c))]).

fof(f0, plain, [![Xx] : Xx = sf(Xx)] --> [c = c], inference(rightRefl, [status(thm), 0], [])).
fof(f1, plain, [c = sf(c), ![Xx] : Xx = sf(Xx)] --> [c = sf(c)], inference(rightSubst, [status(thm), 0, 0, $fof(c = HOLE), 'HOLE'], [f0])).
fof(f2, plain, [![Xx] : Xx = sf(Xx)] --> [c = sf(c)], inference(leftForall, [status(thm), 0, $fot(c)], [f1])).