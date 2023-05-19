import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
                neuralNetwork.backPropagation(errors);

                // Aktualizacja wag
                if (useMomentum) {
                    neuralNetwork.updateWeightsWithMomentum(LEARNING_RATE, MOMENTUM_FACTOR);
                } else {
                    neuralNetwork.updateWeights(LEARNING_RATE);
                }
            }

            // Zapisanie wartości globalnego błędu sieci po zakończeniu epoki
            if (epoch % 10 == 0) {
                double globalError = calculateGlobalError();
                saveGlobalError(epoch, globalError);
            }
        }
    }

    public void test() {
        int numTestingPatterns = data.getTestSize();
        List<Integer> patternOrder = new ArrayList<>();

        for (int i = 0; i < numTestingPatterns; i++) {
            patternOrder.add(i);
        }

        try (FileWriter fileWriter = new FileWriter("wyniki_testu.txt");
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);) {

            for (int i = 0; i < numTestingPatterns; i++) {
                int patternIndex = patternOrder.get(i);
                double[] inputPattern = data.getTestInput(patternIndex);
                double[] targetOutput = data.getTestOutput(patternIndex);

                // Propagacja w przód
                double[] output = neuralNetwork.feedForward(inputPattern);

                // Obliczenie błędów
                double[] errors = calculateErrors(output, targetOutput);

                // Rejestrowanie wartości do pliku
                bufferedWriter.write("Wzorzec wejściowy: " + Arrays.toString(inputPattern) + "\n");
                bufferedWriter.write("Błąd popełniony przez sieć dla całego wzorca: " + Arrays.toString(errors) + "\n");
                bufferedWriter.write("Oczekiwany wzorzec odpowiedzi: " + Arrays.toString(targetOutput) + "\n");
                bufferedWriter.write("Wartości wyjściowe neuronów: " + Arrays.toString(output) + "\n");
                bufferedWriter.write("Wagi neuronów wyjściowych: " + Arrays.deepToString(neuralNetwork.getOutputWeights()) + "\n");
                bufferedWriter.write("Wartości wyjściowe neuronów ukrytych: " + Arrays.toString(neuralNetwork.getHiddenLayerOutput()) + "\n");
                bufferedWriter.write("Wagi neuronów ukrytych: " + Arrays.deepToString(neuralNetwork.getHiddenLayerWeights()) + "\n");
                bufferedWriter.newLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private double[] calculateErrors(double[] output, double[] targetOutput) {
        double[] errors = new double[output.length];

        for (int i = 0; i < output.length; i++) {
            errors[i] = targetOutput[i] - output[i];
        }

        return errors;
    }

    private double calculateGlobalError() {
        int numPatterns = data.getInputSize();
        double totalError = 0.0;

        for (int i = 0; i < numPatterns; i++) {
            double[] inputPattern = data.getInput(i);
            double[] targetOutput = data.getOutput(i);
            double[] output = neuralNetwork.feedForward(inputPattern);

            for (int j = 0; j < output.length; j++) {
                double error = targetOutput[j] - output[j];
                totalError += Math.pow(error, 2);
            }
        }

        return totalError / (numPatterns * neuralNetwork.getOutputSize());
    }

    private void saveGlobalError(int epoch, double globalError) {
        try {
            FileWriter fileWriter = new FileWriter("global_error.txt", true);
            fileWriter.write(epoch + "," + globalError + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
