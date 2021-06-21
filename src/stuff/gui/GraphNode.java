package stuff.gui;

import javafx.geometry.Point2D;

public class GraphNode {

    Position position;
    GraphNode neighbours[] = new GraphNode[4];
    double distances[] = new double[4];
    int passengers[] = new int[4];
    int capacity[] = new int[4];

    GraphNode(Position position) {
        this.position = position;
    }

    public int getAllPassengers() {
        int passengers1 = 0;
        for (int i=0; i<4; i++) {
            passengers1+=passengers[i];
        }
        return passengers1;
    }

    public double getDistance(int i) {
        if (capacity[i]<4 || passengers[i] <= capacity[i]) {
            return distances[i];
        } else {
            //PROSTE LINIOWE ZWIĘKSZENIE
            double multiplier = ((float) passengers[i] / (float) capacity[i]);

            if (multiplier > 1 && multiplier < 1.5) {
                return distances[i]*multiplier;
            } else if (multiplier>1.5 && multiplier < 2) {
                multiplier =multiplier*multiplier;
                return distances[i]*multiplier;
            } else if (multiplier>=2) {
                return distances[i]*multiplier*multiplier*multiplier;
                // Double.MAX_VALUE;
            } else {
                return distances[i];
            }
        }
    }

}
