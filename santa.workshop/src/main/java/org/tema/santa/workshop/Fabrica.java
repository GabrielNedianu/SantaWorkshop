package org.tema.santa.workshop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import org.tema.santa.workshop.utils.LoggerUtil;
import org.tema.santa.workshop.utils.RandomUtil;

public class Fabrica extends Thread {

	private int numarFabrica;
	private int N;
	private int cadouriCreate = 0;
	private boolean[][] spatiiOcupate;
	
	private ArrayList<Elf> elfi = new ArrayList<>();
	private ArrayList<Integer> cadouri = new ArrayList<>();
	
	private Semaphore semaphoreReni = new Semaphore(10);	// Maxim 10 reni pot accesa in acelasi timp o fabrica
	private ReentrantLock fabricaLock = new ReentrantLock();
	private ReentrantLock elfiListLock = new ReentrantLock();
	private ReentrantLock giftsLock = new ReentrantLock();

	/**
	 * @return Lock-ul ce blocheaza activitatea in fabrica
	 */
	public ReentrantLock getFabricaLock() {
		return fabricaLock;
	}
	
	/**
	 * Constructor
	 */
	public Fabrica(int N, int idFabrica) {

		this.spatiiOcupate = new boolean[N][N];
		this.numarFabrica = idFabrica;
		this.N = N;
	}

	public int nrElfiInFabrica() {
		return elfi.size();
	}

	/**
	 * @return dimensiunea N a fabricii (fabrica este de forma N X N)
	 */
	public int getN() {
		return N;
	}
	
	/**
	 * @return numarul unic al fabricii (ID -ul)
	 */
	public int getNumar() {
		return numarFabrica;
	}
	
	/**
	 * @return numarul de cadouri create in fabrica curenta
	 */
	public int getCadouriCreate() {
		return cadouriCreate;
	}
	
	@Override
	public void run() {

		while (true) {
			try {
				cerePozitiaElfilor();		// La fiecare 4 secunde, fabrica va cere ca fiecare elf sa si spuna pozitia
				Thread.sleep(4000);			
			} catch (InterruptedException e) { /*Do nothing*/ }
		}
	}

	/**
	 * Se muta elful prin fabrica
	 */
	public void moveElf(Elf elf) {

		try {
			fabricaLock.lock();		// Doar un elf se poate misca la un moment de timp
			if (tryMoveElf(elf)) {
				cadouriCreate++;	// S-a mai creat inca un cadou in fabrica curenta
			} else {
				elf.stopWork();		// Daca elful nu se poate misca, se opreste din munca
			}
		} finally {
			fabricaLock.unlock();
		}
	}
	
