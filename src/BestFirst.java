import java.util.*;

/**
 * A search algorithm that tests the least cost nodes first.
 *
 * @author Rafael Duarte
 * @author Tiago Nunes
 * @see Search
 * @see ILayout
 */
public class BestFirst extends Search {

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

    /**
     * {@inheritDoc}
     */
    @Override
    final public Iterator<State> solve(ILayout s, ILayout goal) {
        Queue<State> abertos = new PriorityQueue<>(10, (s1, s2) -> (int) Math.signum(s1.getG() - s2.getG()));
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