class Main{
  public static void main(String[] args) {
    if (args.length <= 1){
      System.out.println("Please specify whether you want to solve using dpll or bf as the first argument, and provide a .cnf file path as the second argument");
      return;
    }
    CNF cnf = DIMACS.parseFile(args[1]);
    System.out.println(cnf);
    Result result;
    if (args[0].equals("dpll")){
      result  = DPLL.sat(cnf);
    }
    else if (args[0].equals("bf")){
      result  = BruteForce.sat(cnf);
    }
    else{
      System.out.printf("Unknown first argument: %s. Please choose either dpll or bf\r\n", args[0]);
      return;
    }
    System.out.println(result);
  }}
