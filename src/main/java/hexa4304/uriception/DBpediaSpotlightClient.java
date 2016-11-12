package hexa4304.uriception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
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
    
    private static String getSpotlightResponse(String text, double confidence, int support) throws IOException {
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
 
    private static LinkedList<String> extractURI (String htmlSource)
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
}
