import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Neuron implements Serializable {
    private List<Double> weights;
    private double error;
    private List<Double> lastWeightChanges;
    private double bias = 0.0;
    private double output;

    public Neuron(int inputSize) {
        weights = new ArrayList<>();
        lastWeightChanges = weights;
    }

    public Neuron(int inputSize, double biasIn) {
        weights = new ArrayList<>();
        lastWeightChanges = weights;
        bias = biasIn;
    }

    public double activate(List<Double> inputs) {
        double sum = 0.0;
        for (int i = 0; i < inputs.size(); i++) {
            sum += inputs.get(i) * weights.get(i);
        }
        sum += bias;
        output = sigmoid(sum);

        return output;
    }

    public void updateWeightsWithMomentum(double learningRate, double momentum) {

    }

    public void updateWeights(double learningRate) {
        List<Double> temp = weights;
        for (int i = 0; i < weights.size(); i++) {
            double weightChange = learningRate * lastWeightChanges.get(i);
            weights.set(i, weightChange);
        }
        lastWeightChanges = temp;
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


    public double getWeightAtIndex(int index) {
        return weights.get(index);
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
        for(int i = 0; i < weights.size(); i++)
        {
            lastWeightChanges.add(i, 1.0);
        }
    }

    public List<Double> getWeights() {
        return weights;
    }

    public double getError() {
        return error;
    }

    public double getOutput() {
        return output;
    }
}
