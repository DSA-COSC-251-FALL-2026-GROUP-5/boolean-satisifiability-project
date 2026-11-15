import java.util.Arrays;

class Main{
  public static void main(String[] args) {
    if (args.length <= 0){
      System.out.println("Please provide a file path as an argument");
      return;
    }
    CNF cnf = DIMACS.parseFile(args[0]);
    System.out.println(cnf);
    Result result = BruteForce.sat(cnf);
    System.out.println(result);
  } }
