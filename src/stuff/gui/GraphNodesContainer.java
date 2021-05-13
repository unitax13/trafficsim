package stuff.gui;

import java.util.ArrayList;

public class GraphNodesContainer {


    public ArrayList<GraphNode> graphNodes;

    public GraphNodesContainer(ArrayList<GraphNode> graphNodes) {
        this.graphNodes = graphNodes;
    }

    public int getSize() {
        return graphNodes.size();
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

    public int containsNode(int x, int y) {
        //System.out.println("Checking node at: " + x + ";" + y);
        for (int i=0; i<graphNodes.size(); i++) {
            GraphNode gn = graphNodes.get(i);
            if (x==gn.position.getX() && y==gn.position.getY()) {
                //System.out.println("Found node of position " + x + ";" + y + "at index" + i);
                return i;
            }
        }
        return -1;
    }

    public GraphNode getGraphNodeAt (int x, int y) {
        for (GraphNode gn : graphNodes) {
            if (x == gn.position.getX() && y == gn.position.getY()) {
                return gn;
            }
        }
        return null;
    }

    public double getDistanceBetweenNodes(GraphNode n1, GraphNode n2) {
        if (n1!= null && n2 != null) {
            for (GraphNode gn : graphNodes) {
                if (gn.equals(n1)) {
                    for (int i = 0; i < 4; i++) {
                        if (gn.neighbours[i] != null && gn.neighbours[i].equals(n2)) {
                            return gn.distances[i];
                        }
                    }
                } else if (gn.equals(n2)) {
                    for (int i = 0; i < 4; i++) {
                        if (gn.neighbours[i] != null && gn.neighbours[i].equals(n1)) {
                            return gn.distances[i];
                        }
                    }
                }
            }
        }
        return -1;
    }

    public double getDistanceBetweenNodesById ( int id1, int id2) {
        return getDistanceBetweenNodes(graphNodes.get(id1), graphNodes.get(id2));
    }


    public void setDistanceBetweenNodesById(int id1, int id2, double distance) {
        //if (distance>0) {
            GraphNode node1 = graphNodes.get(id1);
            GraphNode node2 = graphNodes.get(id2);
            for (int i = 0; i < 4; i++) {
                GraphNode n = node1.neighbours[i];
                if (n!=null && n.equals(node2)) {
                    node1.distances[i] = distance;
                    System.out.println("Distance was changed");
                }
                GraphNode n2 = node2.neighbours[i];
                if (n2!=null && n2.equals(node1)) {
                    node2.distances[i] = distance;
                    System.out.println("Distance was changed");
                }
            }
       // }
    }

    public void setDistanceBetweenNodes (GraphNode node1, GraphNode node2, double distance) {
        if (node1!= null && node2 != null) {

            for (GraphNode gn : graphNodes) {
                if (gn.equals(node1)) {
                    for (int i = 0; i < 4; i++) {
                        if (gn.neighbours[i]!=null && gn.neighbours[i].equals(node2)) {
                            gn.distances[i] = distance;
                        }
                    }
                } else if (gn.equals(node2)) {
                    for (int i = 0; i < 4; i++) {
                        if (gn.neighbours[i]!=null && gn.neighbours[i].equals(node1)) {
                            gn.distances[i] = distance;
                        }
                    }
                }
            }
        }
    }



}
