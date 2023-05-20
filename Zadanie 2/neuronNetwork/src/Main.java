import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

       int inputSize = 4;
       int[] hiddenSizes = { 4 };
        int outputSize = 3;

        NeuralNetwork neuralNetwork = new NeuralNetwork(inputSize, hiddenSizes, outputSize);
        DataReader reader = new DataReader();
        reader.loadTrainingDataFromFile("F:\\Studia\\sem4\\sise\\sise\\Zadanie 2\\neuronNetwork\\iris.txt", 0.8);
        //reader.loadTrainingDataFromFile("C:\\\\Studia\\\\SISE\\\\Zadanie 2\\\\Irysy\\\\iris.txt", 0.8);
        NeuralNetworkTrainer train = new NeuralNetworkTrainer(reader, neuralNetwork);
        train.train(false,false);   //aaaaaaaaaaaaaaa
        train.test();

        ResearchResults researchResults = new ResearchResults();

        // Pobierz wyniki klasyfikacji
        List<List<Double>> classificationResults = researchResults.getClassificationResults(reader, neuralNetwork);

        // Wywołaj metody dla statystyk i macierzy pomyłek
        researchResults.getStats();
        researchResults.getConfusionMatrix();
    }

}
