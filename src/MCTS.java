import java.util.*;

/**
 * A naïf variant of the Monte Carlo Search Algorithm that works
 * for single agent problems.
 *
 * @author Rafael Duarte
 * @author Tiago Nunes
 * @see ILayout
 */
public class MCTS {
    private static final int N_SUCESSORS_EXPANDED = 50;
    private static final int N_SIMULATIONS = 300;
    private static final int SIMULATION_DEPTH = 30;
    private static final int MAX_ITERATIONS = 100;
    private final Random random = new Random();

    /**
     * Returns the path from initial to goal
     *
     * @param initial the initial layout
     * @param goal    the objective layout
     * @return the path from initial to goal
     */
    Iterator<Node> solve(ILayout initial, ILayout goal) {
        Node root = new Node(initial);
        Set<Node> closed = new HashSet<>();
        if (root.layout.isGoal(goal)) return root.iterator();
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Node selected = selection(root, closed);
            if (selected.layout.isGoal(goal)) return selected.iterator();
            Set<Node> expanded = expand(selected, closed, goal);
            for (Node node : expanded) {
                node.sim = simulation(node, goal, closed);
            }
            backpropagation(selected);
        }
        return selection(root, closed).iterator();
    }

    private Node selection(Node root, Set<Node> closed) {
        Node result = root;
        while (!result.children.isEmpty()) {
            result = result.cheapestChild();
        }
        closed.add(result);
        return result;
    }

    private Set<Node> expand(Node selected, Set<Node> closed, ILayout goal) {
        List<Node> successors = selected.successors(closed, goal);
        for (int i = 0; i < N_SUCESSORS_EXPANDED && !successors.isEmpty(); i++) {
            int randomIndex = random.nextInt(successors.size());
            selected.children.add(successors.get(randomIndex));
            successors.remove(randomIndex);
        }
        return selected.children;
    }

    private int simulation(Node node, ILayout goal, Set<Node> closed) {
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < N_SIMULATIONS; i++) {
            Node cursor = node;
            Set<Node> closedSim = new HashSet<>(closed);
            closedSim.add(cursor);
            int simulationCost = 0;
            for (int j = 0; j < SIMULATION_DEPTH; j++) {
                if (cursor.layout.isGoal(goal))
                    break;
                closedSim.add(cursor);
                cursor = cursor.randomSuccessor(closedSim, random, goal, SIMULATION_DEPTH);
                if (cursor == null)
                    break;
                simulationCost += cursor.layout.getG();
            }
            result = Math.min(result, simulationCost);
        }
        return result;
    }

    private void backpropagation(Node node) {
        Node bestChild = null;
        for (Node child : node.children) {
            if (bestChild == null || child.sim + (int) child.layout.getG() < bestChild.sim + (int) bestChild.layout.getG()) {
                bestChild = child;
            }
        }
        node.sim = bestChild.sim + (int) bestChild.layout.getG();
        if (node.father != null)
            backpropagation(node.father);
    }

    public static class Node implements Iterable<Node> {
        ILayout layout;
        Node father;
        Set<Node> children = new HashSet<>();
        int g, sim = -1;

        public Node(ILayout layout, Node father) {
            this.layout = layout;
            this.father = father;
            if (father != null)
                g = father.g + (int) layout.getG();
        }

        public Node(ILayout layout) {
            this(layout, null);
        }

        private Node randomSuccessor(Set<Node> closed, Random random, ILayout goal, int cut) {
            if (cut == 0) return null;
            Node result = new Node(layout.randomChild(goal), this);
            return !closed.contains(result) ? result : randomSuccessor(closed, random, goal, cut - 1);
            /*List<Node> successors = successors(closed, goal);
            if (successors.isEmpty()) return null;
            int randomIndex = random.nextInt(successors.size());
            return successors.get(randomIndex);*/
        }

        public Node cheapestChild() {
            Node result = null;
            for (Node child : children) {
                result = result == null || child.getF() < result.getF() ? child : result;
            }
            return result;
        }

        public List<Node> successors(Set<Node> closed, ILayout goal) {
            List<Node> result = new ArrayList<>();
            for (ILayout successor : layout.children(goal)) {
                if (!closed.contains(new Node(successor))) {
                    Node node = new Node(successor, this);
                    result.add(node);
                }
            }
            return result;
        }

        public int getF() {
            return sim == -1 ? g : sim + g;
        }

        @Override
        public String toString() {
            return layout.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof Node)) return false;
            Node s = (Node) obj;
            return layout.equals(s.layout);
        }

        @Override
        public int hashCode() {
            return layout.toString().hashCode();
        }

        @Override
        public Iterator<Node> iterator() {
            List<Node> states = new ArrayList<>();
            Node actual = this;
            while (actual != null) {
                states.add(actual);
                actual = actual.father;
            }
            Collections.reverse(states);
            return states.iterator();
        }
    }
}
