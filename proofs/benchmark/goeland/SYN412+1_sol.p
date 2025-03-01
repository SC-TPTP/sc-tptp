
% SZS output start Proof for SYN412+1.p


fof(c_SYN412_1_p, conjecture, ~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))).

fof(f9, plain, [~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))), (? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))), (! [Y6] : ((f(Sko_0, Y6) <=> ~(f(Sko_0, Sko_0))))), (f(Sko_0, Sko_0) <=> ~(f(Sko_0, Sko_0))), ~(f(Sko_0, Sko_0)), ~(~(f(Sko_0, Sko_0))), f(Sko_0, Sko_0)] --> [], inference(leftHyp, [status(thm), 4], [])).

fof(f7, plain, [~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))), (? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))), (! [Y6] : ((f(Sko_0, Y6) <=> ~(f(Sko_0, Sko_0))))), (f(Sko_0, Sko_0) <=> ~(f(Sko_0, Sko_0))), ~(f(Sko_0, Sko_0)), ~(~(f(Sko_0, Sko_0)))] --> [], inference(leftNotNot, [status(thm), 5], [f9])).

fof(f11, plain, [~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))), (? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))), (! [Y6] : ((f(Sko_0, Y6) <=> ~(f(Sko_0, Sko_0))))), (f(Sko_0, Sko_0) <=> ~(f(Sko_0, Sko_0))), f(Sko_0, Sko_0), ~(f(Sko_0, Sko_0)), ~(~(f(Sko_0, Sko_0)))] --> [], inference(leftHyp, [status(thm), 5], [])).

fof(f12, plain, [~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))), (? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))), (! [Y6] : ((f(Sko_0, Y6) <=> ~(f(Sko_0, Sko_0))))), (f(Sko_0, Sko_0) <=> ~(f(Sko_0, Sko_0))), f(Sko_0, Sko_0), ~(f(Sko_0, Sko_0))] --> [], inference(leftHyp, [status(thm), 5], [])).

fof(f10, plain, [~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))), (? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))), (! [Y6] : ((f(Sko_0, Y6) <=> ~(f(Sko_0, Sko_0))))), (f(Sko_0, Sko_0) <=> ~(f(Sko_0, Sko_0))), f(Sko_0, Sko_0), ~(f(Sko_0, Sko_0))] --> [], inference(leftIff, [status(thm), 3], [f11, f12])).

fof(f8, plain, [~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))), (? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))), (! [Y6] : ((f(Sko_0, Y6) <=> ~(f(Sko_0, Sko_0))))), (f(Sko_0, Sko_0) <=> ~(f(Sko_0, Sko_0))), f(Sko_0, Sko_0), ~(f(Sko_0, Sko_0))] --> [], inference(leftForall, [status(thm), 2, $fot(Sko_0)], [f10])).

fof(f6, plain, [~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))), (? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))), (! [Y6] : ((f(Sko_0, Y6) <=> ~(f(Sko_0, Sko_0))))), (f(Sko_0, Sko_0) <=> ~(f(Sko_0, Sko_0)))] --> [], inference(leftIff, [status(thm), 3], [f7, f8])).

fof(f5, plain, [~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))), (? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))), (! [Y6] : ((f(Sko_0, Y6) <=> ~(f(Sko_0, Sko_0)))))] --> [], inference(leftForall, [status(thm), 2, $fot(Sko_0)], [f6])).

fof(f4, plain, [~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))), (? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4)))))))] --> [], inference(leftExists, [status(thm), 1, 'Sko_0'], [f5])).

fof(f3, plain, [~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4)))))))))] --> [], inference(leftNotNot, [status(thm), 0], [f4])).

fof(f2, plain, [~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))] --> [~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4)))))))), ~(~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4)))))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [~((? [X4] : ((! [Y6] : ((f(X4, Y6) <=> ~(f(X4, X4))))))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN412+1.p
