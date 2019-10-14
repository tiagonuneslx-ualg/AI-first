import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class PalletUnitTests {

    @Test
    public void testConstructor() {
        Pallet b = new Pallet("1324");
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        pw.println("13");
        pw.println("24");
        assertEquals(writer.toString(), b.toString());
        pw.close();
    }

    @Test
    public void testSolve() {
        Pallet initial = new Pallet("1324");
        Pallet goal = new Pallet("1234");
        BestFirst s = new BestFirst();
        Iterator<BestFirst.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                BestFirst.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(5, result);
    }

    @Test
    public void testSolve2() {
        Pallet initial = new Pallet("1432");
        Pallet goal = new Pallet("1234");
        BestFirst s = new BestFirst();
        Iterator<BestFirst.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                BestFirst.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(15, result);
    }

    @Test
    public void testSolve3() {
        Pallet initial = new Pallet("1234");
        Pallet goal = new Pallet("3214");
        BestFirst s = new BestFirst();
        Iterator<BestFirst.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                BestFirst.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(1, result);
    }

    @Test
    public void testSolveIDA() {
        Pallet initial = new Pallet("1324");
        Pallet goal = new Pallet("1234");
        BestFirst s = new BestFirst();
        Iterator<BestFirst.State> it = s.solveIDA(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                BestFirst.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(5, result);
    }

    @Test
    public void testSolveIDA2() {
        Pallet initial = new Pallet("1432");
        Pallet goal = new Pallet("1234");
        BestFirst s = new BestFirst();
        Iterator<BestFirst.State> it = s.solveIDA(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                BestFirst.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(15, result);
    }

    @Test
    public void testSolveIDA3() {
        Pallet initial = new Pallet("1234");
        Pallet goal = new Pallet("3214");
        BestFirst s = new BestFirst();
        Iterator<BestFirst.State> it = s.solveIDA(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                BestFirst.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(1, result);
    }
}