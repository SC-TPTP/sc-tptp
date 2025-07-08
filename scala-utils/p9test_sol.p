% SZS status Theorem for SYN056+1.p
% SZS output start Proof for SYN056+1.p
fof(ax5, axiom, 
    ((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9))))).

fof(ax13, axiom, 
    (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13)))))).

fof(c_SYN056_1_p, conjecture, 
    ((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))).

fof(f11, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9)))), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_0)
     => big_r(Sko_0))), big_p(Sko_0), ~(big_r(Sko_0)), (! [Y13] :
    (((big_p(X11_36)
    & big_q(Y13))
     => (big_r(X11_36)  <=> big_s(Y13))))), ~(big_p(Sko_0))]-->[], inference(leftHyp, [status(thm), 8], [])).

fof(f10, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9)))), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_0)
     => big_r(Sko_0))), big_p(Sko_0), ~(big_r(Sko_0)), (! [Y13] :
    (((big_p(X11_36)
    & big_q(Y13))
     => (big_r(X11_36)  <=> big_s(Y13)))))]-->[], inference(leftNotEx, [status(thm), 3, $fot(Sko_0)], [f11])).

fof(f9, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9)))), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_0)
     => big_r(Sko_0))), big_p(Sko_0), ~(big_r(Sko_0))]-->[], inference(leftForall, [status(thm), 1, $fot(X11_36)], [f10])).

fof(f8, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9)))), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_0)
     => big_r(Sko_0)))]-->[], inference(leftNotImplies, [status(thm), 7], [f9])).

fof(f6, plain, 
    [((? [X7] : (big_p(X7)))  <=>(? [Y9] : (big_q(Y9)))), 
    (! [X11, Y13] : (((big_p(X11) & big_q(Y13)) => (big_r(X11)  <=> big_s(Y13))))), 
    ~(((! [X17] : ((big_p(X17) => big_r(X17))))  <=>(! [Y19] : ((big_q(Y19) => big_s(Y19)))))), 
    ~((? [X7] :(big_p(X7)))), 
    ~((? [Y9] : (big_q(Y9)))), 
    ~((! [X17] : ((big_p(X17) => big_r(X17))))), 
    (! [Y19] : ((big_q(Y19) => big_s(Y19))))]-->[], inference(leftNotAll, [status(thm), 5, 'Sko_0'], [f8])).

fof(f16, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9)))), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_1)
     => big_s(Sko_1))), big_q(Sko_1), ~(big_s(Sko_1)), (! [Y13] :
    (((big_p(X11_34)
    & big_q(Y13))
     => (big_r(X11_34)  <=> big_s(Y13))))), ~(big_p(X7_39)), ~(big_q(Sko_1))]-->[], inference(leftHyp, [status(thm), 8], [])).

fof(f15, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9)))), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_1)
     => big_s(Sko_1))), big_q(Sko_1), ~(big_s(Sko_1)), (! [Y13] :
    (((big_p(X11_34)
    & big_q(Y13))
     => (big_r(X11_34)  <=> big_s(Y13))))), ~(big_p(X7_39))]-->[], inference(leftNotEx, [status(thm), 4, $fot(Sko_1)], [f16])).

fof(f14, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9)))), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_1)
     => big_s(Sko_1))), big_q(Sko_1), ~(big_s(Sko_1)), (! [Y13] :
    (((big_p(X11_34)
    & big_q(Y13))
     => (big_r(X11_34)  <=> big_s(Y13)))))]-->[], inference(leftNotEx, [status(thm), 3, $fot(X7_39)], [f15])).

fof(f13, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9)))), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_1)
     => big_s(Sko_1))), big_q(Sko_1), ~(big_s(Sko_1))]-->[], inference(leftForall, [status(thm), 1, $fot(X11_34)], [f14])).

fof(f12, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9)))), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_1)
     => big_s(Sko_1)))]-->[], inference(leftNotImplies, [status(thm), 7], [f13])).

fof(f7, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9)))), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))]-->[], inference(leftNotAll, [status(thm), 6, 'Sko_1'], [f12])).

