
% SZS output start Proof for SYN952+1.p


fof(c_SYN952_1_p, conjecture, ((! [X4] : (p(X4))) => (? [Y6] : (p(Y6))))).

fof(f6, plain, [~(((! [X4] : (p(X4))) => (? [Y6] : (p(Y6))))), (! [X4] : (p(X4))), ~((? [Y6] : (p(Y6)))), p(Y6_11), ~(p(Y6_11))] --> [], inference(leftHyp, [status(thm), 4], [])).

fof(f5, plain, [~(((! [X4] : (p(X4))) => (? [Y6] : (p(Y6))))), (! [X4] : (p(X4))), ~((? [Y6] : (p(Y6)))), p(Y6_11)] --> [], inference(leftNotEx, [status(thm), 2, $fot(Y6_11)], [f6])).

fof(f4, plain, [~(((! [X4] : (p(X4))) => (? [Y6] : (p(Y6))))), (! [X4] : (p(X4))), ~((? [Y6] : (p(Y6))))] --> [], inference(leftForall, [status(thm), 1, $fot(Y6_11)], [f5])).

fof(f3, plain, [~(((! [X4] : (p(X4))) => (? [Y6] : (p(Y6)))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [((! [X4] : (p(X4))) => (? [Y6] : (p(Y6))))] --> [((! [X4] : (p(X4))) => (? [Y6] : (p(Y6))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((! [X4] : (p(X4))) => (? [Y6] : (p(Y6)))), ~(((! [X4] : (p(X4))) => (? [Y6] : (p(Y6)))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((! [X4] : (p(X4))) => (? [Y6] : (p(Y6))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN952+1.p
