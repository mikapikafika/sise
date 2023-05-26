import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

       int inputSize = 4;
       int[] hiddenSizes = { 4 };
        int outputSize = 3;

        NeuralNetwork neuralNetwork = new NeuralNetwork(inputSize, hiddenSizes, outputSize);
        DataReader reader = new DataReader();
        //reader.loadTrainingDataFromFile("C:\\Studia\\sem4\\sise\\SISE\\Zadanie 2\\neuronNetwork\\iris.txt", 0.8);
        //reader.loadTrainingDataFromFile("C:\\\\Studia\\\\SISE\\\\Zadanie 2\\\\Irysy\\\\iris.txt", 0.8);
        reader.loadTrainingDataFromFile("H:\\GitRepositories\\SISE\\Zadanie 2\\Irysy\\iris.txt", 0.8);
        NeuralNetworkTrainer train = new NeuralNetworkTrainer(reader, neuralNetwork);
        train.train(true,false);
        train.test();

//        ResearchResults researchResults = new ResearchResults();
//        researchResults.getClassificationResults(reader, neuralNetwork);
    }

}
