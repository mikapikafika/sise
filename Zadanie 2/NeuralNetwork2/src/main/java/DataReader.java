import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataReader {
    private List<double[][]> trainingData;

    private List<double[]> testingData;

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
        while (data.size() > trainingSize) {
            int randomIndex = random.nextInt(data.size());
            double[][] values = data.remove(randomIndex);
            //Sprawdza, czy liczba wczytanych danych jest mniejsza trainingSize. Jeśli tak, to wzorzec
            // zostaje dodany do zbioru treningowego. W przeciwnym razie wzorzec zostaje dodany do zbioru testowego
            if (data.size() > trainingSize) {
                addTrainingExample(values);
            } else {
                double[] testvalue = values[1];
                addTestExample(testvalue);
            }
        }

        scanner.close();
    }

    public void addTrainingExample(double[][] trainData) {
        trainingData.add(trainData);
    }

    public void addTestExample(double[] testData) {
        testingData.add(testData);
    }

    public List<double[][]> getTrainingData() {
        return trainingData;
    }

    public List<double[]> getTestingData() {
        return testingData;
    }
    public int getTrainingDataSize(){
        return trainingData.size();
    }
    public int getTestingDataSize(){
        return testingData.size();
    }
}
