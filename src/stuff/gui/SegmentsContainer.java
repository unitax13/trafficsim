package stuff.gui;

import java.util.ArrayList;
import java.util.Random;

public class SegmentsContainer {

    Simulation simulation;

    GraphNodesContainer graphNodesContainer;
    ArrayList<UrbanSegment> urbanSegments;
    ArrayList<IndustrySegment> industrySegments;
    int boundSegments = 0;

    public SegmentsContainer() {
        urbanSegments = new ArrayList<>();
        industrySegments = new ArrayList<>();
        
    }
    
    public SegmentsContainer(Simulation simulation) {
        this.simulation = simulation;
        urbanSegments = new ArrayList<>();
        industrySegments = new ArrayList<>();

        for (int i=0; i<simulation.width; i++) {
            for (int j = 0; j < simulation.height; j++) {
                if (simulation.get(i, j) == Simulation.FieldType.FIELD_URBAN1) {
                    urbanSegments.add(new UrbanSegment(i, j));
                } else if (simulation.get(i, j) == Simulation.FieldType.FIELD_INDUSTRY1) {
                    industrySegments.add(new IndustrySegment(i, j));
                } else if (simulation.get(i, j) == Simulation.FieldType.FIELD_ROAD1) {

                }
            }
        }
    }

    public void setGraphNodesContainer(GraphNodesContainer graphNodesContainer) {
        this.graphNodesContainer = graphNodesContainer;
    }
    
    public Object getSegmentAt(int x, int y) {
        for (int i=0; i<urbanSegments.size(); i++) {
            if (x==urbanSegments.get(i).position.getX() && y==urbanSegments.get(i).position.getY())
                return urbanSegments.get(i);
        }
        for (int i=0; i<industrySegments.size(); i++) {
            if (x==industrySegments.get(i).position.getX() && y==industrySegments.get(i).position.getY())
                return industrySegments.get(i);
        }
        
        return null;
    }

    public ArrayList<UrbanSegment> getUnboundUrbanSegments() {
        ArrayList<UrbanSegment> list = new ArrayList<>();
        for (UrbanSegment us: urbanSegments) {
            if (us.boundIndustrySegment==null)
                list.add(us);
        }
        return list;
    }

    public ArrayList<IndustrySegment> getUnboundIndustrySegments() {
        ArrayList<IndustrySegment> list = new ArrayList<>();
        for (IndustrySegment is: industrySegments) {
            if (is.boundUrbanSegment==null)
                list.add(is);
        }
        return list;
    }
    public void bindRandomly() {
        bindRandomly(false);
    }

    public void bindRandomly(boolean onlyUnbound) {
        if (onlyUnbound) {
            for (int i = 0; i < urbanSegments.size() && i < industrySegments.size(); i++ ) {
                if (urbanSegments.get(i).boundIndustrySegment==null) {
                    Random r = new Random();
                    ArrayList<IndustrySegment> ises = getUnboundIndustrySegments();
                    int pickedOne = r.nextInt(ises.size());

                    UrbanSegment us = urbanSegments.get(i);
                    IndustrySegment is = ises.get(pickedOne);

                    us.boundIndustrySegment = is;
                    is.boundUrbanSegment = us;
                    System.out.println("Urban segment " + i +" at [" + us.position.getX() + ";" + us.position.getY() + "] bound to industry at [" + is.position.getX() + ";" + is.position.getY() + "]");
                    boundSegments++;
                }
            }
        } else {
            boundSegments = 0;
            for (int i = 0; i < urbanSegments.size() && i < industrySegments.size(); i++ ) {
                Random r = new Random();
                ArrayList<IndustrySegment> ises = getUnboundIndustrySegments();
                int pickedOne = r.nextInt(ises.size());

                UrbanSegment us = urbanSegments.get(i);
                IndustrySegment is = ises.get(pickedOne);

                us.boundIndustrySegment = is;
                is.boundUrbanSegment = us;
                System.out.println("Urban segment " + i +" at [" + us.position.getX() + ";" + us.position.getY() + "] bound to industry at [" + is.position.getX() + ";" + is.position.getY() + "]");
                boundSegments++;
            }

        }
    }

