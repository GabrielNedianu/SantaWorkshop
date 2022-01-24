package org.tema.santa.workshop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.tema.santa.workshop.tasks.SpawnerElfCuBariera;
import org.tema.santa.workshop.utils.LoggerUtil;
import org.tema.santa.workshop.utils.RandomUtil;

/**
 * Clasa reprezentand atelierul
 * 
 * Aici se creaza aleatoriu fabricile, dimensiunea lor, renii si se pornesc toate thread-urile tot din aceasta clasa.
 * 
 * @author gabriel_nedianu
 *
 */
public class Atelier {
	
	public static final int NR_FABRICI = RandomUtil.getNrFabrici();
	public static final int NR_RENI = RandomUtil.getNrReni();
	
	private List<Fabrica> fabrici = new ArrayList<>();
	private List<SpawnerElf> spawnere = new ArrayList<>();
	private List<Ren> reni = new ArrayList<>();
	
	private TransferGift giftQueue;
	
	/**
	 * Folosit pentru a contoriza numarul de elfi retrasi
	 */
	public static AtomicInteger NR_ELFI_RETRASI = new AtomicInteger();
	
	/**
	 * Folosit pentru a ii da fiecarui elf un id unic
	 */
	public static AtomicInteger NR_ELF_CURENT = new AtomicInteger();
	
	/**
	 * Semafor folosit pentru a le permite elfilor sa se retraga din munca (la anumite momente de timp se vor adauga permisiuni)
	 */
	public static volatile Semaphore retragereElfSemaphore = new Semaphore(0);
	
	/**
	 * Lock-ul care permite crearea cate unui elf la un moment de timp de catre spawnere
	 */
	private static ReentrantLock spawnElfLock = new ReentrantLock();

	/**
	 * Constructor
	 */
	public Atelier(TransferGift giftQueue) {
		this.giftQueue = giftQueue;
	}
	
	/**
	 * @return lock-ul care permite crearea cate unui elf la un moment de timp de catre spawnere
	 */
	public static ReentrantLock getSpawnElfLockLock() {
		return spawnElfLock;
	}

	/**
	 * Se creaza toate fabricile cu spawnere, apoi se creaza si renii si se incepe la final procesul 
	 * de retragere al elfilor (cand este nevoie de acesta)
	 */
	public void createFabrici() {

		for(int i = 0; i < NR_FABRICI ; ++i) {
			fabrici.add(new Fabrica(RandomUtil.getFabricaN(), i + 1));	// Creez fabricile cu dimensiune random N si numerotate de la 1 la maxim 5
			//spawnere.add(new SpawnerElf(fabrici.get(i)));				// Creez cate un spawner de elfi pentru fiecare fabrica
			
			// Decomenteaza linia urmatoare pentru a simula taskurile 3 si 4, deoarece astfel se va adauga un spawner ce genereaza si cate o bariera in fiecare fabrica
			spawnere.add(new SpawnerElfCuBariera(fabrici.get(i)));		
		}

		for(int i = 0; i < NR_RENI ; ++i) {
			reni.add(new Ren(fabrici, i + 1, giftQueue));				// Creez renii generati aleatoriu
		}

		// Pornesc toate thread-urile din simulare
		for(int i = 0; i < NR_FABRICI ; ++i) {
			spawnere.get(i).start();
			fabrici.get(i).start();
		}
		for(int i = 0; i < NR_RENI ; ++i) {
			reni.get(i).start();
		}

		//new RetragereElf().start();
		
		String atelierDateInitiale = getAtelierStartData();
		LoggerUtil.infoSimulare(atelierDateInitiale);			// Se salveaza starea initiala a atelierului
		System.out.println(atelierDateInitiale);
	}
	
	/**
	 * @return Un String ce contine toate datele initiale ale atelierului
	 */
	public String getAtelierStartData() {
		StringBuilder atelierData = new StringBuilder();
		atelierData.append("Atelierul nostru contine urmatoarele:\n");
		atelierData.append("- ").append(NR_FABRICI).append(" fabrici cu capacitatile:\n");
		for(int i = 0; i < NR_FABRICI ; ++i) {
			atelierData.append("\t- fabrica ").append(i+1).append(" are dimensiunea de ").append(fabrici.get(i).getN() + "X" + fabrici.get(i).getN() + " \n");
		}
		atelierData.append("- " + NR_RENI + " reni.\n");
		
		return atelierData.toString();
	}
	
	/**
	 * @return Un String ce contine toate datele initiale ale atelierului
	 */
	public String getAtelierFinalData() {
		StringBuilder atelierFinalData = new StringBuilder();
		atelierFinalData.append("Mosul a primit toate cadourile pe care le-a dorit:\n");
		for(int i = 0; i < NR_FABRICI ; ++i) {
			atelierFinalData.append("\t- fabrica ").append(i+1).append(" a produs ").append(fabrici.get(i).getCadouriCreate() + " cadouri \n");
		}
		
		atelierFinalData.append("\nNumar elfi creati: ").append(NR_ELF_CURENT.get()).append("\n");
		atelierFinalData.append("Numar elfi retrasi: ").append(NR_ELFI_RETRASI.get()).append("\n");
		
		return atelierFinalData.toString();
	}
}
