import java.util.*;

public class MCTS {

    private static final int N_SUCESSORS_EXPANDED = 40;
    private static final int N_SIMULATIONS = 10;
    private static final int SIMULATION_DEPTH = 10;
    private static final int MAX_ITERATIONS = 100;
    private final Random random = new Random();

    Iterator<State> solve(ILayout s, ILayout goal) {
        State root = new State(s, null);
        if (root.layout.isGoal(goal)) return root.iterator();
        HashSet<ILayout> closed = new HashSet<>();
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Set<State> expanded = treePolicy(root, closed); // selection and expansion
            for (State state : expanded)
                if (state.layout.isGoal(goal))
                    return state.iterator();
            for (State state : expanded) {
                closed.add(state.layout);
                int result = defaultPolicy(state, goal, closed); // simulation
                backup(state, result); // back propagation
            }
            printTree(root);
        }
        return null;
    }

    private void printTree(State state) {
        System.out.print(state.getF());
        System.out.print("(");
        for (State child : state.children)
            printTree(child);
        System.out.print(")");
    }

    private Set<State> treePolicy(State root, HashSet<ILayout> closed) {
        // Selection
        State cursor = root;
        while (!cursor.children.isEmpty())
            cursor = cursor.cheapestChild();
        // Expansion
        List<State> successors = successors(cursor, closed);
        for (int i = 0; i < N_SUCESSORS_EXPANDED && !successors.isEmpty(); i++) {
            int randomIndex = random.nextInt(successors.size());
            cursor.children.add(successors.get(randomIndex));
            successors.remove(randomIndex);
        }
        return cursor.children;
    }

    private int defaultPolicy(State expanded, ILayout goal, HashSet<ILayout> closed) {
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < N_SIMULATIONS; i++) {
            State cursor = expanded;
            Set<ILayout> closed_sim = new HashSet<>();
            int cost_from_expanded = 0;
            for (int j = 0; j < SIMULATION_DEPTH; j++) {
                List<State> successors = successors(cursor, closed_sim);
                if (successors.isEmpty()) {
                    cost_from_expanded = Integer.MAX_VALUE;
                    break;
                }
                int randomIndex = random.nextInt(successors.size());
                cursor = successors.get(randomIndex);
                cost_from_expanded += cursor.layout.getG();
                if (cursor.layout.isGoal(goal)) break;
                closed_sim.add(cursor.layout);
            }
            result = Math.min(result, cost_from_expanded);
        }
        return result;
    }

    private void backup(State expanded, int sim) {
        int cost_from_expanded = 0;
        State cursor = expanded;
        while (cursor != null) {
            int current_sim = sim + cost_from_expanded;
            if (cursor.sim == -1 || current_sim < cursor.sim)
                cursor.sim = current_sim;
            cost_from_expanded += (int) cursor.layout.getG();
            cursor = cursor.father;
        }
    }

    private List<State> successors(State n, Set<ILayout> closed) {
        List<State> sucs = new ArrayList<>();
        List<ILayout> children = n.layout.children();
        for (ILayout e : children) {
            if ((n.father == null || !e.equals(n.father.layout)) && !closed.contains(e)) {
                State nn = new State(e, n);
                sucs.add(nn);
            }
        }
        return sucs;
    }

    public static class State implements Iterable<State> {
        public ILayout layout;
        public State father;
        public Set<State> children = new HashSet<>();
        public int g = 0;
        public int sim = -1;

        public State(ILayout layout, State father) {
            this.layout = layout;
            this.father = father;
            if (father != null)
                g = father.g + (int) layout.getG();
        }

        public int getF() {
            return sim == -1 ? g : g + sim;
        }

        public State cheapestChild() {
            State result = null;
            for (State child : children) {
                if (result == null || child.getF() < result.getF())
                    result = child;
            }
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return layout.toString();
        }

        /**
         * Returns an iterator from the root to the node.
         *
         * @return an iterator from the root to the node
         */
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

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof State)) return false;
            State s = (State) obj;
            return layout.equals(s.layout);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return layout.toString().hashCode();
        }
    }
}

