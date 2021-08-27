package stuff.gui;

import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

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


    public void showLineChartTime(SegmentsContainer segmentsContainer) {


        yAxis.setLabel("Citizens on the map");
        xAxis.setLabel("Steps");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();


        TreeMap<Double, Integer> data = new TreeMap<>();
        int i=0;
        for(int number: MainWindow.statMovingCitizensSizeStat) {
            data.put((double) i, number);
            i++;
        }


        data.forEach((k,v) -> series.getData().add(new XYChart.Data<>(k, v)));


        yAxis.setAnimated(false);

        lineChart.getData().add(series);

        lineChart.setLegendVisible(false);
        lineChart.setTitle("Citizens out at each time step chart");


//        lineChart.setLegendVisible(true);



    }
}
