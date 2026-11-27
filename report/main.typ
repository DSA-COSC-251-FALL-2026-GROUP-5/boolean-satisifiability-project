#import "@preview/fyrst-ru-labreport:0.1.0":*
#import "@preview/unify:0.7.0":*
#import "@preview/note-me:0.6.0": *
#import "@preview/tdtr:0.6.1" : *

#show: project.with(
  title: [Boolean Satisfiability (SAT)],
  course-name: [Data Structures],
  course-abrev: [COSC 251 - 001],
  organization: [Group 5],
  logo:image("assets/aupp_logo.png",width:70%),
  authors:(
    (
      name:"Kimchour LUY",
      email:"2025136luy@aupp.edu.kh",
      phone:none
    ),
    (
      name:"Soksovan HENG",
      email:"2025427heng@aupp.edu.kh",
      phone:none
    ),
    (
      name:"Singyang HOEURNG",
      email:"2025138hoeurng@aupp.edu.kh",
      phone:none
    ),
    (
      name:"Monymatin MOENG",
      email:"2025385moeng@aupp.edu.kh",
      phone:none
    ),
    (
      name:"Sophapor HENG",
      email:"2025530heng@aupp.edu.kh",
      phone:none
    )
  ),
  author-columns:3,
  supervisors:(
    (
      name:"Visethboti Sin",
      title:none,
      email:"v.sin@aupp.edu.kh",
      phone:none
    ),
  ),
  supervisors-columns:2,
  bibliography:bibliography("bibliography.bib", style: "apa", title: "References"),
  paper-size:"a4",
  lang:"en",
)

#set text(size:10pt)
#set heading(numbering: "1.1.1.")

#set page(footer: context [
    #counter(page).display("1")]
  )

= Problem

== Problem Definition

The boolean satisfiability (SAT) problem asks whether a boolean function have any assignments which makes the boolean function evaluate to true. 

When a particular input makes the boolean function evaluate to true, we say that the input "satisfies" the boolean function. Using our new jargon, the boolean satisfiability problem asks whether a boolean function is "satisfiable", that is, if there even exists an input that makes the boolean function evaluates to true.

More formally, we can state the SAT problem as such:

We first define the boolean set

$
bb(B) = {0, 1}
$

We now define a boolean function $f: bb(B)^n -> bb(B)$ which consists of only the compositions of the conjunction operator ($and$), disjunction operator ($or$), and negation operator ($not$).

The SAT problem then asks:

$
exists bold(x) in bb(B)^n "such that" f(bold(x)) = 1
$<satisifiability-definition>

If this evaluates to true, then we say that the boolean function $f$ is satisfiable. Otherwise, we say that it isn't satisfiable.

=== Examples

Suppose that we have:

$
f(x_1, x_2, x_3) = (x_1 or x_2 or x_3) and (not x_1 or not x_2 or not x_3)
$

Notice that when we evaluate $f(0, 1, 0)$:

$
f(0, 1, 0) = (0 or 1 or 0) and (1 or 0 or 1) = 1 and 1 = 1
$

Thus, we say that $f$ is satisfiable, and we say that the assignment $x_1 = 0$, $x_2 = 1$, $x_3 = 0$ "satisfies" our boolean function (or constraints).

Now suppose we have:

$
g(x_1) = x_1 and not x_1
$

Is this satisfiable? Well consider all possible assignments:

- if $x_1 = 0$, then $g(0) = 0 and 1 = 0$
- if $x_1 = 1$, then $g(1) = 1 and 0 = 0$

Thus, since we have exhausted our inputs, and none of them cause $g(x_1) = 1$, then we say that the boolean function is unsatisfiable.

== Computational Complexity

The SAT problem was the first problem proven to be NP-Complete by the Cook-Levin theorem @cook1971complexity.

== Motivation and Applications

The reason why we're interested in the SAT problem is because it can be stated as the most generic NP-Complete problem. And since every problem in NP-complete can be converted from one to another#footnote[A problem is NP-hard if all problems in NP can be reduced into it. A problem is NP-Complete if it is both in NP and in NP-hard. Thus, a consequence of the definition of NP-Completeness is that for every pair of problems that are NP-complete, they can all be reduced to one another.], if we can solve SAT efficiently, and we can convert other problems into SAT in polynomial time as well, then we would be able to solve the problem we started with relatively efficiently.

