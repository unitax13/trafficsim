package stuff.gui;

public class MovingCitizen {

    UrbanSegment originSegment;

    CitizenMovement currentMovement;

    int currentPreviousNodeNumber = 0;



    public MovingCitizen(UrbanSegment originSegment) {

        this.originSegment = originSegment;
    }

    public void setMovement(GraphNode startNode, GraphNode endNode, double time, double duration, GraphNodesContainer graphNodes) {
        if (startNode!=null && endNode!=null)
        currentMovement = new CitizenMovement(startNode, endNode, time, duration);
        System.out.println("Citizen from " + originSegment.position + " started going from " + currentMovement.startNode.position + " to " + currentMovement.endNode.position + "; reaches next node at " + currentMovement.getMovementEndTime());

        currentPreviousNodeNumber++;
        graphNodes.addPassengersBetweenNodes(startNode,endNode,1);
    }

    public void initCitizen(GraphNodesContainer graphNodes, double time) {
        if (originSegment.getRouteToIndustryNotReversed().size()>1) {
            currentMovement = new CitizenMovement(originSegment.getRouteToIndustryNotReversed().get(0), originSegment.getRouteToIndustryNotReversed().get(1), time, graphNodes.getTimeBetweenNodes(originSegment.getRouteToIndustryNotReversed().get(0),originSegment.getRouteToIndustryNotReversed().get(1)));
        }

    }
}
