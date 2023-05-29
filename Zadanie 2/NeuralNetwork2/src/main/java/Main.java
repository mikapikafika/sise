import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
    DataReader dataReader = new DataReader();
    dataReader.loadTrainingDataFromFile("H:\\GitRepositories\\SISE\\Zadanie 2\\Irysy\\iris.txt",0.8);
    //dataReader.loadTrainingDataFromFile("C:\\Studia\\SISE\\Zadanie 2\\Irysy\\iris.txt",0.8);
    NeuralNetwork network = new NeuralNetwork(0.1,0.0,4,4,3);
    NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(network, dataReader);
    trainer.train(1000, false, false, true);
    trainer.test();
    }
}
