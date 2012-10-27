
public class Module {
	private String num;
	private String name;
	private String desc;
	private String preReq[];
	
	Module(String num, String name, String desc)
	{
		this.num = num;
		this.name = name;
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNum() {
		return num;
	}
	
	public void setNum(String num) {
		this.num = num;
	}
	
	public String[] getPreReq() {
		return preReq;
	}
	
	public void setPreReq(String[] preReq) {
		this.preReq = preReq;
	}

}
