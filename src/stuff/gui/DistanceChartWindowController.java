package stuff.gui;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;


public class DistanceChartWindowController {
    public AnchorPane chartsWindowPane;
    @FXML
    private Label averageText;
    @FXML
    private Label medianText;
    @FXML
    private  Label minText;
    @FXML
    private  Label maxText;
    @FXML
    private BarChart barChart;
    @FXML
    private Axis<String> xAxis;
    @FXML
    private Axis<Number> yAxis;


    public void showBarChartDistance(ArrayList<TreeMap<Double, Integer>> distanceChartData) {


        yAxis.setLabel("Amount of citizens");
        xAxis.setLabel("Theoretical distance to destination");

        //series.setName("Hello there");

        for (int i=0; i< distanceChartData.size(); i++) {

            XYChart.Series<String, Number> mainSeries = new XYChart.Series<>();

            TreeMap<Double,Integer> data = distanceChartData.get(i);

            data.forEach((k, v) -> mainSeries.getData().add(new XYChart.Data<>("" + k, v)));

            double min = -1;
            double max = -1;
            if (!data.isEmpty()) {
                min = data.firstKey();
                max = data.firstKey();
            }

            double average = 0;
            int totalCitizens = 0;
            for (double key : data.keySet()) {
                average += key * data.get(key);
                totalCitizens += data.get(key);

                if (key > max)
                    max = key;
                if (key < min)
                    min = key;
            }

            average = average / totalCitizens;

            int citizens = 0;
            double median = 0;

            for (double key : data.keySet()) {
                citizens += data.get(key);
                if (citizens < (totalCitizens / 2)) {
                    median = key;
                }
            }
            averageText.setText("" + ((double) Math.round(average * 1000)) / 1000);
            medianText.setText("" + ((double) Math.round(median * 1000)) / 1000);
            minText.setText("" + ((double) Math.round(min * 1000)) / 1000);
            maxText.setText("" + ((double) Math.round(max * 1000)) / 1000);

            yAxis.setAnimated(false);

            barChart.getData().add(mainSeries);
        }

        barChart.setLegendVisible(true);
        barChart.setTitle("Distance to destination chart");
//        barChart.setLegendVisible(true);



    }
}

