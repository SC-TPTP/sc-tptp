
% SZS output start Proof for SYN981+1.p


fof(c_SYN981_1_p, conjecture, (((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))).

fof(f12, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9)))), (q(Sko_0) => r(Sko_0)), ~(q(Sko_0)), ~((p(Sko_0) => r(Sko_0))), p(Sko_0), ~(r(Sko_0)), (p(Sko_0) => q(Sko_0)), ~(p(Sko_0))] --> [], inference(leftHyp, [status(thm), 11], [])).

fof(f13, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9)))), (q(Sko_0) => r(Sko_0)), ~(q(Sko_0)), ~((p(Sko_0) => r(Sko_0))), p(Sko_0), ~(r(Sko_0)), (p(Sko_0) => q(Sko_0)), q(Sko_0)] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f11, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9)))), (q(Sko_0) => r(Sko_0)), ~(q(Sko_0)), ~((p(Sko_0) => r(Sko_0))), p(Sko_0), ~(r(Sko_0)), (p(Sko_0) => q(Sko_0))] --> [], inference(leftImplies, [status(thm), 10], [f12, f13])).

fof(f10, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9)))), (q(Sko_0) => r(Sko_0)), ~(q(Sko_0)), ~((p(Sko_0) => r(Sko_0))), p(Sko_0), ~(r(Sko_0))] --> [], inference(leftForall, [status(thm), 3, $fot(Sko_0)], [f11])).

fof(f9, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9)))), (q(Sko_0) => r(Sko_0)), ~(q(Sko_0)), ~((p(Sko_0) => r(Sko_0)))] --> [], inference(leftNotImplies, [status(thm), 7], [f10])).

fof(f7, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9)))), (q(Sko_0) => r(Sko_0)), ~(q(Sko_0))] --> [], inference(leftNotEx, [status(thm), 2, $fot(Sko_0)], [f9])).

fof(f15, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9)))), (q(Sko_0) => r(Sko_0)), r(Sko_0), ~((p(Sko_0) => r(Sko_0))), p(Sko_0), ~(r(Sko_0))] --> [], inference(leftHyp, [status(thm), 9], [])).

fof(f14, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9)))), (q(Sko_0) => r(Sko_0)), r(Sko_0), ~((p(Sko_0) => r(Sko_0)))] --> [], inference(leftNotImplies, [status(thm), 7], [f15])).

fof(f8, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9)))), (q(Sko_0) => r(Sko_0)), r(Sko_0)] --> [], inference(leftNotEx, [status(thm), 2, $fot(Sko_0)], [f14])).

fof(f6, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9)))), (q(Sko_0) => r(Sko_0))] --> [], inference(leftImplies, [status(thm), 5], [f7, f8])).

fof(f5, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11))))), (! [X7] : ((p(X7) => q(X7)))), (? [Y9] : ((q(Y9) => r(Y9))))] --> [], inference(leftExists, [status(thm), 4, 'Sko_0'], [f6])).

fof(f4, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))), ((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))), ~((? [Z11] : ((p(Z11) => r(Z11)))))] --> [], inference(leftAnd, [status(thm), 1], [f5])).

fof(f3, plain, [~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11))))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [(((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))] --> [(((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11))))), ~((((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(((! [X7] : ((p(X7) => q(X7)))) & (? [Y9] : ((q(Y9) => r(Y9))))) => (? [Z11] : ((p(Z11) => r(Z11)))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN981+1.p
