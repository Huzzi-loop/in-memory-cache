package org.inmemorycache.interfaces;

public interface EvictionStrategy<KEY, VALUE> {

    KEY evict();

}
