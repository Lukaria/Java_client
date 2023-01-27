package controllers.Usability;

import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public interface Methods {
    default void errorText(Text errortext, String text, String style) {
        errortext.setVisible(true);
        errortext.setText(text);
        errortext.setStyle(style);
    }


    default void buttonChoosed(Button button) {
        button.setStyle("-fx-background-color:#262885; -fx-border-color:white");
    }

    default void setChartLineStyle(XYChart.Series<Integer, Float> series, Color clr) {
        Color color = clr;
        String rgb = String.format("%d, %d, %d", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
        series.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");

        for (XYChart.Data<Integer, Float> data : series.getData())
            data.getNode().lookup(".chart-line-symbol").setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
    }

    default void hideChart(Chart chart, Text text, boolean activator){
        if(activator){
            chart.setEffect(new GaussianBlur());
            text.setVisible(true);
        }
        else{
            chart.setEffect(null);
            text.setVisible(false);
        }

    }

    default void loadAndShowScene(){}
}
