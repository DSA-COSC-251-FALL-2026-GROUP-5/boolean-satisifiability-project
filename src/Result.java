import java.util.Arrays;

class Result{
  // this class represents what the solver should output. First, whether it's satisifiable,
  // and second, the truth assingments as a bitset to make it satisifiable (if it's satisifiable)
  Boolean satisfiable;
  Boolean[] assignment;

  Result(){
  }

  Result(Integer variables){
    assignment = new Boolean[variables];
  }

  @Override
  public String toString(){
    return "satisifiable: %b\r\nassignment: %s".formatted(
      satisfiable,
      Arrays.toString(assignment)
    );
  }
}