fof(f4, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), ~((? [X7] :
    (big_p(X7)))), ~((? [Y9] :
    (big_q(Y9))))]-->[], inference(leftNotIff, [status(thm), 2], [f6, f7])).

fof(f25, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), ~(big_q(Sko_3))]-->[], inference(leftHyp, [status(thm), 6], [])).

fof(f30, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), ~((big_p(Sko_2)
    & big_q(Sko_3))), ~(big_p(Sko_2))]-->[], inference(leftHyp, [status(thm), 5], [])).

fof(f31, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), ~((big_p(Sko_2)
    & big_q(Sko_3))), ~(big_q(Sko_3))]-->[], inference(leftHyp, [status(thm), 6], [])).

fof(f28, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), ~((big_p(Sko_2)
    & big_q(Sko_3)))]-->[], inference(leftNotAnd, [status(thm), 16], [f30, f31])).

fof(f32, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), ~(big_r(Sko_2)), ~(big_s(Sko_3))]-->[], inference(leftHyp, [status(thm), 14], [])).

fof(f38, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_r(Sko_2), (! [Y13] :
    (((big_p(Sko_4)
    & big_q(Y13))
     => (big_r(Sko_4)  <=> big_s(Y13))))), ((big_p(Sko_4)
    & big_q(Sko_3))
     => (big_r(Sko_4)  <=> big_s(Sko_3))), ~((big_p(Sko_4)
    & big_q(Sko_3))), ~(big_p(Sko_4))]-->[], inference(leftHyp, [status(thm), 10], [])).

fof(f39, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_r(Sko_2), (! [Y13] :
    (((big_p(Sko_4)
    & big_q(Y13))
     => (big_r(Sko_4)  <=> big_s(Y13))))), ((big_p(Sko_4)
    & big_q(Sko_3))
     => (big_r(Sko_4)  <=> big_s(Sko_3))), ~((big_p(Sko_4)
    & big_q(Sko_3))), ~(big_q(Sko_3))]-->[], inference(leftHyp, [status(thm), 6], [])).

fof(f36, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_r(Sko_2), (! [Y13] :
    (((big_p(Sko_4)
    & big_q(Y13))
     => (big_r(Sko_4)  <=> big_s(Y13))))), ((big_p(Sko_4)
    & big_q(Sko_3))
     => (big_r(Sko_4)  <=> big_s(Sko_3))), ~((big_p(Sko_4)
    & big_q(Sko_3)))]-->[], inference(leftNotAnd, [status(thm), 20], [f38, f39])).

fof(f40, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_r(Sko_2), (! [Y13] :
    (((big_p(Sko_4)
    & big_q(Y13))
     => (big_r(Sko_4)  <=> big_s(Y13))))), ((big_p(Sko_4)
    & big_q(Sko_3))
     => (big_r(Sko_4)  <=> big_s(Sko_3))), (big_r(Sko_4)  <=> big_s(Sko_3)), ~(big_s(Sko_3))]-->[], inference(leftHyp, [status(thm), 14], [])).

fof(f41, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_r(Sko_2), (! [Y13] :
    (((big_p(Sko_4)
    & big_q(Y13))
     => (big_r(Sko_4)  <=> big_s(Y13))))), ((big_p(Sko_4)
    & big_q(Sko_3))
     => (big_r(Sko_4)  <=> big_s(Sko_3))), (big_r(Sko_4)  <=> big_s(Sko_3)), big_r(Sko_4)]-->[], inference(leftHyp, [status(thm), 21], [])).

fof(f37, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_r(Sko_2), (! [Y13] :
    (((big_p(Sko_4)
    & big_q(Y13))
     => (big_r(Sko_4)  <=> big_s(Y13))))), ((big_p(Sko_4)
    & big_q(Sko_3))
     => (big_r(Sko_4)  <=> big_s(Sko_3))), (big_r(Sko_4)  <=> big_s(Sko_3))]-->[], inference(leftIff, [status(thm), 20], [f40, f41])).

