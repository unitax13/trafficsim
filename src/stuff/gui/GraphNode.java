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
        if (capacity[i]<2 || passengers[i] <= capacity[i]) {
            return distances[i];
        } else {

            double multiplier = ((float) passengers[i] / (float) capacity[i]);

            if (multiplier > 1 && multiplier < 1.5) {//LINIOWE ZWIĘKSZENIE
                return distances[i]*multiplier;

            } else if (multiplier>1.5 && multiplier < 2) { //ZWIĘKSZENIE DO KWADRATU
                multiplier =multiplier*multiplier;
                return distances[i]*multiplier;

            } else if (multiplier>=2) { //ZWIĘKSZENIE DO SZEŚCIANU
                return distances[i]*multiplier*multiplier*multiplier;

            } else {
                return distances[i];
            }
        }
    }

}
