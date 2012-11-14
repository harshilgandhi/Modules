package db;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map.Entry;
//modified from http://www.vogella.com/articles/MySQLJava/article.html
public class SQL {


	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;


	public String toCSV(int id, Hashtable<String, Integer> table) {
		StringBuilder builder = new StringBuilder();

		for (Entry<String, Integer> entry : table.entrySet()) {
			builder.append(id + "," +  entry.getKey() + "," +entry.getValue() + "\n");
		}
		return builder.toString();
	}

	public void write(String fileName, String csv){
		try{
			// Create file 
			FileWriter fstream = new FileWriter(fileName, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(csv);
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
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
		finally{
			close();
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
		finally{
			close();
		}
	}

	public void batchUpdate(int id, Hashtable<String, Integer> table) {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/G5ModulesDB","root","pass@123");    
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("insert into  Module.Terms values (default, ?, ?, ?)");

			int i = 0;
			for (Entry<String, Integer> entry : table.entrySet()) {
				preparedStatement.setInt(1, id);
				preparedStatement.setString(2, entry.getKey());
				preparedStatement.setInt(3, entry.getValue());
				preparedStatement.addBatch();
				if ((i + 1) % 100 == 0) {
					preparedStatement.executeBatch(); // Execute every 1000 items.
				}
				i++;
			}
			preparedStatement.executeUpdate();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			close();
		}
	}

	// You need to close the resultSet
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

		}
	}

}