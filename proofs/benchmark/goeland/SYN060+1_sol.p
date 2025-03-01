
% SZS output start Proof for SYN060+1.p


fof(ax7, axiom, (! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6)))))).

fof(ax16, axiom, (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8)))))).

fof(c_SYN060_1_p, conjecture, (! [X10] : (big_i(X10)))).

fof(f12, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~((big_f(Sko_0) | big_g(Sko_0))), ~(big_f(Sko_0)), ~(big_g(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0))), ~((big_g(Sko_0) => ~(big_i(Sko_0)))), big_g(Sko_0), ~(~(big_i(Sko_0)))] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f10, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~((big_f(Sko_0) | big_g(Sko_0))), ~(big_f(Sko_0)), ~(big_g(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0))), ~((big_g(Sko_0) => ~(big_i(Sko_0))))] --> [], inference(leftNotImplies, [status(thm), 9], [f12])).

fof(f13, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~((big_f(Sko_0) | big_g(Sko_0))), ~(big_f(Sko_0)), ~(big_g(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0))), (big_f(Sko_0) & big_h(Sko_0)), big_f(Sko_0), big_h(Sko_0)] --> [], inference(leftHyp, [status(thm), 6], [])).

fof(f11, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~((big_f(Sko_0) | big_g(Sko_0))), ~(big_f(Sko_0)), ~(big_g(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0))), (big_f(Sko_0) & big_h(Sko_0))] --> [], inference(leftAnd, [status(thm), 9], [f13])).

fof(f9, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~((big_f(Sko_0) | big_g(Sko_0))), ~(big_f(Sko_0)), ~(big_g(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0)))] --> [], inference(leftImplies, [status(thm), 8], [f10, f11])).

fof(f8, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~((big_f(Sko_0) | big_g(Sko_0))), ~(big_f(Sko_0)), ~(big_g(Sko_0))] --> [], inference(leftForall, [status(thm), 1, $fot(Sko_0)], [f9])).

fof(f6, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~((big_f(Sko_0) | big_g(Sko_0)))] --> [], inference(leftNotOr, [status(thm), 5], [f8])).

fof(f18, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~(big_h(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0))), ~((big_g(Sko_0) => ~(big_i(Sko_0)))), big_g(Sko_0), ~(~(big_i(Sko_0))), big_i(Sko_0)] --> [], inference(leftHyp, [status(thm), 3], [])).

fof(f17, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~(big_h(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0))), ~((big_g(Sko_0) => ~(big_i(Sko_0)))), big_g(Sko_0), ~(~(big_i(Sko_0)))] --> [], inference(leftNotNot, [status(thm), 9], [f18])).

fof(f15, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~(big_h(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0))), ~((big_g(Sko_0) => ~(big_i(Sko_0))))] --> [], inference(leftNotImplies, [status(thm), 7], [f17])).

fof(f19, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~(big_h(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0))), (big_f(Sko_0) & big_h(Sko_0)), big_f(Sko_0), big_h(Sko_0)] --> [], inference(leftHyp, [status(thm), 5], [])).

fof(f16, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~(big_h(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0))), (big_f(Sko_0) & big_h(Sko_0))] --> [], inference(leftAnd, [status(thm), 7], [f19])).

fof(f14, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~(big_h(Sko_0)), ((big_g(Sko_0) => ~(big_i(Sko_0))) => (big_f(Sko_0) & big_h(Sko_0)))] --> [], inference(leftImplies, [status(thm), 6], [f15, f16])).

fof(f7, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0))), ~(big_h(Sko_0))] --> [], inference(leftForall, [status(thm), 1, $fot(Sko_0)], [f14])).

fof(f5, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0)), ((big_f(Sko_0) | big_g(Sko_0)) => ~(big_h(Sko_0)))] --> [], inference(leftImplies, [status(thm), 4], [f6, f7])).

fof(f4, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10)))), ~(big_i(Sko_0))] --> [], inference(leftForall, [status(thm), 0, $fot(Sko_0)], [f5])).

fof(f3, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), ~((! [X10] : (big_i(X10))))] --> [], inference(leftNotAll, [status(thm), 2, 'Sko_0'], [f4])).

fof(f2, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8))))), (! [X10] : (big_i(X10)))] --> [(! [X10] : (big_i(X10)))], inference(hyp, [status(thm), 2], [])).

fof(f1, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8)))))] --> [(! [X10] : (big_i(X10))), ~((! [X10] : (big_i(X10))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [(! [X6] : (((big_f(X6) | big_g(X6)) => ~(big_h(X6))))), (! [X8] : (((big_g(X8) => ~(big_i(X8))) => (big_f(X8) & big_h(X8)))))] --> [(! [X10] : (big_i(X10)))], inference(cut, [status(thm), 1], [f1, f3])).

fof(ac1, plain, [(! [X6] : , (((big_f(X6) | big_g(X6)) => ~(big_h(X6)))))] --> [(! [X10] : (big_i(X10)))], inference(cut, [status(thm), 0], [ax16, f0])).

fof(ac0, plain, [] --> [(! [X10] : (big_i(X10)))], inference(cut, [status(thm), 0], [ax7, ac1])).



% SZS output end Proof for SYN060+1.p
