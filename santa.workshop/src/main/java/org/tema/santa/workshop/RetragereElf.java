package org.tema.santa.workshop;

/**
 * Clasa folosita pentru a le permite elfilor sa se retraga dupa ce au indeplinit anumite conditii
 * 
 * @author gabriel_nedianu
 *
 */
public class RetragereElf extends Thread {

	public void run() {

		while(true) {
			// Releasing a permit for an elf to retire
			Atelier.retragereElfSemaphore.release();

			// Sleeping 200 milliseconds
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) { /*Do nothing*/ }
		}
	}




}
