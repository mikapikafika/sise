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
        //Checking whether the board was already in order
        if(solved.isSolved()){
            float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
            result[0] = "";
            result[1] = "0";
            result[2] = "0";
            result[3] = "0";
            result[4] = String.format("%.3f", elapsedTime);
        }
        PriorityQueue<Board> queue = new PriorityQueue<Board>(new ComparatorBoards());
        HashMap<Board, Board> visitedList = new HashMap<>();
        queue.add(solved);
        int visited = 0;

        while (!queue.isEmpty()) {
            Board v = queue.poll();
            if(queue.size() == 0) {
                result[3] = String.valueOf(v.recDepth);
            }
            if (v.isSolved()) {
                float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
                result[0] = v.path;
                result[1] = String.valueOf(visited);
                result[2] = String.valueOf(visitedList.size());
                result[3] = String.valueOf(v.recDepth);
                result[4] = String.format("%.3f", elapsedTime);
                return result;
            }
            visitedList.put(v, v);
            List<Board> neighbours = this.neighbours(v);
            for (int i = 0; i < neighbours.size(); i++) {
                if (!visitedList.containsKey(neighbours.get(i))) {
                    int f = neighbours.get(i).recDepth + heuristicValue(neighbours.get(i));
                    if (!queue.contains(neighbours.get(i))) {
                        neighbours.get(i).priority = f;
                        queue.add(neighbours.get(i));
                        visited++;
                    } else if (neighbours.get(i).priority > f) {
                        queue.remove(neighbours.get(i));
                        neighbours.get(i).priority = f;
                        queue.add(neighbours.get(i));
                    }
                }
            }
        }
        float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
        result[0] = "-1";
        result[1] = String.valueOf(visited);
        result[2] = String.valueOf(visitedList.size());
        result[4] = String.format("%.3f", elapsedTime);
        return result;
    }

    @Override
    public List<Board> neighbours(Board board) {
        List<Board> neighbours = new ArrayList<>();
        for(int i = 0; i < 4; i++)
        {
            Board newNeighbour = board.makeAMove("RDUL".charAt(i));
            if(newNeighbour != null){
                neighbours.add(newNeighbour);
            }
        }
        return neighbours;
    }

    public int heuristicValue(Board board) {
        int distance = 0;
        if (heuristic.equals("manh")) {
            int x1, x2, y1, y2;
            for (int i = 0; i < board.fields.length - 1; i++) {
                if(board.fields[i] != i + 1 && i != board.zeroIndex) {
                    x1 = (board.fields[i] - 1) % board.cols;
                    y1 = (board.fields[i] - 1) / board.cols;
                    x2 = i % board.cols;
                    y2 = i / board.cols;
                    distance += abs(x2 - x1) + abs(y2 - y1);
                }
            }
        } else if (heuristic.equals("hamm")) {
            for (int i = 0; i < board.fields.length - 2; i++) {
                if(board.fields[i] != i + 1) {
                    distance++;
                }
            }
        }
        return distance;
    }
}
