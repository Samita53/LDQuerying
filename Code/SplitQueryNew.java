import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;


import org.apache.commons.lang3.StringUtils;

import com.tenforce.semtech.SPARQLParser.SPARQL.*;
import com.tenforce.semtech.SPARQLParser.SPARQLStatements.*;

/**
*
* @author Samita Bai
*/

public class SplitQueryNew {
	
	


	public static void main(String[] args) throws IOException, InvalidSPARQLException {
		// TODO Auto-generated method stub
		
		
		
		SplitQueryNew sq = new SplitQueryNew();
		 
		
		if (args[0] == null || args[0].trim().isEmpty()) {
	        System.out.println("You need to provide a file containing SPARQL query");
	        return;
	    }else{
	    	File file = new File(args[0]);
	    	String sparqlQuery = sq.ReadFromFile(file);
	    	int [] i = sq.getSeedTPIndex(sparqlQuery);
	    	if(i[0]!= -1 && i[1]!=-1){
	    	System.out.println(sq.getSeedTriplePattern(sparqlQuery, i[0], i[1]));
	    	System.out.println(sq.getSecondQuery(sparqlQuery,i[0],i[1]));
	    } else {System.out.println("This is not a Non-LDaQ query");}
	    	}
	    	   
		
		}
	public String getSeedTriplePattern(String st, int TPindex, int varInd) throws IOException, InvalidSPARQLException{
		
		String spStr[]=getTriples(st);
		String[] prefixes = getPrefixes(st);
		String[] vars = getVariables(st);
		

		String FQ="";
		for(String pre : prefixes) {
			FQ=FQ+pre+"\n";
		}
			
		
		FQ =FQ+"\nSELECT "+vars[varInd] +" WHERE { GRAPH ?g {\n" +spStr[TPindex]+"\n" +"\t}\n"+"}";
		
		
		return FQ;
			
	}
	
	
	
				
public int [] getSeedTPIndex(String st) throws IOException, InvalidSPARQLException{
		
	SplitQueryNew sq = new SplitQueryNew();
		
		String spStr[]=sq.getTriples(st);
		String[] vars = sq.getVariables(st);
		String[] preds =sq.getPredicates();
		int tpIndex=0;
		String var;
		int varInd=0;
		String spTok;
		int [] indexes = new int[2];
		String [] splited = new String[3];
			
		Task:
		for(int i=1; i<spStr.length; i++){
		spTok =spStr[i];
		String checkQM = spTok.substring(0, 2);	
	    if(checkQM.contains("?")){
	    StringTokenizer tokens = new StringTokenizer(spTok, " ");
	    splited = new String[tokens.countTokens()];
	    int index = 0;
	    while(tokens.hasMoreTokens()){
	        splited[index] = tokens.nextToken();
	        //System.out.println(splited[index]);
	        ++index;
	        }
	     
	    
	    
    	String checkObj = splited[2].substring(0, 1); 
    	
    	
	        	for(int j=0; j<preds.length;j++){	
	        		
	        		//System.out.println(pred);
	        		String pred = preds[j];
	        	   
	        	
	        	if((splited[1].contains(pred)) && !("?".equals(checkObj))){
	        		tpIndex=i; 
	        		var=splited[0];
	        		
	        		for(int k=0;k<vars.length;k++){
	        			if(vars[k].contains(var)){
	        				varInd=k;
	        			}}
	        		//System.out.println(tpIndex+" "+" "+varInd+" "+var+" matched"+spTok); 
	        		indexes[0]= tpIndex;
	        		indexes[1]=varInd;
	        		break Task;}
	        	 else {tpIndex=-1; varInd=-1; indexes[0]=tpIndex; indexes[1]=varInd;}
	        	}
	        	}
	        }
	    
		
	return indexes;
	}
	
	
	
