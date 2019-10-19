import java.util.*;

public class IDAStar implements Search<IDAStar.State> {

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

    private List<State> sucessores(State n) {
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

    @Override
    final public Iterator<State> solve(ILayout s, ILayout goal) {
        Stack<State> abertos = new Stack<>();
        HashSet<State> fechados = new HashSet<>();
        int cut = new State(s, null).getF(goal);
        while (true) {
            abertos.push(new State(s, null));
            int nextCut = Integer.MAX_VALUE;
            while (!abertos.isEmpty()) {
                State actual = abertos.pop();
                if (actual.layout.isGoal(goal)) {
                    return actual.iterator();
                }
                fechados.add(actual);
                List<State> sucs = sucessores(actual);
                for (State suc : sucs) {
                    if (!fechados.contains(suc)) {
                        if (suc.getF(goal) <= cut) {
                            abertos.push(suc);
                        } else {
                            int f = suc.getF(goal);
                            if (f < nextCut)
                                nextCut = f;
                        }
                    }
                }
            }
            fechados.clear();
            if (nextCut > cut) {
                cut = nextCut;
            } else return null;
        }
    }
}
