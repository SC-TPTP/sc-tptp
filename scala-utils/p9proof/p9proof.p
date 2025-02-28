fof('3', axiom, [] --> [~(Ts1(V0)) | p(V0)]).
fof('4', axiom, [] --> [Ts1(V0)]).
fof('6', axiom, [] --> [~(Ts1(V0)) | ts3]).
fof('7', axiom, [] --> [~(ts3) | ~(p(a)) | ~(p(b))]).
fof('15', plain, [] --> [~(Ts1(V100)) | p(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['3'])).
fof('16', plain, [] --> [Ts1(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['4'])).
fof('17', plain, [] --> [p(V100)], inference(res, [status(thm), 0, 0], ['15', '16'])).
fof('10', plain, [] --> [p(V0)], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)]]], ['17'])).
fof('18', plain, [] --> [~(Ts1(V100)) | ts3], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['6'])).
fof('19', plain, [] --> [Ts1(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['4'])).
fof('11', plain, [] --> [ts3], inference(res, [status(thm), 0, 0], ['18', '19'])).
fof('a012', plain, [] --> [~(p(a)) | ~(p(b))], inference(res, [status(thm), 0, 0], ['11', '7'])).
fof('20', plain, [] --> [p(a)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(a)]]], ['10'])).
fof('a112', plain, [] --> [~(p(b))], inference(res, [status(thm), 0, 0], ['20', 'a012'])).
fof('21', plain, [] --> [p(b)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(b)]]], ['10'])).
fof('12', plain, [] --> [$false], inference(res, [status(thm), 0, 0], ['21', 'a112'])).