package com.example.demo;

import com.example.demo.gameAI.GameAI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;


public class Main {
    private final Semaphore semaphore = new Semaphore(5); // 3 permits

    public static void main(String[] args) {
        Bhag bhag = new Bhag();
        System.out.println(bhag);
        GameAI game = new GameAI();
        game.boot();
    }

    public void accessResource() {
        try {
            semaphore.acquire(4);
            System.out.println(Thread.currentThread().getName() + " is accessing the resource.");
            Thread.sleep(1000); // Simulating resource access
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release(4);
            System.out.println(Thread.currentThread().getName() + " has released the resource.");
        }
    }

    static class Bhag {
        int x;
    }
}

class Solution {

    Map<String, Map<String, Long>> exchMap = new HashMap<>();

    public void run() {

        addRate("USD", "INR", 45L);
        addRate("USD", "GBP", 5L);
        addRate("GBP", "INR", 2L);
        addRate("USD", "GBP", 6L);
        addRate("GBP", "INR", 2L);
        System.out.println(getBestRate("USD", "INR", new HashSet<String>(), 1L));


    }

    public void addRate(String parentCurrency, String childCurrenct, Long rate) {
        if (!exchMap.containsKey(parentCurrency)) {
            exchMap.put(parentCurrency, new HashMap<>());
        }

        if (!exchMap.containsKey(childCurrenct)) {
            exchMap.put(childCurrenct, new HashMap<>());
        }

        exchMap.get(parentCurrency).put(childCurrenct, rate);
        exchMap.get(childCurrenct).put(parentCurrency, 1L / rate);
    }

    public Long getBestRate(String pc, String cc, Set visited, Long baseRate) {
        if (cc.equals(pc)) {
            return baseRate;
        }
        Long min = Integer.MAX_VALUE * 1L;
        visited.add(pc);

        for (String curr : exchMap.get(pc).keySet()) {
            if (visited.contains(curr)) {
                continue;
            }
            Long currRate = getBestRate(curr, cc, visited, exchMap.get(pc).get(curr));
            min = Long.min(min, currRate);
        }
        return baseRate * min;
    }
}


class Solution2 {
    private final Map<String, Map<Integer, Set<String>>> graph = new HashMap<>();

    public void run() {
        String[][] arr = new String[][]{{"Casper", "Purple", "Wayfair"}, {"Purple", "Wayfair", "Tradesy"}, {"Wayfair", "Tradesy", "Peloton"}};

        for (int i = 0; i < arr.length; i++) {
            var curr = arr[i];
            for (int j = 0; j < arr[i].length; j++) {
                for (int k = 0; k < arr[i].length; k++) {
                    if (curr[j].equals(curr[k])) {
                        continue;
                    }
                    addPair(curr[j], curr[k]);
                }
            }
        }

        for (String key : graph.keySet()) {
            System.out.println(key);
            System.out.println((getMostConnected(key)));
        }
        System.out.println("Done");
    }

    public void addPair(String p1, String p2) {
        if (!graph.containsKey(p1)) {
            graph.put(p1, new TreeMap<>());
        }
        for (Integer key : graph.get(p1).keySet()) {
            if (graph.get(p1).get(key).contains(p2)) {
                graph.get(p1).get(key).remove(p2);
                if (!graph.get(p1).containsKey(key + 1)) {
                    graph.get(p1).put(key + 1, new HashSet<>());
                }
                graph.get(p1).get(key + 1).add(p2);
                return;
            }
        }
        if (!graph.get(p1).containsKey(1)) {
            graph.get(p1).put(1, new HashSet<>());
        }
        graph.get(p1).get(1).add(p2);
    }


    public List<String> getMostConnected(String key) {
        List<Integer> keyList = new ArrayList<>(graph.get(key).keySet());
        keyList = keyList.stream().sorted().toList();
        Integer max = keyList.get(keyList.size() - 1);
//		Integer max = graph.get(key).get(max)
        return new ArrayList<>(graph.get(key).get(max));
    }
}


