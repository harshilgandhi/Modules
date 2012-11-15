import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Set;

public class Digraph {
  
  public static boolean DigraphToFile(String file, Set<Module> nodes) {
    String fileName = file + ".dot";
    File f = new File(fileName);
    try {
      if (!f.createNewFile())
        return false;
      
      FileWriter w = new FileWriter(fileName);
      w.write("digraph {\n");
      for (Module to : nodes) {
        w.write("\"" + to.getName() + "\"\n");
        List<Integer> preReqs = to.getPreReqModulesId();
        for (Module from : nodes)
          if(preReqs.contains(from.getId()))
        	w.write("\"" + from.getName() + "\"->\"" + to.getName() + "\"\n");
      }
      w.write("}\n");
      w.close();
    }
    catch (Exception e) {
      System.err.println(e.toString());
    }
    return true;
  }
  
}
