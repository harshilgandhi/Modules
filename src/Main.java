import java.io.IOException;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
        FindTagsElement test = new FindTagsElement();
        
        test.findElements("http://www.college.columbia.edu/bulletin/depts/psych.php?tab=courses");
	}

}
