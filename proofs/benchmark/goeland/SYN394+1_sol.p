
% SZS output start Proof for SYN394+1.p


fof(c_SYN394_1_p, conjecture, ((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))).

fof(f10, plain, [~(((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))), (! [X6] : ((f(X6) => g(X6)))), ~(((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10))))), (! [Y8] : (f(Y8))), ~((! [Z10] : (g(Z10)))), ~(g(Sko_0)), (f(Sko_0) => g(Sko_0)), ~(f(Sko_0)), f(Sko_0)] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f8, plain, [~(((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))), (! [X6] : ((f(X6) => g(X6)))), ~(((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10))))), (! [Y8] : (f(Y8))), ~((! [Z10] : (g(Z10)))), ~(g(Sko_0)), (f(Sko_0) => g(Sko_0)), ~(f(Sko_0))] --> [], inference(leftForall, [status(thm), 3, $fot(Sko_0)], [f10])).

fof(f9, plain, [~(((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))), (! [X6] : ((f(X6) => g(X6)))), ~(((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10))))), (! [Y8] : (f(Y8))), ~((! [Z10] : (g(Z10)))), ~(g(Sko_0)), (f(Sko_0) => g(Sko_0)), g(Sko_0)] --> [], inference(leftHyp, [status(thm), 5], [])).

fof(f7, plain, [~(((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))), (! [X6] : ((f(X6) => g(X6)))), ~(((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10))))), (! [Y8] : (f(Y8))), ~((! [Z10] : (g(Z10)))), ~(g(Sko_0)), (f(Sko_0) => g(Sko_0))] --> [], inference(leftImplies, [status(thm), 6], [f8, f9])).

fof(f6, plain, [~(((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))), (! [X6] : ((f(X6) => g(X6)))), ~(((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10))))), (! [Y8] : (f(Y8))), ~((! [Z10] : (g(Z10)))), ~(g(Sko_0))] --> [], inference(leftForall, [status(thm), 1, $fot(Sko_0)], [f7])).

fof(f5, plain, [~(((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))), (! [X6] : ((f(X6) => g(X6)))), ~(((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10))))), (! [Y8] : (f(Y8))), ~((! [Z10] : (g(Z10))))] --> [], inference(leftNotAll, [status(thm), 4, 'Sko_0'], [f6])).

fof(f4, plain, [~(((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))), (! [X6] : ((f(X6) => g(X6)))), ~(((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))] --> [], inference(leftNotImplies, [status(thm), 2], [f5])).

fof(f3, plain, [~(((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10))))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))] --> [((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10))))), ~(((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((! [X6] : ((f(X6) => g(X6)))) => ((! [Y8] : (f(Y8))) => (! [Z10] : (g(Z10)))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN394+1.p