In that sense, a SAT solver could act as a generic black box solver for many different NP-complete problems. Once you have figured out a way to solve SAT relatively efficiently, we could then reuse the same machinery to solve other NP-complete problems without having to write specific algorithms that particular NP-complete problem. And if the SAT solver is really efficient, converting an NP-complete problem into SAT and letting the efficient SAT solver solve it might be one of the more optimal ways to get a solution to your problem quickly.

We will give 3 examples of problems that can be expressed in SAT

=== Verifying Equivalent Boolean Functions

Suppose that we have a boolean function $f:bb(B)^n -> bb(B)$ and another boolean function $g:bb(B)^n -> bb(B)$.

And practically, we imagine that the function $f$ is very lengthy with many operations, while $g$ is a lot simpler.

We might want to ask, is $f(bold(x)) equiv g(bold(x))$?

Why might we care about this? Because if they are indeed equivalent, then it would make more practical sense to use the function $g(bold(x))$.

This is useful because, some components of computers do use combinational logic (such as the ALU), which can then be expressed as a boolean function. As such, if we used a simpler boolean function $g$ that can do the same job as the more complicated function $f$, we would not only save cost (by requiring less logic gates), we would also perhaps make the function faster to evaluate as well.

To express this problem into SAT. We can ask the problem in a slightly different manner: does there exist a value $bold(x) in bb(B)^n$ which makes $f(bold(x)) != g(bold(x))$? If the answer is no, then we can say that they're equivalent. Notice that asking whether $f(bold(x)) != g(bold(x))?$ is the same as asking $f(bold(x)) xor g(bold(x))$.

This means that to determine whether $f(bold(x)) equiv g(bold(x))$, we ask:

$
not exists bold(x) in bb(B)^n "such that" f(bold(x)) xor g(bold(x)) = 1
$

=== K-Coloring <k-coloring-to-sat>

The K-Coloring problem ask that given a set of vertices $V$, a set of edges $E$, and a set of colors $C$, we want to construct a coloring function $c: V -> C$ with the constraint that $forall (v_1, v_2) in E, c(v_1) != c(v_2)$.

This problem can be encoded into SAT using boolean variables:

$
x_(v, c) "for" v in V, c in C
$

Which can be interpreted by saying $x_(v, c) = 1$ when we color vertex $v$ with color $c$. Our final boolean function $f: bb(B)^(|V|dot|C|) -> bb(B)$ is then interpreted as "when $f$ evaluates to true, this particular assignment of colors to vertices satisfies all of our constraint".

Our constraint $f(bold(x))$ is the conjunction of these sub-constraints:

- for every vertex, we must assign exactly one color. This can be broken down as 2 conditions that must hold together:
  - for every vertex, at least one color must be assigned. $forall v in V, exists c in C x_(v, c)$
  - for every vertex, at most one color must be assigned. $forall v in V, and.big_(c_l, c_r in C and (c_l < c_r)) (not x_(v,c_l) or not x_(v, c_r))$
- for every edge, a color must never be assigned to both vertices belonging to an edge. $forall (v_1, v_2) in E, forall c in C, not x_(v_1, c) or not x_(v_2, c)$

#note[
  A universal quantifier $forall z in Z, f(z)$ can be expanded as $and.big_(z in Z) f(z)$.

  Similarly, an existential quantifier $exists z in Z, f(z)$ can be expanded as $or.big_(z in Z) f(z)$.
]

=== Sudoku <sudoku-to-sat>

For sudoku, we have a set of digits $D$, a set of columns $C$, and a set of rows $R$. Similar to the K-Coloring problem, We define boolean variables:

$
x_(r, c, d) forall r in R, forall c in C, forall d in D
$

Our constraints as a boolean function $f(bold(x))$ is the conjunction of the following constraint:

- for every cell, we must assign exactly one digit. This can similarly be broken down into the conjunction of 2 constraints:
  - for every cell, at least one digit must be assigned. $forall r in R, forall c in C, exists d in D x_(r, c, d)$
  - for every cell, at most one digit must be assigned. $forall r in R, forall c in C, and.big_(d_l, d_r in D and (d_l < d_r)) (not x_(r,c,d_l) or not x_(r,c,d_r))$
- every digit must be assigned at least once in every row. $forall r in R, forall d in D, exists c in C x_(r, c, d)$
- every digit must be assigned at least once in every column. $forall c in C, forall d in D, exists r in R x_(r, c, d)$
- every digit must appear at least once in every sub-square. Suppose that I have the set of sub-square $S$ which contains sets of coordinates of a particular sub-square. $forall s in S, forall d in D, exists (r, c) in s, x_(r, c, d)$

