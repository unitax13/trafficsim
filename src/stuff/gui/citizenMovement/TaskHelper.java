package stuff.gui.citizenMovement;

import stuff.gui.MainWindow;

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
