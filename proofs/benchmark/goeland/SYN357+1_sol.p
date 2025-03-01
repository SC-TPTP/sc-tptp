
% SZS output start Proof for SYN357+1.p


fof(c_SYN357_1_p, conjecture, (! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6))))))).

fof(f8, plain, [~((! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6))))))), ~((? [Y6] : ((big_p(Sko_0) => big_p(Y6))))), ~((big_p(Sko_0) => big_p(Sko_0))), big_p(Sko_0), ~(big_p(Sko_0)), ~((big_p(Sko_0) => big_p(Y6_12))), ~(big_p(Y6_12))] --> [], inference(leftHyp, [status(thm), 4], [])).

fof(f7, plain, [~((! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6))))))), ~((? [Y6] : ((big_p(Sko_0) => big_p(Y6))))), ~((big_p(Sko_0) => big_p(Sko_0))), big_p(Sko_0), ~(big_p(Sko_0)), ~((big_p(Sko_0) => big_p(Y6_12)))] --> [], inference(leftNotImplies, [status(thm), 5], [f8])).

fof(f6, plain, [~((! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6))))))), ~((? [Y6] : ((big_p(Sko_0) => big_p(Y6))))), ~((big_p(Sko_0) => big_p(Sko_0))), big_p(Sko_0), ~(big_p(Sko_0))] --> [], inference(leftNotEx, [status(thm), 1, $fot(Y6_12)], [f7])).

fof(f5, plain, [~((! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6))))))), ~((? [Y6] : ((big_p(Sko_0) => big_p(Y6))))), ~((big_p(Sko_0) => big_p(Sko_0)))] --> [], inference(leftNotImplies, [status(thm), 2], [f6])).

fof(f4, plain, [~((! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6))))))), ~((? [Y6] : ((big_p(Sko_0) => big_p(Y6)))))] --> [], inference(leftNotEx, [status(thm), 1, $fot(Sko_0)], [f5])).

fof(f3, plain, [~((! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6)))))))] --> [], inference(leftNotAll, [status(thm), 0, 'Sko_0'], [f4])).

fof(f2, plain, [(! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6))))))] --> [(! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6))))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6)))))), ~((! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6)))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(! [X4] : ((? [Y6] : ((big_p(X4) => big_p(Y6))))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN357+1.p
