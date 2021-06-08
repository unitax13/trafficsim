package stuff.gui;

import java.util.ArrayList;
import java.util.Collections;

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
        System.out.println(" ");
        if (movingCitizens.size() > 0) {
            MainWindow.noMovingCitizens = false;
            ArrayList<MovingCitizen> list = getFinishedMovementsAt(time);

            System.out.println("Gotten finished movements at " + time + " of size " + list.size());


            for (MovingCitizen mv : list) {

                GraphNode currentNode = mv.currentMovement.endNode;
                System.out.println("\nCitizen from " + mv.originSegment.position + " has stopped at " + currentNode.position);
                System.out.printf("Previous node route: ");
                for (GraphNode node: mv.originSegment.nodeRouteToIndustry)
                    System.out.printf(node.position + "");
                System.out.printf("\nPrevious position path: ");
                for (Position p : mv.originSegment.pathToIndustry)
                    System.out.printf(p + "");
                System.out.printf("\n");

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

                    ArrayList<Object> graphNodePathAndPositionPath = mv.originSegment.findPathToCorrespondingSegment(graphNodes, nodeId, false);

                    ArrayList<GraphNode> updatedRemainingPath = (ArrayList<GraphNode>) graphNodePathAndPositionPath.get(0);
                    Collections.reverse(updatedRemainingPath);



                    ArrayList<Position> updatedRemainingPositionPath = (ArrayList<Position>) graphNodePathAndPositionPath.get(1);
                    Collections.reverse(updatedRemainingPositionPath);
                    updatedRemainingPositionPath.remove(0);

                    System.out.printf("New node route:");
                    for (GraphNode gn: updatedRemainingPath)
                        if (gn!=null)
                            System.out.printf(gn.position + " ");
                        else
                            System.out.printf("NULL NODE!!!");
                    System.out.printf("\n");
                    System.out.printf("New pos path:");
                    for (Position p: updatedRemainingPositionPath)
                        System.out.printf(p + " " );
                    System.out.printf("\n");



                    ArrayList<GraphNode> newPath = new ArrayList<>();
                    ArrayList<Position> newPositionPath = new ArrayList<>();

                    newPositionPath.add(mv.originSegment.pathToIndustry.get(mv.originSegment.pathToIndustry.size()-1));


                    if (updatedRemainingPath != null) {
                        for (int h = 0; h < mv.originSegment.getRouteToIndustryNotReversed().size() - 1; h++) {
                            GraphNode gnOriginal = mv.originSegment.getRouteToIndustryNotReversed().get(h);
                            newPath.add(gnOriginal);
                            newPositionPath.add(gnOriginal.position);

                            if (gnOriginal.equals(updatedRemainingPath.get(0))) {
                                System.out.println("Found the same node in node route at #" + h);
                                boolean pathsAreNotEqual = false;

                                System.out.print("Current pos path: ");
                                for (Position p : newPositionPath)
                                    System.out.print(p + " ");
                                System.out.print("\n");

                                for (int j = 1; j < mv.originSegment.getRouteToIndustryNotReversed().size() - h; j++) {
                                    if (!mv.originSegment.getRouteToIndustryNotReversed().get(h + j).equals(updatedRemainingPath.get(j))) {
                                        pathsAreNotEqual = true;
                                        System.out.println("Old node route differs from the new path");
                                        break;
                                    }
                                }



                                if (pathsAreNotEqual) {
                                    System.out.println("Paths were not equal, combining them");

                                    //newPositionPath.add(updatedRemainingPositionPath.get(0));
                                    for (int g = 1; g < updatedRemainingPath.size(); g++) {
                                        newPath.add(updatedRemainingPath.get(g));
                                    }
                                    for (int g = 1; g< updatedRemainingPositionPath.size(); g++) {
                                        newPositionPath.add(updatedRemainingPositionPath.get(g));
                                    }

                                    //reversing again and replacing back
                                    System.out.printf("New node path: ");
                                    for (GraphNode gn: newPath)
                                        System.out.printf(gn.position + " ");
                                    System.out.printf("\n");

                                    Collections.reverse(newPath);

                                    mv.originSegment.nodeRouteToIndustry = newPath;

                                    System.out.printf("New position path: ");
                                    for (Position p : newPositionPath)
                                        System.out.printf(p + " ");
                                    System.out.printf("\n");

                                    Collections.reverse(newPositionPath);
                                    mv.originSegment.pathToIndustry = newPositionPath;

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
