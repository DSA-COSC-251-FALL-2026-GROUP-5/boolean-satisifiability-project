import java.util.ArrayList;
import java.util.List;

class Sudoku{
  // we want to encode sudoku into SAT. Essentially, given a sudoku puzzle (we store it as a one dimensional integer array)
  Integer getLiteral(Integer row, Integer column, Integer digit){
    // given a column, row and digit, we want to get the literal index
    return 9*9*row + 9*column + digit + 1;
  }

  Integer getDigit(Integer literal){
    // NOTE: digit is represented 0 to 8 rather than 1 to 9
    return (literal - 1) % 9;
  }

  Integer getRow(Integer literal){
    return (literal - 1) / (9*9);
  }

  Integer getColumn(Integer literal){
    return (literal - getDigit(literal) - 9*9*getRow(literal) - 1) / 9;
  }

  Integer[] satAssignmenttoSolution(Result result){
    Integer[] solution = new Integer[81];
    // TODO: fill in the blank (interpret the results)
    return solution;
  }


  CNF toSat(Integer[] puzzle){
    // notice that if the array is stored as left to right, top to bottom, then
    // if n is the index of the array
    // then column j and row i can be accessed by doing n = 9*i + j
    // at each column j and row i, we have 9 possible values for each cell, and we must encode them as boolean variables
    // thus, we have a SAT problem with 9*9*9 = 729 variables
   
    // let's say that n is the index of the literal in the boolean expression
    // and if we have column j, row i, and digit d
    // we now store it as such: 9*9*i + 9*j + d
    //
    // so that we have 0, 1, 2,..., 8 to be the possible values in column 0, row 0
    // then we have 9, 10, 11,..., 17 to be the possible values in column 1, row 0
    // etc.
    //
    // if we have n, then we can recover d by doing, or d = n % 9
    // i can be determined by doing i = Math.floor(n / (9*9))
    // and j can then be determined by doing j = (n - 9*9*i - d)/9
    //
    // however, since DIMACS is 1st index, we add 1
    //
    // So:
    // n = 9*9*i + 9*j + d + 1
    //
    // and so d = (n - 1) % 9
    // ...
   
    if (puzzle.length != 81){
      throw new IllegalArgumentException("We only accept 9*9 sudoku puzzles for now");
    }

    CNF cnf = new CNF();
    cnf.variables = 9*9*9;

    // first, we must specify that each cell must be assigned a unique literal
    // this means that for each cell:
    // - there must be at least one assignment
    // - there must be at most one assignment

    // for at least one assignment:
    for (int i = 0; i < 9; i++){
      for (int j = 0; j < 9; j++){
        ArrayList<Integer> clause = new ArrayList<>();
        for (int d = 0; d < 9; d++){
          clause.add(getLiteral(i, j, d));
        }
        cnf.expression.add(clause);
      }
    }

    // for at most one assignment:
    for (int i = 0; i < 9; i++){
      for (int j = 0; j < 9; j++){
        // from here, what we want is that for every pair of digits, we never want 2 of them to be assigned at the same time, that is:
        // dl | dr | wanted value
        // T | T | False
        // T | F | True
        // F | T | True
        // F | F | True
        // so this is dl nand dr <=> not dl or not dr
        for (int dr = 0; dr < 9; dr++){
          for (int dl = 0; dl < dr; dl++){
            //clause.add(9*9*i + 9*j + d + 1);
            cnf.expression.add(new ArrayList<>(List.of(
              -getLiteral(i,j,dl),
              -getLiteral(i,j,dr)
            )));
          }
        }
      }
    }

    // now we need to state that for every row, there needs to be only one digit occupying it
    for (int n = 0; n < puzzle.length; n++){
      for (int i = 0; i < 9; i++){
        // for every row
        // for each pair of columns in the row
        for (int jr = 0; jr < 9; jr++){
          for (int jl = 0; jl < 9; jl++){
            // for each digit
            for (int d = 0; d < 9; d++){
              // we never want the same digit of the same column to have 2 assignments
              cnf.expression.add(new ArrayList<>(List.of(
                -getLiteral(i, jl, d),
                -getLiteral(i, jr, d)
              )));
            }
          }
        }
      }
    }

    // we do the same for columns
    for (int n = 0; n < puzzle.length; n++){
      for (int j = 0; j < 9; j++){
        // for every row
        // for each pair of columns in the row
        for (int ir = 0; ir < 9; ir++){
          for (int il = 0; il < 9; il++){
            // for each digit
            for (int d = 0; d < 9; d++){
              // we never want the same digit of the same column to have 2 assignments
              cnf.expression.add(new ArrayList<>(List.of(
                -getLiteral(il, j, d),
                -getLiteral(ir, j, d)
              )));
            }
          }
        }
      }
    }

    // we do the same for sub-squares, but first, we must generate the subsquares
    for (int u = 0; u < 3; u++){
      for (int v = 0; v < 3; v++){
        for (int nr = 0; nr< 9; nr++){
          for (int nl = 0; nl < nr; nl++){
            for (int d = 0; d < 9; d++){
              cnf.expression.add(new ArrayList<>(List.of(
                -getLiteral(3*u+(nl/9), 3*v+(nl%9), d),
                -getLiteral(3*u+(nr/9), 3*v+(nr%9), d)
              )));
            }
          }
        }
      }
    }

    // TODO: now encode the non-null stuff as extra constraints
    for (int n = 0; n < puzzle.length; n++){
      if (puzzle[n] == null){
        continue;
      }
      // if it isn't null, encode that it must be assigned
      cnf.expression.add(new ArrayList<>(List.of(n+1)));
    }
    return cnf;
  }
}
