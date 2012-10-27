import java.io.IOException;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
        FindTagsElement test = new FindTagsElement();
        
        test.find("http://www.college.columbia.edu/bulletin/depts/psych.php?tab=courses","p");
        test.find("http://www.college.columbia.edu/bulletin/depts/psych.php?tab=courses","a");
        
	}

}
