package org.inmemorycache.interfaces;
import org.inmemorycache.models.EventType;

import java.util.*;

public abstract class Producer<KEY> {

    List<Consumer<KEY>> consumers = new ArrayList<>();

    public void register(Consumer<KEY> consumer) {
        consumers.add(consumer);
    }

    public void deregister(Consumer<KEY> consumer) {
        consumers.remove(consumer);
    }

    public void notifyConsumers(EventType eventType, KEY key) {
        consumers.forEach(consumer -> consumer.notifyChange(eventType, key));
    }

}