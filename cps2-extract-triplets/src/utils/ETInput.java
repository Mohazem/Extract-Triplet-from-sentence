package utils;

import java.util.ArrayList;

public class ETInput {
	private ArrayList<String> sentences;
	private String voacbDomain;
	
	public ETInput(ArrayList<String> sentences) {
		// TODO Auto-generated constructor stub
		this.sentences = sentences;
	}
	public ETInput(ArrayList<String> sentences,String vocabDomain) {
		// TODO Auto-generated constructor stub
		this.sentences = sentences;
		this.voacbDomain = vocabDomain;
	}
	
	// to access the list of sentences
	public ArrayList<String> getSentences() {
		return sentences;
	}
	
	//to add sentence to the list
	private void addSentence(String newSentence) {

		this.sentences.add(newSentence);
	}
	public String getVoacbDomain() {
		return voacbDomain;
	}
	public void setVoacbDomain(String voacbDomain) {
		this.voacbDomain = voacbDomain;
	}
}
