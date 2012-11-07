package db;

import java.io.File;

public class TestLynx{
	public static void main(String[] args) throws Exception{
		WordCounter counter = new WordCounter(new File("src/URLs"));
		counter.countAll();
		//LinkQueue list = new LinkQueue("http://www.cs.columbia.edu/education/courses/list?yearterm=20123");//"http://docs.oracle.com/javase/1.4.2/docs/api/java/util/Hashtable.html");
		//list.print();
	}
}