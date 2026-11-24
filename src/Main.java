class Main{
  public static void main(String[] args) {
    if (args.length <= 0){
      System.out.println("Please provide a file path as an argument");
      return;
    }
    CNF cnf = DIMACS.parseFile(args[1]);
    System.out.println(cnf);
    Result result;
    if (args[0].equals("dpll")){
      result  = DPLL.sat(cnf);
    }
    else{
      result  = BruteForce.sat(cnf);
    }
    System.out.println(result);
  }}
