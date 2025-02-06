fof(c, conjecture, 
  ! [X, Y] : (p(X) || (p(f(Y)) && p(c))) => ( p(f(f(a))) &&  p(c) )
).


%----------------- Proof Without Certified Clausification -----------------


  %%-------- Clausification --------

fof(nc1, negated_conjecture, 
   ( p(X) || (p(f(Y)) && p(c)) ) && ( ~p(f(a)) || ~p(c) ),
  inference(negated_conjecture, [], [c])
).

fof(nc2, plain,
  (~q(Y) || p(f(Y))) && (~q(Y) || p(b))   &&   ( p(X) || q(Y) )   &&   ( ~p(f(f(a))) || ~p(c) ),
  inference(tseitin, [], [nc1])
).

  %%-------- Initial Clauses --------

fof(f1, plain,
  ~q(Y) || p(f(Y)),
  inference(conj, [...], [nc2])
).

fof(f2, plain,
  ~q(Y) || p(c),
  inference(conj, [...],  [nc2])
).

fof(f3, plain,
  p(X) || q(Y),
  inference(conj, [...],  [nc2])
).

fof(f4, plain,
  ~p(f(f(a))) || ~p(c),
  inference(conj, [...],  [nc2])
).

  %%-------- Proof --------

fof(f5, plain,
  p(X) || p(f(Y)),
  inference(resolution, [0, 2], [f1, f3])
).

fof(f6, plain,
  p(X) || p(c),
  inference(resolution, [0, 2], [f2, f3])
).

fof(f7, plain,
  p(X) || ~p(c),
  inference(resolution, [0, 2], [f4, f5])
).

fof(f8, plain,
  p(X),
  inference(resolution, [1, 1], [f6, f7])
).

fof(f9, plain,
  ~p(c),
  inference(resolution, [0, 0], [f4, f8])
)

fof(f10, plain,
  $false,
  inference(resolution, [0, 0], [f8, f9])
).



