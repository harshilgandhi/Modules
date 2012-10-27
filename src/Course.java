/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lannister
 */
public class Course {
    private String num;
    private String uni;
    private String desc;
    private String[] preReq;
    
    public Course(String num, String uni, String desc, String[] preReq)
    {
        this.desc=desc;
        this.num=num;
        this.preReq=preReq;
        this.uni=uni;
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

    public String[] getPreReq() {
        return preReq;
    }

    public void setPreReq(String[] preReq) {
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
