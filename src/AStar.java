import java.util.*;

public class AStar extends Search {

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
        Queue<State> abertos = new PriorityQueue<>(10, (s1, s2) -> (int) Math.signum(s1.getF(goal) - s2.getF(goal)));
        HashSet<State> fechados = new HashSet<>();
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
}