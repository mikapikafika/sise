import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

       int inputSize = 4;
       int[] hiddenSizes = { 4 };
        int outputSize = 3;

        NeuralNetwork neuralNetwork = new NeuralNetwork(inputSize, hiddenSizes, outputSize);
        DataReader reader = new DataReader();
        reader.loadTrainingDataFromFile("C:\\Studia\\SISE\\Zadanie 2\\Irysy\\iris.txt", 0.8);
        NeuralNetworkTrainer train = new NeuralNetworkTrainer(reader, neuralNetwork);
        train.train(false,false);
        train.test();

    }

}
