import java.util.ArrayList;
import java.util.List;

public class ResearchResults {
    private double[] truePositive;
    private double[] trueNegative;
    private double[] falsePositive;
    private double[] falseNegative;
    private double accuracy;
    private double precision;
    private double recall;
    private double fMeasure;
    private int numCorrectlyClassified;
    private int[] numCorrectPerClass;
    private List<List<Double>> outputs = new ArrayList<>();
    private List<List<Double>> expectedOutputs = new ArrayList<>();

    // moze cos takiego na przyklad, mozemy to zapisywac do pliku tez
    // outputs to lista ktora ma wyniki klasyfikacji obiektow, zewnetrzna lista dla wszystkich obiektow
    // a wewnetrzna dla klas
    // expectedoutputs no to to samo ale oczekiwane

    // A tylko skapnelam sie ze mamy np metode calculateLayerOutput, predict itd i w sumie mozna jej i podobnych tu uzyc

    public void getClassificationResults(DataReader data, NeuralNetwork neuralNetwork) {
        int numPatterns = data.getInputSize();
        for (int i = 0; i < numPatterns; i++) {
            List<Double> inputPattern = data.getInput(i);
            List<Double> targetOutput = data.getOutput(i);

            // Obliczenie wyników klasyfikacji dla obiektu chyba tak?
            List<Double> output = neuralNetwork.feedForward(inputPattern);

            // Dodanie wyników klasyfikacji i oczekiwanych wyników do odpowiednich list
            outputs.add(output);
            expectedOutputs.add(targetOutput);
        }
        getStats();
        getConfusionMatrix();
    }


    public void getStats() {
        int numObjects = outputs.size();
        int numClasses = outputs.get(0).size();
        
        truePositive = new double[numClasses];
        trueNegative = new double[numClasses];
        falsePositive = new double[numClasses];
        falseNegative = new double[numClasses];
        numCorrectlyClassified = 0;
        numCorrectPerClass = new int[numClasses];

        for (int i = 0; i < numObjects; i++) {

            // Indeks wartości o najwyższym oczekiwanym/obliczonym wyniku dla obiektu
            // Można ocenić, czy klasa jest poprawnie sklasyfikowana
            int expectedMaxIndex = 0;
            int maxIndex = 0;
            // Najwyższa oczekiwana/obliczona wartość dla obiektu
            // Można ocenić pewność klasyfikacji
            double expectedMaxValue = expectedOutputs.get(i).get(0);
            double maxValue = outputs.get(i).get(0);

            for (int j = 1; j < numClasses; j++) {
                if (expectedMaxValue < expectedOutputs.get(i).get(j)) {
                    expectedMaxValue = expectedOutputs.get(i).get(j);
                    expectedMaxIndex = j;
                }
                if (maxValue < outputs.get(i).get(j)) {
                    maxValue = outputs.get(i).get(j);
                    maxIndex = j;
                }
            }

            for (int j = 0; j < numClasses; j++) {
                // Klasa != bieżącej klasie j, ale jest jest tak błędnie sklasyfikowana
                if (expectedMaxIndex != j && maxIndex == j) {
                    falsePositive[j]++;
                }
                // Klasa == bieżącej klasie j, ale jest sklasyfikowana jako inna
                else if (expectedMaxIndex == j && maxIndex != j) {
                    falseNegative[j]++;
                }
                // Klasa == bieżącej klasie j i jest tak sklasyfikowana
                else if (expectedMaxIndex == j) {
                    truePositive[j]++;
                    numCorrectlyClassified++;
                    numCorrectPerClass[j]++;
                } else {
                    trueNegative[j]++;
                }
            }
        }

        // Wyświetlenie statystyk
        for (int i = 0; i < numClasses; i++) {
            accuracy = (truePositive[i] + trueNegative[i]) / numObjects;
            precision = truePositive[i] / (truePositive[i] + falsePositive[i]);
            recall = truePositive[i] / (truePositive[i] + falseNegative[i]);
            fMeasure = (2 * precision * recall) / (precision + recall);

            System.out.println("Klasa " + i + ":");
            System.out.println("Dokładność: " + accuracy);
            System.out.println("Precyzja: " + precision);
            System.out.println("Czułość: " + recall);
            System.out.println("F-Measure: " + fMeasure);
            System.out.println();
        }

        System.out.println("Liczba poprawnie sklasyfikowanych obiektów (łącznie): " + numCorrectlyClassified);
        System.out.println("Liczba poprawnie sklasyfikowanych obiektów (w rozbiciu na klasy):");
        for (int i = 0; i < numClasses; i++) {
            System.out.println("Klasa " + i + ": " + numCorrectPerClass[i]);
        }
    }

    public void getConfusionMatrix() {
        int numClasses = truePositive.length;
        System.out.println("Macierz błędu:");

        System.out.print("isCor/res\t");
        for (int i = 0; i < numClasses; i++) {
            System.out.print("Klasa " + i + "\t");
        }
        System.out.println();

        System.out.print("true\t\t");
        for (int i = 0; i < numClasses; i++) {
            System.out.print(truePositive[i] + "\t\t");
        }
        System.out.println();

        System.out.print("false\t\t");
        for (int i = 0; i < numClasses; i++) {
            System.out.print(falseNegative[i] + "\t\t");
        }
        System.out.println();

        System.out.print("true\t\t");
        for (int i = 0; i < numClasses; i++) {
            System.out.print(falsePositive[i] + "\t\t");
        }
        System.out.println();

        System.out.print("false\t\t");
        for (int i = 0; i < numClasses; i++) {
            System.out.print(trueNegative[i] + "\t\t");
        }
        System.out.println();
    }

}
