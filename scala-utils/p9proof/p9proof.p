fof('2', axiom, [] --> [pp(V0) | ~(pTs1(V0))]).
fof('3', axiom, [] --> [~(pp(Sk5(V0))) | ~(pTs1(V0))]).
fof('4', axiom, [] --> [pTs1(V0)]).
fof('8', plain, [] --> [pp(Sk5(V100)) | ~(pTs1(Sk5(V100)))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(Sk5(V100))]]], ['2'])).
fof('9', plain, [] --> [~(pp(Sk5(V100))) | ~(pTs1(V100))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['3'])).
fof('10', plain, [] --> [~(pTs1(Sk5(V100))) | ~(pTs1(V100))], inference(res, [status(thm), 0, 0], ['8', '9'])).
fof('5', plain, [] --> [~(pTs1(Sk5(V0))) | ~(pTs1(V0))], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)]]], ['10'])).
fof('11', plain, [] --> [pTs1(Sk5(V100))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(Sk5(V100))]]], ['4'])).
fof('12', plain, [] --> [~(pTs1(Sk5(V100))) | ~(pTs1(V100))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['5'])).
fof('13', plain, [] --> [~(pTs1(V100))], inference(res, [status(thm), 0, 0], ['11', '12'])).
fof('a06', plain, [] --> [~(pTs1(V0))], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)]]], ['13'])).
fof('14', plain, [] --> [pTs1(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['4'])).
fof('15', plain, [] --> [~(pTs1(V100))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['a06'])).
fof('6', plain, [] --> [$false], inference(res, [status(thm), 0, 0], ['14', '15'])).