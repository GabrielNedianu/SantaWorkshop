package org.tema.santa.workshop;

public class Santa extends Thread {


	private TransferGift giftQueue;

	public Santa(TransferGift giftQueue) {
		this.giftQueue = giftQueue;

	}

	public void run() {

		while(true) {

			int gift = giftQueue.receiveGift();
			System.out.println("Santa put gift " + gift + " in his sack");

			// Santa has magical powers, he needs no rest
		}
	}

}
