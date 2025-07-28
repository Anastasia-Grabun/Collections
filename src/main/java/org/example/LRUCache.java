package org.example;

import java.util.LinkedHashMap;
import java.util.Map;

/** ### 10. Реализация простого кеша LRU
Создайте простую реализацию LRU (Least Recently Used)
кеша фиксированного размера, используя LinkedHashMap.**/

public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, V> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public Map<K, V> getCacheContent() {
        return new LinkedHashMap<>(cache);
    }

}

