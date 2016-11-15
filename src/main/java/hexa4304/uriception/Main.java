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
 * @author Master-Kyuuby
 */
public class Main {

    public static void main(String[] args) throws IOException, JSONException, SAXException, ParserConfigurationException, XPathExpressionException {
        DBpediaClient.initRequestParameters();

        Scanner sc = new Scanner(System.in);
        System.out.println("Que voulez vous tester ?");
        System.out.println("     1) recuperer et afficher URI jeu");
        System.out.println("     2) affiche les jeux de survie present sur DBpedia");
        System.out.println("     3) recuperer et afficher infos jeu parsees");
        System.out.println("\n saisissez votre choix :");

        int choix = 0;
        boolean choiceIsGood = false;
        String title;
        GameInfo gameInfo;

        while (!choiceIsGood) {
            System.out.println("**");
            choix = sc.nextInt();
            switch (choix) {
                case 1:
                    sc.nextLine();
                    System.out.print("Saisissez le nom du jeu sous forme d'URI DBpedia : ");
                    title = sc.nextLine();
                    gameInfo = new GameInfo(title);
                    gameInfo.testGameInfoURI();
                    choiceIsGood = true;
                    break;
                case 2:
                    sc.nextLine();
                    System.out.print("Affichage des jeux de survie");
                    LinkedList<String> listGames = DBpediaClient.getObjectByPropertyValue(
                            _requestParameters[DBpediaClient.InfoType.GENRES.value()],
                            "<http://dbpedia.org/resource/Survival_game>");
                    System.out.println();
                    for (String t : listGames) {
                        System.out.println(extractTextFromURIForDisplay(t));
                    }
                    choiceIsGood = true;
                    break;
                case 3:
                    sc.nextLine();
                    System.out.print("Saisissez le nom du jeu sous forme d'URI DBpedia : ");
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

}
