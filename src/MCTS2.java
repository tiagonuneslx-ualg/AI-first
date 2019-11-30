import java.util.*;

public class MCTS2 {
    private static final int N_SUCESSORS_EXPANDED = 10;
    private static final int N_SIMULATIONS = 20;
    private static final int SIMULATION_DEPTH = 20;
    private static final int MAX_ITERATIONS = 100;
    private final Random random = new Random();

    Iterator<Node> solve(ILayout initial, ILayout goal) {
        Node root = new Node(initial);
        Set<Node> closed = new HashSet<>();
        if (root.layout.isGoal(goal)) return root.iterator();
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Node selected = selection(root, closed);
            if (selected.layout.isGoal(goal)) return selected.iterator();
            Set<Node> expanded = expand(selected, closed);
            int minSim = Integer.MAX_VALUE;
            for (Node node : expanded) {
                int sim = simulation(node, goal, closed);
                minSim = Math.min(minSim, sim);
            }
            backpropagation(selected, minSim);
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

    private Set<Node> expand(Node selected, Set<Node> closed) {
        List<Node> successors = selected.successors(closed);
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
            boolean foundSolution = false;
            for (int j = 0; j < SIMULATION_DEPTH; j++) {
                cursor = cursor.randomSuccessor(closedSim, random);
                if (cursor == null) break;
                closedSim.add(cursor);
                simulationCost += cursor.layout.getG();
                if (cursor.layout.isGoal(goal)) {
                    foundSolution = true;
                    break;
                }
            }
            if (foundSolution)
                result = Math.min(result, simulationCost);
        }
        node.sim = result;
        return result;
    }

    private void backpropagation(Node node, int sim) {
        Node cursor = node;
        int costFromNode = 0;
        while (cursor.father != null) {
            costFromNode += cursor.layout.getG();
            cursor.father.sim = sim + costFromNode;
            cursor = cursor.father;
        }
    }

    public static class Node implements Iterable<Node> {
        ILayout layout;
        Node father;
        Set<Node> children = new HashSet<>();
        int g, sim;

        public Node(ILayout layout, Node father) {
            this.layout = layout;
            this.father = father;
            if (father != null)
                g = father.g + (int) layout.getG();
        }

        public Node(ILayout layout) {
            this(layout, null);
        }

        private Node randomSuccessor(Set<Node> closed, Random random) {
            /*Node result = new Node(layout.randomChild(), this);
            return !closed.contains(result) ? result : randomSuccessor(closed);*/
            List<Node> successors = successors(closed);
            if (successors.isEmpty()) return null;
            int randomIndex = random.nextInt(successors.size());
            return successors.get(randomIndex);
        }

        public Node cheapestChild() {
            Node result = null;
            for (Node child : children) {
                result = result == null || child.getF() < result.getF() ? child : result;
            }
            return result;
        }

        public List<Node> successors(Set<Node> closed) {
            List<Node> result = new ArrayList<>();
            for (ILayout successor : layout.children()) {
                if (!closed.contains(new Node(successor))) {
                    Node node = new Node(successor, this);
                    result.add(node);
                }
            }
            return result;
        }

        private int getF() {
            return sim + g;
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
