package hexa4304.uriception;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Explorer extends Application {

    @FXML
    private Text title;

    @FXML
    private ImageView image;

    private TextFlow contentFlow;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Pane graphPane;

    private static final String DEFAULT_TITLE = "Aucun objet sélectionné !";
    private static final String DEFAULT_IMAGE_URL = "http://www.knowledgebrain.com/NothingImages/Nothing_512_512.jpg";

    public static void main(String[] args) {
        DBpediaClient.initRequestParameters();
        Application.launch(Explorer.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/hexa4304/uriception/explorer.fxml"));
            primaryStage.setTitle("Web Semantique - H4304");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

            this.title = (Text) scene.lookup("#title");
            this.contentFlow = (TextFlow) scene.lookup("#contentFlow");
            this.image = (ImageView) scene.lookup("#image");
            this.searchField = (TextField) scene.lookup("#searchField");
            this.searchButton = (Button) scene.lookup("#searchButton");

            this.searchButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        String search = Explorer.this.getSearchField().getText().trim();
                        URIFinder uriFinder = new URIFinder();
                        ArrayList<String> list = (ArrayList<String>) uriFinder.dbpediaSearch(search);
                        makeSearchResult(search, list);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });

            this.graphPane = (Pane) scene.lookup("#graph");

            this.title.setText(DEFAULT_TITLE);
            this.image.setImage(new Image(DEFAULT_IMAGE_URL));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNode(Node node) {
        node.toFront();
        this.graphPane.getChildren().add(node);
    }

    public void addRelation(Relation relation) {
        relation.toBack();
        this.graphPane.getChildren().add(relation);
    }

    public void clearGraphPane() {
        this.graphPane.getChildren().clear();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void makeSearchResult(String search, ArrayList<String> list) {
        this.title.setText("Resultat pour '" + search + "'");
        clearContentFlow();
        for (String uri : list) {
            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setText(TextExtractor.extractTextFromURIForDisplay(uri));
            hyperlink.setPrefWidth(280);
            hyperlink.setMaxWidth(280);
            hyperlink.setMinWidth(280);
            hyperlink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        Explorer.this.makeContent(new GameInfo(uri));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
            Explorer.this.addContentToFlow(hyperlink);
        }
    }

    public void makeContent(GameInfo info) {
        clearContentFlow();
        URIObject mainObject = new URIObject();
        String mainUri = info.getURITitles().get(0);
        String title = TextExtractor.extractTextFromURIForDisplay(mainUri);
        String[][] content = makeObjectContent(info);
        for (int i = 0; i < content.length; i++) {
            Text label = new Text(content[i][0] + "\n");
            label.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            addContentToFlow(label);
            Text value = new Text(content[i][1] + "\n\n");
            value.setWrappingWidth(280);
            addContentToFlow(value);
        }
        mainObject.setTitle(title);
        mainObject.setUri(mainUri);
        this.setTitle(title);
        LinkedList<URIObject> relatedObjects = new LinkedList<>();
        LinkedList<String[]> related = DBpediaClient.getSimilarObjects(mainUri);
        int i = 0;
        for (String[] uri : related) {
            if (i >= 20) {
                break;
            }
            URIObject o = new URIObject();
            String readableTitle = TextExtractor.extractTextFromURIForDisplay(uri[0]);
            o.setTitle(readableTitle);
            o.setUri(uri[0]);
            o.setRelation("Identical:" + uri[1]);
            relatedObjects.add(o);
            i++;
        }
        makeGraph(mainObject, relatedObjects);
    }

    public void makeGraph(URIObject mainObject, LinkedList<URIObject> objects) {
        this.clearGraphPane();
        Node mainNode = new Node(mainObject, this);
        int numberOfPoints = objects.size();
        Node nodes[] = new Node[numberOfPoints];
        if (numberOfPoints > 0) {
            double angleIncrement = 360 / numberOfPoints;
            double xRadius = 310;
            double yRadius = 300;
            int i = 0;
            for (URIObject o : objects) {
                double x = (xRadius * Math.cos((angleIncrement * i) * (Math.PI / 180))) + mainNode.getX();
                double y = (yRadius * Math.sin((angleIncrement * i) * (Math.PI / 180))) + mainNode.getY();
                double deltaX = x - mainNode.getX();
                if (deltaX > 5) {
                    x += 50;
                } else if (deltaX < -5) {
                    x -= 50;
                }
                Node n = new Node(x, y, o, this);
                this.addRelation(new Relation(mainNode, n, TextExtractor.extractTextFromURIForDisplay(o.getRelation())));
                nodes[i] = n;
                i++;
            }
        }
        this.addNode(mainNode);
        for (int j = 0; j < numberOfPoints; j++) {
            this.addNode(nodes[j]);
        }
    }

    public String[][] makeObjectContent(GameInfo info) {
        String[][] content = new String[6][2];

        content[0][0] = "Developers :";
        String developers = "";
        for (String developer : info.getURIDevelopers()) {
            developers += TextExtractor.extractTextFromURIForDisplay(" " + developer + ",");
        }
        if (developers.length() > 0) {
            developers = developers.substring(0, developers.length() - 1);
        }
        content[0][1] = developers;

        content[1][0] = "Genres :";
        String genres = "";
        for (String genre : info.getURIGenres()) {
            genres += TextExtractor.extractTextFromURIForDisplay(" " + genre + ",");
        }
        if (genres.length() > 0) {
            genres = genres.substring(0, genres.length() - 1);
        }
        content[1][1] = genres;

        content[2][0] = "Platforms :";
        String platforms = "";
        for (String platform : info.getURIPlatforms()) {
            platforms += TextExtractor.extractTextFromURIForDisplay(" " + platform + ",");
        }
        if (platforms.length() > 0) {
            platforms = platforms.substring(0, platforms.length() - 1);
        }
        content[2][1] = platforms;

        content[3][0] = "Publishers :";
        String publishers = "";
        for (String publisher : info.getURIPublishers()) {
            publishers += TextExtractor.extractTextFromURIForDisplay(" " + publisher + ",");
        }
        if (publishers.length() > 0) {
            publishers = publishers.substring(0, publishers.length() - 1);
        }
        content[3][1] = publishers;

        content[4][0] = "Release date :";
        String releases = "";
        for (String release : info.getURIReleaseDates()) {
            releases += TextExtractor.extractTextFromURIForDisplay(" " + release + ",");
        }
        if (releases.length() > 0) {
            releases = releases.substring(0, releases.length() - 1);
        }
        content[4][1] = releases;

        content[5][0] = "Description :";
        String descriptions = "";
        for (String description : info.getURIDescriptions()) {
            descriptions += " " + description + ",";
        }
        if (descriptions.length() > 0) {
            descriptions = descriptions.substring(0, descriptions.length() - 1);
        }
        content[5][1] = descriptions;

        return content;
    }

    public Pane getGraphPane() {
        return this.graphPane;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public void addContentToFlow(javafx.scene.Node node) {
        this.contentFlow.getChildren().add(node);
    }

    public void clearContentFlow() {
        this.contentFlow.getChildren().clear();
    }
}

