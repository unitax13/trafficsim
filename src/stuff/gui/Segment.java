package stuff.gui;

import stuff.gui.utils.Position;

import java.util.ArrayList;

public class Segment {

    public Position position;
    public Position closestRoadSegment;

    public boolean closestRoadSegmentIsNode = false;
    public ArrayList<Position> closestRoadNodes;
    public ArrayList<Double> distancesToClosestRoadNodes;


    public Segment(int x, int y) {
        position = new Position(x, y);
    }

    public Position calculateClosestRoadSegment(Simulation simulation, int radius) {

        int X = position.getX();
        int Y = position.getY();

        int presentX;
        int presentY;

        //search in a cross
//        for (int i = 1; i <= radius; i++) {
//           Position pos = searchCrossAtRange(simulation, i);
//           if (pos!=null)
//               return pos;
//        }


        //search in a spiral
        int x = 0, y = 0;
        int dx = 0;
        int dy = -1;

        int t = radius * 2 + 1;
        int maxI = t * t;

        int lastCrossCheckI = 1;
        int deltaToTheNextSquareCorner = 8;
        int spiralCircle = 1;

        //search in a cross at range 1
        Position pos = searchCrossAtRange(simulation, 1);
        if (pos!=null)
            return pos;

        for (int i = 0; i < maxI; i++) {

            //search in a cross first
            if (i==(lastCrossCheckI + deltaToTheNextSquareCorner)) {
                //System.out.println("i = " + i + "; searching in a cross at range " + (spiralCircle+1));
                pos = searchCrossAtRange(simulation, spiralCircle+1);
                if (pos!=null) {
                    closestRoadSegment = pos;
                    return closestRoadSegment;
                }
                else {
                    lastCrossCheckI = i;
                    deltaToTheNextSquareCorner += 8;
                    spiralCircle++;
                }
            }

            //search in a spiral
            if ((-X / 2 <= x) && (x <= X / 2) && (-Y / 2 <= y) && (y <= Y / 2)) {
                presentX = (X - x);
                presentY = (Y - y);
                if (simulation.get(presentX,presentY) == Simulation.FieldType.FIELD_ROAD1) {
                    //System.out.println("Found road segment at [" + presentX + "," + presentY + "].");
                    closestRoadSegment = new Position(presentX, presentY);
                    return closestRoadSegment;
                }
                //System.out.println("Step " + i + ". Checking cell [" + presentX + ","+ presentY + "].");

            }

            if ((x == y) || ((x < 0) && (x == -y)) || ((x > 0) && (x == 1 - y))) {
                t = dx;
                dx = -dy;
                dy = t;
            }
            x += dx;
            y += dy;
        }

        return null;
    }

    private Position searchCrossAtRange(Simulation simulation,int i) {


        int X = position.getX();
        int Y = position.getY();



        //LEFT
        int presentX = X - i;
        int presentY = Y;
        if (simulation.get(presentX,presentY) == Simulation.FieldType.FIELD_ROAD1) {
            //System.out.println("Found road segment at [" + presentX + "," + presentY + "].");
            closestRoadSegment = new Position(presentX, presentY);
            return closestRoadSegment;
        }
        presentX = X;
        presentY = Y + i;
        //UP
        if (simulation.get(presentX,presentY) == Simulation.FieldType.FIELD_ROAD1) {
            //System.out.println("Found road segment at [" + presentX + "," + presentY + "].");
            closestRoadSegment = new Position(presentX, presentY);
            return closestRoadSegment;
        }
        //RIGHT
        presentX = X + i;
        presentY = Y;
        if (simulation.get(presentX,presentY) == Simulation.FieldType.FIELD_ROAD1) {
            //System.out.println("Found road segment at [" + presentX + "," + presentY + "].");
            closestRoadSegment = new Position(presentX, presentY);
            return closestRoadSegment;
        }
        //RIGHT
        presentX = X;
        presentY = Y - i;
        if (simulation.get(presentX,presentY) == Simulation.FieldType.FIELD_ROAD1) {
            //System.out.println("Found road segment at [" + presentX + "," + presentY + "].");
            closestRoadSegment = new Position(presentX, presentY);
            return closestRoadSegment;
        }

        return null;
    }

