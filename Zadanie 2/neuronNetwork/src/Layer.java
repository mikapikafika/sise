import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Layer implements Serializable {
    private List<Neuron> neurons;

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public Layer(int neuronCount, int inputSize) {
        neurons = new ArrayList<>();
        initializeWeights(inputSize, neuronCount);
    }

    public void initializeWeights(int inputSize, int neuronCount) {
        Random random = new Random();
        List<Double> weights = new ArrayList<>();

        for (int i = 0; i < inputSize; i++) {
            double weight = random.nextDouble() * 2 - 1; // losowa waga z przedziału [-1, 1]
            weights.add(weight);
        }

        for (int i = 0; i < neuronCount; i++) {
            Neuron neuron = new Neuron(inputSize);
            neuron.setWeights(weights); // ustawienie wspólnych wag dla neuronów w warstwie
            neurons.add(neuron);
        }
    }

    public List<Double> propagate(List<Double> inputs) {
        List<Double> outputs = new ArrayList<>();

        for (Neuron neuron : neurons) {
            double output = neuron.activate(inputs);
            outputs.add(output);
        }

        return outputs;
    }

    public int getSize(){
        return neurons.size();
    }

    public List<Double> getOutputs() {
        List<Double> outputs = new ArrayList<>();
        for (Neuron neuron : neurons) {
            outputs.add(neuron.getOutput());
        }
        return outputs;
    }

    public Neuron getNeuron(int index){
        return neurons.get(index);
    }
}
