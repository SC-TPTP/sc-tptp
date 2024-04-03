% SZS output start Proof for eq1.p


fof(c_eq1_p, conjecture, ((a = b) => (p(a) => p(b)))).

fof(f5, plain, [~(((a = b) => (p(a) => p(b)))), (a = b), ~((p(a) => p(b))), p(a), ~(p(b))] --> [], inference(congruence, [], [])).

fof(f4, plain, [~(((a = b) => (p(a) => p(b)))), (a = b), ~((p(a) => p(b)))] --> [], inference(leftNotImp, [2], [f5])).

fof(f3, plain, [~(((a = b) => (p(a) => p(b))))] --> [], inference(leftNotImp, [0], [f4])).

fof(f2, plain, [((a = b) => (p(a) => p(b)))] --> [((a = b) => (p(a) => p(b)))], inference(hyp, [0, 0], [])).

fof(f1, plain, [] --> [((a = b) => (p(a) => p(b))), ~(((a = b) => (p(a) => p(b))))], inference(rightNot, [1], [f2])).

fof(f0, plain, [] --> [((a = b) => (p(a) => p(b)))], inference(cut, [1, 0], [f1, f3])).



% SZS output end Proof for eq1.p