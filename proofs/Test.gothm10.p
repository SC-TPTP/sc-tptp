% SZS output start Proof for eq_1.p

fof(f4, assumption, [(a => b), b] --> [((a => b) => (~a | b)), (~a | b), ~a, b],inference(hyp, [status(thm), 1, 3], [])).

fof(f3, assumption, [(a => b), ~a] --> [((a => b) => (~a | b)), (~a | b), ~a, b],inference(hyp, [status(thm), 1, 2], [])).

fof(f2, plain, [(a => b)] --> [((a => b) => (~a | b)), (~a | b), ~a, b],inference(leftImp1, [status(thm), 0], [f3, f4])).

fof(f1, plain, [(a => b)] --> [((a => b) => (~a | b)), (~a | b)],inference(rightOr, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((a => b) => (~a | b))],inference(rightImp, [status(thm), 0], [f1])).

fof(my_conjecture, conjecture, ((a <=> b) => (a => b))).

% SZS output end Proof for eq_1.p