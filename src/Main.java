import java.util.Arrays;

class Main{
  public static void main(String[] args) {
    CNF cnf = DIMACS.parseFile("dataset/007and.true.cnf");
    System.out.println(cnf);
    Result result = BruteForce.sat(cnf);
    System.out.println(result);
  } }
