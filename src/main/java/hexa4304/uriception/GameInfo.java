package hexa4304.uriception;

import java.io.IOException;
import java.util.LinkedList;
import org.json.JSONObject;
import org.json.JSONArray;


/* classe GameInfo :
 * Permet de récupérer les informations concernant un jeu à partir de son titre.
 * Les informations sont récupérées en questionnant DBpedia à partir de requêtes SPARQL.
*/


public class GameInfo {
    // Attributs ---------------------------------------------------------------
    private LinkedList<String> _developers;
    private LinkedList<String> _designers;
    private LinkedList<String> _publishers;
    private LinkedList<String> _releaseDates;
    private LinkedList<String> _platforms;
    private LinkedList<String> _titles;
    private LinkedList<String> _descriptions;
    private LinkedList<String> _genres;
    
    // Paramètres dans la requête SPARQL correspondant aux infos souhaitées
    private String[] _requestParameters;
    
    
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
    
    
    // Méthodes ----------------------------------------------------------------
    public GameInfo(String title)
    {
        _titles = new LinkedList<>();
        _titles.add(title);
        _developers = new LinkedList<>();
        _designers = new LinkedList<>();
        _publishers = new LinkedList<>();
        _releaseDates = new LinkedList<>();
        _platforms = new LinkedList<>();
        _descriptions = new LinkedList<>();
        _genres = new LinkedList<>();
        
        initRequestParameters();
    }
    
    private void initRequestParameters()
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
    
    
    public void getAllInformation() throws IOException
    {
        String objectParameter = ":" + _titles.getFirst();
        
        //_releaseDates = DBpediaClient.sendRequest(objectParameter, _requestParameters[InfoType.RELEASE_DATE.value()], "");
        
        // Parse JSON
        /*
        JSONObject obj = new JSONObject(" .... ");
        String pageName = obj.getJSONObject("pageInfo").getString("pageName");

        JSONArray arr = obj.getJSONArray("posts");
        for (int i = 0; i < arr.length(); i++)
        {
            String post_id = arr.getJSONObject(i).getString("post_id");
            ......
        }
        */
    }
    
    public void test() throws IOException
    {
        getAllInformation();
        //System.out.println(_releaseDate);
        //System.out.println(_genres);
    }
}
