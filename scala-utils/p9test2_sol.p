
fof(a1, axiom, [] --> [(! [X]: (f(X) & b) | ! [Y]: (b & g(Y)))]).
fof(a2, axiom, [] --> [~b]).
fof(a1_pre, plain, [] --> [! [V1]: ! [V0]: ((f(V1) & b) | (b & g(V0)))], inference(rightPrenex, [status(thm), 0, 0], [a1])).
fof(a1_V1, plain, [] --> [! [V0]: ((f(V1) & b) | (b & g(V0)))], inference(instForall, [status(thm), 0, $fot(V1)], [a1_pre])).
fof(a1_V0, plain, [] --> [((f(V1) & b) | (b & g(V0)))], inference(instForall, [status(thm), 0, $fot(V0)], [a1_V1])).
fof(a2_pre, plain, [] --> [~b], inference(rightPrenex, [status(thm), 0, 0], [a2])).
fof(a2weak1_1, plain, [! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [~b], inference(leftWeaken, [status(thm), 0], [a2_pre])).
fof(ts_cla1, plain, [(Ts1(V1) <=> (f(V1) & b))] --> [f(V1),~Ts1(V1)], inference(clausify, [status(thm), 0], [])).
fof(ts_claQ1_1, plain, [! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [f(V1),~Ts1(V1)], inference(leftForall, [status(thm), 0, $fot(V1)], [ts_cla1])).
fof(ts_clb1, plain, [(Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts1(V1)], inference(clausify, [status(thm), 0], [])).
fof(ts_clbQ1_1, plain, [! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts1(V1)], inference(leftForall, [status(thm), 0, $fot(V1)], [ts_clb1])).
fof(ts_ax_just1, plain, [(Ts1(V1) <=> (f(V1) & b))] --> [(Ts1(V1) | (b & g(V0)))], inference(rightSubstIff, [status(thm), 0, 0, $fof((HOLE | (b & g(V0)))), 'HOLE'], [a1_V0])).
fof(ts_axQ1_1, plain, [! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [(Ts1(V1) | (b & g(V0)))], inference(leftForall, [status(thm), 0, $fot(V1)], [ts_ax_just1])).
fof(a2weak2_1, plain, [! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [~b], inference(leftWeaken, [status(thm), 0], [a2weak1_1])).
fof(ts_claQ1_1weak2_1, plain, [! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [f(V1),~Ts1(V1)], inference(leftWeaken, [status(thm), 0], [ts_claQ1_1])).
fof(ts_clbQ1_1weak2_1, plain, [! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts1(V1)], inference(leftWeaken, [status(thm), 0], [ts_clbQ1_1])).
fof(ts_cla2, plain, [(Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts2(V0)], inference(clausify, [status(thm), 0], [])).
fof(ts_claQ2_1, plain, [! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts2(V0)], inference(leftForall, [status(thm), 0, $fot(V0)], [ts_cla2])).
fof(ts_clb2, plain, [(Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [g(V0),~Ts2(V0)], inference(clausify, [status(thm), 0], [])).
fof(ts_clbQ2_1, plain, [! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [g(V0),~Ts2(V0)], inference(leftForall, [status(thm), 0, $fot(V0)], [ts_clb2])).
fof(ts_ax_just2, plain, [(Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [(Ts1(V1) | Ts2(V0))], inference(rightSubstIff, [status(thm), 0, 0, $fof((Ts1(V1) | HOLE)), 'HOLE'], [ts_axQ1_1])).
fof(ts_axQ2_1, plain, [! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [(Ts1(V1) | Ts2(V0))], inference(leftForall, [status(thm), 0, $fot(V0)], [ts_ax_just2])).
fof(a2weak3_2, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [~b], inference(leftWeaken, [status(thm), 0], [a2weak2_1])).
fof(ts_claQ1_1weak3_2, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [f(V1),~Ts1(V1)], inference(leftWeaken, [status(thm), 0], [ts_claQ1_1weak2_1])).
fof(ts_clbQ1_1weak3_2, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts1(V1)], inference(leftWeaken, [status(thm), 0], [ts_clbQ1_1weak2_1])).
fof(ts_claQ2_1weak3_2, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts2(V0)], inference(leftWeaken, [status(thm), 0], [ts_claQ2_1])).
fof(ts_clbQ2_1weak3_2, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [g(V0),~Ts2(V0)], inference(leftWeaken, [status(thm), 0], [ts_clbQ2_1])).
fof(ts_clac3, plain, [(Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts2(V0),Ts1(V1),~Ts3(V1,V0)], inference(clausify, [status(thm), 0], [])).
fof(ts_claQ3_1, plain, [! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts2(V0),Ts1(V1),~Ts3(V1,V0)], inference(leftForall, [status(thm), 0, $fot(V0)], [ts_clac3])).
fof(ts_claQ3_2, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts2(V0),Ts1(V1),~Ts3(V1,V0)], inference(leftForall, [status(thm), 0, $fot(V1)], [ts_claQ3_1])).
fof(ts_ax_just3, plain, [(Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts3(V1,V0)], inference(rightSubstIff, [status(thm), 0, 0, $fof(HOLE), 'HOLE'], [ts_axQ2_1])).
fof(ts_axQ3_1, plain, [! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts3(V1,V0)], inference(leftForall, [status(thm), 0, $fot(V0)], [ts_ax_just3])).
fof(ts_axQ3_2, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts3(V1,V0)], inference(leftForall, [status(thm), 0, $fot(V1)], [ts_axQ3_1])).
fof(ts_claQ1_1_p9r, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [f(V0),~Ts1(V0)], inference(instMult, [status(thm), [tuple3('V1', $fot(V0), [])]], [ts_claQ1_1weak3_2])).
fof(ts_clbQ1_1_p9r, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts1(V0)], inference(instMult, [status(thm), [tuple3('V1', $fot(V0), [])]], [ts_clbQ1_1weak3_2])).
fof(ts_claQ2_1_p9r, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts2(V0)], inference(instMult, [status(thm), [tuple3('V0', $fot(V0), [])]], [ts_claQ2_1weak3_2])).
fof(ts_clbQ2_1_p9r, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [g(V0),~Ts2(V0)], inference(instMult, [status(thm), [tuple3('V0', $fot(V0), [])]], [ts_clbQ2_1weak3_2])).
fof(ts_claQ3_2_p9r, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts2(V0),Ts1(V0),~Ts3(V0,V0)], inference(instMult, [status(thm), [tuple3('V1', $fot(V0), []), tuple3('V0', $fot(V0), [])]], [ts_claQ3_2])).
fof(ts_axQ3_2_p9r, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts3(V0,V0)], inference(instMult, [status(thm), [tuple3('V1', $fot(V0), []), tuple3('V0', $fot(V0), [])]], [ts_axQ3_2])).
fof(a2_p9r, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [~b], inference(instMult, [status(thm), []], [a2weak3_2])).
fof(13, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts2(V100),Ts1(V100),~Ts3(V100,V100)], inference(instMult, [status(thm), [tuple3('V0', $fot(V100), [])]], [ts_claQ3_2_p9r])).
fof(14, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts1(V100)], inference(instMult, [status(thm), [tuple3('V0', $fot(V100), [])]], [ts_clbQ1_1_p9r])).
fof(15, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts2(V100),~Ts3(V100,V100),b], inference(res, [status(thm), 1], [13, 14])).
fof(4, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts2(V0),~Ts3(V0,V0),b], inference(instMult, [status(thm), [tuple3('V100', $fot(V0), [])]], [15])).
fof(16, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts2(V100),~Ts3(V100,V100),b], inference(instMult, [status(thm), [tuple3('V0', $fot(V100), [])]], [4])).
fof(17, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,~Ts2(V100)], inference(instMult, [status(thm), [tuple3('V0', $fot(V100), [])]], [ts_claQ2_1_p9r])).
fof(18, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [~Ts3(V100,V100),b,b], inference(res, [status(thm), 0], [16, 17])).
fof(7, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [~Ts3(V0,V0),b,b], inference(instMult, [status(thm), [tuple3('V100', $fot(V0), [])]], [18])).
fof(19, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [~Ts3(V100,V100),b,b], inference(instMult, [status(thm), [tuple3('V0', $fot(V100), [])]], [7])).
fof(20, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [Ts3(V100,V100)], inference(instMult, [status(thm), [tuple3('V0', $fot(V100), [])]], [ts_axQ3_2_p9r])).
fof(10, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b,b], inference(res, [status(thm), 0], [20, 19])).
fof(a011, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [b], inference(rightWeaken, [status(thm), 0], [10])).
fof(ts_sp3, plain, [! [V1]: ! [V0]: (Ts3(V1,V0) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [], inference(res, [status(thm), 0], [a011, a2_p9r])).
fof(ts_inst3, plain, [! [V1]: ! [V0]: ((Ts1(V1) | Ts2(V0)) <=> (Ts1(V1) | Ts2(V0))),! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [], inference(instPred, [status(thm), 'Ts3', $fof((Ts1(V1) | Ts2(V0))), ['V1','V0']], [ts_sp3])).
fof(ts_sp3, plain, [! [V0]: (Ts2(V0) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [], inference(elimIffRefl, [status(thm), 0], [ts_inst3])).
fof(ts_inst3, plain, [! [V0]: ((b & g(V0)) <=> (b & g(V0))),! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [], inference(instPred, [status(thm), 'Ts2', $fof((b & g(V0))), ['V0']], [ts_sp3])).
fof(ts_sp3, plain, [! [V1]: (Ts1(V1) <=> (f(V1) & b))] --> [], inference(elimIffRefl, [status(thm), 0], [ts_inst3])).
fof(ts_inst3, plain, [! [V1]: ((f(V1) & b) <=> (f(V1) & b))] --> [], inference(instPred, [status(thm), 'Ts1', $fof((f(V1) & b)), ['V1']], [ts_sp3])).
fof(prenex_sp, plain, [] --> [], inference(elimIffRefl, [status(thm), 0], [ts_inst3])).