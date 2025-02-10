fof(1, conjecture, [~(p(a)) | ?[V0]: p(V0)] --> []).
fof(2, negated_conjecture, [~(p(V0))] --> [], inference(negated_conjecture, [status(thm)], [1])).
fof(3, negated_conjecture, [p(a)] --> [], inference(negated_conjecture, [status(thm)], [1])).
fof(5, plain, [~(p(a))] --> [], inference(instantiate_l, [status(thm), 0, $fot(V0), $fot(a)], [2])).
fof(4, plain, [$false] --> [], inference(res, [status(thm), 0, 0], [5, 3])).


