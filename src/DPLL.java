import java.util.ArrayList;
import java.util.Arrays;

class DPLL {

  public static Boolean performUnitPropagation(CNF cnf, Boolean[] assignment, ArrayList<Integer> changes){
    Boolean foundUnitLiteral = false;
    for (ArrayList<Integer> clause : cnf.expression) {
      // for every clause, we then loop to try and find a unit literal
      Integer unassignedCount = 0;
      Integer unassignedLiteral = null;
      Boolean isClauseSatisifed = false;
      for (Integer literal : clause) {
        // we need to find clauses that:
        // - doesn't have an assigned true (the clause would be satisified in that case)
        // - have one unassigned value and the rest are false
        Integer sign = Integer.signum(literal);
        if (assignment[sign * literal - 1] == null) {
          unassignedCount++;
          unassignedLiteral = literal;
          continue;
        }

        // now we know that what we have is assigned
        if (!(Util.signToBoolean.get(sign) ^ assignment[sign * literal - 1])) {
          // 0, 0 -> 1; 0, 1 -> 0; 1, 1, 0-> 0; 1, 1 -> 1
          // if whatever is inside evaluates to true, then the whole clause is true, so we
          // raise the satisifed flag and skip the rest
          isClauseSatisifed = true;
          break;
        }
        // now if it evauluates to false on the otherhand, do nothing
      }

      if (!isClauseSatisifed && unassignedCount == 1) {
        // if we have found exactly one unassignedCount
        foundUnitLiteral = true;
        // now if we have found a unit literal, we must set it to a variable such that
        // it's true
        Integer sign = Integer.signum(unassignedLiteral);
        assignment[sign * unassignedLiteral - 1] = Util.signToBoolean.get(sign);
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
    return foundUnitLiteral;
  }

  public static Boolean performPureLiteralElimination(CNF cnf, Boolean[] assignment, ArrayList<Integer> changes){
    Boolean hasPureLiteral = false;
    // so pureLiterals will contain the following:
    // -1 if it's a pure literal of negative sign
    // 1 if it's a pure literal of positive sign
    // 0 if it's not a pure literal
    // null if we haven't found it
    //
    // Also, take note that we must skip the clauses that are already satisified
    Integer[] previousPureLiterals = new Integer[cnf.variables];
    Integer[] pureLiterals = new Integer[cnf.variables];

    for (ArrayList<Integer> clause : cnf.expression) {
      System.arraycopy(pureLiterals, 0, previousPureLiterals, 0, cnf.variables);
      for (Integer literal : clause) {
        Integer sign = Integer.signum(literal);
        // so what we consider as a pure literal is that:
        // - the literal is unassigned
        // however, if the literal is assigned, and it makes teh clause satisifiable, then we reset the pureLiteral state to that when we haven't seen the clause
        // if it's assigned to false though, just continue
        if (assignment[sign*literal - 1] != null){
          if (!(Util.signToBoolean.get(sign) ^ assignment[sign * literal - 1])){
            // remember that a xor b answers the question "is the value of a different than the value of b"?
            // now if they're the same, then our entire clause is satisified, so we reset the pureLiteral to what we had
            System.arraycopy(previousPureLiterals, 0, pureLiterals, 0, cnf.variables);
            break;
          }
          continue;
        }
        // now we know that our literal hasn't been assigned yet
        if (pureLiterals[sign * literal - 1] == null){
          // if this is the first time we have encountered it, store its sign
          pureLiterals[sign * literal - 1] = sign;
          continue;
        };

        if (pureLiterals[sign*literal - 1] == -1 && sign == -1){
          // if it's of negative sign and it's still of negative sign, we're ok
          continue; 
        }

        if (pureLiterals[sign*literal - 1] == 1 && sign == 1){
          // same logic applies for postive sign
          continue; 
        }

        // now otherwise, its either:
        // - the stored sign was -1 but we got 1
        // - the stored sign was 1 but we got -1
        // - the stored sign was 0
        // in all cases, we can store 0
        pureLiterals[sign*literal - 1] = 0;
      }
    }
    // after we've have done this, we should now get an answer to the question "is this literal a pure literal?" 
    // in the form of the pureLiterals array

    for (int i = 0; i < pureLiterals.length; i++){
      if (pureLiterals[i] == null){
        continue;
      }

      if (pureLiterals[i] == -1){
        // if it contains pure literals of only negation in front, set it to false
        assignment[i] = false;
        changes.add(i + 1);
        hasPureLiteral = true;
      }
      else if(pureLiterals[i] == 1){
        // same logic applies for the true assignment
        assignment[i] = true;
        changes.add(i + 1);
        hasPureLiteral = true;
      }
      // otherwise, do nothing
    }
    // NOTE: it seems like if we encountered 2 pure literals in the same search, we would eliminate them in just this one call
    return hasPureLiteral;
  }

  public static ArrayList<Integer> simplify(CNF cnf, Boolean[] assignment) {
    ArrayList<Integer> changes = new ArrayList<Integer>();
    Boolean performedUnitPropagation;
    Boolean performedPureElimination;
    do {
      performedUnitPropagation = false;
      performedPureElimination = false;
      // first, we do try to find a new unitLiteral
      performedUnitPropagation = performUnitPropagation(cnf, assignment, changes);
      // now we do pure elimination
      //performedPureElimination = performPureLiteralElimination(cnf, assignment, changes);
    } while (performedUnitPropagation || performedPureElimination);
    return changes;
  }



  public static Integer pick(CNF cnf, Boolean[] assignment){
    // returns the next literal to assign (follows 1st index as in DIMACS CNF)
    // if we return null, then it's all assigned
    // right now, just pick the first unassigned one we see
    /*
    for (int i = 0; i < assignment.length; i++){
      if (assignment[i] == null){
        return i + 1;
      }
    }
    return null;
    */
    
    // we will now use the shortest clause heuristic
    ArrayList<Integer> shortestClause = null;
    Integer minUnassignedInClause = null;
    Integer lastNullLiteral = null;
    // we will return the lastNullLiteral if we do find an unsatisified clause with null assignments

    for (ArrayList<Integer> clause: cnf.expression){
      Boolean satisified = false;
      Integer unassignedInClause = 0;
      Integer localLastNullLiteral = null;
      for (Integer literal: clause){
        Integer sign = Integer.signum(literal);

        if (assignment[sign*literal - 1] == null){
          localLastNullLiteral = sign*literal;
          unassignedInClause++;
          continue;
        }

        if (Util.evaluateLiteral(literal, assignment)){
          // no need to evaulate further if it's already satisified
          satisified = true;
          break;
        }
      }

      if (satisified){
        continue;
      }

      // if it isn't satisifed, and the clause has a smaller size, then store it
      if (shortestClause == null || unassignedInClause < minUnassignedInClause){
        shortestClause = clause;
        minUnassignedInClause = unassignedInClause;
        lastNullLiteral = localLastNullLiteral;
      }
    }

    if (lastNullLiteral == null) {
      // if you can't assign anything, it might be that it's all satisified, however, to make this work with this architecture
      // we want to return null only if we actually can't assign anymore stuff
      // so from here, we just assign the first null we see
      for (int i = 0; i < assignment.length; i++) {
        if (assignment[i] == null) {
          return i + 1;
        }
      }
      return null;
    }
    return lastNullLiteral;
  }

  public static void undo(Boolean[] assignment, ArrayList<Integer> changes){
    for (Integer changedLiteral : changes){
      assignment[changedLiteral - 1] = null;
    }
  }

  public static boolean dpll(CNF cnf, Boolean[] assignment) {
    // now first of all, we simplify
    ArrayList<Integer> changes = simplify(cnf, assignment);
    System.out.printf("simplified: %s\r\n", Arrays.toString(assignment));

    // detect contradictions early and satisifiability early
    for (ArrayList<Integer> clause: cnf.expression){
      Boolean allFalse = true;
      for (Integer literal: clause){
        Integer sign = Integer.signum(literal);
        if (assignment[sign*literal - 1] == null || Util.evaluateLiteral(literal, assignment)){
          allFalse = false;
          break;
        }
      }
      if (allFalse){
        // if we're here, then it's all false, thus, the entire branch will be unsatisifiable
        undo(assignment, changes);
        return false;
      }
    }



    Integer chosenLiteral = pick(cnf, assignment);
    if (chosenLiteral == null){
      // if we can't choose a literal, then either it's because the formula is satisfied, or because 
      // if we have assigned all values, then simply evaluate it
      boolean assignmentResult = cnf.expression.stream().allMatch(
        clause -> clause.stream().anyMatch(literal -> Util.evaluateLiteral(literal, assignment)));

      if (assignmentResult){
        // if it's true, then we have found a satisifying assignment
        return true; 
      }

      // if we got an unsatisifiable assignment, then we have reached a deadend, and we must undo our changes and return false
      undo(assignment, changes); 
      return false;
    }

    // TODO: add an end early condition when all clauses are satisified, or make sure that we don't perform any really redundant stuff (like, after all clauses are satisfied even though there are still unassigned variables, we hope that even though we still need to assign everything, we immediately terminate on the first branch)
    
    changes.add(chosenLiteral);
    // now if we still have some unassigned variables left, then we take the chosen literal, assign it a value and see what happens
    assignment[chosenLiteral - 1] = true;
    Boolean leftResult = dpll(cnf, assignment);
    if (leftResult){
      // if it's satisifed, then return true
      return true;
    }
    // if its false, try again but with a true assignment instead
    assignment[chosenLiteral - 1] = false;
    Boolean rightResult = dpll(cnf, assignment);
    if (rightResult){
      return true;
    }
    // now if both branches are unsatisifiable, then the main branch is unsatisifiable
    // NOTE: notice that when its unsatisifiable, we undo everything so that when the recusrive call returns, the assignment array would look unchanged
    // however, when we do get a satisifying assignment, we don't undo, thus, the assignment array will contain the assignment that makes the formula satisifiable
    undo(assignment, changes);
    return false;
  }

  public static Result sat(CNF cnf) {
    Result result = new Result();
    result.assignment = new Boolean[cnf.variables];
    // now result.assignment is filled with null
    result.satisfiable = dpll(cnf, result.assignment);
    return result;
  }
}
