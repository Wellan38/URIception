/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexa4304.uriception;

import static hexa4304.uriception.DBpediaClient._requestParameters;
import static hexa4304.uriception.DBpediaSpotlightClient.callAPI;
import static hexa4304.uriception.TextExtractor.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.json.JSONException;
import org.xml.sax.SAXException;

/**
 *
 * @author Master-Kyuuby
 */
public class Main {
    
    public static void main(String[] args) throws IOException, JSONException, SAXException, ParserConfigurationException, XPathExpressionException
    {
        DBpediaClient.initRequestParameters();
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Que voulez vous tester ?");
        System.out.println("     1) SpotLight");
        System.out.println("     2) googleCustomSearchEngine et TextExtraction");
        System.out.println("     3) Recuperation d'uri googleCustomSearchEngine");
        System.out.println("     4) recuperer et afficher URI jeu");
        System.out.println("     5) affiche les jeux de survie present sur DBpedia");
        System.out.println("     6) recuperer et afficher infos jeu parsees");
        System.out.println("\n saisissez votre choix :");
        
        int choix = 0;
        boolean choiceIsGood = false;
        String title;
        GameInfo gameInfo;
        
        while (!choiceIsGood)
        {
            System.out.println("**");
            choix = sc.nextInt();
            switch (choix)
            {
                case 1 :
                    testSpotlight();
                    choiceIsGood = true;
                    break;
                case 2 :
                    testGSE();
                    choiceIsGood = true;
                    break;
                case 3 :
                    sc.nextLine();
                    System.out.print("Saisissez la requÃªte : ");
                    String request = sc.nextLine();
                    testGlobal(request);
                    choiceIsGood = true;
                    break;
                case 4 :
                    sc.nextLine();
                    System.out.print("Saisissez le nom du jeu (format DBPedia avec _ Ã  la place de \" \") : ");
                    title = sc.nextLine();
                    gameInfo = new GameInfo(title);
                    gameInfo.testGameInfoURI();
                    choiceIsGood = true;
                    break;
                case 5 :
                    sc.nextLine();
                    System.out.print("Affichage des jeux de survie");
                    LinkedList<String> listGames = DBpediaClient.getObjectByPropertyValue(
                            _requestParameters[DBpediaClient.InfoType.GENRES.value()],
                            "<http://dbpedia.org/resource/Survival_game>");
                    for(String t : listGames)
                    {
                        System.out.println(extractTextFromURIForDisplay(t));
                    }
                    choiceIsGood = true;
                case 6 :
                    sc.nextLine();
                    System.out.print("Saisissez le nom du jeu (format DBPedia avec _ Ã  la place de \" \") : ");
                    title = sc.nextLine();
                    gameInfo = new GameInfo(title);
                    gameInfo.testGameInfoDisplay();
                    choiceIsGood = true;
                    break;
                default:
                    break;
            }
        }
    }
    
    // Tests
    private static void testSpotlight()
    {      
        String test = "First documented in the 13th century, Berlin was the capital"
                + " of the Kingdom of Prussia (1701–1918), the German Empire (1871–1918),"
                + " the Weimar Republic (1919–33) and the Third Reich (1933–45). Berlin in"
                + " the 1920s was the third largest municipality in the world. After"
                + " World War II, the city became divided into East Berlin -- the capital "
                + "of East Germany -- and West Berlin, a West German exclave surrounded by "
                + "the Berlin Wall from 1961–89. Following German reunification in 1990, the ";

        LinkedList<String> listURI = new LinkedList<>();
        try {
            listURI = callAPI(test);
        } catch (Exception ex) {
            Logger.getLogger(DBpediaSpotlightClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (String URI : listURI)
        {
            System.out.print("URI : ");
            System.out.println(URI);
        };
    }
    
    private static void testGSE() throws IOException, JSONException, SAXException, ParserConfigurationException, XPathExpressionException
    {
        GoogleCustomSearchEngine gcse = new GoogleCustomSearchEngine("AIzaSyDmE16v9wqfViMfWWxkW07qCQQn2Or0uMI", "001556729754408094837:r86b9hjdnoe");
        List<String> urlList = new ArrayList();
        urlList = gcse.RequestSearch("Le seigneur des anneaux");
        TextExtractor te = new TextExtractor();
        List<String> rawTextList = new ArrayList();
        rawTextList = te.extractTextFromURLList(urlList);
        for (String l:rawTextList)
        {
            System.out.println(l);
        }
    }
    
    private static void testGlobal(String request) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException
    {
        GoogleCustomSearchEngine gcse = new GoogleCustomSearchEngine("AIzaSyDmE16v9wqfViMfWWxkW07qCQQn2Or0uMI", "001556729754408094837:r86b9hjdnoe");
        List<String> urlList = new ArrayList();
        urlList = gcse.RequestSearch(request);
        
        TextExtractor te = new TextExtractor();
        List<String> rawTextList = new ArrayList();
        rawTextList = te.extractTextFromURLList(urlList);
        for (String l:rawTextList)
        {
            LinkedList<String> listURI = new LinkedList<>();
            try {
                listURI = callAPI(l);
            } catch (Exception ex) {
                Logger.getLogger(DBpediaSpotlightClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            for (String URI : listURI)
            {
                System.out.print("URI : ");
                System.out.println(URI);
            };
        }
    }
}
