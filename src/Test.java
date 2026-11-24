import java.io.File;

class Test{
  public static void main(String[] args) {
    String baseDir = "dataset/uf20-91";
    File dir = new File(baseDir);
    File[] files = dir.listFiles();
    if (files == null){
      return;
    }
    for (File file: files){
      CNF cnf = DIMACS.parseFile(file.toString());
      System.out.println(cnf);
      Result dpllResult;
      Result bruteForceResult;
      bruteForceResult = BruteForce.sat(cnf);
      dpllResult  = DPLL.sat(cnf);
      System.out.printf("Testing %s\r\n", file);
      // so first check that the assignment result is true
      assert dpllResult.satisfiable == bruteForceResult.satisfiable : String.format("On %s, brute force got %s but dpll got %s.\r\n",
        file,
        bruteForceResult,
        dpllResult);
      if (bruteForceResult.satisfiable){
        // next, actually evauluate it and see whether the assignment actually satisifies the expression
        boolean dpllEvaluation = cnf.expression.stream().allMatch(
          clause -> clause.stream().anyMatch(literal -> DPLL.evaluateLiteral(literal, dpllResult.assignment)));
        assert dpllEvaluation == bruteForceResult.satisfiable: String.format("On %s, the assignment %s doesn't satisify the expression, even though the result outputted as satisifiable\r\n",
        file,
        bruteForceResult.assignment
        );
      }
      System.out.printf("brute force result: %s\r\n", dpllResult);
      System.out.printf("DPLL result: %s\r\n", bruteForceResult);
      System.out.println();
    }
  }
}
