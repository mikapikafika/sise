import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.pow;

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


    /* Tryby nauczania i testowania */

    // Wykolejony ten train trochę hehe
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
                List<Double> inputPattern = data.getInput(patternIndex);
                List<Double> targetOutput = data.getOutput(patternIndex);

                // Propagacja w przód
                List<Double> output = neuralNetwork.feedForward(inputPattern);

                // Obliczenie błędów
                List<Double> errors = calculateErrors(output, targetOutput);

                // Propagacja wsteczna
//                neuralNetwork.backPropagation(inputPattern, errors, targetOutput);
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


    // Obliczanie błędów używane w train()
    private List<Double> calculateErrors(List<Double> output, List<Double> targetOutput) {
        List<Double> errors = new ArrayList<>();

        for (int i = 0; i < output.size(); i++) {
            errors.add(i, pow(targetOutput.get(i) - output.get(i), 2)/2);
        }

        return errors;
    }



    ///////////////

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
                List<Double> inputPattern = data.getTestInput(patternIndex);
                List<Double> targetOutput = data.getTestOutput(patternIndex);

                // Propagacja w przód
                List<Double> output = neuralNetwork.feedForward(inputPattern);

                // Obliczenie błędów
                List<Double> errors = calculateErrors(output, targetOutput);

                // Rejestrowanie wartości do pliku
                bufferedWriter.write("Wzorzec wejściowy: " + inputPattern.toString() + "\n");
                bufferedWriter.write("Błąd popełniony przez sieć dla całego wzorca: " + errors.toString() + "\n");
                bufferedWriter.write("Oczekiwany wzorzec odpowiedzi: " + targetOutput.toString() + "\n");
                bufferedWriter.write("Wartości wyjściowe neuronów: " + output.toString() + "\n");
                bufferedWriter.write("Wagi neuronów wyjściowych: " + neuralNetwork.getOutputWeights() + "\n");
                bufferedWriter.write("Wartości wyjściowe neuronów ukrytych: " + neuralNetwork.getHiddenLayerOutput() + "\n");
                bufferedWriter.write("Wagi neuronów ukrytych: " + Arrays.deepToString(neuralNetwork.getHiddenLayerWeights()) + "\n");
                bufferedWriter.newLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /* Error globalny */

    private double calculateGlobalError() {
        int numPatterns = data.getInputSize();
        double totalError = 0.0;

        for (int i = 0; i < numPatterns; i++) {
            List<Double> inputPattern = data.getInput(i);
            List<Double> targetOutput = data.getOutput(i);
            List<Double> output = neuralNetwork.feedForward(inputPattern);

            for (int j = 0; j < output.size(); j++) {
                double error = targetOutput.get(j) - output.get(j);
                totalError += pow(error, 2);
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
