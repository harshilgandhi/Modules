import net.htmlparser.jericho.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class FindTagsElement {
        
        public ArrayList<String> find(String url, String tag) throws Exception{
                ArrayList<String>  ret=new ArrayList<String>();
		if (url.indexOf(':')==-1) url="file:"+url;
		MicrosoftConditionalCommentTagTypes.register();
		MasonTagTypes.register();
		Source source=new Source(new URL(url));
		
                switch(tag){
                      case "a":
                          System.out.println("A Elements:");
                          displayAndSaveSegments(source.getAllElements(HTMLElementName.A),ret);
                          break;
                      case "p":
                          System.out.println("P Elements:");
                          displayAndSaveSegments(source.getAllElements(HTMLElementName.P),ret);
                          break;        
                      case "div":
                          System.out.println("DIV Elements:");
                          displayAndSaveSegments(source.getAllElements(HTMLElementName.DIV),ret);
                          break;        
                          
                }
		
		return ret;
            
        }

	private static void displayAndSaveSegments(List<? extends Segment> segments, ArrayList<String> ret) {
		
            for (Segment segment : segments) {
			//System.out.println("-------------------------------------------------------------------------------");
			//System.out.println(segment);
                        ret.add(segment.toString());
                        
		}
		System.out.println("\n*******************************************************************************\n");
	}
}