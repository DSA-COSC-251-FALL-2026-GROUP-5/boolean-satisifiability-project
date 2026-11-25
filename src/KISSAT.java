import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;

class KISSAT{
  public static Result solve(String path) {
    String output = "";
    try{
      Process process = new ProcessBuilder(
        "kissat",
        "-q",
        path
      )
      .redirectErrorStream(true)
      .start();
      output = new String(process.getInputStream().readAllBytes());
        }
    catch (IOException e){
      e.printStackTrace();
    }
    ArrayList<Boolean> assignment = new ArrayList<>();
    Result result = new Result();
    String[] lines = output.trim().split("\\n");
    for(String line:lines){
      // first split for literals
      String[] literals = line.trim().split("\\s+");

      if (literals.length == 0){
        continue;
      }

      if (literals[0].equals("s")){
        if (literals[1].equals("UNSATISFIABLE")){
          result.satisfiable = false;
          break;
        }
        if (literals[1].equals("SATISFIABLE")){
          result.satisfiable = true;
          continue;
        }
      }

      if (literals[0].equals("v")){
        for (int i = 1; i < literals.length; i++){
          int literal = Integer.parseInt(literals[i]);
          if (literal == 0){
            break;
          }
          assignment.add(Util.signToBoolean.get(Integer.signum(literal)));
        }
      }
    }
    result.assignment = assignment.toArray(Boolean[]::new);
    return result;
  }

  public static void main(String[] args) {
    String path = "dataset/uf20-91/uf20-01.cnf";
    System.out.println(solve(path));
  }
}
