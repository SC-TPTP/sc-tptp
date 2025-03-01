
% SZS output start Proof for SYN956+1.p


fof(c_SYN956_1_p, conjecture, ((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))).

fof(f9, plain, [~(((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))), (? [X4] : ((a(X4) => b(X4)))), ~(((! [X6] : (a(X6))) => (? [X8] : (b(X8))))), (! [X6] : (a(X6))), ~((? [X8] : (b(X8)))), (a(Sko_0) => b(Sko_0)), ~(a(Sko_0)), a(Sko_0)] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f7, plain, [~(((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))), (? [X4] : ((a(X4) => b(X4)))), ~(((! [X6] : (a(X6))) => (? [X8] : (b(X8))))), (! [X6] : (a(X6))), ~((? [X8] : (b(X8)))), (a(Sko_0) => b(Sko_0)), ~(a(Sko_0))] --> [], inference(leftForall, [status(thm), 3, $fot(Sko_0)], [f9])).

fof(f11, plain, [~(((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))), (? [X4] : ((a(X4) => b(X4)))), ~(((! [X6] : (a(X6))) => (? [X8] : (b(X8))))), (! [X6] : (a(X6))), ~((? [X8] : (b(X8)))), (a(Sko_0) => b(Sko_0)), b(Sko_0), a(X6_13), ~(b(Sko_0))] --> [], inference(leftHyp, [status(thm), 8], [])).

fof(f10, plain, [~(((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))), (? [X4] : ((a(X4) => b(X4)))), ~(((! [X6] : (a(X6))) => (? [X8] : (b(X8))))), (! [X6] : (a(X6))), ~((? [X8] : (b(X8)))), (a(Sko_0) => b(Sko_0)), b(Sko_0), a(X6_13)] --> [], inference(leftNotEx, [status(thm), 4, $fot(Sko_0)], [f11])).

fof(f8, plain, [~(((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))), (? [X4] : ((a(X4) => b(X4)))), ~(((! [X6] : (a(X6))) => (? [X8] : (b(X8))))), (! [X6] : (a(X6))), ~((? [X8] : (b(X8)))), (a(Sko_0) => b(Sko_0)), b(Sko_0)] --> [], inference(leftForall, [status(thm), 3, $fot(X6_13)], [f10])).

fof(f6, plain, [~(((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))), (? [X4] : ((a(X4) => b(X4)))), ~(((! [X6] : (a(X6))) => (? [X8] : (b(X8))))), (! [X6] : (a(X6))), ~((? [X8] : (b(X8)))), (a(Sko_0) => b(Sko_0))] --> [], inference(leftImplies, [status(thm), 5], [f7, f8])).

fof(f5, plain, [~(((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))), (? [X4] : ((a(X4) => b(X4)))), ~(((! [X6] : (a(X6))) => (? [X8] : (b(X8))))), (! [X6] : (a(X6))), ~((? [X8] : (b(X8))))] --> [], inference(leftExists, [status(thm), 1, 'Sko_0'], [f6])).

fof(f4, plain, [~(((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))), (? [X4] : ((a(X4) => b(X4)))), ~(((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))] --> [], inference(leftNotImplies, [status(thm), 2], [f5])).

fof(f3, plain, [~(((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8))))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))] --> [((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8))))), ~(((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((? [X4] : ((a(X4) => b(X4)))) => ((! [X6] : (a(X6))) => (? [X8] : (b(X8)))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN956+1.p
