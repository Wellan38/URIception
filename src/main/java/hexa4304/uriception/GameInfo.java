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
