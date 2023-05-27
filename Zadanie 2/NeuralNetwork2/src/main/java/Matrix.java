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

}
