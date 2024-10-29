use egg::{*, rewrite as rw};


//implement function asString for Vec<FlatTerm<egg::SymbolLang>> as a function not a method
fn as_string(v: &Vec<FlatTerm<egg::SymbolLang>>) -> String {
    let strs: Vec<String> = v.iter()
    .map(|e| e.to_string())
    .collect();
    strs.join("\n")
}

fn main() {
    println!("Hello, world!");

    let rules: &[Rewrite<SymbolLang, ()>] = &[
        rw!("rule8"; "(sf (sf (sf (sf (sf (sf (sf (sf cc))))))))" => "cc"),
        rw!("rule5"; "(sf (sf (sf (sf (sf cc)))))" => "cc"),

    ];

    let expr = "(sf (sf (sf (sf (sf (sf (sf (sf cc))))))))".parse().unwrap();
    let start = "(sf cc)".parse().unwrap();
    let end = "cc".parse().unwrap();
    let mut runner = Runner::default().with_explanations_enabled().with_expr(&expr).run(rules);

    //implement display for Vec<FlatTerm<egg::SymbolLang>>

    println!("{}", as_string(runner.explain_equivalence(&start, &end).make_flat_explanation()));


    


    //complete this
    // (sf cc)
    // (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc)))))))
    // (sf (sf (sf (sf (sf (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc))))))))))))
    // (sf (sf (sf (Rewrite=> rule8 cc))))
    // (sf (sf (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc)))))))))
    // (Rewrite=> rule8 cc)
    let expected_result = vec![
        "fof(f1, plain, [(f(f(f(f(f(f(f(f(cc)))))))) = cc), (f(f(f(f(f(cc))))) = cc)] --> [f(cc) = f(cc)], inference(RightRefl, param(0), [])).", // (sf cc)
        "fof(f2, plain, [(f(f(f(f(f(f(f(f(cc)))))))) = cc), (f(f(f(f(f(cc))))) = cc)] --> [f(cc) = f(f(f(f(f(f(cc))))))], inference(RightSubstEq, param(1, f(cc) = f(Y), Y), [f1])).", // (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc)))))))
        "fof(f3, plain, [(f(f(f(f(f(f(f(f(cc)))))))) = cc), (f(f(f(f(f(cc))))) = cc)] --> [f(cc) = f(f(f(f(f(f(f(f(f(f(f(cc)))))))))))], inference(RightSubstEq, param(1, f(cc) = f(f(f(f(f(f(Y)))))), Y), [f2])).", // (sf (sf (sf (sf (sf (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc)))))))))))
        "fof(f4, plain, [(f(f(f(f(f(f(f(f(cc)))))))) = cc), (f(f(f(f(f(cc))))) = cc)] --> [f(cc) = f(f(f(cc)))], inference(RightSubstEq, param(0, f(cc) = f(f(f(Y))), Y), [f3])).", // (sf (sf (sf (Rewrite=> rule8 cc))))
        "fof(f5, plain, [(f(f(f(f(f(f(f(f(cc)))))))) = cc), (f(f(f(f(f(cc))))) = cc)] --> [f(cc) = f(f(f(f(f(f(f(cc)))))))], inference(RightSubstEq, param(1, f(cc) = f(f(f(Y))), Y), [f4])).", // (sf (sf (sf (Rewrite<= rule5 (sf (sf (sf (sf (sf cc))))))))
        "fof(f6, plain, [(f(f(f(f(f(f(f(f(cc)))))))) = cc), (f(f(f(f(f(cc))))) = cc)] --> [f(cc) = cc], inference(RightSubstEq, param(0, f(cc) = Y, Y), [f5])).", // (Rewrite=> rule8 cc)
    ];
    println!("{:?}", expected_result);




}
