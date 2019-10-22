import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class Search {

    abstract Iterator<State> solve(ILayout s, ILayout goal);

    public static class State implements Iterable<State> {
        public ILayout layout;
        public State father;
        private double g;

        public State(ILayout l, State n) {
            layout = l;
            father = n;
            if (father != null)
                g = father.g + l.getG();
            else g = 0.0;
        }

        @Override
        public String toString() {
            return layout.toString();
        }

        public int getG() {
            return (int) g;
        }

        public int getH(ILayout goal) {
            return (int) layout.getH(goal);
        }

        public int getF(ILayout goal) {
            return getG() + getH(goal);
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

        @Override
        public int hashCode() {
            return layout.toString().hashCode();
        }
    }

}