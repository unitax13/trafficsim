package stuff.gui;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.awt.event.MouseListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class SimulationApplication extends Application {

    public static Stage stage;
    public Scene scene;
    private Parent root;
    public static MainWindow mainWindow;

    public static int SCREEN_WIDTH = 892;
    public static int SCREEN_HEIGHT = 840;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        root = loader.load(getClass().getResource("mainwindow.fxml"));
        this.stage = stage;


        stage.setTitle("TrafficSIM");
        scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

//        scene.setOnKeyPressed(keyEvent -> {
//            System.out.println("Key event:" + keyEvent.toString());
//            if (keyEvent.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc was clicked");
//            }
//        });

        File file = new File("C:\\Users\\Jacek\\Desktop\\stufff\\symetry1");
        mainWindow.openFile(file);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyListener.keyIsPressed(keyEvent.getCode().getCode());
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
