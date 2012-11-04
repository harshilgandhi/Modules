import java.util.ArrayList;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
        FindTagsElement findTagsObj = new FindTagsElement();
        String url="http://registrar.utexas.edu/archived/catalogs/grad07-09//ch04/ns/cs.crs.html";
        ArrayList<String> divList = findTagsObj.find(url,"div");
        ArrayList<String> aList = findTagsObj.find(url,"a");
        ArrayList<String> plist=findTagsObj.find(url,"p");
                  
        ArrayList<String> listUsing=divList;
        ArrayList<String> titles=new ArrayList<String>();
        ArrayList<String> descs=new ArrayList<String>();
        //if(divList.size()<20)
            listUsing=plist;
        for (String i: listUsing)
        {
            String title="";
            String desc="";
            boolean titleFound=false;
            boolean validCourse=true;
            String[] parts=i.split("<");
            int sizeOfTempDescs=5;
            String[] tempDescs=new String[sizeOfTempDescs];
            int descCount=0;
            
            for( String j: parts)
            {
               if(validCourse)  //if the we consider this is a course
               { String[] segs=j.split(">");
                        
	               if (segs.length!=1)
	               {
	                    //if title is found, look for course description
	                   if(titleFound){                             
	                       if(segs[1].length()>60)
	                       {
	                           if(descCount<5)
	                           {desc=segs[1];
	                           tempDescs[descCount]=desc; 
	                           descCount++;
	                           }
	                           else  //if the the number of description we got is greater than 5, we consider this not a course
	                           {
	                              validCourse=false;
	                           }
	                       }
	                   }
	                                    
	                   //look for title is its not found yet
	                   if(!titleFound){
	                       if(segs[1].length()>10)
	                       { 
	                           if(!segs[1].contains("\n")) //if it contains a new line character, we consider it not a title
	                           {title=segs[1];                        
	                           titleFound=true;}
	                       }
	                   }
	               }            
               }     
            }
            if(!desc.equals("")&&!titles.contains(title)&&validCourse)                  
            {
            	titles.add(title);
            	System.out.println("Course Title: "+title);
            	for(String k: tempDescs)
            		if(k!=null)
            			System.out.println("Course Description: "+k);
            }
        }
	}
}
