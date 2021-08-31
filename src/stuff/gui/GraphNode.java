package stuff.gui;

import stuff.gui.utils.Position;

public class GraphNode {

    public Position position;
    public GraphNode neighbours[] = new GraphNode[4];
    public double distances[] = new double[4];
    public int passengers[] = new int[4];
    public int capacity[] = new int[4];

    public GraphNode(Position position) {
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

            if (multiplier > 1) {
                return distances[i]*multiplier*multiplier*multiplier;

//            if (multiplier > 1 && multiplier < 1.5) {//LINIOWE ZWIĘKSZENIE
//                return distances[i]*multiplier;
//
//            } else if (multiplier>1.5 && multiplier < 2) { //ZWIĘKSZENIE DO KWADRATU
//                multiplier =multiplier*multiplier;
//                return distances[i]*multiplier;
//
//            } else if (multiplier>=2) { //ZWIĘKSZENIE DO SZEŚCIANU
//                return distances[i]*multiplier*multiplier*multiplier;

            } else {
                return distances[i];
            }
        }
    }

}
