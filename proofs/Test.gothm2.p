% SZS output start Proof for drinkers.p


fof(c_drinkers_p, conjecture, (? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6))))))).

fof(f8, plain, [~((? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6))))))), ~((d_2(X4_8) => (! [Y6] : (d_2(Y6))))), d_2(X4_8), ~((! [Y6] : (d_2(Y6)))), ~(d_2(Sko_0_15)), ~((d_2(Sko_0_15) => (! [Y6] : (d_2(Y6))))), d_2(Sko_0_15)] --> [], inference(leftHyp, [6, 4], [])).

fof(f7, plain, [~((? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6))))))), ~((d_2(X4_8) => (! [Y6] : (d_2(Y6))))), d_2(X4_8), ~((! [Y6] : (d_2(Y6)))), ~(d_2(Sko_0_15)), ~((d_2(Sko_0_15) => (! [Y6] : (d_2(Y6)))))] --> [], inference(leftNotImp, [5], [f8])).

fof(f6, plain, [~((? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6))))))), ~((d_2(X4_8) => (! [Y6] : (d_2(Y6))))), d_2(X4_8), ~((! [Y6] : (d_2(Y6)))), ~(d_2(Sko_0_15))] --> [], inference(leftNotEx, [0, $fot(Sko_0_15)], [f7])).

fof(f5, plain, [~((? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6))))))), ~((d_2(X4_8) => (! [Y6] : (d_2(Y6))))), d_2(X4_8), ~((! [Y6] : (d_2(Y6))))] --> [], inference(leftNotForall, [3, $fot(Sko_0_15)], [f6])).

fof(f4, plain, [~((? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6))))))), ~((d_2(X4_8) => (! [Y6] : (d_2(Y6)))))] --> [], inference(leftNotImp, [1], [f5])).

fof(f3, plain, [~((? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6)))))))] --> [], inference(leftNotEx, [0, $fot(X4_8)], [f4])).

fof(f2, plain, [(? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6))))))] --> [(? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6))))))], inference(hyp, [0, 0], [])).

fof(f1, plain, [] --> [(? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6)))))), ~((? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6)))))))], inference(rightNot, [1], [f2])).

fof(f0, plain, [] --> [(? [X4] : ((d_2(X4) => (! [Y6] : (d_2(Y6))))))], inference(cut, [1, 0], [f1, f3])).



% SZS output end Proof for drinkers.p