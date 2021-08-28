package stuff.gui;

import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.TreeMap;

public class CitizensOnMapChartWindowController {
    @FXML
    private  AnchorPane chartsWindowPane;
    @FXML
    private LineChart lineChart;
    @FXML
    private Axis<String> xAxis;
    @FXML
    private Axis<Number> yAxis;


    public void showLineChartTime(ArrayList<ArrayList<Integer>> movingCitizensChartData) {


        yAxis.setLabel("Citizens on the map");
        xAxis.setLabel("Steps");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for (int i=0; i<movingCitizensChartData.size(); i++ ) {

            ArrayList<Integer> movingCitizensSizeStat = movingCitizensChartData.get(i);

            TreeMap<Double, Integer> data = new TreeMap<>();
            int j = 0;
            for (int number : movingCitizensSizeStat) {
                data.put((double) j, number);
                j++;
            }


            data.forEach((k, v) -> series.getData().add(new XYChart.Data<>(k, v)));


            yAxis.setAnimated(false);

            lineChart.getData().add(series);
        }

        lineChart.setLegendVisible(false);
        lineChart.setTitle("Citizens out at each time step chart");


//        lineChart.setLegendVisible(true);



    }
}
