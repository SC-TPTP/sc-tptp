fof('2', axiom, [] --> [a]).
fof('3', axiom, [] --> [~(a)]).
fof('4', plain, [] --> [$false], inference(res, [status(thm), 0, 0], ['3', '2'])).