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
        //Checking whether the board was already in order
        if(solved.isSolved()){
            float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
            result[0] = "";
            result[1] = "0";
            result[2] = "0";
            result[3] = "0";
            result[4] = String.format("%.3f", elapsedTime);
        }
        int maxDepth = 0;
        Deque<Board> stack = new ArrayDeque<>();
        HashMap<Board, Board> visitedList = new HashMap<>();
        stack.push(solved);

        while (!stack.isEmpty()) {
            Board v = stack.pop();
            if (v.recDepth < 20) {
                if (!visitedList.containsKey(v) || (visitedList.containsKey(v)
                                    && v.recDepth < visitedList.get(v).recDepth)) {
                    visitedList.remove(v);
                    visitedList.put(v, v);
                    List<Board> neighbours = this.neighbours(v);
                    for (int i = neighbours.size() - 1; i >= 0; i--) {
                        if (maxDepth < neighbours.get(i).recDepth) {
                            maxDepth = neighbours.get(i).recDepth;
                        }
                        if (neighbours.get(i).isSolved()) {
                            float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
                            result[0] = neighbours.get(i).path;
                            result[1] = String.valueOf(visitedList.size() + stack.size());
                            result[2] = String.valueOf(visitedList.size());
                            result[3] = String.valueOf(maxDepth);
                            result[4] = String.format("%.3f", elapsedTime);
                            return result;
                        }
                        stack.push(neighbours.get(i));
                    }
                }
            }
        }
        float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
        result[0] = "-1";
        result[1] = String.valueOf(visitedList.size());
        result[2] = String.valueOf(visitedList.size());
        result[3] = String.valueOf(maxDepth);
        result[4] = String.format("%.3f", elapsedTime);
        return result;
    }


    @Override
    public List<Board> neighbours(Board board) {
        List<Board> neighbours = new ArrayList<>();
        for(int i = 0; i < order.length(); i++)
        {
            Board newNeighbour = board.makeAMove(order.charAt(i));
            if(newNeighbour != null){
                neighbours.add(newNeighbour);
            }
        }
        return neighbours;
    }
}