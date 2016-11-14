package hexa4304.uriception;

import java.io.IOException;
import java.util.LinkedList;


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
    public GameInfo(String title) throws IOException
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
        
        getAllInformation();
    }
    
    // Récupère toutes les informations concernant le jeu.
    // *** Le titre du jeu doit être connu ***
    public void getAllInformation() throws IOException
    {
        String objectParameter = ":" + _titles.getFirst();
        
        _developers = DBpediaClient.getObjectValueByProperty(objectParameter, DBpediaClient._requestParameters[DBpediaClient.InfoType.DEVELOPERS.value()]);
        _designers = DBpediaClient.getObjectValueByProperty(objectParameter, DBpediaClient._requestParameters[DBpediaClient.InfoType.DESIGNERS.value()]);
        _publishers = DBpediaClient.getObjectValueByProperty(objectParameter, DBpediaClient._requestParameters[DBpediaClient.InfoType.PUBLISHERS.value()]);
        _releaseDates = DBpediaClient.getObjectValueByProperty(objectParameter, DBpediaClient._requestParameters[DBpediaClient.InfoType.RELEASE_DATES.value()]);
        _platforms = DBpediaClient.getObjectValueByProperty(objectParameter, DBpediaClient._requestParameters[DBpediaClient.InfoType.PLATFORMS.value()]);
        _descriptions = DBpediaClient.getObjectValueByProperty(objectParameter, DBpediaClient._requestParameters[DBpediaClient.InfoType.DESCRIPTIONS.value()]);
        _genres = DBpediaClient.getObjectValueByProperty(objectParameter, DBpediaClient._requestParameters[DBpediaClient.InfoType.GENRES.value()]);
    }
    
    
    // Getters
    public LinkedList<String> getDevelopers()
    {
        return _developers;
    }
    public LinkedList<String> getDesigners()
    {
        return _designers;
    }
    public LinkedList<String> getPublishers()
    {
        return _publishers;
    }
    public LinkedList<String> getReleaseDates()
    {
        return _releaseDates;
    }
    public LinkedList<String> getPlatforms()
    {
        return _platforms;
    }
    public LinkedList<String> getTitles()
    {
        return _titles;
    }
    public LinkedList<String> getDescriptions()
    {
        return _descriptions;
    }
    public LinkedList<String> getGenres()
    {
        return _genres;
    }
    
    
    // Récupère toutes les infos d'un jeu et affiche les genres de ce jeu
    public void testGameInfo() throws IOException
    {
        System.out.println("Genres du jeu :");
        for (String s : _genres)
        {
            System.out.println(s);
        }
    }
}
