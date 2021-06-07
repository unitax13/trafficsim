package stuff.gui;

import java.util.ArrayList;

public class CitizenMovementsContainer {

    ArrayList<MovingCitizen> movingCitizens;
    GraphNodesContainer graphNodes;

    public CitizenMovementsContainer(GraphNodesContainer graphNodes) {
        movingCitizens = new ArrayList<>();
        this.graphNodes = graphNodes;
        MainWindow.noMovingCitizens = false;
    }


    public void addMovingCitizen(MovingCitizen movingCitizen) {
        movingCitizens.add(movingCitizen);
        System.out.println("Citizen moving from " + movingCitizen.originSegment.position + " added to cmc");
    }

    public ArrayList<MovingCitizen> getFinishedMovementsAt(double time) {
        ArrayList<MovingCitizen> sublist = new ArrayList<>();

        int currentlyMoving = movingCitizens.size();
        System.out.println("Currently moving: " + currentlyMoving);

        for (MovingCitizen mv: movingCitizens) {
            if (mv.currentMovement!= null && mv.currentMovement.getMovementEndTime()<=time) {
                sublist.add(mv);
            }
        }
        for (MovingCitizen mv:sublist) {
            if (movingCitizens.contains(mv)) {
                movingCitizens.remove(mv);
            }
        }

        return sublist;

    }

    public void initMovementForNotMoving(double time) {
        System.out.println("Initting movement for not moving (size: " + movingCitizens.size() + ")");
        for (MovingCitizen mv:movingCitizens) {
            if (mv.currentMovement == null && mv.originSegment.nodeRouteToIndustry!=null) {
                if (mv.originSegment.getRouteToIndustryNotReversed().size() > 1) {
                    mv.setMovement(mv.originSegment.getRouteToIndustryNotReversed().get(0), mv.originSegment.getRouteToIndustryNotReversed().get(1), time, graphNodes.getTimeBetweenNodes(mv.originSegment.getRouteToIndustryNotReversed().get(0), mv.originSegment.getRouteToIndustryNotReversed().get(1)), graphNodes);
                }
            }
        }
    }

    public void getFinishedMovementsAndAddNextOnes(double time) {
        if (movingCitizens.size() > 0) {
            MainWindow.noMovingCitizens = false;
            ArrayList<MovingCitizen> list = getFinishedMovementsAt(time);

            System.out.println("Gotten finished movements at " + time + " of size " + list.size());


            for (MovingCitizen mv : list) {

                GraphNode currentNode = mv.currentMovement.endNode;
                System.out.println("Citizen from " + mv.originSegment.position + " has stopped at " + currentNode.position);

                graphNodes.addPassengersBetweenNodes(mv.currentMovement.startNode, currentNode, -1);

//            if (mv.currentPreviousNodeNumber>=mv.originSegment.getRouteToIndustryNotReversed().size()-1) {
//                System.out.println("This seems to be the end for citizen from " + mv.originSegment.position);
//                break;
//            }
                if (!MainWindow.citizensAreSmart) {
                    boolean movedAway = false;
                    for (int i = 0; i < mv.originSegment.getRouteToIndustryNotReversed().size() - 1; i++) {
                        if (currentNode.equals(mv.originSegment.getRouteToIndustryNotReversed().get(i))) {


                            GraphNode nextNode = mv.originSegment.getRouteToIndustryNotReversed().get(i + 1);
                            System.out.printf("Citizen from " + mv.originSegment.position + " finished movement at node " + currentNode.position + "\t");
                            mv.setMovement(currentNode, nextNode, time, graphNodes.getTimeBetweenNodes(currentNode, nextNode), graphNodes);
                            movingCitizens.add(mv);
                            movedAway = true;
                            break;

                        }
                    }
                    if (!movedAway) {
                        System.out.println("Citizen seems to have reached the destination or evaporated...");
                    }
                } else { //WHEN CITIZENS ARE SMART
                    boolean movedAway = false;

                    int nodeId = graphNodes.getNodeId(currentNode);
                    ArrayList<GraphNode> updatedRemainingPathReversed = mv.originSegment.findPathToCorrespondingSegment(graphNodes, nodeId, false);
                    ArrayList<GraphNode> updatedRemainingPath = new ArrayList<>(updatedRemainingPathReversed.size());
                    for (int k = updatedRemainingPathReversed.size() - 1; k >= 0; k--) {
                        updatedRemainingPath.add(updatedRemainingPathReversed.get(k));
                    }

                    ArrayList<GraphNode> newPath = new ArrayList<>();

                    if (updatedRemainingPath != null) {
                        for (int h = 0; h < mv.originSegment.getRouteToIndustryNotReversed().size() - 1; h++) {
                            GraphNode gnOriginal = mv.originSegment.getRouteToIndustryNotReversed().get(h);
                            newPath.add(gnOriginal);

                            if (gnOriginal.equals(updatedRemainingPath.get(0))) {
                                System.out.println("Found the same node in node route at #" + h);
                                boolean pathsAreNotEqual = false;
                                for (int j = 1; j < mv.originSegment.getRouteToIndustryNotReversed().size() - h; j++) {
                                    if (mv.originSegment.getRouteToIndustryNotReversed().get(h + j).equals(updatedRemainingPath.get(j))) {

                                    } else {
                                        pathsAreNotEqual = true;
                                        System.out.println("Old node route differs from the new path");
                                        break;
                                    }
                                }
                                if (pathsAreNotEqual) {
                                    System.out.println("Paths were not equal, combining them");
                                    for (int g = 1; g < updatedRemainingPath.size(); g++) {
                                        newPath.add(updatedRemainingPath.get(g));
                                    }
                                    ArrayList<GraphNode> newPathReversedToReplace = new ArrayList<>();
                                    for (int k = newPath.size() - 1; k >= 0; k--) {
                                        newPathReversedToReplace.add(newPath.get(k));
                                    }
                                    mv.originSegment.nodeRouteToIndustry = newPathReversedToReplace;

                                    GraphNode nextNode = mv.originSegment.getRouteToIndustryNotReversed().get(h + 1);
                                    System.out.printf("Citizen from " + mv.originSegment.position + " finished movement at node " + currentNode.position + "\t");
                                    mv.setMovement(gnOriginal, nextNode, time, graphNodes.getTimeBetweenNodes(currentNode, nextNode), graphNodes);
                                    movingCitizens.add(mv);
                                    movedAway = true;

                                } else { //!pathsAreNotEqual
                                    System.out.println("Paths were equal, not doing anything");

                                    for (int i = 0; i < mv.originSegment.getRouteToIndustryNotReversed().size() - 1; i++) {
                                        if (currentNode.equals(mv.originSegment.getRouteToIndustryNotReversed().get(i))) {


                                            GraphNode nextNode = mv.originSegment.getRouteToIndustryNotReversed().get(i + 1);
                                            System.out.printf("Citizen from " + mv.originSegment.position + " finished movement at node " + currentNode.position + "\t");
                                            mv.setMovement(currentNode, nextNode, time, graphNodes.getTimeBetweenNodes(currentNode, nextNode), graphNodes);
                                            movingCitizens.add(mv);
                                            movedAway = true;
                                            break;

                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (!movedAway) {
                        System.out.println("Citizen seems to have reached the destination or evaporated...");
                    }
                }
            }
        }
        else {
            System.out.println("NO MOVING CITIZENS");
            MainWindow.noMovingCitizens = true;
        }
    }

    public void printCurrentlyMoving() {
        System.out.println("");
    }


}
