package org.inmemorycache.eviction;
import org.inmemorycache.interfaces.Consumer;
import org.inmemorycache.interfaces.EvictionStrategy;
import org.inmemorycache.models.EventType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RandomEvictionStrategy<KEY, VALUE> implements EvictionStrategy<KEY, VALUE>, Consumer<KEY> {

    private List<KEY> keys = new ArrayList<>();

    @Override
    public KEY evict() {
        int randomIndex = ThreadLocalRandom
                .current()
                .nextInt(keys.size())
                % keys.size();

        return keys.get(randomIndex);
    }

    @Override
    public void notifyChange(EventType eventType, KEY key) {
        if (eventType == EventType.READ) {
            return;
        }

        keys.add(key);
    }

}
