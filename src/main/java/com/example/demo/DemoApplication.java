package com.example.demo;


import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class DemoApplication {

	public static void main(String[] args) throws InterruptedException {



		Map<String, LinkedList>
		Map<Integer, String> map = new HashMap<>();

		Thread thread1 = new Thread(() -> {
			map.put(1, "a");
			map.put(2, "b");
		});

		Thread thread2 = new Thread(() -> {
			map.put(1, "c");
			map.put(2, "d");
		});

		thread1.start();
		thread2.start();

		thread1.join();
		thread2.join();

		System.out.println(map);


	}

}
