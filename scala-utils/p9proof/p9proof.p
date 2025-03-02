fof('2', axiom, [] --> [pb | ~(pTs1)]).
fof('3', axiom, [] --> [pTs1]).
fof('4', axiom, [] --> [~(pb)]).
fof('5', plain, [] --> [pb], inference(res, [status(thm), 1, 0], ['2', '3'])).
fof('6', plain, [] --> [$false], inference(res, [status(thm), 0, 0], ['5', '4'])).