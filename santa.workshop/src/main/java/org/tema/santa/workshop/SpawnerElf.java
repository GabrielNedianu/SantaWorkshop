package org.tema.santa.workshop;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import org.tema.santa.workshop.utils.LoggerUtil;

/**
 * Clasa ce spanwneaza elfii in fabrici la un interval aleatoriu intre 500 si 1000
 * 
 * @author gabriel_nedianu
 *
 */
public class SpawnerElf extends Thread {

	/**
	 * Folosit pentru a avea doar cate o instanta pentru fiecare thread
	 */
	private Random random = new Random();

	private Fabrica fabrica;

	public SpawnerElf(Fabrica fabrica) {
		this.fabrica = fabrica;
	}

	@Override
	public void run() {
		
		// La fiecare interval, se incearca sa se spawneze un Elf
		while(true) {
			try {
				Thread.sleep(random.nextInt(500) + (long)500);
			} catch (InterruptedException e) { /*Do nothing*/ }
			spawnElf();
		}
	}

	/**
	 * Metoda folosita la spawnarea unui elf pe o pozitie aleatorie
	 * Cand elf-ul este spawnat, se blocheaza fabrica(elfii nu se pot misca sau genera cadouri)
	 *  si numaratorul de elfi (alte spawnere nu pot crea alti elfi, deoarece doresc ca fiecare elf sa aiba un id unic)
	 */
	private void spawnElf() {

		ReentrantLock fabricaLock = fabrica.getFabricaLock();
		fabricaLock.lock();

		int dimFabrica = fabrica.getN();

		// Incerc adaugarea unui nou elf la o pozitie aleatorie daca elfii din fabrica sunt mai putini de N/2
		if(fabrica.nrElfiInFabrica() != dimFabrica / 2) {

			// Primesc lacatul pentru spawn-ul elfilor
			ReentrantLock spawnElfLock = Atelier.getSpawnElfLockLock();
			// Alte thread-uri nu trebuie sa acceseze numarul de elfi acum, deoarece va fi modificat cu adaugarea elfului
			spawnElfLock.lock();
			
			// Gasesc prima pozitie aleatorie din fabrica unde pot insera elful apoi creez si adaug elful
			int x;
			int y;
			do {
				x = random.nextInt(dimFabrica);
				y = random.nextInt(dimFabrica);
			} while (!fabrica.esteLiberLa(x, y));		// Cat timp fabrica e ocupata, caut alte date pt spawn
			
			Elf elf = new Elf(Atelier.NR_ELF_CURENT.incrementAndGet(), x, y, fabrica);

			// Incercam introducerea elfului in fabrica
			if(fabrica.addElf(elf)) {
				LoggerUtil.infoElf("Elful cu numarul " + elf.getNrElf() + " a fost creat in fabrica " + fabrica.getNumar());
			}

			spawnElfLock.unlock();	//Dupa ce s-a adaugat elful, deschid accesul altor spawnere
		}

		fabricaLock.unlock();	// Deschid fabrica
	}
	
}
