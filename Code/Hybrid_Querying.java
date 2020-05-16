import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdtjena.HDTGraph;

import com.tenforce.semtech.SPARQLParser.SPARQL.InvalidSPARQLException;

//import com.hp.hpl.jena.vocabulary.RDF;

//import ExecuteSquin;

public class Hybrid_Querying {
	
	static long Local_QueRun_time;
	static long Live_QueRun_time;
	
	
	 public  void useParameterizedSPARQLFinal(String [] arr) throws IOException{
		   
		 Local_QueRun_time =System.currentTimeMillis();
			       
		 	QueHDTFuseki hdtOb = new QueHDTFuseki();
	 
			ResultSet Results = hdtOb.RunAtServer(arr[0]);	
			
			//System.out.println("Total processing time on Local server:"+ TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()- Local_QueRun_time)+" sec(s)");
			
			//WriteFile.appendFile("Total processing time on Local server:"+ (System.currentTimeMillis()- Local_QueRun_time)+" msec(s)"+"|");
			
	        final QuerySolutionMap map = new QuerySolutionMap();
	        
	        while(Results.hasNext()) {
	        
	        map.addAll( Results.next() );
	        final ParameterizedSparqlString pss = new ParameterizedSparqlString( arr[1] );
		       //System.out.println( "Before"+pss.toString() );
		        pss.setParams( map );
		        //System.out.println( "After"+pss.toString() );
		        Live_QueRun_time =System.currentTimeMillis();
		        
		        RunSquin_Copy.liveQuery(pss.toString());
		        
	        }
	        WriteFile.appendFile("Total processing time on Live server:"+ (System.currentTimeMillis()- Live_QueRun_time)+" msec(s)"+ "\n \n");
	      
	       	    }
	 

	 
	 public static void main(String [] args) throws IOException, InvalidSPARQLException {
		 
		 
		 SplitQueryNew sq = new SplitQueryNew();
		 
		 String [] query = new String [2];
		 
		 int QueryCounter= 0;
		 		 
		 Hybrid_Querying hbrObj = new Hybrid_Querying();
		 
		 //Local_QueRun_time =System.currentTimeMillis();
		 
		 		 
		 
		 if (args[0] == null || args[0].trim().isEmpty()) {
		        System.out.println("You need to provide a file containing SPARQL query");
		        return;
		    }else{
		    	File file = new File(args[0]);
		    	String sparqlQuery = sq.ReadFromFile(file);
		    	int [] ind = sq.getSeedTPIndex(sparqlQuery);
		    	System.out.println(ind[0]);
		    	if(ind[0]!= -1 && ind[1]!=-1){
		    	query[0]=sq.getSeedTriplePattern(sparqlQuery, ind[0], ind[1]);
		    	query[1]=sq.getSecondQuery(sparqlQuery,ind[0],ind[1]);
		    	System.out.println(query[0]);
		    } else {System.out.println("This is not a Non-LDaQ query");}
		    	}
		    	   
		 String [] queArr = new String [2]; 
		 String [] queryArr = new String [2]; 
		 String [] queArr_F = new String [2]; 
		 String [] qSWDF = new String [2];
		 
		 queArr[0]= "SELECT * WHERE {graph ?g { ?1 <http://xmlns.com/foaf/0.1/surname>  \"Jackson\"@en.}}";
		 queArr[1]= "SELECT ?2 ?3 ?4 WHERE {?1 <http://dbpedia.org/ontology/birthPlace> ?3. ?1 <http://dbpedia.org/ontology/occupation> ?4.}";

		 
		queryArr [0] = "SELECT ?1 WHERE {graph ?g{ ?1 a <http://dbpedia.org/ontology/Agent>.}}";
		queryArr [1] = "SELECT ?2 ?3 ?4 WHERE { ?1 <http://dbpedia.org/ontology/genre> ?2. ?2 <http://dbpedia.org/ontology/musicSubgenre> ?3. ?2 <http://dbpedia.org/ontology/musicSubgenre> ?4. ?3 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/TopicalConcept>. ?4 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Thing>.  }";
			
		 
		 queArr_F[0]= "PREFIX  foaf: <http://xmlns.com/foaf/0.1/>\n" + 
		 		"			 PREFIX  dbont: <http://dbpedia.org/ontology/>\n" + 
		 		"			 PREFIX  dbp:  <http://dbpedia.org/property/>\n" + 
		 		"			 PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
		 		"\n" + 
		 		"			 SELECT  ?link \n" + 
		 		"			 WHERE\n" + 
		 		"			   { graph ?g  { ?link rdf:type dbont:MusicalArtist }\n" + 
		 		"			     UNION\n" + 
		 		"			       { ?link rdf:type dbont:band }}";
		 queArr_F[1]= "PREFIX  foaf: <http://xmlns.com/foaf/0.1/>\n" + 
		 		"			 PREFIX  dbont: <http://dbpedia.org/ontology/>\n" + 
		 		"			 PREFIX  dbp:  <http://dbpedia.org/property/>\n" + 
		 		"			 PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
		 		"\n" + 
		 		"			 SELECT   ?name\n" + 
		 		"			     ?link foaf:name ?name\n" + 
		 		"			     FILTER regex(?name, \"anastacia\", \"i\")\n" + 
		 		"			   }\n" + 
		 		"		       ";

		 //sp.readQueLog();
		 
		                                        
		 qSWDF[0]= "SELECT * WHERE {graph ?g { ?1 ?2  ?3.}}";
		 qSWDF[1]= "SELECT ?2 ?3 ?4 WHERE {?1 <http://dbpedia.org/ontology/birthPlace> ?3. ?1 <http://dbpedia.org/ontology/occupation> ?4.}";

		
		hbrObj.useParameterizedSPARQLFinal(query);
		
		//WriteFile.appendFile("Query number: " + QueryCounter+1 + "\n");
		//hbrObj.useParameterizedSPARQLFinal(splitQue);
		//System.out.println("Total processing time on Live server:"+ TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()- Live_QueRun_time)+" sec(s)");
		
		//WriteFile.appendFile("Total processing time on Live server:"+ TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()- Live_QueRun_time)+" sec(s)"+ "\n");
		//Model m = createHDTDs();
		//useParameterizedSPARQLString(m, queryArr);
		
		
	 }
	 
	 
}






