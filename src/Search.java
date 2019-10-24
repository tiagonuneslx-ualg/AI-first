import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An algorithm to solve problems by state-space search.
 *
 * @author Rafael Duarte
 * @author Tiago Nunes
 * @see ILayout
 */
public abstract class Search {

    abstract Iterator<State> solve(ILayout s, ILayout goal);

    /**
     * A node for the search algorithm, holding a {@link ILayout},
     * its father, and the accumulated cost from the root to the node.
     */
    public static class State implements Iterable<State> {
        public ILayout layout;
        public State father;
        private int g;
        private double h, f;

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
                this.h = l.getH(goal);
                this.f = h + g;
            }
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
            return (int) h;
        }

        public int getF() {
            return (int) f;
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