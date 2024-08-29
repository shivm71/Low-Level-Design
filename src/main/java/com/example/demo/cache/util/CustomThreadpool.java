package com.example.demo.cache.util;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomThreadpool implements ThreadFactory {

//    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private HashMap<String, ExecutorService> threadPool = new HashMap<>();

    public ExecutorService gettThread(String key) {
        if (!threadPool.containsKey(key)) {
            synchronized (key) {
                if (!threadPool.containsKey(key)) {
                    threadPool.put(key, Executors.newSingleThreadExecutor());
                    return threadPool.get(key);
                } else {
                    return threadPool.get(key);
                }
            }
        } else {
            return threadPool.get(key);
        }
    }




    public void onThreadEnd(String key) {

    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(Runnable r) {
        return null;
    }
}
