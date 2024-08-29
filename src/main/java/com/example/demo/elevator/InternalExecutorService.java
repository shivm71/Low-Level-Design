package com.example.demo.elevator;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class InternalExecutorService {

    private ExecutorService[] executors;
    private ExecutorService dynamicExecutor = newSingleThreadExecutor();
    private int capacity = 1;

    public InternalExecutorService(int maxCapacity) {
        capacity = Math.max(capacity, maxCapacity);
        executors = new ExecutorService[capacity];
        for (int i = 0; i < capacity; i++) {
            executors[i] = newSingleThreadExecutor();
        }
    }

    public <T> CompletableFuture<T> getKeyedAsync(Supplier<T> supplier, String key) {
        return CompletableFuture.supplyAsync(supplier, executors[getThreadId(key)]);
    }

    public <T> CompletionStage<Void> runKeyedAsync(Runnable runnable, String key) {
        return CompletableFuture.runAsync(runnable, executors[getThreadId(key)]);
    }

    public void runAsync(Runnable runnable) {
        dynamicExecutor.execute(runnable);
    }

    private int getThreadId(String key) {
        return key.hashCode() % capacity;
    }

}
