import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class PalletUnitTests {

    public static void test(String initial_s, String goal_s, int expected) {
        Pallet initial = new Pallet(initial_s);
        Pallet goal = new Pallet(goal_s);
        Search s = new IDAStar();
        Iterator<Search.State> it = s.solve(initial, goal);
        int result = -1;
        if (it == null)
            System.out.println("no solution was found");
        else {
            while (it.hasNext()) {
                Search.State i = it.next();
                if (!it.hasNext())
                    result = i.getG();
            }
        }
        assertEquals(expected, result);
    }

    @Test(timeout = 60000)
    public void testConstructor() {
        Pallet b = new Pallet("1324");
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        pw.println("13");
        pw.println("24");
        assertEquals(writer.toString(), b.toString());
        pw.close();
    }

    @Test(timeout = 60000)
    public void test_dim2_1() {
        test("1324", "1234", 5);
    }

    @Test(timeout = 60000)
    public void test_dim2_2() {
        test("1432", "1234", 15);
    }

    @Test(timeout = 60000)
    public void test_dim2_3() {
        test("1234", "3214", 1);
    }

    @Test(timeout = 60000)
    public void test_dim3_1() {
        test("123456789", "213456789", 5);
    }

    @Test(timeout = 60000)
    public void test_dim3_2() {
        test("123456789", "143256789", 15);
    }

    @Test(timeout = 60000)
    public void test_dim3_3() {
        test("123456789", "576891324", 24);
    }

    @Test(timeout = 60000)
    public void test_dim3_4() {
        test("123456789", "987654321", 32);
    }

    @Test(timeout = 60000)
    public void test_dim4_1() {
        test("123456789ABCDEFG", "214356789ABCGEFD", 15);
    }

    @Test(timeout = 60000)
    public void test_dim4_2() {
        test("123456789ABCDEFG", "GFEDCBA987654321", 40);
    }

    @Test(timeout = 60000)
    public void test_dim4_3() {
        test("123456789ABCDEFGHIJKLMNOP", "213456789PBCDEFGHIJKLMNOA", 10);
    }
}