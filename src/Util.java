import java.util.HashMap;
import java.util.Map;

class Util{
  public static HashMap<Integer, Boolean> signToBoolean = new HashMap<Integer, Boolean>(Map.ofEntries(
      Map.entry(-1, false),
      Map.entry(1, true)));

  public static boolean evaluateLiteral(int literal, Boolean[] assignment) {
    if (literal > 0) {
      return assignment[literal - 1];
    }
    return !assignment[-literal - 1];
  }
}
