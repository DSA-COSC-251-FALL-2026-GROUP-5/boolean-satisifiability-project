import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileNotFoundException;


import java.util.HashMap;

class Graph{
  String[] verticies;
  String[][] edges;
  Graph(String[] verticies, String[][] edges){
    this.verticies = verticies;
    this.edges = edges;
  }

  @Override
  public String toString(){
    return String.format(
      "verticies: %s\r\nedges: %s",
      Arrays.toString(verticies),
      Arrays.deepToString(edges)
    );
  }
}


class KColoring{
  String[] colors;
  Graph graph;
  HashMap<String, Integer> colorNameToIndex;
  HashMap<String, Integer> vertexNameToIndex;

  Integer getLiteral(Integer c, Integer v){
    return colors.length*v + c + 1;
  }

  Integer getColorIndex(Integer literal){
    // NOTE: literal is going to be of positive sign
    return (literal - 1) % colors.length;
  }

  Integer getVertexIndex(Integer literal){
    return (literal - 1)/colors.length;
  }

  KColoring(String[] colors, Graph graph){
    this.colors = colors;
    this.graph = graph;
    this.colorNameToIndex = new HashMap<>();
    this.vertexNameToIndex = new HashMap<>();
    for (int i = 0; i < colors.length; i++){
      this.colorNameToIndex.put(colors[i], i);
    }
    for (int i = 0; i < graph.verticies.length; i++){
      this.vertexNameToIndex.put(graph.verticies[i], i);
    }
  }

  public CNF toSat(){
    CNF cnf = new CNF();
    // our constrains are as such:
    // - every vertex must be assigned at least one color
    // - every vertex must be assigned at most one color
    // - for every edge (pair of verticies), they must never be assigned the same color
    cnf.variables = colors.length*graph.verticies.length;

    // at least one color per vertex
    for (int v = 0; v < graph.verticies.length; v++){
      ArrayList<Integer> clause = new ArrayList<>();
      for(int c = 0; c < colors.length; c++){
        clause.add(getLiteral(c, v));
      }
      cnf.expression.add(clause);
    } 

    // at most one color per vertex
    for (int v = 0; v < graph.verticies.length; v++){
      for(int cr = 0; cr < colors.length; cr++){
        for (int cl = 0; cl < cr; cl++){
          cnf.expression.add(new ArrayList<>(List.of(
            -getLiteral(cl, v),
            -getLiteral(cr, v)
          )));
        }
      }
    }

    // for every edge, discard the case where for the same color, they're both assigned
    for (int c = 0; c < colors.length; c++){
      for (String[] edge:graph.edges){
        cnf.expression.add(new ArrayList<>(List.of(
          -getLiteral(c, vertexNameToIndex.get(edge[0])),
          -getLiteral(c, vertexNameToIndex.get(edge[1]))
        )));
      }
    }
    cnf.clauses = cnf.expression.size();
    return cnf;
  }

  public static Graph parseGraph(String filePath){
    ArrayList<String> verticies = new ArrayList<>();
    ArrayList<String[]> edges = new ArrayList<>();
    File file = new File(filePath);
    try{
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()){
        String line = scanner.nextLine();
        String[] spilttedLine = line.trim().split("\\s+");
        if (spilttedLine.length == 1){
          verticies.add(spilttedLine[0]);
        }
        else if (spilttedLine.length == 2){
          edges.add(spilttedLine);
        }
      }
      scanner.close();
    }
    catch(FileNotFoundException e){
      e.printStackTrace();
    }
    return new Graph(verticies.toArray(String[]::new), edges.toArray(String[][]::new));
  }

  public static void main(String[] args) {
    Graph graph = parseGraph("dataset/cambodia.txt");
    String[] myColors = {"R", "G", "B", "Y"};
    KColoring kcoloring = new KColoring(myColors, graph);
    CNF cnf = kcoloring.toSat();
    /*
    String kcoloringAsDIMACS = DIMACS.toDIMACS(cnf);
    String cnfPath = "dataset/kcoloring/kcoloring001.cnf";
    try{
      Files.writeString(
        Paths.get(cnfPath),
        kcoloringAsDIMACS
      );
    }
    catch (IOException e){
      e.printStackTrace();
    }
    Result result = KISSAT.solve(cnfPath);
    */
    Result result = DPLL.sat(cnf);
    System.out.println(result);
  }
}
