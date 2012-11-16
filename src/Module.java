import java.util.ArrayList;
import java.util.List;


public class Module {
	
	private static int count = 0;
	
	private String name;
	private int id;
	private String desc;
        private String uni;
        private List<Module> allPreReqModules= new ArrayList<Module>();
	private List<Integer> preReqModulesId = new ArrayList<Integer>();
	private List<Course> preReqCourses = new ArrayList<Course>();
	private List<Integer> tempPreReqModulesId = new ArrayList<Integer>();
	private List<Integer> tempPreReqModulesCount = new ArrayList<Integer>();
	
	//NON STATIC BLOCK
	{
		initId();
	}
	
	Module()
	{
		
	}

        public List<Module> getAllPreReqModules() {
            return allPreReqModules;
        }

        public void setAllPreReqModules(List<Module> allPreReqModules) {
            this.allPreReqModules = allPreReqModules;
        }
	
	
        public List<Integer> getTempPreReqModulesId() {
		return tempPreReqModulesId;
	}
	
	public void setPreReqModulesId(List<Integer> preReqModulesId) {
		this.preReqModulesId = preReqModulesId;
	}
	
	public void setTempPreReqModulesCount(List<Integer> tempPreReqModulesCount) {
		this.tempPreReqModulesCount = tempPreReqModulesCount;
	}
	
	public List<Integer> getTempPreReqModulesCount() {
		return tempPreReqModulesCount;
	}
	
	public List<Integer> getPreReqModulesId() {
		return preReqModulesId;
	}
	
	public void setTempPreReqModulesId(List<Integer> tempPreReqModulesId) {
		this.tempPreReqModulesId = tempPreReqModulesId;
	}
	
	
	Module(String num, String name, String desc, List<Course> preReqCourses)
	{
		this.name = name;
		this.desc = desc;
        this.preReqCourses = preReqCourses;
	}
	
	public void initId() {
		this.id = Module.getCount() + 1;
		incrementCount();
	}
	
	public static int getCount() {
		return count;
	}
	
	public static void incrementCount()
	{
		count ++;
	}
	
	public int getId() {
		return id;
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
	
	public List<Course> getPreReq() {
		return preReqCourses;
	}
	
	public void setPreReqCourses(List<Course> preReqCourses) {
		this.preReqCourses = preReqCourses;
	}
	
	
	  public String toString()
	    {
	        String a="Module id: "+this.getId()+"\n";
	        String b="Module name: "+this.getName();
	        return a+b;
	    }	 

}
