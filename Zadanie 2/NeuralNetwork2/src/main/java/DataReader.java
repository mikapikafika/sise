import java.io.*;
import java.util.*;

public class DataReader {
    private List<double[][]> trainingData;

    private List<double[][]> testingData;

    public DataReader() {
        trainingData = new ArrayList<>();
        testingData = new ArrayList<>();
    }

    //trainingRatio to stosunek ilości wzorców treningowych do wszystkich danych
    public void loadTrainingDataFromFile(String filePath, double trainingRatio) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        List<double[][]> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<String> values = List.of(line.split(","));

            double[] input = new double[values.size() - 1];
            for (int j = 0; j < input.length; j++) {
                input[j] = Double.parseDouble(values.get(j));
            }
            double[] output;
            if(values.get(values.size() - 1).equals("Iris-setosa")) {
                output = new double[] { 1.0, 0.0, 0.0 };
            } else if (values.get(values.size() - 1).equals("Iris-versicolor")) {
                output = new double[] { 0.0, 1.0, 0.0 };
            } else if (values.get(values.size() - 1).equals("Iris-virginica")) {
                output = new double[] { 0.0, 0.0, 1.0 };
            }
            else break;
            double[][] object = new double[][] { input, output };
            data.add(object);
        }

        int totalSize = data.size();
        int trainingSize = (int) (totalSize * trainingRatio);


        //Losowo wybieramy zestaw danych i usuwamy z głównej listy.
        Random random = new Random();
        while (data.size() > 0) {
            int randomIndex = random.nextInt(data.size());
            double[][] values = data.remove(randomIndex);
            //Sprawdza, czy liczba wczytanych danych jest mniejsza trainingSize. Jeśli tak, to wzorzec
            // zostaje dodany do zbioru treningowego. W przeciwnym razie wzorzec zostaje dodany do zbioru testowego
            if (data.size() < trainingSize) {
                addTrainingExample(values);
            } else {
                addTestExample(values);
            }
        }

        scanner.close();
    }

    public void loadAutoEncoderDataFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        List<double[][]> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<String> values = List.of(line.split(","));

            double[] input = new double[4];
            for (int j = 0; j < 4; j++) {
                input[j] = Double.parseDouble(values.get(j));
            }
            double[] output = new double[4];
            for (int j = 0; j < 4; j++) {
                output[j] = Double.parseDouble(values.get(values.size() - 4 + j)); // Konwersja na int i przypisanie wartości
            }
            double[][] object = new double[][] { input, output };
            data.add(object);
        }
        //Losowo wybieramy zestaw danych i usuwamy z głównej listy.
        Random random = new Random();
        while (data.size() > 0) {
            int randomIndex = random.nextInt(data.size());
            double[][] values = data.remove(randomIndex);
            addTrainingExample(values);
            addTestExample(values);
        }

        scanner.close();
    }

    public void addTrainingExample(double[][] trainData) {
        trainingData.add(trainData);
    }

    public void addTestExample(double[][] testData) {
        testingData.add(testData);
    }

    public List<double[][]> getTrainingData() {
        return trainingData;
    }

    public List<double[][]> getTestingData() {
        return testingData;
    }
    public List<double[]> getTestingResult() {
        List<double[]> desired = new ArrayList<>();
        for (int i = 0; i < testingData.size(); i++) {
            desired.add(testingData.get(i)[1]);
        }
        return desired;
    }
    public List<double[]> getTestingInput() {
        List<double[]> desired = new ArrayList<>();
        for (int i = 0; i < testingData.size(); i++) {
            desired.add(testingData.get(i)[0]);
        }
        return desired;
    }

    public int getTrainingDataSize(){
        return trainingData.size();
    }

    public int getTestingDataSize(){
        return testingData.size();
    }

}
