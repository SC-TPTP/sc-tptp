
% SZS output start Proof for SYN382+1.p


fof(c_SYN382_1_p, conjecture, ((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))).

fof(f12, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~((? [X14] : ((big_p(X14, Sko_0) | big_q(X14, Sko_0))))), (? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Sko_0)))), ((! [Y10] : (big_p(Sko_1, Y10))) | big_q(Sko_1, Sko_0)), (! [Y10] : (big_p(Sko_1, Y10))), ~((big_p(Sko_1, Sko_0) | big_q(Sko_1, Sko_0))), ~(big_p(Sko_1, Sko_0)), ~(big_q(Sko_1, Sko_0)), big_p(Sko_1, Sko_0)] --> [], inference(leftHyp, [status(thm), 8], [])).

fof(f11, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~((? [X14] : ((big_p(X14, Sko_0) | big_q(X14, Sko_0))))), (? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Sko_0)))), ((! [Y10] : (big_p(Sko_1, Y10))) | big_q(Sko_1, Sko_0)), (! [Y10] : (big_p(Sko_1, Y10))), ~((big_p(Sko_1, Sko_0) | big_q(Sko_1, Sko_0))), ~(big_p(Sko_1, Sko_0)), ~(big_q(Sko_1, Sko_0))] --> [], inference(leftForall, [status(thm), 6, $fot(Sko_0)], [f12])).

fof(f10, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~((? [X14] : ((big_p(X14, Sko_0) | big_q(X14, Sko_0))))), (? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Sko_0)))), ((! [Y10] : (big_p(Sko_1, Y10))) | big_q(Sko_1, Sko_0)), (! [Y10] : (big_p(Sko_1, Y10))), ~((big_p(Sko_1, Sko_0) | big_q(Sko_1, Sko_0)))] --> [], inference(leftNotOr, [status(thm), 7], [f11])).

fof(f8, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~((? [X14] : ((big_p(X14, Sko_0) | big_q(X14, Sko_0))))), (? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Sko_0)))), ((! [Y10] : (big_p(Sko_1, Y10))) | big_q(Sko_1, Sko_0)), (! [Y10] : (big_p(Sko_1, Y10)))] --> [], inference(leftNotEx, [status(thm), 3, $fot(Sko_1)], [f10])).

fof(f14, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~((? [X14] : ((big_p(X14, Sko_0) | big_q(X14, Sko_0))))), (? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Sko_0)))), ((! [Y10] : (big_p(Sko_1, Y10))) | big_q(Sko_1, Sko_0)), big_q(Sko_1, Sko_0), ~((big_p(Sko_1, Sko_0) | big_q(Sko_1, Sko_0))), ~(big_p(Sko_1, Sko_0)), ~(big_q(Sko_1, Sko_0))] --> [], inference(leftHyp, [status(thm), 9], [])).

fof(f13, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~((? [X14] : ((big_p(X14, Sko_0) | big_q(X14, Sko_0))))), (? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Sko_0)))), ((! [Y10] : (big_p(Sko_1, Y10))) | big_q(Sko_1, Sko_0)), big_q(Sko_1, Sko_0), ~((big_p(Sko_1, Sko_0) | big_q(Sko_1, Sko_0)))] --> [], inference(leftNotOr, [status(thm), 7], [f14])).

fof(f9, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~((? [X14] : ((big_p(X14, Sko_0) | big_q(X14, Sko_0))))), (? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Sko_0)))), ((! [Y10] : (big_p(Sko_1, Y10))) | big_q(Sko_1, Sko_0)), big_q(Sko_1, Sko_0)] --> [], inference(leftNotEx, [status(thm), 3, $fot(Sko_1)], [f13])).

fof(f7, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~((? [X14] : ((big_p(X14, Sko_0) | big_q(X14, Sko_0))))), (? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Sko_0)))), ((! [Y10] : (big_p(Sko_1, Y10))) | big_q(Sko_1, Sko_0))] --> [], inference(leftOr, [status(thm), 5], [f8, f9])).

fof(f6, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~((? [X14] : ((big_p(X14, Sko_0) | big_q(X14, Sko_0))))), (? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Sko_0))))] --> [], inference(leftExists, [status(thm), 4, 'Sko_1'], [f7])).

fof(f5, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~((? [X14] : ((big_p(X14, Sko_0) | big_q(X14, Sko_0)))))] --> [], inference(leftForall, [status(thm), 1, $fot(Sko_0)], [f6])).

fof(f4, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))), (! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))), ~((! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))] --> [], inference(leftNotAll, [status(thm), 2, 'Sko_0'], [f5])).

fof(f3, plain, [~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))] --> [((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))), ~(((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12))))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((! [Z6] : ((? [X8] : (((! [Y10] : (big_p(X8, Y10))) | big_q(X8, Z6)))))) => (! [Y12] : ((? [X14] : ((big_p(X14, Y12) | big_q(X14, Y12)))))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN382+1.p
