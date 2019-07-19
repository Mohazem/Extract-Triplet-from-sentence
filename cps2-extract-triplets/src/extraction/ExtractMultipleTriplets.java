package extraction;

import misc.Test;
import utils.RDFTriplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExtractMultipleTriplets {

	public ArrayList<RDFTriplet> generateTriplets(String domain, RDFTriplet rdfTriplet) {

//		  Map<String, ArrayList<Vocabulary.PredicateObject>> map = new HashMap<String,
//		  ArrayList<Vocabulary.PredicateObject>>(); Vocabulary.PredicateObject p1 =new
//		  Vocabulary.PredicateObject("politics","is","president");
//		  Vocabulary.PredicateObject p2 =new
//		  Vocabulary.PredicateObject("entertainment","is a","animator");
//		  Vocabulary.PredicateObject p3 =new
//		  Vocabulary.PredicateObject("economy","kind of","diacritical mark");
//		  Vocabulary.PredicateObject p4 =new
//		  Vocabulary.PredicateObject("economy","kind of","genus_of_molluscs");
//		  ArrayList<Vocabulary.PredicateObject> macron=new
//		  ArrayList<Vocabulary.PredicateObject>();
//		  macron.add(p1);macron.add(p3);macron.add(p4);
//		  ArrayList<Vocabulary.PredicateObject> trump=new
//		  ArrayList<Vocabulary.PredicateObject>(); trump.add(p1);trump.add(p2);
//		  map.put("Macron",macron); map.put("Trump", trump);
//

		Vocabulary vocab = new Vocabulary();
		// vocab.write(map);
		ArrayList<RDFTriplet> rdf_triplets = new ArrayList<>();


		if (vocab.map.containsKey(domain)) {
			if(vocab.map.get(domain).containsKey(rdfTriplet.getSubject())){
				if(!vocab.map.get(domain).get(rdfTriplet.getSubject()).object.equals(rdfTriplet.getObject()))
					rdf_triplets.add(new RDFTriplet(rdfTriplet.getSubject(), vocab.map.get(domain).get(rdfTriplet.getSubject()).predicate, vocab.map.get(domain).get(rdfTriplet.getSubject()).object));
			}
			if(vocab.map.get(domain).containsKey(rdfTriplet.getObject())){
				rdf_triplets.add(new RDFTriplet(rdfTriplet.getObject(), vocab.map.get(domain).get(rdfTriplet.getObject()).predicate, vocab.map.get(domain).get(rdfTriplet.getObject()).object));
			}
		} else {




		}


		for (int i = 0; i < rdf_triplets.size(); i++) {
			System.out.println(rdf_triplets.get(i).toString());
		}
		// vocab.read();
		return rdf_triplets;

	}
	public ArrayList<RDFTriplet> generateTriplets(ArrayList<String> domains, RDFTriplet rdfTriplet) {
		ArrayList<RDFTriplet> rdf_triplets = new ArrayList<>();
		rdfTriplet.setFromParser(true);
		rdf_triplets.add(rdfTriplet);
		for(String domain:domains){
			rdf_triplets.addAll(generateTriplets(domain,rdfTriplet));
		}
		Vocabulary vocab =new Vocabulary();
		vocab.addVocabulary(domains,rdfTriplet);
		return rdf_triplets;
	}

}
