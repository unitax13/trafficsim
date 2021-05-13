package stuff.gui;

import java.util.ArrayList;

public class SimulationStats {

    public int numberOfUrbanSegments = 0;
    public int numberOfIndustrySegments = 0;
    public int numberOfRoad1Segments = 0;
    public int boundSegments = 0;

    Simulation simulation;
    public SimulationStats(Simulation simulation) {
        this.simulation = simulation;
    }

    public void updateSegmentsCount() {
        numberOfUrbanSegments = 0;
        numberOfIndustrySegments = 0;
        numberOfRoad1Segments = 0;
        for (int i=0; i<simulation.width; i++) {
            for (int j=0; j<simulation.height; j++) {
                if (simulation.get(i,j) == Simulation.FieldType.FIELD_URBAN1) {
                    numberOfUrbanSegments++;
                } else if (simulation.get(i,j) == Simulation.FieldType.FIELD_INDUSTRY1) {
                    numberOfIndustrySegments++;
                } else if (simulation.get(i,j) == Simulation.FieldType.FIELD_ROAD1) {
                    numberOfRoad1Segments++;
                }
            }
        }
    }

    public void printStats() {
        System.out.println("Number of urban segments: " + numberOfUrbanSegments + " (employed: " + boundSegments + ")");
        System.out.println("Number of industry segments: " + numberOfIndustrySegments);

        System.out.println("Number of road1 segments: " + numberOfRoad1Segments);
    }
}
