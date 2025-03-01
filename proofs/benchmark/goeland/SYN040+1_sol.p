
% SZS output start Proof for SYN040+1.p


fof(c_SYN040_1_p, conjecture, ((p => q) <=> (~(q) => ~(p)))).

fof(f9, plain, [~(((p => q) <=> (~(q) => ~(p)))), ~((p => q)), (~(q) => ~(p)), p, ~(q), ~(~(q)), q] --> [], inference(leftHyp, [status(thm), 4], [])).

fof(f7, plain, [~(((p => q) <=> (~(q) => ~(p)))), ~((p => q)), (~(q) => ~(p)), p, ~(q), ~(~(q))] --> [], inference(leftNotNot, [status(thm), 5], [f9])).

fof(f8, plain, [~(((p => q) <=> (~(q) => ~(p)))), ~((p => q)), (~(q) => ~(p)), p, ~(q), ~(p)] --> [], inference(leftHyp, [status(thm), 5], [])).

fof(f6, plain, [~(((p => q) <=> (~(q) => ~(p)))), ~((p => q)), (~(q) => ~(p)), p, ~(q)] --> [], inference(leftImplies, [status(thm), 2], [f7, f8])).

fof(f4, plain, [~(((p => q) <=> (~(q) => ~(p)))), ~((p => q)), (~(q) => ~(p))] --> [], inference(leftNotImplies, [status(thm), 1], [f6])).

fof(f12, plain, [~(((p => q) <=> (~(q) => ~(p)))), (p => q), ~((~(q) => ~(p))), ~(q), ~(~(p)), p, ~(p)] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f13, plain, [~(((p => q) <=> (~(q) => ~(p)))), (p => q), ~((~(q) => ~(p))), ~(q), ~(~(p)), p, q] --> [], inference(leftHyp, [status(thm), 3], [])).

fof(f11, plain, [~(((p => q) <=> (~(q) => ~(p)))), (p => q), ~((~(q) => ~(p))), ~(q), ~(~(p)), p] --> [], inference(leftImplies, [status(thm), 1], [f12, f13])).

fof(f10, plain, [~(((p => q) <=> (~(q) => ~(p)))), (p => q), ~((~(q) => ~(p))), ~(q), ~(~(p))] --> [], inference(leftNotNot, [status(thm), 4], [f11])).

fof(f5, plain, [~(((p => q) <=> (~(q) => ~(p)))), (p => q), ~((~(q) => ~(p)))] --> [], inference(leftNotImplies, [status(thm), 2], [f10])).

fof(f3, plain, [~(((p => q) <=> (~(q) => ~(p))))] --> [], inference(leftNotIff, [status(thm), 0], [f4, f5])).

fof(f2, plain, [((p => q) <=> (~(q) => ~(p)))] --> [((p => q) <=> (~(q) => ~(p)))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((p => q) <=> (~(q) => ~(p))), ~(((p => q) <=> (~(q) => ~(p))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((p => q) <=> (~(q) => ~(p)))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN040+1.p
