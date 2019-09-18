import java.util.List;

public interface ILayout {
    /**
     @return the children of the receiver.
     */
     List<ILayout> children();
/**
 @return true if the receiver equals the argument l;
 return false otherwise.
 */
boolean isGoal(ILayout l);
    /**
     @return the cost for moving from the input config to the receiver.
     */
    double getG();
}
