package stuff.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.*;

public class MainWindow implements Initializable {


    public int SEARCH_RADIUS = 6;
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

    enum viewMode {
        NORMAL,
        SECOND,
        HEATMAP
    }
    public static viewMode viewMode;
    //public static int viewMode = 0; //0 - NORMAL, 2 - HEATMAP

    public int forNodeId = 0;
    public int toNodeId = 1;
    public boolean pathIsDrawn = false;


    private ArrayList<ArrayList<GraphNode>> nodePaths;

    PathAndDistances[] pathAndDistances;
    GraphNodesContainer paths;
    double distance;

    enum clickingMode {
        NORMAL,
        EXAMINING,
        SHORTEST_PATHING
    }
    public static clickingMode clickingMode = MainWindow.clickingMode.NORMAL; // 0 = normal, 1 - examining, 2 - shortest pathing


    private Simulation.FieldType chosenFieldType = Simulation.FieldType.FIELD_ROAD1;




    private Point2D previousMousePos = new Point2D(-1,-1);
    private Position previousField = new Position(0,0);
    boolean isDragging = false;
    boolean escWasPressed = false;

    Simulation simulation;
    SimulationGrid simulationGrid;
    public GraphicsContext gc;
    ExaminationTool examinationTool;
    ShortestPathingClass shortestPathingClass;
    CitizenMovementsContainer cmc;
    CitizenTimer citizenTimer;
    Timer time;
    double timeSpeed = 50;
    boolean timerPlaying = false;
    TaskHelper currentTaskHelper;
    int timerFps = 5;
    static boolean citizensAreSmart = false;
    static boolean noMovingCitizens = true;


    GraphNodesContainer graphNodes;

    SegmentsContainer segmentsContainer;
    RoadSegmentsContainer roadSegmentsContainer;




    @FXML
    private ToggleButton rectangleToggleButton;
    @FXML
    private ToggleButton lineToggleButton;
    @FXML
    private ToggleButton roadToggleButton;
    @FXML
    private ToggleButton urbanAreaToggleButton;
    @FXML
    private ToggleButton industryAreaToggleButton;
    @FXML
    private ToggleButton examineButton;
    @FXML
    private ToggleButton shortestPathButton;




    @FXML
    private Button newButton;
    @FXML
    private Button openFileButton;
    @FXML
    private Button saveFileButton;
    @FXML
    private ToggleButton normalViewButton;
    @FXML
    private ToggleButton heatMapViewButton;

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
    private Button statsButton;
    @FXML
    private Button useButton;

    @FXML
    private Slider mainSpeedSlider;
    @FXML
    private Button playingStopButton;
    @FXML
    private Button playingPlayButton;
    @FXML
    private Button playingOneStepForwardButton;
    @FXML
    private Label stepLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Spinner<Integer> fpsSpinner;
    @FXML
    private CheckBox makeCitizensSmartCheckBox;

    public TitledPane mainTitledPane;
    public Canvas mainCanvas;

    public Canvas getMainCanvas() {
        return mainCanvas;
    }

    private Point2D currentMousePos = new Point2D(-1,-1);
    public Point2D getCurrentMousePos() {
        return currentMousePos;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SimulationApplication.mainWindow = this;
        System.out.println(getClass() + "was called");
        gc = mainCanvas.getGraphicsContext2D();
        simulation = new Simulation();

        nodeNumbersCheckBox.setSelected(true);
        initCanvas();
        initCallbacks();
        calculateSize(simulation);
        simulationGrid = new SimulationGrid(this,simulation, fieldWidth,fieldHeight);
        gridOpacitySliderUpdated();

        examinationTool = new ExaminationTool(simulation, simulationGrid,graphNodes,segmentsContainer);
        cmc = new CitizenMovementsContainer(graphNodes);
        // = new CitizenTimer(cmc, graphNodes, segmentsContainer, this);

        initGui();



        redraw();

    }

    public void redraw() {
        simulationGrid.draw(gc);
    }

