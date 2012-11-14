import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class DatabaseLookup {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public DatabaseLookup() {

		try{
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/G5ModulesDB","root","pass@123");
			
			preparedStatement = connect.prepareStatement("select count(*) from G5ModulesDB.Terms", Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next())
			{
				System.out.println("================>" + resultSet.getString(1));
				System.out.println("================>" + resultSet.getString(2));
				System.out.println("================>" + resultSet.getString(3));
				System.out.println("================>" + resultSet.getString(4));
			}
			resultSet.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public int updateDocument(String url, String department, int size, int words){
		int newId = -1;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/G5ModulesDB","root","pass@123");
			preparedStatement = connect.prepareStatement("insert into  Module.Documents values (default, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, url);
			preparedStatement.setString(2, department);
			preparedStatement.setInt(3, size);
			preparedStatement.setInt(4, words);
			preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) newId = resultSet.getInt(1);
			resultSet.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return newId;
	}

	public void updateTerm(int id, String key, Integer value){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/G5ModulesDB","root","pass@123");    
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("insert into  Module.Terms values (default, ?, ?, ?)");
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, key);
			preparedStatement.setInt(3, value);
			preparedStatement.executeUpdate();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
