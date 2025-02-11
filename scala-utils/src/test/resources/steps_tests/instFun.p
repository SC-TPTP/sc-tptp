fof(a1, axiom, [q(X)] --> []).
fof(f1, plain, [q(b)] --> [], inference(instFun, [0, 'X', $fof(b), []], [a1])).

fof(a2, axiom, [q(g(X, f(X)))] --> []).
fof(f2, plain, [q(g(f(c), f(f(c))))] --> [], inference(instFun, [0,'A', $fof(f(c)), []], [a2])).

fof(a3, axiom, [q(X), q(g(X, f(X)))] --> [q(g(f(X, Y)))]).
fof(f3, plain, [q(f(b)), q(g(f(b), f(f(b))))] --> [q(g(f(f(b), Y)))], inference(instFun, [0, 'X', $fof(f(b)), []], [a3])).


fof(a4, axiom, [![X] : q(g(X, f(Y)))] --> [q(g(f(X, Y)))]).
fof(f4, plain, [![Z] : q(g(Z, f(f(X))))] --> [q(g(f(X, f(X))))], inference(instFun, [0, 'Y', $fof(f(X)), []], [a4])).

fof(a5, axiom, [![X] : q(g(X, f(Y)))] --> [q(g(f(X, Y)))]).
fof(f5, plain, [![Y] : q(g(Y, f(f(X))))] --> [q(g(f(X, f(X))))], inference(instFun, [0, 'Y', $fof(f(X)), []], [a5])).

fof(a6, axiom, [q(F(c))] --> []).
fof(f6, plain, [q(g(c, c))] --> [], inference(instFun, [0, 'F', $fof(g(X, X)), [X]], [a6])).

fof(a7, axiom, [q(G(X, f(c)))] --> []).
fof(f7, plain, [q(g(F(f(c)), F(F(X))))] --> [], inference(instFun, [0, 'G', $fof(g(F(Y), F(F(X)))), [X, Y]], [a7])).

fof(a8, axiom, [![X] : q(G(X, f(c)))] --> []).
fof(f8, plain, [![X] : q(g(F(f(c)), F(F(X))))] --> [], inference(instFun, [0, 'G', $fof(g(F(Y), F(F(X)))), [X, Y]], [a8])).


fof(a9, axiom, [![X] : q(G(X, f(c)))] --> [?[Y] : q(G(b, f(Y)))]).
fof(f9, plain, [![X] : q(g(F(f(c)), F(F(X))))] --> [?[Y] : q(g(F(f(Y)), F(F(b))))], inference(instFun, [0, 'G', $fof(g(F(Y), F(F(X)))), [X, Y]], [a9])).


