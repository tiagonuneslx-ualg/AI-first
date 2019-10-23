import java.util.ArrayList;
import java.util.List;

/**
 * Grid of numbered pieces with one empty square.
 *
 * <p>The pieces touching the empty square horizontally or vertically can
 * exchange positions with the empty square.
 *
 * @author Rafael Duarte
 * @author Tiago Nunes
 * @see ILayout
 */
public class Board implements ILayout {
    private static final int dim = 3;
    private int[][] board;
    private String origin;

    /**
     * Constructs a board containing the pieces given by the string in the
     * same order. A 0 means an empty square.
     *
     * @param str a string with the pieces' numbers in a certain order
     * @throws IllegalStateException if string length is not dim * dim
     */
    public Board(String str) throws IllegalStateException {
        if (str.length() != dim * dim) throw new
                IllegalStateException("Invalid arg in Board constructor");
        origin = str;
        board = new int[dim][dim];
        int si = 0;
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                board[i][j] = Character.getNumericValue(str.charAt(si++));
    }

    private Board nextBoard(int x1, int y1, int x2, int y2) {
        StringBuilder result_origin = new StringBuilder(origin);
        int i = x1 % dim + y1 * dim;
        int j = x2 % dim + y2 * dim;
        char temp = origin.charAt(i);
        result_origin.setCharAt(i, origin.charAt(j));
        result_origin.setCharAt(j, temp);
        return new Board(result_origin.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ILayout> children() {
        List<ILayout> result = new ArrayList<>();
        int x = 0, y = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] == 0) {
                    x = j;
                    y = i;
                }
            }
        }
        for (int i = x - 1; i <= x + 1; i += 2)
            if (i >= 0 && i < dim)
                result.add(nextBoard(i, y, x, y));
        for (int i = y - 1; i <= y + 1; i += 2)
            if (i >= 0 && i < dim)
                result.add(nextBoard(x, i, x, y));
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGoal(ILayout goal) {
        return equals((Board) goal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Board)) return false;
        Board s = (Board) obj;
        if(board.length != s.board.length) return false;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != s.board[i])
                return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getG() {
        return 1.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getH(ILayout goal) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++)
                output.append(board[i][j] == 0 ? " " : board[i][j]);
            output.append("\n");
        }
        return output.toString();
    }
}
