class Main{
  public static void main(String[] args) {
    CNF cnf = DIMACS.parseFile("dataset/001xor.cnf");
    System.out.println(cnf);
  }
}
