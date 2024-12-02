package org.inmemorycache;
import org.inmemorycache.eviction.RandomEvictionStrategy;
import org.junit.Test;
import org.junit.Before;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CacheTest {

    private InMemoryCache<String, String> cache;

    @Before
    public void setup() {
        RandomEvictionStrategy<String, String> evictionStrategy = new RandomEvictionStrategy<>();
        cache = new InMemoryCache<String, String>(10, evictionStrategy);
        cache.register(evictionStrategy);
    }

    @Test
    public void testPutAndGet() {
        cache.put("Test", "TestString");
        assertEquals("If value is stored, get should return same value", "TestString", cache.get("Test"));
    }

    @Test
    public void testPutAndRemove() {
        cache.put("Test", "TestString");
        cache.remove("Test");
        assertNull("If value is removed, get should return null", cache.get("Test"));
    }

    @Test
    public void testMaxCapacity() {
        IntStream.range(0, 10)
                .forEach(i -> cache.put("Test_" + i, "TestString_" + i));

        assertEquals("Cache size should be equal to capacity", 10, cache.getCache().size());

        cache.put("Test_11", "TestString_11");
        assertEquals("Cache size should be equal to capacity", 10, cache.getCache().size());
        assertEquals("If put is called, value should be present", "TestString_11", cache.get("Test_11"));

    }

}
