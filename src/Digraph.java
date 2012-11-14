import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class Digraph {
  
  public static boolean DigraphToFile(String file, List<Module> nodes) {
    String fileName = file + ".dot";
    File f = new File(fileName);
    try {
      if (!f.createNewFile())
        return false;
      
      FileWriter w = new FileWriter(fileName);
      w.write("digraph {\n");
      for (Module to : nodes) {
        w.write("\"" + to.toString() + "\"\n");
        List<Course> preReqCourses = to.getPreReq();
        for (Course from : preReqCourses)
          w.write("\"" + from.toString() + "\"->\"" + to.toString() + "\"\n");
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
