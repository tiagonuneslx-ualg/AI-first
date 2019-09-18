import java.util.*;

public class BestFirst {
    static class State {
        private ILayout layout;
        private State father;
        private double g;

        public State(ILayout l, State n) {
            layout = l;
            father = n;
            if (father != null) g = father.g + l.getG();
            else g = 0.0;
        }

        public String toString() {
            return layout.toString();
        }

        public double getG() {
            return g;
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
        // TO BE COMPLETED}
        return null;
    }
}