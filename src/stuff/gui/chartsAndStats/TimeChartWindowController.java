package stuff.gui.chartsAndStats;

import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.TreeMap;

public class TimeChartWindowController {
    @FXML
    private  AnchorPane chartsWindowPane;
    @FXML
    private  Label averageText;
    @FXML
    private  Label medianText;
    @FXML
    private  Label minText;
    @FXML
    private  Label maxText;
    @FXML
    private ScatterChart scatterChart;
    @FXML
    private Axis<Number> xAxis;
    @FXML
    private Axis<Number> yAxis;


    public void showBarChartTime(ArrayList<TreeMap<Double, Integer>> timeChartData) {


        yAxis.setLabel("Amount of citizens");
        xAxis.setLabel("Time it took to get to destination");

        //XYChart.Series<String, Number> series = new XYChart.Series<>();


        for (int i=0; i< timeChartData.size(); i++) {

            XYChart.Series<Number, Number> series = new XYChart.Series<>();

            TreeMap<Double, Integer> data = timeChartData.get(i);

            data.forEach((k, v) -> series.getData().add(new XYChart.Data<>(k, v)));

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

            scatterChart.getData().add(series);
        }
        xAxis.setAutoRanging(true);
        scatterChart.setLegendVisible(false);
        scatterChart.setTitle("Time to destination of citizens chart");


//        scatterChart.setLegendVisible(true);



    }
}
