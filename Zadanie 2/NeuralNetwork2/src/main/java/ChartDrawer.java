import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartDrawer extends JFrame {
    public ChartDrawer(String title,ArrayList<Integer> x, ArrayList<Double> y) throws HeadlessException {
        super(title);
        XYSeries series = addData(x, y);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Trenowanie",
                "Epoki",
                "Błąd",
                dataset
        );
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private XYSeries addData(ArrayList<Integer> xData, ArrayList<Double> yData) {
        XYSeries series = new XYSeries("Średni błąd epoki");

        // Sprawdzenie czy listy mają taką samą długość
        if (xData.size() != yData.size()) {
            throw new IllegalArgumentException("Listy muszą mieć taką samą długość");
        }

        // Dodawanie punktów do serii danych
        for (int i = 0; i < xData.size(); i++) {
            int x = xData.get(i);
            double y = yData.get(i);
            series.add(x, y);
        }

        return series;
    }


}
