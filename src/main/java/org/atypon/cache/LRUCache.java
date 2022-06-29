package org.atypon.cache;

import com.google.common.cache.CacheBuilder;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class LRUCache<K, V> implements Cache<K, V> {
    private final ConcurrentMap<K, V> cacheMap;
    private final int maxSize;

    public LRUCache(int capacity) {
        this.maxSize = capacity;
        cacheMap =
                CacheBuilder.newBuilder()
                        .maximumSize(capacity)
                        .<K, V>build().asMap();
    }


    @Override
    public V get(K o) {
        return cacheMap.get(o);
    }


    @Override
    public V put(K k, V v) {
        return cacheMap.put(k, v);
    }


    @Override
    public V remove(K o) {
        return cacheMap.remove(o);
    }

    @Override
    public boolean containsKey(K o) {
        return cacheMap.containsKey(o);
    }

    @Override
    public int size() {
        return maxSize;
    }

    @Override
    public Set<K> keySet() {
        return cacheMap.keySet();
    }
}