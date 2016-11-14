package hexa4304.uriception;


import static hexa4304.uriception.DBpediaSpotlightClient.callAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.util.Pair;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Flo Macé
 */
public class URIFinder {


    private final static String WIKIPEDIA_ID = "001556729754408094837:r86b9hjdnoe"; // 1
    private final static String DBPEDIA_ID = "001556729754408094837:hksxp-tujys"; // 1

    //    private final static String API_KEY = "AIzaSyDmE16v9wqfViMfWWxkW07qCQQn2Or0uMI"; // 1
//    private final static String API_KEY = "AIzaSyDW9tp9BvomeZG2OagHAeolEEyCL0VurJc"; // 2
    private final static String API_KEY = "AIzaSyA9BlmezjrVu-kNXDQnr47UoMhl85V--G0"; // 3

    // moteur de recherche sur Wikipedia
    public List<String> wikipediaSearch(String request) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        System.out.println("recherche wikipedia");

        // initialisation du moteur de recherche sur wikipedia avec la clé de l'API google et l'identifiant du moteur de recherche.
        GoogleCustomSearchEngine gcse = new GoogleCustomSearchEngine(API_KEY, WIKIPEDIA_ID);
        List<String> urlList = new ArrayList();

        // recuperation des resultats (par pair <Requete corrigé, url>
        // recutperaton de la requete corrigé (eviter faute d'orthographe) pour le premier filre (dans textManager)
        Pair<String, List<String>> result = gcse.RequestSearch(request, 1);

        urlList = result.getValue();

        request = result.getKey();

        TextManager te = new TextManager();
        List<List<String>> rawTextList = new ArrayList();

        // extraction du texte brut de toutes les URL
        rawTextList = te.extractTextFromURLList(urlList);
        Set set = new HashSet();

        for (List<String> paragraphs : rawTextList) {
            for (String p : paragraphs) {
                try {
                    System.out.println("fin appel a spotlight");
                    set.addAll(callAPI(p));
                } catch (Exception ex) {
                    Logger.getLogger(DBpediaSpotlightClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
//        System.out.println("fin appel a spotlight");

        LinkedList<String> listURI = new LinkedList(set);

        List<String> refineUriList = te.GetRelevantURI(listURI, request);
        return sparqlFilter(refineUriList);
    }

    public List<String> dbpediaSearch(String request) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
//        System.out.println("recherche dbpedia.");

        // initialisation du moteur de recherche sur dbpedia avec la clé de l'API google et l'identifiant du moteur de recherche.
        GoogleCustomSearchEngine gcse = new GoogleCustomSearchEngine(API_KEY, DBPEDIA_ID);
        List<String> urlList = new ArrayList();

        int page = 1;

        TextManager te = new TextManager();

        List<String> listURI = new ArrayList();

        StringBuilder sb = new StringBuilder();

        while (listURI.size() < 10 && page <= 5) {
//            System.out.println("extraction url page "+page);

            // recuperation des resultats (par pair <Requete corrigé, url>
            // recutperaton de la requete corrigé (eviter faute d'orthographe) pour le premier filre (dans textManager)
            Pair<String, List<String>> result = gcse.RequestSearch(request, page);

            urlList = result.getValue();

            request = result.getKey();

            for (String url : urlList) {
                // magouille pour recuperer les ressources associé aux url recuperé suite a la recherche dbpedia en remplaçant le "page" par "ressource" dans l'url
                String[] words = url.split("/");

                if (words.length >= 4) {
                    words[2] = "dbpedia.org";
                    words[3] = "resource";

                    url = String.join("/", words);

                    listURI.add(url);
                }
            }

            page++;
        }

        List<String> refineUriList = te.GetRelevantURI(listURI, request);
        return sparqlFilter(refineUriList);
    }

    // filtre pour ne conserver que les uri correspondant a des jeux videos.
    private List<String> sparqlFilter(List<String> URIList) throws IOException {
        SparqlProcessor sp = new SparqlProcessor();

        List<String> listURI = sp.URIFilter(URIList);
        return listURI;
    }

}
