package org.tema.santa.workshop;

import org.tema.santa.workshop.utils.LoggerUtil;
import org.tema.santa.workshop.utils.RandomUtil;
import org.tema.start.application.PregatirePentruCraciun;

/**
 * Clasa simuland mosul
 * 
 * @author gabriel_nedianu
 *
 */
public class Santa extends Thread {

	private TransferGift giftQueue;
	private int numarCadouriAsteptate = RandomUtil.getNrCadouriDorite();
	private int numarCadouriCurente = 0;

	/**
	 * Constructor
	 */
	public Santa(TransferGift giftQueue) {
		this.giftQueue = giftQueue;
		System.out.println("Mosul doreste sa se pregateasca " + numarCadouriAsteptate + " cadouri.");
	}

	@Override
	public void run() {
		while(true) {
			int cadou = giftQueue.receiveCadou();
			numarCadouriCurente++;					// Mosul a primit inca un cadou
			LoggerUtil.infoCadou("4. Santa a primit cadoul cu id-ul " + cadou);

			if (numarCadouriCurente >= numarCadouriAsteptate) {
				PregatirePentruCraciun.stopProducereCadouri();
			}
		}
	}

}
