package stuff.gui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;


public class SimulationGrid {
    private Simulation simulation;
    private MainWindow mainWindow;
    private int fieldWidth, fieldHeight;
    private GraphicsContext gc;
    private Canvas mainCanvas;

    public SimulationGrid(MainWindow mainWindow, Simulation simulation, int fieldWidth, int fieldHeight) {
        this.mainWindow = mainWindow;
        this.simulation = simulation;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.mainCanvas = mainWindow.getMainCanvas();
        this.gc = mainCanvas.getGraphicsContext2D();
    }

    public void draw(GraphicsContext gc) {
        drawGrid(gc);
        drawSelection(gc);
        drawCoords();
        drawGridOverlay();
    }
    
    public Point2D getFieldWithMouseOn() {
        Point2D p = mainWindow.getCurrentMousePos();
        if (p!= null) {
            int chosenFieldX = (int)(p.getX() / fieldWidth);
            int chosenFieldY = (int)(p.getY() / fieldHeight);
            if (chosenFieldX >= 0 && chosenFieldX < simulation.width && chosenFieldY >= 0 && chosenFieldY < simulation.height)
                return new Point2D(chosenFieldX, chosenFieldY);
            else
                return new Point2D(-1, -1);
        }
        return new Point2D(-1, -1);
        }

    private void drawSelection(GraphicsContext gc) {
        Point2D selectedField = getFieldWithMouseOn();
        if (selectedField.getX() != -1 && selectedField.getY() != -1) {
            Color inverse = Color.LIGHTGRAY;

            gc.setGlobalBlendMode(BlendMode.DARKEN);
            gc.setFill(inverse);
            gc.fillRect((int) (fieldWidth * selectedField.getX()), (int) (fieldHeight * selectedField.getY()), fieldWidth, fieldHeight);

            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
        }
    }

    public void drawPerpendicularLineBetween(Point2D a, Point2D b) {
        System.out.println("Drawing between " + a + " and "+ b);

        if ( ( Math.abs(b.getX() - a.getX()) > Math.abs(b.getY() - a.getY()) ) && (b.getX() - a.getX() ) >= 0 ) { //heading east
            for (int x = (int) a.getX(); x <= b.getX(); x++) {
                simulation.grid[x][(int) a.getY()] = Simulation.FieldType.FIELD_ROAD1;
            }
        }else if( (Math.abs(b.getX() - a.getX()) > Math.abs(b.getY() - a.getY()) ) && (b.getX() - a.getX() ) < 0 )  {//heading west
            for (int x = (int) a.getX(); x >= b.getX(); x--) {
                simulation.grid[x][(int) a.getY()] = Simulation.FieldType.FIELD_ROAD1;
            }
        } else if ( (Math.abs(b.getX() - a.getX()) < Math.abs(b.getY() - a.getY()) ) && (b.getY() - a.getY() ) < 0 ) {//heading south
            for (int y = (int) a.getY(); y >= b.getY(); y--) {
                simulation.grid[(int)a.getX()][y] = Simulation.FieldType.FIELD_ROAD1;
            }
        } else if ( (Math.abs(b.getX() - a.getX()) < Math.abs(b.getY() - a.getY()) ) && (b.getY() - a.getY() ) >= 0 ) {//heading north
            for (int y = (int) a.getY(); y <= b.getY(); y++) {
                simulation.grid[(int) a.getX()][y] = Simulation.FieldType.FIELD_ROAD1;
            }
        }
    }



    private void drawGrid (GraphicsContext gc) {
        for (int x = 0; x<simulation.width; x++) {
            for (int y=0; y<simulation.height; y++) {
                Color color;
                Simulation.FieldType type = simulation.getCellFromGrid(simulation.grid, x,y);
                 if ( type == Simulation.FieldType.FIELD_URBAN1) {
                    color = Color.CHARTREUSE;
                } else if ( type == Simulation.FieldType.FIELD_INDUSTRY1) {
                    color = Color.DARKKHAKI;
                } else if ( type == Simulation.FieldType.FIELD_ROAD1) {
                    color = Color.DARKSLATEGRAY;
                } else { // EMPTY
                    color = Color.CORNSILK;
                }
                gc.setFill(color);
                gc.fillRect(fieldWidth*x, fieldHeight*y,fieldWidth, fieldHeight);

            }
        }
    }

    private void drawCoords() {
        String text = getFieldWithMouseOn().toString().substring(8) + "(" + mainWindow.getCurrentMousePos().toString().substring(8) + ")";
        gc.setFill(Color.BLACK);
        gc.fillText(text,10,20,200);
    }

    private void drawGridOverlay() {
        for (int x = 0; x<=simulation.width; x++) {
            if (x%10 == 0)
                gc.setLineWidth(1*mainWindow.gridOpacity/100);
            else
                gc.setLineWidth(0.5*mainWindow.gridOpacity/100);
            gc.strokeLine(x*fieldWidth,-1,x*fieldWidth,SimulationApplication.SCREEN_HEIGHT);
            gc.strokeLine(-1, x*fieldWidth, SimulationApplication.SCREEN_WIDTH,x*fieldWidth);
        }
    }

