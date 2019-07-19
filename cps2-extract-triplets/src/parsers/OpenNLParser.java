package parsers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import extraction.ExtractMultipleTriplets;
import extraction.ExtractionServiceStanford;
import extraction.OpenNLPExtractionService;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.util.InvalidFormatException;
import utils.ETInput;
import utils.RDFTriplet;

public class OpenNLParser extends AbstractParser {
	private int sentenceIndex;

	public OpenNLParser(int index) {
		sentenceIndex = index;
	}

	private String removePoint(String str) {
		String res = str.replace(".", "");
		return res;
	}

	public opennlp.tools.parser.Parse[] Parse(String str) throws InvalidFormatException, IOException {
		// http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
		InputStream is = new FileInputStream("en-parser-chunking.bin");

		ParserModel model = new ParserModel(is);
		Parser parser = ParserFactory.create(model);

		opennlp.tools.parser.Parse topParses[] = ParserTool.parseLine(str, parser, 1);
		for (opennlp.tools.parser.Parse p : topParses)
			p.show();
		is.close();
		return topParses;
	}

	@Override
	public void execute(ETInput et_input) {
		System.out.println("OpenNLP Parser Started ....");

		String str = et_input.getSentences().get(sentenceIndex);
		str = removePoint(str);

		OpenNLParser parser = new OpenNLParser(sentenceIndex);

		try {
			opennlp.tools.parser.Parse[] parse = parser.Parse(str);
			OpenNLPExtractionService extractionService = new OpenNLPExtractionService();
			//RDFTriplet triplet = extractionService.extractTriplet(parse);
			Set<RDFTriplet> triplets = extractionService.extractTriplet(parse);

			setTripletListFromSet(triplets);
//			setTriplet_result(triplet);
////			System.out.println("yes open :"+ triplet);
//			ExtractMultipleTriplets ex = new ExtractMultipleTriplets();
//			setTripletList(ex.generateTriplets(et_input.getVoacbDomain(),triplet));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
