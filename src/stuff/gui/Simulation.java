package stuff.gui;

import java.io.Serializable;

public class Simulation implements Serializable {

    private final int defaultGenerationWidth = 100;
    private final int defaultGenerationHeight = 100;

    public enum FieldType {FIELD_EMPTY, FIELD_ROAD1, FIELD_URBAN1, FIELD_INDUSTRY1};

    public int width, height;
    public FieldType [][] grid;

    public Simulation () {
        width = defaultGenerationWidth;
        height = defaultGenerationHeight;
        grid = new FieldType[width][height];

    }

    public Simulation(int width, int height) {
        this.width =width;
        this.height=height;
        grid = new FieldType[width][height];

    }

    public int getNeighboursOfStateFromGrid(FieldType[][] grid, FieldType state, int x, int y) {
        int neighbours = 0;
        //if ( getCellFromGrid(grid, x - 1, y - 1) == state ) neighbours++;
        if ( getCellFromGrid(grid, x, y - 1) == state ) neighbours++;
        //if ( getCellFromGrid(grid, x + 1, y - 1) == state ) neighbours++;
        if ( getCellFromGrid(grid, x - 1, y) == state ) neighbours++;
        if ( getCellFromGrid(grid, x + 1, y) == state ) neighbours++;
        //if ( getCellFromGrid(grid, x - 1, y + 1) == state ) neighbours++;
        if ( getCellFromGrid(grid, x, y + 1) == state ) neighbours++;
        //if ( getCellFromGrid(grid, x + 1, y + 1) == state ) neighbours++;

        return neighbours;
    }


    public void setCell(FieldType state, int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height)
            grid[x][y] = state;
    }

    public FieldType getCellFromGrid(FieldType[][] grid, int x, int y) {
        if ( x < 0 || x >=width || y < 0 || y >= height )
            return FieldType.FIELD_EMPTY; //jezeli poza granicami, to przyjmujemy, ze to FIELD_EMPTY
        else
            return grid[x][y];
    }

}
