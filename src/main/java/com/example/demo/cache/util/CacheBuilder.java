package com.example.demo.cache.util;


import com.example.demo.cache.async.KeyExcecutor;
import com.example.demo.cache.enumm.CacheType;
import com.example.demo.cache.enumm.EvictionType;
import com.example.demo.cache.model.HotLoadingObject;
import com.example.demo.cache.service.Cache;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class CacheBuilder<T> {

    private final Map<String, T> data = new HashMap<>();
    private KeyExcecutor keyExcecutor;
    private HotLoadingObject hotLoading;
    private Map<String, Integer> frequencyMap;
    private Map<String, Timestamp> timestampMap;
    private EvictionType evictionType;
    private CacheType cacheType;
    private int size;
    private int concurrency;

    private CacheBuilder() {
    }

    public static CacheBuilder<Object> builder() {
        return new CacheBuilder<>();
    }

    public CacheBuilder<T> concurrency(int size) {
        this.concurrency = size;
        return this;
    }

    public CacheBuilder<T> cacheType(CacheType cacheType) {
        this.cacheType = cacheType;
        return this;
    }

    public CacheBuilder<T> hotLoading(HotLoadingObject hotLoading) {
        this.hotLoading = hotLoading;
        return this;
    }

    public CacheBuilder<T> size(int size) {
        this.size = size;
        return this;
    }

    public CacheBuilder<T> evictionType(EvictionType evictionType) {
        this.evictionType = evictionType;
        return this;
    }

    public <K> Cache<K> build() {
        return new Cache(hotLoading, evictionType, cacheType, size, concurrency, new KeyExcecutor(concurrency));
    }
}