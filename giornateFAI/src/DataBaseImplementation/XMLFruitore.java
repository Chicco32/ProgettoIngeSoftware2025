package DataBaseImplementation;

import java.io.FileNotFoundException;

import ServicesAPI.GestoreFruitore;

public class XMLFruitore extends XMLManager implements GestoreFruitore {

	public XMLFruitore(String path) {
		super(path);
	}

	@Override
	public int getMaxNumeroPartecipantiIscrizione() throws FileNotFoundException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getMaxNumeroPartecipantiIscrizione'");
	}

}
