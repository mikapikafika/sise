import java.io.*;
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

    public double[] predict(double[] input) {
        double[] output = input;

        // Obliczanie wyjścia dla warstw ukrytych
        for (Neuron[] hiddenLayer : hiddenLayers) {
            output = calculateLayerOutput(output, hiddenLayer);
        }

        // Obliczanie wyjścia dla warstwy wyjściowej
        output = calculateLayerOutput(output, outputLayer);

        return output;
    }

    private double[] calculateLayerOutput(double[] input, Neuron[] layer) {
        double[] output = new double[layer.length];
        for (int i = 0; i < layer.length; i++) {
            output[i] = layer[i].activate(input);
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
}
