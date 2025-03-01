
% SZS output start Proof for SYN978+1.p


fof(c_SYN978_1_p, conjecture, ((a & b) => (a <=> b))).

fof(f6, plain, [~(((a & b) => (a <=> b))), (a & b), ~((a <=> b)), a, b, ~(a)] --> [], inference(leftHyp, [status(thm), 5], [])).

fof(f7, plain, [~(((a & b) => (a <=> b))), (a & b), ~((a <=> b)), a, b, ~(b)] --> [], inference(leftHyp, [status(thm), 5], [])).

fof(f5, plain, [~(((a & b) => (a <=> b))), (a & b), ~((a <=> b)), a, b] --> [], inference(leftNotIff, [status(thm), 2], [f6, f7])).

fof(f4, plain, [~(((a & b) => (a <=> b))), (a & b), ~((a <=> b))] --> [], inference(leftAnd, [status(thm), 1], [f5])).

fof(f3, plain, [~(((a & b) => (a <=> b)))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [((a & b) => (a <=> b))] --> [((a & b) => (a <=> b))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((a & b) => (a <=> b)), ~(((a & b) => (a <=> b)))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((a & b) => (a <=> b))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN978+1.p
