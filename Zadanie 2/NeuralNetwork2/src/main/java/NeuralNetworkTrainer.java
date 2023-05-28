import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NeuralNetworkTrainer {
    NeuralNetwork network;
    DataReader reader;

    public NeuralNetworkTrainer(NeuralNetwork network, DataReader reader) {
        this.network = network;
        this.reader = reader;
    }

    public void train(int epochs, boolean isMomentum, boolean isBias, boolean isRandom) {
        ArrayList<Double> calculatedNetworkErrors = new ArrayList<>();
        ArrayList<Integer> epochsNum = new ArrayList<>();
        int numTrainingPatterns = reader.getTrainingDataSize();
        List<Integer> patternOrder = new ArrayList<>();
        for (int i = 0; i < numTrainingPatterns; i++) {
            patternOrder.add(i);
        }
        for (int ep = 0; ep < epochs; ep++) {
            epochsNum.add(ep+1);
            if (isRandom) {
                Collections.shuffle(patternOrder);
            }
            for (int i = 0; i < numTrainingPatterns; i++) {
                int patternIndex = patternOrder.get(i);
                double[][] sample = reader.getTrainingData().get(patternIndex);
                network.train(sample[0], sample[1]);
            }

            calculatedNetworkErrors.add(network.getNeuralNetworkError() / reader.getTrainingData().size());
            network.neuralNetworkError = 0.0;
        }

        drawErrorChart(calculatedNetworkErrors, epochsNum);

    }

    public void test() {
        double[] stats = new double[3];
        StatsMatrix species1 = new StatsMatrix(1);
        StatsMatrix species2 = new StatsMatrix(2);
        StatsMatrix species3 = new StatsMatrix(3);

        for (int i = 0; i < reader.getTestingDataSize(); i++) {
            Double[] test = network.test(reader.getTestingData().get(i));
            System.out.println(Arrays.toString(test));

            // Rezultat testu
            int testResult = Arrays.asList(test).indexOf(Collections.max(Arrays.asList(test)));

            // To, co powinno być rezultatem testu
            int actualResult = (int) reader.getTestingData().get(i)[4];

            // hipoteza 0 lub 1 lub 2
            species1.confusionMatrixValues(testResult, actualResult);
            species2.confusionMatrixValues(testResult, actualResult);
            species3.confusionMatrixValues(testResult, actualResult);

            stats[testResult]++;
        }

        System.out.println(Arrays.toString(stats));

        species1.displayResults();
        species2.displayResults();
        species3.displayResults();
    }


    private void drawErrorChart(ArrayList<Double> calculatedNetworkErrors, ArrayList<Integer> epochsNum) {
        SwingUtilities.invokeLater(() -> {
            ChartDrawer errorChart = new ChartDrawer("Średni błąd kwadratowy epok", epochsNum, calculatedNetworkErrors);
            errorChart.setAlwaysOnTop(true);
            errorChart.pack();
            errorChart.setSize(600, 400);
            errorChart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            errorChart.setVisible(true);
        });
    }
}

