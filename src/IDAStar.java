import java.util.*;

public class IDAStar extends Search {

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
