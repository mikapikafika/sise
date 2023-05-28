public class StatsMatrix {
    private double TP;
    private double TN;
    private double FP;
    private double FN;
    private double species;
    private double correctlyClassified;

    public StatsMatrix(double species) {
        this.species = species;
    }

    public void confusionMatrixValues(double research, double actual) {
        boolean researchClassification = research == this.species;
        boolean actualClassification = actual == this.species;


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
        System.out.println("============ Klasa " + this.species + " ===============");
        System.out.println("TP: " + this.TP);
        System.out.println("TN: " + this.TN);
        System.out.println("FP: " + this.FP);
        System.out.println("FN: " + this.FN);
        System.out.println("Precyzja: " + getPrecision());
        System.out.println("Czułość: " + getRecall());
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
}
