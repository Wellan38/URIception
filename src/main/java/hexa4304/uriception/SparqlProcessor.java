/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexa4304.uriception;

import java.io.ByteArrayOutputStream;
import org.apache.jena.query.*;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Flo Macé
 */
public class SparqlProcessor {
    
    
    public void URIFilter(String URIList)
    {
//        Model model = ModelFactory.createDefaultModel();
//        String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
//                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
//                            "SELECT * WHERE { " +
//                            " <"+URIList+"> rdf:type ?type. " +
//                            " ?type rdfs:subClassOf <http://dbpedia.org/class/yago/ComputerGame100458890>. " +
//                            "}" ;
//        Query query = QueryFactory.create(queryString) ;
//        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
//          ResultSet results = qexec.execSelect() ;
//          System.out.println(results.toString());
//          if( results.hasNext())
//          {
//              System.out.println("OK");
//          }
//          else
//          {
//               System.out.println("PAS OK");
//          }
//          for ( ; results.hasNext() ; )
//          {
//            QuerySolution soln = results.nextSolution() ;
//            System.out.println("ok");
//            System.out.println(soln.toString());
//            RDFNode x = soln.get("varName") ;       // Get a result variable by name.
//            Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
//            Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
//            System.out.println(x.toString() + r.toString() + l.toString());
//          }
//        }
    
        String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
                            "SELECT * WHERE { " +
                            " <"+URIList+"> rdf:type ?type. " +
                            " ?type rdfs:subClassOf <http://dbpedia.org/class/yago/ComputerGame100458890>. " +
                            "}" ;

        Query query = QueryFactory.create(queryString);
        QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
        ResultSet results = qExe.execSelect();
        //ResultSetFormatter.out(System.out, results, query) ;
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        ResultSetFormatter.outputAsJSON(outputStream, results);
        
        String jsonString = new String(outputStream.toByteArray());

        JSONObject resultJson = new JSONObject(jsonString);
        
        JSONArray uriList = resultJson.getJSONObject("results").getJSONArray("bindings");
        
        System.out.println(uriList);
    }
}
