
public class Module {
	private String name;
	private String desc;
	private String preReq[];
	
	Module(String num, String name, String desc, String[] preReq)
	{
		this.name = name;
		this.desc = desc;
                this.preReq=preReq;
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
	
	
	public String[] getPreReq() {
		return preReq;
	}
	
	public void setPreReq(String[] preReq) {
		this.preReq = preReq;
	}

}
