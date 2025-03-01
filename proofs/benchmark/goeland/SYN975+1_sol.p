
% SZS output start Proof for SYN975+1.p


fof(c_SYN975_1_p, conjecture, ((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8))))).

fof(f7, plain, [~(((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8))))), (! [X4, Y6] : (p(X4, Y6))), ~((! [X8] : (p(X8, X8)))), ~(p(Sko_0, Sko_0)), (! [Y6] : (p(Sko_0, Y6))), p(Sko_0, Sko_0)] --> [], inference(leftHyp, [status(thm), 3], [])).

fof(f6, plain, [~(((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8))))), (! [X4, Y6] : (p(X4, Y6))), ~((! [X8] : (p(X8, X8)))), ~(p(Sko_0, Sko_0)), (! [Y6] : (p(Sko_0, Y6)))] --> [], inference(leftForall, [status(thm), 4, $fot(Sko_0)], [f7])).

fof(f5, plain, [~(((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8))))), (! [X4, Y6] : (p(X4, Y6))), ~((! [X8] : (p(X8, X8)))), ~(p(Sko_0, Sko_0))] --> [], inference(leftForall, [status(thm), 1, $fot(Sko_0)], [f6])).

fof(f4, plain, [~(((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8))))), (! [X4, Y6] : (p(X4, Y6))), ~((! [X8] : (p(X8, X8))))] --> [], inference(leftNotAll, [status(thm), 2, 'Sko_0'], [f5])).

fof(f3, plain, [~(((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8)))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8))))] --> [((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8)))), ~(((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8)))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((! [X4, Y6] : (p(X4, Y6))) => (! [X8] : (p(X8, X8))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN975+1.p
