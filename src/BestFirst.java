import java.util.*;

public class BestFirst {
    static class State implements Iterable<State> {
        private ILayout layout;
        private State father;
        private double g; // Sum of individual operation costs
        private double h;

        public State(ILayout l, State n) {
            layout = l;
            father = n;
            if (father != null)
                g = father.g + l.getG();
            else g = 0.0;
        }

        public State(ILayout l, State n, ILayout goal) {
            layout = l;
            father = n;
            if (father != null)
                g = father.g + l.getG();
            else g = 0.0;
            h = l.getH(goal);
        }

        public String toString() {
            return layout.toString();
        }

        public int getG() {
            return (int) g;
        }

        public int getH() {
            return (int) h;
        }

        public int getF() {
            return getG() + getH();
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

    final private List<State> sucessores(State n, ILayout goal) {
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

    final public Iterator<State> solve(ILayout s, ILayout goal) {
        objective = goal;
        Queue<State> abertos = new PriorityQueue<>(10, (s1, s2) -> (int) Math.signum(s1.getG() - s2.getG()));
        List<State> fechados = new ArrayList<>();
        abertos.offer(new State(s, null));
        List<State> sucs;
        while (!abertos.isEmpty()) {
            State actual = abertos.poll();
            if (actual.layout.isGoal(goal))
                return actual.iterator();
            sucs = sucessores(actual);
            fechados.add(actual);
            for (State suc : sucs) {
                if (!fechados.contains(suc))
                    abertos.add(suc);
            }
        }
        return null;
    }

    final public Iterator<State> solveIDA(ILayout s, ILayout goal) {
        int counter_abertos = 0, counter_iterations = 0;
        objective = goal;
        Stack<State> abertos = new Stack<>();
        int cut = new State(s, null, goal).getF();
        while (true) {
            abertos.push(new State(s, null, goal));
            int nextCut = Integer.MAX_VALUE;
            while (!abertos.isEmpty()) {
                State actual = abertos.pop();
                if (actual.layout.isGoal(goal)) {
                    System.out.println("Iterations: " + counter_iterations + "  Testados: " + counter_abertos);
                    return actual.iterator();
                }
                List<State> sucs = sucessores(actual, goal);
                for (State suc : sucs) {
                    if (suc.getF() <= cut) {
                        abertos.push(suc);
                        counter_abertos++;
                    } else {
                        int f = suc.getF();
                        if (f < nextCut)
                            nextCut = f;
                    }
                }
            }
            if (nextCut > cut) {
                cut = nextCut;
                counter_iterations++;
            } else return null;
        }
    }
}