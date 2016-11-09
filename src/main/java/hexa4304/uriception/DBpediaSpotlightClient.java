package hexa4304.uriception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

public class DBpediaSpotlightClient {

    private final static String API_URL = "http://spotlight.sztaki.hu:2222/";
    private static Exception SpotlightCallException;
    private final static int PARAGRAPH_LENGTH = 10;

    public static String getSpotlightResponse(String text, double confidence, int support) throws IOException {
            String url =    API_URL + "rest/annotate/?"
                            + "confidence=" + confidence
                            + "&support=" + support
                            + "&text=" + URLEncoder.encode(text, "utf-8");

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            return response.toString();
    }
    
    private static List<String> splitText(String text)
    {
//        List<String> paragraphs = new ArrayList();
//        
//        String[] words = text.split(" ");
//        
//        int nb_paragraphs = words.length / PARAGRAPH_LENGTH + 1;
//        
//        for (int i = 0; i < nb_paragraphs; i++)
//        {
//            String parag = "";
//            
//            for (int j = i * PARAGRAPH_LENGTH; i < (i+1) * PARAGRAPH_LENGTH; j++)
//            {
//                parag += words[j] + " ";
//            }
//            
//            paragraphs.add(parag);
//        }
//        
//        return paragraphs;

        List<String> paragraphs = new ArrayList();
          
        boolean splitting = true;
        while(splitting)
        {            
            if(text.length() > PARAGRAPH_LENGTH)
            {
                paragraphs.add(text.substring(0, PARAGRAPH_LENGTH));
                text = text.substring(PARAGRAPH_LENGTH,text.length()); 
            }
            else
            {
                paragraphs.add(text);
                splitting = false;
            }

        }
        
        return paragraphs;
    }
    
    public static LinkedList<String> extractURI (String htmlSource)
    {
        LinkedList<String> listUri = new LinkedList<>();
        Document doc = Jsoup.parse(htmlSource);
        
        Elements body = doc.getElementsByTag("body");
        Elements uriElements = body.get(0).getElementsByTag("a");
        
        for(Element balise  : uriElements)
        {
            listUri.push(balise.attributes().get("title"));
        }
            
        return listUri;
    }
    
    public static LinkedList<String> callAPI(String text) throws Exception
    {
        String htmlResponse = "";       
        try {
            htmlResponse = DBpediaSpotlightClient.getSpotlightResponse(text, 0.8, 0);
        } catch (IOException ex) {
            Logger.getLogger(DBpediaSpotlightClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return extractURI(htmlResponse);
    }
    
    public static void main(String[] args) throws IOException, JSONException, SAXException, ParserConfigurationException, XPathExpressionException
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Que voulez vous tester ?");
        System.out.println("     1) SpotLigth");
        System.out.println("     2) googleCustomSearchEngine et TextExtraction");
        System.out.println("     3) Test Global");
        System.out.println("     4) URI Filter");
        System.out.println("\n saisissez votre choix :");
        
        int choix = 0;
        boolean choiceIsGood = false;
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
                    System.out.print("Saisissez la requête : ");
                    String request = sc.nextLine();
                    testGlobal(request);
                    System.out.println("ok");
                    choiceIsGood = true;
                    break;
//                case 4 :
//                    testJenaArq();
//                    choiceIsGood = true;
//                    break;
                default:
                    break;
            }
        }
    }
    
    public static void testSpotlight()
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
    
    public static void testGSE() throws IOException, JSONException, SAXException, ParserConfigurationException, XPathExpressionException
    {
        GoogleCustomSearchEngine gcse = new GoogleCustomSearchEngine("AIzaSyDmE16v9wqfViMfWWxkW07qCQQn2Or0uMI", "001556729754408094837:r86b9hjdnoe");
        List<String> urlList = new ArrayList();
        urlList = gcse.RequestSearch("Le seigneur des anneaux");
//        for(String l: urlList)
//        {
//            System.out.println(l);
//        }
//        System.out.println();
        TextManager te = new TextManager("api_key.txt");
        List<String> rawTextList = new ArrayList();
        //rawTextList = te.extractTextFromURLList(urlList);
        for (String l:rawTextList)
        {
            System.out.println(l);
        }
    }
    
    public static void testGlobal(String request) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException
    {
        GoogleCustomSearchEngine gcse = new GoogleCustomSearchEngine("AIzaSyDmE16v9wqfViMfWWxkW07qCQQn2Or0uMI", "001556729754408094837:r86b9hjdnoe");
        List<String> urlList = new ArrayList();
        urlList = gcse.RequestSearch(request);
        
        TextManager te = new TextManager("api_key.txt");
        List<List<String>> rawTextList = new ArrayList();
        rawTextList = te.extractTextFromURLList(urlList);
        
        Set set = new HashSet();
        
        for (List<String> paragraphs:rawTextList)
        {
            for (String p : paragraphs)
            {
                try
                {
                    set.addAll(callAPI(p));
                }
                catch (Exception ex)
                {
                    Logger.getLogger(DBpediaSpotlightClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        LinkedList<String> listURI = new LinkedList(set);
        List<String> refineUriList = te.GetRelevantURI(listURI, request);
        testJenaArq(refineUriList);
        
//        for (String URI : listURI)
//        {
//            System.out.print("URI : ");
//            System.out.println(URI);
//        };
    }
    
    public static void testJenaArq(List<String> URIList)
    {
        
        SparqlProcessor sp = new SparqlProcessor();
        
        List<String> listURI = sp.URIFilter(URIList);
        for (String URI : listURI)
        {
            System.out.print("URI : ");
            System.out.println(URI);
        };
    }
}
