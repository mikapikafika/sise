import java.util.List;

public abstract class GameSolver {
    Board solved;

    public abstract String[] solveTheGame();
    public abstract List<Board> neighbours(Board board);
}
