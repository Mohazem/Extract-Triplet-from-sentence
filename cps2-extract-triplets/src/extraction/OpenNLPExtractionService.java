package extraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



import opennlp.tools.parser.Parse;
import opennlp.tools.util.Span;
import utils.RDFTriplet;

public class OpenNLPExtractionService {
	static Set<RDFTriplet> nounPhrases = new HashSet<>();
	private static final String LABEL_TOP = "TOP";
	private static final String LABEL_SENTENCE = "S";
	private static final String LABEL_NOUN_PHRASE = "NP";
	private static final String LABEL_VERBAL_PHRASE = "VP";

	private static final String LABEL_NAME_PREFIX = "NN";
	private static final String LABEL_VERB_PREFIX = "VB";

	public OpenNLPExtractionService() {
	}

//	public Set<RDFTriplet> extractTriplet(Parse[] parse) {
//		for (Parse p : parse)
//			getTriplet(p);
////		String subject= OpenNLPExtractionService.getSubject(parse);
////		String predicate= OpenNLPExtractionService.getPredicate(parse);
////		String object= OpenNLPExtractionService.getObject(parse);
////		System.out.println("subject: "+subject+", predicate: "+predicate+", object: "+object);
////		RDFTriplet triplet = new RDFTriplet(subject, predicate, object);
//
//		return nounPhrases;
//	}

	public Set<RDFTriplet> extractTriplet(Parse[] parse) {
		nounPhrases.clear();
		getBestTriplet(parse[0]);
		for (Parse p : parse)
			getTriplet(p);
		return nounPhrases;
	}

	public static void getBestTriplet(Parse p) {
		String subject = OpenNLPExtractionService.getSubject(p);
		String predicate = OpenNLPExtractionService.getPredicate(p);
		String object = OpenNLPExtractionService.getObject(p);
		System.out.println("subject: " + subject + ", predicate: " + predicate + ", object: " + object);
		if (subject != null && !subject.isEmpty() && predicate != null && !predicate.isEmpty() && object != null
				&& !object.isEmpty()) {
			RDFTriplet triplet = new RDFTriplet(subject, predicate, object);
//			if (!(nounPhrases.contains(triplet))) {
//				nounPhrases.add(triplet);
//			}
			boolean b = true;
			for (RDFTriplet triplet1 : nounPhrases) {
				if (triplet1.equals(triplet)) {
					b = false;
					break;
				}
			}if(b) {
				nounPhrases.add(triplet);
			}

		}
	}

	public static void getTriplet(Parse p) {
		String subject = "";
		String predicate = "";
		String object = "";

		String[] verbs = new String[] { "VBP", "VB", "VBD", "VBG", "VBN", "VBZ" };
		List<String> verbsList = Arrays.asList(verbs);

		String[] nouns = new String[] { "NN", "PP", "PP$", "WP", "WP$", "NNS", "NP", "NPS", "PRP" };
		List<String> nounsList = Arrays.asList(nouns);

		ArrayList<String> subjectList = new ArrayList<String>();
		ArrayList<String> objectList = new ArrayList<String>();

		int noOfChildern = p.getChildCount();
		Parse childern[] = p.getChildren();
		if (noOfChildern >= 2) {

			if (childern[0].getType().equals("NP")) {
				subjectList = processNP(childern[0]);
				if (childern[1].getType().equals("VP")) {
					int noOfnextChildren = childern[1].getChildCount();
					Parse nextChildren[] = childern[1].getChildren();
					if (noOfnextChildren >= 2) {
						if (verbsList.contains(nextChildren[0].getType())) {
							predicate = nextChildren[0].getCoveredText();
							System.out.println(predicate);
							;
							for (int i = 1; i < noOfnextChildren; i++) {
								if (nextChildren[i].getType().equals("NP")) {
									objectList = processNP(nextChildren[i]);

								}
								if (nounsList.contains(nextChildren[i].getType())) {
									object = nextChildren[i].getCoveredText();
									objectList.add(object);
								} else {
									object = nextChildren[i].getCoveredText();
									objectList.add(object);

								}
							}
						}
					}
				}
			}
			System.out.println("sU: " + subjectList.size());
			System.out.println("ob: " + objectList.size());
			for (int k = 0; k < subjectList.size(); k++) {
				subject = subjectList.get(k);
				for (int m = 0; m < objectList.size(); m++) {
					object = objectList.get(m);
					System.out.println("--Subject: " + subject + "\nPredicate: " + predicate + "\nObject: " + object);
					RDFTriplet triplet = new RDFTriplet(subject, predicate, object);
//					if (!(nounPhrases.contains(triplet))) {
//						nounPhrases.add(triplet);
//					}
					
					boolean b = true;
					for (RDFTriplet triplet1 : nounPhrases) {
						if (triplet1.equals(triplet)) {
							b = false;
							break;
						}
					}if(b) {
						nounPhrases.add(triplet);
					}
				}

			}
		}
		for (Parse child : p.getChildren()) {
			System.out.println("start: " + child.getText());
			System.out.println("start type: " + child.getType());
			getTriplet(child);
		}

	}

