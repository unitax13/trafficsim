package stuff.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {


    private static final int minWidth = 100;
    private static final int heightForButtons = 200;
    private int width, height;
    private int fieldWidth, fieldHeight;
    private static final int maxHeight = 960;
    private static final int maxWidth = 1600;
    private static final int preferredFieldSize = 100;

    private Point2D previousMousePos = new Point2D(-1,-1);
    private Point2D previousField = new Point2D(0,0);
    boolean isDragging = false;

    Simulation simulation;
    SimulationGrid simulationGrid;
    public GraphicsContext gc;

    @FXML
    private CheckBox roadsViewButton;
    @FXML
    private CheckBox urbanViewButton;
    @FXML
    private CheckBox industryViewButton;
    @FXML
    private CheckBox otherCheckBox;

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
            MapCanvas.roadsIsOn = true;
        else
            MapCanvas.roadsIsOn = false;
        if (urbanViewButton.isSelected())
            MapCanvas.urbanIsOn = true;
        else
            MapCanvas.urbanIsOn = false;
        if (industryViewButton.isSelected())
            MapCanvas.industryIsOn = true;
        else
            MapCanvas.industryIsOn = false;
        if (otherCheckBox.isSelected())
            MapCanvas.otherIsOn = true;
        else
            MapCanvas.otherIsOn = false;

        System.out.println("Config was updated");
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(getClass() + "was called");
        gc = mainCanvas.getGraphicsContext2D();
        simulation = new Simulation();

        initCanvas();
        initCallbacks();
        calculateSize(simulation);
        simulationGrid = new SimulationGrid(this,simulation, fieldWidth,fieldHeight);
        simulationGrid.draw(gc);
        SimulationApplication.mainCanvas = mainCanvas;
    }

    private void initCanvas() {

        mainCanvas.setOnMouseMoved(mouseEvent -> {
            System.out.println("Mouse is moved");
            currentMousePos = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        });
        
        mainCanvas.setOnMouseClicked(event -> {

        });
        

        gc.setFill(Color.LIGHTCYAN);
        gc.fillRect(50, 100,200,300);
    }

    public void initCallbacks() {
//        gc.setStroke(Color.BLACK);
//        gc.setLineWidth(2);
//        mainCanvas.setOnMouseDragged( e ->{
//            gc.lineTo(e.getX(),e.getY() );
//            gc.stroke();
//        });

        mainCanvas.setOnMouseClicked( e -> {
            System.out.println("Mouse clicked");
            simulationGrid.draw(gc);
        });

        mainCanvas.setOnMousePressed(e -> {
            Point2D coords = simulationGrid.getFieldWithMouseOn();
            simulation.grid[(int) coords.getX()][(int) coords.getY()] = Simulation.FieldType.FIELD_ROAD1;
        });

        mainCanvas.setOnMouseDragged(e -> {
            currentMousePos = new Point2D(e.getX(), e.getY());
           // System.out.println("Is dragging");
            if (isDragging == false) {
                isDragging = true;
                previousMousePos = getCurrentMousePos();

                previousField = simulationGrid.getFieldWithMouseOn();

            }
            System.out.println("Prevoius field: " + previousField);
            //System.out.println("Previous mouse pos: " + previousMousePos);
            simulationGrid.draw(gc);
        });

        mainCanvas.setOnMouseReleased( e -> {
            currentMousePos = new Point2D(e.getX(), e.getY());
            if (isDragging) {
                isDragging = false;
                Point2D newField = simulationGrid.getFieldWithMouseOn();
                simulationGrid.drawPerpendicularLineBetween(previousField, newField );
            }
            isDragging = false;
            simulationGrid.draw(gc);
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
        //simulationGrid.draw(gc);
        //System.out.println("onMouseMoved");
    }
    public void onMousePressed() {
        //System.out.println("onMousePressed");
       // simulationGrid.draw(gc);
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
    
    
}
