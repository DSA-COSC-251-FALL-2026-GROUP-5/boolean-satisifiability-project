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
\forall v \in V, \bigwedge_{i , j \in C \land i < j}(\neg C_{i, v} \lor \neg C_{j, v})
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
\forall (u, v) \in E, \forall c \in C, \neg C_{c, u} \lor \neg C_{c, v}
$$

## Sudoku to SAT

It is possible to encode sudoku into k-coloring (standard sudoku would be 9-coloring) but we can also do it directly.

We want to define a boolean function $f$ such that it will be satisfiable only if the sudoku puzzle have a solution.

We have $r \in R$ columns, $c \in C$ rows and $d \in D$ digits.

Essentially, we probably want the digit set $|D|$ to be of a square number, and then we want $|R| = |C| = |D|$.

But in either case, suppose we already have our parameters for the sudoku board.

So to do this, we define boolean variables as such:

$$
A_{r, c, d}
$$

We now impose the following constraints:

- for every $(r, c) \in R \times C$, we want only one digit in each $(r, c)$, this can be broken down into 2 constraints:

  - for every $(r, c) \in R \times C$, we want at least one of the $A_{r, c, d}$ to be true, that is

  $$
  \forall (r, c) \in R \times C, \exists d \in D, A_{r, c, d}
  $$

  - for every $(r, c) \in R \times C$, we want at most one of the $A_{r, c, d}$ to be true, that is, for every unordered pair $(i, j) \in D^2$ (need to constraint that they're not identical, and doesn't care about order), we never want both of them to be true, that is:

  $$
  \forall (r, c) \in R \times C, \forall (i, j) \in D^2, (i < j) \land (\neg A_{r, c, i} \lor \neg A_{r, c, j})
  $$

- now that we assign uniquely only one digit per $(r, c)$, we now need to constraint that fact that, for every row, they need to be unique, for every column, they need to be unique, and also unique for the sub-square.

I'll just define a set constructor:

$$
S(r, c)
$$

Which gives you the set of coordinates of a sub-square that the coordinate $(r, c)$ belongs in.

We say that for each row, all digits must be assigned.

we do the same for all columns, and for all sub-squares.

For rows:

$$
\forall d \in D, \forall r \in R, \exists c \in C, A_{r, c, d}
$$

For columns:

$$
\forall d \in D, \forall c \in C, \exists r \in R, A_{r, c, d}
$$

And do something similar for sub-squares.

We create a new coordinate system for subsquares, call it $(u, v)$.

Let's say that $S(u, v)$ specifies the set of coordinates inside of the subsquare

$$
\forall d \in D, \forall (u, v) \in U \times V, \exists (r, c) \in S(u, v), A_{r, c, d}
$$

We do the same for all rows, and then we do the same for all sub-squares using the sub-square constructor $S(u, v)$.

We unravel the existential and universal quantifier into the disjunction and conjunction operators.

Also, as long as you have universal quantifiers in the outer loop, existential quantifiers in the inner loop, and inner expression defined without the conjunction $\land$ operator, it should be in CNF form.

And also, if we're provided some digits already, then we just include that as a clause with only one literal.

# Plan to implement a SAT solver

The plan is we first build the generic data structure and utilities that any SAT solver should have and share, and then modify the core of the algorithm to optimize it.

If our input is already in CNF, we don't need to construct any tree structure, because we know structure of the operators already.

## data structure

So first, our boolean variables will be uniquely identified by integers, with a negative sign to indicate the negation sign.

We will essentially have a 2d array, for example:

```
expression = [
  [-1, 2, 3],
  [2, -3, 4, 5],
]
```

In the inner array, it expresses variables to OR into one another, and then the outer loop expresses variables to AND into one another.

Now for assignment, we use `-1` for `false` assignment `0` for unassigned, and `1` for `true` assignment.

For example:

```
assignment = [-1, 1, 0, 1, -1]
```

Where each index corresponds to one of the variables in our expression. Also, note that if we to evaluate, it's going to be $O(m)$ where $m$ is total size of the expression, because we can access the assignment in O(1) time.

We can define an `evaluate` function:

```
evaluate(expression, assignment)
```

## Generic functionalities

Essentially, the universal API is by using the DIMACS CNF file format. So we're going to need to build a DIMACS CNF parser and also DIMACS CNF constructor. The DIMACS CNF file is [specified here](https://jix.github.io/varisat/manual/0.2.0/formats/dimacs.html).

We also need to somehow generate the clauses for Sudoku and also for K-coloring.

I think internally, we should probably just use the array representation, and then just construct it into DIMACS 

## Bruteforce

Essentially, we can just do bruteforce by setting the assignment all to false, incrementing it one by one and then evaluate it.

We can also construct some sort of uniform shuffle function such that we select different assignments randomly, so that the average running time would be:

$$
O(\rho 2^n)
$$

Where $\rho$ is going to be the proportion of $\frac{N(\text{false})}{2^{n}}$, where $N(\text{false})$ is essentially the total amount of `false` results we get from the boolean expression.

Let's see how we can get this uniform shuffle function, without having to do `random.shuffle(list(range(2**n)))`