	/**
	 * Se incearca aleator mutarea elfului intr-o directie
	 * 
	 * @param elf 	Elful
	 * @param x 	Pozitia x curenta
	 * @param y		Pozitia y curenta
	 * 
	 * @return 		<code>true</code> daca elful a fost mutat in fabrica cu succes
	 */
	private boolean tryMoveElf(Elf elf) {
		
		int x = elf.getX();
		int y = elf.getY();
		List<Integer> randomMoves = RandomUtil.getRandomMoves();
		for (Integer move : randomMoves) {
			if (move == 1) {		// Daca elful a putut fi mutat in oricare din directii, returnez true
				if (tryMoveUp(elf, x, y)) {
					return true;
				}								
			} else if (move == 2) {
				if (tryMoveRight(elf, x ,y)) {
					return true;
				}
			} else if (move == 3) {
				if (tryMoveDown(elf, x ,y)) {
					return true;
				}
			} else {
				if (tryMoveLeft(elf, x ,y)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Se incearca mutarea elfului sus
	 * 
	 * @param elf 	Elful
	 * @param x 	Pozitia x curenta
	 * @param y		Pozitia y curenta
	 * 
	 * @return		<code>true</code> daca elful a fost mutat cu succes
	 */
	private boolean tryMoveUp(Elf elf, int x, int y) {
		if (x - 1 > 0 && !spatiiOcupate[x - 1][y]) {	// Elful poate fi mutat

			spatiiOcupate[x][y] = false;
			spatiiOcupate[x - 1][y] = true;		// Se muta elful pe tabla fabricii, astfel el ocupa alt spatiu

			creareCadou(elf);		// Dupa ce se muta, el creaza un cadou

			elf.mutaElfulLa(x - 1, y);		//Se schimba si pozitia elfului in interiorul lui
			cerePozitiaElfilor();
			return true;
		}
		return false;
	}
	
	/**
	 * Se incearca mutarea elfului la dreapta
	 * 
	 * @param elf 	Elful
	 * @param x 	Pozitia x curenta
	 * @param y		Pozitia y curenta
	 * 
	 * @return		<code>true</code> daca elful a fost mutat cu succes
	 */
	private boolean tryMoveRight(Elf elf, int x, int y) {
		if (y + 1 < N && !spatiiOcupate[x][y + 1]) {	// Elful poate fi mutat

			spatiiOcupate[x][y] = false;
			spatiiOcupate[x][y + 1] = true;		// Se muta elful pe tabla fabricii, astfel el ocupa alt spatiu

			creareCadou(elf);				// Dupa ce se muta, el creaza un cadou

			elf.mutaElfulLa(x, y + 1);		//Se schimba si pozitia elfului in interiorul lui
			cerePozitiaElfilor();
			return true;
		}
		return false;
	}
	
	/**
	 * Se incearca mutarea elfului im jos
	 * 
	 * @param elf 	Elful
	 * @param x 	Pozitia x curenta
	 * @param y		Pozitia y curenta
	 * 
	 * @return		<code>true</code> daca elful a fost mutat cu succes
	 */
	private boolean tryMoveDown(Elf elf, int x, int y) {
		if (x + 1 < N && !spatiiOcupate[x + 1][y]) {	// Elful poate fi mutat

			spatiiOcupate[x][y] = false;
			spatiiOcupate[x + 1][y] = true;		// Se muta elful pe tabla fabricii, astfel el ocupa alt spatiu

			creareCadou(elf);				// Dupa ce se muta, el creaza un cadou

			elf.mutaElfulLa(x + 1, y);		//Se schimba si pozitia elfului in interiorul lui
			cerePozitiaElfilor();
			return true;
		}
		return false;
	}
	
	/**
	 * Se incearca mutarea elfului la stanga
	 * 
	 * @param elf 	Elful
	 * @param x 	Pozitia x curenta
	 * @param y		Pozitia y curenta
	 * 
	 * @return		<code>true</code> daca elful a fost mutat cu succes
	 */
	private boolean tryMoveLeft(Elf elf, int x, int y) {
		if (y - 1 > 0 && !spatiiOcupate[x][y - 1]) {	// Elful poate fi mutat

			spatiiOcupate[x][y] = false;
			spatiiOcupate[x][y - 1] = true;		// Se muta elful pe tabla fabricii, astfel el ocupa alt spatiu

			creareCadou(elf);				// Dupa ce se muta, el creaza un cadou

			elf.mutaElfulLa(x, y - 1);		//Se schimba si pozitia elfului in interiorul lui
			cerePozitiaElfilor();
			return true;
		}
		return false;
	}

	/**
	 * @return <code>true</code> daca pozitia [x,y] din fabrica este libera
	 */
	public boolean esteLiberLa(int x, int y) {
		return !spatiiOcupate[x][y];
	}
	
	/**
	 * @param elf Elful ce urmeaza sa fie adaugat in fabrica curenta
	 * 
	 * @return <code>true</code> daca elful a fost adaugat cu succes
	 */
	public boolean addElf(Elf elf) {

		// Cat timp se adauga un elf, se blocheaza lista elfilor pentru a nu putea fi accesata de alte threaduri
		elfiListLock.lock();

		int x = elf.getX();
		int y = elf.getY();

		if (spatiiOcupate[x][y]) {		// Daca pozitia este ocupata, anulam operatiunea de adaugare a elfului si decrementam id-ul unic al lui
			elfiListLock.unlock();
			Atelier.NR_ELF_CURENT.decrementAndGet();
			return false;
		} else {
			spatiiOcupate[x][y] = true;
			elfi.add(elf);
			elf.start();
			elf.comunicaPozitia();
			elfiListLock.unlock();
			return true;
		}
	}
	
	/**
	 * Fiecarui elf ii este cerut sa isi comunice pozitia
	 */
	private void cerePozitiaElfilor() {

		try {
			fabricaLock.lock();		// Nu se pot misca in fabrica elfii 
			elfiListLock.lock();	// Nu se pot crea elfi noi
			giftsLock.lock();		// Renii nu pot accesa lista de cadouri

			for (Elf elf : elfi) {
				elf.comunicaPozitia();
			}
			
		} finally {
			elfiListLock.unlock();
			fabricaLock.unlock();
			giftsLock.unlock();
		}
	}

	/**
	 * @return ID-ul unic al cadoului sau 0 daca nu am putut extrage un cadou
	 */
	public int getGift() {

		int gift = 0;
		try {
			try {
				semaphoreReni.acquire();		// Renul incearca sa se alature renilor ce extrag cadouri din fabrica
			} catch (InterruptedException e) { /*Do nothing*/ }

			giftsLock.lock();			// Nu se pot extrage cadouri in acelasi timp

			try {						// Se incearca extragerea celui mai vechi cadou
				gift = cadouri.get(0);
				cadouri.remove(0);
			} catch (Exception exception) {
				gift = 0;		// Daca nu exista cadouri
			}

		} finally {
			giftsLock.unlock();
			semaphoreReni.release();
		}
		return gift;
	}

	/**
	 *	Elful va crea un cadou ce va fi adaugat in lista de cadouri din fabrica 
	 */
	private void creareCadou(Elf elf) {
		try {
			giftsLock.lock();		// Lista de cadouri nu poate fi accesata cand se adauga un cadou nou
			int idCadouCreat = elf.creazaCadou();
			cadouri.add(idCadouCreat);
		} finally {
			giftsLock.unlock();
		}
	}

	/**
	 *	Elful se va retrage din fabrica, metoda sa run se va termina.  
	 */
	public void retrageElf(Elf elf) {
		try {

			elfiListLock.lock();	// Nu se pot modifica alti elfi, cat timp cel curent este retras
			fabricaLock.lock();		// Cat timp un elf se retrage, ceilalti nu pot sa se mute
		
			elfi.remove(elf);
			spatiiOcupate[elf.getX()][elf.getY()] = false;		// Dupa ce elful se retrage, pozitia lui se elibereaza

			LoggerUtil.infoElf("Elful cu numarul " + elf.getNrElf() + " s-a retras din farbrica " + numarFabrica);
		} finally {
			elfiListLock.unlock();
			fabricaLock.unlock();
		}
	}
	
}
