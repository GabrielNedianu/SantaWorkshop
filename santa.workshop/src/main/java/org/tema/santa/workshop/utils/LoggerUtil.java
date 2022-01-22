package org.tema.santa.workshop.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LoggerUtil {
	
	private LoggerUtil() {/*Private Constructor*/}

	/**
	 * Logger pentru a salva raporatrile pozitiilor elfilor
	 */
	static final Logger elfPositionLogger = LogManager.getLogger("elfPositionLogger");
	/**
	 * Logger pentru a salva toate informarile despre cadouri (creare, preluare de reni, transmitere la mos, primire de catre mos)
	 */
	static final Logger giftLogger = LogManager.getLogger("giftLogger");
	/**
	 * Logger pentru a salva informatiile despre crearea elfilor si retragerea lor
	 */
	static final Logger elfCreationLogger = LogManager.getLogger("elfCreationLogger");
	
	/**
	 * Pozitia elfului va fi logata in fisierul corespunzator
	 */
	public static void informarePozitie(String pozitieElf) {
		elfPositionLogger.info(pozitieElf);
	}
	
	/**
	 * Informare despre cadou, pot fi de 4 tipuri:
	 * 1-> Cadoul x creat de elful y in fabrica z.
	 * 2-> Cadoul x preluat de renul y.
	 * 3-> Cadoul x trimis mosului.
	 * 4-> Cadoul x primit de mos. 
	 */
	public static void infoCadou(String informatieDespreCadou) {
		giftLogger.info(informatieDespreCadou);
	}
	
	/**
	 * Se va salva informatia despre elf, sunt acceptate doua tipuri de informatii:
	 * 1-> Elful cu numarul x a fost creat in fabrica y.
	 * 2-> Elful cu numarul x s-a retras din fabrica y.
	 */
	public static void infoElf(String informatieDespreElf) {
		elfCreationLogger.info(informatieDespreElf);
	}
}
