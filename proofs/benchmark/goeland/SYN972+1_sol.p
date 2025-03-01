
% SZS output start Proof for SYN972+1.p


fof(c_SYN972_1_p, conjecture, ((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8))))))).

fof(f8, plain, [~(((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8))))))), (? [X4] : ((! [Y6] : (p(X4, Y6))))), ~((! [Y8] : ((? [X10] : (p(X10, Y8)))))), (! [Y6] : (p(Sko_0, Y6))), ~((? [X10] : (p(X10, Sko_1)))), p(Sko_0, Sko_1), ~(p(Sko_0, Sko_1))] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f7, plain, [~(((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8))))))), (? [X4] : ((! [Y6] : (p(X4, Y6))))), ~((! [Y8] : ((? [X10] : (p(X10, Y8)))))), (! [Y6] : (p(Sko_0, Y6))), ~((? [X10] : (p(X10, Sko_1)))), p(Sko_0, Sko_1)] --> [], inference(leftNotEx, [status(thm), 4, $fot(Sko_0)], [f8])).

fof(f6, plain, [~(((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8))))))), (? [X4] : ((! [Y6] : (p(X4, Y6))))), ~((! [Y8] : ((? [X10] : (p(X10, Y8)))))), (! [Y6] : (p(Sko_0, Y6))), ~((? [X10] : (p(X10, Sko_1))))] --> [], inference(leftForall, [status(thm), 3, $fot(Sko_1)], [f7])).

fof(f5, plain, [~(((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8))))))), (? [X4] : ((! [Y6] : (p(X4, Y6))))), ~((! [Y8] : ((? [X10] : (p(X10, Y8)))))), (! [Y6] : (p(Sko_0, Y6)))] --> [], inference(leftNotAll, [status(thm), 2, 'Sko_1'], [f6])).

fof(f4, plain, [~(((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8))))))), (? [X4] : ((! [Y6] : (p(X4, Y6))))), ~((! [Y8] : ((? [X10] : (p(X10, Y8))))))] --> [], inference(leftExists, [status(thm), 1, 'Sko_0'], [f5])).

fof(f3, plain, [~(((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8)))))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8))))))] --> [((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8))))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8)))))), ~(((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8)))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((? [X4] : ((! [Y6] : (p(X4, Y6))))) => (! [Y8] : ((? [X10] : (p(X10, Y8))))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN972+1.p
