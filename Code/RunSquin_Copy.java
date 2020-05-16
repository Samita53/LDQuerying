

import java.io.PrintStream;

import org.squin.dataset.QueriedDataset;
import org.squin.dataset.hashimpl.individual.QueriedDatasetImpl;
import org.squin.dataset.jenacommon.JenaIOBasedQueriedDataset;
import org.squin.engine.LinkTraversalBasedQueryEngine;
import org.squin.engine.LinkedDataCacheWrappingDataset;
import org.squin.ldcache.jenaimpl.JenaIOBasedLinkedDataCache;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class RunSquin_Copy {

	public static int ROW_COUNTER;

	public static void main(String[] args) {

		String q1 = "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" + "select ?TheyKnowOlaf " + "where"
				+ " { ?TheyKnowOlaf foaf:knows <http://olafhartig.de/foaf.rdf#olaf> } limit 10";

		String qtest = "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" + "select ?x where { "
				+ "<http://olafhartig.de/foaf.rdf#olaf> foaf:knows ?x } limit 10";

		String qs2 = "Prefix yago: <http://dbpedia.org/class/yago/>"
				+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +

				"select ?v where {" + "?v foaf:knows <http://dbpedia.org/resource/David_Attenborough>." + "}";

		String qs3 = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + "select ?v where {"
				+ "<http://biglynx.co.uk/people/dave-smith> foaf:knows ?v." + "}";

		String Q2 = "PREFIX swc: <http://data.semanticweb.org/ns/swc/ontology#>\n"
				+ "PREFIX swrc: <http://swrc.ontoware.org/ontology#>\n" + "Select * where {"
				+ "?proceedings swc:relatedToEvent <http://data.semanticweb.org/conference/eswc/2010>."
				+ "?paper swc:isPartOf ?proceedings." + "?paper swrc:author ?p." + "}";

		String Q3 = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + "PREFIX nyt: <http://data.nytimes.com/>\n"
				+ "PREFIX dct: <http://purl.org/dc/terms/>\n" +

				"Select * where {" + "?n dct:subject <http://dbpedia.org/resource/Category:Chancellors_of_Germany>."
				+ "?p2 owl:sameAs ?n." + "?p2 nyt:latest_use ?u." + "}";

		String LD3 = "PREFIX swc: <http://data.semanticweb.org/ns/swc/ontology#>\n"
				+ "PREFIX swrc: <http://swrc.ontoware.org/ontology#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + "Select ?paper ?p ?x ?n where {"
				+ "?paper swc:isPartOf <http://data.semanticweb.org/conference/iswc/2008/poster_demo_proceedings>."
				+ "?paper swrc:author ?p." + "?p owl:sameAs ?x." + "?p rdfs:label ?n." + "}";

		String LD8 = "PREFIX owl:  <http://www.w3.org/2002/07/owl#>" + "PREFIX dct:  <http://purl.org/dc/terms/>"
				+ "PREFIX drugbank: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>"
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" + "Prefix skos: <http://www.w3.org/2004/02/skos/core#>"
				+ "Select ?drug ?id ?s ?o where {"
				+ "?drug drugbank:drugCategory <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugcategory/micronutrient> ."
				+

				"}";

		String LD11 = "PREFIX dbowl: <http://dbpedia.org/ontology/>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "Select Distinct ?x ?y ?d ?l where{"
				+ "?x dbowl:team <http://dbpedia.org/resource/Eintracht_Frankfurt> ." + "?x rdfs:label ?y ."
				+ "?x dbowl:birthDate ?d ." + "?x dbowl:birthPlace ?p ." + "?p rdfs:label ?l ." + "}";

		String q2 = "SELECT ?1 ?2 ?3 ?4 WHERE { ?1 a <http://dbpedia.org/ontology/Agent>. ?1 <http://dbpedia.org/ontology/genre> ?2. ?2 <http://dbpedia.org/ontology/musicSubgenre> ?3. ?2 <http://dbpedia.org/ontology/musicSubgenre> ?4. ?3 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/TopicalConcept>. ?4 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Thing>.  }";

		String q3 = "SELECT ?2 ?3 WHERE { <http://dbpedia.org/resource/(I'm_So)_Afraid_of_Losing_You_Again?dbpv=2016-10&nif=table&ref=1_2> ?2 ?3. }";

		liveQuery(qtest);
	}

	
	  public static void liveQuery (String query) {
	 
	 System.out.println("Inside Squin"); 
	 System.out.println(query);
		  
	  LinkTraversalBasedQueryEngine.register(); QueriedDataset qds = new
	  QueriedDatasetImpl(); JenaIOBasedQueriedDataset qdsWrapper = new
	  JenaIOBasedQueriedDataset( qds ); JenaIOBasedLinkedDataCache ldcache = new
	  JenaIOBasedLinkedDataCache( qdsWrapper ); Dataset dsARQ = new
	  LinkedDataCacheWrappingDataset( ldcache ); QueryExecution qe =
	  QueryExecutionFactory.create(query, dsARQ ); PrintStream out = System.out;
	  ResultSet results = qe.execSelect();

		
		  while ( results.hasNext() ) { QuerySolution s = results.nextSolution();
		  System.out.println("Inside While");
		  ROW_COUNTER = ROW_COUNTER + results.getRowNumber(); out.format( "%03d | ",
		  ROW_COUNTER ); for ( String var : results.getResultVars() ) { String b =
		  (s.contains(var) ) ? s.get(var).toString() : "     " ; out.format(
		  "?%s: %s \t ", var, b ); } out.println(); out.flush();
		  
		  }
		 //call method to join query results.
	  //System.out.println(qds.countRDFGraphs());
	  
	  //ResultSetFormatter.out(System.out, results);
	  //System.out.println(qds.getRDFGraphsSourceURLs()); 
		  try { ldcache.shutdownNow(
	  4000 ); } // 4 sec. 
	  catch ( Exception e ) { System.err.println(
	  "Shutting down the Linked Data cache failed: " + e.getMessage() ); } }
	 

	
	  public static void LiveQuery (String query) { 
		  System.out.println(query);
	  LinkTraversalBasedQueryEngine.register(); 
	  QueriedDataset qds = new QueriedDatasetImpl(); 
	  JenaIOBasedQueriedDataset qdsWrapper = new JenaIOBasedQueriedDataset( qds );
	  JenaIOBasedLinkedDataCache ldcache = new JenaIOBasedLinkedDataCache( qdsWrapper ); 
	  Dataset dsARQ = new LinkedDataCacheWrappingDataset( ldcache ); 
	  QueryExecution qe = QueryExecutionFactory.create(query, dsARQ ); 
	  ResultSet results = qe.execSelect();
	  System.out.println(ResultSetFormatter.asText(results));
	  
	  //call method to join query results.
	  
	  try { ldcache.shutdownNow( 4000 );  }  // 4 sec.
	  catch ( Exception e ) {
	  System.err.println( "Shutting down the Linked Data cache failed: " +
	  e.getMessage() ); } 
	  //return results; 
	  
	  }  

}
