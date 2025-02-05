# Header Parameters

SC-TPTP supports variants of first order logic. Such variants can be indicated in the header of problem or proof file, under the `LOGIC` tag.

| Parameter | Description |
| :- | :- | :- | :- |
| `intuitionistic` | Restrict the logic to intuitionistic first order logic. Sequents may never contain more than one formula on the right hand side. |
| `tff` | | |  The proof uses TFF syntax rather than FOF formulas. Without other option, it only allows for `$o`and `$i` types.
|       | `typed` | | Enables term-level types other than `$i`. Not yet supported
|       | `epsilon` | | Enables the $\epsilon$-operator and `rightEpsilon` as level 1 proof step. |
| `schem` | | | Enables schematic formulas, functions and predicates. These symbols cannot be bound. Enables the rule `Inst`.
| `propext` | | | Enables propositional extentionality. `rightSubstIff` and `leftSubstIff` are considered level 1 proof steps. This extension is necessary to substitute equivalent formulas in the scope of an $\epsilon$, if `epsilon`is enabled. |

## Example

```tptp
% LOGIC    : epsilon_propext
```