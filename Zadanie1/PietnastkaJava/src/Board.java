import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Board {
    public int[] fields;
    public int zeroIndex;
    public int rows;
    public int cols;
    public char lastMove;
    public Board parentBoard;
    public int recDepth = 0;
    public String path = "";
    public int priority;

    public Board(int[] fields, int zeroIndex, int rows, int cols, char lastmove, Board parentBoard, int recDepth, String path) {
        this.fields = fields;
        this.zeroIndex = zeroIndex;
        this.rows = rows;
        this.cols = cols;
        this.lastMove = lastmove;
        this.parentBoard = parentBoard;
        this.recDepth = recDepth;
        this.path = path;
    }

    public Board() {
    }

    public void setFields(int[] fields) {
        this.fields = fields;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getRecDepth() {
        return recDepth;
    }

    public void setRecDepth(int recDepth) {
        this.recDepth = recDepth;
    }

    public String getFields() {
        String string ="";
        for (int i = 0; i < fields.length; i++) {
            string += fields[i] + " ";
        }
        return string;
    }

    public void setZeroIndex(int zeroIndex) {
        this.zeroIndex = zeroIndex;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public Board readBoard(String baseFile) {
        File file = new File(baseFile);
        try{
            Scanner scan = new Scanner(file);
            String line = scan.nextLine();
            String[] tmp = line.split(" ");
            rows = Integer.parseInt(tmp[0]);
            cols = Integer.parseInt(tmp[1]);
            fields = new int[rows * cols];
            for (int i = 0; i < rows; i++) {
                line = scan.nextLine();
                tmp = line.split(" ");
                for (int j = 0; j < cols; j++) {
                    fields[i * cols + j] = Integer.parseInt(tmp[j]);
                    if(Integer.parseInt(tmp[j]) == 0)
                    {
                        zeroIndex = i * cols + j;
                    }
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("No such file");
        } catch (NumberFormatException e) {
            System.out.println("Invalid formatting");
        }
        this.setFields(fields);
        this.setCols(cols);
        this.setRows(rows);
        this.setZeroIndex(zeroIndex);
        return this;
    }

    public boolean isSolved() {
        for (int i = 0; i < fields.length - 1; i++) {
            if(fields[i] != i + 1)
            {
                return false;
            }
        }
        return true;
    }

    public Board makeAMove(char whichWay){
        int newZeroIndex = zeroIndex;
        switch(whichWay){
            case 'R':
                if(lastMove == 'L' || zeroIndex % cols == cols - 1 )
                    break;
                newZeroIndex = zeroIndex + 1;
                break;
            case 'L':
                if (lastMove == 'R' || zeroIndex % cols == 0)
                    break;
                newZeroIndex = zeroIndex - 1;
                break;
            case 'U':
                if (lastMove == 'D' ||zeroIndex < cols )
                    break;
                newZeroIndex = zeroIndex - cols;
                break;
            case 'D':
                if (lastMove == 'U' || zeroIndex >= (rows - 1) * cols)
                    break;
                newZeroIndex = zeroIndex + cols;
        }
        int[] newFields = fields.clone();
        newFields[zeroIndex] = newFields[newZeroIndex];
        newFields[newZeroIndex] = 0;
        Board newBoard = new Board(newFields, newZeroIndex, rows, cols, whichWay, this, recDepth++, path + whichWay );
        return newBoard;
    }
}
