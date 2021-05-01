package stuff.gui;

import javafx.scene.input.KeyCode;

import static stuff.gui.SimulationApplication.mainWindow;

public class KeyListener {
    private static KeyListener instance;
    public boolean keyPressed[] = new boolean[350];
    //private boolean keyBeginPress[] = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }

    public static boolean keyIsPressed(int keyCode) {
        get().keyPressed[keyCode] = true;
        keyStateUpdated();
        return true;
    }

    public static boolean keyIsReleased(int keyCode) {
        boolean result = get().keyPressed[keyCode] = false;
        keyStateUpdated();
        return false;
    }

    public static void keyStateUpdated() {
        if (get().keyPressed[KeyCode.ESCAPE.getCode()]) {
            System.out.println("Esc is pressed");
        } else {
            System.out.println("Esc is not pressed");
        }


        
        if (get().keyPressed[KeyCode.ESCAPE.getCode()]) {
            mainWindow.escWasPressed = true;
            System.out.println("esc was pressed det to true");
        }
    }
}
