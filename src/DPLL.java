import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class DPLL {
  public static HashMap<Integer, Boolean> signToBoolean = new HashMap<Integer, Boolean>(Map.ofEntries(
      Map.entry(-1, false),
      Map.entry(1, true)));

  public static ArrayList<Integer> simplify(CNF cnf, Boolean[] assignment) {
    ArrayList<Integer> changes = new ArrayList<Integer>();
    Boolean foundUnitLiteral;
    Boolean performedPureElimination;

    do {
      foundUnitLiteral = false;
      performedPureElimination = false;
      // first, we do try to find a new unitLiteral

      for (ArrayList<Integer> clause : cnf.expression) {
        // for every clause, we then loop to try and find a unit literal
        Integer unassignedCount = 0;
        Integer unassignedLiteral = null;
        for (Integer literal : clause) {
          Integer sign = Integer.signum(literal);
          if (assignment[sign * literal - 1] == null) {
            unassignedCount++;
            unassignedLiteral = literal;
            continue;
          }

          // now we know that what we have is assigned
          if (signToBoolean.get(sign) ^ assignment[sign * literal - 1]) {
            // 0, 0 -> 0; 0, 1 -> 1; 1, 1, 0-> 1; 1, 1 -> 0
            // if whatever is inside evaluates to true, then the whole clause is true, so we
            // skip
            break;
          }
          // now if it evauluates to false on the otherhand, do nothing
        }
        if (unassignedCount == 1) {
          // if we have found exactly one unassignedCount
          foundUnitLiteral = true;
          // now if we have found a unit literal, we must set it to a variable such that
          // it's true
          Integer sign = Integer.signum(unassignedLiteral);
          assignment[sign * unassignedLiteral - 1] = signToBoolean.get(sign);
          // changes will always indicate that we assigned a particular value to a
          // previously unassigned variable
          // this is fine because, for changes in dpll, we can undo what we assigned,
          // however, the simplify method will probably
          // change a bunch of stuff, so to undo, we just loop through it and set it back
          // to "unassigned"
          changes.add(sign * unassignedLiteral);
          break;
        }
      }
      // now we perform pure elimination
      // we need to keep track of whether there exists a literal where its sign
      // doesn't change
      // I think we have no choice but to use a bitset of some kind
      // null if it's unassigned, false if it's if the sign changes, true if the sign
      // doesn't change
      // also, i think we have no choice but to loop through the entire expression,
      // and then search for a "true" in our array (for every no sign change, we set
      // it such that we maintain possible satisifiability in that branch)

      // so pureLiterals will contain the following:
      // -1 if it's a pure literal of negative sign
      // 1 if it's a pure literal of positive sign
      // 0 if it's not a pure literal
      // null if we haven't found it
      //
      // Also, take note that we must skip the clauses that are already satisified
      Integer[] pureLiterals = new Integer[cnf.variables];

      for (ArrayList<Integer> clause : cnf.expression) {
        // for every clause, we then loop to try and find a unit literal
        for (Integer literal : clause) {
          // TODO: fill in the blank
        }
      }

      // suppose that we have assigned everything.
      // there won't be any unit literals
      // moreover, there won't be any pure elimination either
    } while (foundUnitLiteral || performedPureElimination);

    return changes;
  }

  public static boolean dpll(CNF cnf, Boolean[] assignment) {
    // now first of all, we simplify

    return true;
  }

  public static Result sat(CNF cnf) {
    Result result = new Result();
    result.assignment = new Boolean[cnf.variables];
    // now result.assignment is filled with null
    result.satisfiable = dpll(cnf, result.assignment);
    return result;
  }
}
