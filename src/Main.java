import java.util.Iterator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Search s = new IDAStarRecursive();
        Iterator<Search.State> it = s.solve(new Pallet(sc.next()), new Pallet(sc.next()));
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                Search.State i = it.next();
                if (!it.hasNext())
                    System.out.println(i.getG());
            }
        }
        sc.close();
    }
}
