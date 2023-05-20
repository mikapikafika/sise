import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataReader {
        private List<List<Double>> inputs;
        private List<List<Double>> outputs;

        private List<List<Double>> testInputs;
        private List<List<Double>> testOutputs;

        public DataReader() {
            inputs = new ArrayList<>();
            outputs = new ArrayList<>();
            testOutputs = new ArrayList<>();
            testInputs = new ArrayList<>();
        }

        //trainingRatio to stosunek ilości wzorców treningowych do wszystkich danych
        public void loadTrainingDataFromFile(String filePath, double trainingRatio) throws FileNotFoundException {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            List<String[]> data = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                data.add(values);
            }
            //Odejmujemy ostatnią linię, która jest pusta
            int index = data.size() - 1;
            data.remove(index);

            int totalSize = data.size();
            int trainingSize = (int) (totalSize * trainingRatio);

            //Losowo wybieramy zestaw danych i usuwamy z głównej listy.
            Random random = new Random();
            while (data.size() > 0) {
                int randomIndex = random.nextInt(data.size());
                String[] values = data.remove(randomIndex);

                List<Double> input = new ArrayList<>();
                //Wyjście w postaci tablicy trzech double - każdy dla jednej klasy kwiatka
                List<Double> output = new ArrayList<>();

                //Przypisanie usuniętej z data wartości do danych wejściowych
                for (int i = 0; i < values.length - 1; i++) {
                    input.add(i, Double.parseDouble(values[i]));
                }

                //Ustanowienie wzorca wyjścia
                String className = values[values.length - 1];
                if (className.equals("Iris-setosa")) {
                    output.add(0, 1.0);
                    output.add(1, 0.0);
                    output.add(2, 0.0);
                } else if (className.equals("Iris-versicolor")) {
                    output.add(0, 0.0);
                    output.add(1, 1.0);
                    output.add(2, 0.0);
                } else if (className.equals("Iris-virginica")) {
                    output.add(0, 0.0);
                    output.add(1, 0.0);
                    output.add(2, 1.0);
                }

                //Sprawdza, czy liczba wczytanych danych jest mniejsza trainingSize. Jeśli tak, to wzorzec
                // zostaje dodany do zbioru treningowego. W przeciwnym razie wzorzec zostaje dodany do zbioru testowego
                if (data.size() < trainingSize) {
                    addTrainingExample(input, output);
                } else {
                    addTestExample(input, output);
                }
            }

            scanner.close();
        }

        public void addTrainingExample(List<Double> input, List<Double> output) {
            inputs.add(input);
            outputs.add(output);
        }

        public void addTestExample(List<Double> input, List<Double> output) {
            testInputs.add(input);
            testOutputs.add(output);
        }

        public List<Double> getInput(int index) {
            return inputs.get(index);
        }

        public List<Double> getOutput(int index) {
            return outputs.get(index);
        }

        public int getInputSize() {
            return inputs.size();
        }
    public int getTestSize() {
        return testInputs.size();
    }

    public List<Double> getTestInput(int index) {
        return testInputs.get(index);
    }
    public List<Double> getTestOutput(int index) {
        return testOutputs.get(index);
    }

}
