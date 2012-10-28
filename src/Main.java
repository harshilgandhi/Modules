import java.util.ArrayList;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
        FindTagsElement test = new FindTagsElement();
        
        ArrayList<String> pList = test.find("http://www.college.columbia.edu/bulletin/depts/psych.php?tab=courses","p");
        //ArrayList<String> aList = test.find("http://www.college.columbia.edu/bulletin/depts/psych.php?tab=courses","a");
        
        
        
	}

}
