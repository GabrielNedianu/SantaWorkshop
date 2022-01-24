package org.tema.santa.workshop.tasks;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clasa proprie a barierei
 * 
 * @author gabriel_nedianu
 *
 */
public class CyclicBarrier {

	private ReentrantLock barieraLock = new ReentrantLock();
	private int counter = 0;
	
	/**
	 * Numarul de fire ce trebuie sa ajunga la bariera 
	 */
	private int parties;
	
	/** 
	 * Constructor 
	 * 
	 * @param parties	Numarul de fire ce trebuie sa ajunga la bariera 
	 */
	public CyclicBarrier(int parties) {
		this.parties = parties;
	}
	
	public void await() throws InterruptedException, BrokenBarrierException {
		
		try {
			barieraLock.lock();		// Se modifica numarul de elfi ce se odihnesc
			counter++; 
		}	finally {
			barieraLock.unlock();
		}
		
		while(counter < parties) {
			try {
				// Se asteapta ceilalti elfi sa ajunga pentru a putea deschide bariera
				Thread.sleep(10);			
			} catch (InterruptedException e) {
				throw e;
			}
		}
	}
}
