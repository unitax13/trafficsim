package stuff.gui;

public class RoadSegment {

    protected Position position;
    public int passengers;
    public boolean passengersCalculatedAlready = false;

    public RoadSegment(int x, int y) {
        this.position = new Position(x,y);
        this.passengers = 0;
    }


}
