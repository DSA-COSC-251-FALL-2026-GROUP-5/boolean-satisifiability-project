# The SAT problem

Define

$$
\mathbb{B} = \{0, 1\}
$$

We want to answer the question, for a boolean function $f: \mathbb{B}^n \rightarrow \mathbb{B}$ which consists purely of the conjunction $\land$, disjunction $\lor$, and negation operator $\neg$; we want to determine:

$$
\exists \mathbf{x} \in \mathbb{B}^n \text{ such that } f(\mathbf{x}) = 1
$$

# Motivating the SAT problem through reductions to SAT

## Searching for tautologies

Suppose we want to determine whether the boolean function $g: \mathbb{B}^n \rightarrow \mathbb{B}$ is a tautology, that is, we want to determine whether:

$$
\forall \mathbf{x} \in \mathbb{B}^n, g(\mathbf{x}) = 1
$$

We want to make this in the form of the SAT problem.

To do this:

$$
\neg \exists \mathbf{x} \in \mathbb{B}^n, g(\mathbf{x}) = 0
$$

We define:

$$
f(\mathbf{x}) = \neg g(\mathbf{x})
$$

Thus, our problem becomes:

$$
\neg \exists \mathbf{x} \in \mathbb{B}^n, f(\mathbf{x}) = 1
$$

So we just solve the SAT problem and then negate the answer.

## Determining equivalences

Suppose that we have two boolean functions $g: \mathbb{B}^n \rightarrow \mathbb{B}$ and $h: \mathbb{B}^n \rightarrow \mathbb{B}$.

The question is:

$$
\forall \mathbf{x} \in \mathbb{B}^n, g(\mathbf{x}) == h(\mathbf{x})
$$

This means that we can define:

$$
f(\mathbf{x}) = \neg \left( g(\mathbf{x}) == h(\mathbf{x}) \right)
$$

And then ask the question:

$$
\forall \mathbf{x} \in \mathbb{B}^n, f(\mathbf{x}) = 0
$$

And so:

$$
\neg \exists \mathbf{x} \in \mathbb{B}^n, f(\mathbf{x}) = 1
$$

Note, we see that for 2 boolean variables $p, q \in \mathbb{B}$:

$$
p = q \iff p \oplus q = 0
$$

Therefore, we can even define:

$$
f(\mathbf{x}) = g(\mathbf{x}) \oplus h(\mathbf{x})
$$

Note that XOR operator $\oplus$ can be formed from just the $(\land, \lor, \neg)$ symbols, that is:

$$
p \oplus q = (p \land \neg q) \lor (\neg p \land q)
$$

We can place it in CNF form:

$$
p \oplus q = (\neg (\neg p \lor q)) \lor (\neg (p \lor \neg q)) = (\neg p \lor q) \land (p \lor \neg q)
$$

## K-Coloring problem to SAT

Let's first define the K-coloring problem for graphs.

A graph can be defined by the set of edges and verticies.

That is, first, we have a set of vertices $V$, and then we have a set of edges $E$ such that all elements in the set composes of another sets going from one vertex to another vertex in $V$.

The K-coloring first assigns $k$ colors, say in the set $C$ such that $|C| = k$.

Essentially, we want to determine a coloring function $c: V \rightarrow C$, such that:

$$
\forall (i, j) \in V; c(i) \neq c(j)
$$

To turn this into SAT, we essentially want to create a boolean function $f$ which will be satisfiable only if there's a valid color assignment to each vertices from the k colors:

Create boolean variables:

$$
C_{c,v}
$$

Where we override the definition of $c$ to be simply be an element:

$$
c \in C
$$

And:

$$
v \in V
$$

First of all, we encode the fact that for each $v \in V$, we only assign one color.

To do this, we state that to assign exactly one color to a vertex:

- every vertex must have at least one color assignment
- every vertex must have at most one color assignment

To say that every vertex must have at least one color, we essentially want:

$$
\forall v \in V, \bigvee_{c \in C} C_{c, v}
$$

To say that every vertex must have at most one color, we want something like:

- for every vertex, for every pair of boolean variables representing that vertex, it can never be that both of them are true.

That is, we get the truth table:

| p | q | desired |
| --------------- | --------------- | --------------- |
| 0 | 0 | 1 |
| 0 | 1 | 1 |
| 1 | 0 | 1 |
| 1 | 1 | 0 |

So the desired boolean function is $\neg (p \land q)$

So essentially:

$$
\forall v in V, \bigwedge_{i , j \in C \land i < j}(\neg C_{i, v} \lor \neg C_{j, v})
$$

We must also constraint that no neighboring vertices should have the same color. That is to say, if we one of the boolean variable with the same $c$ is true (we assigned some color to a one of the vertex), it we want the whole expression to be false when the other guy is also true.

| p | q | desired |
| --------------- | --------------- | --------------- |
| 0 | 0 | 1 |
| 0 | 1 | 1 |
| 1 | 0 | 1 |
| 1 | 1 | 0 |

So it's going to be $\neg (p \land q)$ again.

$$
\forall (u, v) in E, \forall c \in C, \neg C_{c, u} \lor \neg C_{c, v}
$$
