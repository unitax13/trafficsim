package stuff.gui;

import java.util.ArrayList;

public class ShortestPathingClass {

    public UrbanSegment segment1;
    public IndustrySegment segment2;

    public ArrayList<Position> positionArrayList;

    Simulation simulation;
    GraphNodesContainer graphNodes;
    SegmentsContainer segmentsContainer;
    SimulationGrid simulationGrid;

    public ShortestPathingClass(Simulation simulation,SimulationGrid simulationGrid, GraphNodesContainer graphNodes, SegmentsContainer segmentsContainer) {
        positionArrayList = new ArrayList<>();
        this.simulation = simulation;
        this.graphNodes = graphNodes;
        this.segmentsContainer = segmentsContainer;
        this.simulationGrid = simulationGrid;
    }

    public void add (Position position) {
        if (positionArrayList.size()<=1) {
            positionArrayList.add(position);
        } else if (positionArrayList.size()==2) {
            positionArrayList.remove(0);
            positionArrayList.add(position);
        }

        if (positionArrayList.size() == 2) {
            apply();
        }
    }

    public void apply() {
        if (positionArrayList.size()==2) {
            UrbanSegment us = new UrbanSegment(positionArrayList.get(0).getX(), positionArrayList.get(0).getY());
            IndustrySegment is = new IndustrySegment(positionArrayList.get(1).getX(), positionArrayList.get(1).getY());
            us.boundIndustrySegment = is;

            us.calculateClosestRoadSegment(simulation, 10);
            us.findClosestRoadNodes(simulation, graphNodes);
            is.calculateClosestRoadSegment(simulation, 10);
            is.findClosestRoadNodes(simulation, graphNodes);

            us.findPathToCorrespondingSegment(graphNodes);

            simulationGrid.positionPath = us.pathToIndustry;
            simulationGrid.positionPathToDrawIsOn = true;

        }
    }
}
