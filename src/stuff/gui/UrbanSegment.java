package stuff.gui;

import java.util.ArrayList;

public class UrbanSegment extends Segment {

    public IndustrySegment boundIndustrySegment;
    public ArrayList<Position> pathToIndustry;
    public ArrayList<GraphNode> nodeRouteToIndustry;
    public double distanceToIndustry;
    public boolean outAlready = false;

    public UrbanSegment(int x, int y) {
        super(x, y);
    }

    public ArrayList<Object> findPathToCorrespondingSegment(GraphNodesContainer graphNodesContainer, int startId, boolean doUpdateSegment) {
        ArrayList<Integer> startIds = new ArrayList<>();
        if (startId>-1) {
            startIds.add(startId);
        }
        ArrayList<Integer> destinationIds = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();
        ArrayList<ArrayList<Position>> subPathToIndustry = new ArrayList<>();
        ArrayList<ArrayList<GraphNode>> pathList = new ArrayList<>();
        UrbanSegment urbanSegment = this;


        if (urbanSegment.closestRoadNodes != null && urbanSegment.boundIndustrySegment != null) {
            if (startIds.size()==0) {
                try {
                    startIds.add(graphNodesContainer.containsNode(urbanSegment.closestRoadNodes.get(0).getX(), urbanSegment.closestRoadNodes.get(0).getY()));
                    startIds.add(graphNodesContainer.containsNode(urbanSegment.closestRoadNodes.get(1).getX(), urbanSegment.closestRoadNodes.get(1).getY()));
                } catch (IndexOutOfBoundsException e) {
                    if (startIds.size() == 0) {
                        System.out.println("No closest road nodes found at start");
                        return null;
                    }
                }
            }

            try {
                destinationIds.add(graphNodesContainer.containsNode(urbanSegment.boundIndustrySegment.closestRoadNodes.get(0).getX(), urbanSegment.boundIndustrySegment.closestRoadNodes.get(0).getY()));
                destinationIds.add(graphNodesContainer.containsNode(urbanSegment.boundIndustrySegment.closestRoadNodes.get(1).getX(), urbanSegment.boundIndustrySegment.closestRoadNodes.get(1).getY()));
            } catch (IndexOutOfBoundsException e) {
                if (destinationIds.size() == 0) {
                    System.out.println("No closest road nodes found at destination");
                    return null;
                }
            }

            for (int a = 0; a < startIds.size(); a++) {

                ShortestPath path1 = new ShortestPath();
                PathAndDistances[] pathAndDistances1 = path1.dijkstra(graphNodesContainer.get(), startIds.get(a));
                for (int i = 0; i < pathAndDistances1.length; i++) {

                    if ((destinationIds.size() >= 1 && i == destinationIds.get(0)) || (destinationIds.size() >= 2 && i == destinationIds.get(1))) {

                        System.out.printf(i + ". " + pathAndDistances1[i].dist + " "); //whole distance
                        pathAndDistances1[i].dist += urbanSegment.distancesToClosestRoadNodes.get(a);
                        System.out.printf(" (" + pathAndDistances1[i].dist + ") ");
                        pathAndDistances1[i].dist += urbanSegment.boundIndustrySegment.getDistanceToClosestRoadNodesByNodeId(i, graphNodesContainer);

                        distances.add(pathAndDistances1[i].dist);
                        System.out.printf(" (" + pathAndDistances1[i].dist + ") ");

                        if (pathAndDistances1[i].node != null)
                            System.out.printf(pathAndDistances1[i].node.position.toString()); //first/last node position
                        System.out.printf("----->");

                        PathAndDistances pad2 = pathAndDistances1[i].predecessor;

                        ArrayList<GraphNode> path = new ArrayList<>(); //a path to show is created as an arraylist

                        ArrayList<Position> singleSubPathToIndustry = new ArrayList<>();
                        singleSubPathToIndustry.add(urbanSegment.boundIndustrySegment.closestRoadSegment);
                        if (pathAndDistances1[i].node != null)
                            singleSubPathToIndustry.add(pathAndDistances1[i].node.position);

                        path.add(pathAndDistances1[i].node); //the first/last node is added

                        //while the predecessors are not null
                        while (pad2 != null) {
                            if (pad2.node != null) { //if there's a node
                                path.add(pad2.node); // add it to a path to show

                                singleSubPathToIndustry.add(pad2.node.position);

                                System.out.printf(pad2.node.position.toString() + "-->"); //also print that node
                            }
                            pad2 = pad2.predecessor;
                        }


                        System.out.printf(graphNodesContainer.get().get(startIds.get(a)).position.toString() + "\n"); //print the last one
                        path.add(graphNodesContainer.get().get(startIds.get(a))); //finish the node path and add that path to urban segment
                        pathList.add(path);


                        singleSubPathToIndustry.add(graphNodesContainer.get().get(startIds.get(a)).position);
                        singleSubPathToIndustry.add(urbanSegment.closestRoadSegment);
                        subPathToIndustry.add(singleSubPathToIndustry);


                    }
                }
            }

            double minDistance = distances.get(0);
            int whichOne = 0;
            for (int i = 1; i < distances.size(); i++) {
                if (minDistance > distances.get(i)) {
                    minDistance = distances.get(i);
                    whichOne = i;
                }
            }

            if (doUpdateSegment) {
                urbanSegment.pathToIndustry = subPathToIndustry.get(whichOne);
                urbanSegment.nodeRouteToIndustry = pathList.get(whichOne);
                urbanSegment.distanceToIndustry = minDistance;
            }

            //return pathList.get(whichOne);
            ArrayList<Object> toReturn = new ArrayList<>();
            toReturn.add(pathList.get(whichOne));
            toReturn.add(subPathToIndustry.get(whichOne));
            return toReturn;

        }
        return null;
    }


    public ArrayList<GraphNode> getRouteToIndustryNotReversed() {
        if (nodeRouteToIndustry!= null) {
            ArrayList<GraphNode> nodes = new ArrayList<>(nodeRouteToIndustry.size());
            for (int i = 0; i < nodeRouteToIndustry.size(); i++) {
                nodes.add(nodeRouteToIndustry.get(nodeRouteToIndustry.size() - 1 - i));
            }

            return nodes;
        } else return null;
    }
}
