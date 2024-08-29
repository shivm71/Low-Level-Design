package com.example.demo.cache.controller;


import com.example.demo.cache.async.KeyExcecutor;
import com.example.demo.cache.enumm.CacheType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class CacheController {

    HashMap<String, String> map = new HashMap<>();

    KeyExcecutor keyExcecutor = new KeyExcecutor(2);
    CacheType cacheType = CacheType.WRITE_BACK;
//    HotLoadingObject hotLoadingObject = new HotLoadingObject();
//    Cache<String, String> caffeine = Caffeine.newBuilder().build();
//    Cache<String> cache = Cache.builder().cacheType(cacheType).evictionType(EvictionType.LFU).concurrency(10).hotLoading(hotLoadingObject).build();
//    Cache<Integer> cache2 = Cache.builder().build();

//    System.out.println()

//    public CompletableFuture<String> get(String key) {
//        return cache.get(key);
//    }
//
//    public CompletableFuture<Void> put(String key, String value) {
//        cache.set(key, value);
//    }
}
