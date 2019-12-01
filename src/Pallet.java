import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Grid of numbered containers (cranes).
 *
 * <p>These containers can be exchanged. The costs of these exchanges vary:
 * to exchange two odds, the cost is 1, to exchange one odd and one even,
 * it's 5 and to exchange two even it's 20.
 *
 * @author Rafael Duarte
 * @author Tiago Nunes
 * @see ILayout
 */
public class Pallet implements ILayout, Cloneable {
    private int dim;
    private int[] pallet;
    private double g;
    private Random random = new Random();

    /**
     * Constructs a pallet containing the cranes given by the string in the
     * same order, and calculates the dimension based on the string length.
     *
     * @param str a string with the containers' numbers in a certain order
     * @throws IllegalStateException if string length is not a perfect number
     */
    public Pallet(String str) throws IllegalStateException {
        double sqrt = Math.sqrt(str.length());
        dim = (int) sqrt;
        if (dim != sqrt) throw new
                IllegalStateException("Invalid arg in Pallet constructor");
        pallet = new int[dim * dim];
        for (int i = 0; i < dim * dim; i++)
            pallet[i] = Character.getNumericValue(str.charAt(i));
    }

    /**
     * Returns a copy of this {@code Pallet} instance.
     *
     * @return a clone of this {@code Pallet} instance
     */
    @Override
    protected Object clone() {
        try {
            Pallet result = (Pallet) super.clone();
            result.pallet = pallet.clone();
            return result;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    private Pallet exchange(int i, int j) { //exchanges 2 containers positions
        Pallet result = (Pallet) clone();
        result.pallet[i] = pallet[j];
        result.pallet[j] = pallet[i];
        return result;
    }

    private double calcG(int a, int b) {// returns the cost of an exchange between 2 pallets
        return a % 2 == 0 && b % 2 == 0 ? 20 : a % 2 == 1 && b % 2 == 1 ? 1 : 5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public List<ILayout> children() {
        List<ILayout> result = new ArrayList<>();
        for (int i = 0; i < dim * dim - 1; i++) {
            for (int j = i + 1; j < dim * dim; j++) {
                double g = calcG(pallet[i], pallet[j]);
                if (g != 20) {
                    Pallet child = exchange(i, j);
                    child.g = calcG(pallet[i], pallet[j]);
                    result.add(child);
                }
            }
        }
        return result;
    }

    @Override
    final public List<ILayout> children(ILayout goal) {
        List<ILayout> result = new ArrayList<>();
        for (int i = 0; i < dim * dim - 1; i++) {
            for (int j = i + 1; j < dim * dim; j++) {
                double g = calcG(pallet[i], pallet[j]);
                if (g != 20 && pallet[i] != ((Pallet) goal).pallet[i]) {
                    Pallet child = exchange(i, j);
                    child.g = calcG(pallet[i], pallet[j]);
                    result.add(child);
                }
            }
        }
        return result;
    }

    @Override
    public ILayout randomChild() {
        int randomIndex = random.nextInt(dim * dim - 1);
        int randomIndex2 = random.nextInt(dim * dim - 1);
        randomIndex2 = randomIndex2 >= randomIndex ? randomIndex2 + 1 : randomIndex2;
        Pallet child = exchange(randomIndex, randomIndex2);
        child.g = calcG(pallet[randomIndex], pallet[randomIndex2]);
        return child;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public boolean isGoal(ILayout goal) {
        return equals(goal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Pallet)) return false;
        Pallet s = (Pallet) obj;
        if (pallet.length != s.pallet.length) return false;
        for (int i = 0; i < pallet.length; i++) {
            if (pallet[i] != s.pallet[i])
                return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public int hashCode() {
        return toString().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getG() {
        return g;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public double getH(ILayout goal) {
        double result = 0;
        boolean[] index_exchanged = new boolean[dim * dim];
        Pallet goalPallet = (Pallet) goal;
        for (int i = 0; i < dim * dim; i++) {
            if (index_exchanged[i]) continue;
            if (pallet[i] != goalPallet.pallet[i]) {
                if (pallet[i] % 2 == 0) {
                    for (int j = i + 1; j < dim * dim; j++) {
                        if (!index_exchanged[j] && pallet[i] == goalPallet.pallet[j] && pallet[j] == goalPallet.pallet[i]) {
                            if (pallet[j] % 2 == 0) {
                                result += 15;
                            } else {
                                result += 5;
                            }
                            index_exchanged[i] = true;
                            index_exchanged[j] = true;
                            break;
                        }
                    }
                    if (!index_exchanged[i]) {
                        result += 5;
                    }
                } else {
                    for (int j = i + 1; j < dim * dim; j++) {
                        if (!index_exchanged[j] && pallet[i] == goalPallet.pallet[j] && pallet[j] == goalPallet.pallet[i]) {
                            if (pallet[j] % 2 == 1) {
                                result += 1;
                            } else {
                                result += 5;
                            }
                            index_exchanged[i] = true;
                            index_exchanged[j] = true;
                            break;
                        }
                    }
                    if (!index_exchanged[i]) {
                        if (goalPallet.pallet[i] % 2 == 1) {
                            result++;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                output.append(pallet[i * dim + j]);
                output.append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }
}
