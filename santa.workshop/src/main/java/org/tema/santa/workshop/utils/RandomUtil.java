package org.tema.santa.workshop.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clasa creata cu un singur Random ce ne va ajuta la generarea tuturor variabilelor, dar si la generearea
 * id-urilor unice ale cadourilor
 * 
 * @author gabriel_nedianu
 *
 */
public class RandomUtil {
	
	/**
	 * Counter-ul pentru numarul cadourilor
	 */
	private static final AtomicInteger counter = new AtomicInteger();

	/**
	 * @return un id unic pentru cadou
	 */
	public static int getIdCadouUnic() {
		return counter.incrementAndGet();
	}
	
	
	/**
	 * Folosit pentru a avea un seed unic pentru generarea numerelor
	 */
	static Random random = new Random();
	
	/**
	 * @return un numar random de fabrici intre 2 si 5
	 */
	public static int getNrFabrici() {
		return random.nextInt(4) + 2;
	}
	
	/**
	 * @return o dimensiune random a fabricii intre 100 si 500
	 */
	public static int getFabricaN() {
		return random.nextInt(400) + 100;
	}
	
	/**
	 * @return un numar random de reni intre 8 si 18
	 */
	public static int getNrReni( ) {
		return random.nextInt(10) + 8;
	}
	
	/**
	 * Genereaza ordinea mutarilor in care elful va incerca sa se miste
	 * 1 , 2 , 3 , 4 -> sus, dreapta, jos, stanga
	 * 
	 * @return A List with moves order shuffled
	 */
	public static List<Integer> getRandomMoves(){
		List<Integer> mutari = Arrays.asList(1,2,3,4);
		Collections.shuffle(mutari);
		return mutari;
	}
}
