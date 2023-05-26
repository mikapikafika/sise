public class NeuralNetwork {
    private final double learningRate;
    private final double momentum;
    private boolean BiasHidden = true;
    private boolean BiasOutput = true;
    private Matrix hiddenLayerWeights;
    private Matrix weightsHO;
    private Matrix biasH;
    private Matrix biasO;
    private int inputNeurons;
    private int hiddenNeurons;
    private int outputNeurons;

    public NeuralNetwork(double learningRate, double momentum, int inputNeurons, int hiddenNeurons, int outputNeurons) {
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.inputNeurons = inputNeurons;
        this.hiddenNeurons = hiddenNeurons;
        this.outputNeurons = outputNeurons;
        this.hiddenLayerWeights = new Matrix(hiddenNeurons, inputNeurons);
        this.weightsHO = new Matrix(inputNeurons, hiddenNeurons);
        this.biasH = new Matrix(hiddenNeurons,1);
        this.biasO = new Matrix(outputNeurons,1);
        weightsHO.randomizeValues();
        hiddenLayerWeights.randomizeValues();
    }

    public void train(DataReader data)
    {
//        Propagacja w przód
//        Obliczenia w warstwie ukrytej
        Matrix input = new Matrix();
        input = input.createFromInput(data);
        Matrix hidden = new Matrix();
        hidden = hidden.multiply(hiddenLayerWeights, input);
        biasH.display();
        Matrix tempWeight = hiddenLayerWeights;
//        Jeżeli używamy biasu, kod dodaje wartość 1 do wartości neuronów ukrytych
        if(BiasHidden)
        {
           // hidden = hidden.add(hidden,this.biasH);
        }

    }

}
