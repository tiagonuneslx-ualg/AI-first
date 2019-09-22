package single;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BestFirst s = new BestFirst();
        Iterator<BestFirst.State> it = s.solve(new Board(sc.next()), new Board(sc.next()));
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                BestFirst.State i = it.next();
                System.out.println(i);
                if (!it.hasNext())
                    System.out.println(i.getG());
            }
        }
        sc.close();
    }
}

class BestFirst {
    static class State implements Iterable<State> {
        private ILayout layout;
        private State father;
        private double g;

        public State(ILayout l, State n) {
            layout = l;
            father = n;
            if (father != null)
                g = father.g + l.getG();
            else g = 0.0;
        }

        public String toString() {
            return layout.toString();
        }

        public double getG() {
            return g;
        }

        @Override
        public Iterator<State> iterator() {
            List<State> states = new ArrayList<>();
            State actual = this;
            while (actual != null) {
                states.add(actual);
                actual = actual.father;
            }
            Collections.reverse(states);
            return states.iterator();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof State)) return false;
            State s = (State) obj;
            return layout.equals(s.layout);
        }
    }

    protected Queue<State> abertos;
    private List<State> fechados;
    private State actual;
    private ILayout objective;

    final private List<State> sucessores(State n) {
        List<State> sucs = new ArrayList<>();
        List<ILayout> children = n.layout.children();
        for (ILayout e : children) {
            if (n.father == null || !e.equals(n.father.layout)) {
                State nn = new State(e, n);
                sucs.add(nn);
            }
        }
        return sucs;
    }

    final public Iterator<State> solve(ILayout s, ILayout goal) {
        objective = goal;
        Queue<State> abertos = new PriorityQueue<>(10, (s1, s2) -> (int) Math.signum(s1.getG() - s2.getG()));
        List<State> fechados = new ArrayList<>();
        abertos.add(new State(s, null));
        List<State> sucs;
        while (true) {
            if (abertos.isEmpty())
                return null;
            State actual = abertos.remove();
            if (actual.layout.isGoal(goal))
                return actual.iterator();
            sucs = sucessores(actual);
            fechados.add(actual);
            for (State suc : sucs) {
                if (!fechados.contains(suc))
                    abertos.add(suc);
            }
        }
    }
}

class Board implements ILayout {
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
        StringBuilder result_origin = new StringBuilder(origin);
        int i = x1 % dim + y1 * dim;
        int j = x2 % dim + y2 * dim;
        char temp = origin.charAt(i);
        result_origin.setCharAt(i, origin.charAt(j));
        result_origin.setCharAt(j, temp);
        return new Board(result_origin.toString());
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
        return equals((Board) l);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Board)) return false;
        Board s = (Board) obj;
        return toString().equals(s.toString());
    }

    @Override
    public double getG() {
        return 1.0;
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


interface ILayout {
    /**
     * @return the children of the receiver.
     */
    List<ILayout> children();

    /**
     * @return true if the receiver equals the argument l;
     * return false otherwise.
     */
    boolean isGoal(ILayout l);

    /**
     * @return the cost for moving from the input config to the receiver.
     */
    double getG();
}
