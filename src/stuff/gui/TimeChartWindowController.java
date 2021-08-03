package stuff.gui;

import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.TreeMap;

public class TimeChartWindowController {
    @FXML
    private  AnchorPane chartsWindowPane;
    @FXML
    private  Label averageText;
    @FXML
    private  Label medianText;
    @FXML
    private BarChart barChart;
    @FXML
    private Axis<String> xAxis;
    @FXML
    private Axis<Number> yAxis;


    public void showBarChartTime(SegmentsContainer segmentsContainer) {


        yAxis.setLabel("Amount of citizens");
        xAxis.setLabel("Time it took to get to destination");

        XYChart.Series<String, Number> series = new XYChart.Series<>();


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

        data.forEach((k,v) -> series.getData().add(new XYChart.Data<>("" + k, v)));

        double average = 0;
        int totalCitizens = 0;
        for (double key:data.keySet()) {
            average += key*data.get(key);
            totalCitizens += data.get(key);
        }

        average = average/totalCitizens;

        int citizens = 0;
        double median = 0;

        for (double key:data.keySet()) {
            citizens += data.get(key);
            if (citizens<(totalCitizens/2)) {
                median = key;
            }
        }
        averageText.setText("" + ((double)Math.round(average*1000))/1000);
        medianText.setText("" + ((double)Math.round(median*1000))/1000);


        yAxis.setAnimated(false);

        barChart.getData().add(series);

        barChart.setLegendVisible(false);
        barChart.setTitle("Time to destination of citizens chart");


//        barChart.setLegendVisible(true);



    }
}
