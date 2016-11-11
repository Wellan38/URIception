/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexa4304.uriception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    
    public TextManager(String API_key_path)
    {
    }

    public List<List<String>> extractTextFromURLList(List<String> URLList) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException
    {
        List<List<String>> rawTexts = new ArrayList();
        for (String url:URLList)
        {
            //extractText(url);
            
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
    
    public Elements extractText(String url) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException{
        String html = Jsoup.connect(url).get().html();
        
        return html2text(html);
    	
    }
    
    public static Elements html2text(String html) {
        Document doc = Jsoup.parse(html);
        
        Elements paragraphs = doc.select("p");
        
        return paragraphs;
    }
    
    public List<String> GetRelevantURI(List<String> URIList, String request)
    {
        request = request.replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$", "").toLowerCase();
        String [] requestWord = request.split(" ");
        
        List<String> words = new ArrayList();
        
        for (int i = 0; i < requestWord.length; i++)
        {
            String w = requestWord[i];
            
            if (w.length() > 3)
            {
                words.add(w);
            }
        }
        
        List<String> newURIList = new ArrayList();
        
        for(String uri: URIList)
        {
            String strTmp = uri.replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$", "").toLowerCase();
            int counter=0;
            for(String w : words)
            {
                if(strTmp.contains(w))
                {
                    counter++;
                }
            }
            if((counter/requestWord.length) >= 0.5){
                System.out.println(uri);
                newURIList.add(uri);
            }
        }
        
        return newURIList;
    }
}
