import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;


public class Main
{
	public static void main(String [ ] args) throws InvalidFormatException, IOException
	{
		SentenceModel sd_model = null;
	  try {
		   sd_model = new SentenceModel(new FileInputStream ("/Users/qishuchen/Desktop/models/en-sent.bin"));
		  } catch (InvalidFormatException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (FileNotFoundException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		  SentenceDetectorME mSD = new SentenceDetectorME(sd_model);
		  
		  String param = "Emphasis on understanding algebraic, numerical and graphical approaches making use of graphing calculators.";
		  String[] sents = mSD.sentDetect(param);
		  for(String sent : sents){
		   
			 //System.out.println(sent);
		   InputStream is = new FileInputStream("/Users/qishuchen/Desktop/models/en-parser-chunking.bin");
		   
		   ParserModel model = new ParserModel(is);
	 
		   Parser parser = ParserFactory.create(model);

		    Parse[] topParses = ParserTool.parseLine(sent, parser, 1);
	 
			for (opennlp.tools.parser.Parse p : topParses)
				p.show();
	 
			is.close();
			//Using regular expression String found = (NP (DT a) (JJ good) (NN sentence.))	 
			//found.split(")") ==> array = [(NP (DT a][ (JJ good][ (NN sentence.][]
			//for(entire array) 
		  
		  }
		  }}

   
