import java.util.*;

public class BFS_Solver extends GameSolver{

    String order;
    Board solved;

    public BFS_Solver(String order, Board solved) {
        this.order = order;
        this.solved = solved;
    }

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
        Queue<Board> newList = new LinkedList<>();                  //This list is for not visited yet neighbours
        HashMap<Board, Board> visitedList = new HashMap<>();
        newList.add(solved);
        visitedList.put(solved, solved);

        while (!newList.isEmpty()){
            // Poll removes the head of the queue and returns it
            Board v = newList.poll();
            if (newList.size() == 0) {
                result[3] = String.valueOf(v.recDepth);
            }
            numOfProcessed++;
            List<Board> neighbours = neighbours(v);                 //This adds to list all neighbours of v. Neighbour is a new state of board after
                                                                    //making a move
            for (Board neighbour : neighbours) {
                if (neighbour.isSolved()) {
                    float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
                    result[0] = neighbour.path;
                    result[1] = String.valueOf(visitedList.size() + 1);
                    result[2] = String.valueOf(numOfProcessed);
                    result[3] = String.valueOf(neighbour.recDepth);
                    result[4] = String.format("%.3f", elapsedTime);
                    return result;
                }
                if (!visitedList.containsKey(neighbour)) {          //This checks whether the neighbour was already visited. If not, it's added to
                                                                    //list for processing it later
                    newList.add(neighbour);
                    visitedList.put(neighbour, neighbour);
                }
            }
        }
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
            Board newNeighbour = board.makeAMove(order.charAt(i), order);
            if (newNeighbour != null) {
                neighbours.add(newNeighbour);
            }
        }
        return neighbours;
    }
}
