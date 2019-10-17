import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class PalletUnitTests {

    @Test
    public void testConstructor() {
        Pallet b = new Pallet("1324", 2);
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        pw.println("13");
        pw.println("24");
        assertEquals(writer.toString(), b.toString());
        pw.close();
    }

    @Test
    public void test_IDAStar_dim2_1() {
        Pallet initial = new Pallet("1324", 2);
        Pallet goal = new Pallet("1234", 2);
        IDAStar s = new IDAStar();
        Iterator<IDAStar.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                IDAStar.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(5, result);
    }

    @Test
    public void test_IDAStar_dim2_2() {
        Pallet initial = new Pallet("1432", 2);
        Pallet goal = new Pallet("1234", 2);
        IDAStar s = new IDAStar();
        Iterator<IDAStar.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                IDAStar.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(15, result);
    }

    @Test
    public void test_IDAStar_dim2_3() {
        Pallet initial = new Pallet("1234", 2);
        Pallet goal = new Pallet("3214", 2);
        IDAStar s = new IDAStar();
        Iterator<IDAStar.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                IDAStar.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(1, result);
    }

    @Test
    public void test_IDAStar_dim3_1() {
        Pallet initial = new Pallet("123456789", 3);
        Pallet goal = new Pallet("213456789", 3);
        IDAStar s = new IDAStar();
        Iterator<IDAStar.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                IDAStar.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(5, result);
    }

    @Test
    public void test_IDAStar_dim3_2() {
        Pallet initial = new Pallet("123456789", 3);
        Pallet goal = new Pallet("143256789", 3);
        IDAStar s = new IDAStar();
        Iterator<IDAStar.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                IDAStar.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(15, result);
    }

    @Test(timeout = 60000)
    public void test_IDAStar_dim3_3() {
        Pallet initial = new Pallet("123456789", 3);
        Pallet goal = new Pallet("576891324", 3);
        IDAStar s = new IDAStar();
        Iterator<IDAStar.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                IDAStar.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(24, result);
    }

    @Test(timeout = 60000)
    public void test_IDAStar_dim3_4() {
        Pallet initial = new Pallet("123456789", 3);
        Pallet goal = new Pallet("987654321", 3);
        IDAStar s = new IDAStar();
        Iterator<IDAStar.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                IDAStar.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(32, result);
    }

}