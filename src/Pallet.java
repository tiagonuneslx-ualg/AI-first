import java.util.ArrayList;
import java.util.List;

class Pallet implements ILayout, Cloneable {
    private static final int dim = 2;
    private int[] pallet;
    private double g;

    public Pallet() {
        pallet = new int[dim * dim];
    }

    public Pallet(String str) throws IllegalStateException {
        if (str.length() != dim * dim) throw new
                IllegalStateException("Invalid arg in Board constructor");
        pallet = new int[dim * dim];
        int si = 0;
        for (int i = 0; i < dim * dim; i++)
            pallet[i] = Character.getNumericValue(str.charAt(si++));
    }

    protected Pallet clone() throws CloneNotSupportedException {
        Pallet result = (Pallet) super.clone();
        result.pallet = new int[dim * dim];
        for (int i = 0; i < pallet.length; i++) {
            result.pallet[i] = pallet[i];
        }
        result.g = g;
        return result;
    }

    private Pallet exchange(int i, int j) { //exchanges 2 containers positions
        Pallet result = null;
        try {
            result = clone();
            result.pallet[i] = pallet[j];
            result.pallet[j] = pallet[i];
        } catch (CloneNotSupportedException ignored) {
        }
        return result;
    }

    private double calcG(int a, int b) {// returns the cost of an exchange between 2 pallets
        return a % 2 == 0 && b % 2 == 0 ? 20 : a % 2 == 1 && b % 2 == 1 ? 1 : 5;
    }

    @Override
    public List<ILayout> children() {
        List<ILayout> result = new ArrayList<>();
        for (int i = 0; i < dim * dim - 1; i++) {
            for (int j = i + 1; j < dim * dim; j++) {
                Pallet child = exchange(i, j);
                child.g = calcG(pallet[i], pallet[j]);
                result.add(child);
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
        int result = 0;
        Pallet goalPallet = (Pallet) goal;
        for (int i = 0; i < dim * dim; i++) {
            if (pallet[i] != goalPallet.pallet[i])
                result++;
        }
        return result;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++)
                output.append(pallet[i * dim + j]);
            output.append("\n");
        }
        return output.toString();
    }
}
