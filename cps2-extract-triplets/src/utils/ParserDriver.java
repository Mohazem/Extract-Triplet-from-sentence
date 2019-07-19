package utils;

import java.util.ArrayList;

import parsers.AbstractParser;

public class ParserDriver {
	private AbstractParser strategy;
	
	private RDFTriplet triplet;
	private ArrayList<RDFTriplet> tripletsList;
	
	public ParserDriver(AbstractParser strategy) {
		// TODO Auto-generated constructor stub
		this.strategy = strategy;
	}
	
	public void executeStrategy(ETInput et_input) {
		
		//call the execute on the strategy given by the constructor
		
		strategy.execute(et_input);
		triplet = AbstractParser.getTriplet_result() ; 
		tripletsList = AbstractParser.getTripletsList();
	}

	public RDFTriplet getTriplet() {
		return triplet;
	}

	public ArrayList<RDFTriplet> getTripletsList() {
		return tripletsList;
	}
	
	
}
