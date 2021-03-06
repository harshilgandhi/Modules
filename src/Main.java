import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


public class Main {

	/**
	 * @param args
	 */
	private static boolean debug = true;
	private static URL url;
	private static URLConnection urlConnection;
	private static InputStream inputStream;
	private static String content;
	private static byte[] contentRaw;
	private static Pattern pattern1;
        private static Pattern pattern2;
        private static Pattern pattern3;
	private static Matcher matcher;
	private static String htmlDocTxt;
	private static Elements aElements;
	private static Elements pElements;
	private static Elements divElements;
	private static Elements trElements;
	private static Elements tdElements;
	private static Elements liElements;
	private static List<Course> courseList = new ArrayList<Course>();
	private static List<Module> moduleList = new ArrayList<Module>();

	public static int parallStruct(Elements blocks, int uni, Pattern pattern)
        {
            for(Element e : blocks)
            {
                String title; //used to store course title temporarily
                String id=""; //used to store ids temporarily
                boolean descFound=false;
                int nextNeighborCount=0;
                int parentCount=0;
                Element nextNeighbor=e;
                Element currentElement=e;
                String desc="";
                title=e.text(); //found the course title
                if(title.length()>70) //if title is too long, we consider it not title
                {
                    continue;
                }
                matcher=pattern.matcher(title);

                if(matcher.find()) //it is always found 
                {
                    id=matcher.group();                
                }                      
                
                while((!descFound)&&(nextNeighborCount<3)&&(parentCount<3))//we only look up to next three siblings and two parent/ancestor
                {
                    nextNeighbor=currentElement.nextElementSibling();
                    if(nextNeighbor==null)
                    {
                        currentElement=currentElement.parent();
                        parentCount++;
                        nextNeighborCount=0;
                        continue;
                    }
                    currentElement=nextNeighbor;
                    
                    String tempDesc=currentElement.text();
                    if(tempDesc.length() >2000)
                        break;

                    if((tempDesc.length()>60)) //if description found
                    {   
                        desc=tempDesc;
                        descFound=true;
                    }
                    else
                    {
                        nextNeighborCount++;
                    }
                }

                if(descFound) //only create course object if the description is found
                {
                    Course newCourse=new Course(id,String.valueOf(uni),desc);
                    courseList.add(newCourse);
                }                                        
            }
            
            return courseList.size();
        }
        
        
        public static void main(String[] args) throws Exception {
		
		String[] inputUrls = new String[] {"http://www.ucsd.edu/catalog/courses/CSE.html"};
		//DatabaseLookup dblookup = new DatabaseLookup();
		for(int ip = 0; ip < inputUrls.length; ip ++)
		{
			String inputUrl = inputUrls[ip];
			Document htmlDoc = Jsoup.connect(inputUrl).get();
                        String regex1="(^[A-Z]{2,6}\\s?[a-zA-Z]?[0-9]{1,4}[a-zA-Z]?\\b)";
                        String regex2="(^[0-9]{2,4}[a-zA-Z]\\b)";
                        String regex3="(^Computer\\sScience\\s[0-9]{2,4}\\b)";
                        //String regex3="(\\b[A-Z][0-9]{2,4}[a-zA-Z]?\\b)"
			pattern1 = Pattern.compile(regex1);
                        pattern2 = Pattern.compile(regex2);
                        pattern3 = Pattern.compile(regex3);
                        Pattern pattern=null;
                        Pattern[] pSets={pattern1, pattern2, pattern3};
                        Elements foundBlocks=null;
                        for(int i=0;i<pSets.length;i++)
                        {
                            foundBlocks=htmlDoc.getElementsMatchingOwnText(pSets[i]);
                            if(foundBlocks.size()>10)
                            {    pattern=pSets[i];
                                break;   
                            }
                        }

                        for(Element e : foundBlocks)
                        {
                            String title; //used to store course title temporarily
                            String id=""; //used to store ids temporarily
                            boolean descFound=false;
                            int parentCount=0;
                            Element parent=e.parent();
                            String desc="";
                            title=e.text(); //found the course title
                            if(title.length()>70) //if title is too long, we consider it not title
                            {
                                continue;
                            }
                            matcher=pattern.matcher(title);
                            
                            if(matcher.find()) //it is always found 
                            {
                                id=matcher.group();                
                            }                      
                            while((!descFound)&&(parentCount<4))//we only look up four parent/ancestors
                            {
                                String tempDesc=parent.text();
                                //matcher=pattern.matcher(tempDesc);
//                                while(matcher.find())
//                                {                               
//                                    int count=matcher.groupCount();                                
//                                }
         
                                    if(tempDesc.length() >2500)
                                        break;
                                    
                                    
                                
                                
                                if((tempDesc.length()-title.length())>60) //if description found
                                {   
                                    int i=tempDesc.indexOf(title);
                                    //get the pure description by deleting the title and everything comes before it
                                    desc=tempDesc.substring(i+title.length()); 
                                    descFound=true;
                                }
                                else
                                {
                                    parent=parent.parent();
                                    parentCount++;
                                }      
                            }
                      
                            if(descFound) //only create course object if the description is found
                            {
                                Course newCourse=new Course(id,String.valueOf(ip),desc);
                                courseList.add(newCourse);
                            }                                        
                        }
                        
                        if(courseList.size()<10)
                        {
                            parallStruct(foundBlocks,ip,pattern);
                        }
                               
                       
                }
                
                
               
                
                //print out the results for debugging
                for(Course c: courseList)
                {
                    System.out.println(c);
                }
                
                
                
                
                
                        

	
	
	
		
		
		
//		String[] inputUrls = new String[] {"http://www.cms.caltech.edu/academics/course_desc"};
//		
//		for(int ip = 0; ip < inputUrls.length; ip ++)
//		{
//			String inputUrl = inputUrls[ip];
//			Document htmlDoc = Jsoup.connect(inputUrl).get();

//			aElements = htmlDoc.select("a");
//			pElements = htmlDoc.select("p");
//			divElements = htmlDoc.select("div");
//			trElements = htmlDoc.select("tr");
//			tdElements = htmlDoc.select("td");
//			liElements = htmlDoc.select("li");

                	//htmlDocTxt = htmlDoc.toString();
                
                        
                        
//		    FindTagsElement findTagsObj = new FindTagsElement();


//			htmlDocTxt = htmlDoc.toString();
//			
//			
//			
//			FindTagsElement findTagsObj = new FindTagsElement();

//		    ArrayList<String> divList = findTagsObj.find(inputUrls[ip],"div");
//		    ArrayList<String> aList = findTagsObj.find(inputUrls[ip],"a");
//		    ArrayList<String> plist=findTagsObj.find(inputUrls[ip],"p");
//		                  
//	        ArrayList<String> listUsing=divList;
//	        ArrayList<String> titles=new ArrayList<String>();
//	        ArrayList<String> descs=new ArrayList<String>();
//	        if(divList.size()<30)
//	            listUsing=plist;
//	        for (String i: listUsing)
//	        {
//	            String title="";
//	            String desc="";
//	            boolean titleFound=false;
//	            boolean validCourse=true;
//	            String[] parts=i.split("<");
//	            int sizeOfTempDescs=5;
//	            String[] tempDescs=new String[sizeOfTempDescs];
//	            int descCount=0;
//	            
//	            for( String j: parts)
//	            {
//	               if(validCourse)  //if the we consider this is a course
//	               { String[] segs=j.split(">");
//	                        
//		               if (segs.length!=1)
//		               {
//		                    //if title is found, look for course description
//		                   if(titleFound){                             
//		                       if(segs[1].length()>60)
//		                       {
//		                           if(descCount<5)
//		                           {desc=segs[1];
//		                           tempDescs[descCount]=desc; 
//		                           descCount++;
//		                           }
//		                           else  //if the the number of description we got is greater than 5, we consider this not a course
//		                           {
//		                              validCourse=false;
//		                           }
//		                       }
//		                   }
//		                                    
//		                   //look for title is its not found yet
//		                   if(!titleFound){
//		                       if(segs[1].length()>10)
//		                       { 
//		                           if(!segs[1].contains("\n")) //if it contains a new line character, we consider it not a title
//		                           {title=segs[1];                        
//		                           titleFound=true;}
//		                       }
//		                   }
//		               }            
//	               }     
//	            }
//	            if(!desc.equals("")&&!titles.contains(title)&&validCourse)                  
//	            {
//	            	titles.add(title);
//	            	System.out.println("Course Title: "+title);
//	            	for(String k: tempDescs)
//	            		if(k!=null)
//	            			System.out.println("Course Description: "+k);
//	            }
//	        }
//		}
	}
	
	
	private static void log(Object o)
	{
		if(debug)
			System.err.println(o);
	}
}
	
























