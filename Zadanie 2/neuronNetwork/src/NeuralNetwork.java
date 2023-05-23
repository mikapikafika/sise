import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NeuralNetwork implements Serializable {
    private Neuron[][] hiddenLayers;
    private Neuron[] outputLayer;

    private double[] outputLayerGradient;
    private double[] hiddenLayerGradient;

    /* Inicjalizacja warstw */

    //Kiedy tworzymy sieć, hiddenSizes musimy podać jako, np. {4, 2}, czyli pierwsza warstwa ukryta składa się z 4, a druga z 2 neuronów
    public NeuralNetwork(int inputSize, int[] hiddenSizes, int outputSize) {
        hiddenLayers = new Neuron[hiddenSizes.length][];
        outputLayer = new Neuron[outputSize];
        outputLayerGradient = new double[outputSize];
        hiddenLayerGradient = new double[hiddenSizes.length];

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


    /* Główne metody */

    public List<Double> feedForward(List<Double> inputPattern) {
        // Dla każdego neuronu w warstwie, oblicz sumę ważoną poprzez pomnożenie wartości wejściowych przez odpowiadające im wagi i zsumowanie tych iloczynów
        // Następnie dodaj bias (przesunięcie) do obliczonej sumy ważonej i zastosuj funkcję aktywacji (wszystko dzieje się w activate)
        List<Double> output = inputPattern;

        // WARSTWY UKRYTE
        for (int i = 0; i < hiddenLayers.length; i++) {
            List<Double> layerOutput = new ArrayList<>();

            for (int j = 0; j < hiddenLayers[i].length; j++) {
                Neuron neuron = hiddenLayers[i][j];
                double neuronOutput = neuron.activate(output);
                layerOutput .add(j, neuronOutput);
            }
            output = layerOutput;
        }

        // WARSTWA WYJŚCIOWA
        List<Double> finalOutput = new ArrayList<>();
        for (int i = 0; i < outputLayer.length; i++) {
            Neuron neuron = outputLayer[i];
            double neuronOutput = neuron.activate(output);
            finalOutput.add(i, neuronOutput);
        }

//        System.out.println("rezultat propagacji w przód: " + finalOutput);

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

    /* PRÓBA INACZEJ: */

    // JAK WYKORZYSTAC ERRORS - nie działa ofc bo trzeba dla ukrytych ogarnąć i przetestować
//    public void backPropagation(List<Double> inputPattern, List<Double> errors, List<Double> targetOutput) {
//
//        // WARSTWA WYJŚCIOWA
//        /* Dla każdego neuronu w warstwie wyjściowej o indeksie i obliczamy błąd wyjścia: delta_i = (y_i - t_i) * f'(z_i),
//        gdzie y_i to wartość wyjściowa neuronu, t_i to oczekiwana wartość wyjściowa, f'() to pochodna funkcji aktywacji,
//        a z_i to ważona suma sygnałów dla tego neuronu.
//         */
//        for (int i = 0; i < outputLayer.length; i++) {
//            // tyle zmiennych dla czytelności
//            Neuron neuron = outputLayer[i];
//            double weightedSignalSum = neuron.getWeightedSignalSum(inputPattern);                           // z_i
//            double derivative = neuron.derivativeActivationFunction(weightedSignalSum);                     // f'(z_i)
//            outputLayerGradient[i] = (calculateLayerOutput(inputPattern, outputLayer).get(i) - targetOutput.get(i)) * derivative;         // delta_i = (y_i - t_i) * f'(z_i), hubert ma jeszcze coś
//        }
//        System.out.println("Błąd wyjścia: " + Arrays.toString(outputLayerGradient));
//
//
//        // WARSTWY UKRYTE
//        /* Dla każdej warstwy ukrytej (zaczynając od ostatniej) obliczamy błąd dla każdego neuronu:
//        Dla neuronu o indeksie j w warstwie ukrytej o indeksie l obliczamy błąd: delta_j = f'(z_j) * sum(w_ji * delta_i),
//        gdzie f'() to pochodna funkcji aktywacji, z_j to ważona suma sygnałów dla tego neuronu, w_ji to waga połączenia
//        między neuronem j a neuronem i w kolejnej warstwie, a delta_i to błąd dla neuronu i w kolejnej warstwie.
//         */
//        for (int i = hiddenLayers.length - 1; i >= 0; i--) {
//            Neuron[] hiddenNeurons = hiddenLayers[i];
//            Neuron[] nextHiddenNeurons = hiddenLayers[i + 1];
//
//            if (i + 1 < hiddenLayers.length) {
//                for (int j = 0; j < hiddenNeurons.length; j++) {
//                    Neuron neuron = hiddenNeurons[j];
//                    double weightedSignalSum = neuron.getWeightedSignalSum(inputPattern);                   // z_j
//                    double derivative = neuron.derivativeActivationFunction(weightedSignalSum);             // f'(z_j)
//                    hiddenLayerGradient[i] = (calculateLayerOutput(inputPattern, hiddenNeurons).get(i) - targetOutput.get(i));   // i coś tam dalej
//
//                }
//            } else {
//                for (int j = 0; j < hiddenLayers.length; j++) {
//                    // jest to ostatnia ukryta warstwa i liczymy inaczej... i guess? nie dotarlam
//                }
//            }
//        }
//    }



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







    /* Obsługa plików */

    public void saveToFile(String filePath) {
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






    ////////////////////////////













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



    /* Metody do statystyk, na razie zostawiam */

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
