/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexa4304.uriception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.jena.query.*;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Flo Macé
 */
public class SparqlProcessor
{
    
    public List<String> URIFilter(List<String> URIList) throws IOException
    {
        List<String> videoGameURIList = new ArrayList();
        Set<String> uriSet = new HashSet();
        
        for (String uri : URIList)
        {
            String queryString ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
                                "SELECT DISTINCT ?s WHERE { " +
                                " <" + uri + "> rdf:type <http://dbpedia.org/ontology/VideoGame>."+
                                " <" + uri + "> <http://dbpedia.org/ontology/series> ?s " +
                                "}" ;
            
            Query query = QueryFactory.create(queryString);
            QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
            ResultSet results = qExe.execSelect();
            
            ByteArrayOutputStream outputStreamSeries = new ByteArrayOutputStream();

            ResultSetFormatter.outputAsJSON(outputStreamSeries, results);

            String jsonStringSeries = new String(outputStreamSeries.toByteArray());

            JSONObject resultJsonSeries = new JSONObject(jsonStringSeries);
            
            JSONArray uriListSeries = resultJsonSeries.getJSONObject("results").getJSONArray("bindings");
            
            for (int i = 0; i < uriListSeries.length(); i++)
            {
                queryString ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
                             "SELECT DISTINCT ?opus WHERE { " +
                             " ?opus <http://dbpedia.org/ontology/series> <" + uriListSeries.getJSONObject(i).getJSONObject("s").getString("value") + ">. " +
                             " ?opus rdf:type <http://dbpedia.org/ontology/VideoGame>"+
                             "}" ;
                
                query = QueryFactory.create(queryString);
                qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
                results = qExe.execSelect();

                ByteArrayOutputStream outputStreamOpus = new ByteArrayOutputStream();

                ResultSetFormatter.outputAsJSON(outputStreamOpus, results);

                String jsonStringOpus = new String(outputStreamOpus.toByteArray());

                JSONObject resultJsonOpus = new JSONObject(jsonStringOpus);

                JSONArray uriListOpus = resultJsonOpus.getJSONObject("results").getJSONArray("bindings");
                
                for (int j = 0; j < uriListOpus.length(); j++)
                {
                    uriSet.add(uriListOpus.getJSONObject(j).getJSONObject("opus").getString("value"));
                }
            }
            
            queryString ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
                                "SELECT DISTINCT ?opus WHERE { " +
                                " ?opus <http://dbpedia.org/ontology/series> <" + uri + ">. " +
                                " ?opus rdf:type <http://dbpedia.org/ontology/VideoGame>"+
                                "}" ;

            query = QueryFactory.create(queryString);
            qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
            results = qExe.execSelect();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ResultSetFormatter.outputAsJSON(outputStream, results);

            String jsonString = new String(outputStream.toByteArray());

            JSONObject resultJson = new JSONObject(jsonString);

            JSONArray uriList = resultJson.getJSONObject("results").getJSONArray("bindings");
            
            queryString =   "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
                            "SELECT DISTINCT * WHERE { " +
                            " <" + uri + "> rdf:type <http://dbpedia.org/ontology/VideoGame>. " +
                            "}" ;

            query = QueryFactory.create(queryString);
            qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
            results = qExe.execSelect();
            
            ByteArrayOutputStream outputStreamGame = new ByteArrayOutputStream();

            ResultSetFormatter.outputAsJSON(outputStreamGame, results);

            String jsonStringGame = new String(outputStreamGame.toByteArray());

            JSONObject resultJsonGame = new JSONObject(jsonStringGame);
            
            JSONArray checkGame = resultJsonGame.getJSONObject("results").getJSONArray("bindings");
            
            if (checkGame.length() > 0)
            {
                String isGame = checkGame.getJSONObject(0).getJSONObject("_star_fake").getString("value");
            
                if (isGame.equals("1"))
                {
                    uriSet.add(uri);
                }
            }
           
            for (int i = 0; i < uriList.length(); i++)
            {
                uriSet.add(uriList.getJSONObject(i).getJSONObject("opus").get("value").toString());
            }
        }
        
        videoGameURIList = new ArrayList(uriSet);
        
        return videoGameURIList;
    }
}
