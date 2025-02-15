
fof(a1, axiom, [] --> [p]).
fof(f1, plain, [q] --> [p], inference(leftWeaken, [0], [a1])).
fof(f2, plain, [q, P(X)] --> [p], inference(leftWeaken, [1], [f1])).
fof(f3, plain, [q, P(X), Q(X, c)] --> [p], inference(leftWeaken, [2], [f2])).
fof(f4, plain, [q, ![X] : (P(X) | Q(a, X)), P(X), Q(X, c)] --> [p], inference(leftWeaken, [1], [f3])).
fof(f5, plain, [q, ![X] : (P(X) | Q(a, X)), P(X), Q(X, c)] --> [p], inference(leftWeaken, [2], [f4])).
fof(f6, plain, [q, ![Y] : (P(Y) | Q(a, Y)), P(X), Q(X, c), p1 => p2] --> [p], inference(leftWeaken, [4], [f5])).




