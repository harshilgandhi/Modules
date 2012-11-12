package db;

import java.io.IOException;
import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LinkQueue {

	LinkedList<Element> links;	
	Document doc;

	public LinkQueue(String url){
		try {
			doc = Jsoup.connect(url).get();
			links = new LinkedList<Element>(doc.select("a[href]"));
		} catch (IOException e) {
			System.err.println("Error while loading webpage with Jsoup...");
			e.printStackTrace();
		}
	}

	public String getNext(){
		if(links.size() == 0) return null;
		else return links.pollFirst().attr("abs:href");
	}

	public void print() {
		for (Element link : links){
			System.out.println(link.attr("abs:href"));
		}
	}

}
