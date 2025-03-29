package ServicesAPI;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import ServicesAPI.Eccezioni.ConfigFilesException;

public class RegistroDatePrecluse extends RegistroDate {

    private Date[] datePrecluse;
	private GestoreDatePrecluse fileManager;

    public RegistroDatePrecluse(GestoreDatePrecluse fileManager) {
        super(fileManager); 
		this.fileManager = fileManager;
        String path = fileManager.getPath();
		
		try {
			this.datePrecluse=fileManager.leggiDatePrecluse();
		}
		catch (FileNotFoundException e) {
			GestoreFilesConfigurazione.creaFile(path);
			fileManager.cleanDates(calendario.getTime());
		}
         
    }

    /**
	 * Funziona che prende le date da scrivere in input e le invia in scrittura al manager senza sovrascrivere le date gia registrate
	 * ma aggiungendole in coda. Prima di scrivere le date controlla se vi sono date gia passate per non allungare la coda in maniera inutile.
	 * @param input la lista di nuove date escluse da registrare
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
	 */
	public void registraDatePrecluse(Date[] input) throws FileNotFoundException{

		//creo un array ausiliario e lo riempio con le date non ancora passate 
		ArrayList<Date> aux=new ArrayList<Date>();
		for (Date data : this.datePrecluse) {
			if (data.after(calendario.getTime())) aux.add(data);
		}

		//in coda vi aggiungo le date prese in input per non sovrascrivere le date gia salvate e aggiorno le date in memoria
		aux.addAll(Arrays.asList(input));
		datePrecluse=aux.toArray(new Date[aux.size()]);

		//mando in scrittura la lista di date aggiornata
		this.fileManager.scriviDatePrecluse(calendario.getTime(), datePrecluse);
	}

	public Date[] getDatePrecluse(){
		//System.out.println("Mandando date precluse in quantità di "+this.datePrecluse.length);
		return this.datePrecluse;
	}

	/**
	 * Ovverride del RegistroDate
	 */
	public boolean giornoDiConfigurazione() throws ConfigFilesException {
		try {
			return calendario.aperturaGiornoDiConfigurazione() && !meseGiaConfigurato(fileManager.getPath());
		} catch (ParseException e) {
			throw new ConfigFilesException("Errore nella lettura del file di configurazione", e);
		}
	}
    
}
