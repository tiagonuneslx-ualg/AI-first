import java.util.List;

/**
 * A configuration of a certain environment.
 *
 * <p>In this interface are declared the methods that return the
 * information needed for a Search algorithm to solve a problem.
 *
 * @author Rafael Duarte
 * @author Tiago Nunes
 * @see Search
 */
public interface ILayout {
    /**
     * Returns the children of the receiver.
     *
     * @return the children of the receiver
     */
    List<ILayout> children();

    /**
     * Checks if the receiver equals the config goal.
     *
     * @param goal the goal config
     * @return true if the receiver equals the config goal;
     * return false otherwise
     */
    boolean isGoal(ILayout goal);

    /**
     * Returns the cost of moving from the last config to the current one.
     *
     * @return the cost of moving from the last config to the current one
     */
    double getG();

    /**
     * The heuristic value of the current config.
     *
     * @param goal the goal config
     * @return the heuristic value of the current config
     */
    double getH(ILayout goal);
}
