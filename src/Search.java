import java.util.Iterator;

public interface Search<T> {
    Iterator<T> solve(ILayout s, ILayout goal);
}
