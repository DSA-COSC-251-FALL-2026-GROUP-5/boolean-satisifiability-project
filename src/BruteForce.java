import java.util.Arrays;
import java.util.ArrayList;

class BruteForce {
  public static boolean evaluateLiteral(int literal, Boolean[] assignment) {
    if (literal > 0) {
      return assignment[literal - 1];
    }
    return !assignment[-literal - 1];
  }

  public static Boolean[] getNextAssignment(Boolean[] previousAssignment) {
    int i = 0;
    boolean rollover = true;
    Boolean[] nextAssignment = previousAssignment.clone();
    while (rollover) {
      if (i >= nextAssignment.length) {
        break;
      }
      rollover = !(previousAssignment[i] ^ true);
      nextAssignment[i] = !previousAssignment[i];
      i++;
    }
    return nextAssignment;
  }

  public static Result sat(CNF cnf) {
    Result result = new Result();
    // trust that cnf has variables that properly reflect the expression
    result.assignment = new Boolean[cnf.variables];
    // in the brute force method, we count up sequentially, first set all variables
    // to false
    Arrays.fill(result.assignment, false);
    do {
      boolean assignmentResult = cnf.expression.stream().allMatch(
          clause -> clause.stream().anyMatch(literal -> evaluateLiteral(literal, result.assignment)));
      if (assignmentResult) {
        // if for that particular assignment, we got a true, then it's satisfiable and
        // we can end there
        result.satisfiable = true;
        return result;
      }
      result.assignment = getNextAssignment(result.assignment);
      // NOTE: this essentially is just an not any(result.assignment) in Python
      // we stop when it overflows
    } while (!Arrays.stream(result.assignment).allMatch(x -> !x));
    return result;
  }
}
