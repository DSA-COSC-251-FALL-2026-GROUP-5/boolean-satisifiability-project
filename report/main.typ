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
$

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

=== K Coloring

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

=== Sudoku

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

Essentially, we can treat $bold(x)$ as a binary number, and we just increment that number by one until we reach $1111...1111_2$.

== DPLL

First of all, DPLL requires that we represent our boolean function in conjunctive normal form (CNF). If the conjunction operator is analogous to multiplication operator, and the disjunction operator is analogous to the addition operator, then the CNF is analogous to the product of sums form.

An example of a CNF formula:

$
(a or b or c) and (c or d or not e) and (f or g or h)
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

Which is capable of removing entire branches, thus, reducing our search space significantly. 

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

• How the algorithm works.
• Important steps or operations.
• Time complexity analysis.
• Advantages and limitations.
• Whether the solution is exact, approximate, or heuristic.

= Implementation

Describe how your group implemented the algorithm.
Include:

• Programming language used.
• Important components of the program.
• Data structures used.
• How to run and use the program.

= Findings

Present the results of your implementation and testing.
Include:
• Test cases and results.
• Execution time for different input sizes.
• Observations about algorithm performance.
• Solution quality, where applicable.
• Limitations and challenges encountered.
• Conclusions from your findings.
You may include diagrams, pseudocode, screenshots, tables, graphs, or other supporting materi-
als.
All external sources, algorithms, diagrams, and references must be properly cited.

#figure(
  table(
    align: (left,)*2 + (right,),
    columns: 3,
    table.hline(),
    table.cell(colspan: 2, align(center)[Item]),[],
    table.hline(end:2),
    [Animal]    , [Description] , [Weight (#unit("g"))] ,
    [Gnat     ] , [per gram]    , [#qty("13.65", "g")]      ,
    [         ] , [each    ]    , [#qty("0.01" , "g")]       , 
    [Gnu      ] , [stuffed ]    , [#qty("92.50", "g")]      ,
    [Emu      ] , [stuffed ]    , [#qty("33.33", "g")]      ,
    [Armadillo] , [frozen  ]    , [#qty("8.99" , "g")]       ,
    table.hline(),
  ),
  caption: [Results of measuring different types of frozen perishables.
    The description tells how each item is prepared.]
)<tab:data>
