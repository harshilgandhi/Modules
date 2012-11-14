import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	private static Set<Module> moduleSet = new HashSet<Module>();
	private static List<String> ignoreList = new ArrayList<String>();
	private static Set<String> moduleNames = new HashSet<String>();
	

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
		
		
        String[] ignoreWords = new String[] {"pre","geometry","algebra","assignment","assignments","design","unit","explore","selected","explores","engineering","management","method","specifications","specification","units","programs","implementations","implementation","future","program","emphasis","techniques","more","proofs","'","","fundamental","limited","part","media","scientific","experience","fundamentals","topic","level","levels","topics","knowledge","s","campus","campuses","","fee","fees","none","laboratory","familiar","familiarity","use","books","prior","standard","recommended","book","area","quarter","semester",",","form","learn","learns","good","aka","floor","building","bldg","understanding","problem","problems","topic","student","students","standing","cs","cse","main","principle","principles","same","similar","cheap","math","maths","mathematics","practice","computer","computers","science","sciences","small","study","areas","or","  ","   ","un","cases","case","both","\\/","enrollment","an","sophomore","junior","senior","several","various","freshman","corequisite","corequisites","example","examples","preparation"," ","","do","to","a","no","pc","library","libraries","credit","high-school","high","co-requisites","co-requisite","corequisites","school","high-schools","schools","credits","course","courses","pre-requisite","pre-requisites","prerequisite","prerequisites","concept","concepts","basic","introduce","introduces","skill","skills","practical","research", "projects", "labs", "lab", "laboratories", "seminar", "college", "precollege", "university", "class", "periods", "professor", "professors","undergrad", "undergraduate", "grad", "graduate", "studies", "instructor", "instructors","consent","able","about","across","after","all","almost","also","among","even","better","and","any","are","because","been","so","few","but","can","cannot","further","make","makes","many","ahead","could","dear","did","does","either","else","ever","every","for","from","get","got","had","has","have","her","hers","him","his","how","however","into","its","just","least","let","like","likely","may","might","in","on","most","must","neither","nor","not","off","often","only","our","own","other","say","says","she","the","rather","said","says","should","since","some","than","that","their","them","then","there","was","who","yet","you","why","these","they","this","twas","wants","were","what","when","where","which","while","whom","will","with","would","your","even"};	
        for(int i = 0; i < ignoreWords.length; i ++)
        {
        	ignoreList.add(ignoreWords[i]);
        }	
        	
        	
        	
        	
        	
        	
        String[] inputUrls = new String[] {"http://www.ucsd.edu/catalog/courses/CSE.html", "http://registrar.utexas.edu/archived/catalogs/grad07-09//ch04/ns/cs.crs.html"};
		WikiBingTester wbtester = new WikiBingTester();
		DatabaseLookup dblookup = new DatabaseLookup();
		NlpParser nlpParser = new NlpParser();
		
		System.out.println("PARSING STARTED...");
		
		for(int ip = 0; ip < inputUrls.length; ip ++)
		{
			System.out.println("STILL PARSING...");
			
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
                System.out.println("CREATING COURSES DONE...");
		
                System.out.println("STARTED MODULE FINDING...");
				for(Course currentCourse : courseList)
				{
					String a=currentCourse.getDesc();
					System.out.println(a);
					List<String> potentialModules = nlpParser.getPotentialModules(a);
					boolean isModule = true;
					int isConsidered = 0;
					boolean oneWord = false;
					for(String currentModule : potentialModules)//FIRST DATABASE LOOKUP
					{
						System.out.println("...");
						isModule = true;
						isConsidered = 0;
						oneWord = false;
						String[] moduleWords = currentModule.split(" ");
						for(int i = 0; i < moduleWords.length; i ++)
						{
							moduleWords[i] = moduleWords[i].toLowerCase();
							for(int k = 0; k < moduleWords[i].length(); k ++)
							{
								if(!Character.isLetter(moduleWords[i].charAt(k)))
									break;
							}
							if(!ignoreList.contains(moduleWords[i]))
							{
								isConsidered ++;
								
								System.out.println("imp word........................"+moduleWords[i]);
								int wc = dblookup.getWordCount(moduleWords[i], "ComputerScience");
								int df = dblookup.getWordDocFreq(moduleWords[i], "ComputerScience");
								if(wc <= 10 || df <= 5)
								{
									isModule = false;
								}
								if(wc > 32 && !isModule)
								{
									isModule = true;
								}
								if(df > 12 && !isModule && wc < 32)
								{
									isModule = true;
								}
								if(moduleWords.length == 2)
								{
									if(wc < 50)
										isModule = true;
									if(wc > 50)
										isModule = false;
									if(df > 16)
										isModule = false;
									oneWord = true;
								}
							}
						}
						if(isModule && (double)((double)isConsidered / (double)moduleWords.length) >= 0.5)
						{
							if(!moduleNames.contains(currentModule))
							{
								Module module = new Module();
								module.setName(currentModule);
								moduleSet.add(module);
								if(oneWord)
									System.out.println("ADDED MODULE==============ONE=WORD================>" + currentModule);
								else
									System.out.println("ADDED MODULE======================================>" + currentModule);
							}
						}
					}
				}
               
				System.out.println("PRINTING MODULES...");
                //print out the results for debugging
				for(Module currentModule : moduleSet)
				{
					currentModule.toString();
				}
				
//                for(Course c: courseList)
//                {
//                    System.out.println(c);
//                }
                
				
				//DEPENDENCIES
				for(Course currentCourse : courseList)
				{
					
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