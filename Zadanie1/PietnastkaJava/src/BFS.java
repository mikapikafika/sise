import java.util.*;

public class BFS extends GameSolver{

    String order;
    Board solved;

    public BFS(String order, Board solved) {
        this.order = order;
        this.solved = solved;
    }

    @Override
    public String[] solveTheGame(){
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

        int numOfProcessed = 0;
        // Untouched discovered states
        Queue<Board> newList = new LinkedList<>();
        // Set to keep track of visited states
        Set<Board> visitedList = new HashSet<>();
        newList.add(solved);
        visitedList.add(solved);

        while (!newList.isEmpty()){
            // Poll removes the head of the queue and returns it
            Board v = newList.poll();
            if (newList.size() == 0) {
                result[3] = String.valueOf(v.getRecDepth());
            }
            // Examining the neighbours
            numOfProcessed++;
            for (Board neighbour : neighbours(v)) {
                if (neighbour.isSolved()) {
                    float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
                    result[0] = neighbour.getPath();                        // path length
                    result[1] = String.valueOf(visitedList.size() + 1);  // visited states
                    result[2] = String.valueOf(numOfProcessed);            // processed states
                    result[3] = String.valueOf(neighbour.getRecDepth());   // max rec depth
                    result[4] = String.format("%.3f", elapsedTime);        // time in ms
                    return result;
                }
                // If the neighbour hasn't been visited (add = true),
                // add to discovered states
                if (visitedList.add(neighbour)) {
                    newList.add(neighbour);
                }
            }
        }

        // No solution found
        float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
        result[0] = "-1";
        result[1] = String.valueOf(visitedList.size());
        result[2] = String.valueOf(numOfProcessed);
        result[4] = String.format("%.3f", elapsedTime);
        return result;
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
