package extraction;



import utils.RDFTriplet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Vocabulary {
    public static class PredicateObject{

        public String predicate;
        public String object;
        public PredicateObject(String p,String o){
            predicate=p;
            object=o;

        }
    }
    public Vocabulary(Map<String, Map<String,PredicateObject>> map){
        this.map=map;
    }
    public Vocabulary(){
        this.map=read();

    }
    Map<String, Map<String,PredicateObject>> map = new HashMap<String,  Map<String,PredicateObject>>();
    public void addVocabulary(ArrayList<String> domains, RDFTriplet rdfTriplet){
        PredicateObject po =new PredicateObject(rdfTriplet.getPredicate(),rdfTriplet.getObject());
        for(String domain:domains){
            if(!map.get(domain).containsKey(rdfTriplet.getSubject())){
                map.get(domain).put(rdfTriplet.getSubject(),po);
            }
        }
        write(map);
    }
    public void removeVocabulary(ArrayList<String> domains, RDFTriplet rdfTriplet){
        for(String domain:domains) {
            map.get(domain).remove(rdfTriplet.getSubject());
        }
        write(map);

    }

    public void write(Map<String, Map<String,PredicateObject>> map){
        //write to file : "fileone"
        try{
            File fileOne=new File("fileone.txt");
            FileOutputStream fos=new FileOutputStream(fileOne);
            PrintWriter pw=new PrintWriter(fos);

            for(Map.Entry<String,  Map<String,PredicateObject>> m :map.entrySet()){
                for(Map.Entry<String, PredicateObject> m2 :m.getValue().entrySet()) {
                    pw.println(m.getKey() + "=" + m2.getKey() + "=" +m2.getValue().predicate + "=" +m2.getValue().object );
                }
            }

            pw.flush();
            pw.close();
            fos.close();
        }catch(Exception e){
        	e.printStackTrace();
        }



    }
    public Map read(){
        Map<String,  Map<String,PredicateObject>> mapInFile = new HashMap<String,  Map<String,PredicateObject>>();
        try {
            File toRead = new File("/home/mzemroun/Desktop/cps2-ir-1/fileone.txt");
            FileInputStream fis = new FileInputStream(toRead);
            Scanner sc=new Scanner(fis);

            String currentLine;
            while(sc.hasNextLine()){
                currentLine=sc.nextLine();
                //now tokenize the currentLine:
                StringTokenizer st=new StringTokenizer(currentLine,"=",false);
                //put tokens ot currentLine in map
                String domain=st.nextToken();
                if(!mapInFile.containsKey(domain)) {
                    mapInFile.put(domain,new HashMap<String,PredicateObject>());
                    mapInFile.get(domain).put(st.nextToken(),new PredicateObject( st.nextToken(), st.nextToken()));
                }else{
                    String subject=st.nextToken();
                    if(!mapInFile.get(domain).containsKey(subject))
                        mapInFile.get(domain).put(subject,new PredicateObject( st.nextToken(), st.nextToken()));
                }
            }
            fis.close();
            //print All data in MAP
            /*for (Map.Entry<String, PredicateObject> m : mapInFile.entrySet()) {
                System.out.println(m.getKey() + " : " + m.getValue().predicate);
            }*/
        } catch (Exception e) {
        }
        return mapInFile;
    }
    public ArrayList<String> getDomains(){
        ArrayList<String> domains =new ArrayList<String>(map.keySet());
        return domains;
    }

}
