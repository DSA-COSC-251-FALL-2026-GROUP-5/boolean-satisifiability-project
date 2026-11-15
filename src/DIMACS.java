import java.io.File;
import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

class DIMACS{
  public static CNF parseFile(String uri){
    // Given a URI, this function attempts to parse the .cnf file and then returns an expression
    // the exression is a list of list
    CNF cnf = new CNF();
    ArrayList<Integer> currentClause = new ArrayList<>();

    try {
      Scanner scanner = new Scanner(new File(uri));
      while(scanner.hasNextLine()){
        // every line that begins with a c is a comment, thus, can be discarded
        String line = scanner.nextLine();
        String[] splittedLine = line.trim().split("\\s+");
        if (splittedLine.length == 0){
          // it it's an empty line, continue
          continue;
        }

        if (splittedLine[0].equals("c")){
          // if the line contains a comment, continue
          continue;
        }

        if (splittedLine[0].equals("p")){
          cnf.variables = Integer.parseInt(splittedLine[2]);
          cnf.clauses = Integer.parseInt(splittedLine[3]);
          continue;
        }

        // now we know splittedLine has to be a clause
        // we loop through the clause
        for (String literal: splittedLine){
          int literalAsInt = Integer.parseInt(literal);
          if (literalAsInt == 0){
            // if we get a 0, then that indiates the start of a new clause
            cnf.expression.add(currentClause);
            currentClause = new ArrayList<>();
            continue;
          }
          currentClause.add(literalAsInt);
        }
      }
      scanner.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return cnf;
  }
}
