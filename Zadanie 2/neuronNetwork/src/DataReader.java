import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataReader {
        private List<double[]> inputs;
        private List<double[]> outputs;

        private List<double[]> testInputs;
        private List<double[]> testOutputs;

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

                double[] input = new double[values.length - 1];
                //Wyjście w postaci tablicy trzech double - każdy dla jednej klasy kwiatka
                double[] output = new double[3];

                //Przypisanie usuniętej z data wartości do danych wejściowych
                for (int i = 0; i < values.length - 1; i++) {
                    input[i] = Double.parseDouble(values[i]);
                }

                //Ustanowienie wzorca wyjścia
                String className = values[values.length - 1];
                if (className.equals("Iris-setosa")) {
                    output[0] = 1.0;
                    output[1] = 0.0;
                    output[2] = 0.0;
                } else if (className.equals("Iris-versicolor")) {
                    output[0] = 0.0;
                    output[1] = 1.0;
                    output[2] = 0.0;
                } else if (className.equals("Iris-virginica")) {
                    output[0] = 0.0;
                    output[1] = 0.0;
                    output[2] = 1.0;
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

        public void addTrainingExample(double[] input, double[] output) {
            inputs.add(input);
            outputs.add(output);
        }

        public void addTestExample(double[] input, double[] output) {
            testInputs.add(input);
            testOutputs.add(output);
        }

        public double[] getInput(int index) {
            return inputs.get(index);
        }

        public double[] getOutput(int index) {
            return outputs.get(index);
        }

        public int getInputSize() {
            return inputs.size();
        }
    public int getTestSize() {
        return testInputs.size();
    }

    public double[] getTestInput(int index) {
        return testInputs.get(index);
    }
    public double[] getTestOutput(int index) {
        return testOutputs.get(index);
    }

    public String getStringInput(int index) {
        StringBuilder sb = new StringBuilder();
        double[] array = inputs.get(index);
        sb.append(Arrays.toString(array));
        String result = sb.toString();
        return result;
    }
    public String getStringOutput(int index) {
        StringBuilder sb = new StringBuilder();
        double[] array = outputs.get(index);
        sb.append(Arrays.toString(array));
        String result = sb.toString();
        return result;
    }
    public String getStringTestInput(int index) {
        StringBuilder sb = new StringBuilder();
        double[] array = testInputs.get(index);
        sb.append(Arrays.toString(array));
        String result = sb.toString();
        return result;
    }
    public String getStringTestOutput(int index) {
        StringBuilder sb = new StringBuilder();
        double[] array = testOutputs.get(index);
        sb.append(Arrays.toString(array));
        String result = sb.toString();
        return result;
    }
}