    public ArrayList<GraphNode> generateGraph() {
        int width = simulation.width;
        int height = simulation.height;

        ArrayList<GraphNode> graphNodes = new ArrayList<>();

        Simulation.FieldType[] data = new Simulation.FieldType[width*height];

        for (int i=0; i< simulation.height; i++)
            for (int j=0; j<simulation.width; j++)
                data[i*width+j] = simulation.grid[i][j];


        GraphNode start = null;
        GraphNode end = null;

        GraphNode topNodes[] = new GraphNode[width];
        int count = 0;

        int rowOffset;
        int rowAboveOffset;
        int rowBelowOffset;

        GraphNode t;

        for (int y=0; y<height; y++) {

            rowOffset = y*width;
            rowAboveOffset = rowOffset - width;
            rowBelowOffset = rowOffset + width;

            Simulation.FieldType prv = Simulation.FieldType.FIELD_EMPTY;
            Simulation.FieldType cur = Simulation.FieldType.FIELD_EMPTY;
            Simulation.FieldType nxt = data[rowOffset + 1];

            GraphNode leftNode = null;

            for (int x = 0; x<width-1; x++) {

                prv = cur;
                cur = nxt;
                nxt = data[rowOffset+x+1];

                GraphNode n = null;

                if (cur != Simulation.FieldType.FIELD_ROAD1){
                    // ON WALL - No action
                    continue;
                }

                if (prv == Simulation.FieldType.FIELD_ROAD1) {
                    if (nxt == Simulation.FieldType.FIELD_ROAD1) {
                        //PATH PATH PATH
                        //Create node only if paths above or below
                        if (data[rowAboveOffset + x] == Simulation.FieldType.FIELD_ROAD1 || data[rowBelowOffset + x] == Simulation.FieldType.FIELD_ROAD1) {
                            n = new GraphNode(new Point2D(y, x));
                            leftNode.neighbours[1] = n;
                            n.neighbours[3] = leftNode;
                            leftNode = n;
                        }
                    } else {
                        // PATH PATH WALL
                        //Create path at end of corridor
                        n = new GraphNode(new Point2D(y, x));
                        leftNode.neighbours[1] = n;
                        n.neighbours[3] = leftNode;
                        leftNode = null;
                    }
                } else {
                    if (nxt == Simulation.FieldType.FIELD_ROAD1) {
                        //WALL PATH PATH
                        //Create path at start of corridor
                        n = new GraphNode(new Point2D(y,x));
                        leftNode = n;
                    } else {
                        //WALL PATH WALL
                        //Create node only if in dead end
                        if ((data[rowAboveOffset+x] != Simulation.FieldType.FIELD_ROAD1) || (data[rowBelowOffset + x] != Simulation.FieldType.FIELD_ROAD1)) {
                            //System.out.println("Create node in dead end");
                            n = new GraphNode(new Point2D(y,x));
                        }
                    }
                }

                if (n!=null) {

                    if(data[rowAboveOffset+x] == Simulation.FieldType.FIELD_ROAD1) {
                        t = topNodes[x];
                        t.neighbours[2] = n;
                        n.neighbours[0] = t;
                    }

                    if (data[rowBelowOffset + x] == Simulation.FieldType.FIELD_ROAD1) {
                        topNodes[x] = n;
                    } else {
                        topNodes[x] = null;
                    }

                    graphNodes.add(n);
                    count++;
                }
            }
        }
        rowOffset = (height-1)*width;
        for (int x=0; x<width; x++) {
            if (data[rowOffset + x] == Simulation.FieldType.FIELD_ROAD1) {
                end = new GraphNode(new Point2D(height-1, x));
                t = topNodes[x];
                t.neighbours[2] = end;
                end.neighbours[0] = t;

                graphNodes.add(end);
                count ++;
                break;
            }
        }

        for (GraphNode gn: graphNodes) {
            for (int i=0; i<gn.neighbours.length; i++) {
                GraphNode neighbour = gn.neighbours[i];
                if (neighbour!=null) {
                    double dist = Math.sqrt(
                            (gn.position.getX() - neighbour.position.getX()) * (gn.position.getX() - neighbour.position.getX()) +
                                    (gn.position.getY() - neighbour.position.getY()) * (gn.position.getY() - neighbour.position.getY()));
                    gn.distances[i] = dist;

                    for (int j = 0; j < neighbour.neighbours.length; j++) {
                        GraphNode n = neighbour.neighbours[j];
                        if (n!= null && n.equals(gn)) {
                            neighbour.distances[j] = dist;
                        }
                    }
                }
            }
        }

        return graphNodes;



    }



}
