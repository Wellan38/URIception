/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexa4304.uriception;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

import org.json.*;

/**
 *
 * @author Flo Macé
 */
public class GoogleCustomSearchEngine {
    
    private String customEngineIdentifier;
    private String APIKey;
    
    //String customEngineIdentifier = "001556729754408094837:r86b9hjdnoe";
    //String key = "AIzaSyDmE16v9wqfViMfWWxkW07qCQQn2Or0uMI";
    
    public GoogleCustomSearchEngine() {
    }
    
    public GoogleCustomSearchEngine (String key, String cx){
        APIKey = key;
        customEngineIdentifier = cx;
    }

    public String getCustomEngineIdentifier() {
        return customEngineIdentifier;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setCustomEngineIdentifier(String customEngineIdentifier) {
        this.customEngineIdentifier = customEngineIdentifier;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }
    
    public Pair<String, List<String>> RequestSearch(String searchText) throws IOException, JSONException{
        final String RESOURCE_PATH = "ressources.txt";
        final String PAGE_PATH = "page.txt";

        URL url = new URL("https://www.googleapis.com/customsearch/v1?key="+APIKey+ "&cx="+ customEngineIdentifier +"&q="+ URLEncoder.encode(searchText, "UTF-8")+"&alt=json");
        HttpURLConnection conn2 = (HttpURLConnection) url.openConnection();

        conn2.setRequestMethod("GET");
        conn2.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn2.getInputStream())));

        String res;
        String jsonString = "";

        while((res = br.readLine()) != null)
        {
            jsonString += res;
        }

        JSONObject requestResult = new JSONObject(jsonString);
        
        if (requestResult.has("spelling"))
        {
            searchText = requestResult.getJSONObject("spelling").getString("correctedQuery");
        }
        
        List<String> pageLinks = new ArrayList();
        
        if (requestResult.has("items"))
        {
            JSONArray items = requestResult.getJSONArray("items");
            
            for (int i = 0; i < items.length(); i++)
            {
                JSONObject obj = items.getJSONObject(i);
                String link = obj.getString("link");

                pageLinks.add(link);
            }

            File page_file = new File(PAGE_PATH);
            BufferedWriter PageWriter = new BufferedWriter(new FileWriter(page_file));
            System.out.println("\nPages:\n");
            for (String l : pageLinks)
            {
                System.out.println(l);
                PageWriter.write(l);
                PageWriter.newLine();
            }
            PageWriter.close();
        }

        return new Pair(searchText, pageLinks);
    }
}
