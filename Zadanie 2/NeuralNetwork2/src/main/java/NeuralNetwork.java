import java.io.*;

public class NeuralNetwork implements Serializable {
    private double learningRate = 0.1;
    private double momentum = 0.0;
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

    public NeuralNetwork() {
    }

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

    public void train(double[] input, double[] targetOutput, double momentum, Boolean isBias)
    {
        this.momentum = momentum;
        if(isBias == false)
        {
            BiasHidden = false;
            BiasOutput = false;
        }
//        Reprezentuje docelowe wyjście
        Matrix targetOutputMatrix;
        targetOutputMatrix = Matrix.createFromInput(targetOutput);

//  =======  [Propagacja w przód]  =======
//        Obliczenia w warstwie ukrytej
        Matrix inputMatrix = new Matrix();
        inputMatrix = inputMatrix.createFromInput(input);
        Matrix hidden = new Matrix();
        hidden = hidden.multiply(hiddenLayerWeights, inputMatrix);
//        Klonowanie macierzy jest wykorzystywane do przechowywania oryginalnych wag przed ich aktualizacją
//        za pomocą propagacji wstecznej, ponieważ są używane do obliczeń związanych z momentum
        Matrix tempWeightH = hiddenLayerWeights.clone();

//        Jeżeli używamy biasu, kod dodaje wartość 1 do wartości neuronów ukrytych
        if (BiasHidden)
        {
            hidden = hidden.add(hidden,this.biasH);
        }
//        Obliczenie wyjść warstwy ukrytej
        hidden = hidden.sigmoid();


//        Obliczenia w warstwie wyjściowej
        Matrix output = new Matrix();
        output = output.multiply(outputLayerWeights, hidden);
        Matrix tempWeightO = outputLayerWeights.clone();

//        Jeżeli używamy biasu, kod dodaje wartość 1 do wartości neuronów wyjściowych
        if (BiasOutput)
        {
            output = output.add(output,this.biasO);
        }

//      Obliczenie wyjść warstwy wyjściowej
        output = output.sigmoid();


//  =======  [Obliczanie błędów]  =======
        Matrix outputErrors = new Matrix();
        outputErrors = outputErrors.calculateErrors(targetOutputMatrix, output);

//  =======  [Propagacja wstecz]  =======
//        Obliczanie gradientu
        Matrix gradient = new Matrix();
        gradient = gradient.derivativeSigmoid(output);
        Matrix temp = new Matrix();
        temp = temp.subtract(targetOutputMatrix , output);
        gradient = gradient.multiplyByScalar(-1);
        gradient = gradient.multiplyByScalarMatrix(temp);
        Matrix tempGradient = gradient.clone();
        Matrix transposedHidden = hidden.transpose();

//        Aktualizacja wag warstwy wyjściowej
        Matrix outputWeightsDelta = new Matrix();
        outputWeightsDelta = outputWeightsDelta.multiply(gradient, transposedHidden);
        outputWeightsDelta = outputWeightsDelta.multiplyByScalar(learningRate);
        outputWeightsDelta = outputWeightsDelta.multiplyByScalar(-1);
        outputLayerWeights = outputLayerWeights.add(outputLayerWeights, outputWeightsDelta);

//        Uwzględnianie momentum, jeżeli jest różne od 0
        if (momentum != 0) {
            Matrix momentumMatrix = new Matrix();
            momentumMatrix = momentumMatrix.subtract(outputLayerWeights, tempWeightO);
            momentumMatrix = momentumMatrix.multiplyByScalar(momentum);
            outputLayerWeights = outputLayerWeights.add(outputLayerWeights, momentumMatrix);
        }

//        Aktualizacja biasu dla warstwy wyjściowej
        if (BiasOutput)
        {
            biasO = biasO.subtract(biasO, gradient);
        }

//        Obliczanie błędów warstwy ukrytej
        Matrix tempTWeightO = tempWeightO.transpose();
        Matrix hiddenErrors = new Matrix();
        hiddenErrors = hiddenErrors.multiply(tempTWeightO, tempGradient);
        Matrix gradientHidden = new Matrix();
        gradientHidden = gradientHidden.derivativeSigmoid(hidden);
        gradientHidden = gradientHidden.multiplyByScalarMatrix(hiddenErrors);

//       Aktualizacja wag warstwy ukrytej
        Matrix inputTransposed = inputMatrix.transpose();
        Matrix hiddenWeightsDelta = new Matrix();
        hiddenWeightsDelta = hiddenWeightsDelta.multiply(gradientHidden, inputTransposed);
        hiddenWeightsDelta = hiddenWeightsDelta.multiplyByScalar(learningRate);
        hiddenWeightsDelta = hiddenWeightsDelta.multiplyByScalar(-1);
        hiddenLayerWeights = hiddenLayerWeights.add(hiddenLayerWeights, hiddenWeightsDelta);

//        Uwzględnianie momentum, jeżeli jest różne od 0
        if (momentum != 0) {
            Matrix momentumMatrix = new Matrix();
            momentumMatrix = momentumMatrix.subtract(hiddenLayerWeights, tempWeightH);
            momentumMatrix = momentumMatrix.multiplyByScalar(momentum);
            hiddenLayerWeights = hiddenLayerWeights.add(hiddenLayerWeights, momentumMatrix);
        }
//        Aktualizacja biasu dla warstwy ukrytej
        if (BiasHidden)
        {
            biasH = biasH.subtract(biasH, gradientHidden);
        }


        calculateNeuralNetworkError(outputErrors.data);
    }


