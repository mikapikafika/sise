import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork implements Serializable {
    private Neuron[][] hiddenLayers;
    private Neuron[] outputLayer;

    //Kiedy tworzymy sieć, hiddenSizes musimy podać jako, np. {4, 2}, czyli pierwsza warstwa ukryta składa się z 4, a druga z 2 neuronów
    public NeuralNetwork(int inputSize, int[] hiddenSizes, int outputSize) {
        hiddenLayers = new Neuron[hiddenSizes.length][];
        outputLayer = new Neuron[outputSize];

        // Inicjalizacja warstw ukrytych
        for (int i = 0; i < hiddenSizes.length; i++) {
            int currentSize = hiddenSizes[i];
            hiddenLayers[i] = new Neuron[currentSize];

            // Inicjalizacja neuronów w warstwie ukrytej
            for (int j = 0; j < currentSize; j++) {
                //Jeżeli to pierwsza iteracja, rozmiar wejścia neuronu to inputSize. W przeciwnym wypadku hiddenSizes[i - 1]
                int prevLayerSize = (i == 0) ? inputSize : hiddenSizes[i - 1];
                hiddenLayers[i][j] = new Neuron(prevLayerSize);
            }
        }

        // Inicjalizacja warstwy wyjściowej
        for (int i = 0; i < outputSize; i++) {
            //Ustawiamy rozmiar wejść neuronów wyjściowych na rozmiar outputu ostatniej warstwy ukrytej, ale najpierw sprawdzamy, czy taka w ogóle istnieje
            int prevLayerSize = (hiddenSizes.length == 0) ? inputSize : hiddenSizes[hiddenSizes.length - 1];
            outputLayer[i] = new Neuron(prevLayerSize);
        }
    }

    public List<Double> predict(List<Double> input) {
        List<Double> output = input;

        // Obliczanie wyjścia dla warstw ukrytych
        for (Neuron[] hiddenLayer : hiddenLayers) {
            output = calculateLayerOutput(output, hiddenLayer);

            // Obliczanie wyjścia dla warstwy wyjściowej
            output = calculateLayerOutput(output, outputLayer);
        }
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

            for (int j = 0; j < hiddenLayers[i].length; j++) {
                Neuron neuron = hiddenLayers[i][j];
                double neuronOutput = neuron.activate(output);
                layerOutput.add(j, neuronOutput);
            }
            output = layerOutput;
        }

        // Dla warstwy wyjściowej
        List<Double> finalOutput = new ArrayList<>();
        for (int i = 0; i < outputLayer.length; i++) {
            Neuron neuron = outputLayer[i];
            double neuronOutput = neuron.activate(output);
            finalOutput.add(i, neuronOutput);
        }

        return finalOutput;
    }


    public void backPropagation(List<Double> errors) {
        // Dla warstwy wyjściowej
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayer[i].calculateError(errors.get(i));
        }


        // Dla warstw ukrytych
        for (int i = hiddenLayers.length - 1; i >= 0; i--) {
            Neuron[] hiddenLayer = hiddenLayers[i];
            List<Double> nextLayerErrors = new ArrayList<>();

            if (i + 1 < hiddenLayers.length) {
                List<Neuron> nextLayerNeurons = List.of(hiddenLayers[i + 1]);
                nextLayerErrors.clear();

                for (int j = 0; j < hiddenLayer.length; j++) {
                    double errorSum = 0.0;

                    for (Neuron nextLayerNeuron : nextLayerNeurons) {
                        errorSum += nextLayerNeuron.getWeightAtIndex(j) * nextLayerNeuron.getError();
                    }

                    nextLayerErrors.add(errorSum);
                    hiddenLayer[j].calculateError(nextLayerErrors.get(j)); // Retrieve error from nextLayerErrors
                }
            } else {
                for (int j = 0; j < hiddenLayer.length; j++) {
                    double errorSum = 0.0;
                    hiddenLayer[j].calculateError(errorSum);
                }
            }
        }
    }

    // Nie wiem czy nie trzeba uwzględnić gradientu licząc wagę z momentum dlatego skip implementacji w klasie Neuron - momentum jest stałe więc chyba ni

    public void updateWeightsWithMomentum(double learningRate, double momentum) {

        // Dla warstw ukrytych
        for (int i = hiddenLayers.length - 1; i >= 0; i--) {
            Neuron[] currentLayer = hiddenLayers[i];
            for (Neuron neuron : currentLayer) {
                neuron.updateWeightsWithMomentum(learningRate, momentum);
            }
        }

        // Dla warstwy wyjściowej
        for (Neuron neuron : outputLayer) {
            neuron.updateWeightsWithMomentum(learningRate, momentum);
        }
    }

    public void updateWeights(double learningRate) {

        // Dla warstw ukrytych
        for (int i = hiddenLayers.length - 1; i >= 0; i--) {
            Neuron[] currentLayer = hiddenLayers[i];
            for (Neuron neuron : currentLayer) {
                neuron.updateWeights(learningRate);
            }
        }

        // Dla warstwy wyjściowej
        for (Neuron neuron : outputLayer) {
            neuron.updateWeights(learningRate);
        }
    }

    public String getOutputWeights() {
        List<List<Double>> outputWeights = new ArrayList<>();

        for (int i = 0; i < outputLayer.length; i++) {
            outputWeights.add(i, outputLayer[i].getWeights());
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
            Neuron[] hiddenLayer = hiddenLayers[i];
            hiddenLayerWeights[i] = new double[hiddenLayer.length];

            for (int j = 0; j < hiddenLayer.length; j++) {
                hiddenLayerWeights[i][j] = hiddenLayer[j].getWeightAtIndex(j);
            }
        }

        return hiddenLayerWeights;
    }

    public String getHiddenLayerOutput() {
        List<List<Double>> hiddenLayerOutput = new ArrayList<>();

        for (int i = 0; i < hiddenLayers.length; i++) {
            Neuron[] hiddenLayer = hiddenLayers[i];
            List<Double> layer = new ArrayList<>();
            for(int j = 0 ; j <hiddenLayer.length; j++)
            {
                layer.add(j, hiddenLayer[j].getOutput());
            }
            hiddenLayerOutput.add(i, layer);
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
        return outputLayer.length;
    }
}
