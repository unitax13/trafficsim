package stuff.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class StatsWindowController {
    @FXML
    private PieChart statsPieChart;
    @FXML
    private Label urbanAreaIndicatorLabel;
    @FXML
    private Label industryAreaIndicatorLabel;
    @FXML
    private Label roadAreaIndicatorLabel;

    public void showStatsWindow(SimulationStats simulationStats) {
        urbanAreaIndicatorLabel.setText("" + simulationStats.numberOfUrbanSegments + " (" + simulationStats.boundSegments + " emp)");
        industryAreaIndicatorLabel.setText("" + simulationStats.numberOfIndustrySegments);
        roadAreaIndicatorLabel.setText("" + simulationStats.numberOfRoad1Segments);

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Urban area", simulationStats.numberOfUrbanSegments),
                new PieChart.Data("Industry area", simulationStats.numberOfIndustrySegments)
        );

        statsPieChart.setData(pieData);
        statsPieChart.setStartAngle(90);
        statsPieChart.setLabelsVisible(false);
    }


}
