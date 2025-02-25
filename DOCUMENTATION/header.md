# Header Parameters

SC-TPTP supports variants of first order logic. Such variants can be indicated in the header of problem or proof file, under the `LOGIC` tag.

| Parameter | Description |
| :- | :- |
| `classical` | Indicates the the proof contains sequents with more than one formula on the right-hand side or uses higher-level proof steps requiring classical logic. |
| `tff` |  The proof uses TFF syntax rather than FOF formulas. Without other options, it only allows for `$o`and `$i` types. Not yet supported
| `typed` | Enables term-level types other than `$i`. Not yet supported
| `epsilon` | | Enables the $\epsilon$-operator and `rightEpsilon` as level 1 proof step. |
| `schem` | Enables schematic formulas, functions and predicates. These symbols cannot be bound. Enables the rule `Inst`.
| `propext` | Enables propositional extentionality. This extension is necessary to substitute equivalent formulas with `rightSubstIff` and `leftSubstIff` in the scope of an $\epsilon$, if `epsilon`is enabled. |
| `let` | Enables usage of `let` definitions to permit structure sharing of formulas across the proof |
| `fot` | Enables top-level annotated terms, which can be annotated with `let`or `simplify` |

## Example

```tptp
% LOGIC    : epsilon_propext
```