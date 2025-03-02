
fof(a1, axiom, [] --> [(f(V0) & (b & c))]).
fof(a2, axiom, [] --> [~b]).
fof(a2weak1_0, plain, [(Ts1 <=> (b & c))] --> [~b], inference(leftWeaken, [status(thm), 0], [a2])).
fof(ts_cla1, plain, [(Ts1 <=> (b & c))] --> [b,~Ts1], inference(clausify, [status(thm), 0], [])).
fof(ts_clb1, plain, [(Ts1 <=> (b & c))] --> [c,~Ts1], inference(clausify, [status(thm), 0], [])).
fof(ts_ax_just1, plain, [(Ts1 <=> (b & c))] --> [(f(V0) & Ts1)], inference(rightSubstIff, [status(thm), 0, 0, $fof((f(V0) & HOLE)), 'HOLE'], [a1])).
fof(a2weak2_1, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [~b], inference(leftWeaken, [status(thm), 0], [a2weak1_0])).
fof(ts_cla1weak2_1, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [b,~Ts1], inference(leftWeaken, [status(thm), 0], [ts_cla1])).
fof(ts_clb1weak2_1, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [c,~Ts1], inference(leftWeaken, [status(thm), 0], [ts_clb1])).
fof(ts_cla2, plain, [(Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [f(V0),~Ts2(V0)], inference(clausify, [status(thm), 0], [])).
fof(ts_claQ2_1, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [f(V0),~Ts2(V0)], inference(leftForall, [status(thm), 0, $fot(V0)], [ts_cla2])).
fof(ts_clb2, plain, [(Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [Ts1,~Ts2(V0)], inference(clausify, [status(thm), 0], [])).
fof(ts_clbQ2_1, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [Ts1,~Ts2(V0)], inference(leftForall, [status(thm), 0, $fot(V0)], [ts_clb2])).
fof(ts_ax_just2, plain, [(Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [Ts2(V0)], inference(rightSubstIff, [status(thm), 0, 0, $fof(HOLE), 'HOLE'], [ts_ax_just1])).
fof(ts_axQ2_1, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [Ts2(V0)], inference(leftForall, [status(thm), 0, $fot(V0)], [ts_ax_just2])).
fof(10, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [Ts2(V100)], inference(instMult, [status(thm), [tuple3('V0', $fot(V100), [])]], [ts_axQ2_1])).
fof(11, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [Ts1,~Ts2(V100)], inference(instMult, [status(thm), [tuple3('V0', $fot(V100), [])]], [ts_clbQ2_1])).
fof(7, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [Ts1], inference(res, [status(thm), 0], [10, 11])).
fof(8, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [~Ts1], inference(res, [status(thm), 0], [ts_cla1weak2_1, a2weak2_1])).
fof(ts_sp2, plain, [! [V0]: (Ts2(V0) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [], inference(res, [status(thm), 0], [7, 8])).
fof(ts_inst2, plain, [! [V0]: ((f(V0) & Ts1) <=> (f(V0) & Ts1)),(Ts1 <=> (b & c))] --> [], inference(instPred, [status(thm), 'Ts2', $fof((f(V0) & Ts1)), ['V0']], [ts_sp2])).
fof(ts_sp2, plain, [(Ts1 <=> (b & c))] --> [], inference(elimIffRefl, [status(thm), 0], [ts_inst2])).
fof(ts_inst2, plain, [((b & c) <=> (b & c))] --> [], inference(instPred, [status(thm), 'Ts1', $fof((b & c)), []], [ts_sp2])).
fof(ts_elim2, plain, [] --> [], inference(elimIffRefl, [status(thm), 0], [ts_inst2])).