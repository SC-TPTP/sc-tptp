% SZS output start Proof for fol_2.p


fof(c_fol_2_p, conjecture, (? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4)))))).

fof(f6, plain, [~((? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4)))))), ~((p_2(a_3) => (p_2(a_3) & p_2(b_4)))), p_2(a_3), ~((p_2(a_3) & p_2(b_4))), ~(p_2(a_3))] --> [], inference(leftHyp, [4, 2], [])).

fof(f9, plain, [~((? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4)))))), ~((p_2(a_3) => (p_2(a_3) & p_2(b_4)))), p_2(a_3), ~((p_2(a_3) & p_2(b_4))), ~(p_2(b_4)), ~((p_2(b_4) => (p_2(a_3) & p_2(b_4)))), p_2(b_4)] --> [], inference(leftHyp, [6, 4], [])).

fof(f8, plain, [~((? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4)))))), ~((p_2(a_3) => (p_2(a_3) & p_2(b_4)))), p_2(a_3), ~((p_2(a_3) & p_2(b_4))), ~(p_2(b_4)), ~((p_2(b_4) => (p_2(a_3) & p_2(b_4))))] --> [], inference(leftNotImp, [5], [f9])).

fof(f7, plain, [~((? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4)))))), ~((p_2(a_3) => (p_2(a_3) & p_2(b_4)))), p_2(a_3), ~((p_2(a_3) & p_2(b_4))), ~(p_2(b_4))] --> [], inference(leftNotEx, [0, $fot(b_4)], [f8])).

fof(f5, plain, [~((? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4)))))), ~((p_2(a_3) => (p_2(a_3) & p_2(b_4)))), p_2(a_3), ~((p_2(a_3) & p_2(b_4)))] --> [], inference(leftNotAnd, [3], [f6, f7])).

fof(f4, plain, [~((? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4)))))), ~((p_2(a_3) => (p_2(a_3) & p_2(b_4))))] --> [], inference(leftNotImp, [1], [f5])).

fof(f3, plain, [~((? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4))))))] --> [], inference(leftNotEx, [0, $fot(a_3)], [f4])).

fof(f2, plain, [(? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4)))))] --> [(? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4)))))], inference(hyp, [0, 0], [])).

fof(f1, plain, [] --> [(? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4))))), ~((? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4))))))], inference(rightNot, [1], [f2])).

fof(f0, plain, [] --> [(? [X5] : ((p_2(X5) => (p_2(a_3) & p_2(b_4)))))], inference(cut, [1, 0], [f1, f3])).



% SZS output end Proof for fol_2.p