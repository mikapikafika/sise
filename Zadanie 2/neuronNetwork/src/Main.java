import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

//        int inputSize = 4;
//        int[] hiddenSizes = { 8, 6 };
//        int outputSize = 1;
//
//        NeuralNetwork neuralNetwork = new NeuralNetwork(inputSize, hiddenSizes, outputSize);
//
//        // Przykładowe wejście
//        double[] input = { 0.5, 0.2, 0.7, 0.9 };
//
//        // Przewidywanie wyjścia sieci
//        double[] output = neuralNetwork.predict(input);
//
//        // Wyświetlanie wyniku
//        System.out.println("Wynik sieci:");
//        for (double value : output) {
//            System.out.println(value);
//        }
//        neuralNetwork.saveToFile("zapisana.ser");
//
//        try {
//            NeuralNetwork network = NeuralNetwork.loadFromFile("zapisana.ser");
//            // Dalsze operacje na wczytanej sieci neuronowej
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
        DataReader reader = new DataReader();
        reader.loadTrainingDataFromFile("C:\\Studia\\SISE\\Zadanie 2\\Irysy\\iris.txt", 0.8);
        System.out.println(reader.getInputSize());
        System.out.println(reader.getTestSize());
        System.out.println(reader.getStringTestInput(2));
        System.out.println(reader.getStringTestOutput(2));
    }

}
