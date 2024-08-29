package com.example.demo.pubsub.util;

public class Util {

    public static int getRandomInRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


}
