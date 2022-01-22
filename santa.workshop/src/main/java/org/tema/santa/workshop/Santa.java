package org.tema.santa.workshop;

import org.tema.santa.workshop.utils.LoggerUtil;

/**
 * Clasa simuland mosul
 * 
 * @author gabriel_nedianu
 *
 */
public class Santa extends Thread {

	private TransferGift giftQueue;

	/**
	 * Constructor
	 */
	public Santa(TransferGift giftQueue) {
		this.giftQueue = giftQueue;
	}

	@Override
	public void run() {
		while(true) {
			int cadou = giftQueue.receiveCadou();
			LoggerUtil.infoCadou("4. Santa a primit cadoul cu id-ul " + cadou);
		}
	}

}
