package org.tema.santa.workshop;

import java.util.List;
import java.util.Random;

import org.tema.santa.workshop.utils.LoggerUtil;

/**
 * Clasa reprezentand implementarea Renului, cel care preia cadouri din fabrici si le transmite mosului
 * 
 * @author gabriel_nedianu
 *
 */
public class Ren extends Thread {
	
	/**
	 * Folosit pentru a avea doar cate o instanta pentru fiecare thread
	 */
	private Random random = new Random();

	private int nrRen; 	// Id-ul renului (numarul lui)
	private List<Fabrica> fabrici;
	private TransferGift giftQueue;

	public Ren(List<Fabrica> fabrici, int numar, TransferGift giftQueue) {
		this.fabrici = fabrici;
		this.nrRen = numar;
		this.giftQueue = giftQueue;
	}

	@Override
	public void run() {

		while(true) {

			int cadouId = extrageCadouDinFabrica();		// Renul incearca sa ia un cadou din fabrica

			if(cadouId != 0) {
				LoggerUtil.infoCadou("2. Renul " + nrRen + " a luat cadoul " + cadouId);
				transmiteCadoulMosului(cadouId);			// Renul pleaca cu cadoul sa i-l dea mosului
			}

			try {
				Thread.sleep(random.nextInt(10) + (long)10);		// Renul va dormi random intre 10 si 20 secunde
			} catch (InterruptedException e) { /*Do nothing*/ }
		}
	}
	
	/**
	 * Se trimite cadoul catre Santa
	 * 
	 * @param cadouId id-ul cadoului ce urmeaza sa fie trimis`	
	 */
	private void transmiteCadoulMosului(int cadouId) {
		giftQueue.adaugaCadou(cadouId);
		LoggerUtil.infoCadou("3. Renul " + nrRen + " a trimis cadoul " + cadouId + " catre Santa");
	}
	
	/**
	 * Se incearca extragerea unui cadou dintr-o fabrica aleatoare
	 * 
	 * @return id-ul cadoului
	 */
	private int extrageCadouDinFabrica() {

		int fabrica = random.nextInt(Atelier.NR_FABRICI);	// Selectez o fabrica aleator
		return fabrici.get(fabrica).getGift();				// Incerc sa extrag un cadou din ea
	}
}
