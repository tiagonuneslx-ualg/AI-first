import java.util.*;

public class BestFirst {
    static class State implements Iterable<State> {
        private ILayout layout;
        private State father;
        private double g;

        public State(ILayout l, State n) {
            layout = l;
            father = n;
            g = l.getG();
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
            while(actual != null) {
                states.add(actual);
                actual = actual.father;
            }
            Collections.reverse(states);
            return states.iterator();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) return true;
            if(!(obj instanceof State)) return false;
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