
% SZS output start Proof for SYN934+1.p


fof(c_SYN934_1_p, conjecture, ((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))).

fof(f9, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), ~((? [X4] : ((c => p(X4))))), (c => (? [X6] : (p(X6)))), ~(c), ~((c => p(X4_10))), c, ~(p(X4_10))] --> [], inference(leftHyp, [status(thm), 3], [])).

fof(f8, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), ~((? [X4] : ((c => p(X4))))), (c => (? [X6] : (p(X6)))), ~(c), ~((c => p(X4_10)))] --> [], inference(leftNotImplies, [status(thm), 4], [f9])).

fof(f6, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), ~((? [X4] : ((c => p(X4))))), (c => (? [X6] : (p(X6)))), ~(c)] --> [], inference(leftNotEx, [status(thm), 1, $fot(X4_10)], [f8])).

fof(f12, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), ~((? [X4] : ((c => p(X4))))), (c => (? [X6] : (p(X6)))), (? [X6] : (p(X6))), p(Sko_0), ~((c => p(Sko_0))), c, ~(p(Sko_0))] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f11, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), ~((? [X4] : ((c => p(X4))))), (c => (? [X6] : (p(X6)))), (? [X6] : (p(X6))), p(Sko_0), ~((c => p(Sko_0)))] --> [], inference(leftNotImplies, [status(thm), 5], [f12])).

fof(f10, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), ~((? [X4] : ((c => p(X4))))), (c => (? [X6] : (p(X6)))), (? [X6] : (p(X6))), p(Sko_0)] --> [], inference(leftNotEx, [status(thm), 1, $fot(Sko_0)], [f11])).

fof(f7, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), ~((? [X4] : ((c => p(X4))))), (c => (? [X6] : (p(X6)))), (? [X6] : (p(X6)))] --> [], inference(leftExists, [status(thm), 3, 'Sko_0'], [f10])).

fof(f4, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), ~((? [X4] : ((c => p(X4))))), (c => (? [X6] : (p(X6))))] --> [], inference(leftImplies, [status(thm), 2], [f6, f7])).

fof(f15, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), (? [X4] : ((c => p(X4)))), ~((c => (? [X6] : (p(X6))))), c, ~((? [X6] : (p(X6)))), (c => p(Sko_1)), ~(c)] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f17, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), (? [X4] : ((c => p(X4)))), ~((c => (? [X6] : (p(X6))))), c, ~((? [X6] : (p(X6)))), (c => p(Sko_1)), p(Sko_1), ~(p(Sko_1))] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f16, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), (? [X4] : ((c => p(X4)))), ~((c => (? [X6] : (p(X6))))), c, ~((? [X6] : (p(X6)))), (c => p(Sko_1)), p(Sko_1)] --> [], inference(leftNotEx, [status(thm), 4, $fot(Sko_1)], [f17])).

fof(f14, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), (? [X4] : ((c => p(X4)))), ~((c => (? [X6] : (p(X6))))), c, ~((? [X6] : (p(X6)))), (c => p(Sko_1))] --> [], inference(leftImplies, [status(thm), 5], [f15, f16])).

fof(f13, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), (? [X4] : ((c => p(X4)))), ~((c => (? [X6] : (p(X6))))), c, ~((? [X6] : (p(X6))))] --> [], inference(leftExists, [status(thm), 1, 'Sko_1'], [f14])).

fof(f5, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))), (? [X4] : ((c => p(X4)))), ~((c => (? [X6] : (p(X6)))))] --> [], inference(leftNotImplies, [status(thm), 2], [f13])).

fof(f3, plain, [~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6))))))] --> [], inference(leftNotIff, [status(thm), 0], [f4, f5])).

fof(f2, plain, [((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))] --> [((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6))))), ~(((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((? [X4] : ((c => p(X4)))) <=> (c => (? [X6] : (p(X6)))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN934+1.p
