
% SZS output start Proof for SYN977+1.p


fof(c_SYN977_1_p, conjecture, (((a <=> b) | a) | b)).

fof(f6, plain, [~((((a <=> b) | a) | b)), ~(((a <=> b) | a)), ~(b), ~((a <=> b)), ~(a), b] --> [], inference(leftHyp, [status(thm), 2], [])).

fof(f7, plain, [~((((a <=> b) | a) | b)), ~(((a <=> b) | a)), ~(b), ~((a <=> b)), ~(a), a] --> [], inference(leftHyp, [status(thm), 4], [])).

fof(f5, plain, [~((((a <=> b) | a) | b)), ~(((a <=> b) | a)), ~(b), ~((a <=> b)), ~(a)] --> [], inference(leftNotIff, [status(thm), 3], [f6, f7])).

fof(f4, plain, [~((((a <=> b) | a) | b)), ~(((a <=> b) | a)), ~(b)] --> [], inference(leftNotOr, [status(thm), 1], [f5])).

fof(f3, plain, [~((((a <=> b) | a) | b))] --> [], inference(leftNotOr, [status(thm), 0], [f4])).

fof(f2, plain, [(((a <=> b) | a) | b)] --> [(((a <=> b) | a) | b)], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(((a <=> b) | a) | b), ~((((a <=> b) | a) | b))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(((a <=> b) | a) | b)], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN977+1.p
