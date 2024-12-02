package org.inmemorycache.interfaces;

import org.inmemorycache.models.EventType;

public interface Consumer<KEY> {

    void notifyChange(EventType eventType, KEY key);
}