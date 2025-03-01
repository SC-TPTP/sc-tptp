
% SZS output start Proof for SYN315+1.p


fof(c_SYN315_1_p, conjecture, (? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))).

fof(f9, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), ~(big_f(X5_9)), ~(p), ~(big_f(Sko_0)), p] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f14, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), ~(big_f(X5_9)), ~(p), big_f(Sko_0), ~((! [Y7] : (((big_f(Sko_0) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(Sko_0) <=> p) => (big_f(Sko_1) <=> p))), (big_f(Sko_0) <=> p), ~((big_f(Sko_1) <=> p)), ~(big_f(Sko_0))] --> [], inference(leftHyp, [status(thm), 12], [])).

fof(f15, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), ~(big_f(X5_9)), ~(p), big_f(Sko_0), ~((! [Y7] : (((big_f(Sko_0) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(Sko_0) <=> p) => (big_f(Sko_1) <=> p))), (big_f(Sko_0) <=> p), ~((big_f(Sko_1) <=> p)), p] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f13, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), ~(big_f(X5_9)), ~(p), big_f(Sko_0), ~((! [Y7] : (((big_f(Sko_0) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(Sko_0) <=> p) => (big_f(Sko_1) <=> p))), (big_f(Sko_0) <=> p), ~((big_f(Sko_1) <=> p))] --> [], inference(leftIff, [status(thm), 10], [f14, f15])).

fof(f12, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), ~(big_f(X5_9)), ~(p), big_f(Sko_0), ~((! [Y7] : (((big_f(Sko_0) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(Sko_0) <=> p) => (big_f(Sko_1) <=> p)))] --> [], inference(leftNotImplies, [status(thm), 9], [f13])).

fof(f11, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), ~(big_f(X5_9)), ~(p), big_f(Sko_0), ~((! [Y7] : (((big_f(Sko_0) <=> p) => (big_f(Y7) <=> p)))))] --> [], inference(leftNotAll, [status(thm), 8, 'Sko_1'], [f12])).

fof(f10, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), ~(big_f(X5_9)), ~(p), big_f(Sko_0)] --> [], inference(leftNotEx, [status(thm), 0, $fot(Sko_0)], [f11])).

fof(f7, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), ~(big_f(X5_9)), ~(p)] --> [], inference(leftNotIff, [status(thm), 4], [f9, f10])).

fof(f21, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), big_f(X5_9), p, ~(big_f(Sko_0)), ~((! [Y7] : (((big_f(Sko_0) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(Sko_0) <=> p) => (big_f(Sko_2) <=> p))), (big_f(Sko_0) <=> p), ~((big_f(Sko_2) <=> p)), ~(p)] --> [], inference(leftHyp, [status(thm), 12], [])).

fof(f22, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), big_f(X5_9), p, ~(big_f(Sko_0)), ~((! [Y7] : (((big_f(Sko_0) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(Sko_0) <=> p) => (big_f(Sko_2) <=> p))), (big_f(Sko_0) <=> p), ~((big_f(Sko_2) <=> p)), big_f(Sko_0)] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f20, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), big_f(X5_9), p, ~(big_f(Sko_0)), ~((! [Y7] : (((big_f(Sko_0) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(Sko_0) <=> p) => (big_f(Sko_2) <=> p))), (big_f(Sko_0) <=> p), ~((big_f(Sko_2) <=> p))] --> [], inference(leftIff, [status(thm), 10], [f21, f22])).

fof(f19, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), big_f(X5_9), p, ~(big_f(Sko_0)), ~((! [Y7] : (((big_f(Sko_0) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(Sko_0) <=> p) => (big_f(Sko_2) <=> p)))] --> [], inference(leftNotImplies, [status(thm), 9], [f20])).

fof(f18, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), big_f(X5_9), p, ~(big_f(Sko_0)), ~((! [Y7] : (((big_f(Sko_0) <=> p) => (big_f(Y7) <=> p)))))] --> [], inference(leftNotAll, [status(thm), 8, 'Sko_2'], [f19])).

fof(f16, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), big_f(X5_9), p, ~(big_f(Sko_0))] --> [], inference(leftNotEx, [status(thm), 0, $fot(Sko_0)], [f18])).

fof(f17, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), big_f(X5_9), p, big_f(Sko_0), ~(p)] --> [], inference(leftHyp, [status(thm), 8], [])).

fof(f8, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p)), big_f(X5_9), p] --> [], inference(leftNotIff, [status(thm), 4], [f16, f17])).

fof(f6, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p))), (big_f(X5_9) <=> p), ~((big_f(Sko_0) <=> p))] --> [], inference(leftIff, [status(thm), 3], [f7, f8])).

fof(f5, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p))))), ~(((big_f(X5_9) <=> p) => (big_f(Sko_0) <=> p)))] --> [], inference(leftNotImplies, [status(thm), 2], [f6])).

fof(f4, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))), ~((! [Y7] : (((big_f(X5_9) <=> p) => (big_f(Y7) <=> p)))))] --> [], inference(leftNotAll, [status(thm), 1, 'Sko_0'], [f5])).

fof(f3, plain, [~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p)))))))] --> [], inference(leftNotEx, [status(thm), 0, $fot(X5_9)], [f4])).

fof(f2, plain, [(? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))] --> [(? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p)))))), ~((? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p)))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(? [X5] : ((! [Y7] : (((big_f(X5) <=> p) => (big_f(Y7) <=> p))))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN315+1.p
