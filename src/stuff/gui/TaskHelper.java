package stuff.gui;

import java.util.TimerTask;

public class TaskHelper extends TimerTask {

    MainWindow mainWindow;

    public TaskHelper(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    @Override
    public void run() {
        mainWindow.playingOneStepForwardButtonPressed();
    }
}
