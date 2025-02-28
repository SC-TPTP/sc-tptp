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
fof(div_one, axiom, ! [X]: d(X, t1) = X).
fof(cancel_denominator, axiom, ! [X, Y]: (m(d(X, Y), Y) = X)).
fof(invert_div, axiom, ! [X, Y]: d(X, Y) = d(t1, d(Y, X))).

fof(c, conjecture, d(m(d(t2, t3), d(t3, t2)), t1) = t1).


fof(f0, plain, [] --> [d(m(d(t2, t3), d(t3, t2)), t1) = d(m(d(t2, t3), d(t3, t2)), t1)], inference(rightRefl, [status(thm), 0], [])).
fof(f1, plain, [d(m(d(t2, t3), d(t3, t2)), t1) = m(d(t2, t3), d(t3, t2))] --> [d(m(d(t2, t3), d(t3, t2)), t1) = m(d(t2, t3), d(t3, t2))], inference(rightSubst, [status(thm), 0, 0, $fof(d(m(d(t2, t3), d(t3, t2)), t1) = HOLE), 'HOLE'], [f0])).
fof(f2, plain, [![X] : d(X, t1) = X] --> [d(m(d(t2, t3), d(t3, t2)), t1) = m(d(t2, t3), d(t3, t2))], inference(leftForall, [status(thm), 0, $fot(m(d(t2, t3), d(t3, t2)))], [f1])).
fof(f3, plain, [] --> [d(m(d(t2, t3), d(t3, t2)), t1) = m(d(t2, t3), d(t3, t2))], inference(cut, [status(thm), 0], [div_one, f2])).
fof(f4, plain, [d(t2, t3) = d(t1, d(t3, t2))] --> [d(m(d(t2, t3), d(t3, t2)), t1) = m(d(t1, d(t3, t2)), d(t3, t2))], inference(rightSubst, [status(thm), 0, 0, $fof(d(m(d(t2, t3), d(t3, t2)), t1) = m(HOLE, d(t3, t2))), 'HOLE'], [f3])).
fof(f5, plain, [![Y] : d(t2, Y) = d(t1, d(Y, t2))] --> [d(m(d(t2, t3), d(t3, t2)), t1) = m(d(t1, d(t3, t2)), d(t3, t2))], inference(leftForall, [status(thm), 0, $fot(t3)], [f4])).
fof(f6, plain, [![X, Y] : d(X, Y) = d(t1, d(Y, X))] --> [d(m(d(t2, t3), d(t3, t2)), t1) = m(d(t1, d(t3, t2)), d(t3, t2))], inference(leftForall, [status(thm), 0, $fot(t2)], [f5])).
fof(f7, plain, [] --> [d(m(d(t2, t3), d(t3, t2)), t1) = m(d(t1, d(t3, t2)), d(t3, t2))], inference(cut, [status(thm), 0], [invert_div, f6])).
fof(f8, plain, [m(d(t1, d(t3, t2)), d(t3, t2)) = t1] --> [d(m(d(t2, t3), d(t3, t2)), t1) = t1], inference(rightSubst, [status(thm), 0, 0, $fof(d(m(d(t2, t3), d(t3, t2)), t1) = HOLE), 'HOLE'], [f7])).
fof(f9, plain, [![Y] : m(d(t1, Y), Y) = t1] --> [d(m(d(t2, t3), d(t3, t2)), t1) = t1], inference(leftForall, [status(thm), 0, $fot(d(t3, t2))], [f8])).
fof(f10, plain, [![X, Y] : m(d(X, Y), Y) = X] --> [d(m(d(t2, t3), d(t3, t2)), t1) = t1], inference(leftForall, [status(thm), 0, $fot(t1)], [f9])).
fof(f11, plain, [] --> [d(m(d(t2, t3), d(t3, t2)), t1) = t1], inference(cut, [status(thm), 0], [cancel_denominator, f10])).