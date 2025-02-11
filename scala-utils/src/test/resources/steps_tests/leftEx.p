fof(a1, axiom, [p, q] --> [r]).
fof(f1, plain, [?[X] : p, q] --> [r], inference(leftEx, [0, 'X'], [a1])).

fof(a2, axiom, [a, p(X)] --> [r]).
fof(f2, plain, [a, ?[X] : p(X)] --> [r], inference(leftEx, [1, 'X'], [a2])).

fof(a3, axiom, [P(X), Q(Y)] --> [R]).
fof(f3, plain, [P(X), ?[X] : Q(X)] --> [R], inference(leftEx, [1, 'Y'], [a3])).


fof(a4, axiom, [p(X), q(Z) | q(Y)] --> [r(X, Y)]).
fof(f4, plain, [p(X), ?[X] : (q(X) | q(Y))] --> [r(X, Y)], inference(leftEx, [1, 'Z'], [a4])).


fof(a6, axiom, [?[X]: (p(X) & q(Z)), (r(X) | s(Y))] --> [(t(X) & u(X)), v(X)]).
fof(f6, plain, [?[Y, X]: (p(X) & q(Y)), (r(X) | s(Y))] --> [(t(X) & u(X)), v(X)], inference(leftEx, [0, 'Z'], [a6])).

fof(a7, axiom, [q, ?[X]: (p(X) & q(f(c))), s(Y)] --> []).
fof(f7, plain, [q, ?[X, Y]: (p(Y) & q(X)), s(Y)] --> [], inference(leftEx, [1, 'Y'], [a7])).

fof(a7, axiom, [q, ?[X]: (p(X) & r(f(c), f(c))), s(Y)] --> []).
fof(f7, plain, [q, ?[X, Y]: (p(Y) & r(X, X)), s(Y)] --> [], inference(leftEx, [1, 'Y'], [a7])).
