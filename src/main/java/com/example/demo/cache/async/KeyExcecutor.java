package com.example.demo.cache.async;


import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class KeyExcecutor {

    private final ExecutorService executors;
    private int noOfThreads;
    private HashMap<String, ExecutorService> threadPool = new HashMap<>();

    public KeyExcecutor(int noOfThreads) {
        executors = Executors.newCachedThreadPool();
    }

//    public ExecutorService getExecutor(String key) {
//        if (!threadPool.containsKey(key)) {
//            synchronized (key) {
//                if (!threadPool.containsKey(key)) {
//                    threadPool.put(key, Executors.newSingleThreadExecutor());
//                    return threadPool.get(key);
//                } else {
//                    return threadPool.get(key);
//                }
//            }
//        } else {
//            return threadPool.get(key);
//        }
//    }

    public void removeExecutor(String cacheKey) {
        synchronized (cacheKey) {

        }
    }

    public CompletableFuture<Void> run(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executors);
    }

    public <T> CompletableFuture<T> get(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, executors);
    }

    public void startTimeInvoker(long delayTime, Runnable function) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(function, delayTime, delayTime, java.util.concurrent.TimeUnit.SECONDS);
    }
}
