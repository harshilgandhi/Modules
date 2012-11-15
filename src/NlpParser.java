import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.SimpleTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraph;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;




public class NlpParser {

	private static LexicalizedParser lexParser;
	List<String> returnList = new ArrayList<String>();
	List<String> realReturnList = new ArrayList<String>();
	List<String> finalReturnList = new ArrayList<String>();
	
	static {
		lexParser = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	}
	
	public List<String> getPotentialModules(String inputSentences)
	{
		
		Main.countDescParsed ++;
	    System.out.println("\n\n\n" + Main.countDescParsed + " COURSES PARSED\n\n");
		
		Tree parse = lexParser.apply(inputSentences);
	    
//	    System.out.println(treeGraph.getNodeByIndex(11).toString());
//	    System.out.println(treeGraph.getNodeByIndex(3).pennString());

	    for (Tree subtree : parse) { 
	        if (subtree.label().value().equals("NP")) {
//	            System.out.println("-->"+subtree);
	            returnList.add(subtree.toString());
	        }
	    }
	    System.err.println(returnList.size());
//	    for(String s : returnList)
//	    {
//	    	String substring[] = s.split(")"); 
//	    }
	    
	    for(String s : returnList)
	    {

	    	
	    	
	    	String substr[] = s.split("NP");
			if(substr.length>2)
				continue;
	    	
			System.out.println(s);
			  for(int i=1;i<substr.length;i++)
			  {// System.out.println(substr[i]);
				 // System.out.println("--->"+substr[i].charAt(1));
				  if(substr[i].matches("\\s\\((.*)\\)\\)(.*)"))
					  realReturnList.add(substr[i]);
			  }
			  for(int i=0;i<realReturnList.size();i++)
			  {
				 //System.out.println(list.get(i));
				  String interstr = realReturnList.get(i);
				  int lnum=0, rnum=0;
				  int index=0;
				  boolean match=false;
				  for(int j =0; j<interstr.length();j++)
				  {
					  if(interstr.charAt(j)=='(')
						  lnum++;
					  if(interstr.charAt(j)==')')
						  rnum++;
					  if(rnum>lnum)
					  {
						  index = j;
						  match=true;
						  break;
					  }
				  }
				  if(match==false)
				  {
					 realReturnList.remove(i); 
					 i--;
				  }
				  else 
			      { 
					 substr=interstr.substring(1, index).split("\\s");
					 ArrayList sublist = new ArrayList();
					 for(int k=0;k<substr.length;k++)
					 {
						 if(substr[k].charAt(0)!='(')
						 {
							
							 sublist.add (substr[k].substring(0, substr[k].indexOf(')')));
						 }
					 }
					 String np = new String("");
					 for(int k=0;k<sublist.size();k++)
					 {
						 np=np.concat(" ").concat((String) sublist.get(k));
					 }
					 realReturnList.set(i,np);
					// System.out.println(list.get(i));
			      }
			  }
			  
//			  for(int i=0;i<realReturnList.size();i++)
//			  {
//				  System.out.println(realReturnList.get(i));
//			  }
	    	
	    	
			  for(String str : realReturnList)
			  {
				  finalReturnList.add(str);
			  }
	    	
	    	
	    }
	    
	return finalReturnList;
	}
	
	public static void demoDP(LexicalizedParser lp, String filename) {
	    // This option shows loading and sentence-segment and tokenizing
	    // a file using DocumentPreprocessor
	    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	    // You could also create a tokenizer here (as below) and pass it
	    // to DocumentPreprocessor
	    for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
	      Tree parse = lp.apply(sentence);
	      parse.pennPrint();
	      System.out.println();

	      GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	      Collection tdl = gs.typedDependenciesCCprocessed(true);
	      System.out.println(tdl);
	      System.out.println();
	    }
	
	}
}
