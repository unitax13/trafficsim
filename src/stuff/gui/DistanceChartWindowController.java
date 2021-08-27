package stuff.gui;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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


    public void showBarChartDistance(SegmentsContainer segmentsContainer) {


        yAxis.setLabel("Amount of citizens");
        xAxis.setLabel("Theoretical distance to destination");

        XYChart.Series<String, Number> mainSeries = new XYChart.Series<>();
        //series.setName("Hello there");

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

        data.forEach((k,v) -> mainSeries.getData().add(new XYChart.Data<>("" + k, v)));

        double min = -1;
        double max = -1;
        if (!data.isEmpty()) {
            min = data.firstKey();
            max = data.firstKey();
        }

        double average = 0;
        int totalCitizens = 0;
        for (double key:data.keySet()) {
            average += key*data.get(key);
            totalCitizens += data.get(key);

            if (key>max)
                max = key;
            if (key<min)
                min = key;
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
        minText.setText("" + ((double)Math.round(min*1000))/1000);
        maxText.setText("" + ((double)Math.round(max*1000))/1000);

        yAxis.setAnimated(false);

        barChart.getData().add(mainSeries);

        barChart.setLegendVisible(false);
        barChart.setTitle("Distance to destination chart");
//        barChart.setLegendVisible(true);



    }
}

