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
    double epsilon = 0.01;

    public NeuralNetworkTrainer(NeuralNetwork network, DataReader reader) {
        this.network = network;
        this.reader = reader;
    }

    public void train(int epochs, double momentum, Boolean isBias, Boolean isRandom) {
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
            if(network.getNeuralNetworkError() < epsilon)
            {
                System.out.println("Dokładność 0.01 osiągnięta po: "+ epochsNum.get(epochsNum.size()-1)+ " epokach");
                break;
            }
            network.neuralNetworkError = 0.0;
        }

        drawErrorChart(calculatedNetworkErrors, epochsNum);

    }

    public void testEncoder() {
        for (int i = 0; i < reader.getTestingDataSize(); i++) {
            List<double[]> input = reader.getTestingInput();
            List<double[]> desired = reader.getTestingResult();
            double[] test = network.test(input.get(i), desired.get(i), "test_encoder_result.txt");
        }
    }

    public void test() {
        StatsMatrix species1 = new StatsMatrix(new double[]{1.0, 0.0, 0.0});
        StatsMatrix species2 = new StatsMatrix(new double[]{0.0, 1.0, 0.0});
        StatsMatrix species3 = new StatsMatrix(new double[]{0.0, 0.0, 1.0});
        species1.confusionMatrix();
        species2.confusionMatrix();
        species3.confusionMatrix();
        int tmp1 = 0;
        int tmp2 = 0;
        int tmp3 = 0;

        List<double[]> input = reader.getTestingInput();
        List<double[]> desired = reader.getTestingResult();
        for (int i = 0; i < reader.getTestingDataSize(); i++) {
            double[] test = network.test(input.get(i),desired.get(i),"test_result.txt");

            // Rezultat testu
            double[] testResult = test;
            int predictedClass = getMaxIndex(testResult);
            int actualClass = getMaxIndex(desired.get(i));

            if(predictedClass == actualClass)
            {
                if (predictedClass == 0) {
                    species1.incrementConfusionMatrix(actualClass);
                } else if (predictedClass == 1) {
                    species2.incrementConfusionMatrix(actualClass);
                } else if (predictedClass == 2) {
                    species3.incrementConfusionMatrix(actualClass);
                }
            }
            else
            {
                if (predictedClass == 0) {
                    species1.incrementBadConfusionMatrix(actualClass,predictedClass);
                } else if (predictedClass == 1) {
                    species2.incrementBadConfusionMatrix(actualClass,predictedClass);
                } else if (predictedClass == 2) {
                    species3.incrementBadConfusionMatrix(actualClass,predictedClass);
                }
            }
            // Aktualizacja odpowiedniej macierzy pomyłek

            if (actualClass == predictedClass && predictedClass == 0) {
                tmp1++;
            }

            if (actualClass == predictedClass && predictedClass == 1) {
                tmp2++;
            }

            if (actualClass == predictedClass && predictedClass == 2) {
                tmp3++;
            }


//            // To, co powinno być rezultatem testu
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

        Matrix finalResult = new Matrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                finalResult.data[i][j] += species1.confusionMatrix[i][j];
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                finalResult.data[i][j] += species2.confusionMatrix[i][j];
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                finalResult.data[i][j] += species3.confusionMatrix[i][j];
            }
        }

//        species1.displayConfusionMatrix();
//        species2.displayConfusionMatrix();
//        species3.displayConfusionMatrix();
        finalResult.display();

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
            writer.write("Epoka: " + epoch + ", Błąd: " + error);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getMaxIndex(double[] array) {
        int maxIndex = 0;
        double maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}

