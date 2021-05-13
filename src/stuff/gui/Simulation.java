package stuff.gui;

import java.io.Serializable;
import java.util.ArrayList;

public class Simulation implements Serializable {

    private final int defaultGenerationWidth = 100;
    private final int defaultGenerationHeight = 100;

    public enum FieldType {FIELD_EMPTY, FIELD_ROAD1, FIELD_URBAN1, FIELD_INDUSTRY1};

    public int width, height;
    public FieldType [][] grid;

    public transient SimulationStats simulationStats;


    public Simulation () {
        width = defaultGenerationWidth;
        height = defaultGenerationHeight;
        grid = new FieldType[width][height];
        simulationStats = new SimulationStats(this);

        grid[40][40] = FieldType.FIELD_URBAN1;

    }

    public Simulation(int width, int height) {
        this.width =width;
        this.height=height;
        simulationStats = new SimulationStats(this);

        grid = new FieldType[width][height];

    }

    public ArrayList<Integer> getSegmentsCount() {
        int numberOfUrbanSegments = 0;
        int numberOfIndustrySegments = 0;
        int numberOfRoad1Segments = 0;
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
        ArrayList<Integer> stats = new ArrayList<>();
        stats.add(numberOfUrbanSegments);
        stats.add(numberOfIndustrySegments);
        stats.add(numberOfRoad1Segments);

        return stats;
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
