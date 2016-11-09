/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexa4304.uriception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;

import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;



/**
 *
 */
public class TextExtractor {
    
    public TextExtractor(String API_key_path)
    {
    }

    public List<String> extractTextFromURLList(List<String> URLList) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException
    {
        List<String> rawTexts = new ArrayList();
        for (String url:URLList)
        {
            //extractText(url);
            rawTexts.add(extractText(url));
        }
        return rawTexts;
    }
    
    public String extractText(String url) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException{
        String html = Jsoup.connect(url).get().html();
        return html2text(html);
    	
    }
    
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
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
        String text = uri.substring(uri.lastIndexOf("/"));
        text = text.replace("_", " ");
        return text;
    }
}
