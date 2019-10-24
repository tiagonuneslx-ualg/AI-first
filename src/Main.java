import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Search s = new IDAStarRecursive();
        System.out.println(s.solveD(new Pallet(sc.next()), new Pallet(sc.next())));
        sc.close();
    }
}
