class Debug{

  public static void main(String[] args) {
    CNF cnf = DIMACS.parseFile("dataset/uf20-91/uf20-03.cnf");
    Boolean []assignment = {false, false, true, true, false, false, true, false, true, true, true, true, true, false, false, false, false, true, false, true};
    boolean assignmentResult = cnf.expression.stream().allMatch(
          clause -> clause.stream().anyMatch(literal -> BruteForce.evaluateLiteral(literal, assignment)));
    System.out.println(assignmentResult);
  }
}
