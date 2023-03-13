import java.util.Comparator;

public class ComparatorBoards implements Comparator<Board> {
    @Override
    public int compare(Board b1, Board b2) {
        if (b1.priority > b2.priority) {
            return 1;
        } else if (b1.priority < b2.priority) {
            return -1;
        }
        return 0;
    }
}