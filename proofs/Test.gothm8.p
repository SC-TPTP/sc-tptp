% SZS output start Proof for fol_1.p


fof(c_fol_1_p, conjecture, ~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))))).

fof(f8, plain, [~(~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))))), (! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))), (! [Y7, Z9] : ((p_4(X5_11) & ~(p_4(Y7))))), (! [Z9] : ((p_4(X5_11) & ~(p_4(X5_11))))), (p_4(X5_11) & ~(p_4(X5_11))), p_4(X5_11), ~(p_4(X5_11))] --> [], inference(leftHyp, [5, 6], [])).

fof(f7, plain, [~(~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))))), (! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))), (! [Y7, Z9] : ((p_4(X5_11) & ~(p_4(Y7))))), (! [Z9] : ((p_4(X5_11) & ~(p_4(X5_11))))), (p_4(X5_11) & ~(p_4(X5_11)))] --> [], inference(leftAnd, [4], [f8])).

fof(f6, plain, [~(~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))))), (! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))), (! [Y7, Z9] : ((p_4(X5_11) & ~(p_4(Y7))))), (! [Z9] : ((p_4(X5_11) & ~(p_4(X5_11)))))] --> [], inference(leftForall, [3, $fot(Goeland_I_0)], [f7])).

fof(f5, plain, [~(~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))))), (! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))), (! [Y7, Z9] : ((p_4(X5_11) & ~(p_4(Y7)))))] --> [], inference(leftForall, [2, $fot(X5_11)], [f6])).

fof(f4, plain, [~(~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))))), (! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7)))))] --> [], inference(leftForall, [1, $fot(X5_11)], [f5])).

fof(f3, plain, [~(~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7)))))))] --> [], inference(leftNotNot, [0], [f4])).

fof(f2, plain, [~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))))] --> [~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))))], inference(hyp, [0, 0], [])).

fof(f1, plain, [] --> [~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7)))))), ~(~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7)))))))], inference(rightNot, [1], [f2])).

fof(f0, plain, [] --> [~((! [X5, Y7, Z9] : ((p_4(X5) & ~(p_4(Y7))))))], inference(cut, [1, 0], [f1, f3])).



% SZS output end Proof for fol_1.p