fof(f35, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_r(Sko_2), (! [Y13] :
    (((big_p(Sko_4)
    & big_q(Y13))
     => (big_r(Sko_4)  <=> big_s(Y13))))), ((big_p(Sko_4)
    & big_q(Sko_3))
     => (big_r(Sko_4)  <=> big_s(Sko_3)))]-->[], inference(leftImplies, [status(thm), 19], [f36, f37])).

fof(f34, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_r(Sko_2), (! [Y13] :
    (((big_p(Sko_4)
    & big_q(Y13))
     => (big_r(Sko_4)  <=> big_s(Y13)))))]-->[], inference(leftForall, [status(thm), 18, $fot(Sko_3)], [f35])).

fof(f33, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_r(Sko_2)]-->[], inference(leftForall, [status(thm), 1, $fot(Sko_4)], [f34])).

fof(f29, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3))]-->[], inference(leftIff, [status(thm), 16], [f32, f33])).

fof(f27, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3)))]-->[], inference(leftImplies, [status(thm), 15], [f28, f29])).

fof(f26, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3)), big_s(Sko_3)]-->[], inference(leftForall, [status(thm), 12, $fot(Sko_3)], [f27])).

fof(f24, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_q(Sko_3)
     => big_s(Sko_3))]-->[], inference(leftImplies, [status(thm), 13], [f25, f26])).

fof(f23, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13)))))]-->[], inference(leftForall, [status(thm), 8, $fot(Sko_3)], [f24])).

fof(f22, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4))), big_p(Sko_4), ~(big_r(Sko_4))]-->[], inference(leftForall, [status(thm), 1, $fot(Sko_2)], [f23])).

fof(f21, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))), ~((big_p(Sko_4)
     => big_r(Sko_4)))]-->[], inference(leftNotImplies, [status(thm), 9], [f22])).

fof(f19, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), ~((! [X17] :
    ((big_p(X17)
     => big_r(X17))))), (! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))]-->[], inference(leftNotAll, [status(thm), 7, 'Sko_4'], [f21])).

fof(f46, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), ~(big_p(Sko_2))]-->[], inference(leftHyp, [status(thm), 5], [])).

fof(f51, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), ~((big_p(Sko_2)
    & big_q(Sko_3))), ~(big_p(Sko_2))]-->[], inference(leftHyp, [status(thm), 5], [])).

fof(f52, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), ~((big_p(Sko_2)
    & big_q(Sko_3))), ~(big_q(Sko_3))]-->[], inference(leftHyp, [status(thm), 6], [])).

fof(f49, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), ~((big_p(Sko_2)
    & big_q(Sko_3)))]-->[], inference(leftNotAnd, [status(thm), 16], [f51, f52])).

fof(f53, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), ~(big_r(Sko_2)), ~(big_s(Sko_3))]-->[], inference(leftHyp, [status(thm), 14], [])).

fof(f59, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_5))
     => (big_r(Sko_2)  <=> big_s(Sko_5))), ~((big_p(Sko_2)
    & big_q(Sko_5))), ~(big_p(Sko_2))]-->[], inference(leftHyp, [status(thm), 5], [])).

fof(f60, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_5))
     => (big_r(Sko_2)  <=> big_s(Sko_5))), ~((big_p(Sko_2)
    & big_q(Sko_5))), ~(big_q(Sko_5))]-->[], inference(leftHyp, [status(thm), 10], [])).

fof(f57, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_5))
     => (big_r(Sko_2)  <=> big_s(Sko_5))), ~((big_p(Sko_2)
    & big_q(Sko_5)))]-->[], inference(leftNotAnd, [status(thm), 19], [f59, f60])).

fof(f61, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_5))
     => (big_r(Sko_2)  <=> big_s(Sko_5))), (big_r(Sko_2)  <=> big_s(Sko_5)), ~(big_r(Sko_2))]-->[], inference(leftHyp, [status(thm), 14], [])).

