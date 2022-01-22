package org.tema.start.application;

import org.tema.santa.workshop.Atelier;
import org.tema.santa.workshop.Santa;
import org.tema.santa.workshop.TransferGift;

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
		
		santa = new Santa(giftQueue);			// Creez mosul si atelierul
		atelier = new Atelier(giftQueue);

		atelier.createFabrici();				// Creez si pornesc fabricile

		santa.start();							// Pornesc threadul Mos Craciun
	}
	
	/**
	 * Metoda folosita pentru a opri executia aplicatiei dupa ce goalul a fost indeplinit
	 */
	public static void stopProducereCadouri() {
		
		endTime = System.currentTimeMillis();	// Salvez timpul terminarii pentru a calcula durata aplicatiei
		
		System.out.println(atelier.getAtelierFinalData());
		System.out.println("Pana s-au generat cadourile au trecut " + (endTime - startTime)/1000 + " secunde");
		System.exit(0);
	}
}
