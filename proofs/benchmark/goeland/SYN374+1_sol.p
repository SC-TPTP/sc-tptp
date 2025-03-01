
% SZS output start Proof for SYN374+1.p


fof(c_SYN374_1_p, conjecture, ((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))).

fof(f13, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), ~((? [X8] : (big_p(X8)))), ~((! [Y10] : (big_p(Y10)))), ~(big_p(Sko_0)), ~((! [Y6] : ((big_p(Sko_0) <=> big_p(Y6))))), ~((big_p(Sko_0) <=> big_p(Sko_1))), big_p(Sko_1), ~(big_p(Sko_1))] --> [], inference(leftHyp, [status(thm), 9], [])).

fof(f11, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), ~((? [X8] : (big_p(X8)))), ~((! [Y10] : (big_p(Y10)))), ~(big_p(Sko_0)), ~((! [Y6] : ((big_p(Sko_0) <=> big_p(Y6))))), ~((big_p(Sko_0) <=> big_p(Sko_1))), big_p(Sko_1)] --> [], inference(leftNotEx, [status(thm), 3, $fot(Sko_1)], [f13])).

fof(f12, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), ~((? [X8] : (big_p(X8)))), ~((! [Y10] : (big_p(Y10)))), ~(big_p(Sko_0)), ~((! [Y6] : ((big_p(Sko_0) <=> big_p(Y6))))), ~((big_p(Sko_0) <=> big_p(Sko_1))), big_p(Sko_0), ~(big_p(Sko_1))] --> [], inference(leftHyp, [status(thm), 5], [])).

fof(f10, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), ~((? [X8] : (big_p(X8)))), ~((! [Y10] : (big_p(Y10)))), ~(big_p(Sko_0)), ~((! [Y6] : ((big_p(Sko_0) <=> big_p(Y6))))), ~((big_p(Sko_0) <=> big_p(Sko_1)))] --> [], inference(leftNotIff, [status(thm), 7], [f11, f12])).

fof(f9, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), ~((? [X8] : (big_p(X8)))), ~((! [Y10] : (big_p(Y10)))), ~(big_p(Sko_0)), ~((! [Y6] : ((big_p(Sko_0) <=> big_p(Y6)))))] --> [], inference(leftNotAll, [status(thm), 6, 'Sko_1'], [f10])).

fof(f8, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), ~((? [X8] : (big_p(X8)))), ~((! [Y10] : (big_p(Y10)))), ~(big_p(Sko_0))] --> [], inference(leftNotEx, [status(thm), 1, $fot(Sko_0)], [f9])).

fof(f6, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), ~((? [X8] : (big_p(X8)))), ~((! [Y10] : (big_p(Y10))))] --> [], inference(leftNotAll, [status(thm), 4, 'Sko_0'], [f8])).

fof(f17, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), (? [X8] : (big_p(X8))), (! [Y10] : (big_p(Y10))), big_p(Sko_2), ~((! [Y6] : ((big_p(Sko_2) <=> big_p(Y6))))), ~((big_p(Sko_2) <=> big_p(Sko_3))), ~(big_p(Sko_2)), big_p(Sko_3)] --> [], inference(leftHyp, [status(thm), 8], [])).

fof(f19, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), (? [X8] : (big_p(X8))), (! [Y10] : (big_p(Y10))), big_p(Sko_2), ~((! [Y6] : ((big_p(Sko_2) <=> big_p(Y6))))), ~((big_p(Sko_2) <=> big_p(Sko_3))), ~(big_p(Sko_3)), big_p(Sko_3)] --> [], inference(leftHyp, [status(thm), 8], [])).

fof(f18, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), (? [X8] : (big_p(X8))), (! [Y10] : (big_p(Y10))), big_p(Sko_2), ~((! [Y6] : ((big_p(Sko_2) <=> big_p(Y6))))), ~((big_p(Sko_2) <=> big_p(Sko_3))), ~(big_p(Sko_3))] --> [], inference(leftForall, [status(thm), 4, $fot(Sko_3)], [f19])).

fof(f16, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), (? [X8] : (big_p(X8))), (! [Y10] : (big_p(Y10))), big_p(Sko_2), ~((! [Y6] : ((big_p(Sko_2) <=> big_p(Y6))))), ~((big_p(Sko_2) <=> big_p(Sko_3)))] --> [], inference(leftNotIff, [status(thm), 7], [f17, f18])).

fof(f15, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), (? [X8] : (big_p(X8))), (! [Y10] : (big_p(Y10))), big_p(Sko_2), ~((! [Y6] : ((big_p(Sko_2) <=> big_p(Y6)))))] --> [], inference(leftNotAll, [status(thm), 6, 'Sko_3'], [f16])).

fof(f14, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), (? [X8] : (big_p(X8))), (! [Y10] : (big_p(Y10))), big_p(Sko_2)] --> [], inference(leftNotEx, [status(thm), 1, $fot(Sko_2)], [f15])).

fof(f7, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))), (? [X8] : (big_p(X8))), (! [Y10] : (big_p(Y10)))] --> [], inference(leftExists, [status(thm), 3, 'Sko_2'], [f14])).

fof(f4, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), ~((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6))))))), ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))] --> [], inference(leftIff, [status(thm), 2], [f6, f7])).

fof(f27, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), ~((? [X8] : (big_p(X8)))), (! [Y10] : (big_p(Y10))), (big_p(Sko_4) <=> big_p(X8_32)), ~(big_p(Sko_4)), ~(big_p(X8_32)), ~(big_p(X8_34)), big_p(X8_32)] --> [], inference(leftHyp, [status(thm), 8], [])).

