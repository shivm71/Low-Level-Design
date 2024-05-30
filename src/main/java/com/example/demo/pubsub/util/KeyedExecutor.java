package com.example.demo.pubsub.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class KeyedExecutor {
    public final Executor[] executor;
    public final ExecutorService executorService = Executors.newFixedThreadPool(3);
    public final BlockingQueue<Runnable> er = new ArrayBlockingQueue<>(10);

    public KeyedExecutor(final int threads) {
//        this.er.
        executor = new Executor[threads];
//        executorService.

        for (int i = 0; i < threads; i++) {
            executor[i] = Executors.newSingleThreadExecutor();
        }
    }

    public CompletionStage<Void> submit(final int id, final Runnable task) {
        return CompletableFuture.runAsync(task, executor[id]);
    }

    public <T> CompletionStage<T> get(final String id, final Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executor[id.hashCode() % executor.length]);
    }
}
