import java.util.*;

/**
 * An algorithm to solve problems by state-space search.
 *
 * @author Rafael Duarte
 * @author Tiago Nunes
 * @see ILayout
 */
public abstract class Search {

    abstract Iterator<State> solve(ILayout s, ILayout goal);

    protected int solveD(ILayout s, ILayout goal) {
        return 0;
    }

    // Successors with heuristics
    protected List<State> sucessores(State n, ILayout goal) {
        List<State> sucs = new ArrayList<>();
        List<ILayout> children = n.layout.children();
        for (ILayout e : children) {
            if (n.father == null || !e.equals(n.father.layout)) {
                State nn = new State(e, n, goal);
                sucs.add(nn);
            }
        }
        return sucs;
    }

    // Successors without heuristics
    protected List<State> sucessores(State n) {
        List<State> sucs = new ArrayList<>();
        List<ILayout> children = n.layout.children();
        for (ILayout e : children) {
            if (n.father == null || !e.equals(n.father.layout)) {
                State nn = new State(e, n, null);
                sucs.add(nn);
            }
        }
        return sucs;
    }

    /**
     * A node for the search algorithm, holding a {@link ILayout},
     * its father, and the accumulated cost from the root to the node.
     */
    public static class State implements Iterable<State> {
        public ILayout layout;
        public State father;
        private int g;
        private ILayout goal;

        /**
         * @param l a config
         * @param n the previous node
         */
        public State(ILayout l, State n, ILayout goal) {
            layout = l;
            father = n;
            if (father != null)
                g = father.g + (int) l.getG();
            else g = 0;
            if (goal != null) {
                this.goal = goal;
            }
        }

        public State randomSuccessor(Set<State> closed) {
            State result = new State(layout.randomChild(), this, goal);
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return layout.toString();
        }

        public int getG() {
            return g;
        }

        public int getH() {
            return (int) layout.getH(goal);
        }

        public int getF() {
            return (int) getG() + getH();
        }

        /**
         * Returns an iterator from the root to the node.
         *
         * @return an iterator from the root to the node
         */
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

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof State)) return false;
            State s = (State) obj;
            return layout.equals(s.layout);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return layout.toString().hashCode();
        }
    }
}