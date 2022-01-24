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
	
	/**
	 * Numarul elfului (ID-ul acestuia)
	 */
	protected int nrElf;
	protected int x;
	protected int y;
	protected int cadou = 0;
	/**
	 * Fabrica din care face parte elful
	 */
	protected Fabrica fabrica;
	protected int nrCadouriCreate = 0;
	
	/**
	 * Folosit pentru a avea doar cate o instanta pentru fiecare thread
	 */
	protected Random random = new Random();

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

			// Elful se muta prin fabrica
			fabrica.moveElf(this);

			// Decomenteaza liniile urmatoare pentru functionarea taskului 2
			
//			if(Math.abs(x - y) <= 1) {
//				LoggerUtil.infoElf("Elful cu numarul " + nrElf + " se odihneste pe pozitia " + x + ", " + y);
//				if(fabrica.semaphoreDiagonalaPrincipala.tryAcquire()) {
//					fabrica.elfiOpritiPeDiagonala++;
//					fabrica.semaphoreDiagonalaPrincipala.release();
//				}else {
//					continue;
//				}
//				while(fabrica.elfiOpritiPeDiagonala < fabrica.getN()) {		// Astept sa ajunga toti elfii pe diagonala
//					try {
//						Thread.sleep(10);
//					} catch (InterruptedException e) { /*Do nothing*/ }
//				}
//				fabrica.elfiOpritiPeDiagonala = 0;
//			}

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
