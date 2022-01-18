package org.tema.santa.workshop;

import java.util.Random;

public class Ren extends Thread {

	private int number;
	private Fabrica factories[];
	private TransferGift giftQueue;

	public Ren(Fabrica factories[], int number, TransferGift giftQueue) {
		this.factories = factories;
		this.number = number;
		this.giftQueue = giftQueue;

	}

	public void run() {

		while(true) {

			// Getting the gift from the factory
			int gift = getGiftFromFactory();


			if(gift != 0) {
				System.out.println("Reindeer " + number + " received gift " + gift);

				// Giving the gift to Santa
				giveGiftToSanta(gift);
			}

			// Sleeping between 10 and 30 milliseconds
			Random rand = new Random();
			long milis = rand.nextInt(30) + 10;
			try {
				Thread.sleep(milis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void giveGiftToSanta(int gift) {
		giftQueue.giveGift(gift);
		System.out.println("Reindeer " + number + " gave gift " + gift + " to Santa");
	}

	private int getGiftFromFactory() {

		// Choosing a random factory from the existing ones
		Random rand = new Random();
		int factory = rand.nextInt(Atelier.nrFactories) + 0;

		// Requesting a gift from the chosen factory
		return factories[factory].getGift();
	}
}
