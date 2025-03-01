
% SZS output start Proof for SYN384+1.p


fof(c_SYN384_1_p, conjecture, (! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))).

fof(f12, plain, [~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))), ~((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Sko_0) => big_p(U1014, X8, X8))))))), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_0, Y12, Sko_0) => big_p(U1014, Sko_0, Sko_0))))))), ~((! [U1014] : ((big_p(Sko_0, Y12_18, Sko_0) => big_p(U1014, Sko_0, Sko_0))))), ~((big_p(Sko_0, Y12_18, Sko_0) => big_p(Sko_1, Sko_0, Sko_0))), big_p(Sko_0, Y12_18, Sko_0), ~(big_p(Sko_1, Sko_0, Sko_0)), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_1, Y12, Sko_0) => big_p(U1014, Sko_1, Sko_1))))))), ~((! [U1014] : ((big_p(Sko_1, Sko_0, Sko_0) => big_p(U1014, Sko_1, Sko_1))))), ~((big_p(Sko_1, Sko_0, Sko_0) => big_p(Sko_2, Sko_1, Sko_1))), big_p(Sko_1, Sko_0, Sko_0), ~(big_p(Sko_2, Sko_1, Sko_1))] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f11, plain, [~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))), ~((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Sko_0) => big_p(U1014, X8, X8))))))), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_0, Y12, Sko_0) => big_p(U1014, Sko_0, Sko_0))))))), ~((! [U1014] : ((big_p(Sko_0, Y12_18, Sko_0) => big_p(U1014, Sko_0, Sko_0))))), ~((big_p(Sko_0, Y12_18, Sko_0) => big_p(Sko_1, Sko_0, Sko_0))), big_p(Sko_0, Y12_18, Sko_0), ~(big_p(Sko_1, Sko_0, Sko_0)), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_1, Y12, Sko_0) => big_p(U1014, Sko_1, Sko_1))))))), ~((! [U1014] : ((big_p(Sko_1, Sko_0, Sko_0) => big_p(U1014, Sko_1, Sko_1))))), ~((big_p(Sko_1, Sko_0, Sko_0) => big_p(Sko_2, Sko_1, Sko_1)))] --> [], inference(leftNotImplies, [status(thm), 9], [f12])).

fof(f10, plain, [~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))), ~((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Sko_0) => big_p(U1014, X8, X8))))))), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_0, Y12, Sko_0) => big_p(U1014, Sko_0, Sko_0))))))), ~((! [U1014] : ((big_p(Sko_0, Y12_18, Sko_0) => big_p(U1014, Sko_0, Sko_0))))), ~((big_p(Sko_0, Y12_18, Sko_0) => big_p(Sko_1, Sko_0, Sko_0))), big_p(Sko_0, Y12_18, Sko_0), ~(big_p(Sko_1, Sko_0, Sko_0)), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_1, Y12, Sko_0) => big_p(U1014, Sko_1, Sko_1))))))), ~((! [U1014] : ((big_p(Sko_1, Sko_0, Sko_0) => big_p(U1014, Sko_1, Sko_1)))))] --> [], inference(leftNotAll, [status(thm), 8, 'Sko_2'], [f11])).

fof(f9, plain, [~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))), ~((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Sko_0) => big_p(U1014, X8, X8))))))), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_0, Y12, Sko_0) => big_p(U1014, Sko_0, Sko_0))))))), ~((! [U1014] : ((big_p(Sko_0, Y12_18, Sko_0) => big_p(U1014, Sko_0, Sko_0))))), ~((big_p(Sko_0, Y12_18, Sko_0) => big_p(Sko_1, Sko_0, Sko_0))), big_p(Sko_0, Y12_18, Sko_0), ~(big_p(Sko_1, Sko_0, Sko_0)), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_1, Y12, Sko_0) => big_p(U1014, Sko_1, Sko_1)))))))] --> [], inference(leftNotEx, [status(thm), 7, $fot(Sko_0)], [f10])).

fof(f8, plain, [~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))), ~((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Sko_0) => big_p(U1014, X8, X8))))))), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_0, Y12, Sko_0) => big_p(U1014, Sko_0, Sko_0))))))), ~((! [U1014] : ((big_p(Sko_0, Y12_18, Sko_0) => big_p(U1014, Sko_0, Sko_0))))), ~((big_p(Sko_0, Y12_18, Sko_0) => big_p(Sko_1, Sko_0, Sko_0))), big_p(Sko_0, Y12_18, Sko_0), ~(big_p(Sko_1, Sko_0, Sko_0))] --> [], inference(leftNotEx, [status(thm), 1, $fot(Sko_1)], [f9])).

fof(f7, plain, [~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))), ~((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Sko_0) => big_p(U1014, X8, X8))))))), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_0, Y12, Sko_0) => big_p(U1014, Sko_0, Sko_0))))))), ~((! [U1014] : ((big_p(Sko_0, Y12_18, Sko_0) => big_p(U1014, Sko_0, Sko_0))))), ~((big_p(Sko_0, Y12_18, Sko_0) => big_p(Sko_1, Sko_0, Sko_0)))] --> [], inference(leftNotImplies, [status(thm), 4], [f8])).

fof(f6, plain, [~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))), ~((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Sko_0) => big_p(U1014, X8, X8))))))), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_0, Y12, Sko_0) => big_p(U1014, Sko_0, Sko_0))))))), ~((! [U1014] : ((big_p(Sko_0, Y12_18, Sko_0) => big_p(U1014, Sko_0, Sko_0)))))] --> [], inference(leftNotAll, [status(thm), 3, 'Sko_1'], [f7])).

fof(f5, plain, [~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))), ~((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Sko_0) => big_p(U1014, X8, X8))))))), ~((? [Y12] : ((! [U1014] : ((big_p(Sko_0, Y12, Sko_0) => big_p(U1014, Sko_0, Sko_0)))))))] --> [], inference(leftNotEx, [status(thm), 2, $fot(Y12_18)], [f6])).

fof(f4, plain, [~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))), ~((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Sko_0) => big_p(U1014, X8, X8)))))))] --> [], inference(leftNotEx, [status(thm), 1, $fot(Sko_0)], [f5])).

fof(f3, plain, [~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8)))))))))] --> [], inference(leftNotAll, [status(thm), 0, 'Sko_0'], [f4])).

fof(f2, plain, [(! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))] --> [(! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8)))))))), ~((! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8)))))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(! [Z6] : ((? [X8, Y12] : ((! [U1014] : ((big_p(X8, Y12, Z6) => big_p(U1014, X8, X8))))))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN384+1.p
