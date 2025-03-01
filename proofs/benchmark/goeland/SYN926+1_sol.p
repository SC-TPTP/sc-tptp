
% SZS output start Proof for SYN926+1.p


fof(c_SYN926_1_p, conjecture, ((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))).

fof(f9, plain, [~(((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))), (? [X4] : ((p(X4) & q(X4)))), ~(((? [X6] : (p(X6))) & (? [X8] : (q(X8))))), (p(Sko_0) & q(Sko_0)), p(Sko_0), q(Sko_0), ~((? [X6] : (p(X6)))), ~(p(Sko_0))] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f7, plain, [~(((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))), (? [X4] : ((p(X4) & q(X4)))), ~(((? [X6] : (p(X6))) & (? [X8] : (q(X8))))), (p(Sko_0) & q(Sko_0)), p(Sko_0), q(Sko_0), ~((? [X6] : (p(X6))))] --> [], inference(leftNotEx, [status(thm), 6, $fot(Sko_0)], [f9])).

fof(f10, plain, [~(((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))), (? [X4] : ((p(X4) & q(X4)))), ~(((? [X6] : (p(X6))) & (? [X8] : (q(X8))))), (p(Sko_0) & q(Sko_0)), p(Sko_0), q(Sko_0), ~((? [X8] : (q(X8)))), ~(q(Sko_0))] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f8, plain, [~(((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))), (? [X4] : ((p(X4) & q(X4)))), ~(((? [X6] : (p(X6))) & (? [X8] : (q(X8))))), (p(Sko_0) & q(Sko_0)), p(Sko_0), q(Sko_0), ~((? [X8] : (q(X8))))] --> [], inference(leftNotEx, [status(thm), 6, $fot(Sko_0)], [f10])).

fof(f6, plain, [~(((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))), (? [X4] : ((p(X4) & q(X4)))), ~(((? [X6] : (p(X6))) & (? [X8] : (q(X8))))), (p(Sko_0) & q(Sko_0)), p(Sko_0), q(Sko_0)] --> [], inference(leftNotAnd, [status(thm), 2], [f7, f8])).

fof(f5, plain, [~(((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))), (? [X4] : ((p(X4) & q(X4)))), ~(((? [X6] : (p(X6))) & (? [X8] : (q(X8))))), (p(Sko_0) & q(Sko_0))] --> [], inference(leftAnd, [status(thm), 3], [f6])).

fof(f4, plain, [~(((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))), (? [X4] : ((p(X4) & q(X4)))), ~(((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))] --> [], inference(leftExists, [status(thm), 1, 'Sko_0'], [f5])).

fof(f3, plain, [~(((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8))))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))] --> [((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8))))), ~(((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((? [X4] : ((p(X4) & q(X4)))) => ((? [X6] : (p(X6))) & (? [X8] : (q(X8)))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN926+1.p
