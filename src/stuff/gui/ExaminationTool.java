package stuff.gui;

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
        Object segment = segmentsContainer.getSegmentAt(x,y);
        if (segment!=null) {
            if (segment.getClass() == UrbanSegment.class) {
                UrbanSegment urbanSegment = (UrbanSegment) segment;

                simulationGrid.positionPath = urbanSegment.pathToIndustry;
                simulationGrid.positionPathToDrawIsOn = true;
            } else if (segment.getClass() == IndustrySegment.class) {
                IndustrySegment industrySegment = (IndustrySegment) segment;
                if (industrySegment.boundUrbanSegment!=null) {

                    simulationGrid.positionPath = industrySegment.boundUrbanSegment.pathToIndustry;
                    simulationGrid.positionPathToDrawIsOn = true;
                }
            }
        }

    }

    public void printInfoAboutSegment(int x, int y) {
        System.out.println(getInfoAboutSegment(x,y));
    }

    public String getInfoAboutSegment(int x, int y) {
        String info = "x = " + x + "\t y = " + y + "\n";
        Simulation.FieldType type = simulation.get(x,y);

        Object segment = segmentsContainer.getSegmentAt(x,y);
        if (segment!=null) {
            if (segment.getClass() == UrbanSegment.class) {

            } else if (segment.getClass() == IndustrySegment.class) {

            }
        }

        if (type == Simulation.FieldType.FIELD_ROAD1) {
            info += "Road segment\n";
        } else if (type == Simulation.FieldType.FIELD_URBAN1) {
            info += "Urban segment\n";
            if (segment != null) {
                UrbanSegment urbanSegment = (UrbanSegment) segment;
                IndustrySegment is = urbanSegment.boundIndustrySegment;
                if (is!=null) {
                    info += "Bound to industry at" + is.position.toString();
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
                    info += "Bound to urban at" + us.position.toString();
                } else {
                    info += "Unbound segment.";
                }
            }
        }else if  (type == Simulation.FieldType.FIELD_EMPTY) {
            info += "Empty segment\n";
        } else info += "Unknown segment\n";



        return info;
    }

}
