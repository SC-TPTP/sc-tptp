
fof(a1, axiom, [] --> [a,b,c]).
fof(a2, axiom, [] --> [~b]).
fof(a3, axiom, [] --> [~c,b]).
fof(conj, conjecture, [] --> [a]).
fof(a1_nc, plain, [~a] --> [a,b,c], inference(leftWeaken, [status(thm), 0], [a1])).
fof(a2_nc, plain, [~a] --> [~b], inference(leftWeaken, [status(thm), 0], [a2])).
fof(a3_nc, plain, [~a] --> [~c,b], inference(leftWeaken, [status(thm), 0], [a3])).
fof(neg_conjecture, assumption, [~a] --> [~a], inference(hyp, [status(thm), 0], [])).
fof(5, plain, [~a] --> [~c], inference(res, [status(thm), 1], [a3_nc, a2_nc])).
fof(a07, plain, [~a] --> [b,c], inference(res, [status(thm), 0], [a1_nc, neg_conjecture])).
fof(a17, plain, [~a] --> [c], inference(res, [status(thm), 0], [a07, a2_nc])).
fof(sp_neg_conj, plain, [~a] --> [], inference(res, [status(thm), 0], [a17, 5])).
fof(nc_elim_1, assumption, [a] --> [a], inference(hyp, [status(thm), 0], [])).
fof(nc_elim_2, plain, [] --> [~a,a], inference(rightNot, [status(thm), 1], [nc_elim_1])).
fof(nc_elim_3, plain, [] --> [a], inference(cut, [status(thm), 0], [nc_elim_2, sp_neg_conj])).