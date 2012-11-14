import java.io.IOException;
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
	private static String[] inputDescUrls = new String[] {"http://www.cs.brown.edu/courses/"};
        private static String[] inputLinkUrls = new String[] {"http://www.cs.purdue.edu/academic_programs/courses/schedule/2012/Fall/undergraduate.sxhtml"};	
    
        private static boolean debug = true;
	private static URL url;
	private static URLConnection urlConnection;
	private static InputStream inputStream;
	private static String content;
	private static byte[] contentRaw;
	private static Pattern pattern1;
        private static Pattern pattern2;
        private static Pattern pattern3;
        private static Pattern patternPre1;
        private static Pattern patternPre2;
        private static Pattern patternPre3;
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
	

	public static int parallStruct(Elements blocks, int uni, Pattern pattern, int pIndex, Pattern[] pPreSets)
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
                    ArrayList<String> preReq=new ArrayList<String>();
                    findPrereqInDesc(desc, pIndex, pPreSets, preReq);
                    Course newCourse=new Course(id,String.valueOf(uni),desc,preReq);
                    courseList.add(newCourse);
                }                                        
            }
            
            return courseList.size();
        }
        
        //returns true if it found the "prereq" string within the description
        public static boolean findPrereqInDesc(String desc, int patternIndex,Pattern[] pSet, ArrayList<String> preReq)
        {          
            int index=desc.indexOf("Prerequisite");
            if(index!=-1)
            {   Pattern patternP=pSet[patternIndex];
                String input=desc.substring(index);
                matcher=patternP.matcher(input);
                while(matcher.find())
                {
                    String courseID=matcher.group();
                    preReq.add(courseID);             
                }
                return true;
            }
            else
            {
                return false;
            }
        }
        
         private static void linkStruct(Elements foundBlocks, int ip, Pattern pattern,int patternIndex, 
                 Pattern[] pPreSets, Document htmlDoc, String url) throws IOException 
         {
             System.out.print("inside 134!");
             aElements = htmlDoc.select("a");
             //Elements courseLinks = new Elements();
             //List<String> links=new ArrayList<String>();
//             for(Element f: aElements)
//             {
//                 System.out.println(f.text());
//             }
             for(Element e: foundBlocks)
             {
                 String id=null;
                 matcher=pattern.matcher(e.text());
                if(matcher.find()) //it is always found 
                {
                    id=matcher.group();                
                }
                if(!e.tagName().equals("a"))
                {
                    if(e.parent().tagName().equals("a"))
                        e=e.parent();
                }
                System.out.print("inside 155!");
                if(aElements.contains(e))
                {	      
                    e.getElementsByAttribute("href");
                     int endIndex = e.toString().indexOf("\">"); 
                        //Main.log(courseLinks.get(j).toString().substring((courseLinks.get(j).toString().indexOf("href"))+6,endIndex));
                     String checkUrl=e.toString().substring((e.toString().indexOf("href"))+6,endIndex);
                     String newUrl=null;
                     System.out.print("inside 162!");
                     if (checkUrl.startsWith("/"))
                     {    
                         newUrl = url.substring(0,url.indexOf("edu")+3) + checkUrl;
                     }
                     if (checkUrl.startsWith("http://"))
                         newUrl = checkUrl;
                     if (checkUrl.startsWith("https://"))
                         newUrl = checkUrl;

                     if (checkUrl.startsWith("www"))
                         newUrl = "http://" + checkUrl;
                     
                     if(newUrl!=null)
                     {                        
                         System.out.print("inside 176!");
                         Document innerhtmlDoc=null;
                         //try
                         {
                              innerhtmlDoc = Jsoup.connect(newUrl).get();
                         }
                         //catch (Exception ex)
                         {    //do nothing
                         }
                         if(innerhtmlDoc!=null)
                         {
                             System.out.print("inside 189!");
                             Pattern pattern1=Pattern.compile("(Description)");
                             Pattern pattern2=Pattern.compile("(Summary)");
                             Pattern pattern3=pattern;
                             
                             Pattern[] descNameList={pattern1,pattern2,pattern3};
                             Elements block=null;
                             for(int i=0; i<descNameList.length; i++)
                             {  
                                 block=innerhtmlDoc.getElementsMatchingOwnText(descNameList[i]);
                               if(block.size()>0)
                                   break;
                               else
                                   i++;
                             }
                             if(block.size()>0)
                             { 
                                 boolean descFound=false;
                                 int parentCount=0;
                                 Element currentElement=block.get(0);
                                 String desc=null;
                                 boolean preFound=false;
                                 while((!descFound)&&(parentCount<2))//we only look up to two parent/ancestor
                                 {
                                     Element neighbor=currentElement.nextElementSibling();
                                    if(neighbor==null)
                                    {
                                        currentElement=currentElement.parent();
                                        parentCount++;
                                        continue;
                                    }
                                    //System.out.println("insdie 207");
                                    String tempDesc=neighbor.text();
                                    if((tempDesc.length()>60)&&(tempDesc.length()<2500)) //if description found
                                    {   
                                        //System.out.println("insdie 211");
                                        desc=tempDesc;
                                        descFound=true;
                                        ArrayList<String> preReq=new ArrayList<String>();
                                        //find Prereq in the course description
                                        preFound=findPrereqInDesc(desc, patternIndex, pPreSets, preReq);
                                        if(!preFound) 
                                        {   
                                            Elements pBlock=innerhtmlDoc.getElementsContainingOwnText("Prerequisite");
                                            if(pBlock.size()>0)
                                            {   currentElement=pBlock.get(0);
                                                int pParentCount=0;
                                                while(pParentCount<2)
                                                {
                                                    //System.out.println("insdie 225");
                                                    neighbor=currentElement.nextElementSibling();
                                                    if(neighbor==null)
                                                    {
                                                        currentElement=currentElement.parent();
                                                        pParentCount++;
                                                        continue;
                                                    }
                                                    String preStr=neighbor.text();
                                                    Pattern patternP=pPreSets[patternIndex];
                                                    //String input=desc.substring(index);
                                                    matcher=patternP.matcher(preStr);
                                                    while(matcher.find())
                                                    {
                                                        String courseID=matcher.group();
                                                        preReq.add(courseID);             
                                                    }
                                                    break;
                                                }  
                                            }
                                        }
                                        Course newCourse=new Course(id,String.valueOf(ip),desc,preReq);
                          
                                        courseList.add(newCourse);
                                        
                                    }
                                    else
                                    {
                                        break; //if tempDesc not long enough, then we consider the description is missing
                                    }
                                }
                             }
                         } 
                     }
                }
                else//Find an <a href="...">...</a> in Course title.. basically findin the link in the "CELL"
                {   
                     
                     //we are not duin anythn in this case as atleast 90% of univs have links on course numbers and not titles.
                }

            }
         
         }
        
        
        public static void main(String[] args) throws Exception {
		

		
        String[] ignoreWords = new String[] {"pre","geometry","algebra","assignment","assignments","design","unit","explore","selected","explores","engineering","management","method","specifications","specification","units","programs","implementations","implementation","future","program","emphasis","techniques","more","proofs","'","","fundamental","limited","part","media","scientific","experience","fundamentals","topic","level","levels","topics","knowledge","s","campus","campuses","","fee","fees","none","laboratory","familiar","familiarity","use","books","prior","standard","recommended","book","area","quarter","semester",",","form","learn","learns","good","aka","floor","building","bldg","understanding","problem","problems","topic","student","students","standing","cs","cse","main","principle","principles","same","similar","cheap","math","maths","mathematics","practice","computer","computers","science","sciences","small","study","areas","or","  ","   ","un","cases","case","both","\\/","enrollment","an","sophomore","junior","senior","several","various","freshman","corequisite","corequisites","example","examples","preparation"," ","","do","to","a","no","pc","library","libraries","credit","high-school","high","co-requisites","co-requisite","corequisites","school","high-schools","schools","credits","course","courses","pre-requisite","pre-requisites","prerequisite","prerequisites","concept","concepts","basic","introduce","introduces","skill","skills","practical","research", "projects", "labs", "lab", "laboratories", "seminar", "college", "precollege", "university", "class", "periods", "professor", "professors","undergrad", "undergraduate", "grad", "graduate", "studies", "instructor", "instructors","consent","able","about","across","after","all","almost","also","among","even","better","and","any","are","because","been","so","few","but","can","cannot","further","make","makes","many","ahead","could","dear","did","does","either","else","ever","every","for","from","get","got","had","has","have","her","hers","him","his","how","however","into","its","just","least","let","like","likely","may","might","in","on","most","must","neither","nor","not","off","often","only","our","own","other","say","says","she","the","rather","said","says","should","since","some","than","that","their","them","then","there","was","who","yet","you","why","these","they","this","twas","wants","were","what","when","where","which","while","whom","will","with","would","your","even"};	
        for(int i = 0; i < ignoreWords.length; i ++)
        {
        	ignoreList.add(ignoreWords[i]);
        }	

		WikiBingTester wbtester = new WikiBingTester();
		DatabaseLookup dblookup = new DatabaseLookup();
		NlpParser nlpParser = new NlpParser();
		
		System.out.println("PARSING STARTED...");
		
	
                String[] inputUrls=inputLinkUrls;
                boolean usingLinks=true;
                
		//DatabaseLookup dblookup = new DatabaseLookup();
		for(int ip = 0; ip < inputUrls.length; ip ++)
		{
			System.out.println("STILL PARSING...");
			
			String inputUrl = inputUrls[ip];
			Document htmlDoc = Jsoup.connect(inputUrl).get();
                        String regex1="(^[A-Z]{2,6}\\s?[a-zA-Z]?[0-9]{1,5}-?[a-zA-Z]{0,2}\\b)";
                        String regex2="(^[0-9]{2,4}[a-zA-Z]\\b)";
                        String regex3="(^Computer\\sScience\\s[0-9]{2,4}\\b)";
                        String regexPre1="(\\b[A-Z]{2,6}\\s?[a-zA-Z]?[0-9]{1,5}-?[a-zA-Z]{0,2}\\b)";
                        String regexPre2="(\\[0-9]{2,4}[a-zA-Z]\\b)";
                        String regexPre3="(\\bComputer\\sScience\\s[0-9]{2,4}\\b)";
                        //String regex3="(\\b[A-Z][0-9]{2,4}[a-zA-Z]?\\b)"
			pattern1 = Pattern.compile(regex1);
                        pattern2 = Pattern.compile(regex2);
                        pattern3 = Pattern.compile(regex3);
                        patternPre1 = Pattern.compile(regexPre1);
                        patternPre2 = Pattern.compile(regexPre2);
                        patternPre3 = Pattern.compile(regexPre3);
                        Pattern pattern=null;
                        Pattern[] pSets={pattern1, pattern2, pattern3};
                        Pattern[] pPreSets={patternPre1, patternPre2, patternPre3};
                        String id=""; //used to store ids temporarily
                        String title; //used to store course title temporarily
                        Elements foundBlocks=null;
                        int pIndex=0;
                        for(int i=0;i<pSets.length;i++)
                        {
                            foundBlocks=htmlDoc.getElementsMatchingOwnText(pSets[i]);
                            if(foundBlocks.size()>10)
                            {    pattern=pSets[i];
                                pIndex=i;
                                break;   
                            }
                        }
                        if(!usingLinks)
                        {
                                for(Element e : foundBlocks)
                                {


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
                                    ArrayList<String> preReq=new ArrayList<String>();
                                    boolean preFound=findPrereqInDesc(desc, pIndex, pPreSets, preReq);
    //                                if(!preFound)
    //                                {
    //                                    
    //                                }
                                    Course newCourse=new Course(id,String.valueOf(ip),desc,preReq);
                                    courseList.add(newCourse);
                                }                                        
                                }
                                if(courseList.size()<10) //if dont get enough course, try another approach
                            {
                            parallStruct(foundBlocks,ip,pattern,pIndex,pPreSets);
                            }
                        }
                        else
                        {                     
                            linkStruct(foundBlocks,ip,pattern,pIndex,pPreSets,htmlDoc,inputUrl);
                            
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