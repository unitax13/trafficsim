package stuff.gui;

import stuff.gui.utils.Position;

import java.util.ArrayList;

public class IndustrySegment extends Segment{
    public UrbanSegment boundUrbanSegment;
    public ArrayList<Position> pathToUrban;

    public IndustrySegment(int x, int y) {
        super(x, y);
    }
}
