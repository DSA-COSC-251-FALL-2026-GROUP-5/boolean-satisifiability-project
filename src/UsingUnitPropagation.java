import java.util.ArrayList;

class UsingUnitPropagation{
  public static void main(String[] args) {
    if (args.length <= 0){
      System.out.println("Please provide a file path as an argument");
      return;
    }
    CNF cnf = DIMACS.parseFile(args[0]);
    System.out.println(cnf);
    Result result = new Result(cnf.variables);
    ArrayList<Integer> changes = new ArrayList<>();
    Boolean performedUnitPropagation = DPLL.performUnitPropagation(cnf, result.assignment, changes);
    System.out.printf("performed unit propagation: %s\r\n", performedUnitPropagation);
    System.out.println(result);
    System.out.printf("changes: %s\r\n", changes);
    performedUnitPropagation = DPLL.performUnitPropagation(cnf, result.assignment, changes);
    System.out.printf("performed unit propagation: %s\r\n", performedUnitPropagation);
    System.out.println(result);
    System.out.printf("changes: %s\r\n", changes);
    DPLL.undo(result.assignment, changes);
    System.out.printf("performed unit propagation: %s\r\n", performedUnitPropagation);
    System.out.println(result);
    System.out.printf("changes: %s\r\n", changes);

    /*
    performedUnitPropagation = DPLL.performUnitPropagation(cnf, result.assignment, changes);
    System.out.printf("performed unit propagation: %s\r\n", performedUnitPropagation);
    System.out.println(result);
    System.out.printf("changes: %s\r\n", changes);
    */
  }}
