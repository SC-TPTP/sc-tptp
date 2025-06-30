
fof(c, conjecture, [] --> [(t_a0 <=> t_a0)]).
fof(neg_conjecture, assumption, [~(t_a0 <=> t_a0)] --> [~(t_a0 <=> t_a0)], inference(hyp, [status(thm), 0], [])).
fof(r1, let, ~(t_a0 <=> t_a0)).
fof(neg_conjecture_nnf, plain, [$r1] --> [((t_a0 & ~t_a0) | (t_a0 & ~t_a0))], inference(rightNnf, [status(thm), 0, 0], [neg_conjecture])).
fof(neg_conjecture_pre, plain, [$r1] --> [((t_a0 & ~t_a0) | (t_a0 & ~t_a0))], inference(rightPrenex, [status(thm), 0, 0], [neg_conjecture_nnf])).
fof(ts_cla1, plain, [(Ts1 <=> (t_a0 & ~t_a0)),$r1] --> [~Ts1,t_a0], inference(clausify, [status(thm), 0], [])).
fof(ts_clb1, plain, [(Ts1 <=> (t_a0 & ~t_a0)),$r1] --> [~Ts1,~t_a0], inference(clausify, [status(thm), 0], [])).
fof(ts_ax_just1, plain, [(Ts1 <=> (t_a0 & ~t_a0)),$r1] --> [(Ts1 | (t_a0 & ~t_a0))], inference(rightSubstIff, [status(thm), 0, 1, $fof((HOLE | (t_a0 & ~t_a0))), 'HOLE'], [neg_conjecture_pre])).
fof(r2, let, (Ts1 <=> (t_a0 & ~t_a0))).
fof(ts_cla1weak2_1, plain, [(Ts2 <=> (t_a0 & ~t_a0)),$r2,$r1] --> [~Ts1,t_a0], inference(leftWeaken, [status(thm), 0], [ts_cla1])).
fof(ts_clb1weak2_2, plain, [(Ts2 <=> (t_a0 & ~t_a0)),$r2,$r1] --> [~Ts1,~t_a0], inference(leftWeaken, [status(thm), 0], [ts_clb1])).
fof(ts_cla2, plain, [(Ts2 <=> (t_a0 & ~t_a0)),$r2,$r1] --> [~Ts2,t_a0], inference(clausify, [status(thm), 0], [])).
fof(ts_clb2, plain, [(Ts2 <=> (t_a0 & ~t_a0)),$r2,$r1] --> [~Ts2,~t_a0], inference(clausify, [status(thm), 0], [])).
fof(ts_ax_just2, plain, [(Ts2 <=> (t_a0 & ~t_a0)),$r2,$r1] --> [(Ts1 | Ts2)], inference(rightSubstIff, [status(thm), 0, 1, $fof((Ts1 | HOLE)), 'HOLE'], [ts_ax_just1])).
fof(r3, let, (Ts2 <=> (t_a0 & ~t_a0))).
fof(ts_cla1weak3_1, plain, [(Ts3 <=> (Ts1 | Ts2)),$r3,$r2,$r1] --> [~Ts1,t_a0], inference(leftWeaken, [status(thm), 0], [ts_cla1weak2_1])).
fof(ts_clb1weak3_2, plain, [(Ts3 <=> (Ts1 | Ts2)),$r3,$r2,$r1] --> [~Ts1,~t_a0], inference(leftWeaken, [status(thm), 0], [ts_clb1weak2_2])).
fof(ts_cla2weak3_3, plain, [(Ts3 <=> (Ts1 | Ts2)),$r3,$r2,$r1] --> [~Ts2,t_a0], inference(leftWeaken, [status(thm), 0], [ts_cla2])).
fof(ts_clb2weak3_4, plain, [(Ts3 <=> (Ts1 | Ts2)),$r3,$r2,$r1] --> [~Ts2,~t_a0], inference(leftWeaken, [status(thm), 0], [ts_clb2])).
fof(ts_clac3, plain, [(Ts3 <=> (Ts1 | Ts2)),$r3,$r2,$r1] --> [~Ts3,Ts1,Ts2], inference(clausify, [status(thm), 0], [])).
fof(ts_ax_just3, plain, [(Ts3 <=> (Ts1 | Ts2)),$r3,$r2,$r1] --> [Ts3], inference(rightSubstIff, [status(thm), 0, 1, $fof(HOLE), 'HOLE'], [ts_ax_just2])).
fof(r4, let, (Ts3 <=> (Ts1 | Ts2))).
fof(ts_cla1_p9r, plain, [$r4,$r3,$r2,$r1] --> [~Ts1,t_a0], inference(instMult, [status(thm), []], [ts_cla1weak3_1])).
fof(ts_clb1_p9r, plain, [$r4,$r3,$r2,$r1] --> [~Ts1,~t_a0], inference(instMult, [status(thm), []], [ts_clb1weak3_2])).
fof(ts_cla2_p9r, plain, [$r4,$r3,$r2,$r1] --> [~Ts2,t_a0], inference(instMult, [status(thm), []], [ts_cla2weak3_3])).
fof(ts_clb2_p9r, plain, [$r4,$r3,$r2,$r1] --> [~Ts2,~t_a0], inference(instMult, [status(thm), []], [ts_clb2weak3_4])).
fof(ts_clac3_p9r, plain, [$r4,$r3,$r2,$r1] --> [~Ts3,Ts1,Ts2], inference(instMult, [status(thm), []], [ts_clac3])).
fof(ts_ax_just3_p9r, plain, [$r4,$r3,$r2,$r1] --> [Ts3], inference(instMult, [status(thm), []], [ts_ax_just3])).
fof(8, plain, [$r4,$r3,$r2,$r1] --> [Ts1,Ts2], inference(res, [status(thm), 0], [ts_ax_just3_p9r, ts_clac3_p9r])).
fof(9, plain, [$r4,$r3,$r2,$r1] --> [Ts1,t_a0], inference(res, [status(thm), 1], [8, ts_cla2_p9r])).
fof(10, plain, [$r4,$r3,$r2,$r1] --> [Ts1,~Ts2], inference(res, [status(thm), 1], [9, ts_clb2_p9r])).
fof(a011, plain, [$r4,$r3,$r2,$r1] --> [Ts1,Ts1], inference(res, [status(thm), 1], [8, 10])).
fof(11, plain, [$r4,$r3,$r2,$r1] --> [Ts1], inference(rightWeaken, [status(thm), 0], [a011])).
fof(12, plain, [$r4,$r3,$r2,$r1] --> [~t_a0], inference(res, [status(thm), 0], [11, ts_clb1_p9r])).
fof(a013, plain, [$r4,$r3,$r2,$r1] --> [t_a0], inference(res, [status(thm), 0], [11, ts_cla1_p9r])).
fof(ts_sp3, plain, [$r4,$r3,$r2,$r1] --> [], inference(res, [status(thm), 0], [a013, 12])).
fof(ts_inst3, plain, [((Ts1 | Ts2) <=> (Ts1 | Ts2)),$r3,$r2,$r1] --> [], inference(instPred, [status(thm), 'Ts3', $fof((Ts1 | Ts2)), []], [ts_sp3])).
fof(ts_sp2, plain, [$r3,$r2,$r1] --> [], inference(elimIffRefl, [status(thm), 0], [ts_inst3])).
fof(ts_inst2, plain, [((t_a0 & ~t_a0) <=> (t_a0 & ~t_a0)),$r2,$r1] --> [], inference(instPred, [status(thm), 'Ts2', $fof((t_a0 & ~t_a0)), []], [ts_sp2])).
fof(ts_sp1, plain, [$r2,$r1] --> [], inference(elimIffRefl, [status(thm), 0], [ts_inst2])).
fof(ts_inst1, plain, [((t_a0 & ~t_a0) <=> (t_a0 & ~t_a0)),$r1] --> [], inference(instPred, [status(thm), 'Ts1', $fof((t_a0 & ~t_a0)), []], [ts_sp1])).
fof(sp_neg_conj, plain, [$r1] --> [], inference(elimIffRefl, [status(thm), 0], [ts_inst1])).
fof(nc_elim_1, assumption, [(t_a0 <=> t_a0)] --> [(t_a0 <=> t_a0)], inference(hyp, [status(thm), 0], [])).
fof(nc_elim_2, plain, [] --> [(t_a0 <=> t_a0),~(t_a0 <=> t_a0)], inference(rightNot, [status(thm), 1], [nc_elim_1])).
fof(nc_elim_3, plain, [] --> [(t_a0 <=> t_a0)], inference(cut, [status(thm), 1], [nc_elim_2, sp_neg_conj])).