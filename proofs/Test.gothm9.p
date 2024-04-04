% SZS output start Proof for eq_1.p


fof(ax1, axiom, (a = b)).

fof(c_eq_1_p, conjecture, (p(a) => p(b))).

fof(f4, plain, [(a = b), ~((p(a) => p(b))), p(a), ~(p(b))] --> [], inference(congruence, [], [])).

fof(f3, plain, [(a = b), ~((p(a) => p(b)))] --> [], inference(leftNotImp, [1], [f4])).

fof(f2, plain, [(a = b), (p(a) => p(b))] --> [(p(a) => p(b))], inference(hyp, [1, 0], [])).

fof(f1, plain, [(a = b)] --> [(p(a) => p(b)), ~((p(a) => p(b)))], inference(rightNot, [1], [f2])).

fof(f0, plain, [(a = b)] --> [(p(a) => p(b))], inference(cut, [1, 1], [f1, f3])).

fof(ac0, plain, [] --> [(p(a) => p(b))], inference(cut, [0, 0], [ax1, f0])).



% SZS output end Proof for eq_1.p