	public String getSecondQuery(String st, int TPindex, int varInd) throws IOException, InvalidSPARQLException {
		
	String spStr[]=getTriples(st);
	String[] prefixes = getPrefixes(st);
	String[] var = getVariables(st);
	String variables="";

	String SQ="";
	for(String pre : prefixes) {
		SQ=SQ+pre+"\n";
	}
	
	String triples="";
	for(int i=1;i<spStr.length;i++){
		if(i!=TPindex){
		triples = triples+"\n"+spStr[i];
	}}

	if(var.length==0)
		SQ =SQ+"\nSELECT * WHERE { \n" + triples+"\n"+"}";
	else {
	
		for(int i=0;i<var.length;i++) {
			if(i!=varInd){
			variables=variables+var[i]+" ";
		}}
	}
	
	SQ =SQ+"\nSELECT "+variables+" WHERE { \n" + triples+"\n"+"}";
		
	return SQ;
	
}

public String ReadFromFile(File file) {
	 Scanner readFromFile = null;
     String line = null;
     try {
        //create a Scanner object to read from the file
        readFromFile = new Scanner(file);
     } 
     catch (FileNotFoundException exception) {
     /*
     Print error message.
     In order to print double quotes("), 
     the escape sequence for double quotes (\") must be used.
     */
        System.out.print("ERROR: File not found for \"");
        System.out.println(file+"\"");
     //end the program
        System.exit(1);
     }        
  //if made connection to file, read from file
  /*
  In order to print double quotes("), 
  the escape sequence for double quotes (\") must be used.				
  */
     //System.out.print("Reading from file \"" + file + "\":\n");
  //keeps looping if file has more lines..
     String query="";
     while (readFromFile.hasNextLine()) {
     //get a line of text..
        line = readFromFile.nextLine();
     //display a line of text to screen..
        //System.out.println(line);
        query=query+"\n"+line;
     }//end of while
	
	return query;
}

public String[] getTriples(String query) throws InvalidSPARQLException {
	SPARQLQuery parsedQuery = new SPARQLQuery(query);
	List<IStatement> strlist = parsedQuery.getStatements();
	
	String str=strlist.get(0).toString();
	//System.out.println(StringUtils.substringBetween(str, "{", "}"));
	String splitStr = StringUtils.substringBetween(str, "{", "}");
	String spStr[] = StringUtils.split(splitStr, "\n");	
	//System.out.println("List Size: "+spStr.length);
	
	return spStr;
}

public String[] getPrefixes(String query) throws InvalidSPARQLException {
	SPARQLQuery parsedQuery = new SPARQLQuery(query);
	Map<String, String> prefixes = parsedQuery.getPrefixes();
	String[] prefixesArray= new String[prefixes.size()];
	int preLength =0;
	for (Map.Entry<String,String> entry : prefixes.entrySet()) {  
        //System.out.println("PREFIX "+entry.getKey()+": <"+entry.getValue()+">");
        prefixesArray[preLength]="PREFIX "+entry.getKey()+": <"+entry.getValue()+">";
        preLength=preLength+1;
	}
	
	return prefixesArray;
}

public String[] getVariables(String query) throws InvalidSPARQLException {
	SPARQLQuery parsedQuery = new SPARQLQuery(query);
	String[] var= new String[parsedQuery.getUnknowns().size()];
	int varLength=0;
	Iterator<String> itr = parsedQuery.getUnknowns().iterator();
	while(itr.hasNext()){
		//var=System.out.println(itr.next().replace(".", ""));
		var[varLength]="?"+itr.next().replace(".", "");
		varLength=varLength+1;
		}
	return var;
}



public String[] getPredicates(){
	
	String [] predicates = new  String [102];
	
	predicates[0]= "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	predicates[1]= "http://dbpedia.org/ontology/wikiPageID";
	predicates[2]= "http://www.w3.org/2000/01/rdf-schema#label";
	predicates[3]= "http://purl.org/dc/terms/subject";
	predicates[4]= "http://dbpedia.org/property/taxon" ;
	predicates[5]= "http://www.w3.org/2002/07/owl#sameAs";
	predicates[6]= "http://www.w3.org/2000/01/rdf-schema#subClassOf";
	predicates[7] = "http://xmlns.com/foaf/0.1/homepage";
	predicates[8] = "http://www.w3.org/2004/02/skos/core#broader";
	predicates[9] = "http://dbpedia.org/property/starring";
	predicates[10]= "http://dbpedia.org/ontology/wikiPageRedirects";
	predicates[11]= "http://dbpedia.org/ontology/city";
	predicates[12]= "http://dbpedia.org/ontology/locationCity";
	predicates[13] = "http://de.dbpedia.org/property/ort";
	predicates[14] = "http://de.dbpedia.org/property/traegerschaft";
	predicates[15] = "http://dbpedia.org/ontology/parentOrganisation";
	predicates[16] = "http://dbpedia.org/ontology/director";
	predicates[17] = "http://dbpedia.org/property/runtime";
	predicates[18] = "http://dbpedia.org/resource/runtime";
	predicates[19] = "http://www.w3.org/2003/01/geo/wgs84_pos#lat";
	predicates[20] = "http://de.dbpedia.org/property/betreiber";
	predicates[21] = "http://xmlns.com/foaf/0.1/name";
	predicates[22] = "http://dbpedia.org/property/name";
	predicates[23] = "http://purl.org/dc/elements/1.1/rights";
	predicates[24] = "http://dbpedia.org/resource/Belgian_Pro_Leagueruntime";
	predicates[25] = "http://dbpedia.org/ontology/notableCommander";
	predicates[26] = "http://dbpedia.org/ontology/team";
	predicates[27] = "http://dbpedia.org/ontology/leaderName";
	predicates[28] = "http://dbpedia.org/ontology/developer";
	predicates[29] = "http://dbpedia.org/ontology/manufacturer";
	predicates[30] = "http://dbpedia.org/property/manufacturer";
	predicates[31] = "http://dbpedia.org/ontology/designer";
	predicates[32] = "http://dbpedia.org/resource/Categor%C3%ADa_Primera_Aruntime";
	predicates[33] = "http://dbpedia.org/ontology/distributor";
	predicates[34] = "http://dbpedia.org/ontology/parent";
	predicates[35] = "http://dbpedia.org/ontology/owningCompany";
	predicates[36] = "http://dbpedia.org/ontology/owner";
	predicates[37] = "http://dbpedia.org/property/developer";
	predicates[38]= "http://dbpedia.org/property/owner" ; 
	predicates[39]= "http://dbpedia.org/ontology/builder" ;
	predicates[40]= "http://dbpedia.org/ontology/publisher" ;
	predicates[41]= "http://dbpedia.org/ontology/tenant";
	predicates[42]= "http://dbpedia.org/ontology/parentCompany";
	predicates[43]= "http://dbpedia.org/property/originalDeveloper";
	predicates[44]= "http://dbpedia.org/property/mainContractor" ;
	predicates[45]= "http://dbpedia.org/ontology/mainContractor";
	predicates[46]= "http://dbpedia.org/ontology/originalDeveloper";
	predicates[47]= "http://dbpedia.org/property/distributor";
	predicates[48]= "http://dbpedia.org/property/parentCompany";
	predicates[49]= "http://dbpedia.org/property/owningCompany";
	predicates[50]= "http://dbpedia.org/property/builder";
	predicates[51]= "http://dbpedia.org/property/designer";
	predicates[52]= "http://dbpedia.org/property/parent";
	predicates[53]= "http://dbpedia.org/property/publisher";
	predicates[54]= "http://dbpedia.org/ontology/currency";
	predicates[55]= "http://dbpedia.org/ontology/wikiPageExternalLink";
	predicates[56]= "http://dbpedia.org/property/director";
	predicates[57]= "http://www.w3.org/2004/02/skos/core#subject";
	predicates[58]= "http://dbpedia.org/ontology/abstract";
	predicates[59]= "http://xmlns.com/foaf/0.1/isPrimaryTopicOf";
	predicates[60]= "http://www.w3.org/2000/01/rdf-schema#comment";
	predicates[61]= "http://dbpedia.org/ontology/birthPlace";
	predicates[62]= "http://dbpedia.org/ontology/birthDate";
	predicates[63]= "http://dbpedia.org/ontology/thumbnail";
	predicates[64]= "http://www.w3.org/1999/02/22-rdf-syntax-ns#birthPlace";
	predicates[65]= "http://www.w3.org/1999/02/22-rdf-syntax-ns#campus";
	predicates[66]= "http://www.w3.org/1999/02/22-rdf-syntax-ns#deathPlace";
	predicates[67]= "http://www.w3.org/1999/02/22-rdf-syntax-ns#hometown";
	predicates[68]= "http://dbpedia.org/ontology/wikiPageDisambiguates";
	predicates[69]= "http://dbpedia.org/ontology/wikiPageRevisionID";
	predicates[70]= "http://dbpedia.org/property/familia";
	predicates[71]= "http://www.w3.org/ns/prov#wasDerivedFrom";
	predicates[72]= "http://xmlns.com/foaf/0.1/page";
	predicates[73]= "http://dbpedia.org/ontology/occupation";
	predicates[74]= "http://dbpedia.org/property/movieMusic";
	predicates[75]= "http://xmlns.com/foaf/0.1/givenName";
	predicates[76]= "http://dbpedia.org/resource/K-Leagueruntime";
	predicates[77]= "http://dbpedia.org/resource/La_Ligaruntime";
	predicates[78]= "http://dbpedia.org/ontology/runtime";
	predicates[79]= "http://dbpedia.org/property/children";
	predicates[80]= "http://dbpedia.org/property/hasPhotoCollection";
	predicates[81]= "http://dbpedia.org/property/imageCaption";
	predicates[82]= "http://dbpedia.org/property/subdivisionType";
	predicates[83]= "http://dbpedia.org/ontology/associatedMusicalArtist";
	predicates[84]= "http://xmlns.com/foaf/0.1/surname";
	predicates[85]= "http://dbpedia.org/ontology/birthYear";
	predicates[86]= "http://xmlns.com/foaf/0.1/primaryTopic";
	predicates[87]= "http://dbpedia.org/ontology/producer";
	predicates[88]= "http://dbpedia.org/property/dateOfBirth";
	predicates[89]= "http://purl.org/dc/terms/rights";
	predicates[90] = "http://dbpedia.org/property/birthPlace";
	predicates[91] = "http://dbpedia.org/ontology/writer";
	predicates[92] = "http://dbpedia.org/property/wikiPageID";
	predicates[93] = "http://dbpedia.org/property/occupation";
	predicates[94] = "http://dbpedia.org/property/placeOfBirth";
	predicates[95] = "http://dbpedia.org/ontology/activeYearsStartYear";
	predicates[96] = "http://dbpedia.org/ontology/genre";
	predicates[97] = "http://dbpedia.org/property/birthDate";
	predicates[98] = "http://purl.org/dc/elements/1.1/description";
	predicates[99] = "http://dbpedia.org/ontology/isPartOf";
	predicates[100] = "http://dbpedia.org/property/writer";
	predicates[101]= "http://dbpedia.org/resource/Serie_Aruntime";
	
	return predicates;
	
}
}