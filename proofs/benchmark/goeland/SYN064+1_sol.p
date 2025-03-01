
% SZS output start Proof for SYN064+1.p


fof(c_SYN064_1_p, conjecture, (? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))).

fof(f11, plain, [~((? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((? [Y12] : ((big_p(X6_24, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((big_p(X6_24, Y12_25) => (! [Z814, W1016] : (big_p(Z814, W1016))))), big_p(X6_24, Y12_25), ~((! [Z814, W1016] : (big_p(Z814, W1016)))), ~((! [W1016] : (big_p(Sko_0, W1016)))), ~(big_p(Sko_0, Sko_1)), ~((? [Y12] : ((big_p(Sko_0, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((big_p(Sko_0, Sko_1) => (! [Z814, W1016] : (big_p(Z814, W1016))))), big_p(Sko_0, Sko_1)] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f10, plain, [~((? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((? [Y12] : ((big_p(X6_24, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((big_p(X6_24, Y12_25) => (! [Z814, W1016] : (big_p(Z814, W1016))))), big_p(X6_24, Y12_25), ~((! [Z814, W1016] : (big_p(Z814, W1016)))), ~((! [W1016] : (big_p(Sko_0, W1016)))), ~(big_p(Sko_0, Sko_1)), ~((? [Y12] : ((big_p(Sko_0, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((big_p(Sko_0, Sko_1) => (! [Z814, W1016] : (big_p(Z814, W1016)))))] --> [], inference(leftNotImplies, [status(thm), 8], [f11])).

fof(f9, plain, [~((? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((? [Y12] : ((big_p(X6_24, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((big_p(X6_24, Y12_25) => (! [Z814, W1016] : (big_p(Z814, W1016))))), big_p(X6_24, Y12_25), ~((! [Z814, W1016] : (big_p(Z814, W1016)))), ~((! [W1016] : (big_p(Sko_0, W1016)))), ~(big_p(Sko_0, Sko_1)), ~((? [Y12] : ((big_p(Sko_0, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016)))))))] --> [], inference(leftNotEx, [status(thm), 7, $fot(Sko_1)], [f10])).

fof(f8, plain, [~((? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((? [Y12] : ((big_p(X6_24, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((big_p(X6_24, Y12_25) => (! [Z814, W1016] : (big_p(Z814, W1016))))), big_p(X6_24, Y12_25), ~((! [Z814, W1016] : (big_p(Z814, W1016)))), ~((! [W1016] : (big_p(Sko_0, W1016)))), ~(big_p(Sko_0, Sko_1))] --> [], inference(leftNotEx, [status(thm), 0, $fot(Sko_0)], [f9])).

fof(f7, plain, [~((? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((? [Y12] : ((big_p(X6_24, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((big_p(X6_24, Y12_25) => (! [Z814, W1016] : (big_p(Z814, W1016))))), big_p(X6_24, Y12_25), ~((! [Z814, W1016] : (big_p(Z814, W1016)))), ~((! [W1016] : (big_p(Sko_0, W1016))))] --> [], inference(leftNotAll, [status(thm), 5, 'Sko_1'], [f8])).

fof(f6, plain, [~((? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((? [Y12] : ((big_p(X6_24, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((big_p(X6_24, Y12_25) => (! [Z814, W1016] : (big_p(Z814, W1016))))), big_p(X6_24, Y12_25), ~((! [Z814, W1016] : (big_p(Z814, W1016))))] --> [], inference(leftNotAll, [status(thm), 4, 'Sko_0'], [f7])).

fof(f5, plain, [~((? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((? [Y12] : ((big_p(X6_24, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((big_p(X6_24, Y12_25) => (! [Z814, W1016] : (big_p(Z814, W1016)))))] --> [], inference(leftNotImplies, [status(thm), 2], [f6])).

fof(f4, plain, [~((? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))), ~((? [Y12] : ((big_p(X6_24, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016)))))))] --> [], inference(leftNotEx, [status(thm), 1, $fot(Y12_25)], [f5])).

fof(f3, plain, [~((? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016)))))))] --> [], inference(leftNotEx, [status(thm), 0, $fot(X6_24)], [f4])).

fof(f2, plain, [(? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))] --> [(? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016)))))), ~((? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016)))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(? [X6, Y12] : ((big_p(X6, Y12) => (! [Z814, W1016] : (big_p(Z814, W1016))))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN064+1.p
