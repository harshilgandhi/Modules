import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class MechDB {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public void populateDB()
	{
		String[] urls = new String[] {"http://me.stanford.edu/groups/bme/courses.html",
										"http://www.mce.caltech.edu/academics/course_desc#me",
										"http://www.cmu.edu/me/undergraduate/ugcourses.html",
										"http://www.registrar.ucla.edu/schedule/catalog.aspx?sa=MECH%26AE&funsel=3",
										"http://www.ucsd.edu/catalog/courses/MAE.html",
										"http://ame-www.usc.edu/classes/classes_all.shtml",
										"http://www.enme.umd.edu/undergrad/courses/core.html",
										"http://www.enme.umd.edu/undergrad/courses/electives.html"};
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/G5ModulesDB","root","pass@123");
			
			for(int i = 0; i < urls.length; i ++)
			{
				preparedStatement = connect.prepareStatement("insert into documents values ('"+urls[i]+"','Mechanical',0,0)", Statement.RETURN_GENERATED_KEYS);
				preparedStatement.executeUpdate();
			}
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			close();
		}
	}
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}

			if (preparedStatement != null) {
				preparedStatement.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
