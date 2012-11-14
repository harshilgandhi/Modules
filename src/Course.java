import java.util.ArrayList;
import java.util.List;


public class Course {
    private String id;
    private String uni;
    private String desc;
    private ArrayList<String> preReq;
    private String title;
    List<String> phrases = new ArrayList<String>();
    
    public Course(String num, String uni, String desc, ArrayList<String> preReq)
    {
        this.desc=desc;
        this.id=num;
        this.preReq=preReq;
        this.uni=uni;
    }
    
    public Course(String num, String uni, String desc)
    {
        this.desc=desc;
        this.id=num;
        this.uni=uni;
    }
    
    public List<String> getPhrases() {
		return phrases;
	}
    
    public void setPhrases(List<String> phrases) {
		this.phrases = phrases;
	}
    
    public String getTitle() {
		return title;
	}
    
    public void setTitle(String title) {
		this.title = title;
	}
    
    public String getNum() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getUni() {
        return uni;
    }

    public ArrayList<String> getPreReq() {
        return preReq;
    }

    public void setPreReq(ArrayList<String> preReq) {
        this.preReq = preReq;
    } 

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setNum(String num) {
        this.id = num;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }  
    
    public String toString()
    {
        String a="Course ID: "+id+"\n";
        String b="Course Description: "+this.desc+"\n";
        String c="Prereq: "+preReq.toString();
        return a+b+c;
    }
    
}
