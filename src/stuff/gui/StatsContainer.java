package stuff.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class StatsContainer {

    public ArrayList<ArrayList<Integer>> movingCitizensChartData = new ArrayList<>();
    public ArrayList<TreeMap<Double, Integer>> distanceChartData = new ArrayList<>();
    public ArrayList<TreeMap<Double, Integer>> timeChartData = new ArrayList<>();

    public int currentlyMoving = 0;
    public int pathsChanges = 0;
    public int turnsBackAround = 0;


    StatsContainer() {

    }

    public void printSummary() {
        System.out.println("\nSIMULATION SUMMARY:");
        System.out.println("Smart citizen path changes: " + pathsChanges);
        System.out.println("Number of back-around turns: " + turnsBackAround);
        System.out.println(" ");
    }

    public void clearStats() {
        movingCitizensChartData = new ArrayList<>();
        distanceChartData = new ArrayList<>();
        timeChartData = new ArrayList<>();
        clearLightStats();
    }

    public void clearLightStats() {
        currentlyMoving = 0;
        pathsChanges = 0;
        turnsBackAround = 0;
    }

    public void createStatMovingCitizensSizeStat() {
        movingCitizensChartData.add(new ArrayList<Integer>());
    }

    public void addStatMovingCitizensSizeStat(int currentlyMoving) {
        if (movingCitizensChartData.size() == 0) {
            createStatMovingCitizensSizeStat();
        } else {
            movingCitizensChartData.get(movingCitizensChartData.size()-1).add(currentlyMoving);
        }

    }

    public void createDistanceData (SegmentsContainer segmentsContainer) {
        TreeMap<Double, Integer> data = new TreeMap<>();
        for (UrbanSegment us: segmentsContainer.urbanSegments) {
            if (us.boundIndustrySegment!=null && us.pathToIndustry!=null) {
                us.distanceToIndustry = us.calculatePositionDistance();
                if (data.containsKey(us.distanceToIndustry)) {
                    data.replace(us.distanceToIndustry,data.get(us.distanceToIndustry)+1);
                } else {
                    data.put(us.distanceToIndustry, 1);
                }
            }
        }
        double min = data.firstKey();
        double max = data.lastKey();
        for (int j= (int) Math.floor(min); j< (int) Math.ceil(max); j++) {
            if (!data.containsKey((double) j)) {
                data.put((double) j, 0);
            }
        }

        if (!distanceChartData.contains(data)) //So that they are unique
            distanceChartData.add(data);
    }

    public void createTimeData(SegmentsContainer segmentsContainer) {
        TreeMap<Double, Integer> data = new TreeMap<>();
        for (UrbanSegment us: segmentsContainer.urbanSegments) {
            if (us.boundIndustrySegment!=null) {

                if (data.containsKey(us.totalTimeTravellingTook)) {
                    data.replace(us.totalTimeTravellingTook,data.get(us.totalTimeTravellingTook)+1);
                } else {
                    data.put(us.totalTimeTravellingTook, 1);
                }
            }
        }
//        double min = data.firstKey();
//        double max = data.lastKey();
//        for (double j= Math.floor(min*100)/100; j< Math.ceil(max*100)/100; j+=0.01) {
//            if (!data.containsKey( j)) {
//                data.put( j, 0);
//            }
//        }

        if (!timeChartData.contains(data)) {
            timeChartData.add(data);
        }

    }

    public void showDistanceChart(SegmentsContainer segmentsContainer) throws IOException {

        if (distanceChartData.size()>0) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("distanceChartWindow.fxml"));
            Parent root = loader.load();

            Stage window = new Stage();
            window.setTitle("Distance chart");
            window.setMinWidth(250.0);

            window.setScene(new Scene(root));
            window.show();

            DistanceChartWindowController charts = loader.getController();

            if (charts == null) {
                System.out.println("charts is null");
            }


            charts.showBarChartDistance(distanceChartData);
        }
        else System.out.println("No data");
    }

    public void showTimeChart(SegmentsContainer segmentsContainer) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("timeChartWindow.fxml"));
        Parent root = loader.load();

        Stage window = new Stage();
        window.setTitle("Time chart");
        window.setMinWidth(250.0);

        window.setScene(new Scene(root));
        window.show();

        TimeChartWindowController charts = loader.getController();

        if (charts==null) {
            System.out.println("charts is null");
        }

        charts.showBarChartTime(timeChartData);
    }

    public void showCitizensNumberOnMapChart() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("citizensOnMapChartWindow.fxml"));
        Parent root = loader.load();

        Stage window = new Stage();
        window.setTitle("Citizens out chart");
        window.setMinWidth(250.0);

        window.setScene(new Scene(root));
        window.show();

        CitizensOnMapChartWindowController charts = loader.getController();

        if (charts==null) {
            System.out.println("charts is null");
        }

        charts.showLineChartTime(movingCitizensChartData);
    }

}