Now this is in fact a completely valid encoding of sudoku, however, this program won't complete in time for the DPLL algorithm without these particular redundant constraint:

- every digit must be assigned at most once in every row. $forall r in R, forall d in D, and.big_(c_l, c_r in C and c_l < c_r) not x_(r, c_l, d) or not x_(r, c_r, d)$
- every digit must be assigned at most once in every column. $forall c in C, forall d in D, and.big_(r_l, r_r in R and r_l < r_r) not x_(r, r_l, d) or not x_(r, r_r, d)$
- every digit must be assigned at most once in every sub-square. $forall s in S, forall d in D, and.big_((r_1, c_1), (r_2, c_2) in s and (r_1, c_1) < (r_2, c_2)) not x_(r_1, c_1, d) or not x_(r_2, c_2, d)$


== Difficulty of Problem <difficulty-of-problem>

The difficulty of the problem is that the naive approach to answering this problem requires that you check all possible assignments $forall x in bb(B)^n$ for a boolean function with $n$ variables. Since $|bb(B)| = 2$, this means: $|bb(B)^n| = 2^n$. If it takes $O(l)$ time to evaluate a boolean function with $l$ number of literals, then the worst case time complexity would be $O(l dot 2^n)$.

In terms time complexity, it is extremely hard to improve it. This was known as the Exponential Time Hypothesis (ETH), which stated that 3-SAT (SAT written in conjunctive normal form with 3 literal clauses) cannot be solved in sub-exponential time (that is, in terms of theoretical time complexity, it doesn't get significantly better than the naive approach). It seems however that recently, OpenAI has claimed to have shown that ETH is false, by formulating an alternate algorithm for 3-SAT that has a sub-exponential time complexity @exponentialTimeHypothesis2026. Nonetheless, the proof must still be validated and checked further by experts.

= Solution

For our algorithm, we compared 2 approaches:

1. the brute force approach
2. the Davis-Putnam-Logemann-Loveland (DPLL) algorithm @davis1962machine

== Brute Force Approach

Suppose we have a boolean function $f:bb(B)^n -> bb(B)$, and we have an input $bold(x) in bb(B)^n$.

- We simply first set $bold(x) = (0, 0, ..., 0, 0)$, and then evaluate $f(bold(x))$
- if $f(bold(x)) == 1$, then it's satisfiable, otherwise, check for $bold(x) = (0, 0, ..., 0, 1)$
- and so on...
- after we have checked $bold(x) = (1, 1, ..., 1, 1)$, and the formula still isn't true, then our boolean formula is not satisfiable.

Essentially, we can treat $bold(x)$ as a binary number, and we just increment that number by one until we reach $1111...1111_2$. Thus, the time complexity is clearly exponential time.

== DPLL

DPLL is an optimization over brute force search, which uses some simplifications to half the number of assignments we have to check. It returns an exactly solution to SAT.

First of all, DPLL requires that we represent our boolean function in conjunctive normal form (CNF). If the conjunction operator is analogous to multiplication operator, and the disjunction operator is analogous to the addition operator, then the CNF is analogous to the product of sums form.

An example of a CNF formula:

$
(x_1 or x_2 or x_3) and (x_3 or x_4 or not x_5) and (x_6 or x_7 or x_8)
$

The DPLL algorithm is an algorithm that does depth first search on the "assignment tree". 

#align(center)[
  #tidy-tree-graph(
    compact: true,
    spacing: (20pt, 20pt),
    node-inset: 4pt
  )[
    - Start
      - $x_1 = 0$
        - $x_2 = 0$
          - $x_3 = 0$
          - $x_3 = 1$
        - $x_2 = 1$
          - $x_3 = 0$
          - $x_3 = 1$
      - $x_1 = 1$
        - $x_2 = 0$
          - $x_3 = 0$
          - $x_3 = 1$
        - $x_2 = 1$
          - $x_3 = 0$
          - $x_3 = 1$
  ]
]

However, what's special is that it uses mechanisms like:

- unit-propagation
- pure literal elimination

Which is capable of removing entire branches, thus, reducing our search space significantly. Nonetheless, this algorithm is still exponential time in the worst case because we can say that every time we performed a simplification, we reduce the search space in half. Let's say that we can perform such simplifications $c$ times for a particular boolean function. This means that the run time is $O(l 2^n / 2^c)$ but since $C = 1/2^c$ is still a constant, it's still $O(l 2^n)$.

