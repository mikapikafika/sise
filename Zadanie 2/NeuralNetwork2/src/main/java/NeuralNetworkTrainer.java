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
        StatsMatrix species1 = new StatsMatrix();
        StatsMatrix species2 = new StatsMatrix();
        StatsMatrix species3 = new StatsMatrix();
        species1.confusionMatrix();
        species2.confusionMatrix();
        species3.confusionMatrix();

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
                    species1.incrementBadConfusionMatrix(predictedClass, actualClass);
                } else if (predictedClass == 1) {
                    species2.incrementBadConfusionMatrix(predictedClass, actualClass);
                } else if (predictedClass == 2) {
                    species3.incrementBadConfusionMatrix(predictedClass,actualClass);
                }
            }
        }

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
        finalResult.display();

        //Total population
        int tmp = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tmp += (int)finalResult.data[i][j] ;
            }
        }
        System.out.println("Liczebność: " + tmp);
        //Total population
        tmp = 0;
        for (int i = 0; i < 3; i++) {
            tmp += (int)finalResult.data[i][i] ;
        }
        System.out.println("Łącznie poprawnie sklasyfikowane: " + tmp + "\n");

        System.out.println("Klasa [1.0, 0.0, 0.0]");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                species1.confusionMatrix[i][j] = (int)finalResult.data[i][j];
            }}
        System.out.println("Poprawnie sklasyfikowane: " + (int)finalResult.data[0][0]);
        System.out.println("Błędnie sklasyfikowane: " + ((int)finalResult.data[0][1] + (int)finalResult.data[0][2]));
        double precision1 = species1.calculatePrecision(0);
        double recall1 = species1.calculateRecall(0);
        System.out.println("Precision: " + precision1);
        System.out.println("Recall: " + recall1);
        double fMeasure1 = species1.calculateFMeasure(0);
        System.out.println("F-measure: " + fMeasure1);
        species1.calculateTP(0);
        species1.calculateTN(0);
        species1.calculateFN(0);
        species1.calculateFP(0);

        System.out.println("Klasa [0.0, 1.0, 0.0]");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                 species2.confusionMatrix[i][j] = (int)finalResult.data[i][j];
            }}
        System.out.println("Poprawnie sklasyfikowane: " + (int)finalResult.data[1][1]);
        System.out.println("Błędnie sklasyfikowane: " + ((int)finalResult.data[1][0] + (int)finalResult.data[1][2]));
        double precision2 = species2.calculatePrecision(1);
        double recall2 = species2.calculateRecall(1);
        System.out.println("Precision: " + precision2);
        System.out.println("Recall: " + recall2);
        double fMeasure2 = species2.calculateFMeasure(1);
        System.out.println("F-measure: " + fMeasure2);
        species2.calculateTP(1);
        species2.calculateTN(1);
        species2.calculateFN(1);
        species2.calculateFP(1);

        System.out.println("Klasa [0.0, 0.0, 1.0]");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                species3.confusionMatrix[i][j] = (int)finalResult.data[i][j];
            }}
        System.out.println("Poprawnie sklasyfikowane: " + (int)finalResult.data[2][2]);
        System.out.println("Błędnie sklasyfikowane: " + ((int)finalResult.data[2][0] + (int)finalResult.data[2][1]));
        double precision3 = species3.calculatePrecision(2);
        double recall3 = species3.calculateRecall(2);
        System.out.println("Precision: " + precision3);
        System.out.println("Recall: " + recall3);
        double fMeasure3 = species3.calculateFMeasure(2);
        System.out.println("F-measure: " + fMeasure3);
        species3.calculateTP(2);
        species3.calculateTN(2);
        species3.calculateFN(2);
        species3.calculateFP(2);
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

