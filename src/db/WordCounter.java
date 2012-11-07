package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

public class WordCounter {
	
	File directory;
	
	public WordCounter(File directory){
		this.directory = directory;
		if(!directory.isDirectory()) throw new Error("Not a directory!");
	}
	
	public void countAll() throws Exception{
		SQL mysql = new SQL();
		for(File f : directory.listFiles()){
			FileReader fReader = new FileReader(f);
			BufferedReader bReader = new BufferedReader(fReader);
			System.out.println(f.getCanonicalPath());
			String url;
			while((url = bReader.readLine()) != null){
				count(url, f.getName(), mysql);
			}
			bReader.close();
		}
	}
	
	private Hashtable<String,Integer> count(String url, String fileName, SQL mysql) throws Exception{
		System.out.println(url);
		Hashtable<String,Integer> table = GetWordsLynx.runLynx(url);
		//Set<String> table = GetWordsLynx2.runLynx(url);
		int id = mysql.updateDocument(url, fileName, 0, table.size());
		System.out.println("Inserted url #" + id + " (" + url + ") Count:" + table.size());
		//mysql.batchUpdate(id, table);
		mysql.write("data", mysql.toCSV(id, table));
		
		return table;
	}
	
}
