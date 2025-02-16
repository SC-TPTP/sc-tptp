fof(2, plain, [~(q(V0)) | p(V0)] --> [], inference(clausify, [status(thm)] , [0])).
fof(3, plain, [q(V0)] --> [], inference(clausify, [status(thm)] , [0])).
fof(4, plain, [~(q(V0)) | ~(p(c))] --> [], inference(clausify, [status(thm)] , [0])).
fof(8, plain, [~(q(V100)) | ~(p(c))] --> [], inference(instantiate, [status(thm), 0, $fot(V0), $fot(V100)], [4])).
fof(9, plain, [q(V100)] --> [], inference(instantiate, [status(thm), 0, $fot(V0), $fot(V100)], [3])).
fof(5, plain, [~(p(c))] --> [], inference(res, [status(thm), 0, 0], [8, 9])).
fof(10, plain, [~(q(V100)) | p(V100)] --> [], inference(instantiate, [status(thm), 0, $fot(V0), $fot(V100)], [2])).
fof(11, plain, [q(V100)] --> [], inference(instantiate, [status(thm), 0, $fot(V0), $fot(V100)], [3])).
fof(12, plain, [p(V100)] --> [], inference(res, [status(thm), 0, 0], [10, 11])).
fof(6, plain, [p(V0)] --> [], inference(instantiate, [status(thm), 0, $fot(V100), $fot(V0)], [12])).
fof(13, plain, [p(c)] --> [], inference(instantiate, [status(thm), 0, $fot(V0), $fot(c)], [6])).
fof(7, plain, [$false] --> [], inference(res, [status(thm), 0, 0], [5, 13])).