fof(f62, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_5))
     => (big_r(Sko_2)  <=> big_s(Sko_5))), (big_r(Sko_2)  <=> big_s(Sko_5)), big_s(Sko_5)]-->[], inference(leftHyp, [status(thm), 20], [])).

fof(f58, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_5))
     => (big_r(Sko_2)  <=> big_s(Sko_5))), (big_r(Sko_2)  <=> big_s(Sko_5))]-->[], inference(leftIff, [status(thm), 19], [f61, f62])).

fof(f56, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_s(Sko_3), ((big_p(Sko_2)
    & big_q(Sko_5))
     => (big_r(Sko_2)  <=> big_s(Sko_5)))]-->[], inference(leftImplies, [status(thm), 18], [f57, f58])).

fof(f55, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_s(Sko_3)]-->[], inference(leftForall, [status(thm), 12, $fot(Sko_5)], [f56])).

fof(f54, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3)), big_s(Sko_3)]-->[], inference(leftForall, [status(thm), 1, $fot(Sko_2)], [f55])).

fof(f50, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3))), (big_r(Sko_2)  <=> big_s(Sko_3))]-->[], inference(leftIff, [status(thm), 16], [f53, f54])).

fof(f48, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2), ((big_p(Sko_2)
    & big_q(Sko_3))
     => (big_r(Sko_2)  <=> big_s(Sko_3)))]-->[], inference(leftImplies, [status(thm), 15], [f49, f50])).

fof(f47, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2)), big_r(Sko_2)]-->[], inference(leftForall, [status(thm), 12, $fot(Sko_3)], [f48])).

fof(f45, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13))))), (big_p(Sko_2)
     => big_r(Sko_2))]-->[], inference(leftImplies, [status(thm), 13], [f46, f47])).

fof(f44, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5)), (! [Y13] :
    (((big_p(Sko_2)
    & big_q(Y13))
     => (big_r(Sko_2)  <=> big_s(Y13)))))]-->[], inference(leftForall, [status(thm), 7, $fot(Sko_2)], [f45])).

fof(f43, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5))), big_q(Sko_5), ~(big_s(Sko_5))]-->[], inference(leftForall, [status(thm), 1, $fot(Sko_2)], [f44])).

fof(f42, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~((big_q(Sko_5)
     => big_s(Sko_5)))]-->[], inference(leftNotImplies, [status(thm), 9], [f43])).

fof(f20, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3), (! [X17] :
    ((big_p(X17)
     => big_r(X17)))), ~((! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))]-->[], inference(leftNotAll, [status(thm), 8, 'Sko_5'], [f42])).

fof(f18, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2), big_q(Sko_3)]-->[], inference(leftNotIff, [status(thm), 2], [f19, f20])).

fof(f17, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9))), big_p(Sko_2)]-->[], inference(leftExists, [status(thm), 4, 'Sko_3'], [f18])).

fof(f5, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))), (? [X7] :
    (big_p(X7))), (? [Y9] :
    (big_q(Y9)))]-->[], inference(leftExists, [status(thm), 3, 'Sko_2'], [f17])).

fof(f3, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))))]-->[], inference(leftIff, [status(thm), 0], [f4, f5])).

fof(f2, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13))))), ((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))]-->[((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))], inference(hyp, [status(thm), 2], [])).

fof(f1, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13)))))]-->[((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))), ~(((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9)))), (! [X11, Y13] :
    (((big_p(X11)
    & big_q(Y13))
     => (big_r(X11)  <=> big_s(Y13)))))]-->[((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))], inference(cut, [status(thm), 1], [f1, f3])).

fof(ac1, plain, 
    [((? [X7] :
    (big_p(X7)))  <=>(? [Y9] :
    (big_q(Y9))))]-->[((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))], inference(cut, [status(thm), 0], [ax13, f0])).

fof(ac0, plain, 
    []-->[((! [X17] :
    ((big_p(X17)
     => big_r(X17))))  <=>(! [Y19] :
    ((big_q(Y19)
     => big_s(Y19)))))], inference(cut, [status(thm), 0], [ax5, ac1])).


% SZS output end Proof for SYN056+1.p