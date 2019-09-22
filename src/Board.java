import java.util.ArrayList;
import java.util.List;

class Board implements ILayout, Cloneable {
    private static final int dim = 3;
    private int board[][];
    String origin;

    public Board() {
        board = new int[dim][dim];
    }

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
        Board result = new Board(origin);
        int temp = result.board[x1][y1];
        result.board[x1][y1] = result.board[x2][y2];
        result.board[x2][y2] = temp;
        return result;
    }

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
        //TODO: Bug is prolly here
        for (int i = x - 1; i <= x + 1; i += 2)
            if (i >= 0 && i < dim)
                result.add(nextBoard(i, y, x, y));
        for (int i = y - 1; i <= y + 1; i += 2)
            if (i >= 0 && i < dim)
                result.add(nextBoard(x, i, x, y));
        return result;
    }

    @Override
    public boolean isGoal(ILayout l) {
        return equals(l);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Board)) return false;
        Board s = (Board) obj;
        return toString().equals(obj.toString());
    }

    @Override
    public double getG() {
        return 0;
    }

    public String toString() {
        String output = "";
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++)
                output += board[i][j] == 0 ? " " : board[i][j];
            output += "\n";
        }
        return output;
    }
}
