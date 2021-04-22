package stuff.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SimulationApplication extends Application {

    private Stage stage;
    private Parent root;
    public static Canvas mainCanvas;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        root = loader.load(getClass().getResource("mainwindow.fxml"));

        this.stage = stage;

        stage.setTitle("COVID-33");
        Scene scene = new Scene(root, 800, 800);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();



    }

    private void initPane() {

        //initControls();
        initMapCanvas();
        //root.getChildrenUnmodifiable().add(mapCanvas);
        //StackPane stackPane = new StackPane();
        //stackPane.getChildren().add(mapCanvas);
        //layout.getChildren().addAll(stackPane, pane);
    }

    private void initMapCanvas() {


    }
}
