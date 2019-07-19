package parsers;

import utils.ETInput;
import utils.RDFTriplet;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils.Property;
import extraction.ExtractMultipleTriplets;
import extraction.ExtractionServiceStanford;

public class StanfordNLParser extends AbstractParser{

	private int sentenceIndex;

	//could implement other constructors if needed to accept some parameters
	private StanfordCoreNLP pipeline;
	Properties props;
	
	public StanfordNLParser(int index) {
		// TODO Auto-generated constructor stub
		sentenceIndex=index;
		
		// init pipeline with some properties for the parser
		
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, parse");
//		pipeline =  new StanfordCoreNLP(props);
	}
	private final static String PCG_MODEL = "/home/mzemroun/Desktop/cps2-ir-1/cps2-extract-triplets/src/parsers/src2/edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";

	private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");

	private final LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);

	public Tree parse(String str) {
		List<CoreLabel> tokens = tokenize(str);
		Tree tree = parser.apply(tokens);
		return tree;
	}
	
	public void parse2(String str) {
		List<CoreMap> sentences = annotate(str);

		ExtractionServiceStanford extractionService = new ExtractionServiceStanford();
		
		for (CoreMap sentence : sentences) {
			Tree tree = sentence.get(TreeAnnotation.class);
			RDFTriplet triplet = extractionService.extractTriplet(tree);
			
			System.out.println(triplet);
		}
	}

	protected List<CoreMap> annotate(String entryString) {
		Annotation document = new Annotation(entryString);
	   
	    pipeline.annotate(document);
	    
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    return sentences;
	}
	
	private List<CoreLabel> tokenize(String str) {
		Tokenizer<CoreLabel> tokenizer =
				tokenizerFactory.getTokenizer(
						new StringReader(str));
		return tokenizer.tokenize();
	}
	private String removePoint(String str) {
		String res = str.replace(".", "");
		return res;
	}
	@Override
	public void execute(ETInput et_input) {
		// TODO Auto-generated method stub
		System.out.println("Parser Started ....");

		//work to be done using functions
		//I chose 1 as an index but we can modify it when we chose the sentence

		StanfordNLParser parser = new StanfordNLParser(sentenceIndex);
		String str = et_input.getSentences().get(sentenceIndex);
		str = removePoint(str);

		Tree tree = parser.parse(str);

		ExtractionServiceStanford extractionService = new ExtractionServiceStanford();
		RDFTriplet triplet = extractionService.extractTriplet(tree);

		
		System.out.println(triplet);
		
		List<Tree> leaves = tree.getLeaves();
		// Print words and Pos Tags
		for (Tree leaf : leaves) {
			Tree parent = leaf.parent(tree);
			System.out.print(leaf.label().value() + "->" + parent.label().value() + " ");
		}
		System.out.println("\n***********");
		ArrayList<String> domain = new ArrayList<>();
		domain.add("politics");
		domain.add("economy");
		domain.add("entertainment");
		ExtractMultipleTriplets ex =new ExtractMultipleTriplets();

		setTripletList(ex.generateTriplets(domain,triplet));
	}
	public static void main(String[] args) {
		StanfordNLParser parser = new StanfordNLParser(0);
		//String n="Trump is a president";
		String n="Ali know Trump";
		ArrayList<String> sentences= new ArrayList<>();
		sentences.add(n);
		ETInput list=new ETInput(sentences);
		parser.execute(list);
		
	}


}
