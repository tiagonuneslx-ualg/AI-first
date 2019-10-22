import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Pallet is a grid of numbered containers (cranes).
 * <p>
 * These containers can be exchanged.
 *
 * <table>
 *     <tr>
 *         <td></td> <td>ODD</td> <td>EVEN</td>
 *     </tr>
 *     <tr>
 *         <td>ODD</td> <td>1</td> <td>5</td>
 *     </tr>
 *     <tr>
 *         <td>EVEN</td> <td>5</td> <td>20</td>
 *     </tr>
 * </table>
 *
 * @author Rafael Duarte
 * @author Tiago Nunes
 * @see ILayout
 */
class Pallet implements ILayout, Cloneable {
    private int dim;
    private int[] pallet;
    private double g;

    /**
     * Constructs a pallet containing the cranes given by the string in the
     * same order, and calculates the dimension based on the string length.
     *
     * @param str a string with the containers' number in a certain order
     * @throws IllegalStateException if string length is not a perfect number
     */
    public Pallet(String str) throws IllegalStateException {
        double sqrt = Math.sqrt(str.length());
        dim = (int) sqrt;
        if(dim != sqrt) throw new
                IllegalStateException("Invalid arg in Board constructor");
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

    @Override
    public boolean isGoal(ILayout l) {
        return equals((Pallet) l);
    }

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

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public double getG() {
        return g;
    }

    @Override
    public double getH(ILayout goal) {
        double result = 0;
        int np = 0, ni = 0;
        Pallet goalPallet = (Pallet) goal;
        for(int i = 0; i < dim * dim; i++) {
            if(pallet[i] != goalPallet.pallet[i]) {
                if(pallet[i] % 2 == 0) np++;
                else ni++;
            }
        }
        result += np * 5;
        if(ni > np)
            result += (double) (ni - np) / 2 + 0.5;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < dim; i++) {
            for(int j = 0; j < dim; j++)
                output.append(pallet[i * dim + j]);
            output.append("\n");
        }
        return output.toString();
    }
}
