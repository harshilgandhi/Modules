import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import qtag.Qtag;

public class QtagTest{
    
    public QtagTest(String s)
    {
        super(s);
                
    }
    
    private static Qtag qt;
  public static List<String> chunkQtag(String str) throws IOException {
    List<String> result = new ArrayList<String>();
    if (qt == null) {
      qt = new Qtag("lib/english");
      qt.setOutputFormat(2);
    }
    String[] split = str.split("\n");
    for (String line : split) {
      String s = qt.tagLine(line, true);
      String lastTag = null;
      String lastToken = null;
      StringBuilder accum = new StringBuilder();
      for (String token : s.split("\n")) {
        String[] s2 = token.split("\t");
        if (s2.length < 2) continue;
        String tag = s2[1];

        if (tag.equals("JJ")
            || tag.startsWith("NN")
            || tag.startsWith("??")
            || (lastTag != null && lastTag.startsWith("NN") && s2[0].equalsIgnoreCase("of"))
            || (lastToken != null && lastToken.equalsIgnoreCase("of") && s2[0].equalsIgnoreCase("the"))
            ) {
          accum.append(s2[0]).append("-");
        } else {
          if (accum.length() > 0) {
            accum.deleteCharAt(accum.length() - 1);
            result.add(accum.toString());
            accum = new StringBuilder();
          }
        }
        lastTag = tag;
        lastToken = s2[0];
      }
      if (accum.length() > 0) {
        accum.deleteCharAt(accum.length() - 1);
        result.add(accum.toString());
      }
    }
    return result;
  }
    
    
    
    
    
    
    
    
    
}