package ExpiringValuesMap;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EvictionMapTest {
    EvictionMap<String, String> evictionMap;

    @Test
    void valueRemovedAfterCertainTime() throws InterruptedException {
        evictionMap = new EvictionMap<>(2000);
        evictionMap.put("Hello", "World");
        Thread.sleep(1000);
        assertEquals("World", evictionMap.get("Hello"));
        Thread.sleep(1000);
        assertNull(evictionMap.get("Hello"));
    }

    @Test
    void valueUpdated() throws InterruptedException {
        evictionMap = new EvictionMap<>(3000);
        evictionMap.put("Hello", "World");
        Thread.sleep(1000);
        evictionMap.put("Hello", "Mars");
        assertEquals("Mars", evictionMap.get("Hello"));
        Thread.sleep(1000);
        assertEquals("Mars", evictionMap.get("Hello"));
    }

    @Test
    void continuousPut() throws InterruptedException {
        evictionMap = new EvictionMap<>(3000);
        for (int i = 0; i < 10000; i++) {
            evictionMap.put("Hello" + i, "World" + i);
        }
        evictionMap.put("Hello" + 5000, "Mars" + 5000);
        assertEquals("Mars5000", evictionMap.get("Hello" + 5000));
        Thread.sleep(3000);
        assertNull(evictionMap.get("Hello"));
        assertNull(evictionMap.get("Hello9999"));
    }

    @Test
    void nullPutAndGet() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            evictionMap = new EvictionMap<>(-10000);
        });
        evictionMap = new EvictionMap<>(3000);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            evictionMap.put(null, "World");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            evictionMap.put("World", null);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            evictionMap.get(null);
        });
    }
}