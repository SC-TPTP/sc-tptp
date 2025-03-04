
fof(a1, axiom, [] --> [? [X]: (f(X) & b)]).
fof(a2, axiom, [] --> [~b]).
fof(a2weak0_1, plain, [# [X]: ((f(X) & b)) = SkX] --> [~b], inference(leftWeaken, [status(thm), 0], [a2])).
fof(sko_iff0, plain, [] --> [(? [X]: (f(X) & b) <=> (f(# [X]: ((f(X) & b))) & b))], inference(existsIffEpsilon, [status(thm), 0], [])).
fof(eps_subst0, plain, [(? [X]: (f(X) & b) <=> (f(# [X]: ((f(X) & b))) & b))] --> [(f(# [X]: ((f(X) & b))) & b)], inference(rightSubstPred, [status(thm), 0, 0, 'HOLE', $fof(HOLE)], [a1])).
fof(sko_cut0, plain, [] --> [(f(# [X]: ((f(X) & b))) & b)], inference(cut, [status(thm), 0], [sko_iff0, eps_subst0])).
fof(sko_subst0, plain, [# [X]: ((f(X) & b)) = SkX] --> [(f(SkX) & b)], inference(rightSubstFun, [status(thm), 0, 0, 'THOLE', $fof((f(THOLE) & b))], [sko_cut0])).
fof(a1_pre, plain, [# [X]: ((f(X) & b)) = SkX] --> [(f(SkX) & b)], inference(rightPrenex, [status(thm), 0, 0], [sko_subst0])).
fof(a2_pre, plain, [# [X]: ((f(X) & b)) = SkX] --> [~b], inference(rightPrenex, [status(thm), 0, 0], [a2weak0_1])).
fof(a2weak1_1, plain, [(Ts1 <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [~b], inference(leftWeaken, [status(thm), 0], [a2_pre])).
fof(ts_cla1, plain, [(Ts1 <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [f(SkX),~Ts1], inference(clausify, [status(thm), 0], [])).
fof(ts_clb1, plain, [(Ts1 <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [b,~Ts1], inference(clausify, [status(thm), 0], [])).
fof(ts_ax_just1, plain, [(Ts1 <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [Ts1], inference(rightSubstIff, [status(thm), 0, 0, $fof(HOLE), 'HOLE'], [a1_pre])).
fof(ts_cla1_p9r, plain, [(Ts1 <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [f(SkX),~Ts1], inference(instMult, [status(thm), []], [ts_cla1])).
fof(ts_clb1_p9r, plain, [(Ts1 <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [b,~Ts1], inference(instMult, [status(thm), []], [ts_clb1])).
fof(ts_ax_just1_p9r, plain, [(Ts1 <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [Ts1], inference(instMult, [status(thm), []], [ts_ax_just1])).
fof(a2_p9r, plain, [(Ts1 <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [~b], inference(instMult, [status(thm), []], [a2weak1_1])).
fof(5, plain, [(Ts1 <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [b], inference(res, [status(thm), 0], [ts_ax_just1_p9r, ts_clb1_p9r])).
fof(ts_sp1, plain, [(Ts1 <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [], inference(res, [status(thm), 0], [5, a2_p9r])).
fof(ts_inst1, plain, [((f(SkX) & b) <=> (f(SkX) & b)),# [X]: ((f(X) & b)) = SkX] --> [], inference(instPred, [status(thm), 'Ts1', $fof((f(SkX) & b)), []], [ts_sp1])).
fof(sko_sp0, plain, [# [X]: ((f(X) & b)) = SkX] --> [], inference(elimIffRefl, [status(thm), 0], [ts_inst1])).
fof(sko_inst0, plain, [# [X]: ((f(X) & b)) = # [X]: ((f(X) & b))] --> [], inference(instFun, [status(thm), 'SkX', $fot(# [X]: ((f(X) & b))), []], [sko_sp0])).
fof(sko_elim0, plain, [] --> [], inference(elimEqRefl, [status(thm), 0], [sko_inst0])).