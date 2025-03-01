
% SZS output start Proof for SYN355+1.p


fof(c_SYN355_1_p, conjecture, (((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))).

fof(f14, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9))))), (! [X5] : ((big_r(X5) => big_p(X5)))), (! [X7] : ((~(big_q(X7)) => big_r(X7)))), ~((big_p(Sko_0) | big_q(Sko_0))), ~(big_p(Sko_0)), ~(big_q(Sko_0)), (big_r(Sko_0) => big_p(Sko_0)), ~(big_r(Sko_0)), (~(big_q(Sko_0)) => big_r(Sko_0)), ~(~(big_q(Sko_0))), big_q(Sko_0)] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f12, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9))))), (! [X5] : ((big_r(X5) => big_p(X5)))), (! [X7] : ((~(big_q(X7)) => big_r(X7)))), ~((big_p(Sko_0) | big_q(Sko_0))), ~(big_p(Sko_0)), ~(big_q(Sko_0)), (big_r(Sko_0) => big_p(Sko_0)), ~(big_r(Sko_0)), (~(big_q(Sko_0)) => big_r(Sko_0)), ~(~(big_q(Sko_0)))] --> [], inference(leftNotNot, [status(thm), 11], [f14])).

fof(f13, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9))))), (! [X5] : ((big_r(X5) => big_p(X5)))), (! [X7] : ((~(big_q(X7)) => big_r(X7)))), ~((big_p(Sko_0) | big_q(Sko_0))), ~(big_p(Sko_0)), ~(big_q(Sko_0)), (big_r(Sko_0) => big_p(Sko_0)), ~(big_r(Sko_0)), (~(big_q(Sko_0)) => big_r(Sko_0)), big_r(Sko_0)] --> [], inference(leftHyp, [status(thm), 9], [])).

fof(f11, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9))))), (! [X5] : ((big_r(X5) => big_p(X5)))), (! [X7] : ((~(big_q(X7)) => big_r(X7)))), ~((big_p(Sko_0) | big_q(Sko_0))), ~(big_p(Sko_0)), ~(big_q(Sko_0)), (big_r(Sko_0) => big_p(Sko_0)), ~(big_r(Sko_0)), (~(big_q(Sko_0)) => big_r(Sko_0))] --> [], inference(leftImplies, [status(thm), 10], [f12, f13])).

fof(f9, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9))))), (! [X5] : ((big_r(X5) => big_p(X5)))), (! [X7] : ((~(big_q(X7)) => big_r(X7)))), ~((big_p(Sko_0) | big_q(Sko_0))), ~(big_p(Sko_0)), ~(big_q(Sko_0)), (big_r(Sko_0) => big_p(Sko_0)), ~(big_r(Sko_0))] --> [], inference(leftForall, [status(thm), 4, $fot(Sko_0)], [f11])).

fof(f10, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9))))), (! [X5] : ((big_r(X5) => big_p(X5)))), (! [X7] : ((~(big_q(X7)) => big_r(X7)))), ~((big_p(Sko_0) | big_q(Sko_0))), ~(big_p(Sko_0)), ~(big_q(Sko_0)), (big_r(Sko_0) => big_p(Sko_0)), big_p(Sko_0)] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f8, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9))))), (! [X5] : ((big_r(X5) => big_p(X5)))), (! [X7] : ((~(big_q(X7)) => big_r(X7)))), ~((big_p(Sko_0) | big_q(Sko_0))), ~(big_p(Sko_0)), ~(big_q(Sko_0)), (big_r(Sko_0) => big_p(Sko_0))] --> [], inference(leftImplies, [status(thm), 8], [f9, f10])).

fof(f7, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9))))), (! [X5] : ((big_r(X5) => big_p(X5)))), (! [X7] : ((~(big_q(X7)) => big_r(X7)))), ~((big_p(Sko_0) | big_q(Sko_0))), ~(big_p(Sko_0)), ~(big_q(Sko_0))] --> [], inference(leftForall, [status(thm), 3, $fot(Sko_0)], [f8])).

fof(f6, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9))))), (! [X5] : ((big_r(X5) => big_p(X5)))), (! [X7] : ((~(big_q(X7)) => big_r(X7)))), ~((big_p(Sko_0) | big_q(Sko_0)))] --> [], inference(leftNotOr, [status(thm), 5], [f7])).

fof(f5, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9))))), (! [X5] : ((big_r(X5) => big_p(X5)))), (! [X7] : ((~(big_q(X7)) => big_r(X7))))] --> [], inference(leftNotAll, [status(thm), 2, 'Sko_0'], [f6])).

fof(f4, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))), ((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))), ~((! [X9] : ((big_p(X9) | big_q(X9)))))] --> [], inference(leftAnd, [status(thm), 1], [f5])).

fof(f3, plain, [~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9))))))] --> [], inference(leftNotImplies, [status(thm), 0], [f4])).

fof(f2, plain, [(((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))] --> [(((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [(((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9))))), ~((((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [(((! [X5] : ((big_r(X5) => big_p(X5)))) & (! [X7] : ((~(big_q(X7)) => big_r(X7))))) => (! [X9] : ((big_p(X9) | big_q(X9)))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN355+1.p
