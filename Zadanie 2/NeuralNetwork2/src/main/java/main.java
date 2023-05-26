import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws FileNotFoundException {
    DataReader dataReader = new DataReader();
    dataReader.loadTrainingDataFromFile("H:\\GitRepositories\\SISE\\Zadanie 2\\Irysy\\iris.txt",0.8);
    NeuralNetwork network = new NeuralNetwork(0.1,0.0,4,4,3);
    network.train(dataReader);
    }
}
