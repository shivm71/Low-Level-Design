package com.example.demo.cache;


import com.example.demo.cache.async.KeyExcecutor;
import com.example.demo.cache.enumm.CacheType;
import com.example.demo.cache.enumm.EvictionType;
import com.example.demo.cache.model.HotLoadingObject;
import com.example.demo.cache.service.Cache;
import com.example.demo.cache.util.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CacheDriver {

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();

        KeyExcecutor keyExcecutor = new KeyExcecutor(2);
        CacheType cacheType = CacheType.WRITE_BACK;
        HotLoadingObject hotLoadingObject = HotLoadingObject.builder()
                .isEnabled(true)
                .valueExtractor(() -> {
                    FileReader reader = null;
                    try {
                        reader = new FileReader("../db/db.json");
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    // Parse the JSON file using JsonParser
                    JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                    return new Gson().fromJson(new Gson().toJson(jsonObject), Map.class);
                })
                .build();
//        com.github.benmanes.caffeine.cache.Cache<String, String> caffeine = Caffeine.newBuilder().build();
//        CacheController cacheController = new CacheController();
        Cache<String> cache = CacheBuilder.builder()
                .cacheType(cacheType)
                .evictionType(EvictionType.LFU)
                .concurrency(10)
                .hotLoading(hotLoadingObject)
                .build();
////        Cache<Integer> cache2 = Cache.builder().build();
//        log.info(cache.toString());
//        log.info(cache2.toString());
//        cacheDriver.testCache();
//        new Thread(()-> lo(cacheController.get("1")));
    }

//    public void testCache() {
//        Cache cache = new Cache();
//        cache.setCache("key", "value");
//        System.out.println(cache.getCache("key"));
//    }
//
//    public void testCache2() {
//        Cache cache = new Cache();
//        cache.setCache("key", "value");
//        System.out.println(cache.getCache("key"));
//    }
//
//    public void testCache3() {
//        Cache cache = new Cache();
//        cache.setCache("key", "value");
//        System.out.println(cache.getCache("key"));
//    }
//
//    public void testCache4() {
//        Cache cache = new Cache();
//        cache.setCache("key", "value");
//        System.out.println(cache.getCache("key"));
//    }
//
//    public void testCache5() {
//        Cache cache = new Cache();
//        cache.setCache("key", "value");
//    }
}
