package misc;

import utils.RDFTriplet;

import java.util.*;
import java.io.*;

public class Test {

	static class PO{
		public String predict;
		public String object;
		public PO(String p,String o){
			predict=p;
			object=o;
		}
	}
	static void write(Map<String,PO> map){
		//write to file : "fileone"
		try{
			File fileOne=new File("fileone.txt");
			FileOutputStream fos=new FileOutputStream(fileOne);
			PrintWriter pw=new PrintWriter(fos);

			for(Map.Entry<String,PO> m :map.entrySet()){
				pw.println(m.getKey()+"="+m.getValue().predict+"="+m.getValue().object);
			}

			pw.flush();
			pw.close();
			fos.close();
		}catch(Exception e){}



	}
	static void read(){
		Map<String, PO> mapInFile = null;
		try {
			File toRead = new File("fileone.txt");
			FileInputStream fis = new FileInputStream(toRead);
			Scanner sc=new Scanner(fis);

			String currentLine;
			while(sc.hasNextLine()){
				currentLine=sc.nextLine();
				//now tokenize the currentLine:
				StringTokenizer st=new StringTokenizer(currentLine,"=",false);
				//put tokens ot currentLine in map
				System.out.println(st.nextToken() + " : " + st.nextToken()+" : " + st.nextToken());
				mapInFile.put(st.nextToken(),new PO(st.nextToken(),st.nextToken()));
			}
			fis.close();
			//print All data in MAP
			for (Map.Entry<String, PO> m : mapInFile.entrySet()) {
				System.out.println(m.getKey() + " : " + m.getValue().predict);
			}
		} catch (Exception e) {
		}

	}
	public static void main(String[] args) {
		Map<String, PO> map = new HashMap<String, PO>();
		PO p1 =new PO("is a","president");
		PO p2 =new PO("is a","human");
		PO p3 =new PO("kind of","country");
		map.put("macron",p1);
		map.put("president",p2);
		//map.put("macron",p2);
		map.put("france",p3);
		write(map);
		RDFTriplet test = new RDFTriplet("ali","live in","france");
		ArrayList<RDFTriplet> rdf_triplets=new ArrayList<>();
		//rdf_triplets.add(test);
		if(!map.containsKey(test.getSubject())){
			map.put(test.getSubject(),new PO(test.getPredicate(),test.getObject()));
			rdf_triplets.add(test);
			write(map);
		}
		else if(map.get(test.getSubject()).equals(new PO(test.getPredicate(),test.getObject()))){
			rdf_triplets.add(test);
		}else{
			rdf_triplets.add(new RDFTriplet(test.getSubject(),map.get(test.getSubject()).predict,map.get(test.getSubject()).object));
		}
		if(map.get(test.getObject())!=null){
			rdf_triplets.add(new RDFTriplet(test.getObject(),map.get(test.getObject()).predict,map.get(test.getObject()).object));
		}
		for (int i=0;i<rdf_triplets.size();i++){
			System.out.println(rdf_triplets.get(i).toString());
		}
		System.out.println("****************************");
		read();

	}
}
