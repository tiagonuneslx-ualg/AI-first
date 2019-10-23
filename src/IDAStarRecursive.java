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

    private List<State> sucessores(State n) {
        List<State> result = new ArrayList<>();
        List<ILayout> children = n.layout.children();
        for(ILayout e : children) {
            if(n.father == null || !e.equals(n.father.layout)) {
                State nn = new State(e, n);
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
        State root = new State(s, null);
        cut = root.getH(goal);
        while(true) {
            nextCut = Integer.MAX_VALUE;
            Set<State> closed = new HashSet<>();
            State temp = search(root, closed);
            if(temp != null) return temp.iterator();
            if(nextCut > cut) cut = nextCut;
            else return null;
        }
    }

    private State search(State node, Set<State> closed) {
        int f = node.getF(goal);
        if(f > cut) {
            if(f < nextCut) nextCut = f;
            return null;
        }
        if(node.layout.isGoal(goal)) return node;
        closed.add(node);
        for(State suc : sucessores(node)) {
            if(!closed.contains(suc)) {
                State temp = search(suc, closed);
                if(temp != null) return temp;
            }
        }
        return null;
    }
}