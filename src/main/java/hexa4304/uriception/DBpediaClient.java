package hexa4304.uriception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/* classe DBpediaClient :
 * Permet d'envoyer une requête SPARQL à DBpedia en indiquant quels sont les paramètres connus.
*/


public class DBpediaClient {

    // Paramètres dans la requête SPARQL correspondant aux infos souhaitées
    public static String[] _requestParameters;

    // Types -------------------------------------------------------------------
    // Enumération pour les paramètres
    public enum InfoType {
        DEVELOPERS(0),
        DESIGNERS(1),
        PUBLISHERS(2),
        RELEASE_DATES(3),
        PLATFORMS(4),
        TITLES(5),
        DESCRIPTIONS(6),
        GENRES(7),
        NUMBER_INFO(8);

        @SuppressWarnings("unused")
        private final int id;

        private InfoType(int id) {
            this.id = id;
        }

        public int value() {
            return id;
        }
    }

    public static void initRequestParameters() {
        _requestParameters = new String[InfoType.NUMBER_INFO.value()];
        _requestParameters[InfoType.DEVELOPERS.value()] = "<http://dbpedia.org/ontology/developer>";
        _requestParameters[InfoType.DESIGNERS.value()] = "<http://dbpedia.org/ontology/designer>";
        _requestParameters[InfoType.PUBLISHERS.value()] = "<http://dbpedia.org/ontology/publisher>";
        _requestParameters[InfoType.RELEASE_DATES.value()] = "<http://dbpedia.org/ontology/releaseDate>";
        _requestParameters[InfoType.PLATFORMS.value()] = "<http://dbpedia.org/ontology/computingPlatform>";
        _requestParameters[InfoType.TITLES.value()] = "<http://www.w3.org/2000/01/rdf-schema#label>";
        _requestParameters[InfoType.DESCRIPTIONS.value()] = "<http://www.w3.org/2000/01/rdf-schema#comment>";
        _requestParameters[InfoType.GENRES.value()] = "<http://dbpedia.org/ontology/genre>";
    }

    // Renvoie le résultat de la requête SPARQL de paramètres object, predicate, value.
    // Si un paramètre n'est pas connu, entrer la valeur "null" ou une String vide.
    public static JSONArray sendRequest(String object, String predicate, String value) throws IOException {
        String request = buildRequest(object, predicate, value);

          SparqlProcessor proc = new SparqlProcessor();
          
          return proc.sparqlQuery(request);
    }

    // Construit une requête SPARQL simple avec les paramètres objet, prédicat, valeur.
    // Si un paramètre n'est pas connu, entrer la valeur "null" ou une String vide.
    public static String buildRequest(String object, String predicate, String value) {
        String request = "SELECT * WHERE { ";

        request += (object == null || object.isEmpty()) ? "?o " : (object + " ");
        request += (predicate == null || predicate.isEmpty()) ? "?p " : (predicate + " ");
        request += (value == null || value.isEmpty()) ? "?v " : (value + " ");

        request += "}";

        return request;
    }

    // Extrait les données du document "json" (sous forme de String) envoyé en paramètre
    // valueTypeToExtract désigne qu'elle partie de la requête nous interesse c'est à dire
    // o, p, ou v. Il faut donc indiqué une de ces 3 valeur en string (cf build Request)
    public static LinkedList<String> jsonResultToStrings(JSONArray arr, String valueTypeToExtract) {
        LinkedList<String> listStrings = new LinkedList<>();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject objI = arr.getJSONObject(i);
            JSONObject valueExtracted = objI.getJSONObject(valueTypeToExtract);

            // S'il y a une valeur pour chaque langue, on ne garde que celle en anglais
            String lang;
            try {
                lang = valueExtracted.getString("xml:lang");
                if (lang.equals("en")) {
                    listStrings.add(valueExtracted.getString("value"));
                }
            }
            // S'il n'y a pas de propriétés "langue", alors on l'ajoute
            catch (JSONException e) {
                listStrings.add(valueExtracted.getString("value"));
            }
        }

        return listStrings;
    }

    public static LinkedList<String[]> jsonSimilarResultToStrings(JSONArray arr) {
        LinkedList<String[]> listStrings = new LinkedList<>();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject objI = arr.getJSONObject(i);
            String valueExtracted = objI.getJSONObject("v").getString("value");
            String countExtracted = objI.getJSONObject("c").getString("value");
            String[] result = {valueExtracted, countExtracted};
            listStrings.add(result);
        }

        return listStrings;
    }

    // Renvoie null en cas de problème
    public static LinkedList<String> getObjectByPropertyValue(String property, String valueofProperty) {
        JSONArray gamesReturned = null;
        try {
            gamesReturned = sendRequest(null, property, valueofProperty);
        } catch (IOException ex) {
            Logger.getLogger(DBpediaClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (gamesReturned != null) {
            LinkedList<String> games = jsonResultToStrings(gamesReturned, "o");
            return games;
        }

        return new LinkedList<>();
    }

    // Renvoie null en cas de problème
    public static LinkedList<String> getObjectValueByProperty(String object, String property) {
        JSONArray gamesJsonReturned = null;
        try {
            gamesJsonReturned = sendRequest(object, property, null);
        } catch (IOException ex) {
            Logger.getLogger(DBpediaClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (gamesJsonReturned != null) {
            LinkedList<String> games = jsonResultToStrings(gamesJsonReturned, "v");
            return games;
        }

        return new LinkedList<>();
    }

    // Renvoie null en cas de problème
    public static LinkedList<String[]> getSimilarObjects(String object) {
        JSONArray gamesJsonReturned = null;
        try {
            gamesJsonReturned = sendSimilarRequest(object);
        } catch (IOException ex) {
            Logger.getLogger(DBpediaClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (gamesJsonReturned != null) {
            //LinkedList<String> games = jsonResultToStrings(gamesJsonReturned, "v");
            LinkedList<String[]> games = jsonSimilarResultToStrings(gamesJsonReturned);
            return games;
        }

        return new LinkedList<>();
    }

    public static JSONArray sendSimilarRequest(String object) throws IOException {
        String request = "select ?v (count(?p) as ?c) where { \n" +
                "  values ?game { <" + object + "> }\n" +
                "  ?v ?p ?o ; a <http://dbpedia.org/ontology/VideoGame> .\n" +
                "  ?game   ?p ?o .\n" +
                "  FILTER (?game != ?v)\n" +
                "}\n" +
                "group by ?v ?game\n" +
                "order by desc(?c)\n" +
                "limit 20";

        SparqlProcessor proc = new SparqlProcessor();
        
        return proc.sparqlQuery(request);
    }
}