Although it doesn't improve it's asymptotic time complexity, for SAT instances where you can perform a lot of simplifications, it can turn a SAT instance that could not be solved in a reasonable amount of time using bruteforce, to something that you can solve. For instance, a $9 times 9$ sudoku encoded into SAT using the method in section @sudoku-to-sat can be solved in a reasonable amount of time using DPLL, however, it could not be solved using brute force (since we would need to evaluate $2^729$ assignments).

=== Unit Propagation

Unit propagation relies on the fact that when we have a unit clause (a clause with only one unassigned literal), we can discard one of the branches because it is automatically unsatisfiable.

For example, if we have $f(x_1, x_2, x_3) = x_1 and (x_2 or x_3)$, 

We can see that for the branch $x_1 = 0$, regardless of what values $x_2$ and $x_3$ are, the whole formula will always be false.

Thus, our search tree gets reduced to:

#align(center)[
  #tidy-tree-graph(
    compact: true,
    spacing: (20pt, 20pt),
    node-inset: 4pt
  )[
    - Start
      - $x_1 = 1$
        - $x_2 = 0$
          - $x_3 = 0$
          - $x_3 = 1$
        - $x_2 = 1$
          - $x_3 = 0$
          - $x_3 = 1$
  ]
]

As such, whenever we can perform unit propagation, we reduce the amount of leaves we need to check by a half. If we get lucky and can perform many instances of unit-propagation, it may allow us to solve SAT instances of many more variables compared to the brute force approach.


=== Pure Literal Elimination

A pure literal is a literal where at every instance where it appears, it has the same sign.

For example:

$
f(x_1, x_2, x_3) = (not x_1 or x_2) and (x_3 or not x_1) and (x_2 or x_3) and (not x_2 or x_3) and (not x_1 or not x_3)
$ <pure-literal-elimination-example>

Notice that $x_1$ is a pure literal, because every time it appears, it has the same sign $not x_1$.

Thus, what we can do is reduce the search space by making $not x_1 = 1$ or $x_1 = 0$ in order to satisfy every clause that it appears in, without affecting the state of the clauses in which it doesn't appear in.

$
f(0, x_2, x_3) = (1 or x_2) and (x_3 or 1) and (x_2 or x_3) and (not x_2 or x_3) and (1 or not x_3) = (x_2 or x_3) and (not x_2 or x_3)
$

Notice that we removed an entire branch where we set $x_1 = 1$, because, we can argue that the original function $f(x_1, x_2, x_3)$ is satisfiable, if and only if $f(0, x_2, x_3)$ is satisfiable. We will prove this at section @pure-literal-elimination-proof.

Notice that we can then perform pure literal elimination again on $x_3$ by setting $x_3 = 1$.

$
f(0, x_2, 1) = (x_2 or 1) and (not x_2 or 1) = 1
$

Thus, the specific assignment $x_1 = 0$, $x_2 = 0$, and $x_3 = 1$ would satisfy it.

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

Now we to prove this _if and only if_ statement, we must prove it in the forward direction, and the reverse direction.

For the forward direction we first assume that $f(bold(y), x_i) "is satisfiable"$. Because $f(bold(y), x_i)$ is satisfiable, this also means $f_0(bold(y)) and f_1(bold(y), x_i)$ is satisfiable. Now, what it means for something to be satisfiable is when there exists $bold(y) in bb(B)^(n - 1)$, and $x_i in bb(B)$ in order to make the whole $f(bold(y), x_i) = 1$. Now since we assumed that to be the case, then clearly that particular assignment would also make $f_0(bold(y)) = 1$ and $f_1(bold(y), x_i) = 1$. Thus it is implied that $f_0(bold(y)) = 1$ for that particular assignment, making $f_0(bold(y))$ satisfiable.

For the reverse direction, we assume that $f_0(bold(y))$ is satisfiable. Now when we choose $x_i = 1$, it automatically makes $f_1(bold(y), x_i) = x_i or g(bold(y)) = 1$. This makes $f_1(bold(y), x_i)$ satisfiable. Because both $f_0(bold(y))$ and $f_1(bold(y), x_i)$ is satisfiable, putting the two pieces together gives you $f_0(bold(y)) and f_1(bold(y), x_i) = f(bold(y), x_i)$ is satisfiable $qed$.

=== Other heuristics

There are other heuristics we implemented to improve the performance of the algorithm, such as:

- contradiction detection: when we detect that a clause is false, then no longer explore that the whole branch
- shortest clause branching: when selecting which variable to assign, we assign the variable inside of the clause with the smallest number of unassigned variables

= Implementation

