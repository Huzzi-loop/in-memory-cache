package org.inmemorycache.eviction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.inmemorycache.interfaces.Consumer;
import org.inmemorycache.interfaces.EvictionStrategy;
import org.inmemorycache.models.EventType;

public class RandomEvictionStrategy<KEY, VALUE> implements EvictionStrategy<KEY, VALUE>, Consumer<KEY> {

    private List<KEY> keys = new ArrayList<>();

    @Override
    public KEY evict() {
        if (keys.isEmpty()) {
            return null; // No keys to evict
        }
        int randomIndex = ThreadLocalRandom
                .current()
                .nextInt(keys.size());
        KEY keyToEvict = keys.get(randomIndex);
        keys.remove(randomIndex); // Remove from the keys list
        return keyToEvict;
    }

    @Override
    public void notifyChange(EventType eventType, KEY key) {
        if (eventType == EventType.READ) {
            return; // Do nothing on read events
        }

        if (eventType == EventType.WRITE) {
            if (!keys.contains(key)) {
                keys.add(key); // Add new key if not already present
            }
        } else if (eventType == EventType.DELETE) {
            keys.remove(key); // Remove the key if explicitly deleted
        }
    }
}
