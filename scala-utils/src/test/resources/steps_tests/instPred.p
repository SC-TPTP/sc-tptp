fof(a1, axiom, [A] --> []).
fof(f1, plain, [p(b, c) ] --> [], inference(instPred, [0, 'A', $fof(p(b, c)), []], [a1])).

fof(a2, axiom, [~(A | ~A)] --> []).
fof(f2, plain, [~(~p(b) | ~(~p(b)) )] --> [], inference(instPred, [0, 'A', $fof(~p(b)), []], [a2])).

fof(a3, axiom, [A, (A & ~p(b))] --> [(~A) => B]).
fof(f3, plain, [~p(B), (~p(B) & ~p(B))] --> [(~~p(B)) => B], inference(instPred, [0, 'A', $fof(~p(B)), []], [a3])).

fof(a4, axiom, [![X] : P(b, X)] --> []).
fof(f4, plain, [![Z] : p(X, Z)] --> [], inference(instPred, [0, 'P', $fof(p(Y, Z)), [X, Y]], [a4])).

fof(a5, axiom, [![X] : P(X, f(Y))] --> []).
fof(f5, plain, [![Y] : P(f(Y), f(f(X)))] --> [], inference(instPred, [0, 'P', $fof(P(Y, f(f(X)))), []], [a5])).

fof(a6, axiom, [P(a, b) && ~P(c, d)] --> []).
fof(f6, plain, [P(X, f(a)) && ~P(X, f(c))] --> [], inference(instPred, [0, 'P', $fof(P(X, f(Y))), [Y, Z]], [a6])).

fof(a7, axiom, [![X] : P(X, f(c))] --> [?[Y] : P(b, f(Y))]).
fof(f7, plain, [![Z] : q(Z) | ![X] : ~(q(X, f(f(c))))] --> [?[Z] : q(b) | ![X] : ~(q(X, f(f(Z))))], 
      inference(instPred, [0, 'P', $fof(q(X) | ![X] : ~(q(X, f(Y)))), [X, Y]], [a7])).
