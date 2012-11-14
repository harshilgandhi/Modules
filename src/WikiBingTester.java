import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;


public class WikiBingTester {

	private static StringBuffer bingURL;
	private static byte[] accountKeyBytes;
	private static String accountKeyEnc;
	private static URL url;
	private static URLConnection urlConnection;
	private static InputStream inputStream;
	private static String content;
	private static byte[] contentRaw;
	private static org.jsoup.nodes.Document resultDoc;
	private static String accountKey = "";
	
	private static String titleResult[] = new String[5];
	
	public boolean testWikiBing(String testString) throws Exception
	{
		boolean isModule = false;
		//formulate the bingURL according to requirement
		bingURL = new StringBuffer("https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Query=");
		
		String[] testArray = testString.split(" ");
		
		//%27gates%27
		int i;
		bingURL.append("'");
		for ( i = 0; i < testArray.length; i ++)
		{	
			if(i != 0)
				bingURL.append("+");
			if (!testArray[i].equals(""))
			{
				bingURL.append(testArray[i]);
			}
		}
		bingURL.append("'");
	
		//&$top=5&$format=Atom";
		bingURL.append("&$top=5&$format=Atom");
		
		System.out.println("--->" + bingURL + "<---");
		
		//Actual connection between BingSearch++ and Bing
		accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
		accountKeyEnc = new String(accountKeyBytes);

		//Query dispatched to Bing
		url = new URL(bingURL+"");
		urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
	
		//Bing sends 10 results to BingSearch++
		inputStream = (InputStream) urlConnection.getContent();		
		contentRaw = new byte[urlConnection.getContentLength()];
		inputStream.read(contentRaw);
		content = new String(contentRaw);
		
		resultDoc = Jsoup.parse(content,"",Parser.xmlParser());
	
		//System.err.println(resultDoc.toString());
	
		// Extract the Title of the 5 searched items
		i = 0;
		for ( Element entryElements : resultDoc.select("entry"))
		{	
			Elements e = entryElements.getElementsByTag("content");
			titleResult[i] = new String();
			titleResult[i]=new String();
			titleResult[i]=(e.first().children().first().children().get(1).text());
			System.out.println("=====>" + titleResult[i] + "<=====");
			i ++;
		}
		
		for (i = 0; i < titleResult.length; i ++)
		{
			if(titleResult[i].contains("Wikipedia"))
				isModule = true;
		}
		
		return isModule;
	}
}
