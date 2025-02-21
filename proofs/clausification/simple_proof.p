fof('2', plain, [] --> [~(X1(V0)) | p(V0)], inference(clausify, [status(thm)] , ['0'])).
fof('3', plain, [] --> [X1(V0)], inference(clausify, [status(thm)] , ['0'])).
fof('4', plain, [] --> [~(X1(V0)) | ladr3], inference(clausify, [status(thm)] , ['0'])).
fof('5', plain, [] --> [~(ladr3) | ~(p(a)) | ~(p(b))], inference(clausify, [status(thm)] , ['0'])).
fof('13', plain, [] --> [~(X1(V100)) | p(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['2'])).
fof('14', plain, [] --> [X1(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['3'])).
fof('15', plain, [] --> [p(V100)], inference(res, [status(thm), 0, 0], ['13', '14'])).
fof('8', plain, [] --> [p(V0)], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)]]], ['15'])).
fof('16', plain, [] --> [~(X1(V100)) | ladr3], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['4'])).
fof('17', plain, [] --> [X1(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['3'])).
fof('9', plain, [] --> [ladr3], inference(res, [status(thm), 0, 0], ['16', '17'])).
fof('A010', plain, [] --> [~(p(a)) | ~(p(b))], inference(res, [status(thm), 0, 0], ['9', '5'])).
fof('18', plain, [] --> [p(a)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(a)]]], ['8'])).
fof('A110', plain, [] --> [~(p(b))], inference(res, [status(thm), 0, 0], ['18', 'A010'])).
fof('19', plain, [] --> [p(b)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(b)]]], ['8'])).
fof('10', plain, [] --> [$false], inference(res, [status(thm), 0, 0], ['19', 'A110'])).