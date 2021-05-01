package stuff.gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {


    private static final int minWidth = 100;
    private static final int heightForButtons = 200;
    private int width, height;
    private int fieldWidth, fieldHeight;
    private static final int maxHeight = 960;
    private static final int maxWidth = 1600;
    private static final int preferredFieldSize = 100;
    public double gridOpacity = 100;

    public static boolean roadsIsOn = true;
    public static boolean urbanIsOn = true;
    public static boolean industryIsOn = true;
    public static boolean nodeNumbersAreOn = true;

    public int forNodeId = 0;
    public int toNodeId = 1;
    public boolean pathIsDrawn = false;

    public int distanceBetweenId1;
    public int distanceBetweenId2;


    private ArrayList<ArrayList<GraphNode>> nodePaths;

    PathAndDistances[] pathAndDistances;
    ArrayList<GraphNode> paths;
    double distance;




    private Point2D previousMousePos = new Point2D(-1,-1);
    private Point2D previousField = new Point2D(0,0);
    boolean isDragging = false;
    boolean escWasPressed = false;

    Simulation simulation;
    SimulationGrid simulationGrid;
    public GraphicsContext gc;
    ArrayList<GraphNode> graphNodes;

    @FXML
    private CheckBox roadsViewButton;
    @FXML
    private CheckBox urbanViewButton;
    @FXML
    private CheckBox industryViewButton;
    @FXML
    private CheckBox nodeNumbersCheckBox;
    @FXML
    private Slider gridOpacitySlider;
    @FXML
    private Button generateGraphButton;
    @FXML
    private Button dijkstraButton;
    @FXML
    private Button showPathToIdButton;
    @FXML
    private TextField forNodeIdTextField;
    @FXML
    private TextField toNodeIdTextField;
    @FXML
    private TextField distanceBetweenIdField1;
    @FXML
    private TextField distanceBetweenIdField2;
    @FXML
    private TextField distanceBetweenDistanceField;
    @FXML
    private Button distanceBetweenSetButton;

    public TitledPane mainTitledPane;
    public Canvas mainCanvas;

    public Canvas getMainCanvas() {
        return mainCanvas;
    }

    private Point2D currentMousePos = new Point2D(-1,-1);
    public Point2D getCurrentMousePos() {
        return currentMousePos;
    }

    public void configUpdated () {
        if (roadsViewButton.isSelected())
            roadsIsOn = true;
        else
            roadsIsOn = false;
        if (urbanViewButton.isSelected())
            urbanIsOn = true;
        else
            urbanIsOn = false;
        if (industryViewButton.isSelected())
            industryIsOn = true;
        else
            industryIsOn = false;
        if (nodeNumbersCheckBox.isSelected())
            nodeNumbersAreOn = true;
        else
            nodeNumbersAreOn = false;

        if (forNodeIdTextField.getText()!=null) {
            try {
                forNodeId = Integer.parseInt(forNodeIdTextField.getText());
            } catch (Exception e) {
                System.out.println("Invalid number entered!");
            }
        }

        if (toNodeIdTextField.getText()!=null) {
            try {
                System.out.println("Getting" + toNodeIdTextField.getText());
                toNodeId = Integer.parseInt(toNodeIdTextField.getText());
                System.out.println("Entered "+ toNodeId);
            } catch (Exception e) {
                System.out.println("Invalid number entered!");
            }
        }




        System.out.println("Config was updated");
        redraw();
    }

    public void gridOpacitySliderUpdated() {
        gridOpacity = gridOpacitySlider.getValue();
    }

    public void generateGraphButtonPressed() {
        System.out.println("Printing graph");
        graphNodes = simulationGrid.generateGraph();
        for(GraphNode node: graphNodes ) {
            System.out.println(node.position);
            System.out.println(Arrays.toString(node.distances));
        }
    }







    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(getClass() + "was called");
        gc = mainCanvas.getGraphicsContext2D();
        simulation = new Simulation();

        nodeNumbersCheckBox.setSelected(true);
        initCanvas();
        initCallbacks();
        calculateSize(simulation);
        simulationGrid = new SimulationGrid(this,simulation, fieldWidth,fieldHeight);
        gridOpacitySliderUpdated();
        redraw();
        SimulationApplication.mainWindow = this;
    }

    public void redraw() {
        simulationGrid.draw(gc);
    }

    private void initCanvas() {

        mainCanvas.setOnMouseMoved(mouseEvent -> {
            currentMousePos = new Point2D(mouseEvent.getX(), mouseEvent.getY());
            redraw();
        });
        

        gc.setFill(Color.LIGHTCYAN);
        gc.fillRect(50, 100,200,300);
    }

    public void initCallbacks() {



//        mainCanvas.setOnMouseClicked( e -> {
//            System.out.println("Mouse clicked");
//            redraw();
//        });

        mainCanvas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                System.out.println("Left mouse was pressed");
                escWasPressed = false;
                Point2D coords = simulationGrid.getFieldWithMouseOn();
                simulation.grid[(int) coords.getX()][(int) coords.getY()] = Simulation.FieldType.FIELD_ROAD1;
            } else if (e.getButton() == MouseButton.SECONDARY) {
                System.out.println("Right mouse was pressed");
                escWasPressed = false;
                Point2D coords = simulationGrid.getFieldWithMouseOn();
                simulation.grid[(int) coords.getX()][(int) coords.getY()] = Simulation.FieldType.FIELD_EMPTY;
            }

        });

        mainCanvas.setOnMouseDragged(e -> {
            currentMousePos = new Point2D(e.getX(), e.getY());
           // System.out.println("Is dragging");
            if (isDragging == false) {
                isDragging = true;
                previousMousePos = getCurrentMousePos();

                previousField = simulationGrid.getFieldWithMouseOn();

            }
            //System.out.println("Prevoius field: " + previousField);
            //System.out.println("Previous mouse pos: " + previousMousePos);
            redraw();
        });

        mainCanvas.setOnMouseReleased( e -> {
            currentMousePos = new Point2D(e.getX(), e.getY());
            if (e.getButton() == MouseButton.PRIMARY) {
                if (isDragging && !escWasPressed) {
                    isDragging = false;
                    Point2D newField = simulationGrid.getFieldWithMouseOn();
                    simulationGrid.drawPerpendicularLineBetween(previousField, newField, Simulation.FieldType.FIELD_ROAD1);
                }
            } else if (e.getButton() == MouseButton.SECONDARY) {
                if (isDragging && !escWasPressed) {
                    isDragging = false;
                    Point2D newField = simulationGrid.getFieldWithMouseOn();
                    simulationGrid.drawPerpendicularLineBetween(previousField, newField, Simulation.FieldType.FIELD_EMPTY);
                }
            }
            isDragging = false;
            redraw();
        });
    }

    public void onMouseClicked() {
        //System.out.println("onMouseClicked");
    }

    public void onMouseDragged() {
        //System.out.println("onMouseDragged");
    }

    public void onMouseEntered() {
       // System.out.println("onMouseEntered");
    }
    public void onMouseExited() {
        //System.out.println("onMouseExited");
    }
    public void onMouseMoved() {
        //redraw();
        //System.out.println("onMouseMoved");
    }
    public void onMousePressed() {
        //System.out.println("onMousePressed");
       // redraw();
        //System.out.println(simulationGrid.getFieldWithMouseOn());
    }
    public void onMouseReleased() {
        //System.out.println("onMouseReleased");
    }

    private void calculateSize(Simulation sim) {
        /*
        int tmpHeight = sim.height * preferredFieldSize;
        if (tmpHeight > (int) mainCanvas.getHeight()) {
            height = (int) mainCanvas.getHeight();
            fieldHeight = (int)( mainCanvas.getHeight() / sim.height);
        } else {
            height = tmpHeight;
            fieldHeight = preferredFieldSize;
        }

        int tmpWidth = sim.width * fieldHeight;
        if (tmpWidth < minWidth) {
            width = minWidth;
            fieldWidth = (minWidth) / sim.width;
        } else if (tmpWidth > (int) mainCanvas.getWidth()) {
            width = (int) mainCanvas.getWidth();
            fieldWidth = (int)(mainCanvas.getWidth() / sim.width);
        } else {
            width = tmpWidth;
            fieldWidth = fieldHeight;
        }
        */
        //ASSUMING THE SIMULATION IS SQUARE
        if ( mainCanvas.getWidth() > mainCanvas.getHeight()) {
            fieldWidth = (int) (mainCanvas.getWidth()/sim.width);
            fieldHeight = fieldWidth;
        } else {
            fieldWidth = (int) (mainCanvas.getHeight()/sim.width);
            fieldHeight = fieldWidth;
        }


    }

    public void dijkstraButtonPressed() {
        configUpdated();
        int start = forNodeId;

        nodePaths = new ArrayList<>();


        ShortestPath t= new ShortestPath();
        pathAndDistances = t.dijkstra(graphNodes,start);
        System.out.println("Distances");
        for (int i=0; i<pathAndDistances.length; i++) {
            System.out.printf(i+ "." + pathAndDistances[i].dist);
            System.out.printf("[");
            if (pathAndDistances[i].node != null)
                System.out.printf(pathAndDistances[i].node.position.toString());
            System.out.printf("----->");

            PathAndDistances pad2 = pathAndDistances[i].predecessor;

            ArrayList<GraphNode> path = new ArrayList<>();
            path.add(pathAndDistances[i].node);


            while (pad2 !=null) {
                if (pad2.node != null) {
                    path.add(pad2.node);
                    System.out.printf(pad2.node.position.toString() + "-->");
                }
                pad2 = pad2.predecessor;
            }
            System.out.printf(graphNodes.get(start).position.toString());
            path.add(graphNodes.get(start));
            nodePaths.add(path);

            System.out.printf("\n");
        }

    }

    public void showPathToIdButtonPressed() {
        if (nodePaths!=null) {

            configUpdated();
            pathIsDrawn = false;
            redraw();

            System.out.println("Showing path from " + forNodeId + " to " + toNodeId);
            paths = nodePaths.get(toNodeId);
            for (int i=paths.size()-1; i>=0; i--) {
                if (paths.get(i)!=null)
                    System.out.println(paths.get(i).position);
            }
            distance = pathAndDistances[toNodeId].dist;
            System.out.println(distance);

            pathIsDrawn = true;
            simulationGrid.drawPath();
            redraw();

        }

    }

    public void distancePanelUpdated() {
        if (distanceBetweenIdField1.getText()!=null) {
            try {
                distanceBetweenId1 = Integer.parseInt(distanceBetweenIdField1.getText());
            } catch (Exception e) {
                System.out.println("Invalid number entered!");
                distanceBetweenId1 = -1;
            }
        }

        if (distanceBetweenIdField2.getText()!=null) {
            try {
                distanceBetweenId2 = Integer.parseInt(distanceBetweenIdField2.getText());
            } catch (Exception e) {
                System.out.println("Invalid number entered!");
                distanceBetweenId2 = -1;
            }
        }

        if (distanceBetweenId1 != -1 && distanceBetweenId2 != -1) {
            if (graphNodes != null) {
                GraphNode node1 = graphNodes.get(distanceBetweenId1);
                GraphNode node2 = graphNodes.get(distanceBetweenId2);
                if (node1!= null && node2 != null) {
                    for (int i = 0; i < 4; i++) {
                        GraphNode n = node1.neighbours[i];
                        if (n!=null && n.equals(node2)) {
                            double distance = n.distances[i];
                            if (distance > 0)
                                distanceBetweenDistanceField.setText(String.valueOf((int) distance));
                        }
                    }
                }

            }
        }
    }

    public void distanceBetweenSetButtonPressed() {
        //distancePanelUpdated();
        if (distanceBetweenId1>0 && distanceBetweenId2 >0 && distanceBetweenId1 != distanceBetweenId2) {
            double distance = -1;
            try {
                distance= Integer.parseInt(distanceBetweenDistanceField.getText());
            } catch (Exception e) {
                System.out.println("Invalid number entered!");
            }
            if (distance>0) {
                GraphNode node1 = graphNodes.get(distanceBetweenId1);
                GraphNode node2 = graphNodes.get(distanceBetweenId2);
                for (int i = 0; i < 4; i++) {
                    GraphNode n = node1.neighbours[i];
                    if (n!=null && n.equals(node2)) {
                        node1.distances[i] = distance;
                        System.out.println("Distance was changed");
                    }
                    GraphNode n2 = node2.neighbours[i];
                    if (n2!=null && n2.equals(node1)) {
                        node2.distances[i] = distance;
                        System.out.println("Distance was changed");
                    }
                }
            }
        }
    }
    
    
}
