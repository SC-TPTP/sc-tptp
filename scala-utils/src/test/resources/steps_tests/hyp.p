fof(f1, plain, [p] --> [p], inference(hyp, [0, 0], [])).
fof(f3, plain, [p, q] --> [p], inference(hyp, [0, 0], [])).
fof(f4, plain, [q] --> [p, q], inference(hyp, [0, 1], [])).
fof(f5, plain, [p, q] --> [r, q], inference(hyp, [1, 1], [])).
fof(f1, plain, [p & q] --> [p & q], inference(hyp, [0, 0], [])).
fof(f1, plain, [p | q] --> [p | q], inference(hyp, [0, 0], [])).
fof(f1, plain, [p => q] --> [p => q], inference(hyp, [0, 0], [])).
fof(f1, plain, [p(X, c) => q(X)] --> [p(X, c) => q(X)], inference(hyp, [0, 0], [])).
fof(f1, plain, [![X] :( p(X, c) => q(X))] --> [![X] : (p(X, c) => q(X))], inference(hyp, [0, 0], [])).
fof(f1, plain, [![X] : (p(X, c) => q(X))] --> [![Y] : (p(Y, c) => q(Y))], inference(hyp, [0, 0], [])).
fof(f1, plain, [?[Y] : ![X] : (p(X, c) => q(X))] --> [?[X] : ![Y] : (p(Y, c) => q(Y))], inference(hyp, [0, 0], [])).
fof(f1, plain, [?[Y] : ![X] : (p(X, c) => q(Y))] --> [?[X] : ![Y] : (p(Y, c) => q(X))], inference(hyp, [0, 0], [])).

