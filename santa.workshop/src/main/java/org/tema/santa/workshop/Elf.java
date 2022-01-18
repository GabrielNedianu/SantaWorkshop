package org.tema.santa.workshop;

import java.util.Random;

/**
 * Clasa reprezentand un Elf ca un Thread
 * 
 * @author gabriel_nedianu
 *
 */
public class Elf extends Thread {

	private int nrElf;
	private int x;
	private int y;
	private int gift = 0;
	private Fabrica factory;

	/**
	 * Constructor
	 * 
	 * @param nrElf	numarul curent al elfului
	 * @param x		pozitia pe x
	 * @param y		pozitia pe y
	 * @param fabricaElfului	fabrica din care face parte elful
	 */
	public Elf(int nrElf, int x, int y, Fabrica fabricaElfului) {

		this.nrElf = nrElf;
		this.x = x;
		this.y = y;
		this.factory = fabricaElfului;
	}

	public void run() {

		while (true) {

			gift = gift + nrElf;

			// Moving the elf in the factory
			factory.moveElf(this);

			// The elf sleeps 30 milliseconds after creating a gift
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Try retiring an elf from the factory
			if (Atelier.elfRetireSemaphore.tryAcquire()) {
				factory.retireElf(this);
				break;
			}
		}
	}

	public void changePosition(int newX, int newY) {

		x = newX;
		y = newY;
	}

	public int getNumber() {
		return nrElf;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getGift() {
		return gift;
	}

	public void stopWork() {

		Random rand = new Random();

		long milis = rand.nextInt(50) + 10;

		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void reportPosition() {

		System.out.println("Elf " + nrElf + " is at (" + x + "," + y + ") in factory " + factory.getNumber());
	}

}
