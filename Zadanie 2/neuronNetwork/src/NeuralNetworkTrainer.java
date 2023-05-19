import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NeuralNetworkTrainer {
    private static final double LEARNING_RATE = 0.1;
    private static final double MOMENTUM_FACTOR = 0.9;
    private static final int MAX_EPOCHS = 100;

    private DataReader data;
    private NeuralNetwork neuralNetwork; // Obiekt sieci neuronowej

    public NeuralNetworkTrainer(DataReader data, NeuralNetwork neuralNetwork) {
        this.data = data;
        this.neuralNetwork = neuralNetwork;
    }

    public void train(boolean randomOrder, boolean useMomentum) {
        int numTrainingPatterns = data.getInputSize();
        List<Integer> patternOrder = new ArrayList<>();

        for (int i = 0; i < numTrainingPatterns; i++) {
            patternOrder.add(i);
        }

        Random random = new Random();

        for (int epoch = 1; epoch <= MAX_EPOCHS; epoch++) {
            if (randomOrder) {
                Collections.shuffle(patternOrder, random);
            }

            for (int i = 0; i < numTrainingPatterns; i++) {
                int patternIndex = patternOrder.get(i);
                double[] inputPattern = data.getInput(patternIndex);
                double[] targetOutput = data.getOutput(patternIndex);

                // Propagacja w przód
                double[] output = neuralNetwork.feedForward(inputPattern);

                // Obliczenie błędów
                double[] errors = calculateErrors(output, targetOutput);

                // Propagacja wsteczna
                neuralNetwork.backpropagation(errors, useMomentum);

                // Aktualizacja wag
                neuralNetwork.updateWeights(LEARNING_RATE);
            }
        }
    }

    private double[] calculateErrors(double[] output, double[] targetOutput) {
        double[] errors = new double[output.length];

        for (int i = 0; i < output.length; i++) {
            errors[i] = targetOutput[i] - output[i];
        }

        return errors;
    }

}
