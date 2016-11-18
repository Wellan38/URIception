/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.query.*;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * @author Flo Macé
 */
public class SparqlProcessor {
    // filtre les URI et recupere tout les jeux videos appartenant a la franchise.
    public List<String> URIFilter(List<String> URIList) throws IOException {
//        System.out.println("Filtre Sparql");


        List<String> videoGameURIList = new ArrayList();
        Set<String> uriSet = new HashSet();

        for (String uri : URIList) {
            String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                    "SELECT DISTINCT ?s WHERE { " +
                    " <" + uri + "> rdf:type <http://dbpedia.org/ontology/VideoGame>." +
                    " <" + uri + "> <http://dbpedia.org/ontology/series> ?s " +
                    "}";

            JSONArray uriListSeries = sparqlQuery(queryString);

            for (int i = 0; i < uriListSeries.length(); i++) {
                queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "SELECT DISTINCT ?opus WHERE { " +
                        " ?opus <http://dbpedia.org/ontology/series> <" + uriListSeries.getJSONObject(i).getJSONObject("s").getString("value") + ">. " +
                        " ?opus rdf:type <http://dbpedia.org/ontology/VideoGame>" +
                        "}";

                JSONArray uriListOpus = sparqlQuery(queryString);

                for (int j = 0; j < uriListOpus.length(); j++) {
                    uriSet.add(uriListOpus.getJSONObject(j).getJSONObject("opus").getString("value"));
                }
            }

            queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                    "SELECT DISTINCT ?opus WHERE { " +
                    " ?opus <http://dbpedia.org/ontology/series> <" + uri + ">. " +
                    " ?opus rdf:type <http://dbpedia.org/ontology/VideoGame>" +
                    "}";

            JSONArray uriList = sparqlQuery(queryString);

            queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                    "SELECT DISTINCT * WHERE { " +
                    " <" + uri + "> rdf:type <http://dbpedia.org/ontology/VideoGame>. " +
                    "}";

            JSONArray checkGame = sparqlQuery(queryString);

            if (checkGame.length() > 0) {
                String isGame = checkGame.getJSONObject(0).getJSONObject("_star_fake").getString("value");

                if (isGame.equals("1")) {
                    uriSet.add(uri);
                }
            }

            for (int i = 0; i < uriList.length(); i++) {
                uriSet.add(uriList.getJSONObject(i).getJSONObject("opus").getString("value"));
            }
        }

        videoGameURIList = new ArrayList(uriSet);

        return videoGameURIList;
    }

    public JSONArray sparqlQuery(String queryString) throws UnsupportedEncodingException {
        Query query = QueryFactory.create(queryString);
        QueryExecution qExe = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
        ResultSet results = qExe.execSelect();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ResultSetFormatter.outputAsJSON(outputStream, results);
        String jsonString = new String(outputStream.toByteArray(), "UTF-8");
        JSONObject resultJson = new JSONObject(jsonString);

        return resultJson.getJSONObject("results").getJSONArray("bindings");
    }
}