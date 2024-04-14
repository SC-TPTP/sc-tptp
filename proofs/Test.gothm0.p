
% SZS output start Proof for Test.gothm0.p


fof(c_Test_gothm0_p, conjecture, (((sp & sq) | (sp & sr)) => (sp & (sq | sr)))).

fof(f8, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr))), (sp & sq), sp, sq, ~(sp)] --> [], inference(leftHyp, [6, 4], [])).

fof(f10, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr))), (sp & sq), sp, sq, ~((sq | sr)), ~(sq), ~(sr)] --> [], inference(leftHyp, [7, 5], [])).

fof(f9, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr))), (sp & sq), sp, sq, ~((sq | sr))] --> [], inference(leftNotOr, [6], [f10])).

fof(f7, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr))), (sp & sq), sp, sq] --> [], inference(leftNotAnd, [2], [f8, f9])).

fof(f5, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr))), (sp & sq)] --> [], inference(leftAnd, [3], [f7])).

fof(f12, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr))), (sp & sr), sp, sr, ~(sp)] --> [], inference(leftHyp, [6, 4], [])).

fof(f14, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr))), (sp & sr), sp, sr, ~((sq | sr)), ~(sq), ~(sr)] --> [], inference(leftHyp, [8, 5], [])).

fof(f13, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr))), (sp & sr), sp, sr, ~((sq | sr))] --> [], inference(leftNotOr, [6], [f14])).

fof(f11, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr))), (sp & sr), sp, sr] --> [], inference(leftNotAnd, [2], [f12, f13])).

fof(f6, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr))), (sp & sr)] --> [], inference(leftAnd, [3], [f11])).

fof(f4, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr)))), ((sp & sq) | (sp & sr)), ~((sp & (sq | sr)))] --> [], inference(leftOr, [1], [f5, f6])).

fof(f3, plain, [~((((sp & sq) | (sp & sr)) => (sp & (sq | sr))))] --> [], inference(leftNotImp, [0], [f4])).

fof(f2, plain, [(((sp & sq) | (sp & sr)) => (sp & (sq | sr)))] --> [(((sp & sq) | (sp & sr)) => (sp & (sq | sr)))], inference(hyp, [0, 0], [])).

fof(f1, plain, [] --> [(((sp & sq) | (sp & sr)) => (sp & (sq | sr))), ~((((sp & sq) | (sp & sr)) => (sp & (sq | sr))))], inference(rightNot, [1], [f2])).

fof(f0, plain, [] --> [(((sp & sq) | (sp & sr)) => (sp & (sq | sr)))], inference(cut, [1, 0], [f1, f3])).



% SZS output end Proof for Test.gothm0.p