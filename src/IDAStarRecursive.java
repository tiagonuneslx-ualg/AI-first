import java.util.*;

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

    private int cut, nextCut;
    private ILayout goal;
    private int tested;
    private boolean success;
    private int result = -1;

    private List<State> sucessores(State n, ILayout goal) {
        List<State> result = new ArrayList<>();
        List<ILayout> children = n.layout.children();
        for (ILayout e : children) {
            if (n.father == null || !e.equals(n.father.layout)) {
                State nn = new State(e, n, goal);
                result.add(nn);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public Iterator<State> solve(ILayout s, ILayout g) {
        goal = g;
        State root = new State(s, null, goal);
        cut = root.getH();
        while (true) {
            nextCut = Integer.MAX_VALUE;
            Set<State> closed = new HashSet<>();
            State temp = search(root, closed);
            if (temp != null) {
                System.out.println("Tested: " + tested);
                return temp.iterator();
            }
            if (nextCut > cut) cut = nextCut;
            else return null;
        }
    }

    private State search(State node, Set<State> closed) {
        int f = node.getF();
        if (f > cut) {
            if (f < nextCut) nextCut = f;
            return null;
        }
        tested++;
        if (node.layout.isGoal(goal)) return node;
        closed.add(node);
        for (State suc : sucessores(node, goal)) {
            if (!closed.contains(suc)) {
                State temp = search(suc, closed);
                if (temp != null) return temp;
            }
        }
        return null;
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