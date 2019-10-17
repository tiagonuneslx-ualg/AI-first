import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void testBoard(){
        Scanner sc = new Scanner(System.in);
        BestFirst s = new BestFirst();
        Iterator<BestFirst.State> it = s.solve(new Board(sc.next()), new Board(sc.next()));
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                BestFirst.State i = it.next();
                System.out.println(i);
                if (!it.hasNext())
                    System.out.println(i.getG());
            }
        }
        sc.close();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        IDAStar s = new IDAStar();
        Iterator<IDAStar.State> it = s.solve(new Pallet(sc.next(), 3), new Pallet(sc.next(), 3));
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                IDAStar.State i = it.next();
                if (!it.hasNext())
                    System.out.println(i.getG());
            }
        }
        sc.close();
    }
}
