package com.example.demo.cache.async;



import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

public class KeyExcecutor {

    private int noOfThreads;

    private final ExecutorService executors;

    private HashMap<String, ExecutorService> threadPool = new HashMap<>();

    public void removeExecutor(String cacheKey) {
        synchronized (cacheKey){

        }
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

    public KeyExcecutor(int noOfThreads) {
        executors = Executors.newCachedThreadPool();
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