    public void findClosestRoadNodes(Simulation simulation, GraphNodesContainer graphNodes) {
        closestRoadNodes = new ArrayList<>();
        distancesToClosestRoadNodes = new ArrayList<>();
        if (closestRoadSegment != null) {
            int x = closestRoadSegment.getX();
            int y = closestRoadSegment.getY();


            if (graphNodes.isNode(x, y)) {
                closestRoadSegmentIsNode = true;
                //System.out.println("Road segment at [" + x + "," + y + "] is a node already;");
                closestRoadNodes.add(new Position(x, y));
                distancesToClosestRoadNodes.add(0.0);
                return;
            }


            //CHECK FOR THE SECOND ONE ONLY IF THE ARE ROAD SEGMENTS AROUND THE CLOSEST SEGMENT

            if (simulation.grid[x - 1][y] == Simulation.FieldType.FIELD_ROAD1 && simulation.grid[x + 1][y] == Simulation.FieldType.FIELD_ROAD1) {

                //W PRAWO
                for (int i = x; simulation.grid[i][y] == Simulation.FieldType.FIELD_ROAD1; i++) {
                    if (graphNodes.isNode(i, y)) {
                        closestRoadNodes.add(new Position(i, y));
                        distancesToClosestRoadNodes.add((double) Math.abs(i - x));
                        break;
                    }
                }
                //W LEWO
                for (int i = x; simulation.grid[i][y] == Simulation.FieldType.FIELD_ROAD1; i--) {
                    if (graphNodes.isNode(i, y)) {
                        closestRoadNodes.add(new Position(i, y));
                        distancesToClosestRoadNodes.add((double) Math.abs(i - x));
                        break;
                    }
                }

            }
            if (simulation.grid[x][y - 1] == Simulation.FieldType.FIELD_ROAD1 && simulation.grid[x][y + 1] == Simulation.FieldType.FIELD_ROAD1) {

                // W GÓRĘ
                for (int i = y; simulation.grid[x][i] == Simulation.FieldType.FIELD_ROAD1; i++) {
                    if (graphNodes.isNode(x, i)) {
                        closestRoadNodes.add(new Position(x, i));
                        distancesToClosestRoadNodes.add((double) Math.abs(i - y));
                        break;
                    }
                }
                //W DÓł
                for (int i = y; simulation.grid[x][i] == Simulation.FieldType.FIELD_ROAD1; i--) {
                    if (graphNodes.isNode(x, i)) {
                        closestRoadNodes.add(new Position(x, i));
                        distancesToClosestRoadNodes.add((double) Math.abs(i - y));
                        break;
                    }
                }

            }
        } else System.out.println("Closest road segment is null");

    }

    public double getDistanceToClosestRoadNodesByNodeId(int id, GraphNodesContainer graphNodes) {

        for (int i=0; i<closestRoadNodes.size(); i++) {
            if (id == graphNodes.containsNode(closestRoadNodes.get(i).getX(),closestRoadNodes.get(i).getY())) {
                return distancesToClosestRoadNodes.get(i);
            }
        }
        System.out.println("Did not find any ClosestRoadNodesByNodeId nodes matching id " + id);
        return -1;
    }

    public void printSegmentStats() {
        System.out.printf("Segment at [" + position.getX() + "," + position.getY() + "]. ");
        if (closestRoadSegment != null) {
            System.out.printf("Closest road segment is at " + closestRoadSegment.getX() + "," + closestRoadSegment.getY() + "] ");

            if (closestRoadSegmentIsNode) {
                System.out.printf("and IS a node. \n");
            } else
                System.out.println("and IS NOT a node. \n");

        } else {
            System.out.printf("No closest road segment\n");
        }

        int n = closestRoadNodes.size();
        if (n>0) {
            System.out.println("Closest road nodes (" + n + "): ");
        }
        for (int i = 0; i < n; i++) {
            System.out.printf("[" + closestRoadNodes.get(i).getX() + "," + closestRoadNodes.get(i).getY() + "] and " + distancesToClosestRoadNodes.get(i) + " from closest road segment\n");

        }

    }




}
