import java.io.File;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class TimeTrialSudoku{
  public static void main(String[] args) {
    File file = new File("dataset/sudoku.csv");
    try{
      Scanner scanner = new Scanner(file);
      String csvContent = "";
      // remove the first line (because it's a header)
      scanner.nextLine();
      while (scanner.hasNextLine()){
        String line = scanner.nextLine();
        String[] splittedLine = line.split(",");
        String puzzleAsString = splittedLine[0].trim();
        System.out.println(puzzleAsString);
        String trueResultAsString = splittedLine[1].trim();
        Integer[] puzzle = Sudoku.puzzleStringToInteger(puzzleAsString);
        CNF sudokuAsSat = Sudoku.toSat(puzzle);
        String sudokuAsDIMACS = DIMACS.toDIMACS(sudokuAsSat);
        String path = "/tmp/sudoku.cnf";
        Files.writeString(Paths.get(path),
          sudokuAsDIMACS
        );
        long startTime = System.nanoTime();
        Result result = KISSAT.solve(path);
        //Result result = DPLL.sat(sudokuAsSat);
        long endTime = System.nanoTime();
        Integer[] solvedSudoku = Sudoku.satAssignmentToSolution(result);
        String resultAsString = Sudoku.puzzleIntegerToString(solvedSudoku);
        assert trueResultAsString.equals(resultAsString):"Result of sudoku differs from the actual solution";
        csvContent = csvContent.concat(String.format("%s, %s\n",
          puzzleAsString,
          endTime - startTime));
      };
      scanner.close();
      Files.writeString(Path.of("results/sudoku_kissat.csv"),
        csvContent
      );

    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
}
