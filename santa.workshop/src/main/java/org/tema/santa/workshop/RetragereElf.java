package org.tema.santa.workshop;

public class RetragereElf extends Thread {

	public void run() {

		while(true) {
			
			// Releasing a permit for an elf to retire
			Atelier.elfRetireSemaphore.release();


			// Sleeping 50 milliseconds
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}




}
