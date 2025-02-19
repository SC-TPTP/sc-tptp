fof(test_minimaliste, conjecture,
    (! [X] : p(X)) => (p(a) & (p(b)))    
).

% NC
fof(nc_step, assumption, [~(! [X]: p(X) => (p(a) & p(b)))] --> [], inference(negated_conjecture, [status(thm)], [nnf_step])).

% NNF
fof(nnf_step, plain, [(! [X]: p(X) & (~p(a) | ~p(b)))] --> [], inference(nnf, [status(thm)], [prenex_step])).
% Parent : NC_step 

% Prenex
fof(prenex_step, assumption, [! [X]: (p(X) & (~p(a) | ~p(b)))] --> [], inference(prenex_step, [status(thm)], [i0])).
% Parent : nnf_step + unfold 
fof(i0, plain, [] --> [(p(V0) & (~p(a) | ~p(b)))], inference(rightForall, [status(thm), 0, $fot(X)], [TODO])).

% Tseitin Variables
% Voir renmage
fof(tsStep1, let, (ladr3 <=> (~p(a) | ~p(b)))).
fof(tsStep0, let, ! [V0]: (X1(V0) <=> (p(V0) & ladr3))).

% Tseitin explanation
fof(tsStep1Expl, plain, [tsStep0] --> [(ladr3 <=> (~p(a) | ~p(b)))], inference(rightSubstIff, [status(thm), 0, $fof((ladr3 <=> (~p(a) | ~p(b)))), $fot(ladr3)], [tsStep1Expl0])).
fof(tsStep0Expl, plain, [] --> [! [V0_1]: (X1(V0_1) <=> (p(V0_1) & ladr3))], inference(rightSubstIff, [status(thm), 0, $fof(! [V0_1]: (X1(V0_1) <=> (p(V0_1) & ladr3))), $fot(X1(V0))], [tsStep0Expl-1])).
% Inverser sens : 0 before 1 
% add assumption tstep 0 dans step0 eplain, ts0 + 1 dans explain 1 
% parent de 0 = prenex
[tsstep0] --> [pv0 et ladr3]
suivi d'un leftForall

[ts1, ts0] --> [x1(v0)] 


% passer les axiom à p9
% ts1, ts0 à gauche

% Ce que je done à P9
fof(a1, axiom, X1(X)).
fof(a2, axiom, ~ladr3 | ~p(a) | ~p(b)).
fof(a3, axiom, ladr3 | ~~p(a)).
fof(a4, axiom, ladr3 | ~~p(b)).
fof(a5, axiom, X1(X) | ~p(X) | ~ladr3).
fof(a6, axiom, ~X1(X) | p(X)).
fof(a7, axiom, ~X1(X) | ladr3).

% ce qu'on réecrit en sortie
..., (tseiTin + rule, quel ts0, pas de parent)
fof('3', axiom, [ts1, ts0] --> [~(X1(V0)) | p(V0)]). 
fof('4', axiom, [ts1, ts0] --> [X1(V0)]).            
fof('6', axiom, [ts1, ts0] --> [~(X1(V0)) | ladr3]).
fof('7', axiom, [ts1, ts0] --> [~(ladr3) | ~(p(a)) | ~(p(b))]).


% P9
fof('15', plain, [ts1, ts0] --> [~(X1(V100)) | p(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['3'])).
fof('16', plain, [ts1, ts0] --> [X1(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['4'])).
fof('17', plain, [ts1, ts0] --> [p(V100)], inference(res, [status(thm), 0, 0], ['15', '16'])).
fof('10', plain, [ts1, ts0] --> [p(V0)], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)]]], ['17'])).
fof('18', plain, [ts1, ts0] --> [~(X1(V100)) | ladr3], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['6'])).
fof('19', plain, [ts1, ts0] --> [X1(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['4'])).
fof('11', plain, [ts1, ts0] --> [ladr3], inference(res, [status(thm), 0, 0], ['18', '19'])).
fof('A012', plain, [ts1, ts0] --> [~(p(a)) | ~(p(b))], inference(res, [status(thm), 0, 0], ['11', '7'])).
fof('20', plain, [ts1, ts0] --> [p(a)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(a)]]], ['10'])).
fof('A112', plain, [ts1, ts0] --> [~(p(b))], inference(res, [status(thm), 0, 0], ['20', 'A012'])).
fof('21', plain, [ts1, ts0] --> [p(b)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(b)]]], ['10'])).
fof('12', plain, [ts1, ts0] --> [$false], inference(res, [status(thm), 0, 0], ['21', 'A112'])).


* Parents (précédence)
* Ordre des étapes
* Étapes manquantes ? 




fof(i0, plain, [] --> [(p(V0) & (~p(a) | ~p(b)))], inference(rightForall, [status(thm), 0, $fot(X)], [prenex_step])).
fof(tsStep1, let, (ts3 <=> (~p(a) | ~p(b)))).
fof(tsStep0, let, ! [V0]: (Ts1(V0) <=> (p(V0) & ts3))).
fof(tsStepExpl0, plain, [tsStep0] --> [! [V0_1]: (Ts1(V0_1) <=> (p(V0_1) & ts3))], inference(rightSubstIff, [status(thm), 0, $fof(! [V0_1]: (Ts1(V0_1) <=> (p(V0_1) & ts3))), $fot(Ts1(V0))], [i0])).
fof(tsStepExpl1, plain, [tsStep0,tsStep1] --> [(ts3 <=> (~p(a) | ~p(b)))], inference(rightSubstIff, [status(thm), 0, $fof((ts3 <=> (~p(a) | ~p(b)))), $fot(ts3)], [tsStepExpl0])).