%fof(a1, axiom, [] --> [~? [X]: ! [Y]: (p(Y) | ~p(X))]).

%fof(a1, axiom, [] --> [! [X] : ? [Y] : ((f(X, Y) & b)) ]).
%fof(a1, axiom, [] --> [? [X] : ((f(X) & b)) | (b & c) ]).
%fof(a2, axiom, ? [X] : ((g(X) & ~b)) ).
%fof(a2, axiom, ~c ).

fof(conj, conjecture, [] --> [? [X]: ! [Y]: (p(Y) | ~p(X))]).