/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lannister
 */
import java.util.regex.*;

class TestRegex
{
  public static void main(String[] args)
  {
    String txt="asdfl Abcd W134 adfe w123";

    String re1="(\\b[a-z]{2,4}\\s[a-z]?[0-9]{2,4}\\b)";	// Any Single Word Character (Not Whitespace) 1

    Pattern p = Pattern.compile(re1,Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(txt);
    int count=0;
    while (m.find())
	{
		String whole=m.group();
		m.groupCount();
                count++;
		System.out.print(whole+" "+count+"\n"); 
	}
  }
}