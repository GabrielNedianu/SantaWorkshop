package org.tema.santa.workshop;

import java.util.Random;

import org.tema.santa.workshop.utils.LoggerUtil;
import org.tema.santa.workshop.utils.RandomUtil;

/**
 * Clasa reprezentand un Elf ca un Thread
 * 
 * @author gabriel_nedianu
 *
 */
public class Elf extends Thread {
	
	private int nrElf;
	private int x;
	private int y;
	private int cadou = 0;
	private Fabrica fabrica;
	private int nrCadouriCreate = 0;
	
	/**
	 * Folosit pentru a avea doar cate o instanta pentru fiecare thread
	 */
	private Random random = new Random();

	/**
	 * Constructor
	 * 
	 * @param nrElf				numarul elfului (ID-ul acestuia)
	 * @param x					pozitia pe x
	 * @param y					pozitia pe y
	 * @param fabricaElfului	fabrica din care face parte elful
	 */
	public Elf(int nrElf, int x, int y, Fabrica fabricaElfului) {
		this.nrElf = nrElf;
		this.x = x;
		this.y = y;
		this.fabrica = fabricaElfului;
	}
	
	@Override
	public void run() {

		while (true) {

			// Moving the elf in the factory
			fabrica.moveElf(this);

			try {
				Thread.sleep(30);	// Elful ia pauza 30ms dupa ce creaza un cadou
			} catch (InterruptedException e) { /*Do nothing*/ }

			// Daca elful a creat mai mult de 10 cadouri, acesta poate sa incerce sa se retraga din lucru
			if (nrCadouriCreate > 10 && Atelier.retragereElfSemaphore.tryAcquire()) {
				fabrica.retrageElf(this);
				Atelier.NR_ELFI_RETRASI.incrementAndGet();
				
				break;						// Bucla run se va termina daca acesta este retras
			}
		}
	}
	
	/**
	 * Se muta elful pe noile pozitii
	 */
	public void mutaElfulLa(int newX, int newY) {
		x = newX;
		y = newY;
	}

	/**
	 * @return Numarul unic al Elf-ului
	 */
	public int getNrElf() {
		return nrElf;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	/**
	 * @return Id-ul ultimului cadou creat de elf
	 */
	public int getCadou() {
		return cadou;
	}
	
	/**
	 * @return Id-ul cadoului creat de elf
	 */
	public int creazaCadou() {
		cadou = RandomUtil.getIdCadouUnic();
		LoggerUtil.infoCadou("1. Elful " + this.nrElf + " a creat cadoul cu id-ul: " + cadou);
		incrementCadou();		// Dupa ce elful creaza un cadou, se incrementeaza numarul cadourilor create de el
		return cadou;
	}
	
	/**
	 * @return numarul de cadouri create de acest elf
	 */
	public int getNrCadouriCreate() {
		return nrCadouriCreate;
	}
	
	/**
	 * Incrementeaza numarul cadourilor create de elf
	 */
	public void incrementCadou() {
		nrCadouriCreate++;
	}
	
	/**
	 * Opreste elf-ul din munca daca este inconjurat de alti elfi si nu se poate misca
	 */
	public void stopWork() {
		try {
			Thread.sleep(random.nextInt(40) + (long)10);
		} catch (InterruptedException e) { /*Do nothing*/ } 

	}
	
	/**
	 * Pozitia elfului este trimisa in fisierul corespunzator
	 */
	public void comunicaPozitia() {
		LoggerUtil.informarePozitie("Elful " + nrElf + " este la " + x + ", " + y + " in fabrica " + fabrica.getNumar());
	}

}
