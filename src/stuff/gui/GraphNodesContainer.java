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

    public int getPassengersBetweenNodes(GraphNode n1, GraphNode n2) {
            if (n1!= null && n2 != null) {
                for (GraphNode gn : graphNodes) {
                    if (gn.equals(n1)) {
                        for (int i = 0; i < 4; i++) {
                            if (gn.neighbours[i] != null && gn.neighbours[i].equals(n2)) {
                                return gn.passengers[i];
                            }
                        }
                    } else if (gn.equals(n2)) {
                        for (int i = 0; i < 4; i++) {
                            if (gn.neighbours[i] != null && gn.neighbours[i].equals(n1)) {
                                return gn.passengers[i];
                            }
                        }
                    }
                }
            }
            return -1;
    }

    public void addPassengersBetweenNodes(GraphNode node1, GraphNode node2, int passengersAdd) {
        if (node1!= null && node2 != null) {

            for (GraphNode gn : graphNodes) {
                if (gn.equals(node1)) {
                    for (int i = 0; i < 4; i++) {
                        if (gn.neighbours[i]!=null && gn.neighbours[i].equals(node2)) {
                            gn.passengers[i] += passengersAdd;
                        }
                    }
                } else if (gn.equals(node2)) {
                    for (int i = 0; i < 4; i++) {
                        if (gn.neighbours[i]!=null && gn.neighbours[i].equals(node1)) {
                            gn.passengers[i] += passengersAdd;
                        }
                    }
                }
            }
        }
    }

    public void setPassengersBetweenNodes(GraphNode node1, GraphNode node2, int passengersAdd) {
        if (node1!= null && node2 != null) {

            for (GraphNode gn : graphNodes) {
                if (gn.equals(node1)) {
                    for (int i = 0; i < 4; i++) {
                        if (gn.neighbours[i]!=null && gn.neighbours[i].equals(node2)) {
                            gn.passengers[i] = passengersAdd;
                        }
                    }
                } else if (gn.equals(node2)) {
                    for (int i = 0; i < 4; i++) {
                        if (gn.neighbours[i]!=null && gn.neighbours[i].equals(node1)) {
                            gn.passengers[i] = passengersAdd;
                        }
                    }
                }
            }
        }
    }

    public int getPassengersAt(int x, int y, SegmentsContainer segmentsContainer, Simulation simulation) {
        int passengers = 0;
        if (isNode(x,y)) {
            GraphNode node = getGraphNodeAt(x,y);
            for (int i: node.passengers) {
                passengers+= node.passengers[i];
            }
            return passengers;
        } else {

            ArrayList<GraphNode> closestRoadNodes = getClosestRoadNodes(x, y, simulation);

            for (UrbanSegment us : segmentsContainer.urbanSegments) {
                for (int i = 0; i < us.nodeRouteToIndustry.size(); i++) {
                    GraphNode routeNode = us.nodeRouteToIndustry.get(i);
                    if (routeNode.equals(closestRoadNodes.get(0))) {
                        if ((i > 0 && us.nodeRouteToIndustry.get(i - 1).equals(closestRoadNodes.get(1))) ||
                                (i + 1 < us.nodeRouteToIndustry.size() && us.nodeRouteToIndustry.get(i + 1).equals(closestRoadNodes.get(1)))) {
                            passengers++;
                            break;
                        }
                    } else if (routeNode.equals(closestRoadNodes.get(1))) {
                        if ((i > 0 && us.nodeRouteToIndustry.get(i - 1).equals(closestRoadNodes.get(0))) ||
                                (i + 1 < us.nodeRouteToIndustry.size() && us.nodeRouteToIndustry.get(i + 1).equals(closestRoadNodes.get(0)))) {
                            passengers++;
                            break;
                        }
                    }
                }
            }
        }


        return passengers;
    }

    public int getPassengersBetweenNodes(GraphNode n1, GraphNode n2, SegmentsContainer segmentsContainer, Simulation simulation) {
        if (n1!=null && n2!=null && segmentsContainer!=null && simulation !=null) {
            int passengers = 0;
            for (UrbanSegment us : segmentsContainer.urbanSegments) {
                for (int i = 0; i < us.nodeRouteToIndustry.size(); i++) {
                    GraphNode routeNode = us.nodeRouteToIndustry.get(i);
                    if (routeNode.equals(n1)) {
                        if ((i > 0 && us.nodeRouteToIndustry.get(i - 1).equals(n2)) ||
                                (i + 1 < us.nodeRouteToIndustry.size() && us.nodeRouteToIndustry.get(i + 1).equals(n2))) {
                            passengers++;
                            break;
                        }
                    } else if (routeNode.equals(n2)) {
                        if ((i > 0 && us.nodeRouteToIndustry.get(i - 1).equals(n1)) ||
                                (i + 1 < us.nodeRouteToIndustry.size() && us.nodeRouteToIndustry.get(i + 1).equals(n1))) {
                            passengers++;
                            break;
                        }
                    }
                }
            }
            return passengers;
        } else return -1;
    }

    public ArrayList<GraphNode> getClosestRoadNodes(int x, int y, Simulation simulation) {
        ArrayList<GraphNode> closestRoadNodes = new ArrayList<>();
        if (simulation.grid[x - 1][y] == Simulation.FieldType.FIELD_ROAD1 && simulation.grid[x + 1][y] == Simulation.FieldType.FIELD_ROAD1) {

            //W PRAWO
            for (int i = x; simulation.grid[i][y] == Simulation.FieldType.FIELD_ROAD1; i++) {
                if (getGraphNodeAt(i, y) != null) {
                    closestRoadNodes.add(getGraphNodeAt(i, y));
                    break;
                }
            }
            //W LEWO
            for (int i = x; simulation.grid[i][y] == Simulation.FieldType.FIELD_ROAD1; i--) {
                if (getGraphNodeAt(i, y) != null) {
                    closestRoadNodes.add(getGraphNodeAt(i, y));
                    break;
                }
            }

        }
        if (simulation.grid[x][y - 1] == Simulation.FieldType.FIELD_ROAD1 && simulation.grid[x][y + 1] == Simulation.FieldType.FIELD_ROAD1) {

            // W GÓRĘ
            for (int i = y; simulation.grid[x][i] == Simulation.FieldType.FIELD_ROAD1; i++) {
                if (getGraphNodeAt(x, i) != null) {
                    closestRoadNodes.add(getGraphNodeAt(x, i));
                    break;
                }
            }
            //W DÓł
            for (int i = y; simulation.grid[x][i] == Simulation.FieldType.FIELD_ROAD1; i--) {
                if (getGraphNodeAt(x, i) != null) {
                    closestRoadNodes.add(getGraphNodeAt(x, i));
                    break;
                }
            }
        }
        return closestRoadNodes;
    }







}
