import javax.swing.*;
import javax.xml.crypto.Data;
import java.util.ArrayList;
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
    //    for (int ep = 0; ep < epochs; ep++) {
      //      epochsNum.add(ep+1);
            if (isRandom) {
                Collections.shuffle(patternOrder);
            }
            for (int i = 0; i < numTrainingPatterns; i++) {
                int patternIndex = patternOrder.get(i);
                double[][] sample = reader.getTrainingData().get(patternIndex);
                    network.train(sample[0], sample[1]);

            }
//            calculatedNetworkErrors.add(network.getCurrentNeuralNetworkError() / reader.getTrainingData());
//            network.currentNeuralNetworkError = 0.0;
        }

        //drawErrorChart(calculatedNetworkErrors, epochsNum);

       // }

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

