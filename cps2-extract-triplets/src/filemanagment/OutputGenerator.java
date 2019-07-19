package filemanagment;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utils.RDFTriplet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputGenerator {
	private ArrayList<RDFTriplet> rdf_triplets;
	private String outFileName;
	//constructor takes input the result of the chosen triplets of each sentence.
	public OutputGenerator(ArrayList<RDFTriplet> rdf_triplets,String outFileName) {
		this.rdf_triplets = new ArrayList<>(rdf_triplets);
		this.outFileName = outFileName;
	}
	
	//function to write the result in a JSON file.
	
	public void write() {
		JSONObject obj = new JSONObject();
		JSONArray triplets = new JSONArray();

		for (RDFTriplet triplet:rdf_triplets) {
			JSONObject sub_obj = new JSONObject();
			sub_obj.put("Subject", triplet.getSubject());
			sub_obj.put("Predicate", triplet.getPredicate());
			sub_obj.put("Object", triplet.getObject());
			triplets.add(sub_obj);


		}
		obj.put("Triplets List",triplets);
		
		try {
		File file = new File("src\\out\\"+ outFileName+".json");
		FileWriter writer = new FileWriter(file);
			writer.write(obj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + obj);
		writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		RDFTriplet test = new RDFTriplet("He","is","cat");
		RDFTriplet test2 = new RDFTriplet("He","is","cat");
		ArrayList<RDFTriplet> rdf_triplets=new ArrayList<>();
		rdf_triplets.add(test);
		rdf_triplets.add(test2);
		OutputGenerator generator=new OutputGenerator(rdf_triplets,"out.txt");
		generator.write();

	}
}
