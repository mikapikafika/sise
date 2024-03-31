import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

/* [Autorzy]
Michalina Wysocka
Laura Nowogórska

  [Argumenty]
0 - test/train/autoenkoder
1 - ilość epok(np. 1000 - dla trenowania, 0 - dla testu(albo jakakolwiek inna wartość))
2 - momentum - 0.0 jeśli bez
3 - bias - czy używamy biasu - false/true
4 - losowość - czy dane treningowe mają być losowane - true/false
5 - learning rate - np. 0.1
6 - neurony wejściowe
7 - neurony ukryte
8 - neurony wyjściowe

 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        DataReader dataReader = new DataReader();

        //dataReader.loadTrainingDataFromFile("C:\\Studia\\SISE\\Zadanie 2\\Irysy\\iris.txt",0.8);

        NeuralNetwork network;

        System.out.println("Czy chcesz wczytać sieć z pliku? t/n");
        if(Objects.equals(scanner.nextLine(), "t"))
        {
            System.out.println("Podaj ścieżkę pliku");
            network = NeuralNetwork.readFromFile(scanner.nextLine());
        }
        else {
            network = new NeuralNetwork(Double.parseDouble(args[5]),Double.parseDouble(args[2]),Integer.parseInt(args[6]),Integer.parseInt(args[7]),Integer.parseInt(args[8]));
        }
        if(Objects.equals(args[0], "autoencoder"))
        {
            dataReader.loadAutoEncoderDataFromFile("C:\\Studia\\SISE\\Zadanie 2\\Irysy\\autoencoder.txt");
            NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(network, dataReader);
            trainer.train(Integer.parseInt(args[1]), Double.parseDouble(args[2]), Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4]));
            trainer.testEncoder();
        }
        else
        {
            dataReader.loadTrainingDataFromFile("C:\\Studia\\SISE\\Zadanie 2\\Irysy\\iris.txt",0.8);
            NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(network, dataReader);
            switch (args[0]){
                case"train":
                    trainer.train(Integer.parseInt(args[1]), Double.parseDouble(args[2]), Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4]));
                    System.out.println("Czy chcesz zapisać sieć do pliku? t/n");
                    if(Objects.equals(scanner.nextLine(), "t"))
                    {
                        System.out.println("Podaj ścieżkę pliku");
                        network.saveToFile(scanner.nextLine());
                    }
                    trainer.test();
                    break;
                case "test":
                    trainer.test();
                    break;
        }
    }
}
}
