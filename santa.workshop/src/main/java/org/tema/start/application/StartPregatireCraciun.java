package org.tema.start.application;

import org.tema.santa.workshop.Atelier;
import org.tema.santa.workshop.Santa;
import org.tema.santa.workshop.TransferGift;

public class StartPregatireCraciun {

	public static void main(String[] args) {

		// Creating the gift transfer queue
		TransferGift giftQueue = new TransferGift();

		// Creating Santa (with help from God)
		Santa santa = new Santa(giftQueue);

		// Creating Santa's workshop
		Atelier workshop = new Atelier(giftQueue);

		// Starting factory creation
		workshop.createFabrici();

		// Santa starts receiving gifts
		santa.start();
	}
	
	public static void stopProducereCadouri() {
		// TODO: stop the application
	}
}
