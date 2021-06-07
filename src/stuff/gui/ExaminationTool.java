package stuff.gui;

import java.util.ArrayList;

public class ExaminationTool {

    Simulation simulation;
    GraphNodesContainer graphNodes;
    SegmentsContainer segmentsContainer;
    SimulationGrid simulationGrid;


    public ExaminationTool(Simulation simulation, SimulationGrid simulationGrid, GraphNodesContainer graphNodes, SegmentsContainer segmentsContainer) {
        this.graphNodes = graphNodes;
        this.simulation = simulation;
        this.segmentsContainer = segmentsContainer;
        this.simulationGrid = simulationGrid;
    }

    public void showPath(int x, int y) {
        if (segmentsContainer != null) {
            Object segment = segmentsContainer.getSegmentAt(x, y);
            if (segment != null) {
                if (segment.getClass() == UrbanSegment.class) {
                    UrbanSegment urbanSegment = (UrbanSegment) segment;

                    simulationGrid.positionPath = urbanSegment.pathToIndustry;
                    simulationGrid.positionPathToDrawIsOn = true;
                } else if (segment.getClass() == IndustrySegment.class) {
                    IndustrySegment industrySegment = (IndustrySegment) segment;
                    if (industrySegment.boundUrbanSegment != null) {

                        simulationGrid.positionPath = industrySegment.boundUrbanSegment.pathToIndustry;
                        simulationGrid.positionPathToDrawIsOn = true;
                    }
                }
            }
        }

    }

    public void printInfoAboutSegment(int x, int y) {
        System.out.println(getInfoAboutSegment(x,y));
    }

    public String getInfoAboutSegment(int x, int y) {
        String info = "x = " + x + "\t y = " + y + "\n";
        Simulation.FieldType type = simulation.get(x, y);

        Object segment;
        if (segmentsContainer != null) {
                segment = segmentsContainer.getSegmentAt(x, y);
            if (segment != null) {
                if (segment.getClass() == UrbanSegment.class) {

                } else if (segment.getClass() == IndustrySegment.class) {

                }
            }
        } else {
            segment = null;
        }

        if (type == Simulation.FieldType.FIELD_ROAD1) {
            info += "Road segment\n";
            info += examineRoadSegment(x,y);



        } else if (type == Simulation.FieldType.FIELD_URBAN1) {
            info += "Urban segment\n";
            if (segment != null) {
                UrbanSegment urbanSegment = (UrbanSegment) segment;
                IndustrySegment is = urbanSegment.boundIndustrySegment;
                if (is!=null) {
                    info += "Bound to industry at" + is.position;
                    info += "\t Distance between: " + urbanSegment.distanceToIndustry;
                } else {
                    info += "Unbound segment.";
                }
            }
        } else if (type == Simulation.FieldType.FIELD_INDUSTRY1) {
            info += "Industry segment\n";
            if (segment != null) {
                IndustrySegment industrySegment = (IndustrySegment) segment;
                UrbanSegment us = industrySegment.boundUrbanSegment;
                if (us!=null) {
                    info += "Bound to urban at" + us.position;
                    info += "\t Distance between: " + us.distanceToIndustry;
                } else {
                    info += "Unbound segment.";
                }
            }
        }else if  (type == Simulation.FieldType.FIELD_EMPTY) {
            info += "Empty segment\n";
        } else info += "Unknown segment\n";



        return info;
    }

    public String examineRoadSegment (int x, int y) {
        String info = "";

        if (graphNodes != null) {


            if (graphNodes.isNode(x, y)) {
                int passengers = graphNodes.getGraphNodeAt(x,y).getAllPassengers();
                return "Is a intersection, passengers: not implemented";
            }

            ArrayList<GraphNode> closestRoadNodes = new ArrayList<>();
            if (simulation.grid[x - 1][y] == Simulation.FieldType.FIELD_ROAD1 && simulation.grid[x + 1][y] == Simulation.FieldType.FIELD_ROAD1) {

                //W PRAWO
                for (int i = x; simulation.grid[i][y] == Simulation.FieldType.FIELD_ROAD1; i++) {
                    if (graphNodes.getGraphNodeAt(i, y) != null) {
                        closestRoadNodes.add(graphNodes.getGraphNodeAt(i, y));
                        break;
                    }
                }
                //W LEWO
                for (int i = x; simulation.grid[i][y] == Simulation.FieldType.FIELD_ROAD1; i--) {
                    if (graphNodes.getGraphNodeAt(i, y) != null) {
                        closestRoadNodes.add(graphNodes.getGraphNodeAt(i, y));
                        break;
                    }
                }

            }
            if (simulation.grid[x][y - 1] == Simulation.FieldType.FIELD_ROAD1 && simulation.grid[x][y + 1] == Simulation.FieldType.FIELD_ROAD1) {

                // W GÓRĘ
                for (int i = y; simulation.grid[x][i] == Simulation.FieldType.FIELD_ROAD1; i++) {
                    if (graphNodes.getGraphNodeAt(x, i) != null) {
                        closestRoadNodes.add(graphNodes.getGraphNodeAt(x, i));
                        break;
                    }
                }
                //W DÓł
                for (int i = y; simulation.grid[x][i] == Simulation.FieldType.FIELD_ROAD1; i--) {
                    if (graphNodes.getGraphNodeAt(x, i) != null) {
                        closestRoadNodes.add(graphNodes.getGraphNodeAt(x, i));
                        break;
                    }
                }
            }

            if (closestRoadNodes.size() == 2) {
                info += "Road between " + closestRoadNodes.get(0).position + " and " + closestRoadNodes.get(1).position + " of current distance "
                        + graphNodes.getDistanceBetweenNodes(closestRoadNodes.get(0), closestRoadNodes.get(1));

                //search in the graphnodes, count that pair containings
                int passengers = 0;
                passengers = graphNodes.getPassengersBetweenNodes(closestRoadNodes.get(0), closestRoadNodes.get(1));
                double capacity = graphNodes.calculateCapacityBetweenNodes(closestRoadNodes.get(0), closestRoadNodes.get(1));
                info += "\nPassengers: " + passengers  + " [capacity: " + capacity + "]";

            }
        }
        return info;
    }



}
