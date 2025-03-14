package giornateFAI;

import java.util.Date;
import java.util.Arrays;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * La classe per la scrittura lettura e salvataggio delle date, in particolare delle date precluse. La classe scrive le date registrate su file XML e si appoggia sul gestore per
 * la gestione dei file XML e utilizza la classe Calendario per poter ottenere info sulle date
 * 
 * @see XMLManager 
 * @see Calendario
 */
public class RegistroDate{

	private static final DateFormat formatoData =new SimpleDateFormat("yyyy-MM-dd");
	private Calendario cal;
	private Date[] datePrecluse;
	private String path;

	public RegistroDate(String path,Calendario cal){
		this.cal=cal;
		try{
			this.path=path;
			if(XMLManager.fileExists(path)){
				this.datePrecluse=XMLManager.leggiDatePrecluse(path);
			}else{
				XMLManager.cleanDates(path, cal.getTime());
			}
		}catch(Exception e){
			CliUtente.erroreRegistrazione();
		}
	}

	//costruttore implementato per debugging
	/*public RegistroDate(String path, int year, int month, int day){
		cal=new Calendario(year,month,day);
		try{
			this.path=path;
			if(XMLManager.fileExists(path)){
				if(cal.giornoDiConfigurazione()){
					System.out.println("Oggi è disponibile la configurazione delle date precluse");
				}
				this.datePrecluse=XMLManager.leggiDatePrecluse(path);
			}else{
				XMLManager.cleanDates(path, cal.getTime());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	} */

	/**
	 * Funzione che controlla se il mese salavato nel file XML corrisponde effetitvamente al mese corrente, ovvero che per il mese corrente la 
	 * configuraizone è gia avvenuta.
	 * @param path il path in cui cercare il file XML
	 * @return false se il mese corrente è superiore al mese registrato o se non trova il file XML, true altrimenti
	 */
	public boolean meseGiaConfigurato(String path) {
		if(XMLManager.fileExists(path)){
			String  ultimaDataStr = XMLManager.leggiVariabile(path, "meseCorrente");
			try {
				Date ultimaDataSalvata = formatoData.parse(ultimaDataStr);
				int ultimoMese = new Calendario(ultimaDataSalvata).getMonth();
				int meseOdierno = new Calendario().getMonth();
				if (meseOdierno > ultimoMese) return false;
				else return true;
			} catch (ParseException e) {
				CliUtente.erroreRegistrazione();
			}
		}
		return false;
	}

	/**
	 * Funziona che prende le date da scrivere in input e le invia in scrittura al manager senza sovrascrivere le date gia registrate
	 * ma aggiungendole in coda. Prima di scrivere le date controlla se vi sono date gia passate per non allungare la coda in maniera inutile.
	 * @param input la lista di nuove date escluse da registrare
	 */
	public void registraDatePrecluse(Date[] input){

		//creo un array ausiliario e lo riempio con le date non ancora passate 
		ArrayList<Date> aux=new ArrayList<Date>();
		for (Date data : this.datePrecluse) {
			if (data.after(cal.getTime())) aux.add(data);
		}

		//in coda vi aggiungo le date prese in input per non sovrascrivere le date gia salvate e aggiorno le date in memoria
		aux.addAll(Arrays.asList(input));
		datePrecluse=aux.toArray(new Date[aux.size()]);

		//mando in scrittura la lista di date aggiornata
		XMLManager.scriviDatePrecluse(this.path,cal.getTime(), datePrecluse);
	}

	public Date[] getDatePrecluse(){
		//System.out.println("Mandando date precluse in quantità di "+this.datePrecluse.length);
		return this.datePrecluse;
	}
}
