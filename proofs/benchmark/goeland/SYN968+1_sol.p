
% SZS output start Proof for SYN968+1.p


fof(c_SYN968_1_p, conjecture, (? [X4] : ((! [Y6] : ((p(X4) => p(Y6))))))).

fof(f9, plain, [~((? [X4] : ((! [Y6] : ((p(X4) => p(Y6))))))), ~((! [Y6] : ((p(X4_8) => p(Y6))))), ~((p(X4_8) => p(Sko_0))), p(X4_8), ~(p(Sko_0)), ~((! [Y6] : ((p(Sko_0) => p(Y6))))), ~((p(Sko_0) => p(Sko_1))), p(Sko_0), ~(p(Sko_1))] --> [], inference(leftHyp, [status(thm), 4], [])).

fof(f8, plain, [~((? [X4] : ((! [Y6] : ((p(X4) => p(Y6))))))), ~((! [Y6] : ((p(X4_8) => p(Y6))))), ~((p(X4_8) => p(Sko_0))), p(X4_8), ~(p(Sko_0)), ~((! [Y6] : ((p(Sko_0) => p(Y6))))), ~((p(Sko_0) => p(Sko_1)))] --> [], inference(leftNotImplies, [status(thm), 6], [f9])).

fof(f7, plain, [~((? [X4] : ((! [Y6] : ((p(X4) => p(Y6))))))), ~((! [Y6] : ((p(X4_8) => p(Y6))))), ~((p(X4_8) => p(Sko_0))), p(X4_8), ~(p(Sko_0)), ~((! [Y6] : ((p(Sko_0) => p(Y6)))))] --> [], inference(leftNotAll, [status(thm), 5, 'Sko_1'], [f8])).

fof(f6, plain, [~((? [X4] : ((! [Y6] : ((p(X4) => p(Y6))))))), ~((! [Y6] : ((p(X4_8) => p(Y6))))), ~((p(X4_8) => p(Sko_0))), p(X4_8), ~(p(Sko_0))] --> [], inference(leftNotEx, [status(thm), 0, $fot(Sko_0)], [f7])).

fof(f5, plain, [~((? [X4] : ((! [Y6] : ((p(X4) => p(Y6))))))), ~((! [Y6] : ((p(X4_8) => p(Y6))))), ~((p(X4_8) => p(Sko_0)))] --> [], inference(leftNotImplies, [status(thm), 2], [f6])).

fof(f4, plain, [~((? [X4] : ((! [Y6] : ((p(X4) => p(Y6))))))), ~((! [Y6] : ((p(X4_8) => p(Y6)))))] --> [], inference(leftNotAll, [status(thm), 1, 'Sko_0'], [f5])).

fof(f3, plain, [~((? [X4] : ((! [Y6] : ((p(X4) => p(Y6)))))))] --> [], inference(leftNotEx, [status(thm), 0, $fot(X4_8)], [f4])).

fof(f2, plain, [(? [X4] : ((! [Y6] : ((p(X4) => p(Y6))))))] --> [(? [X4] : ((! [Y6] : ((p(X4) => p(Y6))))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(? [X4] : ((! [Y6] : ((p(X4) => p(Y6)))))), ~((? [X4] : ((! [Y6] : ((p(X4) => p(Y6)))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(? [X4] : ((! [Y6] : ((p(X4) => p(Y6))))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN968+1.p
