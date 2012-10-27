import net.htmlparser.jericho.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class FindTagsElement {
	public static void main(String[] args) throws Exception {
		String sourceUrlString="http://www.psych.nyu.edu/courses/ugfall12.html";
		if (sourceUrlString.indexOf(':')==-1) sourceUrlString="file:"+sourceUrlString;
		MicrosoftConditionalCommentTagTypes.register();
		MasonTagTypes.register();
		Source source=new Source(new URL(sourceUrlString));
		System.out.println("\n*******************************************************************************\n");

		System.out.println("A Elements:");
		displaySegments(source.getAllElements(HTMLElementName.A));
		System.out.println("P Elements:");
		displaySegments(source.getAllElements(HTMLElementName.P));
  }

	private static void displaySegments(List<? extends Segment> segments) {
		for (Segment segment : segments) {
			System.out.println("-------------------------------------------------------------------------------");
			System.out.println(segment.getDebugInfo());
			System.out.println(segment);
		}
		System.out.println("\n*******************************************************************************\n");
	}
}