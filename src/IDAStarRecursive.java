import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A search algorithm that tests the nodes depth-first iteratively
 * with an incremental cut, using f^(n) as the distance between nodes.
 *
 * <p>The initial cut is f^(root).
 *
 * <p>f^(n) = g(n) + h^(n), where g(n) is the cost of the node n
 * and h^(n) is the heuristic value of the node n.
 *
 * @author Rafael Duarte
 * @author Tiago Nunes
 * @see Search
 * @see ILayout
 */
public class IDAStarRecursive extends Search {

    private int threshold;
    private Set<State> closed;
    private State solution;
    private ILayout goal;
    private boolean success;
    private int result = -1;

    /**
     * {@inheritDoc}
     */
    @Override
    final public Iterator<State> solve(ILayout s, ILayout g) {
        State root = new State(s, null, g);
        threshold = root.getH();
        goal = g;
        solution = null;
        while (true) {
            closed = new HashSet<>();
            State result = search(root);
            if (result.layout.isGoal(g)) return result.iterator();
            if (result.getF() <= threshold) return null;
            else threshold = result.getF();
        }
    }

    private State search(State node) {
        closed.add(node);
        if (solution != null) return solution;
        if (node.getF() > threshold) return node;
        if (node.layout.isGoal(goal)) {
            solution = node;
            return node;
        }
        State min = null;
        for (State suc : sucessores(node, goal)) {
            if (!closed.contains(suc)) {
                State temp = search(suc);
                min = minF(min, temp);
            }
        }
        return min;
    }

    private State minF(State a, State b) {
        if (a == null) return b;
        return a.getF() < b.getF() ? a : b;
    }

    @Override
    protected int solveD(ILayout s, ILayout goal) {
        State root = new State(s, null, goal);
        int threshold = root.getH();
        int f = threshold;
        do {
            Set<State> closed = new HashSet<>();
            threshold = f;
            f = searchD(root, threshold, goal, closed);
        } while (f > threshold);
        return f;
    }

    private int searchD(State node, int threshold, ILayout goal, Set<State> closed) {
        closed.add(node);
        if (success) return result;
        int f = node.getF();
        if (f > threshold)
            return f;
        if (node.layout.isGoal(goal)) {
            result = f;
            success = true;
            return f;
        }
        int min = Integer.MAX_VALUE;
        for (State suc : sucessores(node, goal)) {
            if (!closed.contains(suc)) {
                int m = searchD(suc, threshold, goal, closed);
                min = Math.min(m, min);
            }
        }
        return min;
    }
}