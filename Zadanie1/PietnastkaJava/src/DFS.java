import java.util.*;

public class DFS extends GameSolver {

    String order;
    Board solved;

    public DFS(String order, Board solved) {
        this.order = order;
        this.solved = solved;
    }

//    public String[] solveTheGame() {
//        long startTime = System.nanoTime();
//        String[] result = new String[5];
//        //Checking whether the board was already in order
//        if(solved.isSolved()){
//            float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
//            result[0] = "";
//            result[1] = "0";
//            result[2] = "0";
//            result[3] = "0";
//            result[4] = String.format("%.3f", elapsedTime);
//        }
//        int maxDepth = 0;
//        Deque<Board> stack = new ArrayDeque<>();
//        HashSet<Board> visitedList = new HashSet<>();
//        stack.push(solved);
//
//        while (!stack.isEmpty()) {
//            Board v = stack.pop();
//
//            if (v.recDepth < 20) {
//                if (visitedList.add(v)) {
//                    List<Board> neighbours = this.neighbours(v);
//                    for (int i = neighbours.size() - 1; i >= 0; i--) {
//                        Board neighbour = neighbours.get(i);
//                        if (maxDepth < neighbour.recDepth) {
//                            maxDepth = neighbour.recDepth;
//                        }
//                        if (neighbour.isSolved()) {
//                            float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
//                            result[0] = neighbour.path;
//                            result[1] = String.valueOf(visitedList.size() + stack.size());
//                            result[2] = String.valueOf(visitedList.size());
//                            result[3] = String.valueOf(maxDepth);
//                            result[4] = String.format("%.3f", elapsedTime);
//                            return result;
//                        }
//                        stack.push(neighbour);
//                    }
//                }
//            }
//        }
//        float elapsedTime = (System.nanoTime() - startTime) / 1000000f;
//        result[0] = "-1";
//        result[1] = String.valueOf(visitedList.size());
//        result[2] = String.valueOf(visitedList.size());
//        result[3] = String.valueOf(maxDepth);
//        result[4] = String.format("%.3f", elapsedTime);
//        return result;
//    }

    public String[] solveTheGame() {
        long startTime = System.nanoTime();
        String[] result = new String[5];
        if (this.solved.isSolved()) {
            float elapsedTime = (float)(System.nanoTime() - startTime) / 1000000.0F;
            result[0] = "";
            result[1] = "0";
            result[2] = "0";
            result[3] = "0";
            result[4] = String.format("%.3f", elapsedTime);
        }

        int maxDepth = 0;
        Deque<Board> stack = new ArrayDeque();
        HashMap<Board, Board> visitedList = new HashMap();
        stack.push(this.solved);

        while(true) {
            Board v;
            do {
                do {
                    if (stack.isEmpty()) {
                        float elapsedTime = (float)(System.nanoTime() - startTime) / 1000000.0F;
                        result[0] = "-1";
                        result[1] = String.valueOf(visitedList.size());
                        result[2] = String.valueOf(visitedList.size());
                        result[3] = String.valueOf(maxDepth);
                        result[4] = String.format("%.3f", elapsedTime);
                        return result;
                    }

                    v = (Board)stack.pop();
                } while(v.recDepth >= 20);
            } while(visitedList.containsKey(v) && (!visitedList.containsKey(v) || v.recDepth >= ((Board)visitedList.get(v)).recDepth));

            visitedList.remove(v);
            visitedList.put(v, v);
            List<Board> neighbours = this.neighbours(v);

            for(int i = neighbours.size() - 1; i >= 0; --i) {
                if (maxDepth < ((Board)neighbours.get(i)).recDepth) {
                    maxDepth = ((Board)neighbours.get(i)).recDepth;
                }

                if (((Board)neighbours.get(i)).isSolved()) {
                    float elapsedTime = (float)(System.nanoTime() - startTime) / 1000000.0F;
                    result[0] = ((Board)neighbours.get(i)).path;
                    result[1] = String.valueOf(visitedList.size() + stack.size());
                    result[2] = String.valueOf(visitedList.size());
                    result[3] = String.valueOf(maxDepth);
                    result[4] = String.format("%.3f", elapsedTime);
                    return result;
                }

                stack.push((Board)neighbours.get(i));
            }
        }
    }


    @Override
    public List<Board> neighbours(Board board) {
        List<Board> neighbours = new ArrayList<>();
        for(int i = 0; i < order.length(); i++) {
            Board newNeighbour = board.makeAMove(order.charAt(i));
            if(newNeighbour != null){
                // Modify the existing board object instead of creating a new one
                newNeighbour.setPath(board.getPath() + order.charAt(i));
                newNeighbour.setRecDepth(board.getRecDepth() + 1);
                neighbours.add(newNeighbour);
            }
        }
        return neighbours;
    }
}