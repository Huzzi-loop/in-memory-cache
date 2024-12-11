package org.inmemorycache.eviction;

import java.util.HashMap;
import java.util.Map;

import org.inmemorycache.interfaces.Consumer;
import org.inmemorycache.interfaces.EvictionStrategy;
import org.inmemorycache.models.EventType;

public class LRUEvictionStrategy<KEY, VALUE> implements EvictionStrategy<KEY, VALUE>, Consumer<KEY> {

    private final int capacity;
    private final Map<KEY, Node<KEY, VALUE>> cacheMap;
    private final Node<KEY, VALUE> head;
    private final Node<KEY, VALUE> tail;

    public LRUEvictionStrategy(int capacity) {
        this.capacity = capacity;
        this.cacheMap = new HashMap<>(capacity);
        this.head = new Node<>(null, null); // Dummy head
        this.tail = new Node<>(null, null); // Dummy tail
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public KEY evict() {
        // Remove the least recently used node (tail.prev)
        if (tail.prev == head) {
            return null; // No nodes to evict

        }
        Node<KEY, VALUE> lruNode = tail.prev;
        removeNode(lruNode);
        cacheMap.remove(lruNode.key);
        return lruNode.key;
    }

    @Override
    public void notifyChange(EventType eventType, KEY key) {
        if (eventType == EventType.READ || eventType == EventType.WRITE) {
            Node<KEY, VALUE> node = cacheMap.get(key);
            if (node != null) {
                // Move the accessed node to the head
                removeNode(node);
                addToHead(node);
            } else if (eventType == EventType.WRITE) {
                // Add a new node if it doesn't exist
                if (cacheMap.size() == capacity) {
                    evict();
                }
                Node<KEY, VALUE> newNode = new Node<>(key, null);
                cacheMap.put(key, newNode);
                addToHead(newNode);
            }
        } else if (eventType == EventType.DELETE) {
            Node<KEY, VALUE> node = cacheMap.remove(key);
            if (node != null) {
                removeNode(node);
            }
        }
    }

    // Helper to remove a node from the linked list
    private void removeNode(Node<KEY, VALUE> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // Helper to add a node to the head of the linked list
    private void addToHead(Node<KEY, VALUE> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private class Node<KEY, VALUE> {

        KEY key;
        VALUE value;
        Node<KEY, VALUE> prev;
        Node<KEY, VALUE> next;

        public Node(KEY key, VALUE value) {
            this.key = key;
            this.value = value;
        }
    }

}
