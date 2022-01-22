package org.tema.santa.workshop;

/**
 * Clasa folosita pentru a transmite datele printr-o conducta
 * 
 * @author gabriel_nedianu
 *
 */
public class TransferGift {

	private volatile int head = 0;
	private volatile int tail = 0;
	private int[] cadouri = new int[10];

	/**
	 * Se scoate ultimul cadou adaugat in conducta
	 */
	public synchronized int receiveCadou() {

		int cadouID = 0;
		while (tail == head) {
			try {
				wait();		// Se asteapta golirea bufferului
			} catch (InterruptedException e) { /*Do nothing*/ }
		}

		cadouID = cadouri[head % cadouri.length];	// Se primeste cadoul
		head++;
		notifyAll();
		return cadouID;
	}

	/**
	 * Se adauga cadoul in conducta
	 */
	public synchronized void adaugaCadou(int cadou) {

		while (tail - head == cadouri.length) {
			try {
				wait();			// Se asteapta golirea bufferului
			} catch (InterruptedException e) { /*Do nothing*/ }
		}
		
		cadouri[tail % cadouri.length] = cadou;		// Se adauga cadoul in buffer
		tail++;
		notifyAll();

	}
}
