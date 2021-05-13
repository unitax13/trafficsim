package stuff.gui;

import java.util.ArrayList;

public class UrbanSegment extends Segment {

    public IndustrySegment boundIndustrySegment;
    public ArrayList<Position> pathToIndustry;
    public ArrayList<GraphNode> nodeRouteToIndustry;
    public double distanceToIndustry;

    public UrbanSegment(int x, int y) {
        super(x, y);
    }
}
