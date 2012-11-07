import java.util.List;


public class Module {
	private String name;
	private String desc;
	private List<Module> preReqModules;
	private List<Course> preReqCourses;
	
	Module(String num, String name, String desc, List<Module> preReqModules, List<Course> preReqCourses)
	{
		this.name = name;
		this.desc = desc;
        this.preReqModules=preReqModules;
        this.preReqCourses = preReqCourses;
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
	
	
	public List<Module> getPreReqModules() {
		return preReqModules;
	}
	
	public List<Course> getPreReq() {
		return preReqCourses;
	}
	
	public void setPreReqModules(List<Module> preReqModules) {
		this.preReqModules = preReqModules;
	}
	
	public void setPreReqCourses(List<Course> preReqCourses) {
		this.preReqCourses = preReqCourses;
	}

}
