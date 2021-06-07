package stuff.gui;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

class CitizenTimer{

    private double time = 0.001;
    private double step = 0.05;
    private int generation = 0;

    int urbanSegmentsNotOutYet = 100000;

    CitizenMovementsContainer cmc;
    GraphNodesContainer graphNodes;
    SegmentsContainer segmentsContainer;

    public CitizenTimer(CitizenMovementsContainer cmc, GraphNodesContainer graphNodes, SegmentsContainer segmentsContainer, MainWindow mainWindow) {
        this.cmc = cmc;
        this.graphNodes = graphNodes;
        this.segmentsContainer = segmentsContainer;
    }


    public double getTime () {
        return time;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public void setTimeSpeed(double speed) {
        setStep(speed/1000);
    }
    public void incrementTimeAndCheck() {
        generation++;
        incrementOnlyTime();
        applyStepTask();
    }

    public int getGeneration() {
        return generation;
    }

    public void incrementOnlyTime() {
        time += step;
    }

    public void printTimeStats() {
        System.out.println("The time is " + time + " (generation " + generation + "), with a step of " + step);
    }



    public void applyStepTask() {

        cmc.getFinishedMovementsAndAddNextOnes(time);
        cmc.initMovementForNotMoving(time);



    }

    public void startCitizens() {
        if (segmentsContainer.getUrbanSegmentsNotOutYet()!=null && urbanSegmentsNotOutYet>0) {

            for (int i = 0; i < 30 && i < segmentsContainer.getUrbanSegmentsNotOutYet().size(); i++) {
                Random r = new Random();
                int bound = segmentsContainer.getUrbanSegmentsNotOutYet().size()>10 ? 10 : segmentsContainer.getUrbanSegmentsNotOutYet().size();
                int j = r.nextInt(bound);
                MovingCitizen movingCitizen = new MovingCitizen(segmentsContainer.getUrbanSegmentsNotOutYet().get(j));
                segmentsContainer.getUrbanSegmentsNotOutYet().get(j).outAlready = true;
                cmc.addMovingCitizen(movingCitizen);

            }
            urbanSegmentsNotOutYet = segmentsContainer.getUrbanSegmentsNotOutYet().size();
        }
    }

}