	public static ArrayList<String> processNP(Parse p) {
		ArrayList<String> subjectList = new ArrayList<String>();
		String[] nouns = new String[] { "NN", "PP", "PP$", "WP", "WP$", "NNS", "NP", "NPS", "PRP" };
		List<String> nounsList = Arrays.asList(nouns);

		String subject = p.getCoveredText();
		subjectList.add(subject);

		int noOfChildren = p.getChildCount();
		Parse children[] = p.getChildren();
		for (int j = 0; j < noOfChildren; j++) {
			if (nounsList.contains(children[j].getType())) {
				subject = children[j].getCoveredText();
				if (!(subjectList.contains(subject))) {
					subjectList.add(subject);
				}
			}

		}

		return subjectList;
	}

	public static ArrayList<String> processNPFroObject(Parse p) {
		System.out.println("this: " + p.getText());
		ArrayList<String> objectList = new ArrayList<String>();
		String[] nouns = new String[] { "NN", "PP", "PP$", "WP", "WP$", "NNS", "NP", "NPS", "PRP" };
		List<String> nounsList = Arrays.asList(nouns);

		String object = p.getCoveredText();
		objectList.add(object);

		int noOfChildren = p.getChildCount();
		Parse children[] = p.getChildren();

		for (int j = 0; j < noOfChildren; j++) {
			if (nounsList.contains(children[j].getType())) {
				object = children[j].getCoveredText();
				objectList.add(object);
			}

		}
//		for (int j = 0; j < objectList.size(); j++) {
//			System.out.println("----kllp-"+objectList.get(j));
//
//		}
		return objectList;
	}

	public static String getSubject(final Parse parse) {
		if (((opennlp.tools.parser.Parse) parse).getType().equals(LABEL_TOP)) {
			return getSubject(((opennlp.tools.parser.Parse) parse).getChildren()[0]);
		}

		if (parse.getType().equals(LABEL_SENTENCE)) {
			for (Parse child : parse.getChildren()) {
				if (child.getType().equals(LABEL_NOUN_PHRASE)) {
					return getSubject(child);
				}
			}
		}
		if (parse.getType().equals(LABEL_NOUN_PHRASE)) {
			return getFirstOccurenceForType(parse, LABEL_NAME_PREFIX);
		}

		return "";
	}

	public static String getPredicate(final Parse parse) {
		if (parse.getType().equals(LABEL_TOP)) {
			return getPredicate(parse.getChildren()[0]);
		}

		if (parse.getType().equals(LABEL_SENTENCE)) {
			for (Parse child : parse.getChildren()) {
				if (child.getType().equals(LABEL_VERBAL_PHRASE)) {
					return getPredicate(child);
				}
			}
			return "";
		}
		if (parse.getType().equals(LABEL_VERBAL_PHRASE)) {
			return getFirstOccurenceForType(parse, LABEL_VERB_PREFIX);
		}

		return "";
	}

	public static String getObject(final Parse parse) {
		String object = "";
		if (parse.getType().equals(LABEL_TOP)) {
			return getObject(parse.getChildren()[0]);
		}

		if (parse.getType().equals(LABEL_SENTENCE)) {
			for (Parse child : parse.getChildren()) {
				if (child.getType().equals(LABEL_VERBAL_PHRASE)) {
					object = getObject(child);
					if (!object.isEmpty()) {
						return object;
					}
				}
			}
			return object;
		}
		if (parse.getType().equals(LABEL_VERBAL_PHRASE)) {
			return getFirstOccurenceForType(parse, LABEL_NAME_PREFIX);
		}

		return object;
	}

	private static String getFirstOccurenceForType(final Parse parse, final String typePrefix) {

		// For now we are only checking the prefix

		// check current
		if (parse.getType().length() > 1 && parse.getType().substring(0, 2).equals(typePrefix)) {
			Span span = parse.getSpan();
			String text = parse.getText().substring(span.getStart(), span.getEnd());
			return text;
		}

		if (parse.getType().equals("PRP")) {
			Span span = parse.getSpan();
			String text = parse.getText().substring(span.getStart(), span.getEnd());
			return text;
		}

		// check children (breadth)
		for (Parse child : parse.getChildren()) {
			if (child.getType().length() > 1 && child.getType().substring(0, 2).equals(typePrefix)) {
				Span span = child.getSpan();
				String text = child.getText().substring(span.getStart(), span.getEnd());
				if (!text.isEmpty())
					return text;
			}
		}

		// recursively check for children (deep)
		for (Parse child : parse.getChildren()) {
			String text = getFirstOccurenceForType(child, typePrefix);
			if (!text.isEmpty())
				return text;
		}

		return "";
	}

}
