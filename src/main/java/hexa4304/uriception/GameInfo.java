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
    // Renvoie les données sous forme d'URI, avec par exemple le texte "dbpedia/ontology..."
    public LinkedList<String> getURIDevelopers()
    {
        return _developers;
    }
    public LinkedList<String> getURIDesigners()
    {
        return _designers;
    }
    public LinkedList<String> getURIPublishers()
    {
        return _publishers;
    }
    public LinkedList<String> getURIReleaseDates()
    {
        return _releaseDates;
    }
    public LinkedList<String> getURIPlatforms()
    {
        return _platforms;
    }
    public LinkedList<String> getURITitles()
    {
        return _titles;
    }
    public LinkedList<String> getURIDescriptions()
    {
        return _descriptions;
    }
    public LinkedList<String> getURIGenres()
    {
        return _genres;
    }
    
    // Renvoie les données sous forme de texte à des fins d'affichage (sans le texte "dpbedia/ontology...")
    public LinkedList<String> getDevelopersForDisplay()
    {
        return extractTextsListFromURIForDisplay(_developers);
    }
    public LinkedList<String> getDesignersForDisplay()
    {
        return extractTextsListFromURIForDisplay(_designers);
    }
    public LinkedList<String> getPublishersForDisplay()
    {
        return extractTextsListFromURIForDisplay(_publishers);
    }
    public LinkedList<String> getReleaseDatesForDisplay()
    {
        return extractTextsListFromURIForDisplay(_releaseDates);
    }
    public LinkedList<String> getPlatformsForDisplay()
    {
        return extractTextsListFromURIForDisplay(_platforms);
    }
    public LinkedList<String> getTitlesForDisplay()
    {
        return extractTextsListFromURIForDisplay(_titles);
    }
    public LinkedList<String> getDescriptionsForDisplay()
    {
        return extractTextsListFromURIForDisplay(_descriptions);
    }
    public LinkedList<String> getGenresForDisplay()
    {
        return extractTextsListFromURIForDisplay(_genres);
    }
    
    // Prend en paramètre une liste d'URI et met en forme des String pour afficher les données à un humain
    private LinkedList<String> extractTextsListFromURIForDisplay(LinkedList<String> uri)
    {
        LinkedList<String> textsForDisplay = new LinkedList<>();
        for (String s : uri)
        {
            textsForDisplay.add(extractTextFromURIForDisplay(s));
        }
        return textsForDisplay;
    }
    
    // Prend en paramètre une URI et met en forme une String pour afficher la donnée à un humain
    // Par exemple, prend en paramètre "http://dbpedia.org/resource/PlayStation_4" et renvoie "PlayStation 4"
    private String extractTextFromURIForDisplay(String uri)
    {
        String text = uri.substring(uri.lastIndexOf("/"));
        text = text.replace("_", " ");
        return text;
    }
    
    
    // Récupère et affiche toutes les infos d'un jeu
    public void testGameInfoURI() throws IOException
    {
        getAllInformation();
        
        System.out.println();
        System.out.println("- Titre(s) du jeu :");
        for (String s : _titles)
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Genres du jeu :");
        for (String s : _genres)
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Description(s) du jeu :");
        for (String s : _descriptions)
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Développeur(s) du jeu :");
        for (String s : _developers)
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Designer(s) du jeu :");
        for (String s : _designers)
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Editeur(s) du jeu :");
        for (String s : _publishers)
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Date(s) de sortie du jeu :");
        for (String s : _releaseDates)
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Plateforme(s) du jeu :");
        for (String s : _platforms)
        {
            System.out.println(s);
        }
    }
    
    public void testGameInfoDisplay() throws IOException
    {
        getAllInformation();
        
        System.out.println();
        System.out.println("- Titre(s) du jeu :");
        for (String s : getTitlesForDisplay())
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Genres du jeu :");
        for (String s : getGenresForDisplay())
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Description(s) du jeu :");
        for (String s : getDescriptionsForDisplay())
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Développeur(s) du jeu :");
        for (String s : getDevelopersForDisplay())
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Designer(s) du jeu :");
        for (String s : getDesignersForDisplay())
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Editeur(s) du jeu :");
        for (String s : getPublishersForDisplay())
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Date(s) de sortie du jeu :");
        for (String s : getReleaseDatesForDisplay())
        {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("- Plateforme(s) du jeu :");
        for (String s : getPlatformsForDisplay())
        {
            System.out.println(s);
        }
    }
}
