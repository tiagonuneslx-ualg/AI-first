import java.util.*;

/**
 * A search algorithm that tests the least f^(n) nodes first.
 *
 * <p>f^(n) = g(n) + h^(n), where g(n) is the cost of the node n
 * and h^(n) is the heuristic value of the node n.
 *
 * @author Rafael Duarte
 * @author Tiago Nunes
 * @see Search
 * @see ILayout
 */
public class AStar extends Search {

    private static final int N_SIMULATIONS = 30;
    private static final int SIMULATION_DEPTH = 10;


    /**
     * {@inheritDoc}
     */
    @Override
    final public Iterator<State> solve(ILayout s, ILayout goal) {
        Queue<State> abertos = new PriorityQueue<>(10, (s1, s2) -> (int) Math.signum(s1.getF() - s2.getF()));
        HashSet<State> fechados = new HashSet<>();
        abertos.offer(new State(s, null, goal));
        List<State> sucs;
        while (!abertos.isEmpty()) {
            State actual = abertos.poll();
            if (actual.layout.isGoal(goal))
                return actual.iterator();
            sucs = sucessores(actual, goal);
            fechados.add(actual);
            for (State suc : sucs) {
                if (!fechados.contains(suc))
                    abertos.add(suc);
            }
        }
        return null;
    }
}