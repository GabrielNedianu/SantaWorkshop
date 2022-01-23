package org.tema.start.application;

import org.tema.santa.workshop.Atelier;
import org.tema.santa.workshop.Santa;
import org.tema.santa.workshop.TransferGift;
import org.tema.santa.workshop.utils.LoggerUtil;

/**
 * Clasa folosita pentru a porni toate clasele aplicatiei
 * 
 * @author gabriel_nedianu
 *
 */
public class PregatirePentruCraciun {

	private static Santa santa;
	private static Atelier atelier;
	private static long startTime;
	private static long endTime;
	
	/**
	 * Main class
	 */
	public static void main(String[] args) {

		TransferGift giftQueue = new TransferGift();

		startTime = System.currentTimeMillis();	// Salvez timpul de inceput pentru a calcula durata aplicatiei
		
		atelier = new Atelier(giftQueue);		// Creez atelierul si mosul
		atelier.createFabrici();				// Creez si pornesc fabricile

		santa = new Santa(giftQueue);			

		santa.start();							// Pornesc threadul Mos Craciun
	}
	
	/**
	 * Metoda folosita pentru a opri executia aplicatiei dupa ce goalul a fost indeplinit
	 */
	public static void stopProducereCadouri() {
		
		endTime = System.currentTimeMillis();	// Salvez timpul terminarii pentru a calcula durata aplicatiei
		
		String atelierDateFinale = atelier.getAtelierFinalData();
		System.out.println(atelierDateFinale);
		LoggerUtil.infoSimulare(atelierDateFinale);
		
		System.out.println("Pana s-au generat cadourile au trecut " + (endTime - startTime)/1000 + " secunde\n");
		LoggerUtil.infoSimulare("Pana s-au generat cadourile au trecut " + (endTime - startTime)/1000 + " secunde");
		System.exit(0);
	}
}
