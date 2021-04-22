package stuff.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class MapCanvas extends Canvas {

    private static final String BACKGROUND_COLOR = "#CEF51C";
    private static final int OFFSET = 20;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int CANVAS_BORDER_WIDTH = 6;
    private static final int RENDER_FRAME_OFFSET = 80;

    private int renderOffsetX;
    private int renderOffsetY;
    private double scale;

    public static boolean roadsIsOn = true;
    public static boolean urbanIsOn = true;
    public static boolean industryIsOn = true;
    public static boolean otherIsOn = true;



    public MapCanvas() {
        super(WIDTH, HEIGHT);
        GraphicsContext gc = getGraphicsContext2D();
        renderMap(gc);

        //loadImages();

    }

    private void renderMap(GraphicsContext gc) {
//        renderBackground(gc);
//        renderBorders(gc);
//        renderRoads(gc);
//        renderMonuments(gc);
//        renderHospitals(gc);
//        renderCoordinates(gc);
        renderFrame(gc);
    }



    private void renderFrame(GraphicsContext gc) {
        gc.setFill(Color.web("#777777"));
        gc.fillRect(0, 0, OFFSET, HEIGHT);
        gc.fillRect(0, 0, WIDTH, OFFSET);
        gc.fillRect(WIDTH - OFFSET, 0, OFFSET, HEIGHT);

        gc.setFill(Color.BLACK);
        gc.fillRect(OFFSET, OFFSET, CANVAS_BORDER_WIDTH, HEIGHT - OFFSET);
        gc.fillRect(OFFSET, OFFSET, WIDTH - OFFSET * 2, CANVAS_BORDER_WIDTH);
        gc.fillRect(WIDTH - OFFSET - CANVAS_BORDER_WIDTH, OFFSET, CANVAS_BORDER_WIDTH, HEIGHT - OFFSET);
        gc.fillRect(OFFSET, HEIGHT - CANVAS_BORDER_WIDTH, WIDTH - OFFSET * 2, CANVAS_BORDER_WIDTH);
    }

    private Position drawImageCentered(GraphicsContext gc, Image image, Position physicalPosition) {
        double positionX = translatePhysicalToCanvasX(physicalPosition.getX());
        double positionY = translatePhysicalToCanvasY(physicalPosition.getY());
        gc.drawImage(image, positionX - image.getWidth() / 2, positionY - image.getHeight() / 2);
        return new Position((int)positionX, (int)positionY);
    }

    private double translatePhysicalToCanvasX(int physicalX) {
        return (physicalX + renderOffsetX) * scale + OFFSET + RENDER_FRAME_OFFSET;
    }

    private double translatePhysicalToCanvasY(int physicalY) {
        return HEIGHT - ((physicalY + renderOffsetY) * scale + OFFSET + RENDER_FRAME_OFFSET);
    }

    private int translateCanvasToPhysicalX(double canvasX) {
        return (int)((canvasX - RENDER_FRAME_OFFSET - OFFSET) / scale - renderOffsetX);
    }

    private int translateCanvasToPhysicalY(double canvasY) {
        return (int)((canvasY - RENDER_FRAME_OFFSET - OFFSET) / scale - renderOffsetY);
    }

    private void drawCoordinates(GraphicsContext gc, Position physicalPosition) {
        double x = translatePhysicalToCanvasX(physicalPosition.getX());
        double y = translatePhysicalToCanvasY(physicalPosition.getY());
        String coordinatesString = "(" + physicalPosition.getX() + ", " + physicalPosition.getY() + ")";
        gc.setFill(Color.BLUE);
        gc.fillText(coordinatesString, x - 30, y - 32);
    }
}
