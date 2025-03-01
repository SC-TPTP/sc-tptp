
% SZS output start Proof for SYN416+1.p


fof(c_SYN416_1_p, conjecture, ((p => q) | (q => p))).

fof(f6, plain, [~(((p => q) | (q => p))), ~((p => q)), ~((q => p)), p, ~(q), q, ~(p)] --> [], inference(leftHyp, [status(thm), 4], [])).

fof(f5, plain, [~(((p => q) | (q => p))), ~((p => q)), ~((q => p)), p, ~(q)] --> [], inference(leftNotImplies, [status(thm), 2], [f6])).

fof(f4, plain, [~(((p => q) | (q => p))), ~((p => q)), ~((q => p))] --> [], inference(leftNotImplies, [status(thm), 1], [f5])).

fof(f3, plain, [~(((p => q) | (q => p)))] --> [], inference(leftNotOr, [status(thm), 0], [f4])).

fof(f2, plain, [((p => q) | (q => p))] --> [((p => q) | (q => p))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((p => q) | (q => p)), ~(((p => q) | (q => p)))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((p => q) | (q => p))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN416+1.p
