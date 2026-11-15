import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

class DIMACS{
  public static CNF parseFile(String uri){
    // Given a URI, this function attempts to parse the .cnf file and then returns an expression
    // the exression is a list of list
    CNF cnf = new CNF();
    ArrayList<Integer> currentClause = new ArrayList<>();
    // the problem statement is the `p cnf <variables> <clauses>` line (it isn't required)
    Boolean containsProblemStatement = false;
    int actualVariables = 0;
    int actualClauses = 0;

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

        if (splittedLine[0].equals("%")){
          // we use % to indicate the end of the expression
          break;
        }

        if (splittedLine[0].equals("c")){
          // if the line contains a comment, continue
          continue;
        }

        if (splittedLine[0].equals("p")){
          containsProblemStatement = true;
          // Trust it for now. We'll do a sanitiy check later.
          cnf.variables = Integer.parseInt(splittedLine[2]);
          cnf.clauses = Integer.parseInt(splittedLine[3]);
          continue;
        }

        // now we know splittedLine has to be a clause
        actualClauses++;
        // we loop through the clause
        for (String literal: splittedLine){
          int literalAsInt = Integer.parseInt(literal);
          actualVariables = Math.max(actualVariables, Math.abs(literalAsInt));
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

      if (!containsProblemStatement){
        cnf.clauses = actualClauses;
        cnf.variables = actualVariables;
        return cnf;
      }

      if (cnf.clauses != actualClauses){
        System.out.println("WARNING: the number of clauses specified in the file (%d) is different from the observed number of clauses (%d)".formatted(
          cnf.clauses,
          actualClauses
        ));
        cnf.clauses = actualClauses;
      }

      if (cnf.variables != actualVariables){
        System.out.println("WARNING: the range of variables specified in the file (%d) is different from the observed range of variables (%d)".formatted(
          cnf.variables,
          actualVariables
        ));
        cnf.variables = actualVariables;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return cnf;
  }
}
