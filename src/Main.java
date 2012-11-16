import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	private static String[] inputDescUrls = new String[] {"http://www.ucsd.edu/catalog/courses/CSE.html"};
    private static String[] inputLinkUrls = new String[] {"http://www.cs.purdue.edu/academic_programs/courses/schedule/2012/Fall/undergraduate.sxhtml"};	
    private static String[] inputUrls=inputDescUrls;
    private static boolean usingLinks=false;
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
	private static Set<Module> moduleSet = new HashSet<Module>();
	private static List<String> ignoreList = new ArrayList<String>();
	private static Set<String> moduleNames = new HashSet<String>();
	private static HashMap<String, Course> courseList = new HashMap<String, Course>();
	public static int countDescParsed = 0;
        public static Set<String> allPreReqCourseNames=new HashSet<String>();
        public static Set<Integer> allPreReqModuleIDs=new HashSet<Integer>();
	

	public static int parallStruct(Elements blocks, int uni, Pattern pattern, int pIndex, Pattern[] pPreSets, String courseOwner)
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
                    findPrereqInDesc(desc, pIndex, pPreSets, preReq,courseOwner);
                    Course newCourse=new Course(id,String.valueOf(uni),desc,preReq);
                    courseList.put(id, newCourse);
                }                                        
            }
            
            return courseList.size();
        }
        
        //returns true if it found the "prereq" string within the description
        public static boolean findPrereqInDesc(String desc, int patternIndex,Pattern[] pSet, ArrayList<String> preReq, String courseOwner)
        {          
            int index=desc.indexOf("Prerequisite");
            if(index!=-1)
            {   
                String reg="\\b[a-zA-Z]?[0-9]{2,5}[a-zA-Z]?\\b";
                Pattern patternP=Pattern.compile(reg);
                String input=desc.substring(index);
                matcher=patternP.matcher(input);
                while(matcher.find())
                {
                    String courseID;
                    String numPart=matcher.group();
                    int i=courseOwner.indexOf(" ");
                    if(i==-1)
                    {   courseID=numPart;}
                    else
                    {   courseID=courseOwner.substring(0, i)+" "+numPart;}
                    System.out.println("courseID Apended:"+courseID);
                    allPreReqCourseNames.add(courseID);
                    preReq.add(courseID);             
                }
                
                return true;
            }
//            if(index!=-1)
//            {   Pattern patternP=pSet[patternIndex];
//                String input=desc.substring(index);
//                matcher=patternP.matcher(input);
//                while(matcher.find())
//                {
//                    String courseID1=matcher.group();
//                    int i=courseID1.indexOf("s ");
//                    String courseID=courseID1.substring(i+2);
//                    allPreReqCourseNames.add(courseID);
//                    preReq.add(courseID);             
//                }
//                return true;
//            }
            else
            {
                return false;
            }
        }
        
         private static void linkStruct(Elements foundBlocks, int ip, Pattern pattern,int patternIndex, 
                 Pattern[] pPreSets, Document htmlDoc, String url, String courseOwner) throws IOException 
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
                                        preFound=findPrereqInDesc(desc, patternIndex, pPreSets, preReq, courseOwner);
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
                                                        allPreReqCourseNames.add(courseID);
                                                        preReq.add(courseID);             
                                                    }
                                                    break;
                                                }  
                                            }
                                        }
                                        Course newCourse=new Course(id,String.valueOf(ip),desc,preReq);
                                        
                                        courseList.put(id, newCourse);
                                        
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
		

		
        String[] ignoreWords = new String[] {"pre","data","one","two","only","team","majors","major","end","following","the","informal","formal","variety","excersises","tools","tool","excercise","curriculum","papers","paper","projects","classes","class","project","analysis","lower","data","team","teams","environment","partitioning","reuse","features","bound","relate","related","task","tasks","both","attention","member","exact","faculty","members","recent","developments","faculties","description","formal","focuses","focus","competition","competitions","hostile","candidates","candidacy","introductory","course","courses","candidate","lectures","uses","use","modern","schemes","scheme","elementary","proof","hour","hours","lecture","homework","curiculum","homeworks","teaching","assistant","assistants","","geometry","algebra","assignment","special","attention","assignments","design","unit","explore","selected","explores","engineering","management","method","specifications","specification","units","programs","implementations","implementation","future","program","emphasis","techniques","more","theory","proofs","'","","fundamental","limited","part","media","scientific","experience","fundamentals","topic","level","levels","topics","knowledge","s","campus","campuses","","fee","fees","none","laboratory","familiar","familiarity","use","books","prior","standard","recommended","book","area","quarter","semester",",","form","learn","learns","good","aka","floor","building","bldg","understanding","problem","problems","topic","student","students","standing","cs","cse","main","principle","principles","same","similar","cheap","math","maths","mathematics","practice","computer","computers","science","sciences","small","study","areas","or","  ","   ","un","cases","case","both","\\/","enrollment","an","sophomore","junior","senior","several","various","freshman","corequisite","corequisites","example","examples","preparation"," ","","do","to","a","no","pc","library","libraries","credit","high-school","high","co-requisites","co-requisite","corequisites","school","high-schools","schools","credits","course","courses","pre-requisite","pre-requisites","prerequisite","prerequisites","concept","concepts","basic","introduce","current","other","others","whose","introduces","skill","skills","practical","research", "projects", "labs", "lab", "laboratories", "seminar", "college", "precollege", "university", "class", "periods", "professor", "professors","undergrad", "undergraduate", "grad", "graduate", "studies", "instructor", "instructors","consent","able","about","across","after","all","almost","also","among","even","better","and","any","are","because","been","so","few","but","can","cannot","further","make","makes","many","ahead","could","dear","did","does","either","else","ever","every","for","from","get","got","had","has","have","her","hers","him","his","how","however","into","its","just","least","let","like","likely","may","might","in","on","most","must","neither","nor","not","off","often","only","our","own","other","say","says","she","the","rather","said","says","should","since","some","than","that","their","them","then","there","was","who","yet","you","why","these","they","this","twas","wants","were","what","when","where","which","while","whom","will","with","would","your","even"};	
        for(int i = 0; i < ignoreWords.length; i ++)
        {
        	ignoreList.add(ignoreWords[i]);
        }	

		WikiBingTester wbtester = new WikiBingTester();
		DatabaseLookup dblookup = new DatabaseLookup();
		NlpParser nlpParser = new NlpParser();
		
		System.out.println("PARSING STARTED...");
		
	
                
		//DatabaseLookup dblookup = new DatabaseLookup();
		for(int ip = 0; ip < inputUrls.length; ip ++)
		{
			System.out.println("STILL PARSING...");
			
			Document htmlDoc = Jsoup.connect(inputUrls[ip]).get();
                        String regex1="(^[A-Z]{2,6}\\s?[a-zA-Z]?[0-9]{1,5}-?[a-zA-Z]{0,2}\\b)";
                        String regex2="(^[0-9]{2,4}[a-zA-Z]\\b)";
                        String regex3="(^Computer\\sScience\\s[0-9]{2,4}\\b)";
                        String regexPre1="(\\b[A-Z]{2,6}\\s?[a-zA-Z]?[0-9]{1,5}-?[a-zA-Z]{0,2}\\b)";
                        String regexPre2="(\\[0-9]{2,4}[a-zA-Z]\\b)";
                        String regexPre3="(\\bComputer\\sScience[s]?\\s[0-9]{2,4}[a-zA-Z]?\\b)";
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
                                    boolean preFound=findPrereqInDesc(desc, pIndex, pPreSets, preReq,id);
    //                                if(!preFound)
    //                                {
    //                                    
    //                                }
                                    Course newCourse=new Course(id,String.valueOf(ip),desc,preReq);
                                    courseList.put(id, newCourse);
                                }                                        
                                }
                                if(courseList.size()<10) //if dont get enough course, try another approach
                            {
                            parallStruct(foundBlocks,ip,pattern,pIndex,pPreSets, id);
                            }
                        }
                        else
                        {                     
                            linkStruct(foundBlocks,ip,pattern,pIndex,pPreSets,htmlDoc,inputUrls[ip],id);
                            
                        }     
                }
                
        
				Iterator<Entry<String, Course>> iteratorX = courseList.entrySet().iterator();
		        while(iteratorX.hasNext())
		        {
		            Course currentCourse=courseList.get(iteratorX.next().getKey());
		            System.out.println("Course: "+currentCourse.getNum());
		            System.out.println("Description: "+currentCourse.getDesc());
		            System.out.println("Prereq: "+currentCourse.getPreReq().toString());
		        }        
				System.out.println("CREATING COURSES DONE...");
		
                System.out.println("STARTED MODULE FINDING...");
                Iterator<Entry<String, Course>> iterator = courseList.entrySet().iterator();
                System.err.println();
                Course thisCourse = null;
                int courseCount = 0;
				while(iterator.hasNext())// && courseCount < 2)
				{
					thisCourse=courseList.get(iterator.next().getKey());
                                        if(thisCourse.getPreReq().size()==0)
                                            if(!allPreReqCourseNames.contains(thisCourse.getNum()))
                                                continue;
                                        
                                        String a=thisCourse.getDesc();
					System.out.println(a);
//					if(thisCourse.getPreReq().size() == 0)
//					{
//						continue;
//					}
					courseCount ++;
					List<String> potentialModules = nlpParser.getPotentialModules(a);
					boolean isModule = true;
					int isConsidered = 0;
					boolean isDigit = false;
					int wc=0,df=0;
                                        ArrayList<Module> modulesInsideThisCourse=new ArrayList<Module>();
					for(String currentModule : potentialModules)//FIRST DATABASE LOOKUP
					{
						System.out.println("...");
						currentModule = currentModule.trim();
						isModule = true;
						isConsidered = 0;
						String[] moduleWords = currentModule.split(" ");
						wc = 0; df = 0;
						for(int i = 0; i < moduleWords.length; i ++)
						{
							isDigit = false;
							
							moduleWords[i] = moduleWords[i].toLowerCase();
							for(int k = 0; k < moduleWords[i].length(); k ++)
							{
								if(!Character.isLetter(moduleWords[i].charAt(k)))
									isDigit = true;
							}
							if(isDigit)
								continue;
							if(!ignoreList.contains(moduleWords[i]))
							{
								isConsidered ++;

								System.out.println("imp word........................"+moduleWords[i]);
								wc += dblookup.getWordCount(moduleWords[i], "ComputerScience");
								df += dblookup.getWordDocFreq(moduleWords[i], "ComputerScience");
								
							}
						}
						
						if(wc <= 100 || df <= 30)
						{
							isModule = false;
						}
						if(wc > 280 && df < 50)
						{
							isModule = true;
						}
						if(df > 120 && wc < 140)
						{
							isModule = true;
						}
						if(moduleWords.length == 2)
						{
							if(wc > 6 && df > 5)
								isModule = true;
							if(wc > 180)
								isModule = false;
							if(df > 20)
								isModule = false;
						}
						if(isModule && (double)((double)isConsidered / (double)moduleWords.length) >= 0.5)
						{
							if(!moduleNames.contains(currentModule))
							{
								Module module = new Module();
								module.setName(currentModule);
                                                                modulesInsideThisCourse.add(module);
								moduleSet.add(module);
								moduleNames.add(module.getName());
								System.out.println("ADDED MODULE======================================>" + currentModule);
							}
						}
					}
                                        thisCourse.setModules(modulesInsideThisCourse);
				}
               
