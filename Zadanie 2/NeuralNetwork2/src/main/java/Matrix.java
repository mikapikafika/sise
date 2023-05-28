import java.util.ArrayList;
import java.util.List;

public class Matrix implements Cloneable{
    public int rowNum;
    public int colNum;
    public double[][] data;

    public Matrix(int rowNum, int colNum) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.data = new double[rowNum][colNum];
    }

    public Matrix() {
    }

    /**
     * Ustawia losowe wartości z zakresu (-1/sqrt(rowNum), 1/sqrt(rowNum))
     */
    public void randomizeValues()
    {
        for (int i = 0; i < rowNum; i++)
        {
            for (int j = 0; j < colNum; j++)
            {
                data[i][j] = (((Math.random() * 2) - 1) / (Math.sqrt(rowNum)));
            }
        }
    }

    /**
     * Tworzy macierz mającą tyle samo wierszy, co długość input i jedną kolumnę
     * Elementy macierzy = wartości z tablicy input
     * @param input
     * @return nową macierz
     */
    public static Matrix createFromInput(double[] input) {
        int rowNum = input.length;
        Matrix matrix = new Matrix(rowNum, 1);
        for (int i = 0; i < rowNum; i++) {
            matrix.data[i][0] = input[i];
        }
        return matrix;
    }

    public void display() {
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                System.out.print(data[i][j]);
            }
            System.out.println('\n');
        }
    }

    /**
     * Tworzy wektor obciążenia (bias) wypełniony jedynkami
     * @param row
     * @param col
     * @return bias
     */
    public static Matrix createBias(int row, int col)
    {
        Matrix bias = new Matrix(row, col);
        for(int i = 0; i< row;i++)
        {
            bias.data[i][0] = 1;
        }
        return bias;
    }

    public static Matrix multiply(Matrix a, Matrix b) {

        Matrix c = new Matrix(a.rowNum, b.colNum);
        for (int i = 0; i < c.rowNum; i++) {
            for (int j = 0; j < c.colNum; j++) {
                double sum = 0.0;
                for (int k = 0; k < a.colNum; k++) {
                    sum += a.data[i][k] * b.data[k][j];
                }
                c.data[i][j] = sum;
            }
        }
        return c;
    }

    public static Matrix add(Matrix a, Matrix b) {

        Matrix c = new Matrix(a.rowNum, a.colNum);

        for (int i = 0; i < a.rowNum; i++) {
            for (int j = 0; j < a.colNum; j++) {
                c.data[i][j] = a.data[i][j] + b.data[i][j];
            }
        }
        return c;
    }

    /**
     * Oblicza funkcję sigmoidalną dla każdego elementu w macierzy
     * @return wynik - nowa macierz
     */
    public Matrix sigmoid() {
        Matrix c = new Matrix(rowNum, colNum);
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                c.data[i][j] = (1/(1+Math.exp(-this.data[i][j])));
            }
        }
        return c;
    }

    /**
     * Oblicza błędy dla każdego elementu w macierzy na podstawie docelowej macierzy wyjściowej i macierzy wyjściowej
     * @param targetOutputMatrix
     * @param output
     * @return nową macierz
     */
    public Matrix calculateErrors(Matrix targetOutputMatrix, Matrix output) {
        int rows = targetOutputMatrix.rowNum;
        int cols = targetOutputMatrix.colNum;
        Matrix c = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                c.data[i][j] = Math.pow(targetOutputMatrix.data[i][j]-output.data[i][j],2)/2;
            }
        }
        return c;
    }

    /**
     * Oblicza pochodną funkcji sigmoidalnej dla każdego elementu w macierzy
     * @param output
     * @return nową macierz
     */
    public Matrix derivativeSigmoid(Matrix output) {
        int rows = output.rowNum;
        int cols = output.colNum;
        Matrix c = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // x * (1 - x)
                c.data[i][j] = output.data[i][j] * (1 - output.data[i][j]);
            }
        }
        return c;
    }

    public Matrix subtract(Matrix targetOutputMatrix, Matrix output) {
        int rows = targetOutputMatrix.rowNum;
        int cols = targetOutputMatrix.colNum;
        Matrix c = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                c.data[i][j] = targetOutputMatrix.data[i][j] - output.data[i][j];
            }
        }
        return c;
    }

    public static Double[] toDoubleArray(Matrix temp) {
        List<Double> array = new ArrayList<>();
        for (int i = 0; i < temp.rowNum; i++) {
            for (int j = 0; j < temp.colNum; j++) {
                array.add(temp.data[i][j]);
            }
        }
        return array.toArray(Double[]::new);
    }

    public Matrix multiplyByScalar(double i) {
        int rows = this.rowNum;
        int cols = this.colNum;
        Matrix c = new Matrix(rows, cols);
        for (int j = 0; j < rows; j++) {
            for (int k = 0; k < cols; k++) {
                c.data[j][k] = this.data[j][k] * i;
            }
        }
        return c;
    }

    public Matrix multiplyByScalarMatrix(Matrix temp) {
        int rows = this.rowNum;
        int cols = this.colNum;
        Matrix c = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                c.data[i][j] = this.data[i][j] * temp.data[i][j];
            }
        }
        return c;
    }

    public Matrix transpose() {
        int rows = this.rowNum;
        int cols = this.colNum;
        Matrix c = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                c.data[j][i] = this.data[i][j];
            }
        }
        return c;
    }

    public static double[] doubleToDouble(Double[] array) {
        double[] newArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static String matrixToString(double[][] temp) {
        StringBuilder sb = new StringBuilder();
        for (double[] row : temp) {
            for (double value : row) {
                sb.append(value).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public Matrix clone() {
        try {
            Matrix clone = (Matrix) super.clone();
            clone.data = new double[clone.rowNum][clone.colNum];
            for (int i = 0; i < clone.rowNum; i++) {
                if (clone.colNum >= 0) System.arraycopy(this.data[i], 0, clone.data[i], 0, clone.colNum);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
