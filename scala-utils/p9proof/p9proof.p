fof('2', axiom, [] --> [pTs2(V0) | pTs1(V0) | ~(pTs3(V0, V0))]).
fof('3', axiom, [] --> [pb | ~(pTs1(V0))]).
fof('13', plain, [] --> [pTs2(V100) | pTs1(V100) | ~(pTs3(V100, V100))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['2'])).
fof('14', plain, [] --> [pb | ~(pTs1(V100))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['3'])).
fof('15', plain, [] --> [pTs2(V100) | ~(pTs3(V100, V100)) | pb], inference(res, [status(thm), 1, 1], ['13', '14'])).
fof('4', plain, [] --> [pTs2(V0) | ~(pTs3(V0, V0)) | pb], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)]]], ['15'])).
fof('5', axiom, [] --> [pb | ~(pTs2(V0))]).
fof('16', plain, [] --> [pTs2(V100) | ~(pTs3(V100, V100)) | pb], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['4'])).
fof('17', plain, [] --> [pb | ~(pTs2(V100))], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['5'])).
fof('18', plain, [] --> [~(pTs3(V100, V100)) | pb | pb], inference(res, [status(thm), 0, 1], ['16', '17'])).
fof('7', plain, [] --> [~(pTs3(V0, V0)) | pb | pb], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)]]], ['18'])).
fof('8', axiom, [] --> [pTs3(V0, V0)]).
fof('9', axiom, [] --> [~(pb)]).
fof('19', plain, [] --> [~(pTs3(V100, V100)) | pb | pb], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['7'])).
fof('20', plain, [] --> [pTs3(V100, V100)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)]]], ['8'])).
fof('10', plain, [] --> [pb | pb], inference(res, [status(thm), 0, 0], ['19', '20'])).
fof('a011', plain, [] --> [pb], inference(leftWeakenRes, [status(thm), 0] , ['10'])).
fof('11', plain, [] --> [$false], inference(res, [status(thm), 0, 0], ['9', 'a011'])).