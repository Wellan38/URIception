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
        
        public int value()
        {
            return id;
        }
    }
    
    public static void initRequestParameters()
    {
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
    public static String sendRequest(String object, String predicate, String value) throws IOException
    {
        String request = buildRequest(object, predicate, value);
        // Cette url est looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooongue
        String url = "http://dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=PREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0D%0APREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0D%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0D%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0D%0APREFIX+foaf%3A+%3Chttp%3A%2F%2Fxmlns.com%2Ffoaf%2F0.1%2F%3E%0D%0APREFIX+dc%3A+%3Chttp%3A%2F%2Fpurl.org%2Fdc%2Felements%2F1.1%2F%3E%0D%0APREFIX+%3A+%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F%3E%0D%0APREFIX+dbpedia2%3A+%3Chttp%3A%2F%2Fdbpedia.org%2Fproperty%2F%3E%0D%0APREFIX+dbpedia%3A+%3Chttp%3A%2F%2Fdbpedia.org%2F%3E%0D%0APREFIX+skos%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2004%2F02%2Fskos%2Fcore%23%3E%0D%0A";
        url += URLEncoder.encode(request, "utf-8");
        url += "&output=json";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        con.setRequestMethod("GET"); // optional default is GET

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
    
    // Construit une requête SPARQL simple avec les paramètres objet, prédicat, valeur.
    // Si un paramètre n'est pas connu, entrer la valeur "null" ou une String vide.
    public static String buildRequest(String object, String predicate, String value)
    {
        String request = "SELECT * WHERE { ";
        
        request += (object == null || object.isEmpty()) ? "?o " : (object + " ");
        request += (predicate == null || predicate.isEmpty()) ? "?p " : (predicate + " ");
        request += (value == null || value.isEmpty()) ? "?v " : (value + " ");
        
        request += "}";
        
        return request;
    }
    
    // Extrait les données du document "json" (sous forme de String) envoyé en paramètre
    public static LinkedList<String> jsonResultToStrings(String json)
    {
        LinkedList<String> listStrings = new LinkedList<>();
        
        JSONObject obj = new JSONObject(json);
        JSONObject results = obj.getJSONObject("results");
        JSONArray arr = results.getJSONArray("bindings");
        
        for (int i = 0; i < arr.length(); i++)
        {
            JSONObject objI = arr.getJSONObject(i);
            JSONObject v = objI.getJSONObject("v");
            
            // S'il y a une valeur pour chaque langue, on ne garde que celle en anglais
            String lang;
            try {
                lang = v.getString("xml:lang");
                if (lang.equals("en"))
                {
                    listStrings.add(v.getString("value"));
                }
            }
            // S'il n'y a pas de propriétés "langue", alors on l'ajoute
            catch (JSONException e) {
                listStrings.add(v.getString("value"));
            }
        }
        
        return listStrings;
    }
    
    // Renvoie null en cas de problème
    public static LinkedList<String> getObjectByPropertyValue(String property, String valueofProperty)
    {       
        String gamesJsonReturned = null;
        try
        {
            gamesJsonReturned = sendRequest(null, property, valueofProperty);
        } catch (IOException ex)
        {
            Logger.getLogger(DBpediaClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(gamesJsonReturned != null)
        {
            LinkedList<String> games = jsonResultToStrings(gamesJsonReturned);
            return games;
        }

        return null;
    }
    
    // Renvoie null en cas de problème
    public static LinkedList<String> getObjectValueByProperty(String object, String property)
    {       
        String gamesJsonReturned = null;
        try
        {
            gamesJsonReturned = sendRequest(object, property, null);
        } catch (IOException ex)
        {
            Logger.getLogger(DBpediaClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(gamesJsonReturned != null)
        {
            LinkedList<String> games = jsonResultToStrings(gamesJsonReturned);
            return games;
        }

        return null;
    }
}
