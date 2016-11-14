package hexa4304.uriception;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Relation extends Parent {

    private Node start;

    private Node end;

    private double xStart;
    private double yStart;
    private double xEnd;
    private double yEnd;

    private String text;

    public Relation() {
        this.xStart = 0;
        this.xEnd = 0;
        this.yStart = 0;
        this.yEnd = 0;
    }

    public Relation(Node s, Node e, String r) {
        this.start = s;
        this.end = e;

        this.text = r;

        xStart = s.getX();
        xEnd = e.getX();
        yStart = s.getY();
        yEnd = e.getY();

        double xText = xStart + (xStart - xEnd) / 2;
        double yText = yStart + (yStart - yEnd) / 2;

        Line l = new Line(this.xStart, this.yStart, this.xEnd, this.yEnd);
        this.getChildren().add(l);

        Text t = new Text(this.text);
        t.setX(xText);
        t.setY(yText);
        t.setFont(new Font(10));
        t.setFill(Color.BLUE);
        t.setRotate(Math.toDegrees(Math.atan((yStart - yEnd) / (xStart - xEnd))));
        this.getChildren().add(t);
    }
}
