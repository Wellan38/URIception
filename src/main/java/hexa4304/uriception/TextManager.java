/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexa4304.uriception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.jsoup.Jsoup;

import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 *
 * @author Flo Macé
 */
public class TextManager {

    // extrait le contenue de toutes les pages correspondant aux URLs dans URLList
    public List<List<String>> extractTextFromURLList(List<String> URLList) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException
    {
        System.out.println("extraction texte brut");
        List<List<String>> rawTexts = new ArrayList();
        for (String url:URLList)
        {
            Elements elements = extractText(url);
            List<String> paragraphs = new ArrayList();
            
            for (Element e : elements)
            {
                if (!e.text().equals(""))
                {
                    paragraphs.add(e.text());
                }
            }
            rawTexts.add(paragraphs);
        }
        return rawTexts;
    }
    
    private Elements extractText(String url) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException{
        System.out.println("        extraction text de la page "+url);
        String html = Jsoup.connect(url).get().html();
        
        return html2text(html);
    }
    
    private Elements html2text(String html) {
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");
        
        return paragraphs;
    }
    
    // filtre les URI pour ne conserver que celle correspondant a la requete initiale (utiliser aprés spotlight)
    public List<String> GetRelevantURI(List<String> URIList, String request)
    {
//        System.out.println("text filter");
        
        request = request.replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$", "").toLowerCase();
        String [] requestWord = request.split(" ");
        
        Set ignoredWords = new HashSet();
        
        // Anglais
        ignoredWords.add("the");
        ignoredWords.add("a");
        ignoredWords.add("of");
        ignoredWords.add("and");
        
        //Français
        ignoredWords.add("le");
        ignoredWords.add("la");
        ignoredWords.add("les");
        ignoredWords.add("de");
        ignoredWords.add("du");
        ignoredWords.add("des");
        ignoredWords.add("à");
        ignoredWords.add("au");
        ignoredWords.add("aux");
        ignoredWords.add("et");
        
        List<String> words = new ArrayList();
        
        for (int i = 0; i < requestWord.length; i++)
        {
            String w = requestWord[i];
            
            if (!w.equals("") && !ignoredWords.contains(w))
            {
                words.add(w);
            }
        }
        
        List<String> newURIList = new ArrayList();
        
        for(String uri: URIList)
        {
            String strTmp = uri.split("/")[4].replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$", "").toLowerCase();
            int counter=0;
            for(String w : words)
            {
                if(strTmp.contains(w))
                {
                    counter++;
                }
            }
            
            if (words.size() > 0)
            {
                if(((double)counter/(double)words.size()) >= 0.5){
                    newURIList.add(uri);
                }
            }
            else
            {
                newURIList.add(uri);
            }
            
        }
        
        return newURIList;
    }
    
    // Renvoie les données sous forme de texte à des fins d'affichage (sans le texte "dpbedia/ontology...")
    // Prend en paramètre une liste d'URI et met en forme des String pour afficher les données à un humain
    public static LinkedList<String> extractTextsListFromURIForDisplay(LinkedList<String> uri)
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
    public static String extractTextFromURIForDisplay(String uri)
    {
        // Si l'uri ne contient pas le mot dbpedia, c'est qu'il n'y a pas besoin de la parser
        if (uri.contains("dbpedia"))
        {
            String text = uri.substring(uri.lastIndexOf("/") + 1);
            text = text.replace("_", " ");
            return text;
        }
        
        return uri;
    }
}
