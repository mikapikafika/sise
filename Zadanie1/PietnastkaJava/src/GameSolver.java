import java.util.List;

public abstract class GameSolver {
    public abstract String[] solveTheGame();
    public abstract List<Board> neighbours(Board board);
}
