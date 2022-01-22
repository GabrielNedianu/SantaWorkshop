package org.tema.santa.workshop;

/**
 * Clasa folosita pentru a le permite elfilor sa se retraga dupa ce au indeplinit anumite conditii
 * 
 * @author gabriel_nedianu
 *
 */
public class RetragereElf extends Thread {

	@Override
	public void run() {

		while(true) {
			// La fiecare 200ms un elf care a creat destule cadouri va ptuea incerca sa se retraga

			Atelier.retragereElfSemaphore.release();

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) { /*Do nothing*/ }
		}
	}
}
