public class NeuralNetwork {
    private final double learningRate;
    private final double momentum;
    private boolean BiasHidden = true;
    private boolean BiasOutput = true;
    private Matrix hiddenLayerWeights;
    private Matrix outputLayerWeights;
    private Matrix biasH;
    private Matrix biasO;
    private int inputNeurons;
    private int hiddenNeurons;
    private int outputNeurons;
    public double neuralNetworkError = 0.0;


    public NeuralNetwork(double learningRate, double momentum, int inputNeurons, int hiddenNeurons, int outputNeurons) {
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.inputNeurons = inputNeurons;
        this.hiddenNeurons = hiddenNeurons;
        this.outputNeurons = outputNeurons;
        this.hiddenLayerWeights = new Matrix(hiddenNeurons, inputNeurons);
        this.outputLayerWeights = new Matrix(outputNeurons, hiddenNeurons);
        this.biasH = Matrix.createBias(hiddenNeurons,1);
        this.biasO = Matrix.createBias(outputNeurons,1);
        outputLayerWeights.randomizeValues();
        hiddenLayerWeights.randomizeValues();
    }

    public void train(double[] input, double[] targetOutput)
    {
        Matrix targetOutputMatrix = new Matrix();
        targetOutputMatrix = Matrix.createFromInput(targetOutput);
//        [Propagacja w przód]
//        Obliczenia w warstwie ukrytej
        Matrix inputMatrix = new Matrix();
        inputMatrix = inputMatrix.createFromInput(input);
        Matrix hidden = new Matrix();
        hidden = hidden.multiply(hiddenLayerWeights, inputMatrix);
        Matrix tempWeightH = hiddenLayerWeights.clone();
//        Jeżeli używamy biasu, kod dodaje wartość 1 do wartości neuronów ukrytych
        if(BiasHidden)
        {
            //biasH = biasH.createBias(hiddenNeurons,1);
            hidden = hidden.add(hidden,this.biasH);
        }
//        Obliczenie wyjść warstwy ukrytej
        hidden = hidden.sigmoid();

//      Obliczenia w warstwie wyjściowej
        Matrix output = new Matrix();
        output = output.multiply(outputLayerWeights, hidden);
        Matrix tempWeightO = outputLayerWeights.clone();
//        Jeżeli używamy biasu, kod dodaje wartość 1 do wartości neuronów wyjściowych
        if(BiasOutput)
        {
            //biasO = biasO.createBias(outputNeurons,1);
            output = output.add(output,this.biasO);
        }
//      Obliczenie wyjść warstwy wyjściowej
        output = output.sigmoid();

//        [Obliczanie błędów]
        Matrix outputErrors = new Matrix();
        outputErrors = outputErrors.calculateErrors(targetOutputMatrix, output);

//        [Propagacja wstecz]
//        Obliczanie gradientu
        Matrix gradient = new Matrix();
        gradient = gradient.derivativeSigmoid(output);
        Matrix temp = new Matrix();
        temp = temp.subtract(targetOutputMatrix , output);
        gradient = gradient.multiplyByScalar(-1);
        gradient = gradient.multiplyByScalarMatrix(temp);
        Matrix tempGradient = gradient.clone();
        Matrix transposedHidden = hidden.transpose();
        Matrix outputWeightsDelta = new Matrix();
        outputWeightsDelta = outputWeightsDelta.multiply(gradient, transposedHidden);
        outputWeightsDelta = outputWeightsDelta.multiplyByScalar(learningRate);
        outputWeightsDelta = outputWeightsDelta.multiplyByScalar(-1);
//        Aktualizacja wag
        outputLayerWeights = outputLayerWeights.add(outputLayerWeights, outputWeightsDelta);

//        Uwzględnianie momentum, jeżeli jest różne od 0
        if(momentum != 0) {
            Matrix momentumMatrix = new Matrix();
            momentumMatrix = momentumMatrix.subtract(outputLayerWeights, tempWeightO);
            momentumMatrix = momentumMatrix.multiplyByScalar(momentum);
            outputLayerWeights = outputLayerWeights.add(outputLayerWeights, momentumMatrix);
        }
//      Aktualizacja biasu
        if(BiasOutput)
        {
            biasO = biasO.subtract(biasO, gradient);
        }

//    Obliczanie błędów warstwy ukrytej
        Matrix tempTWeightO = tempWeightO.transpose();
        Matrix hiddenErrors = new Matrix();
        hiddenErrors = hiddenErrors.multiply(tempTWeightO, tempGradient);
        Matrix gradientHidden = new Matrix();
        gradientHidden = gradientHidden.derivativeSigmoid(hidden);
        gradientHidden = gradientHidden.multiplyByScalarMatrix(hiddenErrors);
//      Aktualizacja wag warstwy ukrytej
        Matrix inputTransposed = inputMatrix.transpose();
        Matrix hiddenWeightsDelta = new Matrix();
        hiddenWeightsDelta = hiddenWeightsDelta.multiply(gradientHidden, inputTransposed);
        hiddenWeightsDelta = hiddenWeightsDelta.multiplyByScalar(learningRate);
        hiddenWeightsDelta = hiddenWeightsDelta.multiplyByScalar(-1);
        hiddenLayerWeights = hiddenLayerWeights.add(hiddenLayerWeights, hiddenWeightsDelta);
//        Uwzględnianie momentum, jeżeli jest różne od 0
        if(momentum != 0) {
            Matrix momentumMatrix = new Matrix();
            momentumMatrix = momentumMatrix.subtract(hiddenLayerWeights, tempWeightH);
            momentumMatrix = momentumMatrix.multiplyByScalar(momentum);
            hiddenLayerWeights = hiddenLayerWeights.add(hiddenLayerWeights, momentumMatrix);
        }
//      Aktualizacja biasu
        if(BiasHidden)
        {
            biasH = biasH.subtract(biasH, gradientHidden);
        }
        calculateNeuralNetworkError(outputErrors.data);
    }

    private void calculateNeuralNetworkError(double[][] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                neuralNetworkError += data[i][j];
            }
        }
    }

    public double getNeuralNetworkError() {
        return neuralNetworkError;
    }
}
