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

    public RoadOverlay roadOverlay;
    public ArrayList<Position> positionPath;
    public boolean positionPathToDrawIsOn = false;


    public int prevCameraX = 0;
    public int prevCameraY = 0;
    public int cameraX = 0;
    public int cameraY = 0;
    public float cameraScale = 1;

    public SimulationGrid(MainWindow mainWindow, Simulation simulation, int fieldWidth, int fieldHeight) {
        this.mainWindow = mainWindow;
        this.simulation = simulation;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
        this.mainCanvas = mainWindow.getMainCanvas();
        this.gc = mainCanvas.getGraphicsContext2D();
    }

    public void draw(GraphicsContext gc) {
        //gc.clearRect(0,0, fieldWidth*cameraScale*simulation.width, fieldHeight*cameraScale*simulation.height);
        gc.setFill(Color.CORNSILK);
        gc.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        drawGrid(gc);

        drawSelection(gc);
        if (mainWindow.isDragging)
            drawPerpendicularSelection(mainWindow.previousField, mainWindow.simulationGrid.getFieldWithMouseOn());

        drawCoords();
        drawGridOverlay();

        if (mainWindow.nodeNumbersAreOn)
            drawNodeNumbers();
        if (mainWindow.pathIsDrawn)
            drawPath();
        if (mainWindow.viewMode == MainWindow.viewMode.HEATMAP && roadOverlay != null)
            drawRoadHeatOverlay();
        if (positionPathToDrawIsOn)
            drawPositionPath();


    }

    public Position getFieldWithMouseOn() {
        Point2D p = mainWindow.getCurrentMousePos();
        if (p != null) {
            int chosenFieldX = (int) ((p.getX() + cameraX) / (fieldWidth * cameraScale));
            int chosenFieldY = (int) ((p.getY() + cameraY) / (fieldHeight * cameraScale));
            if (chosenFieldX >= 0 && chosenFieldX < simulation.width && chosenFieldY >= 0 && chosenFieldY < simulation.height)
                return new Position(chosenFieldX, chosenFieldY);
            else {
                //throw new ArrayIndexOutOfBoundsException();
                System.out.println("Unreachable coords");
                return mainWindow.currentCoords;
                //return new Position(-1, -1);
            }

        }
        return new Position(-1, -1);
    }

    private void drawSelection(GraphicsContext gc) {
        if (!mainWindow.isDragging) {
            Position selectedField = getFieldWithMouseOn();
            if (selectedField.getX() != -1 && selectedField.getY() != -1) {
                Color inverse = Color.LIGHTGRAY;

                gc.setGlobalBlendMode(BlendMode.MULTIPLY);
                gc.setFill(inverse);
                gc.fillRect((int) (fieldWidth * cameraScale * selectedField.getX() - cameraX), (int) (fieldHeight * cameraScale * selectedField.getY() - cameraY), fieldWidth * cameraScale, fieldHeight * cameraScale);

                gc.setGlobalBlendMode(BlendMode.SRC_OVER);
            }
        }
    }

    public void drawPerpendicularSelection(Position a, Position b) {
        double deltaX = b.getX() - a.getX();
        double deltaY = b.getY() - a.getY();
        int signumDeltaX = (int) Math.signum(deltaX);
        int signumDeltaY = (int) Math.signum(deltaY);

        Color inverse = Color.LIGHTGRAY;
        gc.setGlobalBlendMode(BlendMode.MULTIPLY);
        gc.setFill(inverse);
        if (!mainWindow.middleIsDown) {
            if (deltaX == 0 || deltaY == 0 || (mainWindow.chosenFieldType == Simulation.FieldType.FIELD_ROAD1 && !mainWindow.secondaryIsDown)) {
                if (Math.abs(deltaX) >= Math.abs(deltaY)) {
                    if (deltaX >= 0) {
                        gc.fillRect((int) (fieldWidth * cameraScale * a.getX() - cameraX), (int) (fieldHeight * cameraScale * a.getY() - cameraY), fieldWidth * cameraScale * deltaX, fieldHeight * cameraScale);
                    } else {
                        gc.fillRect((int) (fieldWidth * cameraScale * b.getX() - cameraX), (int) (fieldHeight * cameraScale * a.getY() - cameraY), -fieldWidth * cameraScale * deltaX, fieldHeight * cameraScale);
                    }
                } else {
                    if (deltaY >= 0) {
                        gc.fillRect((int) (fieldWidth * cameraScale * a.getX() - cameraX), (int) (fieldHeight * cameraScale * a.getY() - cameraY), fieldWidth * cameraScale, fieldHeight * cameraScale * deltaY);
                    } else {
                        gc.fillRect((int) (fieldWidth * cameraScale * a.getX() - cameraX), (int) (fieldHeight * cameraScale * b.getY() - cameraY), fieldWidth * cameraScale, -fieldHeight * cameraScale * deltaY);
                    }
                }
            } else {
                if (signumDeltaX >= 0 && signumDeltaY >= 0) { //heading south-east
                    gc.fillRect((int) (fieldWidth * cameraScale * a.getX() - cameraX), (int) (fieldHeight * cameraScale * a.getY() - cameraY), fieldWidth * cameraScale * (deltaX + 1), fieldHeight * cameraScale * (deltaY + 1));
                } else if (signumDeltaX >= 0 && signumDeltaY < 0) { //heading north-east
                    gc.fillRect((int) (fieldWidth * cameraScale * a.getX() - cameraX), (int) (fieldHeight * cameraScale * b.getY() - cameraY), fieldWidth * cameraScale * (deltaX + 1), -fieldHeight * cameraScale * (deltaY - 1));
                } else if (signumDeltaX < 0 && signumDeltaY < 0) { //heading north-west
                    gc.fillRect((int) (fieldWidth * cameraScale * b.getX() - cameraX), (int) (fieldHeight * cameraScale * b.getY() - cameraY), -fieldWidth * cameraScale * (deltaX - 1), -fieldHeight * cameraScale * (deltaY - 1));
                } else if (signumDeltaX < 0 && signumDeltaY >= 0) {//heading south-west
                    gc.fillRect((int) (fieldWidth * cameraScale * b.getX() - cameraX), (int) (fieldHeight * cameraScale * a.getY() - cameraY), -fieldWidth * cameraScale * (deltaX - 1), fieldHeight * cameraScale * (deltaY + 1));
                }

            }
        }


        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
    }

    public void drawPerpendicularLineBetween(Position a, Position b, Simulation.FieldType fieldType) {
        System.out.println("Drawing between " + a + " and " + b);

        if ((Math.abs(b.getX() - a.getX()) > Math.abs(b.getY() - a.getY())) && (b.getX() - a.getX()) >= 0) { //heading east
            for (int x = a.getX(); x <= b.getX(); x++) {
                if (simulation.grid[x][a.getY()] != Simulation.FieldType.FIELD_ROAD1) {
                    simulation.grid[x][a.getY()] = fieldType;
                }
            }
        } else if ((Math.abs(b.getX() - a.getX()) > Math.abs(b.getY() - a.getY())) && (b.getX() - a.getX()) < 0) {//heading west
            for (int x = a.getX(); x >= b.getX(); x--) {
                if (simulation.grid[x][a.getY()] != Simulation.FieldType.FIELD_ROAD1) {
                    simulation.grid[x][a.getY()] = fieldType;
                }
            }
        } else if ((Math.abs(b.getX() - a.getX()) < Math.abs(b.getY() - a.getY())) && (b.getY() - a.getY()) < 0) {//heading south
            for (int y = a.getY(); y >= b.getY(); y--) {
                if (simulation.grid[a.getX()][y] != Simulation.FieldType.FIELD_ROAD1) {
                    simulation.grid[a.getX()][y] = fieldType;
                }
            }
        } else if ((Math.abs(b.getX() - a.getX()) < Math.abs(b.getY() - a.getY())) && (b.getY() - a.getY()) >= 0) {//heading north
            for (int y = a.getY(); y <= b.getY(); y++) {
                if (simulation.grid[a.getX()][y] != Simulation.FieldType.FIELD_ROAD1) {
                    simulation.grid[a.getX()][y] = fieldType;
                }
            }
        }
        simulation.simulationStats.updateSegmentsCount();
    }

    public void drawRectangleBetween(Position a, Position b, Simulation.FieldType fieldType) {
        double deltaX = b.getX() - a.getX();
        double deltaY = b.getY() - a.getY();
        int signumDeltaX = (int) Math.signum(deltaX);
        int signumDeltaY = (int) Math.signum(deltaY);

        if (deltaX != 0 && deltaY != 0) {

            for (int y = (a.getY() - signumDeltaY); y != b.getY() && y >= 0 && y <= simulation.height; y += signumDeltaY) {

                for (int x = (a.getX() - signumDeltaX); x != b.getX() && x >= 0 && x <= simulation.width; x += signumDeltaX) {
                    if ((simulation.grid[x + signumDeltaX][y + signumDeltaY] != Simulation.FieldType.FIELD_ROAD1) || fieldType == Simulation.FieldType.FIELD_EMPTY) {
                        simulation.grid[x + signumDeltaX][y + signumDeltaY] = fieldType;
                    }
                }
            }
        } else if (deltaY == 0) {

            int y = (a.getY() - signumDeltaY);
            for (int x = (a.getX() - signumDeltaX); x != b.getX() && x >= 0 && x <= simulation.width; x += signumDeltaX) {
                if ((simulation.grid[x + signumDeltaX][y + signumDeltaY] != Simulation.FieldType.FIELD_ROAD1) || fieldType == Simulation.FieldType.FIELD_EMPTY) {
                    simulation.grid[x + signumDeltaX][y + signumDeltaY] = fieldType;
                }
            }
        } else if (deltaX == 0) {

            int x = (a.getX() - signumDeltaX);
            for (int y = (a.getY() - signumDeltaY); y != b.getY() && y >= 0 && y <= simulation.height; y += signumDeltaY) {

                if ((simulation.grid[x + signumDeltaX][y + signumDeltaY] != Simulation.FieldType.FIELD_ROAD1) || fieldType == Simulation.FieldType.FIELD_EMPTY) {
                    simulation.grid[x + signumDeltaX][y + signumDeltaY] = fieldType;
                }
            }
        }
        simulation.simulationStats.updateSegmentsCount();

    }


    private void drawGrid(GraphicsContext gc) {
        for (int x = 0; x < simulation.width; x++) {
            for (int y = 0; y < simulation.height; y++) {
                Color color;
                Simulation.FieldType type = simulation.getCellFromGrid(simulation.grid, x, y);
                if (type == Simulation.FieldType.FIELD_URBAN1 && mainWindow.urbanIsOn) {
                    color = Color.CHARTREUSE;
                } else if (type == Simulation.FieldType.FIELD_INDUSTRY1 && mainWindow.industryIsOn) {
                    color = Color.DARKKHAKI;
                } else if (type == Simulation.FieldType.FIELD_ROAD1 && mainWindow.roadsIsOn) {
                    color = Color.DARKSLATEGRAY;
                } else { // EMPTY
                    color = Color.CORNSILK;
                }
                gc.setFill(color);
                gc.fillRect(fieldWidth * cameraScale * x - cameraX, fieldHeight * cameraScale * y - cameraY, fieldWidth * cameraScale, fieldHeight * cameraScale);

            }
        }
    }

    private void drawCoords() {
        String text = getFieldWithMouseOn().toString().substring(8) + "(" + mainWindow.getCurrentMousePos().toString().substring(8) + ")";
        gc.setFill(Color.BLACK);
        gc.fillText(text, 10, 20, 200);
    }

    private void drawGridOverlay() {
        for (int x = 0; x <= simulation.width; x++) {
            if (x % 10 == 0)
                gc.setLineWidth(1 * mainWindow.gridOpacity / 100);
            else
                gc.setLineWidth(0.5 * mainWindow.gridOpacity / 100);
            gc.strokeLine(x * fieldWidth * cameraScale - cameraX, -1 - cameraY, x * fieldWidth * cameraScale - cameraX, SimulationApplication.SCREEN_HEIGHT * cameraScale - cameraY);
            gc.strokeLine(-1 - cameraX, x * fieldWidth * cameraScale - cameraY, SimulationApplication.SCREEN_WIDTH * cameraScale - cameraX, x * fieldWidth * cameraScale - cameraY);
        }
    }

    public ArrayList<GraphNode> generateGraph() {
        int width = simulation.width;
        int height = simulation.height;

        ArrayList<GraphNode> graphNodes = new ArrayList<>();

        Simulation.FieldType[] data = new Simulation.FieldType[width * height];

        for (int i = 0; i < simulation.height; i++)
            if (simulation.width >= 0) System.arraycopy(simulation.grid[i], 0, data, i * width + 0, simulation.width);


        GraphNode start = null;
        GraphNode end = null;

        GraphNode[] topNodes = new GraphNode[width];
        int count = 0;

        int rowOffset;
        int rowAboveOffset;
        int rowBelowOffset;

        GraphNode t;

        for (int y = 0; y < height; y++) {

            rowOffset = y * width;
            rowAboveOffset = rowOffset - width;
            rowBelowOffset = rowOffset + width;

            Simulation.FieldType prv = Simulation.FieldType.FIELD_EMPTY;
            Simulation.FieldType cur = Simulation.FieldType.FIELD_EMPTY;
            Simulation.FieldType nxt = data[rowOffset + 1];

            GraphNode leftNode = null;

            for (int x = 0; x < width - 1; x++) {

                prv = cur;
                cur = nxt;
                nxt = data[rowOffset + x + 1];

                GraphNode n = null;

                if (cur != Simulation.FieldType.FIELD_ROAD1) {
                    // ON WALL - No action
                    continue;
                }

                if (prv == Simulation.FieldType.FIELD_ROAD1) {
                    if (nxt == Simulation.FieldType.FIELD_ROAD1) {
                        //PATH PATH PATH
                        //Create node only if paths above or below
                        if (data[rowAboveOffset + x] == Simulation.FieldType.FIELD_ROAD1 || data[rowBelowOffset + x] == Simulation.FieldType.FIELD_ROAD1) {
                            n = new GraphNode(new Position(y, x));
                            leftNode.neighbours[1] = n;
                            n.neighbours[3] = leftNode;
                            leftNode = n;
                        }
                    } else {
                        // PATH PATH WALL
                        //Create path at end of corridor
                        n = new GraphNode(new Position(y, x));
                        leftNode.neighbours[1] = n;
                        n.neighbours[3] = leftNode;
                        leftNode = null;
                    }
                } else {
                    if (nxt == Simulation.FieldType.FIELD_ROAD1) {
                        //WALL PATH PATH
                        //Create path at start of corridor
                        n = new GraphNode(new Position(y, x));
                        leftNode = n;
                    } else {
                        //WALL PATH WALL
                        //Create node only if in dead end
                        if ((data[rowAboveOffset + x] != Simulation.FieldType.FIELD_ROAD1) || (data[rowBelowOffset + x] != Simulation.FieldType.FIELD_ROAD1)) {
                            //System.out.println("Create node in dead end");
                            n = new GraphNode(new Position(y, x));
                        }
                    }
                }

                if (n != null) {

                    if (data[rowAboveOffset + x] == Simulation.FieldType.FIELD_ROAD1) {
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
        rowOffset = (height - 1) * width;
        for (int x = 0; x < width; x++) {
            if (data[rowOffset + x] == Simulation.FieldType.FIELD_ROAD1) {
                end = new GraphNode(new Position(height - 1, x));
                t = topNodes[x];
                t.neighbours[2] = end;
                end.neighbours[0] = t;

                graphNodes.add(end);
                count++;
                break;
            }
        }

        for (GraphNode gn : graphNodes) {
            for (int i = 0; i < gn.neighbours.length; i++) {
                GraphNode neighbour = gn.neighbours[i];
                if (neighbour != null) {
                    double dist = Math.sqrt(
                            (gn.position.getX() - neighbour.position.getX()) * (gn.position.getX() - neighbour.position.getX()) +
                                    (gn.position.getY() - neighbour.position.getY()) * (gn.position.getY() - neighbour.position.getY()));
                    gn.distances[i] = dist;

                    for (int j = 0; j < neighbour.neighbours.length; j++) {
                        GraphNode n = neighbour.neighbours[j];
                        if (n != null && n.equals(gn)) {
                            neighbour.distances[j] = dist;
                        }
                    }
                }
            }
        }

        return graphNodes;


    }

    public void drawNodeNumbers() {
        if (mainWindow.graphNodes != null) {
            for (int i = 0; i < mainWindow.graphNodes.getSize(); i++) {
                GraphNode node = mainWindow.graphNodes.get().get(i);
                gc.setFill(Color.BLACK);
                gc.fillText(String.valueOf(i), node.position.getX() * fieldWidth * cameraScale + fieldWidth * cameraScale, node.position.getY() * fieldWidth * cameraScale, fieldWidth * cameraScale);

            }
        }
    }

    public void drawPath() {

        GraphNodesContainer path = mainWindow.paths;
        double distance = mainWindow.distance;

        Color startColor = Color.rgb(163, 52, 235);
        Color endColor = Color.rgb(89, 29, 247);
        double scRed = startColor.getRed() * 255;
        double scGreen = startColor.getGreen() * 255;
        double scBlue = startColor.getBlue() * 255;


        double stepRed = (scRed - endColor.getRed() * 255) / distance;
        double stepGreen = (scGreen - endColor.getGreen() * 255) / distance;
        double stepBlue = (scBlue - endColor.getBlue() * 255) / distance;
        GraphNode one;
        GraphNode two;
        for (int i = 0; i < path.getSize() - 1; i++) {
            one = path.get().get(i);
            two = path.get().get(i + 1);
            if (one != null && two != null) {
                int x = one.position.getX();
                int y = one.position.getY();

                if (one.position.getX() == two.position.getX()) {
                    double delta = two.position.getY() - one.position.getY();
                    for (int j = 0; j < Math.abs(delta); j++) {
                        scRed += stepRed;
                        scGreen += stepGreen;
                        scBlue += stepBlue;


                        gc.setFill(Color.rgb((int) scRed, (int) scGreen, (int) scBlue));
                        gc.fillRect(fieldWidth * cameraScale * x - cameraX, fieldHeight * cameraScale * y - cameraY, fieldWidth * cameraScale, fieldHeight * cameraScale);
                        y += Math.signum(delta);
                    }
                } else if (one.position.getY() == two.position.getY()) {
                    double delta = two.position.getX() - one.position.getX();
                    for (int j = 0; j < Math.abs(delta); j++) {

                        scRed += stepRed;
                        scGreen += stepGreen;
                        scBlue += stepBlue;

                        if (scRed < 0)
                            scRed = 0;
                        else if (scRed > 255)
                            scRed = 255;
                        if (scGreen < 0)
                            scGreen = 0;
                        else if (scGreen > 255)
                            scGreen = 255;
                        if (scBlue < 0)
                            scBlue = 0;
                        else if (scBlue > 255)
                            scBlue = 255;


                        gc.setFill(Color.rgb((int) scRed, (int) scGreen, (int) scBlue));
                        gc.fillRect(fieldWidth * cameraScale * x - cameraX, fieldHeight * cameraScale * y - cameraY, fieldWidth * cameraScale, fieldHeight * cameraScale);
                        x += Math.signum(delta);
                    }
                }
            }
        }


    }

    public void drawPositionPath() {

        Color color = Color.rgb(250, 22, 199);

        if (positionPath != null) {
            for (int i = 0; i < positionPath.size() - 1; i++) {
                Position one = positionPath.get(i);
                Position two = positionPath.get(i + 1);
                if (one != null && two != null) {
                    int x = one.getX();
                    int y = one.getY();

                    if (one.getX() == two.getX()) {
                        double delta = two.getY() - one.getY();
                        for (int j = 0; j <= Math.abs(delta); j++) {
                            gc.setFill(color);
                            gc.fillRect(fieldWidth * cameraScale * x - cameraX, fieldHeight * cameraScale * y - cameraY, fieldWidth * cameraScale, fieldHeight * cameraScale);
                            y += Math.signum(delta);
                        }
                    } else if (one.getY() == two.getY()) {
                        double delta = two.getX() - one.getX();
                        for (int j = 0; j <= Math.abs(delta); j++) {

                            gc.setFill(color);
                            gc.fillRect(fieldWidth * cameraScale * x - cameraX, fieldHeight * cameraScale * y - cameraY, fieldWidth * cameraScale, fieldHeight * cameraScale);
                            x += Math.signum(delta);
                        }
                    }
                }
            }
        }
    }

    public void drawRoadHeatOverlay() {
        for (int i = 0; i < simulation.width; i++) {
            for (int j = 0; j < simulation.height; j++) {
                if (roadOverlay.colorMap[i][j].getOpacity() != 0.0) {
                    //System.out.println(i + ";" + j +" opacity is not 0");
                    gc.setFill(roadOverlay.colorMap[i][j]);
                    gc.fillRect(fieldWidth * cameraScale * i - cameraX, fieldHeight * cameraScale * j - cameraY, fieldWidth * cameraScale, fieldHeight * cameraScale);
                }
            }
        }


    }

}
