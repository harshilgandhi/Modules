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
	}
	
	public int getWordCount(String word, String dept)
	{
		int wordCount = -1;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/G5ModulesDB","root","pass@123");
			
			preparedStatement = connect.prepareStatement("select sum(count) from Terms where word = '"+word+"' and docId in (select docId from Documents where Department = '"+dept+"');", Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
			{
				wordCount = resultSet.getInt(1);
			}
			resultSet.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			close();
		}
		System.err.println("wordCount" + wordCount);
		return wordCount;
	}
	
	public int getWordDocFreq(String word, String dept)
	{
		int docFreq = -1;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/G5ModulesDB","root","pass@123");
			
			preparedStatement = connect.prepareStatement("select count(*) from Documents where Department = '"+dept+"' and docId in (select docId from Terms where word = '"+word+"');", Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
			{
				docFreq = resultSet.getInt(1);
			}
			resultSet.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			close();
		}
		System.err.println("docFreq" + docFreq);
		return docFreq;
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
