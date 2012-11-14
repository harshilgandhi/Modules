import java.util.ArrayList;
import java.util.List;


public class Module {
	
	private static int count = 0;
	
	private String name;
	private int id;
	private String desc;
	private List<Integer> preReqModulesId;
	private List<Course> preReqCourses;
	private List<Integer> tempPreReqModulesId = new ArrayList<Integer>();
	private List<Integer> tempPreReqModulesCount = new ArrayList<Integer>();
	
	//NON STATIC BLOCK
	{
		initId();
	}
	
	Module()
	{
		
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
	
	public void sortTempArrays()
	{
		sort(getTempPreReqModulesCount(), getTempPreReqModulesId());
	}
	
	 public void sort(List<Integer> listCount, List<Integer> listId) {

		    quicksort(0, listCount.size() - 1, listCount, listId);
		  }
		  
		  private void quicksort(int low, int high, List<Integer> listCount, List<Integer> listId) {
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

		  private void exchange(int i, int j, List<Integer> listCount, List<Integer> listId) {
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
		  public String toString()
		    {
		        String a="Module id: "+this.getId()+"\n";
		        String b="Module name: "+this.getName();
		        return a+b;
		    }	 

}
