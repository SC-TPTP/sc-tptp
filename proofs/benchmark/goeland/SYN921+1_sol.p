
% SZS output start Proof for SYN921+1.p


fof(c_SYN921_1_p, conjecture, (? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))).

fof(f13, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))), ~((! [Y7] : (((p(Y7) => q(X5_9)) => (p(X5_9) => q(X5_9)))))), ~(((p(Sko_0) => q(X5_9)) => (p(X5_9) => q(X5_9)))), (p(Sko_0) => q(X5_9)), ~((p(X5_9) => q(X5_9))), p(X5_9), ~(q(X5_9)), ~(p(Sko_0)), ~((! [Y7] : (((p(Y7) => q(Sko_0)) => (p(Sko_0) => q(Sko_0)))))), ~(((p(Sko_1) => q(Sko_0)) => (p(Sko_0) => q(Sko_0)))), (p(Sko_1) => q(Sko_0)), ~((p(Sko_0) => q(Sko_0))), p(Sko_0), ~(q(Sko_0))] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f12, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))), ~((! [Y7] : (((p(Y7) => q(X5_9)) => (p(X5_9) => q(X5_9)))))), ~(((p(Sko_0) => q(X5_9)) => (p(X5_9) => q(X5_9)))), (p(Sko_0) => q(X5_9)), ~((p(X5_9) => q(X5_9))), p(X5_9), ~(q(X5_9)), ~(p(Sko_0)), ~((! [Y7] : (((p(Y7) => q(Sko_0)) => (p(Sko_0) => q(Sko_0)))))), ~(((p(Sko_1) => q(Sko_0)) => (p(Sko_0) => q(Sko_0)))), (p(Sko_1) => q(Sko_0)), ~((p(Sko_0) => q(Sko_0)))] --> [], inference(leftNotImplies, [status(thm), 11], [f13])).

fof(f11, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))), ~((! [Y7] : (((p(Y7) => q(X5_9)) => (p(X5_9) => q(X5_9)))))), ~(((p(Sko_0) => q(X5_9)) => (p(X5_9) => q(X5_9)))), (p(Sko_0) => q(X5_9)), ~((p(X5_9) => q(X5_9))), p(X5_9), ~(q(X5_9)), ~(p(Sko_0)), ~((! [Y7] : (((p(Y7) => q(Sko_0)) => (p(Sko_0) => q(Sko_0)))))), ~(((p(Sko_1) => q(Sko_0)) => (p(Sko_0) => q(Sko_0))))] --> [], inference(leftNotImplies, [status(thm), 9], [f12])).

fof(f10, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))), ~((! [Y7] : (((p(Y7) => q(X5_9)) => (p(X5_9) => q(X5_9)))))), ~(((p(Sko_0) => q(X5_9)) => (p(X5_9) => q(X5_9)))), (p(Sko_0) => q(X5_9)), ~((p(X5_9) => q(X5_9))), p(X5_9), ~(q(X5_9)), ~(p(Sko_0)), ~((! [Y7] : (((p(Y7) => q(Sko_0)) => (p(Sko_0) => q(Sko_0))))))] --> [], inference(leftNotAll, [status(thm), 8, 'Sko_1'], [f11])).

fof(f8, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))), ~((! [Y7] : (((p(Y7) => q(X5_9)) => (p(X5_9) => q(X5_9)))))), ~(((p(Sko_0) => q(X5_9)) => (p(X5_9) => q(X5_9)))), (p(Sko_0) => q(X5_9)), ~((p(X5_9) => q(X5_9))), p(X5_9), ~(q(X5_9)), ~(p(Sko_0))] --> [], inference(leftNotEx, [status(thm), 0, $fot(Sko_0)], [f10])).

fof(f9, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))), ~((! [Y7] : (((p(Y7) => q(X5_9)) => (p(X5_9) => q(X5_9)))))), ~(((p(Sko_0) => q(X5_9)) => (p(X5_9) => q(X5_9)))), (p(Sko_0) => q(X5_9)), ~((p(X5_9) => q(X5_9))), p(X5_9), ~(q(X5_9)), q(X5_9)] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f7, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))), ~((! [Y7] : (((p(Y7) => q(X5_9)) => (p(X5_9) => q(X5_9)))))), ~(((p(Sko_0) => q(X5_9)) => (p(X5_9) => q(X5_9)))), (p(Sko_0) => q(X5_9)), ~((p(X5_9) => q(X5_9))), p(X5_9), ~(q(X5_9))] --> [], inference(leftImplies, [status(thm), 3], [f8, f9])).

fof(f6, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))), ~((! [Y7] : (((p(Y7) => q(X5_9)) => (p(X5_9) => q(X5_9)))))), ~(((p(Sko_0) => q(X5_9)) => (p(X5_9) => q(X5_9)))), (p(Sko_0) => q(X5_9)), ~((p(X5_9) => q(X5_9)))] --> [], inference(leftNotImplies, [status(thm), 4], [f7])).

fof(f5, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))), ~((! [Y7] : (((p(Y7) => q(X5_9)) => (p(X5_9) => q(X5_9)))))), ~(((p(Sko_0) => q(X5_9)) => (p(X5_9) => q(X5_9))))] --> [], inference(leftNotImplies, [status(thm), 2], [f6])).

fof(f4, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))), ~((! [Y7] : (((p(Y7) => q(X5_9)) => (p(X5_9) => q(X5_9))))))] --> [], inference(leftNotAll, [status(thm), 1, 'Sko_0'], [f5])).

fof(f3, plain, [~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5))))))))] --> [], inference(leftNotEx, [status(thm), 0, $fot(X5_9)], [f4])).

fof(f2, plain, [(? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))] --> [(? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5))))))), ~((? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5))))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(? [X5] : ((! [Y7] : (((p(Y7) => q(X5)) => (p(X5) => q(X5)))))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN921+1.p
