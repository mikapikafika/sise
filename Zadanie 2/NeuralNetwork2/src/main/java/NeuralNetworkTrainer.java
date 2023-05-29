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

    public void train(int epochs, double momentum, boolean isBias, boolean isRandom) {
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
                network.train(sample[0], sample[1], momentum, isBias);
            }

            calculatedNetworkErrors.add(network.getNeuralNetworkError() / reader.getTrainingData().size());
            if ((ep+1) % 20 == 0) {
                saveErrorToFile(network.getNeuralNetworkError() / reader.getTrainingData().size(), ep+1);
            }
            network.neuralNetworkError = 0.0;
        }

        drawErrorChart(calculatedNetworkErrors, epochsNum);

    }

    public void test() {
        double[] stats = new double[3];
        double epsilon = 0.1;
        StatsMatrix species1 = new StatsMatrix(new double[]{1.0, 0.0, 0.0});
        StatsMatrix species2 = new StatsMatrix(new double[]{0.0, 1.0, 0.0});
        StatsMatrix species3 = new StatsMatrix(new double[]{0.0, 0.0, 1.0});
        int spec1count = 0;
        int spec2count = 0;
        int spec3count = 0;
        int tmp1 = 0;
        int tmp2 = 0;
        int tmp3 = 0;

        List<double[]> input = reader.getTestingInput();
        List<double[]> desired = reader.getTestingResult();
        for (int i = 0; i < reader.getTestingDataSize(); i++) {
            double[] test = network.test(input.get(i),desired.get(i),"test_result.txt");

            // Rezultat testu
            double[] testResult = test;

            if (Math.abs(testResult[0] - 1.0) < epsilon) {
                tmp1++;
            }

            if (Math.abs(testResult[1] - 1.0) < epsilon) {
                tmp2++;
            }

            if (Math.abs(testResult[2] - 1.0) < epsilon) {
                tmp3++;
            }


//            // To, co powinno być rezultatem testu
//            int actualResult = (int) desired.get(i);
            double[] actualResult = desired.get(i);
            double[] bigTestResult = new double[]{tmp1, tmp2, tmp3};
            
            species1.confusionMatrixValues(bigTestResult, actualResult);
            species2.confusionMatrixValues(bigTestResult, actualResult);
            species3.confusionMatrixValues(bigTestResult, actualResult);

            tmp1 = 0;
            tmp2 = 0;
            tmp3 = 0;
        }


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

    private void saveErrorToFile(double error, int epoch) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("error_log.txt", true))) {
            writer.write("Epoch: " + epoch + ", Error: " + error);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

