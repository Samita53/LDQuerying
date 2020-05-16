import java.io.IOException;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import org.apache.jena.query.ResultSetFormatter;


public class QueHDTFuseki {
	
	ResultSet results;

	public static void main(String [] args) throws IOException {
		
		System.out.println("yes");
		
		QueHDTFuseki quehdt = new QueHDTFuseki();
		
		String q= "select (count(*) as ?triples) where {graph ?g {?s ?p ?o.}}";
		
	    quehdt.RunOnServer(q);
		
		
		
	}
	
public ResultSet RunAtServer(String arr) {
	
	    System.out.println("before");
		
        Query query = QueryFactory.create(arr) ;
        
		
		QueryExecution QExec = QueryExecutionFactory.sparqlService("http://localhost:3030/hdtservice/query", query);

        System.out.println("after");
        //QueryExecution QExec = QueryExecutionFactory.sparqlService("http://localhost:8890/conductor/isql.vspx", query);
		
		
		results = QExec.execSelect(); //This line is causing error.
		
		//Store_Que1_res(results);

		System.out.println("come back here");
		
		
		
		return (results);
		
	}


	
	public void RunOnServer(String Query) {
		
		System.out.println("inside method");
		
        Query query = QueryFactory.create(Query) ;
		
		QueryExecution QExec = QueryExecutionFactory.sparqlService("http://localhost:3030/hdtservice/query", query);
		
		ResultSet results = QExec.execSelect();

		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			// assumes that you have an "?x" in your query
			//RDFNode x = soln.get("x");
			System.out.println(soln.toString());
		}	
		
	}
	
public void Store_Que1_res(ResultSet rs) {
	
	System.out.println("store here");
	
	//String [] arr = null;
	
	while (rs.hasNext()) {
		System.out.println(rs.next().toString());
	    //String em = rs.toString();
	    //arr[0] = em;
	    //System.out.println(em);
	}
	
	
}
       

}