    public void findPathToCorrespondingSegment(UrbanSegment urbanSegment) {
        ArrayList<Integer> startIds = new ArrayList<>();
        ArrayList<Integer> destinationIds = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();
        ArrayList<ArrayList<Position>> subPathToIndustry = new ArrayList<>();


        if (urbanSegment.closestRoadNodes!=null && urbanSegment.boundIndustrySegment != null) {

            System.out.println("urbanSegment.closestRoadNodes!=null");
            //System.out.println(urbanSegment.closestRoadNodes.get(0));
            //System.out.println(urbanSegment.closestRoadNodes.get(1));
            try {
                startIds.add(graphNodesContainer.containsNode(urbanSegment.closestRoadNodes.get(0).getX(), urbanSegment.closestRoadNodes.get(0).getY()));
                startIds.add(graphNodesContainer.containsNode(urbanSegment.closestRoadNodes.get(1).getX(), urbanSegment.closestRoadNodes.get(1).getY()));
            } catch (IndexOutOfBoundsException e) {
                if (startIds.size()==0) {
                    System.out.println("No closest road nodes found at start");
                    return;
                }
            }

            try {
                destinationIds.add(graphNodesContainer.containsNode(urbanSegment.boundIndustrySegment.closestRoadNodes.get(0).getX(), urbanSegment.boundIndustrySegment.closestRoadNodes.get(0).getY()));
                destinationIds.add(graphNodesContainer.containsNode(urbanSegment.boundIndustrySegment.closestRoadNodes.get(1).getX(), urbanSegment.boundIndustrySegment.closestRoadNodes.get(1).getY()));
            } catch (IndexOutOfBoundsException e) {
                if (destinationIds.size()==0) {
                    System.out.println("No closest road nodes found at destination");
                    return;
                }
            }

            for (int a=0; a<startIds.size(); a++) {

                ShortestPath path1 = new ShortestPath();
                PathAndDistances pathAndDistances1[] = path1.dijkstra(graphNodesContainer.get(), startIds.get(a));
                int destinationNodeI = -1;
                for (int i = 0; i < pathAndDistances1.length; i++) {

                    if ( ( destinationIds.size()>=1 && i == destinationIds.get(0) ) || ( destinationIds.size()>=2 && i == destinationIds.get(1) ) ) {

                        System.out.printf(i+ ". " + pathAndDistances1[i].dist +" "); //whole distance
                        pathAndDistances1[i].dist += urbanSegment.distancesToClosestRoadNodes.get(a);
                        System.out.printf(" (" + pathAndDistances1[i].dist +") ");
                        pathAndDistances1[i].dist += urbanSegment.boundIndustrySegment.getDistanceToClosestRoadNodesByNodeId(i, graphNodesContainer);

                        distances.add(pathAndDistances1[i].dist);
                        System.out.printf(" (" + pathAndDistances1[i].dist +") ");

                        if (pathAndDistances1[i].node != null)
                            System.out.printf(pathAndDistances1[i].node.position.toString()); //first/last node position
                        System.out.printf("----->");

                        PathAndDistances pad2 = pathAndDistances1[i].predecessor;

                        ArrayList<GraphNode> path = new ArrayList<>(); //a path to show is created as an arraylist

                        ArrayList<Position> singleSubPathToIndustry = new ArrayList<>();
                        singleSubPathToIndustry.add(urbanSegment.boundIndustrySegment.closestRoadSegment);
                        if (pathAndDistances1[i].node != null )
                            singleSubPathToIndustry.add(pathAndDistances1[i].node.position);

                        path.add(pathAndDistances1[i].node); //the first/last node is added

                        //while the predecessors are not null
                        while (pad2 !=null) {
                            if (pad2.node != null) { //if there's a node
                                path.add(pad2.node); // add it to a path to show

                                singleSubPathToIndustry.add(pad2.node.position);

                                System.out.printf(pad2.node.position.toString() + "-->"); //also print that node
                            }
                            pad2 = pad2.predecessor;
                        }


                        System.out.printf(graphNodesContainer.get().get(startIds.get(a)).position.toString() + "\n"); //print the last one
                        path.add(graphNodesContainer.get().get(startIds.get(a)));

                        singleSubPathToIndustry.add(graphNodesContainer.get().get(startIds.get(a)).position);
                        singleSubPathToIndustry.add(urbanSegment.closestRoadSegment);
                        subPathToIndustry.add(singleSubPathToIndustry);

                        //nodePaths.add(path);
                    }
                }
            }

            double minDistance = distances.get(0);
            int whichOne =0;
            for (int i=1; i< distances.size(); i++) {
                if (minDistance>distances.get(i)) {
                    minDistance = distances.get(i);
                    whichOne = i;
                }
            }

            urbanSegment.pathToIndustry = subPathToIndustry.get(whichOne);
            urbanSegment.distanceToIndustry = minDistance;












        }

    }




}
