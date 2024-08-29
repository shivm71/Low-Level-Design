package com.example.demo.cache.service;


import com.example.demo.cache.async.KeyExcecutor;
import com.example.demo.cache.db.Repository;
import com.example.demo.cache.enumm.CacheType;
import com.example.demo.cache.enumm.EvictionType;
import com.example.demo.cache.model.HotLoadingObject;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListMap;

//@Builder
@ToString
@Slf4j
public class Cache<T> {

    private Repository<T> repository = Repository.instance();
    private ConcurrentSkipListMap<String, T> data = new ConcurrentSkipListMap<>();
    private KeyExcecutor keyExcecutor;
    private HotLoadingObject hotLoading;
    private Map<String, Integer> frequencyMap;
    private TreeMap<Long, List<String>> timestampMap = new TreeMap<>();
    private EvictionType evictionType;
    private CacheType cacheType;
    private int size;
    private int concurrency;
    private long ttl = 5 * 60;

    public Cache(HotLoadingObject hotLoading, EvictionType evictionType, CacheType cacheType, int size, int concurrency, KeyExcecutor keyExcecutor) {
        this.hotLoading = hotLoading;
        this.evictionType = evictionType;
        this.cacheType = cacheType;
        this.size = size;
        this.concurrency = concurrency;
        this.keyExcecutor = keyExcecutor;
        startDeleteSchedular();
        if (hotLoading.isEnabled()) {
            hotLoading.getValueExtractor().get().entrySet().parallelStream()
                    .forEach((stringObjectEntry -> put(stringObjectEntry.getKey(), (T)stringObjectEntry.getValue())));
        }
    }

    private void startDeleteSchedular() {
        keyExcecutor.startTimeInvoker(1, () -> {
            while (true) {
                if (!timestampMap.keySet().isEmpty() && timestampMap.firstKey() + ttl < Instant.now().getEpochSecond()) {
                    String key = data.firstKey();
                    timestampMap.getOrDefault(key, new ArrayList<>()).forEach((cacheKey) -> {
                        data.remove(cacheKey);
                        keyExcecutor.removeExecutor(cacheKey);
                    });
                    timestampMap.remove(key);
                    log.info("Removing the key - {} from the cache", key);
                } else {
                    log.info("No keys to remove");
                    break;
                }
            }

        });
    }

    public CompletableFuture<T> get(String key) {
        if (data.containsKey(key)) {
            return CompletableFuture.completedFuture(data.get(key));
        } else {
            log.error("Getting the value from DB for key - {}", key);
            return keyExcecutor.get(() -> repository.getValue(key));
        }
    }

    public CompletableFuture<Void> put(String key, T value) {
        switch (cacheType) {
            case WRITE_BACK:
                return handleWriteBackPut(key, value);
            case WRITE_THROUGH:
                return handleWriteThroughPut(key, value);
        }
        return null;
    }

    private CompletableFuture<Void> handleWriteThroughPut(String key, T value) {
        return keyExcecutor.run(() -> {
            repository.putValue(key, value);
            data.put(key, value);
            timestampMap.putIfAbsent(Instant.now().getEpochSecond(), new ArrayList<>());
            timestampMap.get(Instant.now().getEpochSecond() + ttl).add(key);
        });

    }

    @SneakyThrows
    private CompletableFuture<Void> handleWriteBackPut(String key, T value) {
        CompletableFuture toWaitFuture = keyExcecutor.run(() -> {
            timestampMap.putIfAbsent(Instant.now().getEpochSecond(), new ArrayList<>());
            timestampMap.get(Instant.now().getEpochSecond() + ttl).add(key);
            data.put(key, value);
        });
        toWaitFuture.wait();
        // independent of ID
        keyExcecutor.run(() -> repository.putValue(key, value));
        return CompletableFuture.completedFuture(null);

    }

}
