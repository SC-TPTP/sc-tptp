% SZS output start Proof for eq1.p


fof(c_eq1_p, conjecture, ((a_1 = b_2) => (p_3(a_1) => p_3(b_2)))).

fof(f5, plain, [~(((a_1 = b_2) => (p_3(a_1) => p_3(b_2)))), (a_1 = b_2), ~((p_3(a_1) => p_3(b_2))), p_3(a_1), ~(p_3(b_2))] --> [], inference(congruence, param(), [])).

fof(f4, plain, [~(((a_1 = b_2) => (p_3(a_1) => p_3(b_2)))), (a_1 = b_2), ~((p_3(a_1) => p_3(b_2)))] --> [], inference(leftNotImp, param(2), [f5])).

fof(f3, plain, [~(((a_1 = b_2) => (p_3(a_1) => p_3(b_2))))] --> [], inference(leftNotImp, param(0), [f4])).

fof(f2, plain, [((a_1 = b_2) => (p_3(a_1) => p_3(b_2)))] --> [((a_1 = b_2) => (p_3(a_1) => p_3(b_2)))], inference(hyp, param(0, 0), [])).

fof(f1, plain, [] --> [((a_1 = b_2) => (p_3(a_1) => p_3(b_2))), ~(((a_1 = b_2) => (p_3(a_1) => p_3(b_2))))], inference(rightNot, param(1), [f2])).

fof(f0, plain, [] --> [((a_1 = b_2) => (p_3(a_1) => p_3(b_2)))], inference(cut, param(1, 0), [f1, f3])).



% SZS output end Proof for eq1.p