We implemented the program in `java`, and many different functionalities are split of into different classes. The source code is stored in the `src` directory, found in our #link("https://github.com/DSA-COSC-251-FALL-2026-GROUP-5/boolean-satisifiability-project")[github repository]. The important files include:

- `CNF.java`: the internal representation of our boolean expression. We represented the CNF using a 2D `ArrayList` of integers.
- `DIMACS.java`: a DIMACS CNF file parser #footnote[The specification of the DIMACS CNF file can be found here https://jix.github.io/varisat/manual/0.2.0/formats/dimacs.html] <dimacs-specs>
- `BruteForce.java`: a brute force SAT solver.
- `DPLL.java`: a SAT solver implementation using the DPLL algorithm
- `KISSAT.java`: a wrapper around the a state of the art SAT solver called Kissat (used for comparison, and debugging) @kissat.
- `KColoring`: methods to solve the K-coloring problem using SAT
- `Sudoku.java`: methods to solve a $9 times 9$ sudoku puzzle to SAT, and then interpret the results as a solution to sudoku
- `Main.java`: a simple CLI that accepts a SAT solver and a DIMACS CNF file as input, and returns the solution to that instance
- `Test.java`: a small test suite to check for DPLL algorithm's correctness.

== Usage

To build the program, run:

```
./build
```

in the base directory of the project. On windows, make sure that `javac` is included in your PATH environmental variable, and run the command inside of the build script.

The `Main` program accepts a CNF statement using the DIMAC CNF file #footnote(<dimacs-specs>). To use the brute force algorithm:

```
./run Main bf ../dataset/uf20-91/uf20-01.cnf
```

And to run it using the DPLL algorithm:

```
./run Main dpll ../dataset/uf20-91/uf20-01.cnf
```

Converting a graph coloring problem can be done as such:

```java
Graph graph = KColoring.parseGraph("dataset/cambodia.txt");
String[] myColors = { "R", "G", "B", "Y" };
KColoring kcoloring = new KColoring(myColors, graph);
CNF cnf = kcoloring.toSat();
Result result = DPLL.sat(cnf);
Graph coloredGraph = kcoloring.satAssignmentToSolution(result);
System.out.println(coloredGraph.asFileContent());
```

Converting a Sudoku problem can be done as such:

```java
String puzzleAsString = ".......6.4.18.35..97.5.4.1..9....1...857......1.4.57.....1...........358..3..7...";
Integer[] puzzle = Sudoku.puzzleStringToInteger(puzzleAsString);
CNF sudokuAsSat = Sudoku.toSat(puzzle);
Result result = DPLL.sat(sudokuAsSat);
System.out.println(result);
Integer[] solvedSudoku = Sudoku.satAssignmentToSolution(result);
System.out.println(Sudoku.puzzleIntegerToString(solvedSudoku));
```

#note[
  When a graph can't be colored with the set of colors given, or when a sudoku puzzle cannot be solved, the SAT problem will be unsatisfiable.
]

== Data structure and representation

Since our SAT solver accepts the DIMACS CNF file, we expect that our boolean expression will be stored in CNF. This can be done using an 2D `ArrayList` of `Integers`. Moreover, our literals will be represented using `Integer` (rather than symbols) from $1$ onwards, with negative signs indicating a negation in front. For example, the boolean expression @pure-literal-elimination-example can be written in DIMACS CNF as such:

```
p cnf 3 5
-1 2 0
3 -1 0
2 3 0
-2 3 0
-1 -3 0
```

And we can then turn this into a 2D `ArrayList`. Where we have a list of clauses, and in each clause, we have a list of literals represented as integers. In our implementation, the expression is stored as a `CNF cnf` object, which has a `cnf.expression` which is the 2D `ArrayList`, and also with it, the number of variables `cnf.variables` and the number of clauses `cnf.clauses`.

To perform an assignment, we essentially have an assignment array of type `Boolean[] assignment`, which is of size `cnf.variables`. Every variable gets mapped to an index of the `assignment` array, and we say that if that particular value is `null`, then the variable is unassigned; if it's `true`, the variable is assigned to `true`; if it's `false`, the variable is assigned to `false`. As such, in order to assign a value to a variable, it just takes constant time.

When a SAT solver finishes, it returns a `Result result`, which contains `result.satisfiable` which answers whether the expression is satisfiable, and when it is satisfiable, we can interpret `result.assignment` to be one such assignment that makes the boolean expression evaluate to `true`.

= Findings

