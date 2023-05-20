import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork implements Serializable {
    private Layer[] hiddenLayers;
    private Layer outputLayer;

    //Kiedy tworzymy sieć, hiddenSizes musimy podać jako, np. {4, 2}, czyli pierwsza warstwa ukryta składa się z 4, a druga z 2 neuronów
    public NeuralNetwork(int inputSize, int[] hiddenSizes, int outputSize) {
        hiddenLayers = new Layer[hiddenSizes.length];
        outputLayer = new Layer(outputSize, hiddenSizes[hiddenSizes.length - 1]);

        // Inicjalizacja warstw ukrytych
        for (int i = 0; i < hiddenSizes.length; i++) {
            int currentSize = hiddenSizes[i];
            int prevSize = (i == 0) ? inputSize : hiddenSizes[i - 1];
            hiddenLayers[i] = new Layer(currentSize, prevSize);
        }
    }

    public List<Double> predict(List<Double> input) {
        List<Double> output = input;

        // Propagacja sygnału przez warstwy ukryte
        for (Layer hiddenLayer : hiddenLayers) {
            output = hiddenLayer.propagate(output);
        }

        // Propagacja sygnału przez warstwę wyjściową
        output = outputLayer.propagate(output);

        return output;
    }

    private List<Double> calculateLayerOutput(List<Double> input, Neuron[] layer) {
        List<Double> output = new ArrayList<>();
        for (int i = 0; i < layer.length; i++) {
            output.set(i, layer[i].activate(input));
        }
        return output;
    }

    public void saveToFile(String filePath)
    {
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            fileOut.close();
            System.out.println("Sieć neuronowa została zapisana do pliku.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NeuralNetwork loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filePath);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        NeuralNetwork network = (NeuralNetwork) objectIn.readObject();
        objectIn.close();
        fileIn.close();
        System.out.println("Sieć neuronowa została wczytana z pliku.");
        return network;
    }

    public List<Double> feedForward(List<Double> inputPattern) {
        List<Double> output = inputPattern;

        // Dla warstw ukrytych
        for (int i = 0; i < hiddenLayers.length; i++) {
            List<Double> layerOutput = new ArrayList<>();

            for (int j = 0; j < hiddenLayers[i].getSize(); j++) {
                Neuron neuron = hiddenLayers[i].getNeuron(j);
                double neuronOutput = neuron.activate(output);
                layerOutput.add(j, neuronOutput);
            }
            output = layerOutput;
        }

        // Dla warstwy wyjściowej
        List<Double> finalOutput = new ArrayList<>();
        for (int i = 0; i < outputLayer.getSize(); i++) {
            Neuron neuron = outputLayer.getNeuron(i);
            double neuronOutput = neuron.activate(output);
            finalOutput.add(i, neuronOutput);
        }

        return finalOutput;
    }

    // trzeba wykorzystac jeszcze useMomentum
    public void backPropagation(List<Double> errors) {
        // Obliczanie błędów dla warstwy wyjściowej
        for (int i = 0; i < outputLayer.getSize(); i++) {
            outputLayer.getNeuron(i).calculateError(errors.get(i));
        }

        // Obliczanie błędów dla warstw ukrytych
        for (int i = hiddenLayers.length - 1; i >= 0; i--) {
            Layer hiddenLayer = hiddenLayers[i];

            // Sprawdź, czy istnieje następna warstwa ukryta
            if (i + 1 < hiddenLayers.length) {
                List<Neuron> nextLayerNeurons = hiddenLayers[i + 1].getNeurons();
                List<Double> nextLayerErrors = new ArrayList<>();

                for (int j = 0; j < hiddenLayer.getSize(); j++) {
                    double errorSum = 0.0;

                    // Sumowanie wag neuronów w następnej warstwie ukrytej wchodzących do neuronu i przemnożonych przez gradient neuronu w następnej warstwie ukrytej
                    for (Neuron nextLayerNeuron : nextLayerNeurons) {
                        errorSum += nextLayerNeuron.getWeightAtIndex(j) * nextLayerNeuron.getError();
                    }

                    nextLayerErrors.add(j, errorSum);
                    hiddenLayer.getNeuron(j).calculateError(errorSum);
                }

                errors = nextLayerErrors;
            } else {
                // Jeśli nie ma następnej warstwy ukrytej, oblicz błędy bez uwzględniania neuronów z następnej warstwy
                for (int j = 0; j < hiddenLayer.getSize(); j++) {
                    double errorSum = 0.0;
                    hiddenLayer.getNeuron(j).calculateError(errorSum);
                }
            }
        }
    }

    // Nie wiem czy nie trzeba uwzględnić gradientu licząc wagę z momentum dlatego skip implementacji w klasie Neuron - momentum jest stałe więc chyba ni

    public void updateWeightsWithMomentum(double learningRate, double momentum) {

        // Dla warstw ukrytych
        for (int i = hiddenLayers.length - 1; i >= 0; i--) {
            Layer currentLayer = hiddenLayers[i];
            for (Neuron neuron : currentLayer.getNeurons()) {
                neuron.updateWeightsWithMomentum(learningRate, momentum);
            }
        }

        // Dla warstwy wyjściowej
        for (Neuron neuron : outputLayer.getNeurons()) {
            neuron.updateWeightsWithMomentum(learningRate, momentum);
        }
    }

    public void updateWeights(double learningRate) {

        // Dla warstw ukrytych
        for (int i = hiddenLayers.length - 1; i >= 0; i--) {
            Layer currentLayer = hiddenLayers[i];
            for (Neuron neuron : currentLayer.getNeurons()) {
                neuron.updateWeights(learningRate);
            }
        }

        // Dla warstwy wyjściowej
        for (Neuron neuron : outputLayer.getNeurons()) {
            neuron.updateWeights(learningRate);
        }
    }

    public String getOutputWeights() {
        List<List<Double>> outputWeights = new ArrayList<>();

        for (int i = 0; i < outputLayer.getSize(); i++) {
            outputWeights.add(i, outputLayer.getNeuron(i).getWeights());
        }
        StringBuilder sb = new StringBuilder();
        for (List<Double> weights : outputWeights) {
            sb.append(weights.toString()).append(", ");
        }
        String output = sb.toString();
        if (!output.isEmpty()) {
            output = output.substring(0, output.length() - 2); // Usuń ostatni przecinek i spację
        }
        return output;
    }

    public double[][] getHiddenLayerWeights() {
        double[][] hiddenLayerWeights = new double[hiddenLayers.length][];

        for (int i = 0; i < hiddenLayers.length; i++) {
            Layer hiddenLayer = hiddenLayers[i];
            hiddenLayerWeights[i] = new double[hiddenLayer.getSize()];

            for (int j = 0; j < hiddenLayer.getSize(); j++) {
                hiddenLayerWeights[i][j] = hiddenLayer.getNeuron(j).getWeightAtIndex(j);
            }
        }

        return hiddenLayerWeights;
    }

    public String getHiddenLayerOutput() {
        List<List<Double>> hiddenLayerOutput = new ArrayList<>();

        for (int i = 0; i < hiddenLayers.length; i++) {
            Layer hiddenLayer = hiddenLayers[i];
            hiddenLayerOutput.add(i, hiddenLayer.getOutputs());
        }
        StringBuilder sb = new StringBuilder();
        for (List<Double> weights : hiddenLayerOutput) {
            sb.append(weights.toString()).append(", ");
        }
        String output = sb.toString();
        if (!output.isEmpty()) {
            output = output.substring(0, output.length() - 2); // Usuń ostatni przecinek i spację
        }
        return output;
    }

    public int getOutputSize() {
        return outputLayer.getSize();
    }
}
