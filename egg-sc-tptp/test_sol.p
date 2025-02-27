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

% Comments : This problem, was printed from a statement in a proof of a theorem by the Lisa theorem prover for submission to proof-producing ATPs.
%--------------------------------------------------------------------------
fof(c1, conjecture, [(! [Xx]: ((! [Xy]: ((smult(sdiv(Xx,Xy),Xy) = Xx))))),(! [Xx]: ((! [Xy]: ((sdiv(Xx,Xy) = sdiv(ct1,sdiv(Xy,Xx))))))),(! [Xx]: ((sdiv(Xx,ct1) = Xx)))] --> [(sdiv(smult(sdiv(ct2,ct3),sdiv(ct3,ct2)),ct1) = ct1)]).

% Solver   : egg v0.9.5
%          : egg-sc-tptp v0.1.0
% Logic    : schem

fof(f0, plain, [![Xx] : ![Xy] : (smult(sdiv(Xx, Xy), Xy) = Xx), ![Xx] : ![Xy] : (sdiv(Xx, Xy) = sdiv(ct1, sdiv(Xy, Xx))), ![Xx] : (sdiv(Xx, ct1) = Xx)] --> [(sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1))], inference(rightRefl, [status(thm), 0], [])).
fof(f1, plain, [(sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = smult(sdiv(ct2, ct3), sdiv(ct3, ct2))), ![Xx] : ![Xy] : (smult(sdiv(Xx, Xy), Xy) = Xx), ![Xx] : ![Xy] : (sdiv(Xx, Xy) = sdiv(ct1, sdiv(Xy, Xx))), ![Xx] : (sdiv(Xx, ct1) = Xx)] --> [(sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = smult(sdiv(ct2, ct3), sdiv(ct3, ct2)))], inference(rightSubst, [status(thm), 0, 0, $fof((sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = HOLE)), 'HOLE'], [f0])).
fof(f2, plain, [![Xx] : ![Xy] : (smult(sdiv(Xx, Xy), Xy) = Xx), ![Xx] : ![Xy] : (sdiv(Xx, Xy) = sdiv(ct1, sdiv(Xy, Xx))), ![Xx] : (sdiv(Xx, ct1) = Xx)] --> [(sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = smult(sdiv(ct2, ct3), sdiv(ct3, ct2)))], inference(leftForall, [status(thm), 2, $fot(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)))], [f1])).
fof(f3, plain, [(sdiv(ct2, ct3) = sdiv(ct1, sdiv(ct3, ct2))), ![Xx] : ![Xy] : (smult(sdiv(Xx, Xy), Xy) = Xx), ![Xx] : ![Xy] : (sdiv(Xx, Xy) = sdiv(ct1, sdiv(Xy, Xx))), ![Xx] : (sdiv(Xx, ct1) = Xx)] --> [(sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = smult(sdiv(ct1, sdiv(ct3, ct2)), sdiv(ct3, ct2)))], inference(rightSubst, [status(thm), 0, 0, $fof((sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = smult(HOLE, sdiv(ct3, ct2)))), 'HOLE'], [f2])).
fof(f4, plain, [![Xy] : (sdiv(ct2, Xy) = sdiv(ct1, sdiv(Xy, ct2))), ![Xx] : ![Xy] : (smult(sdiv(Xx, Xy), Xy) = Xx), ![Xx] : ![Xy] : (sdiv(Xx, Xy) = sdiv(ct1, sdiv(Xy, Xx))), ![Xx] : (sdiv(Xx, ct1) = Xx)] --> [(sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = smult(sdiv(ct1, sdiv(ct3, ct2)), sdiv(ct3, ct2)))], inference(leftForall, [status(thm), 0, $fot(ct3)], [f3])).
fof(f5, plain, [![Xx] : ![Xy] : (smult(sdiv(Xx, Xy), Xy) = Xx), ![Xx] : ![Xy] : (sdiv(Xx, Xy) = sdiv(ct1, sdiv(Xy, Xx))), ![Xx] : (sdiv(Xx, ct1) = Xx)] --> [(sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = smult(sdiv(ct1, sdiv(ct3, ct2)), sdiv(ct3, ct2)))], inference(leftForall, [status(thm), 1, $fot(ct2)], [f4])).
fof(f6, plain, [(smult(sdiv(ct1, sdiv(ct3, ct2)), sdiv(ct3, ct2)) = ct1), ![Xx] : ![Xy] : (smult(sdiv(Xx, Xy), Xy) = Xx), ![Xx] : ![Xy] : (sdiv(Xx, Xy) = sdiv(ct1, sdiv(Xy, Xx))), ![Xx] : (sdiv(Xx, ct1) = Xx)] --> [(sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = ct1)], inference(rightSubst, [status(thm), 0, 0, $fof((sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = HOLE)), 'HOLE'], [f5])).
fof(f7, plain, [![Xy] : (smult(sdiv(ct1, Xy), Xy) = ct1), ![Xx] : ![Xy] : (smult(sdiv(Xx, Xy), Xy) = Xx), ![Xx] : ![Xy] : (sdiv(Xx, Xy) = sdiv(ct1, sdiv(Xy, Xx))), ![Xx] : (sdiv(Xx, ct1) = Xx)] --> [(sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = ct1)], inference(leftForall, [status(thm), 0, $fot(sdiv(ct3, ct2))], [f6])).
fof(f8, plain, [![Xx] : ![Xy] : (smult(sdiv(Xx, Xy), Xy) = Xx), ![Xx] : ![Xy] : (sdiv(Xx, Xy) = sdiv(ct1, sdiv(Xy, Xx))), ![Xx] : (sdiv(Xx, ct1) = Xx)] --> [(sdiv(smult(sdiv(ct2, ct3), sdiv(ct3, ct2)), ct1) = ct1)], inference(leftForall, [status(thm), 0, $fot(ct1)], [f7])).