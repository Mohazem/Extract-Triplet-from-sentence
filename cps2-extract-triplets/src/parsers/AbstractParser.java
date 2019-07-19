package parsers;


import java.util.ArrayList;
import java.util.Set;

import utils.ETInput;
import utils.RDFTriplet;

public abstract class AbstractParser {
	
	
	private static RDFTriplet triplet_result;
	
	private static ArrayList<RDFTriplet> tripletsList;
	
	public abstract void execute(ETInput et_input);

	public static RDFTriplet getTriplet_result() {
		return triplet_result;
	}

	public static void setTriplet_result(RDFTriplet triplet_result) {
		AbstractParser.triplet_result = triplet_result;
	}
	public static void setTripletList(ArrayList<RDFTriplet> rdfTriplets) {
		AbstractParser.tripletsList = new ArrayList<>(rdfTriplets);
	}
	public static ArrayList<RDFTriplet> getTripletsList() {
		return tripletsList;
	}

	public static void setTripletListFromSet(Set<RDFTriplet> rdfTriplets) {
		AbstractParser.tripletsList = new ArrayList<RDFTriplet>(rdfTriplets);
	}
	
}
