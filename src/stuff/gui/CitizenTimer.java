package stuff.gui;

import javafx.concurrent.Task;

import java.util.ArrayList;

class CitizenTimer {

    private double time = 0.001;
    private double step = 0.05;
    private int generation = 0;

    CitizenMovementsContainer cmc;
    GraphNodesContainer graphNodes;

    public CitizenTimer(CitizenMovementsContainer cmc, GraphNodesContainer graphNodes) {
        this.cmc = cmc;
        this.graphNodes = graphNodes;
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
}
