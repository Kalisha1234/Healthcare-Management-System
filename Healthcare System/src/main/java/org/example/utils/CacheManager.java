package org.example.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager<K, V> {
    private final Map<K, V> cache;
    private final Map<String, List<V>> listCache;

    public CacheManager() {
        this.cache = new ConcurrentHashMap<>();
        this.listCache = new ConcurrentHashMap<>();
    }

    // Single item cache operations
    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    // List cache operations
    public void putList(String key, List<V> list) {
        listCache.put(key, new ArrayList<>(list));
    }

    public List<V> getList(String key) {
        List<V> list = listCache.get(key);
        return list != null ? new ArrayList<>(list) : null;
    }

    public void removeList(String key) {
        listCache.remove(key);
    }

    // Clear all caches
    public void clear() {
        cache.clear();
        listCache.clear();
    }

    // Invalidate all list caches (useful after updates)
    public void invalidateListCaches() {
        listCache.clear();
    }

    public int size() {
        return cache.size();
    }

    public int listCacheSize() {
        return listCache.size();
    }

    public String getCacheStatus() {
        return String.format("Items: %d | Lists: %d", cache.size(), listCache.size());
    }

    public boolean isEmpty() {
        return cache.isEmpty() && listCache.isEmpty();
    }
}
