package org.tema.santa.workshop.tasks;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.tema.santa.workshop.Atelier;
import org.tema.santa.workshop.Elf;
import org.tema.santa.workshop.Fabrica;
import org.tema.santa.workshop.utils.LoggerUtil;

/**
 * Clasa reprezentand un Elf ca un Thread pentru taskurile 3 si 4
 * acest elf are in componenta sa o bariera ciclica
 * 
 * @author gabriel_nedianu
 *
 */
public class ElfCuBariera extends Elf {
	
	/**
	 * bariera folosita pentru odihnirea pe zona diagonalei principale
	 */
	private CyclicBarrier barieraElf;
	
	/**
	 * Constructor
	 * 
	 * @param nrElf				numarul elfului (ID-ul acestuia)
	 * @param x					pozitia pe x
	 * @param y					pozitia pe y
	 * @param fabricaElfului	fabrica din care face parte elful
	 * @param barieraElf		bariera folosita pentru odihnirea pe zona diagonalei principale
	 */
	public ElfCuBariera(int nrElf, int x, int y, Fabrica fabricaElfului, CyclicBarrier barieraElf) {
		super(nrElf, x, y, fabricaElfului);
		this.barieraElf = barieraElf;
	}
	
	@Override
	public void run() {

		while (true) {

			// Elful se muta prin fabrica
			fabrica.moveElf(this);

			if(Math.abs(x - y) <= 1) {
				LoggerUtil.infoElf("Elful cu numarul " + nrElf + " se odihneste pe pozitia " + x + ", " + y);
				try {
					barieraElf.await();		// Dupa ce elful ajunge aici, asteapta
				} catch (InterruptedException | BrokenBarrierException e) { /*Do nothing*/}
			}

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
}
