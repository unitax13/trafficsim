package stuff.gui.citizenMovement;

import stuff.gui.GraphNode;

public class CitizenMovement {

    public GraphNode startNode;
    public GraphNode endNode;
    public double startTime;
    public double duration;

    public CitizenMovement(GraphNode startNode, GraphNode endNode, double startTime, double duration) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.startTime = startTime;
        this.duration = duration;
    }

    public double getMovementEndTime() {
        return startTime+duration;
    }

}
