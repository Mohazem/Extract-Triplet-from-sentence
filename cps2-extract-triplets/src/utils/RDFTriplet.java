package utils;

public class RDFTriplet {
	private String subject;
	private String predicate;
	private String object;
	private Boolean fromParser=false;

	private boolean vaildated;

	public RDFTriplet(String subject, String predicate, String object) {
		// TODO Auto-generated constructor stub
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;

	}

	public boolean isValidated() {
		return this.vaildated;
	}

	public void setValidated(boolean b) {
		this.vaildated = b;
	}

	public String getObject() {
		return object;
	}

	public String getPredicate() {
		return predicate;
	}

	public String getSubject() {
		return subject;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Boolean getFromParser() {
		return fromParser;
	}

	public void setFromParser(Boolean fromParser) {
		this.fromParser = fromParser;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		System.out.println( "Subject: " + this.subject + "\nPredicate: " + this.predicate + "\nObject: " + object);
		return "Subject: " + this.subject + "\nPredicate: " + this.predicate + "\nObject: " + object;
		
	}
	@Override
	public boolean equals(Object obj) {
		RDFTriplet triplet =(RDFTriplet)obj;
		if(this.subject.equals(triplet.subject) && this.predicate.equals(triplet.predicate) && this.object.equals(triplet.object))
			return true;
		return false;
	}
}
