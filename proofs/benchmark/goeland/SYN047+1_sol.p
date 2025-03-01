
% SZS output start Proof for SYN047+1.p


fof(c_SYN047_1_p, conjecture, (((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))).

fof(f15, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q), (~(p) | ~(r)), ~(q), ~(p)] --> [], inference(leftHyp, [status(thm), 12], [])).

fof(f16, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q), (~(p) | ~(r)), ~(q), q] --> [], inference(leftHyp, [status(thm), 11], [])).

fof(f13, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q), (~(p) | ~(r)), ~(q)] --> [], inference(leftOr, [status(thm), 9], [f15, f16])).

fof(f17, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q), (~(p) | ~(r)), r, ~(p)] --> [], inference(leftHyp, [status(thm), 12], [])).

fof(f19, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q), (~(p) | ~(r)), r, q, ~(p)] --> [], inference(leftHyp, [status(thm), 13], [])).

fof(f20, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q), (~(p) | ~(r)), r, q, ~(r)] --> [], inference(leftHyp, [status(thm), 13], [])).

fof(f18, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q), (~(p) | ~(r)), r, q] --> [], inference(leftOr, [status(thm), 10], [f19, f20])).

fof(f14, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q), (~(p) | ~(r)), r] --> [], inference(leftOr, [status(thm), 9], [f17, f18])).

fof(f11, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q), (~(p) | ~(r))] --> [], inference(leftImplies, [status(thm), 8], [f13, f14])).

fof(f12, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q), s] --> [], inference(leftHyp, [status(thm), 4], [])).

fof(f9, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), (~(p) | q)] --> [], inference(leftOr, [status(thm), 6], [f11, f12])).

fof(f10, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r), s] --> [], inference(leftHyp, [status(thm), 4], [])).

fof(f8, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s), p, (q => r)] --> [], inference(leftOr, [status(thm), 5], [f9, f10])).

fof(f7, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s), ((~(p) | q) | s), ((~(p) | ~(r)) | s)] --> [], inference(leftAnd, [status(thm), 3], [f8])).

fof(f6, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s)), (p & (q => r)), ~(s)] --> [], inference(leftAnd, [status(thm), 2], [f7])).

fof(f4, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ~(((p & (q => r)) => s)), (((~(p) | q) | s) & ((~(p) | ~(r)) | s))] --> [], inference(leftNotImplies, [status(thm), 1], [f6])).

fof(f28, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | q) | s)), ~((~(p) | q)), ~(s), ~(~(p)), ~(q), p, ~(p)] --> [], inference(leftHyp, [status(thm), 10], [])).

fof(f30, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | q) | s)), ~((~(p) | q)), ~(s), ~(~(p)), ~(q), p, ~((q => r)), q, ~(r)] --> [], inference(leftHyp, [status(thm), 8], [])).

fof(f29, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | q) | s)), ~((~(p) | q)), ~(s), ~(~(p)), ~(q), p, ~((q => r))] --> [], inference(leftNotImplies, [status(thm), 10], [f30])).

fof(f27, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | q) | s)), ~((~(p) | q)), ~(s), ~(~(p)), ~(q), p] --> [], inference(leftNotAnd, [status(thm), 3], [f28, f29])).

fof(f26, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | q) | s)), ~((~(p) | q)), ~(s), ~(~(p)), ~(q)] --> [], inference(leftNotNot, [status(thm), 7], [f27])).

fof(f25, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | q) | s)), ~((~(p) | q)), ~(s)] --> [], inference(leftNotOr, [status(thm), 5], [f26])).

fof(f23, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | q) | s))] --> [], inference(leftNotOr, [status(thm), 4], [f25])).

fof(f35, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | ~(r)) | s)), ~((~(p) | ~(r))), ~(s), ~(~(p)), ~(~(r)), p, r, ~(p)] --> [], inference(leftHyp, [status(thm), 11], [])).

fof(f37, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | ~(r)) | s)), ~((~(p) | ~(r))), ~(s), ~(~(p)), ~(~(r)), p, r, ~((q => r)), q, ~(r)] --> [], inference(leftHyp, [status(thm), 13], [])).

fof(f36, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | ~(r)) | s)), ~((~(p) | ~(r))), ~(s), ~(~(p)), ~(~(r)), p, r, ~((q => r))] --> [], inference(leftNotImplies, [status(thm), 11], [f37])).

fof(f34, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | ~(r)) | s)), ~((~(p) | ~(r))), ~(s), ~(~(p)), ~(~(r)), p, r] --> [], inference(leftNotAnd, [status(thm), 3], [f35, f36])).

fof(f33, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | ~(r)) | s)), ~((~(p) | ~(r))), ~(s), ~(~(p)), ~(~(r)), p] --> [], inference(leftNotNot, [status(thm), 8], [f34])).

fof(f32, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | ~(r)) | s)), ~((~(p) | ~(r))), ~(s), ~(~(p)), ~(~(r))] --> [], inference(leftNotNot, [status(thm), 7], [f33])).

fof(f31, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | ~(r)) | s)), ~((~(p) | ~(r))), ~(s)] --> [], inference(leftNotOr, [status(thm), 5], [f32])).

fof(f24, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r))), ~(((~(p) | ~(r)) | s))] --> [], inference(leftNotOr, [status(thm), 4], [f31])).

fof(f21, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((p & (q => r)))] --> [], inference(leftNotAnd, [status(thm), 2], [f23, f24])).

fof(f40, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), s, ~(((~(p) | q) | s)), ~((~(p) | q)), ~(s)] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f38, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), s, ~(((~(p) | q) | s))] --> [], inference(leftNotOr, [status(thm), 4], [f40])).

fof(f41, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), s, ~(((~(p) | ~(r)) | s)), ~((~(p) | ~(r))), ~(s)] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f39, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), s, ~(((~(p) | ~(r)) | s))] --> [], inference(leftNotOr, [status(thm), 4], [f41])).

fof(f22, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s))), s] --> [], inference(leftNotAnd, [status(thm), 2], [f38, f39])).

fof(f5, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))), ((p & (q => r)) => s), ~((((~(p) | q) | s) & ((~(p) | ~(r)) | s)))] --> [], inference(leftImplies, [status(thm), 1], [f21, f22])).

fof(f3, plain, [~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s))))] --> [], inference(leftNotIff, [status(thm), 0], [f4, f5])).

fof(f2, plain, [(((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))] --> [(((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s))), ~((((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(((p & (q => r)) => s) <=> (((~(p) | q) | s) & ((~(p) | ~(r)) | s)))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN047+1.p
