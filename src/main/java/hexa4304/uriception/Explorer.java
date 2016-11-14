package hexa4304.uriception;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Explorer extends Application {

    @FXML
    private Text title;

    @FXML
    private ImageView image;

    @FXML
    private Text content;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Pane graphPane;

    private static final String DEFAULT_TITLE = "Aucun objet sélectionné !";
    private static final String DEFAULT_IMAGE_URL = "http://www.knowledgebrain.com/NothingImages/Nothing_512_512.jpg";
    private static final String DEFAULT_CONTENT = "Aucun objet sélectionné !";

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
            this.content = (Text) scene.lookup("#content");
            this.image = (ImageView) scene.lookup("#image");
            this.searchField = (TextField) scene.lookup("#searchField");
            this.searchButton = (Button) scene.lookup("#searchButton");

            this.searchButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        Explorer.this.makeContent(new GameInfo(Explorer.this.getSearchField().getText().trim()));
                    } catch(Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });

            this.graphPane = (Pane) scene.lookup("#graph");

            this.title.setText(DEFAULT_TITLE);
            this.content.setText(DEFAULT_CONTENT);
            this.image.setImage(new Image(DEFAULT_IMAGE_URL));

            // TEST
            URIObject object = new URIObject();
            LinkedList<URIObject> list = new LinkedList<>();
            for (int i = 0; i < 20; i++) {
                list.add(new URIObject());
            }
            this.makeGraph(object, list);

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

    public void setContent(String content) {
        this.content.setText(content);
    }

    public void makeContent(GameInfo info) {
        URIObject mainObject = new URIObject();
        String title = info.getTitles().get(0);
        String content = "";
        mainObject.setTitle(title);
        mainObject.setContent(content);
        this.setTitle(title);
        this.setContent(content);
        String genre = info.getGenres().get(0);
        /*Pattern pattern = Pattern.compile("[^, /]+$");
        Matcher matcher = pattern.matcher(genre);
        if (matcher.find())
        {
            genre = matcher.group(0);
        }*/
        LinkedList<URIObject> relatedObjects = new LinkedList<>();
        LinkedList<String> related = DBpediaClient.getObjectByPropertyValue(DBpediaClient._requestParameters[DBpediaClient.InfoType.GENRES.value()], genre);
        System.out.println(related.get(0));
        int i = 0;
        for(String uri : related) {
            if(i >= 20) {
                break;
            }
            URIObject o = new URIObject();
            o.setTitle(uri);
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
        if(numberOfPoints > 0) {
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
                Node n = new Node(x, y, o, this, Node.DEFAULT_RELATION);
                this.addRelation(new Relation(mainNode, n));
                nodes[i] = n;
                i++;
            }
        }
        this.addNode(mainNode);
        for (int j = 0; j < numberOfPoints; j++) {
            this.addNode(nodes[j]);
        }
    }

    public Pane getGraphPane() {
        return this.graphPane;
    }

    public Text getTitle() {
        return title;
    }

    public void setTitle(Text title) {
        this.title = title;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public Text getContent() {
        return content;
    }

    public void setContent(Text content) {
        this.content = content;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public void setSearchField(TextField searchField) {
        this.searchField = searchField;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(Button searchButton) {
        this.searchButton = searchButton;
    }

    public void setGraphPane(Pane graphPane) {
        this.graphPane = graphPane;
    }
}

