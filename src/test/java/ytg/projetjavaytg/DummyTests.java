package ytg.projetjavaytg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DummyTests {

    @Test
    void testAlwaysPasses() {
        assertTrue(true);
    }

    @Test
    void testAlwaysFails() {
        // Tu peux commenter cette ligne pour éviter d’échouer le build
        // fail("This test fails intentionally");
    }

    @Test
    void testMath() {
        int sum = 2 + 3;
        assertEquals(5, sum);
    }

    @Test
    void testString() {
        String s = "hello";
        assertEquals("hello", s);
    }

    @Test
    void testArray() {
        int[] arr = {1, 2, 3};
        assertEquals(3, arr.length);
    }
}
