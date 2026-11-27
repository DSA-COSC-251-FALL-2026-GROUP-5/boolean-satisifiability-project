==== Proof of pure literal elimination <pure-literal-elimination-proof>

To formalize the simplification, we need to define some variables and functions. We have a boolean function $f: bb(B)^n -> bb(B)$ in CNF. Let's say that we have a pure literal $x_i$ (and without loss of generality, assume that it's not negated). We can then split the boolean function into the conjunction of 2 functions, one containing $x_i$ and one that doesn't contain $x_i$.

$
f(bold(y), x_i) = f_0(bold(y)) and f_1(bold(y), x_i)
$<pure-literal-partition>

For some $bold(y) in bb(B)^(n - 1)$, $x_i in bb(B)$.

And to make use of the fact that $x_i$ is a pure literal, and that everything is in CNF, we can write:

$
f_1(bold(y), x_i) = x_i or g(bold(y))
$

What we want to prove is, given that $x_i$ is a pure literal:

$
f(bold(y), x_i) "is satisfiable" <=> f_0(bold(y)) "is satisfiable"
$

Why would this be helpful? Because if we knew that this is true, then working on $f_0(bold(y))$ would be easier because you'd now only have to check $n - 1$ variables rather than $n$ variables.

Now let's expand to what it means for a boolean function to be satisfiable by the definition of satisfiability @satisifiability-definition.

$
exists y in bb(B)^(n - 1), exists x_i in bb(B) "such that" f(bold(y), x_i) = 1 <=> exists y in bb(B)^(n - 1) "such that" f_0(bold(y))
$

Now we to prove this _if and only if_ statement, we must prove it in the forward direction, and the reverse direction.

First, assume that $f(bold(y), x_i)$ is satisfiable, that is $exists y in bb(B)^(n - 1), exists x_i in bb(B) "such that" f(bold(y), x_i) = 1$.

Because we can split it as seen in @pure-literal-partition. We transform our statement into: $exists y in bb(B)^(n - 1), exists x_i in bb(B) "such that" f_0(bold(y)) and f_1(bold(y), x_i)= 1$.

Now because we can use the simplification law, where for some boolean variable $p, q in bb(B)$, $p and q = 1 => p = 1 $; it is implied that $exists y in bb(B)^(n - 1), exists x_i in bb(B) "such that" f_0(bold(y)) = 1$. This means that $f_0(bold(y)) "is satisfiable"$.

Now to prove this in the reverse direction, suppose that $f_0(bold(y))$ is satisfiable, that is: 

$
exists bold(y) in bb(B)^(n - 1) "such that" f_0(bold(y))
$<pure-literal-elimination-reverse-assumption>

Now because $x_i$ is a pure literal, we know that when we set $x_i = 1$, $f_1(bold(y), 1) = 1 or (bold(y)) = 1$. This means that it has to be that:

$
forall bold(y) in bb(B)^(n - 1), exists x_i in bb(B) "such that" f_1(bold(y), x_i) = x_i or g(bold(y)) = 1 
$<pure-literal-implication>

Putting our assumption @pure-literal-elimination-reverse-assumption , and our implication @pure-literal-implication together, we can say:

$
exists y in bb(B)^(n - 1), exists x_i in bb(B) "such that" f_0(bold(y)) = 1 and f_1(bold(y), x_i) = 1
$

And through the partition we used @pure-literal-partition.

$
exists y in bb(B)^(n - 1), exists x_i in bb(B) "such that" f(bold(y), x_i) = 1 <=> f(bold(y), x_i) "is satisfiable" qed
$



