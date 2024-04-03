% SZS output start Proof for prop_1.p


fof(c_prop_1_p, conjecture, (~(a) | a)).

fof(f5, plain, [~((~(a) | a)), ~(~(a)), ~(a), a] --> [], inference(leftHyp, param(3, 2), [])).

fof(f4, plain, [~((~(a) | a)), ~(~(a)), ~(a)] --> [], inference(leftNotNot, param(1), [f5])).

fof(f3, plain, [~((~(a) | a))] --> [], inference(leftNotOr, param(0), [f4])).

fof(f2, plain, [(~(a) | a)] --> [(~(a) | a)], inference(hyp, param(0, 0), [])).

fof(f1, plain, [] --> [(~(a) | a), ~((~(a) | a))], inference(rightNot, param(1), [f2])).

fof(f0, plain, [] --> [(~(a) | a)], inference(cut, param(1, 0), [f1, f3])).



% SZS output end Proof for prop_1.p