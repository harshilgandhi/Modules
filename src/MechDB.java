import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MechDB {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private HashMap<String,List<Integer>> terms = new HashMap<String,List<Integer>>();
	
	public static void main(String[] arg)
	{
		MechDB object = new MechDB();
		object.populateDB();
	}
	
	public void populateDB()
	{
		
//		try{
//			Class.forName("com.mysql.jdbc.Driver");
//			connect = DriverManager.getConnection("jdbc:mysql://localhost/G5ModulesDB","root","pass@123");
//			preparedStatement = connect.prepareStatement("delete from terms where ", Statement.RETURN_GENERATED_KEYS);
//			preparedStatement.executeUpdate();
//			delete from terms where docId not in (select docId from documents where Department = 'Mechanical');
//		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
//		finally{
//			close();
//		}
		
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
			int docTableSize = 0;
//			for(int i = 0; i < urls.length; i ++)
//			{
//				preparedStatement = connect.prepareStatement("insert into documents values (default,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
//				preparedStatement.setString(1, urls[i]);
//				preparedStatement.setString(2, "Mechanical");
//				preparedStatement.setInt(3, 0);
//				preparedStatement.setInt(4, 0);
//				preparedStatement.executeUpdate();
//				ResultSet resultSet = preparedStatement.getGeneratedKeys();
//				if (resultSet.next())
//					System.out.println(resultSet.getInt(1));
//				System.out.println("Added Doc:" + urls[i]);
				preparedStatement = connect.prepareStatement("select count(*) from Documents", Statement.RETURN_GENERATED_KEYS);
				resultSet = preparedStatement.executeQuery();
				
				if (resultSet.next())
				{
					docTableSize = resultSet.getInt(1);
				}
				for(int ip = 0; ip < urls.length; ip ++)
				{
					Document htmlDoc = Jsoup.connect(urls[ip]).get();
					Elements e = htmlDoc.getElementsByTag("body");
					String docSentences = e.get(0).text();
					String[] docWords = docSentences.split("[-*=(){} !@#$%&_+=~`;:<>,.|/?\"\\^]");
					for(String a : docWords)
					{
						List<Integer> list;
						if(terms.containsKey(a))
						{
							list = (List<Integer>)terms.get(a);
							list.add(ip + 1 + docTableSize);
							terms.remove(a);
							terms.put(a, list);
						}
						else
						{
							list = new ArrayList<Integer>();
							list.add(ip + 1 + docTableSize);
							terms.put(a, list);
						}
					}
				}
			//}//equivalent, practical, issue, issues
			Iterator<Entry<String, List<Integer>>> iterator = terms.entrySet().iterator();
			while(iterator.hasNext())
			{
				Entry<String, List<Integer>> entry = iterator.next();
				List<Integer> tempList = entry.getValue();
				for(int i = 0; i < tempList.size(); i ++)
				{
					preparedStatement = connect.prepareStatement("insert into terms values (default,?,?,?)", Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setInt(1, tempList.get(i));
					preparedStatement.setString(2, entry.getKey());
					
					preparedStatement.setInt(3, (tempList.get(i))/(tempList.size()));
//					preparedStatement.executeUpdate();
//					ResultSet resultSet = preparedStatement.getGeneratedKeys();
//					if (resultSet.next())
//						System.out.println(resultSet.getInt(1));
					System.out.println("Added Term:" + entry.getKey() + " to doc:"+tempList.get(i)+"...count"+tempList.size());
				}
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
