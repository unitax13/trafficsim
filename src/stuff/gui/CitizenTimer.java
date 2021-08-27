package stuff.gui;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TimerTask;

class CitizenTimer{

    private double time = 0.000001;
    private double step = 0.05;
    private int generation = 0;

    int urbanSegmentsNotOutYet = 1000000;

    CitizenMovementsContainer cmc;
    GraphNodesContainer graphNodes;
    SegmentsContainer segmentsContainer;
    MainWindow mainWindow;

    public CitizenTimer(CitizenMovementsContainer cmc, GraphNodesContainer graphNodes, SegmentsContainer segmentsContainer, MainWindow mainWindow) {
        this.cmc = cmc;
        this.graphNodes = graphNodes;
        this.segmentsContainer = segmentsContainer;
        this.mainWindow = mainWindow;
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
        if (mainWindow.noMovingCitizens==false) {
            generation++;
            incrementOnlyTime();
            applyStepTask();
        } else {
            mainWindow.timerPlaying = false;
            mainWindow.currentTaskHelper.cancel();
        }
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
        int MAX_PACK = 30;
        if (segmentsContainer.getUrbanSegmentsNotOutYet()!=null && urbanSegmentsNotOutYet>0) {
            ArrayList<UrbanSegment> segmentsNotOutYet = segmentsContainer.getUrbanSegmentsNotOutYet();

            for (int i = 0; i < MAX_PACK && i < segmentsNotOutYet.size(); i++) {
                Random r = new Random();
                int bound = segmentsNotOutYet.size();
                int j = r.nextInt(bound);
                Collections.swap(segmentsNotOutYet, i,j);

            }

            for (int i = 0; i < MAX_PACK && i < segmentsNotOutYet.size(); i++) {
                Random r = new Random();
                int bound = segmentsNotOutYet.size()>MAX_PACK ? MAX_PACK : segmentsNotOutYet.size();
                int j = r.nextInt(bound);
                if (segmentsNotOutYet.get(j).outAlready == false) {
                    MovingCitizen movingCitizen = new MovingCitizen(segmentsNotOutYet.get(j), generation, time, step);
                    segmentsNotOutYet.get(j).outAlready = true;
                    cmc.addMovingCitizen(movingCitizen);
                }


            }
            urbanSegmentsNotOutYet = segmentsNotOutYet.size();
        }
    }

}
