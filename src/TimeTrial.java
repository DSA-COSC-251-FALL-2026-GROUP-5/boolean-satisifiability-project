import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;

class TimeTrial{
  public static void main(String[] args) {
    if (args.length <= 1){
      System.out.println("Please provide a directory path as the first argument, and the path to results file as the next argument");
      return;
    }

    File dir = new File(args[0]);
    File[] files = dir.listFiles();
    for (int i = 0; i < files.length; i++){
      //CNF cnf = DIMACS.parseFile(files[i].toString());
      long startTime = System.nanoTime();
      //DPLL.sat(cnf);
      KISSAT.solve(files[i].toString());
      long endTime = System.nanoTime();
      try{
        System.out.println(files[i].toString());
        Files.writeString(Path.of(args[1]),
          String.format("%s, %s\n",
            files[i].toString(),
            endTime - startTime),
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
        );
      }

      catch(IOException e){
        e.printStackTrace();
      }
    }
  }
}
