# Usage

TODO: do write the usage

## Building the program

We can build the program by using the `build` executable.

```
./build
```

## Running the main program

To run the Main program, we can choose either to run using DPLL or bruteforce

Running using DPLL:

```
./run Main dpll ./dataset/uf50-218/uf50-01.cnf
```

Running using bf:

```
./run Main bf ./dataset/uf20-91/uf20-01.cnf
```

# Brute force approach

We naively try out every single possible assignment for the boolean expression. Therefore, if we have $n$ variables, we would have to search in the worst case for $2^n$ possible combinations.

This approach works fine for the `uf-20-91` dataset (20 variables, thus, $2^20 = 1048576$ total assignments), which finishes in a finite amount of time (usually less than a second).

For instance, for the `uf50-01.cnf` instance, the result is as such:

```
❯ time ./run Main dataset/uf20-91/uf20-01.cnf 
Expression: [[4, -18, 19], [3, 18, -5], [-5, -8, -15], [-20, 7, -16], [10, -13, -7], [-12, -9, 17], [17, 19, 5], [-16, 9, 15], [11, -5, -14], [18, -10, 13], [-3, 11, 12], [
-6, -17, -8], [-18, 14, 1], [-19, -15, 10], [12, 18, -19], [-8, 4, 7], [-8, -9, 4], [7, 17, -15], [12, -7, -14], [-10, -11, 8], [2, -15, -11], [9, 6, 1], [-11, 20, -17], [9
, -15, 13], [12, -7, -17], [-18, -2, 20], [20, 12, 4], [19, 11, 14], [-16, 18, -4], [-1, -17, -19], [-13, 15, 10], [-12, -14, -13], [12, -14, -7], [-7, 16, 10], [6, 10, 7],
 [20, 14, -16], [-19, 17, 11], [-7, 1, -20], [-5, 12, 15], [-4, -9, -13], [12, -11, -7], [-5, 19, -8], [1, 16, 17], [20, -14, -15], [13, -4, 10], [14, 7, 10], [-5, 9, 20], 
[10, 1, -19], [-16, -15, -1], [16, 3, -11], [-15, -10, 4], [4, -15, -3], [-10, -16, 11], [-8, 12, -5], [14, -6, 12], [1, 6, 11], [-13, -5, -1], [-7, -2, 12], [1, -20, 19], 
[-2, -13, -8], [15, 18, 4], [-11, 14, 9], [-6, -15, -2], [5, -12, -15], [-6, 17, 5], [-13, 5, -19], [20, -1, 14], [9, -17, 15], [-5, 19, -18], [-12, 8, -10], [-18, 14, -4],
 [15, -9, 13], [9, -5, -1], [10, -19, -14], [20, 9, 4], [-9, -2, 19], [-5, 13, -17], [2, -10, -18], [-18, 3, 11], [7, -9, 17], [-15, -6, -3], [-2, 3, -13], [12, 3, -2], [-2
, -3, 17], [20, -15, -16], [-5, -17, -19], [-20, -18, 11], [-9, 1, -5], [-19, 9, 17], [12, -2, 17], [4, -16, -5]]
variables: 20
clauses: 91
satisifiable: true
assignment: [true, false, false, false, false, true, false, false, true, false, false, false, false, true, true, false, true, false, false, true]

real    0m0.608s
user    0m0.426s
sys     0m0.075s
```

With a modern sat solver like `kissat` however, it over 100 times faster:

```
real    0m0.002s
user    0m0.001s
sys     0m0.001s
```

Now, the barrier is 50 variables. A bruteforce approach would require checking $2^50 = 1125899906842624$ different combinations, which on average, cannot be finished in a reasonable amount of time.

Just for fun:

```
real    64m53.084s
user    64m14.261s
sys     0m25.699s
```

and on going... (program hasn't terminated at that point)

However, with a state-of-the-art solver like `kissat`, it can still finish it relatively quickly:

```
real    0m0.004s
user    0m0.002s
sys     0m0.002s
```

Thus, if we perform some optimizations, we should be able to solve significantly bigger SAT instances.

# DPLL

In a nutshell:

1. Perform unit propagation.

2. Perform pure literal elimination.

3. If an empty clause exists, return UNSAT.

4. If no clauses remain, return SAT.

5. Choose an unassigned variable.

6. Branch and recursively solve.

So here's what we have to figure out:

- when we perform eliminations, do we create another expression? or do we do some kind of internal representation?
- if we do an internal representation, we might loose track of what we have eliminated, thus performing redundant computation
- the easiest way is to create another expression, however, if we're doing some kind of recursion and have to backtrack, this is going to consume a lot of memory

- an internal representation can be a bit mask of what to consider, and I guess to backtrack we just keep a history of our eliminations before we make the recursive call


# TODO

- [x] write the DIMACS parser
- [x] build some utils

## Data structure

- Expression is stored as a 2d array list
- Assignment is one array list
