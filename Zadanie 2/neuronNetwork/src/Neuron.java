import java.io.Serializable;
import java.util.List;

public class Neuron implements Serializable {
    private double[] weights;
    private double error;
    private double[] lastWeightChanges;
    private double bias = 0.0;
    private double output;

    public Neuron(int inputSize) {
        weights = new double[inputSize];
        initializeWeights();
    }

    public Neuron(int inputSize, double biasIn) {
        weights = new double[inputSize];
        initializeWeights();
        bias = biasIn;
    }

    public double activate(double[] inputs) {
        if (inputs.length != weights.length) {
            throw new IllegalArgumentException("Input size doesn't match weight size");
        }

        double sum = 0.0;
        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }
        sum += bias;
        output = sigmoid(sum);

        return output;
    }

    public void updateWeights(double learningRate) {
        for (int i = 0; i < weights.length; i++) {
            double weightChange = learningRate * lastWeightChanges[i];
            weights[i] -= weightChange;
            lastWeightChanges[i] = weightChange; // Aktualizacja ostatnich zmian wag
        }
    }

    public void calculateError(double error) {
        this.error = error * derivativeActivationFunction(output);
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double derivativeActivationFunction(double x) {
        return sigmoid(x) * (1 - sigmoid(x));
    }


    private void initializeWeights() {
        // Randomly initialize weights between -1 and 1
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random() * 2 - 1;
        }
    }

    public double getWeights(int index) {
        return weights[index];
    }

    public double getError() {
        return error;
    }
}
