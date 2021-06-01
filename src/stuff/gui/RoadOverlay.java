package stuff.gui;

import javafx.scene.paint.Color;

public class RoadOverlay {

    public Color[][] colorMap;
    int width=0;
    int height=0;

    public RoadOverlay(RoadSegmentsContainer rsc, Simulation simulation) {
        width = simulation.width;
        height = simulation.height;
        colorMap = new Color[width][height];
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                colorMap[i][j] = Color.color(0,0,0,0);
            }
        }

        for (RoadSegment rs: rsc.roadSegments) {
            int red = (int) ( 250*((double) rs.passengers/rs.capacity));
            if (red<0)
                red = 0;
            else if (red>255)
                red = 255;
            int green = (int) (250 - 250 *((double) rs.passengers/rs.capacity));
            if (green<0)
                green = 0;
            else if (green>255)
                green = 255;
            Color color = Color.rgb(red,green,0,0.9);
            colorMap[rs.position.getX()][rs.position.getY()] = color;
            //System.out.println("Colora map of " + rs.position + " has gotten color " + color);
        }

    }

}
