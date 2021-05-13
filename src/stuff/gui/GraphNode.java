package stuff.gui;

import javafx.geometry.Point2D;

public class GraphNode {

    Position position;
    GraphNode neighbours[] = new GraphNode[4];
    double distances[] = new double[4];

    GraphNode(Position position) {
        this.position = position;
    }

}
