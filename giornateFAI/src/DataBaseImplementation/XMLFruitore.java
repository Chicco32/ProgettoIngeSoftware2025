package DataBaseImplementation;

import java.io.FileNotFoundException;

import ServicesAPI.GestoreFruitore;

public class XMLFruitore extends XMLManager implements GestoreFruitore {

	public XMLFruitore(String path) {
		super(path);
	}

	
	public int getMaxNumeroPartecipantiIscrizione() throws FileNotFoundException {
		return Integer.parseInt(this.leggiVariabile(GestoreFruitore.MAX_PARTECIPANTI));
	}

}
