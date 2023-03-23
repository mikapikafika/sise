import java.util.*;

public class DFS_Solver extends GameSolver {

    String order;
    Board solved;

    public DFS_Solver(String order, Board solved) {
        this.order = order;
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

        int maxDepth = 0;
        Deque<Board> newList = new ArrayDeque<>();
        HashMap<Board, Board> processedList = new HashMap<>();
        newList.push(solved);

        while (!newList.isEmpty()) {
            Board v = newList.pop();
            if (v.recDepth < 20) {
                if (!processedList.containsKey(v) || (processedList.containsKey(v)
                                    && v.recDepth < processedList.get(v).recDepth)) {
                    processedList.remove(v);
                    processedList.put(v, v);
                    List<Board> neighbours = this.neighbours(v);
                    for (int i = neighbours.size() - 1; i >= 0; i--) {
                        if (maxDepth < neighbours.get(i).recDepth) {
                            maxDepth = neighbours.get(i).recDepth;
                        }
                        if (neighbours.get(i).isSolved()) {
                            float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
                            result[0] = neighbours.get(i).path;
                            result[1] = String.valueOf(processedList.size() + newList.size());
                            result[2] = String.valueOf(processedList.size());
                            result[3] = String.valueOf(maxDepth);
                            result[4] = String.format("%.3f", elapsedTime);
                            return result;
                        }
                        newList.push(neighbours.get(i));
                    }
                }
            }
        }
        float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
        result[0] = "-1";
        result[1] = String.valueOf(processedList.size());
        result[2] = String.valueOf(processedList.size());
        result[3] = String.valueOf(maxDepth);
        result[4] = String.format("%.3f", elapsedTime);
        return result;
    }


    @Override
    public List<Board> neighbours(Board board) {
        List<Board> neighbours = new ArrayList<>();
        for (int i = 0; i < order.length(); i++) {
            Board newNeighbour = board.makeAMove(order.charAt(i), order);
            if (newNeighbour != null) {
                neighbours.add(newNeighbour);
            }
        }
        return neighbours;
    }
}