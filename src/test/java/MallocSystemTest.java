import static org.junit.Assert.*;

import org.junit.Assert;

import org.junit.Test;

public class MallocSystemTest {
    MallocSystem mallocSystem = new MallocSystem(100);

    @Test
    public final void allocTest() {
        dump();
        for (int i = 0; i < 10; i++) {
            mallocSystem.malloc(10);
            dump();
        }

        for (int i = 0; i < 100; i = i + 10) {
            mallocSystem.free(i);
            dump();
        }

        assertTrue(mallocSystem.free(0) == -1);
        assertTrue(mallocSystem.free(-1) == -1);
        assertTrue(mallocSystem.malloc(1000) == -1);
        assertTrue(mallocSystem.malloc(-1) == -1);

    }


    public void dump() {
        mallocSystem.printMemorySegments();
        mallocSystem.printFreeSegments();
        mallocSystem.printLinksToSegments();
    }

}