fof(f26, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), ~((? [X8] : (big_p(X8)))), (! [Y10] : (big_p(Y10))), (big_p(Sko_4) <=> big_p(X8_32)), ~(big_p(Sko_4)), ~(big_p(X8_32)), ~(big_p(X8_34))] --> [], inference(leftForall, [status(thm), 5, $fot(X8_32)], [f27])).

fof(f24, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), ~((? [X8] : (big_p(X8)))), (! [Y10] : (big_p(Y10))), (big_p(Sko_4) <=> big_p(X8_32)), ~(big_p(Sko_4)), ~(big_p(X8_32))] --> [], inference(leftNotEx, [status(thm), 4, $fot(X8_34)], [f26])).

fof(f28, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), ~((? [X8] : (big_p(X8)))), (! [Y10] : (big_p(Y10))), (big_p(Sko_4) <=> big_p(X8_32)), big_p(Sko_4), big_p(X8_32), ~(big_p(X8_32))] --> [], inference(leftHyp, [status(thm), 9], [])).

fof(f25, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), ~((? [X8] : (big_p(X8)))), (! [Y10] : (big_p(Y10))), (big_p(Sko_4) <=> big_p(X8_32)), big_p(Sko_4), big_p(X8_32)] --> [], inference(leftNotEx, [status(thm), 4, $fot(X8_32)], [f28])).

fof(f23, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), ~((? [X8] : (big_p(X8)))), (! [Y10] : (big_p(Y10))), (big_p(Sko_4) <=> big_p(X8_32))] --> [], inference(leftIff, [status(thm), 6], [f24, f25])).

fof(f21, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), ~((? [X8] : (big_p(X8)))), (! [Y10] : (big_p(Y10)))] --> [], inference(leftForall, [status(thm), 3, $fot(X8_32)], [f23])).

fof(f35, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), (? [X8] : (big_p(X8))), ~((! [Y10] : (big_p(Y10)))), big_p(Sko_5), ~(big_p(Sko_6)), (big_p(Sko_4) <=> big_p(Sko_6)), ~(big_p(Sko_4)), (big_p(Sko_4) <=> big_p(Sko_5)), ~(big_p(Sko_5))] --> [], inference(leftHyp, [status(thm), 11], [])).

fof(f36, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), (? [X8] : (big_p(X8))), ~((! [Y10] : (big_p(Y10)))), big_p(Sko_5), ~(big_p(Sko_6)), (big_p(Sko_4) <=> big_p(Sko_6)), ~(big_p(Sko_4)), (big_p(Sko_4) <=> big_p(Sko_5)), big_p(Sko_4)] --> [], inference(leftHyp, [status(thm), 9], [])).

fof(f34, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), (? [X8] : (big_p(X8))), ~((! [Y10] : (big_p(Y10)))), big_p(Sko_5), ~(big_p(Sko_6)), (big_p(Sko_4) <=> big_p(Sko_6)), ~(big_p(Sko_4)), (big_p(Sko_4) <=> big_p(Sko_5))] --> [], inference(leftIff, [status(thm), 10], [f35, f36])).

fof(f32, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), (? [X8] : (big_p(X8))), ~((! [Y10] : (big_p(Y10)))), big_p(Sko_5), ~(big_p(Sko_6)), (big_p(Sko_4) <=> big_p(Sko_6)), ~(big_p(Sko_4))] --> [], inference(leftForall, [status(thm), 3, $fot(Sko_5)], [f34])).

fof(f33, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), (? [X8] : (big_p(X8))), ~((! [Y10] : (big_p(Y10)))), big_p(Sko_5), ~(big_p(Sko_6)), (big_p(Sko_4) <=> big_p(Sko_6)), big_p(Sko_4), big_p(Sko_6)] --> [], inference(leftHyp, [status(thm), 7], [])).

fof(f31, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), (? [X8] : (big_p(X8))), ~((! [Y10] : (big_p(Y10)))), big_p(Sko_5), ~(big_p(Sko_6)), (big_p(Sko_4) <=> big_p(Sko_6))] --> [], inference(leftIff, [status(thm), 8], [f32, f33])).

fof(f30, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), (? [X8] : (big_p(X8))), ~((! [Y10] : (big_p(Y10)))), big_p(Sko_5), ~(big_p(Sko_6))] --> [], inference(leftForall, [status(thm), 3, $fot(Sko_6)], [f31])).

fof(f29, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), (? [X8] : (big_p(X8))), ~((! [Y10] : (big_p(Y10)))), big_p(Sko_5)] --> [], inference(leftNotAll, [status(thm), 5, 'Sko_6'], [f30])).

fof(f22, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6)))), (? [X8] : (big_p(X8))), ~((! [Y10] : (big_p(Y10))))] --> [], inference(leftExists, [status(thm), 4, 'Sko_5'], [f29])).

fof(f20, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), (! [Y6] : ((big_p(Sko_4) <=> big_p(Y6))))] --> [], inference(leftNotIff, [status(thm), 2], [f21, f22])).

fof(f5, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))), (? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))), ~(((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))] --> [], inference(leftExists, [status(thm), 1, 'Sko_4'], [f20])).

fof(f3, plain, [~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))))] --> [], inference(leftNotIff, [status(thm), 0], [f4, f5])).

fof(f2, plain, [((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))] --> [((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))], inference(hyp, [status(thm), 0], [])).

fof(f1, plain, [] --> [((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))), ~(((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10))))))], inference(rightNot, [status(thm), 1], [f2])).

fof(f0, plain, [] --> [((? [X4] : ((! [Y6] : ((big_p(X4) <=> big_p(Y6)))))) <=> ((? [X8] : (big_p(X8))) <=> (! [Y10] : (big_p(Y10)))))], inference(cut, [status(thm), 1], [f1, f3])).



% SZS output end Proof for SYN374+1.p
