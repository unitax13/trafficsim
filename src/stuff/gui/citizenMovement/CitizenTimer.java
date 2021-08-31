package stuff.gui.citizenMovement;

import stuff.gui.*;
import stuff.gui.citizenMovement.CitizenMovementsContainer;
import stuff.gui.citizenMovement.MovingCitizen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CitizenTimer{

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
            SimulationApplication.statsContainer.printSummary();
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

        cmc.getFinishedMovementsAndAddNextOnes(time, generation);
        cmc.initMovementForNotMoving(time, generation);



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
