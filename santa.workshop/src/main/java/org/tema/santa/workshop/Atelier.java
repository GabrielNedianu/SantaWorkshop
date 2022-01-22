package org.tema.santa.workshop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

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
			spawnere.add(new SpawnerElf(fabrici.get(i)));				// Creez cate un spawner de elfi pentru fiecare fabrica
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

		new RetragereElf().start();
		
		System.out.println(getAtelierData());
	}
	
	/**
	 * @return Un String ce contine toate datele initiale ale atelierului
	 */
	public String getAtelierData() {
		StringBuilder atelierData = new StringBuilder();
		atelierData.append("Atelierul nostru contine urmatoarele:\n");
		atelierData.append("- ").append(NR_FABRICI).append(" fabrici cu capacitatile:\n");
		for(int i = 0; i < NR_FABRICI ; ++i) {
			atelierData.append("\t- fabrica ").append(i+1).append(" are dimensiunea de ").append(fabrici.get(i).getN() + "X" + fabrici.get(i).getN() + " \n");
		}
		atelierData.append("- " + NR_RENI + " reni.\n");
		
		return atelierData.toString();
	}
}
