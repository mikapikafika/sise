import java.io.Serializable;
import java.util.Arrays;

public class StatsMatrix implements Serializable {
    public int[][] confusionMatrix;

    public void confusionMatrix() {
        int numClasses = 3;  // Liczba klas irysów

        // Inicjalizacja macierzy pomyłek
        confusionMatrix = new int[numClasses][numClasses];
        for (int i = 0; i < numClasses; i++) {
            for (int j = 0; j < numClasses; j++) {
                confusionMatrix[i][j] = 0;
            }
        }
    }

    public void calculateTP(int state) {
        int tp = confusionMatrix[state][state];
        System.out.println("TP: " + tp);
    }
    public double calculateFMeasure(int state) {
        double precision = calculatePrecision(state);
        double recall = calculateRecall(state);
        double fMeasure = 2 * (precision * recall) / (precision + recall);
        return fMeasure;
    }
    public void calculateTN(int state) {
        int tn = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            for (int j = 0; j < confusionMatrix.length; j++) {
                if (i != state && j != state) {
                    tn += confusionMatrix[i][j];
                }
            }
        }
        System.out.println("TN: " + tn);
    }

    public void calculateFP(int state) {
        int fp = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            if (i != state) {
                fp += confusionMatrix[i][state];
            }
        }
        System.out.println("FP: " + fp);
    }

    public void calculateFN(int state) {
        int fn = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            if (i != state) {
                fn += confusionMatrix[state][i];
            }
        }
        System.out.println("FN: " + fn);
    }
    public double calculatePrecision(int state) {
        int tp = confusionMatrix[state][state];
        int fp = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            if (i != state) {
                fp += confusionMatrix[i][state];
            }
        }
        double precision = (double) tp / (tp + fp);
        return precision;
    }

    public double calculateRecall(int state) {
        int tp = confusionMatrix[state][state];
        int fn = 0;
        for (int i = 0; i < confusionMatrix.length; i++) {
            if (i != state) {
                fn += confusionMatrix[state][i];
            }
        }
        double recall = (double) tp / (tp + fn);
        return recall;
    }

    public void incrementConfusionMatrix(int actualClass) {
        // Zwiększ wartość odpowiedniego pola w macierzy pomyłek
        confusionMatrix[actualClass][actualClass]++;
    }

    public void incrementBadConfusionMatrix(int actualClass, int predictedClass) {
        // Zwiększ wartość odpowiedniego pola w macierzy pomyłek
        confusionMatrix[predictedClass][actualClass]++;
    }

}
