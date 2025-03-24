package ServicesAPI;

import java.util.GregorianCalendar;
import java.util.Date;
import java.util.function.Function;
import java.util.ArrayList;

/**
 * Classe di utilità per la gestione di eventi e date interpellabile per avere informazioni sulla data corrente e su alcuni formati.
 * In particolare il calendario fa riferimento e da informazioni a una specifica data su cui viene creato, se la data non è specificata durante la creazione viene 
 * impostata la data corrente.
 * Si basa sul calendario gregoriano.
 * 
 * @see GregorianCalendar
 */
public class Calendario extends GregorianCalendar{

	/**
	 * Costruisce un oggetto calendario basato sul {@code GregorianCalendar} impostato sulla data odierna nel fuso orario locale
	 */
	public Calendario(){
		super();
	}

	/**
	 * costruisce un oggetto calendario gia settato sulla data inserita
	 * @param data la data su cui impostare il calendario
	 */
	public Calendario(Date data) {
		super();
		this.setTime(data);
	}

	/**
	 * Costruisce un oggetto calendario basato sul {@code GregorianCalendar} impostato sulla data inserita utile per il debugging
	 * @param year
	 * @param month
	 * @param day
	 */
	public Calendario(int year,int month,int day){
		super(year,month,day);
	}
	
	/**
	 * Funzione per sapere se la data odierna ricade in un giorno lavorativo della settimana
	 * @return false se il giorno corrente è sabato o domenica, true altrimenti
	 */
	private boolean eLavorativo(){
		int today=getDOW();
		int saturday=SATURDAY;
		int sunday=SUNDAY;
		return today!=saturday&&today!=sunday;
	}

	/**
	 * Funzione per sapere se si è passato il giorno di configurazione. Per giorno di configurazone si intende il il 16esimo giorno del mese.
	 * Questo perchè le attività decritte anche nel diagramma richiedono l'esecuzione dal primo giorno utile dopo il 16 del mese i.
	 * @return true se è passato il giorno di configurazione e la giornata odierna è lavorativa
	 */
	public boolean aperturaGiornoDiConfigurazione(){
		return getDay()>=16&&eLavorativo();
	}

	/**
	 * Funzione che restituisce il numero del giorno corrente
	 * @return il numero del giorno da 1 a 31
	 */
	public int getDay(){
		return this.get(DAY_OF_MONTH);
	}


	/**
	 * Funzione che restituisce il numero del giorno della settimana. Da notare come i numeri sono progressivi e partono dalla Domenica con valore 1 fino
	 * a Sabato con valore 7
	 * @return il numero del giorno da 1 a 7
	 */
	public int getDOW(){
		return this.get(DAY_OF_WEEK);
	}

	/**
	 * Funzione che restiturisce il numero del mese corrente. Attenzione da notare come i valori siano sfasati di una posizione partendo da Gennaio con valore 0
	 * fino a Dicembre con valore 11. Questo è causato dalla struttura di Gregorian Calnedar per unire il calendario Gregoriano e Giuliano
	 * @return il numero del giorno da 0 a 11
	 */
	public int getMonth(){
		return this.get(MONTH);
	}
	
	/**
	 * Funzione che restituisce il numero che rappresenta l'anno corrente
	 * @return il valore dell'anno corrente
	 */
	public int getYear() {
		return this.get(YEAR);
	}

	/**
	 * Funzione che restituisce un oggetto {@code Date} rappresentante la data inserita nel metodo.
	 * @param year
	 * @param month
	 * @param day
	 * @return la data inserita in formato {@code Date}
	 */
	public static Date getDate(int year, int month, int day){
		return (new GregorianCalendar(year,month,day).getTime());
	}

	/**
	 * Funzione che restituisce un oggetto {@code Date} rappresentante la data in cui è stata settato il calendario.
	 * In particolare se evocato su un calendario creato con 
	 * <pre> new Calendario(); </pre> 
	 * ritorna la data odierna.
	 * @return la data del calendario in formato {@code Date}
	 */
	public Date getData(){
		return this.getTime();
	}
	/**
	 * Azzera i campi del calendario più fini rispetto al giorno
	 */
	public void onlyDay(){
		this.set(HOUR,0);
		this.set(MINUTE,0);
		this.set(SECOND,0);
		this.set(MILLISECOND,0);
	}
	/**
	 * funzione che dato un numero di mesi e conoscendo la data attuale calcola una data del mese che per il dominio applicativo è il mese i+delay
	 * @param delay il numero di mesi di cui spostarsi
	 */
	public static Date getTargetMonth(int delay){
		Calendario aux=new Calendario();
		aux.add(-15,DATE);
		aux.add(delay,MONTH);
		return aux.getTime();
	}
	public static Date[] scan(DateRange toScan, Function<Calendario,Boolean> filter){
		ArrayList<Date> aux=new ArrayList<Date>();
		Calendario day=new Calendario();
		day.setTime(toScan.getStartDate());
		while(!day.after(toScan.getEndDate())){
			if(filter.apply(day)){
				aux.add(day.getTime());
			}
			day.add(1,DATE);
		}
		return aux.toArray(new Date[0]);
	}
	/**
	 * Metodo che restituisce il DateRange che contiene l'intero mese della data data in input
	 * @param day la data di cui ci serve il mese
	 */
	public static DateRange getWholeMonth(Date day){
		Calendario inizio=new Calendario();
		inizio.setTime(day);
		inizio.onlyDay();
		Calendario fine=(Calendario) (inizio.clone());
		inizio.add(1-inizio.getDay(),DATE);
		fine.add(fine.getActualMaximum(DATE)-fine.getDay(),DATE);
		return new DateRange(inizio.getTime(),fine.getTime());
	}
}
