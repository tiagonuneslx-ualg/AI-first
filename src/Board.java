import java.util.List;

class Board implements ILayout, Cloneable {
    private static final int dim = 3;
    private int board[][];

    public Board() {
        board = new int[dim][dim];
    }

    public Board(String str) throws IllegalStateException {
        if (str.length() != dim * dim) throw new
                IllegalStateException("Invalid arg in Board constructor");
        board = new int[dim][dim];
        int si = 0;
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                board[i][j] = Character.getNumericValue(str.charAt(si++));
    }

    @Override
    public List<ILayout> children() {
        return null;
    }

    @Override
    public boolean isGoal(ILayout l) {
        return false;
    }

    @Override
    public double getG() {
        return 0;
    }

    public String toString(){
        String output="";
        for(int i = 0 ; i<dim ; i++) {
            for (int j = 0; j<dim; j++)
                output += board[i][j] == 0 ? " " : board[i][j];
            output += "\n";
        }
        return output;
    }
}
