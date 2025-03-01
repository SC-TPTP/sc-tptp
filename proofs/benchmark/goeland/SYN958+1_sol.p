
% SZS output start Proof for SYN958+1.p


fof(c_SYN958_1_p, conjecture, (((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))).

fof(f8, plain, [~((((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))), ((? [X4] : (a(X4))) & (! [X6] : (b(X6)))), ~((? [X8] : ((a(X8) & b(X8))))), (? [X4] : (a(X4))), (! [X6] : (b(X6))), a(Sko_0), ~((a(Sko_0) & b(Sko_0))), ~(a(Sko_0))] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f10, plain, [~((((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))), ((? [X4] : (a(X4))) & (! [X6] : (b(X6)))), ~((? [X8] : ((a(X8) & b(X8))))), (? [X4] : (a(X4))), (! [X6] : (b(X6))), a(Sko_0), ~((a(Sko_0) & b(Sko_0))), ~(b(Sko_0)), b(Sko_0)] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f9, plain, [~((((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))), ((? [X4] : (a(X4))) & (! [X6] : (b(X6)))), ~((? [X8] : ((a(X8) & b(X8))))), (? [X4] : (a(X4))), (! [X6] : (b(X6))), a(Sko_0), ~((a(Sko_0) & b(Sko_0))), ~(b(Sko_0))] --> [], inference(leftForall, [status(thm), 4, $fot(Sko_0)], [f10])).

fof(f7, plain, [~((((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))), ((? [X4] : (a(X4))) & (! [X6] : (b(X6)))), ~((? [X8] : ((a(X8) & b(X8))))), (? [X4] : (a(X4))), (! [X6] : (b(X6))), a(Sko_0), ~((a(Sko_0) & b(Sko_0)))] --> [], inference(leftNotAnd, [status(thm), 6], [f8, f9])).

fof(f6, plain, [~((((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))), ((? [X4] : (a(X4))) & (! [X6] : (b(X6)))), ~((? [X8] : ((a(X8) & b(X8))))), (? [X4] : (a(X4))), (! [X6] : (b(X6))), a(Sko_0)] --> [], inference(leftNotEx, [status(thm), 2, $fot(Sko_0)], [f7])).

fof(f5, plain, [~((((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))), ((? [X4] : (a(X4))) & (! [X6] : (b(X6)))), ~((? [X8] : ((a(X8) & b(X8))))), (? [X4] : (a(X4))), (! [X6] : (b(X6)))] --> [], inference(leftExists, [status(thm), 3, 'Sko_0'], [f6])).

fof(f4, plain, [~((((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))), ((? [X4] : (a(X4))) & (! [X6] : (b(X6)))), ~((? [X8] : ((a(X8) & b(X8)))))] --> [], inference(leftAnd, [status(thm), 1], [f5])).

fof(f3, plain, [~((((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8))))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [(((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))] --> [(((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8))))), ~((((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(((? [X4] : (a(X4))) & (! [X6] : (b(X6)))) => (? [X8] : ((a(X8) & b(X8)))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN958+1.p
