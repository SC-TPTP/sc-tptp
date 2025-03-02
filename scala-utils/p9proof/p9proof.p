fof('2', axiom, [] --> [pTs2(V0)]).
fof('3', axiom, [] --> [pTs1 | ~(pTs2(V0))]).
fof('4', axiom, [] --> [pb | ~(pTs1)]).
fof('6', axiom, [] --> [~(pb)]).
fof('10', plain, [] --> [pTs2(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['2'])).
fof('11', plain, [] --> [pTs1 | ~(pTs2(V100))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['3'])).
fof('7', plain, [] --> [pTs1], inference(res, [status(thm), 0, 1], ['10', '11'])).
fof('8', plain, [] --> [~(pTs1)], inference(res, [status(thm), 0, 0], ['6', '4'])).
fof('9', plain, [] --> [$false], inference(res, [status(thm), 0, 0], ['8', '7'])).