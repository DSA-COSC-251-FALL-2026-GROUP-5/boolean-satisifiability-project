import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;

class Sudoku{
  // we want to encode sudoku into SAT. Essentially, given a sudoku puzzle (we store it as a one dimensional integer array)
  public static Integer getLiteral(Integer row, Integer column, Integer digit){
    // NOTE: digit is represented 0 to 8
    // given a column, row and digit, we want to get the literal index
    return 9*9*row + 9*column + digit + 1;
  }

  public static Integer getDigit(Integer literal){
    // NOTE: digit is represented 0 to 8 rather than 1 to 9
    return (literal - 1) % 9;
  }

  public static Integer getRow(Integer literal){
    return (literal - 1) / (9*9);
  }

  public static Integer getColumn(Integer literal){
    return (literal - getDigit(literal) - 9*9*getRow(literal) - 1) / 9;
  }

  public static Integer[] satAssignmentToSolution(Result result){
    Integer[] solution = new Integer[81];
    // TODO: fill in the blank (interpret the results)
    for (int n = 0; n < result.assignment.length; n++){
      if (!result.assignment[n]){
        // if we didn't assign it, whatever
        continue;
      }
      // if it did get assigned however,
      solution[9*getRow(n+1) + getColumn(n+1)] = getDigit(n+1);
    }
    return solution;
  }


  public static CNF toSat(Integer[] puzzle){
    // NOTE: puzzle must be 0 to 8 rather than 1 to 9
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

    // now we need to state that for every row, every digit must occupy all columns in the row
    for (int i = 0; i < 9; i++){
      for (int d = 0; d < 9; d++){
        ArrayList<Integer> clause = new ArrayList<>();
        for (int j = 0; j < 9; j++){
          clause.add(getLiteral(i, j, d));
        }
        cnf.expression.add(clause);
      }
    }

    // do the same for all columns
    for (int j = 0; j < 9; j++){
      for (int d = 0; d < 9; d++){
        ArrayList<Integer> clause = new ArrayList<>();

        for (int i = 0; i < 9; i++){
          clause.add(getLiteral(i, j, d));
        }
        cnf.expression.add(clause);
      }
    }

    // we do the same for sub-squares, but first, we must generate the subsquares
    for (int u = 0; u < 3; u++){
      for (int v = 0; v < 3; v++){
        for (int d = 0; d < 9; d++){
          ArrayList<Integer> clause = new ArrayList<>();
          for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
              clause.add(
                getLiteral(3*u+i, 3*v+j, d)
              );
            }
          }
          cnf.expression.add(clause);
        }
      }
    }

    // at most one of each digit in every row
    for (int i = 0; i < 9; i++){
      for (int d = 0; d < 9; d++){
        for (int jr = 0; jr < 9; jr++){
          for (int jl = 0; jl < jr; jl++)
          cnf.expression.add(new ArrayList<>(List.of(
            -getLiteral(i, jl, d),
            -getLiteral(i, jr, d))));
        }
      }
    }

    // at most one of each digit in every row
    for (int j = 0; j < 9; j++){
      for (int d = 0; d < 9; d++){
        for (int ir = 0; ir < 9; ir++){
          for (int il = 0; il < ir; il++)
          cnf.expression.add(new ArrayList<>(List.of(
            -getLiteral(il, j, d),
            -getLiteral(ir, j, d))));
        }
      }
    }

    // at most one of each digit in every 3x3 box
    for (int u = 0; u < 3; u++)  {
      for (int v = 0; v < 3; v++) {
        for (int d = 0; d < 9; d++){
          for (int nr = 0; nr < 9; nr++){
            for (int nl = 0; nl < nr; nl++){
              cnf.expression.add(new ArrayList<>(List.of(
                -getLiteral(3*u + nl/3, 3*v + nl%3, d),
                -getLiteral(3*u + nr/3, 3*v + nr%3, d))));
            }
          }
        }
      }
    }


    for (int n = 0; n < puzzle.length; n++){
      if (puzzle[n] == null){
        continue;
      }
      // 9*9*i + 9*j + d
      // 9*(9*i + j) + d
      // 9*(n) + d
      // if it isn't null, encode that it must be assigned
      cnf.expression.add(new ArrayList<>(List.of(9*n + puzzle[n] + 1)));
    }

    cnf.clauses = cnf.expression.size();
    return cnf;
  }

  public static Integer[] puzzleStringToInteger(String stringPuzzle){
    // NOTE: stringPuzzle is has digtis 1 to 9, while what we return will be 0 to 8
    // NOTE: not going to deal with error checking for werid use cases
    Integer[] puzzle = new Integer[stringPuzzle.length()];
    for (int i = 0; i < stringPuzzle.length(); i++){
      Character cell = stringPuzzle.charAt(i);
      if (cell.equals('.')){
        puzzle[i] = null;
      }
      else{
        // assume that what we have is a digit
        puzzle[i] = Character.getNumericValue(cell) - 1;
      }
    }
    return puzzle;
  }

  public static String puzzleIntegerToString(Integer[] puzzle){
    String puzzleString = "";
    for (int i = 0; i < puzzle.length;i++){
      puzzleString = puzzleString.concat(((Integer)(puzzle[i]+ 1)).toString());
    }
    return puzzleString;
  }

  public static void main(String[] args) {
    String puzzleAsString = ".......6.4.18.35..97.5.4.1..9....1...857......1.4.57.....1...........358..3..7...";
    System.out.println(puzzleAsString);
    Integer[] puzzle = puzzleStringToInteger(puzzleAsString);
    CNF sudokuAsSat = Sudoku.toSat(puzzle);
    String sudokuAsDIMACS = DIMACS.toDIMACS(sudokuAsSat);
    String path = "dataset/sudoku/sudoku002.cnf";
    try{
      Files.writeString(Paths.get(path),
        sudokuAsDIMACS
      );
    }
    catch (IOException e){
      e.printStackTrace();
    }
    //Result result = KISSAT.solve(path);
    Result result = DPLL.sat(DIMACS.parseFile(path));
    System.out.println(result);
    Integer[] solvedSudoku = Sudoku.satAssignmentToSolution(result);
    //System.out.println(Arrays.toString(solvedSudoku));
    System.out.println(puzzleIntegerToString(solvedSudoku));
  }
}
