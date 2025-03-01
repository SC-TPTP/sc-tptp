
% SZS output start Proof for SYN945+1.p


fof(c_SYN945_1_p, conjecture, ((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))).

fof(f9, plain, [~(((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))), (! [X5] : (p(X5))), ~((! [A7, B9] : ((p(A7) & p(B9))))), ~((! [B9] : ((p(Sko_0) & p(B9))))), ~((p(Sko_0) & p(Sko_1))), ~(p(Sko_0)), p(Sko_0)] --> [], inference(leftHyp, [status(thm), 5], [])).

fof(f7, plain, [~(((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))), (! [X5] : (p(X5))), ~((! [A7, B9] : ((p(A7) & p(B9))))), ~((! [B9] : ((p(Sko_0) & p(B9))))), ~((p(Sko_0) & p(Sko_1))), ~(p(Sko_0))] --> [], inference(leftForall, [status(thm), 1, $fot(Sko_0)], [f9])).

fof(f10, plain, [~(((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))), (! [X5] : (p(X5))), ~((! [A7, B9] : ((p(A7) & p(B9))))), ~((! [B9] : ((p(Sko_0) & p(B9))))), ~((p(Sko_0) & p(Sko_1))), ~(p(Sko_1)), p(Sko_1)] --> [], inference(leftHyp, [status(thm), 5], [])).

fof(f8, plain, [~(((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))), (! [X5] : (p(X5))), ~((! [A7, B9] : ((p(A7) & p(B9))))), ~((! [B9] : ((p(Sko_0) & p(B9))))), ~((p(Sko_0) & p(Sko_1))), ~(p(Sko_1))] --> [], inference(leftForall, [status(thm), 1, $fot(Sko_1)], [f10])).

fof(f6, plain, [~(((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))), (! [X5] : (p(X5))), ~((! [A7, B9] : ((p(A7) & p(B9))))), ~((! [B9] : ((p(Sko_0) & p(B9))))), ~((p(Sko_0) & p(Sko_1)))] --> [], inference(leftNotAnd, [status(thm), 4], [f7, f8])).

fof(f5, plain, [~(((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))), (! [X5] : (p(X5))), ~((! [A7, B9] : ((p(A7) & p(B9))))), ~((! [B9] : ((p(Sko_0) & p(B9)))))] --> [], inference(leftNotAll, [status(thm), 3, 'Sko_1'], [f6])).

fof(f4, plain, [~(((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))), (! [X5] : (p(X5))), ~((! [A7, B9] : ((p(A7) & p(B9)))))] --> [], inference(leftNotAll, [status(thm), 2, 'Sko_0'], [f5])).

fof(f3, plain, [~(((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9))))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))] --> [((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9))))), ~(((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((! [X5] : (p(X5))) => (! [A7, B9] : ((p(A7) & p(B9)))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN945+1.p
