
% SZS output start Proof for SYN401+1.p


fof(c_SYN401_1_p, conjecture, (! [X4] : (((! [Y6] : (f(Y6))) => f(X4))))).

fof(f6, plain, [~((! [X4] : (((! [Y6] : (f(Y6))) => f(X4))))), ~(((! [Y6] : (f(Y6))) => f(Sko_0))), (! [Y6] : (f(Y6))), ~(f(Sko_0)), f(Sko_0)] --> [], inference(leftHyp, [status(thm), 3], [])).

fof(f5, plain, [~((! [X4] : (((! [Y6] : (f(Y6))) => f(X4))))), ~(((! [Y6] : (f(Y6))) => f(Sko_0))), (! [Y6] : (f(Y6))), ~(f(Sko_0))] --> [], inference(leftForall, [status(thm), 2, $fot(Sko_0)], [f6])).

fof(f4, plain, [~((! [X4] : (((! [Y6] : (f(Y6))) => f(X4))))), ~(((! [Y6] : (f(Y6))) => f(Sko_0)))] --> [], inference(leftNotImplies, [status(thm), 1], [f5])).

fof(f3, plain, [~((! [X4] : (((! [Y6] : (f(Y6))) => f(X4)))))] --> [], inference(leftNotAll, [status(thm), 0, 'Sko_0'], [f4])).

fof(f2, plain, [(! [X4] : (((! [Y6] : (f(Y6))) => f(X4))))] --> [(! [X4] : (((! [Y6] : (f(Y6))) => f(X4))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(! [X4] : (((! [Y6] : (f(Y6))) => f(X4)))), ~((! [X4] : (((! [Y6] : (f(Y6))) => f(X4)))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(! [X4] : (((! [Y6] : (f(Y6))) => f(X4))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN401+1.p
