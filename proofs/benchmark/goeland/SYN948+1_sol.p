
% SZS output start Proof for SYN948+1.p


fof(c_SYN948_1_p, conjecture, ((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9))))).

fof(f8, plain, [~(((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9))))), (! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))), ~((? [Z9] : (a(Z9, Z9)))), (? [Y7] : ((a(X5_11, Y7) & a(Y7, Y7)))), (a(X5_11, Sko_0) & a(Sko_0, Sko_0)), a(X5_11, Sko_0), a(Sko_0, Sko_0), ~(a(Sko_0, Sko_0))] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f7, plain, [~(((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9))))), (! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))), ~((? [Z9] : (a(Z9, Z9)))), (? [Y7] : ((a(X5_11, Y7) & a(Y7, Y7)))), (a(X5_11, Sko_0) & a(Sko_0, Sko_0)), a(X5_11, Sko_0), a(Sko_0, Sko_0)] --> [], inference(leftNotEx, [status(thm), 2, $fot(Sko_0)], [f8])).

fof(f6, plain, [~(((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9))))), (! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))), ~((? [Z9] : (a(Z9, Z9)))), (? [Y7] : ((a(X5_11, Y7) & a(Y7, Y7)))), (a(X5_11, Sko_0) & a(Sko_0, Sko_0))] --> [], inference(leftAnd, [status(thm), 4], [f7])).

fof(f5, plain, [~(((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9))))), (! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))), ~((? [Z9] : (a(Z9, Z9)))), (? [Y7] : ((a(X5_11, Y7) & a(Y7, Y7))))] --> [], inference(leftExists, [status(thm), 3, 'Sko_0'], [f6])).

fof(f4, plain, [~(((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9))))), (! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))), ~((? [Z9] : (a(Z9, Z9))))] --> [], inference(leftForall, [status(thm), 1, $fot(X5_11)], [f5])).

fof(f3, plain, [~(((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9)))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9))))] --> [((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9)))), ~(((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9)))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((! [X5] : ((? [Y7] : ((a(X5, Y7) & a(Y7, Y7)))))) => (? [Z9] : (a(Z9, Z9))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN948+1.p
