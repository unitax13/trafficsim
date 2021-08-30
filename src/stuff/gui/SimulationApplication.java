package stuff.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import stuff.gui.chartsAndStats.StatsContainer;
import stuff.gui.utils.KeyListener;

import java.io.File;


public class SimulationApplication extends Application {

    public static Stage stage;
    public Scene scene;
    private Parent root;
    public static MainWindow mainWindow;
    public static StatsContainer statsContainer = new StatsContainer();

    public static int SCREEN_WIDTH = 892;
    public static int SCREEN_HEIGHT = 840;
    public static boolean IS_DEBUGGING = false;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        root = loader.load(getClass().getResource("mainWindow.fxml"));
        this.stage = stage;


        stage.setTitle("TrafficSIM");
        scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);


        File file = new File("C:\\Users\\Jacek\\Desktop\\stufff\\mala_kratownica");
        mainWindow.openFile(file);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //KeyListener.keyIsPressed(keyEvent.getCode().getCode());

                //System.out.println("Key of code " + keyEvent.getCode() + " was pressed");
                switch (keyEvent.getCode()) {
                    case A:
                        mainWindow.simulationGrid.cameraX -= 10;
                        break;
                    case D:
                        mainWindow.simulationGrid.cameraX += 10;
                        break;
                    case W:
                        mainWindow.simulationGrid.cameraY -= 10;
                        break;
                    case S:
                        mainWindow.simulationGrid.cameraY += 10;
                        break;
                    case F1:
                        mainWindow.simulationGrid.cameraX = 0;
                        mainWindow.simulationGrid.cameraY = 0;
                        mainWindow.simulationGrid.cameraScale = 1;
                        mainWindow.redraw();
                    default:
                        break;
                }
                mainWindow.redraw();
            }

        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyListener.keyIsReleased(keyEvent.getCode().getCode());
            }
        });




        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();



    }

    public Stage getStage() {
        return stage;
    }

}
