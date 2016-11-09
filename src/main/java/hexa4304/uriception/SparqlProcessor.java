/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexa4304.uriception;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.*;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Flo Macé
 */
public class SparqlProcessor {
    
    public static final String VIDEOGAME_CLASS = "http://dbpedia.org/class/yago/Game100456199";
    
    public List<String> URIFilter(List<String> URIList)
    {
        List<String> videoGameURIList = new ArrayList();
        
        for (String uri : URIList)
        {
            System.out.println("URI List Filter");
            String queryString ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
                                "SELECT DISTINCT ?opus WHERE { " +
                                " ?opus <http://dbpedia.org/ontology/series> <" + uri + ">. " +
                                " ?opus rdf:type <http://dbpedia.org/ontology/VideoGame>"+
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
            
            if(uriList.length() == 0)
            {
                queryString =   "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
                                "SELECT DISTINCT * WHERE { " +
                                " <" + uri + "> rdf:type <http://dbpedia.org/ontology/VideoGame>. " +
                                "}" ;

                query = QueryFactory.create(queryString);
                qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
                results = qExe.execSelect();
                uriList = resultJson.getJSONObject("results").getJSONArray("bindings");
            }
           
            for (int i = 0; i < uriList.length(); i++)
            {
                videoGameURIList.add(uriList.getJSONObject(i).getJSONObject("opus").get("value").toString());
            }
        }
        
//        System.out.println("List of video games :");
//        
//        for (String uri : videoGameURIList)
//        {
//            System.out.println(uri);
//        }
        
        return videoGameURIList;
    }
}
