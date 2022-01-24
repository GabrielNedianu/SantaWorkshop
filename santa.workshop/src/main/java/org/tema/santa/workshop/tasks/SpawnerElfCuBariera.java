package org.tema.santa.workshop.tasks;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

import org.tema.santa.workshop.Atelier;
import org.tema.santa.workshop.Elf;
import org.tema.santa.workshop.Fabrica;
import org.tema.santa.workshop.SpawnerElf;
import org.tema.santa.workshop.utils.LoggerUtil;

public class SpawnerElfCuBariera extends SpawnerElf {
	
	/**
	 * Bariera folosita pentru elfii din fiecare fabrica care stationeaza in zona diagonalei principale
	 */
	private CyclicBarrier barieraElf;

	public SpawnerElfCuBariera(Fabrica fabrica) {
		super(fabrica);
		barieraElf = new CyclicBarrier(fabrica.getN());
	}

	@Override
	public void run() {
		
		// La fiecare interval, se incearca sa se spawneze un Elf Cu Bariera
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
			// Pentru taskurile 2,3 si 4 se vor putea genera mai multi elfi(pana la N)
		if(fabrica.nrElfiInFabrica() < dimFabrica + 1) {

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
			
			Elf elf = new ElfCuBariera(Atelier.NR_ELF_CURENT.incrementAndGet(), x, y, fabrica, barieraElf);

			// Incercam introducerea elfului in fabrica
			if(fabrica.addElf(elf)) {
				LoggerUtil.infoElf("Elful cu numarul " + elf.getNrElf() + " a fost creat in fabrica " + fabrica.getNumar());
			}

			spawnElfLock.unlock();	//Dupa ce s-a adaugat elful, deschid accesul altor spawnere
		}

		fabricaLock.unlock();	// Deschid fabrica
	}
	
}
