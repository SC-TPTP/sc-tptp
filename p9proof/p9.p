fof(0, axiom, Ts3(V0,V1)).

fof(1, axiom, (~Ts2(V0,V1) | sP(V0) | sQ(V1))).

fof(2, axiom, (Ts2(V0,V1) | ~sP(V0))).

fof(3, axiom, (Ts2(V0,V1) | ~sQ(V1))).

fof(4, axiom, (ts5 | ~~sP(cemptySet) | ~~sQ(cemptySet))).

fof(5, axiom, (~ts5 | ~sP(cemptySet))).

fof(6, axiom, (~ts5 | ~sQ(cemptySet))).

fof(7, axiom, (Ts3(V0,V1) | ~Ts2(V0,V1) | ~ts5)).

fof(8, axiom, (~Ts3(V0,V1) | Ts2(V0,V1))).

fof(9, axiom, (~Ts3(V0,V1) | ts5)).

