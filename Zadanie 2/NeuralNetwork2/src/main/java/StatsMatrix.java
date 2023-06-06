import java.io.Serializable;
import java.util.Arrays;

public class StatsMatrix implements Serializable {
    private double TP;
    private double TN;
    private double FP;
    private double FN;
    public int[][] confusionMatrix;
    private double[] species;
    private double correctlyClassified;

    public StatsMatrix(double[] species) {
        this.species = species;
    }

    public void confusionMatrixValues(double[] research, double[] actual) {
        boolean researchClassification = Arrays.equals(research, this.species);
        boolean actualClassification = Arrays.equals(actual, this.species);


        if (researchClassification && actualClassification) {
            this.TP++;
            this.correctlyClassified++;
        } else if (!actualClassification && researchClassification) {
            this.FP++;
        } else if (!researchClassification && actualClassification) {
            this.FN++;
        } else {
            this.TN++;
        }
    }

    public void displayResults() {
        System.out.println("============ Klasa " + doubleArrayToString(species) + " ===============");
        System.out.println("TP: " + this.TP);   //Sieć wskazuje na poprawne i w rzeczywistości też jest poprawne
        System.out.println("TN: " + this.TN);   //Sieć wskazuje na niepoprawne i w rzeczywistości też jest niepoprawne
        System.out.println("FP: " + this.FP);   //Sieć wskazuje na poprawne, ale w rzeczywistości jest niepoprawne
        System.out.println("FN: " + this.FN);   //Sieć wskazuje na niepoprawne, ale w rzeczywistości jest poprawne
        System.out.println("Precyzja: " + getPrecision());  //Ile otrzymanych jest prawidłowych(z wszystkich)
        System.out.println("Czułość: " + getRecall());      //Ile prawidłowych otrzymano(ze wszystkich prawidłowych)
        System.out.println("F-measure: " + getFMeasure());
        System.out.println("Poprawnie sklasyfikowane: " + this.correctlyClassified);
        System.out.println("=========================================");
    }


    public double getPrecision() {
        return (this.TP / (this.TP + this.FP));
    }

    public double getRecall() {
        return (this.TP / (this.TP + this.FN));
    }

    public double getFMeasure() {
        return 2 * ((getPrecision() * getRecall()) / (getPrecision() + getRecall()));
    }

    public static String doubleArrayToString(double[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

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
    public void incrementConfusionMatrix(int actualClass) {
        // Zwiększ wartość odpowiedniego pola w macierzy pomyłek
        confusionMatrix[actualClass][actualClass]++;
    }

    public void incrementBadConfusionMatrix(int actualClass, int predictedClass) {
        // Zwiększ wartość odpowiedniego pola w macierzy pomyłek
        confusionMatrix[predictedClass][actualClass]++;
    }

    public void displayConfusionMatrix() {
        int numClasses = confusionMatrix.length;

        System.out.println("Confusion Matrix:");
        for (int i = 0; i < numClasses; i++) {
            for (int j = 0; j < numClasses; j++) {
                System.out.print(confusionMatrix[i][j] + "\t");
            }
            System.out.println();
        }
    }


}
