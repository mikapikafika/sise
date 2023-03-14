import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Board board = new Board();
        GameSolver solver = null;
        try{
            board.readBoard(args[2]);
        } catch (Exception e) {
            System.exit(1);
        }

        switch(args[0]) {
            case "bfs":
                solver = new BFS(args[1], board);
                break;
            case "dfs":
                solver = new DFS(args[1], board);
                break;
            case "astr":
                solver = new Astr(args[1], board);
                break;
            default:
                System.err.println("Unsupported solver: " + args[0]);
                System.exit(2);
        }
        String[] generatedSolution = solver.solveTheGame();
        try {
            writeToFiles(args[3], args[4], generatedSolution);
        } catch (Exception e) {
            System.exit(3);
        }
    }

    public static void writeToFiles(String endFileName, String extraFileName, String[] text){
        try {
            FileWriter writer = new FileWriter(endFileName);
            //If solution was found
            if(!(text[0].equals("-1")))
            {
                //How many steps
                writer.write(String.valueOf(text[0].length()));
                writer.write("\n");
                //Listed steps
                writer.write(text[0]);
            }
            else
            {
                writer.write("-1");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Couldn't write to file" + endFileName);
        }
        try{
            FileWriter writer = new FileWriter(extraFileName);
            //This if works exactly like in the end file but without listing steps
            if(!(text[0].equals("-1")))
            {
                writer.write(String.valueOf(text[0].length()));
            }
            else
            {
                writer.write("-1");
            }
            writer.write("\n");
//            Here it writes visited, processed, recursion depth and time, each in another line
            for (int i = 1; i < text.length; i++) {
                writer.write(text[i]);
                writer.write("\n");
            }
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Couldn't write to file" + extraFileName);
        }
    }
}