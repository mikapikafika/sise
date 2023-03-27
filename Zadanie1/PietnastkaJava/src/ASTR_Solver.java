import java.util.*;

import static java.lang.Math.abs;

public class ASTR_Solver extends GameSolver {

    String heuristic;
    Board solved;

    public ASTR_Solver(String heuristic, Board solved) {
        this.heuristic = heuristic;
        this.solved = solved;
    }

    public String[] solveTheGame() {
        long startTime = System.nanoTime();
        String[] result = new String[5];
        // Checking whether the board was already in order
        if (solved.isSolved()) {
            float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
            result[0] = "";
            result[1] = "0";
            result[2] = "0";
            result[3] = "0";
            result[4] = String.format("%.3f", elapsedTime);
            return result;
        }
        PriorityQueue<Board> newList = new PriorityQueue<>(new ComparatorBoards());
        HashMap<Board, Board> processedList = new HashMap<>();
        newList.add(solved);
        int numOfVisited = 0;

        while (!newList.isEmpty()) {
            Board v = newList.poll();
            if (newList.size() == 0) {
                result[3] = String.valueOf(v.recDepth);
            }
            if (v.isSolved()) {
                float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
                result[0] = v.path;
                result[1] = String.valueOf(numOfVisited);
                result[2] = String.valueOf(processedList.size());
                result[3] = String.valueOf(v.recDepth);
                result[4] = String.format("%.3f", elapsedTime);
                return result;
            }
            processedList.put(v, v);
            List<Board> neighbours = this.neighbours(v);
            for (Board neighbour : neighbours) {
                if (!processedList.containsKey(neighbour)) {
                    int cost = neighbour.recDepth + heuristicValue(neighbour);
                    // Sets priority of the neighbour to the calculated cost
                    if (!newList.contains(neighbour)) {
                        neighbour.priority = cost;
                        newList.add(neighbour);
                        numOfVisited++;
                    } else if (neighbour.priority > cost) {
                        newList.remove(neighbour);
                        neighbour.priority = cost;
                        newList.add(neighbour);
                    }
                }
            }
        }
        float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
        result[0] = "-1";
        result[1] = String.valueOf(numOfVisited);
        result[2] = String.valueOf(processedList.size());
        result[4] = String.format("%.3f", elapsedTime);
        return result;
    }

    @Override
    public List<Board> neighbours(Board board) {
        List<Board> neighbours = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Board newNeighbour = board.makeAMove("RDUL".charAt(i), "RDUL");
            if (newNeighbour != null) {
                neighbours.add(newNeighbour);
            }
        }
        return neighbours;
    }

    public int heuristicValue(Board board) {
        int distance = 0;
        if (heuristic.equals("manh")) {
            int x, correctX, y, correctY;
            // Iterates over fields and sums the distances of each misplaced
            // tile to its correct position
            for (int i = 0; i < board.fields.length - 1; i++) {
                if (board.fields[i] != i + 1 && i != board.zeroIndex) {
                    x = (board.fields[i] - 1) % board.cols;
                    y = (board.fields[i] - 1) / board.cols;
                    correctX = i % board.cols;
                    correctY = i / board.cols;
                    // Distance between the current and corrent positions
                    // along both axes
                    distance += abs(correctX - x) + abs(correctY - y);
                }
            }
        } else if (heuristic.equals("hamm")) {
            // Iterates over fields and increments distance of each misplaced tile
            for (int i = 0; i < board.fields.length - 2; i++) {
                // Distance - comparing the current value and the expected value
                if (board.fields[i] != i + 1) {
                    distance++;
                }
            }
        }
        return distance;
    }
}
