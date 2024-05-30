package com.example.demo.pubsub.queue;

public class Squeue {
    private static final Squeue INSTANCE = new Squeue();

    private Squeue() {
    }

    public static Squeue getInstance() {
        return INSTANCE;
    }
}
