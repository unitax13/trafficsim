package stuff.gui;

import java.util.ArrayList;
import java.util.Random;

public class SegmentsContainer {

    Simulation simulation;

    GraphNodesContainer graphNodesContainer;
    ArrayList<UrbanSegment> urbanSegments;
    ArrayList<IndustrySegment> industrySegments;


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

    public ArrayList<UrbanSegment> getUrbanSegmentsNotOutYet() {
        ArrayList<UrbanSegment> list = new ArrayList<>();
        for (UrbanSegment us: urbanSegments) {
            if (us.outAlready==false)
                list.add(us);
        }
        return list;
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
                    simulation.simulationStats.boundSegments++;
                }
            }
        } else {
            simulation.simulationStats.boundSegments = 0;
            for (int i = 0; i < urbanSegments.size() && i < industrySegments.size(); i++ ) {
                Random r = new Random();
                ArrayList<IndustrySegment> ises = getUnboundIndustrySegments();
                int pickedOne = r.nextInt(ises.size());

                UrbanSegment us = urbanSegments.get(i);
                IndustrySegment is = ises.get(pickedOne);

                us.boundIndustrySegment = is;
                is.boundUrbanSegment = us;
                System.out.println("Urban segment " + i +" at [" + us.position.getX() + ";" + us.position.getY() + "] bound to industry at [" + is.position.getX() + ";" + is.position.getY() + "]");
                simulation.simulationStats.boundSegments++;
            }

        }
    }






}
