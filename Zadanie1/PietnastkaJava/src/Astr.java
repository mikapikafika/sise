import java.util.*;

import static java.lang.Math.abs;

public class Astr extends GameSolver {

    String heuristic;
    Board solved;

    public Astr(String heuristic, Board solved) {
        this.heuristic = heuristic;
        this.solved = solved;
    }


    @Override
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

        int numOfVisited = 0;
        // Current cost and estimated cost to the goal state
        PriorityQueue<Board> newList = new PriorityQueue<Board>(new ComparatorBoards());
        Set<Board> processedList = new HashSet<>();
        newList.add(solved);

        while (!newList.isEmpty()) {
            // Poll removes the head of the queue and returns it
            // (board with the lowest priority)
            Board v = newList.poll();
            // If it's empty, the algorithm can't find a solution
            if (newList.size() == 0) {
                result[3] = String.valueOf(v.getRecDepth());
            }

            if (v.isSolved()) {
                float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
                result[0] = v.getPath();                            // path length
                result[1] = String.valueOf(numOfVisited);           // visited states
                result[2] = String.valueOf(processedList.size());   // processed states
                result[3] = String.valueOf(v.getRecDepth());        // max rec depth
                result[4] = String.format("%.3f", elapsedTime);     // time in ms
                return result;
            }

            processedList.add(v);
            List<Board> neighbours = this.neighbours(v);
            for (Board neighbour : neighbours) {
                if (!processedList.contains(neighbour)) {
                    // Calculates the cost of moving from the current state to the neighbour
                    int cost = neighbour.getRecDepth() + heuristicValue(neighbour);
                    // Adding the neighbour with its cost
                    if (!newList.contains(neighbour)) {
                        neighbour.priority = cost;
                        newList.add(neighbour);
                        numOfVisited ++;
                    } else if (neighbour.priority > cost) {
                        // Updating the cost
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
            Board newNeighbour = board.makeAMove("RDUL".charAt(i));
            if (newNeighbour != null) {
                // Modifies the existing board object instead of creating a new one
                newNeighbour.setPath(board.getPath() + "RDUL".charAt(i));
                newNeighbour.setRecDepth(board.getRecDepth() + 1);
                neighbours.add(newNeighbour);
            }
        }
        return neighbours;
    }

    public int heuristicValue(Board board) {
        int distance = 0;
        // Manhattan
        if (heuristic.equals("manh")) {
            int y, correctY, x, correctX;
            // Iterating over fields and summing the distances of each misplaced
            // tile to its correct position
            for (int i = 0; i < board.fields.length - 1; i++) {
                if (board.fields[i] != i + 1 && i != board.zeroIndex) {
                    y = (board.fields[i] - 1) / board.cols;
                    x = (board.fields[i] - 1) % board.cols;
                    correctY = i / board.cols;
                    correctX = i % board.cols;
                    // Absolute difference between the horizontal and vertical position
                    distance += abs(correctX - x) + abs(correctY - y);
                }
            }
        // Hamming:
        } else if (heuristic.equals("hamm")) {
            // Iterating over fields and incrementing distance of each misplaced tile
            for (int i = 0; i < board.fields.length - 2; i++) {
                if(board.fields[i] != i + 1) {
                    distance++;
                }
            }
        }
        return distance;
    }
}
