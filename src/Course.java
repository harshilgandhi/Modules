import java.util.ArrayList;
import java.util.List;


public class Course {
    private String num;
    private String uni;
    private String desc;
    private List<Course> preReq;
    private String title;
    List<String> phrases = new ArrayList<String>();
    
    public Course(String num, String uni, String desc, List preReq)
    {
        this.desc=desc;
        this.num=num;
        this.preReq=preReq;
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
        return num;
    }

    public String getDesc() {
        return desc;
    }

    public String getUni() {
        return uni;
    }

    public List<Course> getPreReq() {
        return preReq;
    }

    public void setPreReq(List<Course> preReq) {
        this.preReq = preReq;
    } 

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }  
    
}
