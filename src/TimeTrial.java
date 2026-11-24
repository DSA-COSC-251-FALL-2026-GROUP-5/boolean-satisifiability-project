import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class TimeTrial{
  public static void main(String[] args) {
    if (args.length <= 1){
      System.out.println("Please provide a directory path as the first argument, and the path to results file as the next argument");
      return;
    }
    File dir = new File(args[0]);
    File[] files = dir.listFiles();
    System.out.println(files);
    String csvText = "";
    for (int i = 0; i < files.length; i++){
      long startTime = System.nanoTime();
      CNF cnf = DIMACS.parseFile(files[i].toString());
      DPLL.sat(cnf);
      long endTime = System.nanoTime();
      csvText = csvText.concat(String.format("%s, %s\n", 
        files[i].toString(), 
        endTime - startTime));
    }
    try{
      Files.writeString(Path.of(args[1]), csvText);
    }
    catch(IOException e){
      e.printStackTrace();
    }
  }
}
