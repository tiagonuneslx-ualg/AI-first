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
public class IDAStar extends Search {

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
    final public Iterator<State> solve(ILayout s, ILayout goal) {
        int generated = 0;
        int tested = 0;
        int iterations = 0;
        Stack<State> abertos = new Stack<>();
        int cut = new State(s, null).getH(goal);
        while(true) {
            iterations++;
            HashSet<State> fechados = new HashSet<>();
            abertos.push(new State(s, null));
            int nextCut = Integer.MAX_VALUE;
            while(!abertos.isEmpty()) {
                tested++;
                State actual = abertos.pop();
                if(actual.layout.isGoal(goal)) {
                    System.out.println("Generated: " + generated + " Tested: " + tested + " Iterations: " + iterations);
                    return actual.iterator();
                }
                fechados.add(actual);
                List<State> sucs = sucessores(actual);
                for(State suc : sucs) {
                    generated++;
                    if(!fechados.contains(suc)) {
                        int f = suc.getF(goal);
                        if(f <= cut)
                            abertos.push(suc);
                        else if(f < nextCut)
                            nextCut = f;
                    }
                }
            }
            if(nextCut > cut) {
                cut = nextCut;
            } else return null;
        }
    }
}