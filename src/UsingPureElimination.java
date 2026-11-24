import java.util.ArrayList;

class UsingPureElimination{
  public static void main(String[] args) {
    CNF cnf = DIMACS.parseFile("dataset/custom/009pureEliminationTest.true.cnf");
    // we expect -1 to be set, and then 3 to be set
    System.out.println(cnf);
    Result result = new Result(cnf.variables);
    ArrayList<Integer> changes = new ArrayList<>();
    Boolean performedPureElimination = DPLL.performPureLiteralElimination(cnf, result.assignment, changes);
    System.out.printf("performed pure elimination: %s\r\n", performedPureElimination);
    System.out.println(result);
    System.out.printf("changes: %s\r\n", changes);
    performedPureElimination = DPLL.performPureLiteralElimination(cnf, result.assignment, changes);
    System.out.printf("performed pure elimination: %s\r\n", performedPureElimination);
    System.out.println(result);
    System.out.printf("changes: %s\r\n", changes);
  }
}
