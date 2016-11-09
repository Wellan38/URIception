package hexa4304.uriception;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Explorer extends Application {

    @FXML
    private Text title;

    @FXML
    private ImageView image;

    @FXML
    private Text content;

    @FXML
    private Pane graphPane;

    private static final String DEFAULT_TITLE = "Aucun objet sélectionné !";
    private static final String DEFAULT_IMAGE_URL = "http://www.knowledgebrain.com/NothingImages/Nothing_512_512.jpg";
    private static final String DEFAULT_CONTENT = "Aucun objet sélectionné !";

    public static void main(String[] args) {
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

            this.graphPane = (Pane) scene.lookup("#graph");

            this.title.setText(DEFAULT_TITLE);
            this.content.setText(DEFAULT_CONTENT);
            this.image.setImage(new Image(DEFAULT_IMAGE_URL));

            // TEST
            URIObject object = new URIObject();
            ArrayList<URIObject> list = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                list.add(new URIObject());
            }
            this.makeGraph(object, list);

        } catch (IOException e) {
            System.out.println(e);
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

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setContent(String content) {
        this.content.setText(content);
    }

    public void makeGraph(URIObject mainObject, ArrayList<URIObject> objects) {
        Node mainNode = new Node(mainObject, this);
        int numberOfPoints = objects.size();
        Node nodes[] = new Node[numberOfPoints];
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
        this.addNode(mainNode);
        for (int j = 0; j < numberOfPoints; j++) {
            this.addNode(nodes[j]);
        }
    }

    public Pane getGraphPane() {
        return this.graphPane;
    }

}

