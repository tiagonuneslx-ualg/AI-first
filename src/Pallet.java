import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        if(dim != sqrt) throw new
                IllegalStateException("Invalid arg in Pallet constructor");
        pallet = new int[dim * dim];
        for(int i = 0; i < dim * dim; i++)
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
            result.pallet = Arrays.copyOf(pallet, pallet.length);
            return result;
        } catch(CloneNotSupportedException e) {
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
    public List<ILayout> children() {
        List<ILayout> result = new ArrayList<>();
        for(int i = 0; i < dim * dim - 1; i++) {
            for(int j = i + 1; j < dim * dim; j++) {
                double g = calcG(pallet[i], pallet[j]);
                if(g != 20) {
                    Pallet child = exchange(i, j);
                    child.g = calcG(pallet[i], pallet[j]);
                    result.add(child);
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGoal(ILayout goal) {
        return equals(goal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof Pallet)) return false;
        Pallet s = (Pallet) obj;
        if(pallet.length != s.pallet.length) return false;
        for(int i = 0; i < pallet.length; i++) {
            if(pallet[i] != s.pallet[i])
                return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
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
    public double getH(ILayout goal) {
        int np = 0, ni = 0, ni_in_i = 0;
        Pallet goalPallet = (Pallet) goal;
        for(int i = 0; i < dim * dim; i++) {
            if(pallet[i] != goalPallet.pallet[i]) {
                if(pallet[i] % 2 == 0) np++;
                else {
                    ni++;
                    if(goalPallet.pallet[i] % 2 == 1) ni_in_i++;
                }
            }
        }
        /*
        if(ni > np)
            result = np * 5 + (double) (ni - np) / 2 + 0.5;
        else if (ni == 0 && np > 0)
            result = np * 5 + 5;
        else
            result = np * 5;
        return result;
         */
        double result = np * 5;
        if(ni == 0 && np > 0) result += 5;
        else {
            result += (int) ((double) (ni_in_i / 2) + 0.5);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < dim; i++) {
            for(int j = 0; j < dim; j++) {
                output.append(pallet[i * dim + j]);
                output.append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }
}
