package hexa4304.uriception;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;

public class Node extends Parent {

    public static final double DEFAULT_RADIUS_X = 80;
    public static final double DEFAULT_RADIUS_Y = 15;
    public static final double DEFAULT_FONT = 10;
    public static final String DEFAULT_RELATION = "Relation";

    private double x;
    private double y;

    private URIObject object;

    private Explorer explorer;

    private Tooltip tooltip;

    public Node(double x, double y, URIObject o, Explorer e) {
        this.object = o;
        this.explorer = e;
        this.x = x;
        this.y = y;

        this.getChildren().add(this.makeEllipse());
        this.getChildren().add(this.makeButton());
    }

    public Node(URIObject o, Explorer e) {

        this.object = o;
        this.explorer = e;
        this.x = this.explorer.getGraphPane().getWidth() / 2;
        this.y = this.explorer.getGraphPane().getHeight() / 2;

        this.getChildren().add(this.makeEllipse());
        this.getChildren().add(this.makeButton());
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Explorer getExplorer() {
        return this.explorer;
    }

    public URIObject getObject() {
        return this.object;
    }

    private Ellipse makeEllipse() {
        Ellipse ellipse = new Ellipse();
        ellipse.setRadiusX(this.DEFAULT_RADIUS_X);
        ellipse.setRadiusY(this.DEFAULT_RADIUS_Y);
        ellipse.setCenterX(this.x);
        ellipse.setCenterY(this.y);
        ellipse.setFill(Color.WHITE);
        ellipse.setStroke(Color.DARKGREY);
        return ellipse;
    }

    private Button makeButton() {
        Button button = new Button(this.object.getTitle());
        button.setPrefWidth(120);
        button.setPrefHeight(20);
        button.setMaxWidth(120);
        button.setMaxHeight(20);
        button.setMinWidth(120);
        button.setMinHeight(20);
        button.setTooltip(new Tooltip(this.getObject().getTitle()));
        button.setFont(new Font(DEFAULT_FONT));
        button.setTranslateX(this.x - 18 - this.DEFAULT_RADIUS_X / 2);
        button.setTranslateY(this.y - 2 - this.DEFAULT_RADIUS_Y / 2);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Node.this.getExplorer().makeContent(new GameInfo(Node.this.getObject().getUri()));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        return button;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setObject(URIObject object) {
        this.object = object;
    }

    public void setExplorer(Explorer explorer) {
        this.explorer = explorer;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public void setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
    }
}