    public double[] test(double[] input, double[] targetOutput, String filePath) {
        Matrix inputMatrix = new Matrix();
        inputMatrix = inputMatrix.createFromInput(input);

//  =======  [Propagacja w przód]  =======
//        Obliczenia w warstwie ukrytej
        Matrix hidden = new Matrix();
        hidden = hidden.multiply(hiddenLayerWeights, inputMatrix);

//       Jeżeli używamy biasu, kod dodaje wartość 1 do wartości neuronów wyjściowych
        if (BiasHidden)
        {
            hidden = hidden.add(hidden,this.biasH);
        }

//       Obliczenie wyjść warstwy ukrytej
        hidden = hidden.sigmoid();


//        Obliczenia w warstwie wyjściowej
        Matrix output = new Matrix();
        output = output.multiply(outputLayerWeights, hidden);

//       Jeżeli używamy biasu, kod dodaje wartość 1 do wartości neuronów wyjściowych
        if (BiasOutput)
        {
            output = output.add(output,this.biasO);
        }

//      Obliczenie wyjść warstwy wyjściowej
        output = output.sigmoid();

        try (FileWriter fileWriter = new FileWriter(filePath, true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write("Wzorzec wejściowy: " + inputMatrix + "\n");
            Matrix outputErrors = new Matrix();
            outputErrors = outputErrors.calculateErrors(Matrix.createFromInput(targetOutput), output);
            calculateNeuralNetworkError(outputErrors.data);
            writer.write("Błąd popełniony przez sieć dla wzorca: " + getNeuralNetworkError() + "\n");
            Matrix targetOutputMatrix = Matrix.createFromInput(targetOutput);
            writer.write("Pożądany wzorzec odpowiedzi: " + targetOutputMatrix + "\n");
            writer.write("Błędy popełnione na wyjściach sieci: " + outputErrors + "\n");
            writer.write("Wartości wyjściowe neuronów wyjściowych: " + output + "\n");
            writer.write("Wagi neuronów wyjściowych: " + Matrix.matrixToString(outputLayerWeights.data) + "\n");
            writer.write("Wartości wyjściowe neuronów ukrytych: " + Matrix.matrixToString(hidden.data) + "\n");
            writer.write("Wagi neuronów ukrytych: " + Matrix.matrixToString(hiddenLayerWeights.data) + "\n\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Matrix.toDoubleArray(output);
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

    public void saveToFile(String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(this);
            System.out.println("Obiekt został zapisany do pliku.");
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisu obiektu do pliku: " + e.getMessage());
        }
    }

    public static NeuralNetwork readFromFile(String filePath)
    {
        NeuralNetwork neuralNetwork = null;
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            neuralNetwork = (NeuralNetwork) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return neuralNetwork;
    }

}
