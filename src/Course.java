import java.util.List;


public class Course {
    private String id;
    private String uni;
    private String desc;
    private List<Course> preReq;
    private String title;
    
    public Course(String id, String uni, String desc, List preReq)
    {
        this.desc=desc;
        this.id=id;
        this.preReq=preReq;
        this.uni=uni;
    }
    
    public Course(String id, String uni, String desc)
    {
        this.desc=desc;
        this.id=id;
        this.preReq=preReq;
        this.uni=uni;
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
        this.id = num;
    }

    public void setUni(String uni) {
        this.uni = uni;
    } 
    
    public String toString()
    {
        String a="Course ID: "+id+"\n";
        String b="Course Description: "+this.desc;
        return a+b;
    }
    
}
