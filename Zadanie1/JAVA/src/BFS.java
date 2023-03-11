import java.util.ArrayList;
import java.util.List;

public class BFS extends GameSolver{

    String order;
    Board solved;

    public BFS(String order, Board solved) {
        this.order = order;
        this.solved = solved;
    }

    public String[] solveTheGame(){
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
        int recDepth = 0;

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