/* IN MAIN:

 */


/*
///////////////////////////////////THIS IS FOR WEB PAGES DAT HAVE NO DESCRIPTIONS DIRECTLY.
			//Finding course numbers
			String regex="(\\b[A-Z]{2,4}\\s?[a-zA-Z]?[0-9]{2,4}[a-zA-Z]?)";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(htmlDocTxt);
			List<String> links = new ArrayList<String>();
			Elements courseLinks = new Elements();
			List<String> foundMatches = new ArrayList<String>();
			int legitCount = 0;
			while (matcher.find())
			{
				String found=matcher.group();//Course number in "found"
				foundMatches.add(found);
//				int countFound=matcher.groupCount();
				
				//gets "a" or "p" or "td" or "li" in which this course number is found
				Elements e = htmlDoc.getElementsContainingOwnText(found);
				if(aElements.contains(e.get(0)))
				{	legitCount ++;
					courseLinks.add(e.get(0));
				}
				else//Find an <a href="...">...</a> in Course title.. basically findin the link in the "CELL"
				{
					//we are not duin anythn in this case as atleast 90% of univs have links on course numbers and not titles.
				}
			}
			if(legitCount <= 5)//If there are no course numbers at all on the page given... (links on Course Titles..) eg. MIT CS site... http://ocw.mit.edu/courses/electrical-engineering-and-computer-science/
			{
				for(Element e : aElements)// get all links first.. so we can find links that are on course titles somehow
				{
					Main.log(e);
				}
			}
			for(int j = 0; j < courseLinks.size(); j ++)
			{
				courseLinks.get(j).getElementsByAttribute("href");
				Main.log(foundMatches.get(j) + "\t" + courseLinks.get(j));
				int endIndex = courseLinks.get(j).toString().indexOf("\">");
				Main.log(courseLinks.get(j).toString().substring((courseLinks.get(j).toString().indexOf("href"))+6,endIndex));
				links.add(courseLinks.get(j).toString().substring((courseLinks.get(j).toString().indexOf("href"))+6,endIndex));
				Main.log(legitCount);
				//all unprocessed course links stored in "links" list..
			}
 */