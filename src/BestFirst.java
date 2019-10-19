import java.util.*;

public class BestFirst implements Search<BestFirst.State> {

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

        public int getG() {
            return (int) g;
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

    public static void testBoard() {
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