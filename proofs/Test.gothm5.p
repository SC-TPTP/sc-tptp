% SZS output start Proof for prop_3.p


fof(c_prop_3_p, conjecture, ((((a | b) | c) | d) | ~(a))).

fof(f8, plain, [~(((((a | b) | c) | d) | ~(a))), ~((((a | b) | c) | d)), ~(~(a)), ~(((a | b) | c)), ~(d), a, ~((a | b)), ~(c), ~(a), ~(b)] --> [], inference(leftHyp, [8, 5], [])).

fof(f7, plain, [~(((((a | b) | c) | d) | ~(a))), ~((((a | b) | c) | d)), ~(~(a)), ~(((a | b) | c)), ~(d), a, ~((a | b)), ~(c)] --> [], inference(leftNotOr, [6], [f8])).

fof(f6, plain, [~(((((a | b) | c) | d) | ~(a))), ~((((a | b) | c) | d)), ~(~(a)), ~(((a | b) | c)), ~(d), a] --> [], inference(leftNotOr, [3], [f7])).

fof(f5, plain, [~(((((a | b) | c) | d) | ~(a))), ~((((a | b) | c) | d)), ~(~(a)), ~(((a | b) | c)), ~(d)] --> [], inference(leftNotNot, [2], [f6])).

fof(f4, plain, [~(((((a | b) | c) | d) | ~(a))), ~((((a | b) | c) | d)), ~(~(a))] --> [], inference(leftNotOr, [1], [f5])).

fof(f3, plain, [~(((((a | b) | c) | d) | ~(a)))] --> [], inference(leftNotOr, [0], [f4])).

fof(f2, plain, [((((a | b) | c) | d) | ~(a))] --> [((((a | b) | c) | d) | ~(a))], inference(hyp, [0, 0], [])).

fof(f1, plain, [] --> [((((a | b) | c) | d) | ~(a)), ~(((((a | b) | c) | d) | ~(a)))], inference(rightNot, [1], [f2])).

fof(f0, plain, [] --> [((((a | b) | c) | d) | ~(a))], inference(cut, [1, 0], [f1, f3])).



% SZS output end Proof for prop_3.p