    private void initGui() {
        //dijkstraButton.setStyle("-fx-border-color: yellow; -fx-text-fill: blue; -fx-border-width: 3px; -fx-font-size: 30px;-fxbackground-color:rgb(255, 99, 71);");
        //dijkstraButton.setStyle("-fx-background-color: rgb(0, 99, 71); -fx-border-width: 3px;");
        ToggleGroup brushToggleGroup = new ToggleGroup();
        roadToggleButton.setToggleGroup(brushToggleGroup);
        urbanAreaToggleButton.setToggleGroup(brushToggleGroup);
        industryAreaToggleButton.setToggleGroup(brushToggleGroup);
        examineButton.setToggleGroup(brushToggleGroup);
        shortestPathButton.setToggleGroup(brushToggleGroup);
        //roadToggleButton.setStyle("-fx-background-color: rgb(47, 79, 79);-fx-text-fill: white; -fx-background-insets: 0,1,2;.focused{-fx-background-color:rgb(87, 99, 99)}");


        ToggleGroup viewModeToggleGroup = new ToggleGroup();
        normalViewButton.setToggleGroup(viewModeToggleGroup);
        heatMapViewButton.setToggleGroup(viewModeToggleGroup);


        roadToggleButton.setSelected(true);

        mainSpeedSlider.setValue(timeSpeed);

        fpsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,15, timerFps));





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




        mainCanvas.setOnMousePressed(e -> {
            Position coords = simulationGrid.getFieldWithMouseOn();
            if (clickingMode== clickingMode.NORMAL) {
                if (e.getButton() == MouseButton.PRIMARY) {
                    System.out.println("Left mouse was pressed");
                    escWasPressed = false;
                    simulation.grid[coords.getX()][coords.getY()] = chosenFieldType;
                } else if (e.getButton() == MouseButton.SECONDARY) {
                    System.out.println("Right mouse was pressed");
                    escWasPressed = false;
                    simulation.grid[coords.getX()][coords.getY()] = Simulation.FieldType.FIELD_EMPTY;
                }
            } else if (clickingMode== clickingMode.EXAMINING){ //isExamining == true
                examinationTool.showPath(coords.getX(), coords.getY());
                examinationTool.printInfoAboutSegment(coords.getX(), coords.getY());
            } else if (clickingMode == clickingMode.SHORTEST_PATHING) {
                if (shortestPathingClass != null) {
                    shortestPathingClass.add(coords);
                }
            }

        });

        mainCanvas.setOnMouseDragged(e -> {
            if (clickingMode== clickingMode.NORMAL) {
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
            }
        });

        mainCanvas.setOnMouseReleased( e -> {
            currentMousePos = new Point2D(e.getX(), e.getY());
            if (e.getButton() == MouseButton.PRIMARY) {
                if (isDragging && !escWasPressed) {
                    isDragging = false;
                    Position newField = simulationGrid.getFieldWithMouseOn();
                    if (chosenFieldType != Simulation.FieldType.FIELD_ROAD1) {
                        simulationGrid.drawRectangleBetween(previousField, newField, chosenFieldType);
                    } else {
                        simulationGrid.drawPerpendicularLineBetween(previousField, newField, chosenFieldType);
                    }
                }
            } else if (e.getButton() == MouseButton.SECONDARY) {
                if (isDragging && !escWasPressed) {
                    isDragging = false;
                    Position newField = simulationGrid.getFieldWithMouseOn();
                    simulationGrid.drawRectangleBetween(previousField, newField, Simulation.FieldType.FIELD_EMPTY);
                }
            }
            isDragging = false;
            redraw();
        });
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

        System.out.println("Config was updated");
        redraw();
    }

    public void gridOpacitySliderUpdated() {
        gridOpacity = gridOpacitySlider.getValue();
    }


    public void generateGraphButtonPressed() {
        System.out.println("Printing graph");
        graphNodes = new GraphNodesContainer(simulationGrid.generateGraph());
        for(GraphNode node: graphNodes.get() ) {
            System.out.println(node.position);
            System.out.println(Arrays.toString(node.distances));
        }
        redraw();
        cmc = new CitizenMovementsContainer(graphNodes);

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
        pathAndDistances = t.dijkstra(graphNodes.get(),start);
        System.out.println("Distances");
        for (int i=0; i<pathAndDistances.length; i++) {
            System.out.printf(i+ "." + pathAndDistances[i].dist); //whole distance
            System.out.printf("[");
            if (pathAndDistances[i].node != null)
                System.out.printf(pathAndDistances[i].node.position.toString()); //first/last node position
            System.out.printf("----->");

            PathAndDistances pad2 = pathAndDistances[i].predecessor;

            ArrayList<GraphNode> path = new ArrayList<>(); //a path to show is created as an arraylist
            path.add(pathAndDistances[i].node); //the first/last node is added

            //while the predecessors are not null
            while (pad2 !=null) {
                if (pad2.node != null) { //if there's a node
                    path.add(pad2.node); // add it to a path to show
                    System.out.printf(pad2.node.position.toString() + "-->"); //also print that node
                }
                pad2 = pad2.predecessor;
            }
            System.out.printf(graphNodes.get().get(start).position.toString()); //print the last one
            path.add(graphNodes.get().get(start));
            nodePaths.add(path);

            System.out.printf("\n");
        }

    }

    public void shortestPathButtonPressed() {
        if (clickingMode!= clickingMode.SHORTEST_PATHING) {
            clickingMode = clickingMode.SHORTEST_PATHING;
            if (graphNodes != null) {
                shortestPathingClass = new ShortestPathingClass(simulation, simulationGrid, graphNodes, segmentsContainer);


            } else {
                System.out.println("GRAPH NOT GENERATED YET");
                System.out.println("GRAPH NOT GENERATED YET");
                System.out.println("GRAPH NOT GENERATED YET");
            }
        } else {
            clickingMode = clickingMode.NORMAL;
            simulationGrid.positionPathToDrawIsOn = false;
            redraw();
        }
    }


    public void roadToggleButtonToggled() {
        chosenFieldType = Simulation.FieldType.FIELD_ROAD1;
        simulationGrid.positionPathToDrawIsOn = false;
        viewMode = viewMode.NORMAL;
        clickingMode = clickingMode.NORMAL;
    }
    public void urbanAreaButtonToggled() {
        chosenFieldType = Simulation.FieldType.FIELD_URBAN1;
        viewMode = viewMode.NORMAL;
        clickingMode = clickingMode.NORMAL;
        simulationGrid.positionPathToDrawIsOn = false;
    }
    public void industryAreaButtonToggled() {
        chosenFieldType = Simulation.FieldType.FIELD_INDUSTRY1;
        viewMode = viewMode.NORMAL;
        clickingMode = clickingMode.NORMAL;
        simulationGrid.positionPathToDrawIsOn = false;
    }
    public void examineButtonPressed() {
        if (clickingMode!= clickingMode.EXAMINING) {
            examinationTool = new ExaminationTool(simulation, simulationGrid, graphNodes, segmentsContainer);
            clickingMode = clickingMode.EXAMINING;
        } else {
            clickingMode = clickingMode.NORMAL;
            simulationGrid.positionPathToDrawIsOn = false;
            redraw();
        }
    }

    public void newButtonPressed() {
        Simulation simulation1 = new Simulation();
        simulation = simulation1;
        calculateSize(simulation);
        simulationGrid = new SimulationGrid(this,simulation, fieldWidth,fieldHeight);
    }

    public void openFileButtonPressed() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file...");
        fileChooser.setInitialDirectory(new File("C:\\Users\\Jacek\\Desktop\\stufff\\"));
        File selectedFile = fileChooser.showOpenDialog(SimulationApplication.stage);
        openFile(selectedFile);
    }

    public void openFile(File selectedFile) {
        if (selectedFile!=null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                Simulation simulation1 = null;
                simulation1 = (Simulation) objectInputStream.readObject();
                simulation = simulation1;
                simulation.simulationStats = new SimulationStats(simulation);
                calculateSize(simulation);

                simulationGrid = new SimulationGrid(this,simulation, fieldWidth,fieldHeight);
                simulation.simulationStats.updateSegmentsCount();

                objectInputStream.close();
                fileInputStream.close();

                System.out.println("FileSuccessfully opedned");

                redraw();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFileButtonPressed() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file...");
        File file = fileChooser.showSaveDialog(SimulationApplication.stage);
        saveFile(file);
    }

    public void saveFile (File file) {
        if (file!=null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);

                out.writeObject(simulation);

                out.close();
                fileOutputStream.close();

                System.out.println("File successfully saved");


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void viewModeNormalButtonPressed() {
        viewMode = viewMode.NORMAL;
        redraw();
    }

    public void viewModeTrafficHeatButtonPressed() {
        if (viewMode!=viewMode.HEATMAP) {
            roadSegmentsContainer = new RoadSegmentsContainer(simulation);
            roadSegmentsContainer.generatePassengersMap(simulation, graphNodes, segmentsContainer);
            simulationGrid.roadOverlay = roadSegmentsContainer.getRoadOverlay();
            viewMode = viewMode.HEATMAP;
            redraw();
        }
        else {
            viewMode = viewMode.NORMAL;
            redraw();
        }

    }

    public void useButton1Pressed() {
        segmentsContainer = new SegmentsContainer(simulation);
        for (UrbanSegment us: segmentsContainer.urbanSegments) {
            us.calculateClosestRoadSegment(simulation, SEARCH_RADIUS);
            us.findClosestRoadNodes(simulation, graphNodes);
        }
        for (UrbanSegment us: segmentsContainer.urbanSegments)
            us.printSegmentStats();

        //urbanSegment = new UrbanSegment(40,40);
        //urbanSegment.calculateClosestRoadSegment(simulation,SEARCH_RADIUS);
    }

    public void useButton2Pressed() {
        for (IndustrySegment is: segmentsContainer.industrySegments) {
            is.calculateClosestRoadSegment(simulation, SEARCH_RADIUS);
            is.findClosestRoadNodes(simulation, graphNodes);
        }
        for (IndustrySegment is: segmentsContainer.industrySegments)
            is.printSegmentStats();

        //urbanSegment.findClosestRoadNodes(simulation, graphNodes);
    }
    public void useButton3Pressed() {
        //urbanSegment.printSegmentStats();




    }
    public void useButton4Pressed() {

        segmentsContainer = new SegmentsContainer(simulation);
        citizenTimer = new CitizenTimer(cmc, graphNodes,segmentsContainer, this);
        segmentsContainer.setGraphNodesContainer(graphNodes);
        segmentsContainer.bindRandomly();

        for (UrbanSegment us: segmentsContainer.urbanSegments) {
            us.calculateClosestRoadSegment(simulation, SEARCH_RADIUS);
            us.findClosestRoadNodes(simulation, graphNodes);
        }

        for (IndustrySegment is: segmentsContainer.industrySegments) {
            is.calculateClosestRoadSegment(simulation, SEARCH_RADIUS);
            is.findClosestRoadNodes(simulation, graphNodes);
        }

        System.out.println("Urban segments stats:");
        for (UrbanSegment us: segmentsContainer.urbanSegments)
            us.printSegmentStats();
        System.out.println("Industry segments stats:");
        for (IndustrySegment is: segmentsContainer.industrySegments)
            is.printSegmentStats();

        System.out.println("Finding paths");
        for (UrbanSegment us: segmentsContainer.urbanSegments) {
            us.findPathToCorrespondingSegment(graphNodes, -1, true);
            System.out.println(String.valueOf(us.distanceToIndustry) + ":" + us.pathToIndustry);

        }

    }

    public void printStats() {
        simulation.simulationStats.updateSegmentsCount();
        simulation.simulationStats.printStats();
    }

    public void setPlayingStopButtonPressed() {
        timerPlaying = false;
        currentTaskHelper.cancel();
        time.cancel();
        time.purge();
        System.out.println("TIMER RESET");
        cmc = new CitizenMovementsContainer(graphNodes);
        citizenTimer = new CitizenTimer(cmc, graphNodes,segmentsContainer, this);

        for (UrbanSegment us : segmentsContainer.urbanSegments) {
            us.outAlready = false;
        }

    }


    public void setPlayingPlayButtonPressed() {
        if (timerPlaying) {
            timerPlaying = false;
            currentTaskHelper.cancel();


        } else {
            timerPlaying = true;
            time = new Timer();
            currentTaskHelper = new TaskHelper(this);
            citizenTimer.setTimeSpeed(timeSpeed);
            time.schedule(currentTaskHelper,0, (long) (1000*(1.0f/timerFps)));
        }
    }

    public void playingOneStepForwardButtonPressed() {
        citizenTimer.startCitizens();

        citizenTimer.incrementTimeAndCheck();
        Platform.runLater(
                () -> {
                    stepLabel.setText(String.valueOf(citizenTimer.getGeneration()));
                    timeLabel.setText(String.valueOf(citizenTimer.getTime()));
                }
        );

        citizenTimer.printTimeStats();

        if (roadSegmentsContainer!=null) {
            System.out.println("Updating heat");
            roadSegmentsContainer.resetCalculatedAlready();
            roadSegmentsContainer.generatePassengersMap(simulation, graphNodes, segmentsContainer);
            simulationGrid.roadOverlay = roadSegmentsContainer.getRoadOverlay();
        }
        Platform.runLater(
                () -> {
            redraw();
        });

    }

    public void setMainSpeedSliderUpdated() {
        timeSpeed = mainSpeedSlider.getValue();
        System.out.println("Slider's value: " + timeSpeed);
        if (timerPlaying) {
            currentTaskHelper.cancel();
            currentTaskHelper = new TaskHelper(this);

            citizenTimer.setTimeSpeed(timeSpeed);
            time.scheduleAtFixedRate(currentTaskHelper, 500, (long) (1000*(1.0f/timerFps)));
        }
    }

    public void fpsSpinnerUpdated() {
        timerFps = fpsSpinner.getValue();

        if (timerPlaying) {
            currentTaskHelper.cancel();
            currentTaskHelper = new TaskHelper(this);

            citizenTimer.setTimeSpeed(timeSpeed);
            time.scheduleAtFixedRate(currentTaskHelper, 500, (long) (1000*(1.0f/timerFps)));
        }
    }

    public void makeCitizensSmartCheckBoxUpdated(){
        citizensAreSmart = !citizensAreSmart;
    }



}
