package stuff.gui;

import javafx.geometry.Point2D;

public class GraphNode {

    Point2D position;
    GraphNode neighbours[] = new GraphNode[4];
    double distances[] = new double[4];

    GraphNode(Point2D position) {
        this.position = position;
    }

}
