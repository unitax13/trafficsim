package stuff.gui;

import java.util.ArrayList;

public class GraphNodesContainer {

    int n = 0;
    public ArrayList<GraphNode> graphNodes;

    public GraphNodesContainer(ArrayList<GraphNode> graphNodes) {
        this.graphNodes = graphNodes;
        n = graphNodes.size();
    }

    public int getSize() {
        return n;
    }

    public ArrayList<GraphNode> get() {
        return graphNodes;
    }

    public boolean isNode(int x, int y) {
        for (GraphNode gn: graphNodes) {
            if (x==gn.position.getX() && y==gn.position.getY()) {
                return true;
            }
        }
        return false;
    }
    //  TO MAKE IT MORE EFFICIENT FOR SOME BIGGER GRAPHS WE COULD SAVE OUR CHECKINGS AFTER WE DO THEM, AND LATER CHECK THEM FIRST
    public int containsNode(int x, int y) {
        for (int i=0; i<n; i++) {
            GraphNode gn = graphNodes.get(i);
            if (x==gn.position.getX() && y==gn.position.getY()) {
                return i;
            }
        }
        return -1;
    }



}
