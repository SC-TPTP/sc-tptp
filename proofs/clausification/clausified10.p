fof('2', plain, [] --> [~(X8(V0, V1)) | X3(V0, V1)], inference(clausify, [status(thm)] , ['0'])).
fof('3', plain, [] --> [X8(V0, V1)], inference(clausify, [status(thm)] , ['0'])).
fof('4', plain, [] --> [~(X8(V0, V1)) | ladr8], inference(clausify, [status(thm)] , ['0'])).
fof('5', plain, [] --> [~(X6(V0)) | p(f(V0))], inference(clausify, [status(thm)] , ['0'])).
fof('7', plain, [] --> [~(X6(V0)) | p(c)], inference(clausify, [status(thm)] , ['0'])).
fof('8', plain, [] --> [~(X3(V0, V1)) | p(V0) | X6(V1)], inference(clausify, [status(thm)] , ['0'])).
fof('25', plain, [] --> [~(X3(V0, V100)) | p(V0) | X6(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V1), $fot(V100)]]], ['8'])).
fof('26', plain, [] --> [~(X6(V100)) | p(f(V100))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['5'])).
fof('27', plain, [] --> [~(X3(V0, V100)) | p(V0) | p(f(V100))], inference(res, [status(thm), 2, 0], ['25', '26'])).
fof('10', plain, [] --> [~(X3(V0, V1)) | p(V0) | p(f(V1))], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V1)]]], ['27'])).
fof('28', plain, [] --> [~(X8(V100, V101)) | X3(V100, V101)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['2'])).
fof('29', plain, [] --> [X8(V100, V101)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['3'])).
fof('30', plain, [] --> [X3(V100, V101)], inference(res, [status(thm), 0, 0], ['28', '29'])).
fof('12', plain, [] --> [X3(V0, V1)], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)], [$fot(V101), $fot(V1)]]], ['30'])).
fof('31', plain, [] --> [~(X3(V0, V100)) | p(V0) | X6(V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V1), $fot(V100)]]], ['8'])).
fof('32', plain, [] --> [~(X6(V100)) | p(c)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['7'])).
fof('33', plain, [] --> [~(X3(V0, V100)) | p(V0) | p(c)], inference(res, [status(thm), 2, 0], ['31', '32'])).
fof('13', plain, [] --> [~(X3(V0, V1)) | p(V0) | p(c)], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V1)]]], ['33'])).
fof('14', plain, [] --> [~(ladr8) | ~(p(f(f(a)))) | ~(p(c))], inference(clausify, [status(thm)] , ['0'])).
fof('34', plain, [] --> [~(X8(V100, V101)) | ladr8], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['4'])).
fof('35', plain, [] --> [X8(V100, V101)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['3'])).
fof('17', plain, [] --> [ladr8], inference(res, [status(thm), 0, 0], ['34', '35'])).
fof('36', plain, [] --> [~(X3(V100, V101)) | p(V100) | p(f(V101))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['10'])).
fof('37', plain, [] --> [X3(V100, V101)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['12'])).
fof('38', plain, [] --> [p(V100) | p(f(V101))], inference(res, [status(thm), 0, 0], ['36', '37'])).
fof('18', plain, [] --> [p(V0) | p(f(V1))], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)], [$fot(V101), $fot(V1)]]], ['38'])).
fof('39', plain, [] --> [~(X3(V100, V101)) | p(V100) | p(c)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['13'])).
fof('40', plain, [] --> [X3(V100, V101)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['12'])).
fof('41', plain, [] --> [p(V100) | p(c)], inference(res, [status(thm), 0, 0], ['39', '40'])).
fof('19', plain, [] --> [p(V0) | p(c)], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)]]], ['41'])).
fof('20', plain, [] --> [~(p(f(f(a)))) | ~(p(c))], inference(res, [status(thm), 0, 0], ['17', '14'])).
fof('42', plain, [] --> [p(f(V1)) | p(f(V1))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(f(V1))]]], ['18'])).
fof('43', plain, [] --> [p(f(V1))], inference(leftWeakenRes, [status(thm), 0] , ['42'])).
fof('21', plain, [] --> [p(f(V0))], inference(instantiateMult, [status(thm), 0, [[$fot(V1), $fot(V0)]]], ['43'])).
fof('44', plain, [] --> [p(c) | p(c)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(c)]]], ['19'])).
fof('22', plain, [] --> [p(c)], inference(leftWeakenRes, [status(thm), 0] , ['44'])).
fof('45', plain, [] --> [p(f(f(a)))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(f(a))]]], ['21'])).
fof('A023', plain, [] --> [~(p(c))], inference(res, [status(thm), 0, 0], ['45', '20'])).
fof('23', plain, [] --> [$false], inference(res, [status(thm), 0, 0], ['22', 'A023'])).