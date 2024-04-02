% SZS output start Proof for prop_2.p


fof(c_prop_2_p, conjecture, ~(($true & ((~(a) & a) | (~(b) & b))))).

fof(f8, plain, [~(~(($true & ((~(a) & a) | (~(b) & b))))), ($true & ((~(a) & a) | (~(b) & b))), $true, ((~(a) & a) | (~(b) & b)), (~(a) & a), ~(a), a] --> [], inference(leftHyp, param(6, 5), [])).

fof(f6, plain, [~(~(($true & ((~(a) & a) | (~(b) & b))))), ($true & ((~(a) & a) | (~(b) & b))), $true, ((~(a) & a) | (~(b) & b)), (~(a) & a)] --> [], inference(leftAnd, param(4), [f8])).

fof(f9, plain, [~(~(($true & ((~(a) & a) | (~(b) & b))))), ($true & ((~(a) & a) | (~(b) & b))), $true, ((~(a) & a) | (~(b) & b)), (~(b) & b), ~(b), b] --> [], inference(leftHyp, param(6, 5), [])).

fof(f7, plain, [~(~(($true & ((~(a) & a) | (~(b) & b))))), ($true & ((~(a) & a) | (~(b) & b))), $true, ((~(a) & a) | (~(b) & b)), (~(b) & b)] --> [], inference(leftAnd, param(4), [f9])).

fof(f5, plain, [~(~(($true & ((~(a) & a) | (~(b) & b))))), ($true & ((~(a) & a) | (~(b) & b))), $true, ((~(a) & a) | (~(b) & b))] --> [], inference(leftOr, param(3), [f6, f7])).

fof(f4, plain, [~(~(($true & ((~(a) & a) | (~(b) & b))))), ($true & ((~(a) & a) | (~(b) & b)))] --> [], inference(leftAnd, param(1), [f5])).

fof(f3, plain, [~(~(($true & ((~(a) & a) | (~(b) & b)))))] --> [], inference(leftNotNot, param(0), [f4])).

fof(f2, plain, [~(($true & ((~(a) & a) | (~(b) & b))))] --> [~(($true & ((~(a) & a) | (~(b) & b))))], inference(hyp, param(0, 0), [])).

fof(f1, plain, [] --> [~(($true & ((~(a) & a) | (~(b) & b)))), ~(~(($true & ((~(a) & a) | (~(b) & b)))))], inference(rightNot, param(1), [f2])).

fof(f0, plain, [] --> [~(($true & ((~(a) & a) | (~(b) & b))))], inference(cut, param(1, 0), [f1, f3])).



% SZS output end Proof for prop_2.p