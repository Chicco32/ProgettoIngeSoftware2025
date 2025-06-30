package ServicesAPI;

import java.util.Date;

import ServicesAPI.Eccezioni.ConfigFilesException;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * La classe per la scrittura lettura e salvataggio delle date, in particolare delle date precluse. La classe scrive le date registrate su file XML e si appoggia sul gestore per
 * la gestione dei file XML e utilizza la classe Calendario per poter ottenere info sulle date.
 * Si basa anch'essa sull'interfaccia dei file di configurazione per accedere ai dati dei files.
 * 
 * @see GestoreFilesConfigurazione 
 * @see Calendario
 */
public abstract class RegistroDate{

	protected static final DateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
	protected Calendario calendario;
	protected GestoreFilesConfigurazione fileManager;

	public RegistroDate (GestoreFilesConfigurazione fileManager) {
		this.calendario = new Calendario();
		this.fileManager = fileManager;
	}

	/**
	 * Funzione che controlla se il mese salvato nel file corrisponde effettivamente al mese corrente, ovvero che per il mese corrente la 
	 * configurazione è gia avvenuta.
	 * @param path il path in cui cercare il file XML
	 * @return false se il mese corrente è superiore al mese registrato o se non trova il file XML, true altrimenti
	 */
	protected boolean meseGiaConfigurato(String path)  throws ParseException, ConfigFilesException {
		if(fileManager.fileExists(path)){
			String ultimaDataStr;
			try {
				ultimaDataStr = fileManager.leggiVariabile("dataCorrente");
				Date ultimaDataSalvata = formatoData.parse(ultimaDataStr);
			int ultimoMese = new Calendario(ultimaDataSalvata).getMonth();
			int meseOdierno = new Calendario().getMonth();
			if (meseOdierno != ultimoMese) return false;
			else return true;
			} catch (FileNotFoundException e) {
				throw new ConfigFilesException("File non trovato", e);
			}
			
		}
		return false;
	}

	/**
	 * Funzione che specifica se il giorno di esecuzione è effettivamente un giorno di configurazione. In particolare controlla
	 * da calendario che sia un possibile giorno di configurazione e se non è gia stata fatta la configurazione del mese
	 * @return
	 * @throws Exception
	 */
	public boolean giornoDiConfigurazione(String path) throws Exception {
		return calendario.aperturaGiornoDiConfigurazione() && !meseGiaConfigurato(path);
	}

}
