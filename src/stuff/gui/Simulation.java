package stuff.gui;

import java.io.Serializable;

public class Simulation implements Serializable {

    private final int defaultGenerationWidth = 100;
    private final int defaultGenerationHeight = 100;

    public enum FieldType {FIELD_EMPTY, FIELD_ROAD1, FIELD_URBAN1, FIELD_INDUSTRY1};

    public int width, height;
    public FieldType [][] grid;

    public int numberOfUrbanSegments = 0;
    public int numberOfIndustrySegments = 0;
    public int numberOfRoad1Segments = 0;


    public Simulation () {
        width = defaultGenerationWidth;
        height = defaultGenerationHeight;
        grid = new FieldType[width][height];

        grid[40][40] = FieldType.FIELD_URBAN1;

    }

    public Simulation(int width, int height) {
        this.width =width;
        this.height=height;
        grid = new FieldType[width][height];

    }

    public void updateSegmentsCount() {
        numberOfUrbanSegments = 0;
        numberOfIndustrySegments = 0;
        numberOfRoad1Segments = 0;
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                if (grid[i][j] == FieldType.FIELD_URBAN1) {
                    numberOfUrbanSegments++;
                } else if (grid[i][j] == FieldType.FIELD_INDUSTRY1) {
                    numberOfIndustrySegments++;
                } else if (grid[i][j] == FieldType.FIELD_ROAD1) {
                    numberOfRoad1Segments++;
                }
            }
        }
    }

    public FieldType get(int x, int y) {
        if (x>=0 && x<width && y>=0 && y<height) {
            return grid[x][y];
        } else return FieldType.FIELD_EMPTY;
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
