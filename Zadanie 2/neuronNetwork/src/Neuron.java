import java.util.List;

public class Neuron {
    private double[] weights;
    private double bias = 0.0;

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
        double output = sigmoid(sum);

        return output;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private void initializeWeights() {
        // Randomly initialize weights between -1 and 1
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random() * 2 - 1;
        }
    }
}
