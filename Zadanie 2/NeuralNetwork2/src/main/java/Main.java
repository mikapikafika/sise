import java.io.FileNotFoundException;



/*  Argumenty
0 - test/train
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
        DataReader dataReader = new DataReader();
        dataReader.loadTrainingDataFromFile("H:\\GitRepositories\\SISE\\Zadanie 2\\Irysy\\iris.txt",0.8);
        NeuralNetwork network = new NeuralNetwork(Double.parseDouble(args[5]),Double.parseDouble(args[2]),Integer.parseInt(args[6]),Integer.parseInt(args[7]),Integer.parseInt(args[8]));
        NeuralNetworkTrainer trainer = new NeuralNetworkTrainer(network, dataReader);
        switch (args[0]){
            case"train":
                if(Double.parseDouble(args[2]) != 0.0)
                    trainer.train(Integer.parseInt(args[1]), true, Boolean.getBoolean(args[3]), Boolean.getBoolean(args[4]));
                else trainer.train(Integer.parseInt(args[1]), false, Boolean.getBoolean(args[3]), Boolean.getBoolean(args[4]));
            case "test":
                trainer.test();
    //dataReader.loadTrainingDataFromFile("C:\\Studia\\SISE\\Zadanie 2\\Irysy\\iris.txt",0.8);
    }
}
}
