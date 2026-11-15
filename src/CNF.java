import java.util.ArrayList;

class CNF{
  public Integer variables;
  public Integer clauses;
  public ArrayList<ArrayList<Integer>> expression;
  CNF(){
    expression = new ArrayList<ArrayList<Integer>>();
  }

  @Override
  public String toString(){
    String returnString = "Expression: %s\r\n".formatted(expression.toString());

    returnString = returnString.concat("variables: %d\r\n".formatted(
      variables
    ));

    returnString = returnString.concat("clauses: %d".formatted(
      clauses
    ));

    return returnString;
  }
}