The test data are used from #link("https://www.cs.ubc.ca/~hoos/SATLIB/benchm.html")[this repository by the University of British Columbia]. The directory is written in the form of `uf<variable-size>-<clause-size>`, and they're all 3-SAT instances.

As for the Sudoku instances, they were generated from #link("https://qqwing.com/generate.html")[this site].

Brute force could solve `uf20-91` instances, however, could not finish `uf50-218` instances in a reasonable amount of time.

#figure(
  table(
    columns: 3,
    align: (left, right, right),
    table.hline(),
    [Dataset], [DPLL (s)], [Kissat (s)],
    table.hline(),
    [sudoku], [$6.12 times 10^(-2) plus.minus 1.23 times 10^(-2)$], [$3.79 times 10^(-3) plus.minus 6.10 times 10^(-3)$],
    [uf20-91], [$2.75 times 10^(-4) plus.minus 3.63 times 10^(-4)$], [$3.80 times 10^(-3) plus.minus 1.82 times 10^(-3)$],
    [uf50-218], [$4.26 times 10^(-3) plus.minus 3.88 times 10^(-3)$], [$2.11 times 10^(-3) plus.minus 1.79 times 10^(-3)$],
    [uf100-430], [$4.02 times 10^(-1) plus.minus 4.30 times 10^(-1)$], [$2.92 times 10^(-3) plus.minus 4.66 times 10^(-3)$],
    [uf125-538], [$3.01 plus.minus 2.69$], [$5.04 times 10^(-3) plus.minus 6.04 times 10^(-3)$],
    [uf150-645], [$1.85 times 10^(1) plus.minus 2.41 times 10^(1)$], [$3.84 times 10^(-3) plus.minus 1.47 times 10^(-2)$],
    table.hline(),
  ),
  caption: [time trial results written in $mu plus.minus sigma$ seconds],
)<time-trial-results>

DPLL hits a wall at `uf175-753` instances, where it took `11m25.264s` to complete `uf175-01.cnf.`, meaning that it would take approximately `19h` to finish the whole dataset of size 100. Kissat has no problem with that dataset, completing it in $5.42 times 10^(-3) plus.minus 1.48 times 10^(-2)$ seconds. Also, strangely enough, Kissat's time to solve doesn't necessary increase with input size, even though we have a strictly increasing trend with the DPLL algorithm.

Notice that our Sudoku instance contains $729$ variables, and at least $11988$ clauses, however, our DPLL algorithm can handle that relatively quickly. This suggests that whether our program can evaluate a SAT instance quickly not only depends on the number of variables, but also the structure of our SAT instance as well. If our SAT instance have many simplifications, our algorithm would then be able to complete the problem in a reasonable amount of time. In section @sudoku-to-sat, we mentioned that the constraint that at most one digit must be present in every row, column, and sub-square is redundant because it has been implied by the _at least one_ constraint. Regardless, if we disregard that constraint, Kissat would still be able to solve it, however, our DPLL algorithm could no longer solve it in a reasonable amount of time because the structure wasn't well suited to the simplifications that DPLL makes.

This means that the DPLL algorithm is an improvement over the brute force approach, as it allows us to solve bigger problems that brute force search could not handle, such as the $9 times 9$ sudoku grid, or even to figure out whether the provincial map of Cambodia is 3-colourable.

Cambodia has 25 provinces, and so, to try to color that map using 3 colors, we would need $25 times 3 = 75$ boolean variables. This cannot be done reasonably using bruteforce search. However, with DPLL, the program terminates in a reasonable amount of time (around $0.018$ seconds). Given that the graph of Cambodia (seen in `dataset/cambodia.txt`) is correct, the SAT problem is unsatisfiable, thus proving that the map of Cambodia is not 3-colourable.

= Conclusion and Limitations

The DPLL algorithm is a big step up from brute force search, however, when compared against a state-of-the-art solver such as Kissat, we still see that there are limitations, in that the structure has to be well suited for DPLL (as seen in the redundant constraints needed for Sudoku); whereas a more modern solver like Kissat can handle more flexible SAT structures (it doesn't need the redundant constraints for Sudoku). Moreover, Kissat can tackle bigger SAT instances of many more variables, while DPLL starts to struggle after hundreds of variables. Kissat implements a more general technique for SAT solving called conflict-driven clause learning (CDCL), which is the successor of DPLL @kissat. As such, practically, there are still many more techniques one can apply to create better SAT solvers. And because SAT solvers can act as a generic black box solver for many different problems, improving a SAT solver's efficiency has a wide ranging impact those problems; creating incentive to further improve SAT solvers in the future.
