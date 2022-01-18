package org.tema.santa.workshop;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Atelier {

	public static int nrFactories;
	private Fabrica factories[];
	private SpawnerElf spawners[];
	public static volatile int nrTotalElves = 1;
	private static ReentrantLock elvesCounterLock = new ReentrantLock();
	private Ren reni[];
	private TransferGift giftQueue;
	public static volatile Semaphore elfRetireSemaphore = new Semaphore(0);
	private RetragereElf elfRetire = new RetragereElf();


	public Atelier(TransferGift giftQueue) {
		this.giftQueue = giftQueue;
	}

	public static ReentrantLock getElvesCounterLock() {
		return elvesCounterLock;
	}

	public void createFactories() {

		Random rand = new Random();
		nrFactories = rand.nextInt(4) + 2;
		int nrreni = rand.nextInt(10) + 8;

		factories = new Fabrica[nrFactories];
		spawners = new SpawnerElf[nrFactories];
		reni = new Ren[nrreni];


		System.out.println("There were created " + nrFactories + " factories");
		System.out.println("There were created " + nrreni + " reni");

		for(int i = 0; i < nrFactories ; ++i) {

			int N = rand.nextInt(500) + 100;
			factories[i] = new Fabrica(N, i + 1);
			spawners[i] = new SpawnerElf(factories[i]);
			System.out.println("Factory " + (i + 1) + " has N = " + N);
		}

		for(int i = 0; i < nrreni ; ++i) {
			reni[i] = new Ren(factories, i + 1, giftQueue);
		}

		for(int i = 0; i < nrFactories ; ++i) {
			spawners[i].start();
			factories[i].start();
		}

		for(int i = 0; i < nrreni ; ++i) {
			reni[i].start();
		}

		elfRetire.start();

	}
}
