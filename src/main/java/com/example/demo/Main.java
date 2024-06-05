package com.example.demo;

import com.example.demo.gameAI.GameAI;
import com.example.demo.ttt.Game;

import java.util.concurrent.Semaphore;



public class Main {
	private final Semaphore semaphore = new Semaphore(5); // 3 permits

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

	public static void main(String[] args) {
		Bhag bhag = new Bhag();
		System.out.println(bhag);
		GameAI game = new GameAI();
		game.boot();
	}
}

