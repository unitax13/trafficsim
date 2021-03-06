package stuff.gui;

import stuff.gui.utils.Position;

import java.util.ArrayList;

public class RoadSegmentsContainer {
    
    int width, height;
    
    public ArrayList<RoadSegment> roadSegments;
    private Simulation simulation;
    
    public RoadSegmentsContainer(int width, int height) {
        this.width = width;
        this.height = height;
        roadSegments = new ArrayList<>();
    }

    public RoadSegmentsContainer(Simulation simulation) {
        this.simulation = simulation;
        this.width = simulation.width;
        this.height = simulation.height;

        roadSegments = new ArrayList<>();
        for (int x=0; x<simulation.width; x++){
            for (int y=0; y<simulation.height; y++) {
                if (simulation.get(x,y) == Simulation.FieldType.FIELD_ROAD1) {
                    roadSegments.add(new RoadSegment(x,y));
                }
            }
        }

    }

    public void resetCalculatedAlready () {
        for (RoadSegment rs: roadSegments) {
            rs.passengersCalculatedAlready = false;
        }
    }

    public int checkCalculatedAlready() {
        int calculatedAlreadies = 0;
        for (RoadSegment rs: roadSegments) {
            if (rs.passengersCalculatedAlready)
                calculatedAlreadies++;
        }
        return calculatedAlreadies;
    }

    public RoadSegment getRoadSegmentAt(int x, int y) {
        for (RoadSegment roadSegment : roadSegments) {
            if (x == roadSegment.position.getX() && y == roadSegment.position.getY())
                return roadSegment;
        }
        return null;
    }

    public ArrayList<RoadSegment> getUncalculatedRoadSegments() {
        ArrayList<RoadSegment> uncalculatedRoadSegments = new ArrayList<>();
        for (RoadSegment rs: roadSegments) {
            if (!rs.passengersCalculatedAlready) {
                uncalculatedRoadSegments.add(rs);
            }
        }
        return uncalculatedRoadSegments;
    }

    public void generatePassengersMap(Simulation simulation, GraphNodesContainer graphNodesContainer, SegmentsContainer segmentsContainer) {
        while (checkCalculatedAlready()<roadSegments.size()) {
            for (RoadSegment rs: roadSegments) {
                if (!rs.passengersCalculatedAlready) {
                    getAndMakePassengersAtSegment(rs.position.getX(),rs.position.getY(),simulation, graphNodesContainer, segmentsContainer);
                }
            }
        }
    }

    public int getAndMakePassengersAtSegment(int x, int y, Simulation simulation, GraphNodesContainer graphNodes, SegmentsContainer segmentsContainer) {

        if (getRoadSegmentAt(x, y).passengersCalculatedAlready == false) {

            ArrayList<Position> segmentsToUpdate = new ArrayList<>();

            int passengers = 0;
            int capacityForWholeEdge = 20;

            //System.out.println("Checking segment " + x + " ; " + y);

            if (graphNodes.isNode(x, y)) {
                passengers = graphNodes.getGraphNodeAt(x, y).getAllPassengers();
                getRoadSegmentAt(x, y).passengers = passengers;
                getRoadSegmentAt(x,y).passengersCalculatedAlready = true;
                getRoadSegmentAt(x,y).capacity = 20;
                return passengers;
            }

            ArrayList<GraphNode> closestRoadNodes = new ArrayList<>();
            if (simulation.grid[x - 1][y] == Simulation.FieldType.FIELD_ROAD1 && simulation.grid[x + 1][y] == Simulation.FieldType.FIELD_ROAD1) {

                //W PRAWO
                for (int i = x; simulation.grid[i][y] == Simulation.FieldType.FIELD_ROAD1; i++) {
                    segmentsToUpdate.add(new Position(i, y));
                    if (graphNodes.getGraphNodeAt(i, y) != null) {
                        closestRoadNodes.add(graphNodes.getGraphNodeAt(i, y));
                        break;
                    }
                }
                //W LEWO
                for (int i = x; simulation.grid[i][y] == Simulation.FieldType.FIELD_ROAD1; i--) {
                    segmentsToUpdate.add(new Position(i, y));
                    if (graphNodes.getGraphNodeAt(i, y) != null) {
                        closestRoadNodes.add(graphNodes.getGraphNodeAt(i, y));
                        break;
                    }
                }

            }
            if (simulation.grid[x][y - 1] == Simulation.FieldType.FIELD_ROAD1 && simulation.grid[x][y + 1] == Simulation.FieldType.FIELD_ROAD1) {

                // W G??R??
                for (int i = y; simulation.grid[x][i] == Simulation.FieldType.FIELD_ROAD1; i++) {
                    segmentsToUpdate.add(new Position(x, i));
                    if (graphNodes.getGraphNodeAt(x, i) != null) {
                        closestRoadNodes.add(graphNodes.getGraphNodeAt(x, i));
                        break;
                    }
                }
                //W D????
                for (int i = y; simulation.grid[x][i] == Simulation.FieldType.FIELD_ROAD1; i--) {
                    segmentsToUpdate.add(new Position(x, i));
                    if (graphNodes.getGraphNodeAt(x, i) != null) {
                        closestRoadNodes.add(graphNodes.getGraphNodeAt(x, i));
                        break;
                    }
                }
            }

            if (closestRoadNodes.size() == 2) {
                //System.out.println("Road between " + closestRoadNodes.get(0).position + " and " + closestRoadNodes.get(1).position + " of current distance "
                 //       + graphNodes.getDistanceBetweenNodes(closestRoadNodes.get(0), closestRoadNodes.get(1)));

                passengers = graphNodes.getPassengersBetweenNodes(closestRoadNodes.get(0),closestRoadNodes.get(1));
                capacityForWholeEdge = graphNodes.calculateCapacityBetweenNodes(closestRoadNodes.get(0),closestRoadNodes.get(1)); // capacity
                graphNodes.setCapacityBetweenNodes(closestRoadNodes.get(0),closestRoadNodes.get(1),capacityForWholeEdge);


            }

            for (Position position : segmentsToUpdate) {
                getRoadSegmentAt(position.getX(), position.getY()).passengers = passengers;
                getRoadSegmentAt(position.getX(), position.getY()).passengersCalculatedAlready = true;
                getRoadSegmentAt(position.getX(), position.getY()).capacity = capacityForWholeEdge;
            }

            return passengers;
        } else return getRoadSegmentAt(x,y).passengers;
    }

    public RoadOverlay getRoadOverlay() {
        RoadOverlay roadOverlay = new RoadOverlay(this, simulation);
        return roadOverlay;
    }

}
