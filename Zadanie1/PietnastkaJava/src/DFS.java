import java.util.*;

public class DFS extends GameSolver {

    String order;
    Board solved;

    public DFS(String order, Board solved) {
        this.order = order;
        this.solved = solved;
    }

    @Override
    public String[] solveTheGame() {
        long startTime = System.nanoTime();
        String[] result = new String[5];

        // Checking whether the board was already in order
        if (this.solved.isSolved()) {
            float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
            result[0] = "";
            result[1] = "0";
            result[2] = "0";
            result[3] = "0";
            result[4] = String.format("%.3f", elapsedTime);
            return result;
        }

        int recDepthCounter = 0;
        int MAX_DEPTH = 20;
        // Untouched discovered states
        Deque<Board> newList = new ArrayDeque<>();
        Set<Board> processedList = new HashSet<>();
        // Push initial state into deque
        newList.push(this.solved);

        while (true) {
            Board v;
            do {
                if (newList.isEmpty()) {
                    float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
                    result[0] = "-1";
                    result[1] = String.valueOf(processedList.size());
                    result[2] = String.valueOf(processedList.size());
                    result[3] = String.valueOf(recDepthCounter);
                    result[4] = String.format("%.3f", elapsedTime);
                    return result;
                }
                // Pops the top board object
                v = newList.pop();
            } while (v.getRecDepth() >= MAX_DEPTH || processedList.contains(v));

            // To not process the same board twice
            processedList.remove(v);
            // To keep track of processed states
            processedList.add(v);
            // To generate neighbours
            List<Board> neighbours = this.neighbours(v);

            for (Board neighbour : neighbours) {
                if (recDepthCounter < neighbour.getRecDepth()) {
                    recDepthCounter = neighbour.getRecDepth();
                }

                if (neighbour.isSolved()) {
                    float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
                    result[0] = neighbour.getPath();                    // path length
                    result[1] = String.valueOf(processedList.size() + newList.size()); // visited states
                    result[2] = String.valueOf(processedList.size());   // processed states
                    result[3] = String.valueOf(recDepthCounter);        // max rec depth
                    result[4] = String.format("%.3f", elapsedTime);     // time in ms
                    return result;
                }

                newList.push(neighbour);
            }
        }
    }


    @Override
    public List<Board> neighbours(Board board) {
        List<Board> neighbours = new ArrayList<>();
        for (int i = 0; i < order.length(); i++) {
            // Makes a move and creates a new board with that move
            Board newNeighbour = board.makeAMove(order.charAt(i));
            // If the move was valid
            if (newNeighbour != null) {
                // Modifies the existing board object instead of creating a new one
                newNeighbour.setPath(board.getPath() + order.charAt(i));
                newNeighbour.setRecDepth(board.getRecDepth() + 1);
                neighbours.add(newNeighbour);
            }
        }
        // Returns list of neighbouring boards
        return neighbours;
    }
}