Essentially, we want a function $f$ that has a domain of $D = \{0, 1, ..., n\}$ and a range of $D$ also, such that it's bijective, and such that it shuffles it in a sort of uniform pseudo random manner. I suppose if we want this function to be deterministic, you should also input a seed.

It means that:

$$
f: D \times \mathbb{R} \rightarrow D
$$

And it's bijective when we fix $r \in \mathbb{R}$.

The reason why we want to construct such a function is because, we don't want to store $O(2^n)$ assignments

One of the ways to construct such a function that's bijective is to use modulo arithmetic, however, it's not going to be random because it's just going to translate.

I think we can modify some well-known hash function to do this.

Also, this problem is called pseudo random permutation.

I suppose I can look into Feistel networks

## DPLL

The core idea behind DPLL and optimizations beyond bruteforce is that instead of assigning all of the boolean variables in one go, we assign just one boolean variable, and then look ahead to see whether we can simplify the expression.

For example:

- if any boolean variable inside of the clause evaluates to true, we can remove it
- if any of the boolean variables in the expression have all negation sign in front, or have all no negation sign, we can just set it to true or false accordingly to make the whole clause false, and just remove the entire clause that's in it.

For unit propagation, we essentially search for unit clauses, and then say that for the whole expression to be satisfiable, we must let that unit clause be assigned some boolean value.

Pure literal elimination is what the second optimization is.

So DPLL looks like:

```
1. Perform unit propagation.
2. Perform pure literal elimination.
3. If an empty clause exists, return UNSAT.
4. If no clauses remain, return SAT.
5. Choose an unassigned variable.
6. Branch and recursively solve.
```

We also need to do branching for guessing.

Main idea:

The main idea is to treat the satisifiability problem as a Constraint Satisifiability Problem, that is to say, each clause is a constraint, and we ask whether there's any kind of stuff that actually satisfies the constraint. If not, that there's nothing in the universe that can satisfy the constraint, and I guess it would amount to having "no solution" etc.

For a constraint satisifiability problem, what we do is a depth first search.

So essentially, we build a tree (a binary tree). At each layer, we assign either "true" or "false" to the one of the variables, and we continue on-wards.

> [!NOTE]
> Notice that if we have for example, variables, a, b, c; we can have the tree be a, then b then c. Or we could have b, then a, then c, etc. There are probably ways to choose which layer to pick next dynamically, although making the tree very dynamic (a, b, c changes as the depth first search continues) might be a bit difficult.

Before we branch out, we essentially check a couple of things:

- unit clauses
- pure literal elimination

## general idea

base cases
- if the expression has no more clauses, then it's satisfied (the idea is that, if we have no constraints, then anything satisfies our constraint)
- if there's a clause without any literals, then it's false (this a bit weird, but we say that the constraint cannot be satisifed no matter what or something... Either way, in the CNF, an empty clause is the same as a clause with just "false")

Unit clauses simplify our expression, to say, ok, when I assign a value to a literal, it doesn't change whether the simplified expression is satisfiable or unsatisfiable. 

For implementation, this is going to be a bit tough... Because there are times when you assign varaibles because you're doing depth first search, but there are also other times when you're assigning variables because it has to be the case... But the thing is, the "it has to be the case" is probably only the case for the current branch.

# Counting up by one on a bitset

Let's say that we have:

```java
boolean[] arr = {false, false, false};
```

the next step is:

```
{true, false, false}
```

next is:

```
{false, true, false}
```

next is:

```
{true, true, false}
```