//                for(Course c: courseList)
//                {
//                    System.out.println(c);
//                }
                //CACHING...
				String fileName = "coursecache.txt";
			    File f = new File(fileName);
			    try {
			      FileWriter w = new FileWriter(fileName);
			      Iterator<Entry<String, Course>> iterator2 = courseList.entrySet().iterator();
					while(iterator2.hasNext())
					{
						Course currentCourse=courseList.get(iterator2.next().getKey());
						w.write(currentCourse.getNum() + "---" + currentCourse.getDesc() + "---");
						for(Module m : currentCourse.getModules())
						{
							w.write("preReqModules: "+m.getId() + "---" + m.getName() + "---" + m.getPreReqModulesId().toArray().toString() + "\n");
							
						}
						w.write("EOC");
					}
			    }
			    catch(Exception e)
			    {
			    	e.printStackTrace();
			    }
				//CACHING ENDS
			    
			    
			    //READ FROM CACHE
//			    CacheReader.getCourseList();
			    //READING ENDS
			    
			    
				//DEPENDENCIES
				Iterator<Entry<String, Course>> iterator2 = courseList.entrySet().iterator();
				while(iterator2.hasNext())
				{
					Course currentCourse=courseList.get(iterator2.next().getKey());
					ArrayList<String> preIDs=currentCourse.getPreReq();
					ArrayList<Module> allPrereqModulesInCourse=new ArrayList<Module>();
					ArrayList<Integer> tempIdList = new ArrayList<Integer>(20);
					ArrayList<Integer> tempCountList = new ArrayList<Integer>(20);
					for(String preID: preIDs)
					{
						if(courseList.get(preID) == null)
							continue;
						allPrereqModulesInCourse.addAll(courseList.get(preID).getModules());
						for(int i = 0; i < allPrereqModulesInCourse.size(); i ++)
						{
							if(tempIdList.contains(allPrereqModulesInCourse.get(i).getId()))
							{
								int index = tempIdList.indexOf(allPrereqModulesInCourse.get(i).getId());
								tempCountList.set(index, tempCountList.get(index)+1);
							}
							else
							{
								tempIdList.add(allPrereqModulesInCourse.get(i).getId());
								int index = tempIdList.indexOf(allPrereqModulesInCourse.get(i).getId());
								tempCountList.add(1);
							}
						}
					}
					if(tempCountList.size() > 0)
					{
						sortTempArrays(tempCountList, tempIdList);
//					Iterator it = moduleSet.iterator();
						ArrayList<Integer> realTop2Prereq=new ArrayList<Integer>();
						if(tempIdList.size()>2)
                                                {
                                                    for(int i=1; i<4;i++)
                                                    {
                                                       realTop2Prereq.add(tempIdList.get(tempIdList.size()-i));
                                                       allPreReqModuleIDs.add(tempIdList.get(tempIdList.size()-i));   
                                                    }                                                
                                                
                                                }
                                                else
                                                {
                                                    realTop2Prereq.addAll(tempIdList);
                                                    allPreReqModuleIDs.addAll(tempIdList);
                                                }
						for(Module m : currentCourse.getModules())
                                                {
                                                    m.setPreReqModulesId(realTop2Prereq);
						}
					}
				}

				System.out.println("PRINTING MODULES AND THEIR DEPENDENCIES...");
                //print out the results for debugging
                                Set<Module> moduleNodes= (HashSet<Module>)((HashSet<Module>)moduleSet).clone();
				for(Module currentModule : moduleSet)
				{
					System.out.println(currentModule.toString());
					if(currentModule.getPreReqModulesId().size()==0)
                                        {
                                            System.out.println("No Pre-requisites found for this Module--0 size");
                                            if(!allPreReqModuleIDs.contains(currentModule.getId()))
                                            {
                                                moduleNodes.remove(currentModule);
                                            }
                                        }
					else if(currentModule.getPreReqModulesId().size()==2)
						System.out.println("Dependency --> " + currentModule.getPreReqModulesId().get(0) + ", " + currentModule.getPreReqModulesId().get(1));
					else
						System.out.println("Dependency --> " + currentModule.getPreReqModulesId().get(0));
				}
			Digraph.DigraphToFile("g5-cs", moduleNodes);
		
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

   
	public static void sortTempArrays(ArrayList<Integer> tempCountList, ArrayList<Integer> tempIdList)
	{
		sort(tempCountList, tempIdList);
	}
	
	 public static void sort(List<Integer> listCount, List<Integer> listId) {

		    quicksort(0, listCount.size() - 1, listCount, listId);
		  }
		  
		  private static void quicksort(int low, int high, List<Integer> listCount, List<Integer> listId) {
		    int i = low, j = high;
		    // Get the pivot element from the middle of the list
		    int pivot = listCount.get(low + (high-low)/2);//numbers[low + (high-low)/2];

		    // Divide into two lists
		    while (i <= j) {
		      // If the current value from the left list is smaller then the pivot
		      // element then get the next element from the left list
		      while (listCount.get(i) < pivot)//(numbers[i] < pivot)
		      {
		        i++;
		      }
		      // If the current value from the right list is larger then the pivot
		      // element then get the next element from the right list
		      while (listCount.get(j) > pivot)//(numbers[j] > pivot)
		      {
		        j--;
		      }

		      // If we have found a values in the left list which is larger then
		      // the pivot element and if we have found a value in the right list
		      // which is smaller then the pivot element then we exchange the
		      // values.
		      // As we are done we can increase i and j
		      if (i <= j) {
		        exchange(i, j, listCount, listId);
		        i++;
		        j--;
		      }
		    }
		    // Recursion
		    if (low < j)
		      quicksort(low, j, listCount, listId);
		    if (i < high)
		      quicksort(i, high, listCount, listId);
		  }

		  private static void exchange(int i, int j, List<Integer> listCount, List<Integer> listId) {
			//numbers[i];
			int temp = listCount.get(i);
		    //numbers[i] = numbers[j];
		    listCount.set(i, listCount.get(j));
			//numbers[j] = temp;
		    listCount.set(j, temp);
		    
			//numbers[i];
			int temp2 = listId.get(i);
		    //numbers[i] = numbers[j];
		    listId.set(i, listId.get(j));
			//numbers[j] = temp;
		    listId.set(j, temp2);
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