fof('2', axiom, [] --> [~(Ts3(V0, V1)) | Ts2(V0, V1)]).
fof('3', axiom, [] --> [Ts3(V0, V1)]).
fof('5', axiom, [] --> [~(Ts3(V0, V1)) | ts5]).
fof('7', axiom, [] --> [~(Ts2(V0, V1)) | sP(V0) | sQ(V1)]).
fof('19', plain, [] --> [~(Ts3(V100, V101)) | Ts2(V100, V101)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['2'])).
fof('20', plain, [] --> [Ts3(V100, V101)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['3'])).
fof('21', plain, [] --> [Ts2(V100, V101)], inference(res, [status(thm), 0, 0], ['19', '20'])).
fof('9', plain, [] --> [Ts2(V0, V1)], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)], [$fot(V101), $fot(V1)]]], ['21'])).
fof('10', axiom, [] --> [~(ts5) | ~(sP(cemptySet))]).
fof('22', plain, [] --> [Ts2(V100, V101)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['9'])).
fof('23', plain, [] --> [~(Ts2(V100, V101)) | sP(V100) | sQ(V101)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['7'])).
fof('24', plain, [] --> [sP(V100) | sQ(V101)], inference(res, [status(thm), 0, 0], ['22', '23'])).
fof('12', plain, [] --> [sP(V0) | sQ(V1)], inference(instantiateMult, [status(thm), 0, [[$fot(V100), $fot(V0)], [$fot(V101), $fot(V1)]]], ['24'])).
fof('25', plain, [] --> [sP(cemptySet) | sQ(V1)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(cemptySet)]]], ['12'])).
fof('26', plain, [] --> [sQ(V1) | ~(ts5)], inference(res, [status(thm), 0, 1], ['25', '10'])).
fof('13', plain, [] --> [sQ(V0) | ~(ts5)], inference(instantiateMult, [status(thm), 0, [[$fot(V1), $fot(V0)]]], ['26'])).
fof('14', axiom, [] --> [~(ts5) | ~(sQ(cemptySet))]).
fof('27', plain, [] --> [~(Ts3(V100, V101)) | ts5], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['5'])).
fof('28', plain, [] --> [Ts3(V100, V101)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(V100)], [$fot(V1), $fot(V101)]]], ['3'])).
fof('15', plain, [] --> [ts5], inference(res, [status(thm), 0, 0], ['27', '28'])).
fof('29', plain, [] --> [sQ(cemptySet) | ~(ts5)], inference(instantiateMult, [status(thm), 0, [[$fot(V0), $fot(cemptySet)]]], ['13'])).
fof('16', plain, [] --> [~(ts5) | ~(ts5)], inference(res, [status(thm), 0, 1], ['29', '14'])).
fof('a017', plain, [] --> [~(ts5)], inference(leftWeakenRes, [status(thm), 0] , ['16'])).
fof('17', plain, [] --> [$false], inference(res, [status(thm), 0, 0], ['15', 'a017'])).