to do a count up (let's say that LSB is on the left), what we do is say, we store a temporary rollover variable, and say:

```java
boolean rollover = true;
for (int i = 0; i < arr; i++){
  if (rollover){
    // if we did have a rollover, then first see if we'll rollover
    rollover = ! (a ^ 1);
    // if we didn't have a rollover, then just 
    arr[i] = ! arr[i];
  }
}
```

we can simplify this:

```java
boolean rollover = true;
int i = 0;
while (rollover){
  if (i >= arr[i].length){
    break;
  }
  rollover = ! (arr[i] ^ 1);
  arr[i] = ! arr[i];
  i++;
}
```

## DPLL algorithm outline

### Unit propagation

If we have a unit literal:

$$
a \land (\neg a \lor b)
$$

We know that if we set $a$ to false, then that whole branch isn't going to be satisfiable.

So we need to set $a$ to true, as such, it's a simplification that can be done by restricting $a = True$. That's to say, when we simplify, we set $a = True$ and remove the branch where $b = False$ in our search, and we continue.

### Pure Literal Elimination

If we have a pure literal, that is all instances of the literal contains the same sign, then what we'll need to do is that we can remove it, so we can set it to true if there's no negation, and false if there's a negation.

For example:

$$
(\neg a \lor b) \land (\neg a \lor c)
$$

We can set $\neg a$ to be true, so that we don't consider $\neg a$ anymore.

Now here's the iterative step. Inside of `simplify`, we continue doing this simplification until we hit a deadend, that is, there's no unit clauses, and no more pure literals.

Now in that sense, after we've done `simplify`, we now have no choice but to choose another assignment and then continue on the recursive call, however, if we've simplified iteratively, until we have assigned like everything, then it's satisfiable, since we choose all possible options such that we maintain a possible satisfiable solution.

Even something like a contradiction:

$$
a \land (\neg a)
$$

If we do unit propagation, pure literal elimination iteratively, then here's what we can do:

Using pure literal elimination, we say $a$ has to be True

So our assignment array looks like `[true]`

Now we say ok, `simplify` terminates when there's no None left.

Now we exit out of `simplify`, we go into the `if None in assignments` condition, we'll see that we get a false, thus, for that particular branch, it's automatically unsatisfiable, or if we do get a true, then we've found a truth assignment, thus, it's satisifiable.

Now even if we haven't assigned everything yet after the simplification process, we still know that whatever was done in the `simplify` call was eliminating a bunch of branches that immediately makes the whole thing false (by setting a clear assignment to some literals in the `assignments` array).

We can then only look at the branch that `simplify` has narrowed down for us, and then choose a new assignment for some literal and then recusively call the same function. Now, if that particular branch actually returns a `true`, then that's good, we'll just state that it's satisifiable. Otherwise, we'll set it to false, and look at that branch, and if they're both `false`, then ew say ok, given that you have already assigned like all of this in `assignments`, for these particular unassigned literals in `assignments`, no matter what you assign them to, they'll always return a false for the expression.

```python
def dpll(expression, assignments):
  # so basically, we're going to write this recursively
  # to contract is that assignments are either true, false, or unassigned (None)
  # the function essentially returns whether or not it's satisfiable if we lock in the assigned variables, 
  # and choose all possible values for the unassigned variables
  
  # so the first step is to simplify the expression if it can be simplified (if we can assign particular variables immediately without changing whether it's satisfiable or satisfiable)
  changes = simplify(expression, assignments)
  # changes will contain a dictionary of the "change history" that the simplification did
 
  # the base case is whether we've assigned everything:
  if not (None in assignments):
    # evaluate is just going to plug in all of the assignments (non-None assingments, and give the answer)
    # it's going to return a true if it evaluates to true, false if it evaluates to false (I think this still satisfy our contract)
    return evaluate(expression, assignments)
  
  # if there's still some stuff unassigned, then we essentially pick a variable to assign.
  # pick will be a function because, we might use a different heuristic other than "pick the first unassigned variable" (although making sure no redundant computation would be kinda hard)
  variable_to_assign = pick(assignments)
  
  # so we'll first try to assign it true
  assignments[variable_to_assign - 1] = True
  changes[variables_to_assign - 1] = True #Note: although this looks like an array, hash map would be better because we dont want to copy the whole array, we only want differential backups
  if (dpll(expression, assignments)):
    # so if it does return a True, meaning that there was a satisifiable assignment, just say yes
    return True

  # if after setting it as True, we get no satisifiable assignments, then we set it to False and try again
  
  assignments[variables_to_assign - 1] = False
  changes[variables_to_assign - 1] = False

  if (dpll(expression, assignments)):
    return True

  # now if after all that, we didn't get any truth assignments, then it's unsatisfiable for that particular branch
  undo(assignments, changes)
  return False
```

> [!NOTE]
> Think about the case when we don't use a particular variable in an expression

# Understanding simplification

So right now, we have implemented 2 simplifications:

- unit propagation
- pure elimination

But we must understand what it is we're doing when we're performing a "simplification".

When we simplify a expression, we say, ok, if we have expression $E_1$ and we simplified it to expression $E_2$, then this means that $E_1$ is satisifiable if and only if $E_2$ is satisfiable as well?

In that sense, if $E_1$ wasn't satisfiable to begin with, but we simplified it to $E_2$, and we can more quickly evaluate that $E_2$ isn't satisfiable, then we automatically know that $E_1$ wasn't satisifiable.

This means that after simplification, even if our simplification assigned all literals, there might still be the case that we get an unsatisfied assignment?

# One sample run

| number of variables | time taken |
| 20 | 0m0.086s|
| 50 | 0m0.366s|
| 100 | 0m7.538s|
|125 | 0m15.783s|

# Sudoku

Sudoku is generated from [this site](https://qqwing.com/generate.html).

# KISSAT

Using this flag:

```
kissat -q dataset/flat30-60/flat30-10.cnf 
```

Gives:

```
s SATISFIABLE
v -1 -2 3 -4 -5 6 -7 -8 9 -10 -11 12 -13 -14 15 -16 -17 18 -19 -20 21 -22 -23
v 24 -25 -26 27 -28 -29 30 -31 32 -33 -34 35 -36 -37 38 -39 -40 41 -42 -43 44
v -45 -46 47 -48 -49 50 -51 -52 53 -54 -55 -56 57 -58 59 -60 61 -62 -63 64 -65
v -66 67 -68 -69 70 -71 -72 73 -74 -75 76 -77 -78 79 -80 -81 82 -83 -84 -85 86
v -87 88 -89 -90 0
```

So we'll need to write a simple parser for this
