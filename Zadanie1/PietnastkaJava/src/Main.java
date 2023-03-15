import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        // Read board from file
        Board board = new Board();
        try {
            board.readBoard(args[2]);
        } catch (Exception e) {
            System.exit(1);
        }

        // Enhanced switch to choose solving algorithm
        GameSolver solver = switch (args[0]) {
            case "bfs" -> new BFS(args[1], board);
            case "dfs" -> new DFS(args[1], board);
            case "astr" -> new Astr(args[1], board);
            default -> {
                System.err.println("Unsupported solver: " + args[0]);
                System.exit(2);
                yield null;
            }
        };

        // Write solution to files
        String[] generatedSolution = solver.solveTheGame();
        try {
            writeToFiles(args[3], args[4], generatedSolution);
        } catch (Exception e) {
            System.exit(3);
        }
    }

    public static void writeToFiles(String endFileName, String extraFileName, String[] text) {
        // Try with resources automatically closes files
        try (FileWriter endFileWriter = new FileWriter(endFileName);
             FileWriter extraFileWriter = new FileWriter(extraFileName)) {

            // End file:
            // If solution was found (there's no "-1")
            if (!(text[0].equals("-1"))) {
                // How many steps it took
                endFileWriter.write(String.valueOf(text[0].length()));
                endFileWriter.write("\n");
                // Listed steps (letters)
                endFileWriter.write(text[0]);
            } else {
                endFileWriter.write("-1");
            }

            // Extra file:
            // This if works exactly like in the end file but without listing steps
            if (!(text[0].equals("-1"))) {
                extraFileWriter.write(String.valueOf(text[0].length()));
            } else {
                extraFileWriter.write("-1");
            }
            extraFileWriter.write("\n");
            // Here it writes visited, processed, recursion depth and time, each in another line
            for (int i = 1; i < text.length; i++) {
                extraFileWriter.write(text[i]);
                extraFileWriter.write("\n");
            }

        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }
    }
}