package com.decduck3.ironcomputing.ironserver;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class IronServerPacketSync<K, V> {
    private final Map<K, BlockingQueue<V>> map = new ConcurrentHashMap<>();

    private synchronized BlockingQueue<V> ensureQueueExists(K key) {
        return map.computeIfAbsent(key, k -> new ArrayBlockingQueue<>(1));
    }

    public boolean put(K key, V value, long timeout, TimeUnit timeUnit) {
        BlockingQueue<V> queue = ensureQueueExists(key);
        try {
            return queue.offer(value, timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public V get(K key, long timeout, TimeUnit timeUnit) {
        BlockingQueue<V> queue = ensureQueueExists(key);
        try {
            return queue.poll(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
