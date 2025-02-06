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







%---------- Deductive Proof With Certified Clausification ----------

let(phi,  (p(X) || (p(f(Y)) && p(c))) && ( ~p(f(a)) || ~p(c)) ).

  %%-------- Clausification --------

fof(nc1, plain, 
  [~phi] --> [~phi], 
inference(Hyp, fof(phi), [])
).

let(ts_q, Q(Y) <=> (p(f(Y)) || p(c)))

fof(nc2, plain, 
  [~phi, ts_q] --> [p(X) || Q(Y) && ~p(f(f(a))) || ~p(c)],
  inference(rightSubstIff, [2, ..., ...], [nc1])
).

  %%-------- Initial Clauses --------

fof(f1, plain
  [~phi, ts_q] --> [~Q(Y), p(f(Y))],
  inference(conj, [...], [nc2])
).

fof(f2, plain
  [~phi, ts_q] --> [~Q(Y), p(c)],
  inference(conj, [...], [nc2])
).

fof(f3, plain
  [~phi, ts_q] --> [p(X), Q(Y)],
  inference(conj, [...], [nc2])
).

fof(f4, plain
  [~phi, ts_q] --> [~p(f(f(a))), ~p(c)],
  inference(conj, [...], [nc2])
).

  %%-------- Proof --------

fof(f5, plain
  [~phi, ts_q] --> [p(X), p(f(Y))],
  inference(resolution, [1, 2], [f1, f3])
).

fof(f6, plain
  [~phi, ts_q] --> [p(X), p(c)],
  inference(resolution, [1, 2], [f2, f3])
).

fof(f7, plain
  [~phi, ts_q] --> [p(X), ~p(c)],
  inference(resolution, [1, 2], [f4, f5])
).

fof(f8, plain
  [~phi, ts_q] --> [p(X)],
  inference(resolution, [2, 2], [f6, f7])
).

fof(f9, plain
  [~phi, ts_q] --> [~p(c)],
  inference(resolution, [1, 1], [f4, f8])
).

fof(f10, plain
  [~phi, ts_q] --> [],
  inference(resolution, [1, 1], [f8, f9])
).

  %%-------- Elimination of Tseitin Assumptions --------
  
fof(lem1, assumption,
  [] --> [A <=> A]
  inference(iff_refl, [], [])
).

fof(s1, plain
  [~phi, (p(f(Y)) || p(c)) <=> (p(f(Y)) || p(c))] --> [],
  inference(inst, [Q(Y), (p(f(Y)) || p(c))], [f10])
).

fof(s2, plain
  [] --> [(p(f(Y)) || p(c)) <=> (p(f(Y)) || p(c))],
  inference(inst, [A, (p(f(Y)) || p(c))], [lem1])
).

fof(s3, plain
  [~phi] --> [],
  inference(cut, [0, 1], [s2, s1])
).

fof(s4, plain
  [] --> [phi],
  inference(rightNeg_inv, [0], [s3])
).

fof(s5, plain
  [] --> [( p(X) || (p(f(Y)) && p(c)) ) => ( p(f(f(a))) &&  p(c) )],
  inference(sameNNF, [], [s4])
).

fof(s6, plain
  [] --> [ ! [Y] : ( p(X) || (p(f(Y)) && p(c)) ) => ( p(f(f(a))) &&  p(c) )],
  inference(leftForall, [0, $fot(Y)], [s4])
).

fof(s7, plain
  [] --> [ ! [X, Y] : ( p(X) || (p(f(Y)) && p(c)) ) => ( p(f(f(a))) &&  p(c) )],
  inference(leftForall, [0, $fot(X)], [s6])
).

% CQFD