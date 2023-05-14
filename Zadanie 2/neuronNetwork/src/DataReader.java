import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataReader {
    private List<double[]> inputs;
    private List<String> outputs;

    public DataReader() {
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    public void loadTrainingDataFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(",");

            double[] input = new double[values.length - 1];
            String output = values[values.length - 1];

            for (int i = 0; i < values.length - 1; i++) {
                input[i] = Double.parseDouble(values[i]);
            }

            addTrainingExample(input, output);
        }

        scanner.close();
    }

    public void addTrainingExample(double[] input, String output) {
        inputs.add(input);
        outputs.add(output);
    }

    public double[] getInput(int index) {
        return inputs.get(index);
    }

    public String getOutput(int index) {
        return outputs.get(index);
    }

    public int getSize() {
        return inputs.size();
    }
}