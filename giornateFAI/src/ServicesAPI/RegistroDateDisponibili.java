package ServicesAPI;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.text.ParseException;

import ServicesAPI.Eccezioni.ConfigFilesException;
import ServicesAPI.Eccezioni.DBConnectionException;

public class RegistroDateDisponibili extends RegistroDate {

	private RegistroDatePrecluse datePrecluse;
	private Date[] dateDisponibili;
	private GestoreDateDisponibili fileManager;
	
	/**
	 * Costruttore con nome specifico per il volontario, usa il nome per funzioni di creazione e modifica dei file riferiti al volntario stesso
	 * @param fileManager
	 * @param datePrecluse
	 * @param nome
	 */
	public RegistroDateDisponibili(GestoreDateDisponibili fileManager, RegistroDatePrecluse datePrecluse, String nome) {
		super(fileManager);
		this.datePrecluse=datePrecluse;
		this.fileManager=fileManager;

		try {
			this.dateDisponibili=fileManager.leggiDateDisponibili(nome);
		} catch (FileNotFoundException e) {
			fileManager.cleanDates(calendario.getTime(),nome);
			this.dateDisponibili=new Date[0];
		}
	
	}

	/**
	 * Costruttore per il configuratore generale senza i diritti sui dati solo di un volontario specifico
	 * @param fileManager
	 * @param datePrecluse
	 */
	public RegistroDateDisponibili(GestoreDateDisponibili fileManager, RegistroDatePrecluse datePrecluse){
		super(fileManager);
		this.datePrecluse=datePrecluse;
		this.fileManager=fileManager;
		this.dateDisponibili=new Date[0];
	}

	/**
	 * Metodo che carica le date disponibili da un volontario specifico nell'oggetto
	 * @param nome il nickname del volontario
	 */
	public void caricaDateDisponibili(String nome) throws IllegalArgumentException{
		try{
			this.dateDisponibili=fileManager.leggiDateDisponibili(nome);
		}catch(FileNotFoundException e){
			throw new IllegalArgumentException("Il volontario non ha un file di date disponibili");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void registraDateDisponibili(Date[] input,String nome) throws FileNotFoundException {
		ArrayList<Date> aux=new ArrayList<Date>();
		aux.addAll(Arrays.asList(dateDisponibili));
		aux.addAll(Arrays.asList(input));
		dateDisponibili=aux.toArray(new Date[aux.size()]);
		this.fileManager.registraDateDisponibili(calendario.getTime(),dateDisponibili,nome);
	}

	public Date[] getDateDisponibili(){
		return this.dateDisponibili;
	}

	/**
	 * Funzione che carica nel registro e restituisce in output le date disponibili di un dato volontario
	 * @param nome il nickname del volontario
	 */
	public Date[] getDateDisponibili(String nome){
		try{
			caricaDateDisponibili(nome);
			return getDateDisponibili();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void cancellaDate(DateRange periodo,String nome){
		caricaDateDisponibili(nome);
		ArrayList<Date> aux=new ArrayList<Date>();
		ArrayList<Date> toRem=new ArrayList<Date>();
		aux.addAll(Arrays.asList(dateDisponibili));
		for(Date data: aux){
			if(periodo.insideRange(data))toRem.add(data);
		}
		aux.removeAll(toRem);
		dateDisponibili=aux.toArray(new Date[0]);
		try{
			fileManager.registraDateDisponibili(new Calendario().getTime(), dateDisponibili,nome);
		}catch(Exception e){
			System.out.println("Errore nella cancellazione delle date disponibili");
		}
	}

	/**
	 * Metodo per ottenere le date possibili su cui un volontario può dare disponibilità
	 * @param nome
	 * @return
	 * @throws DBConnectionException 
	 */
	public Date[] calcolaPossibiliDate(String nome, VisualizzatoreVolontario visualizzatore) throws DBConnectionException {
		Date meseBersaglio=Calendario.getTargetMonth(2);
		ArrayList<Date[]> parziale=new ArrayList<>();

		DTObject[] tabella = visualizzatore.estraiDOWPossibiliVolontario(nome); 
		HashMap<Integer,ArrayList<DTObject>> map=new HashMap<>();
		
		for(DTObject entry:tabella){
			int codice=(Integer)(entry.getValoreCampo("Codice Tipo di Visita"));
			if(map.get(codice)==null){
				ArrayList<DTObject> aux=new ArrayList<>();
				aux.add(entry);
				map.put(codice,aux);
			}else{
				ArrayList<DTObject> aux=map.get(codice);
				aux.add(entry);
			}
		}

		for(ArrayList<DTObject> temp : map.values()){
			parziale.add(new TipoDiVisita(temp.toArray(new DTObject[0])).getDatePossibili(meseBersaglio));
		}

		return Calendario.scan(Calendario.getWholeMonth(meseBersaglio),(day)->{
			for(Date[] array: parziale){
				if(Arrays.stream(datePrecluse.getDatePrecluse()).anyMatch((dataPreclusa)->dataPreclusa.equals(day.getTime())))return false;
				if(Arrays.asList(array).contains(day.getTime())){
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Copia di debug di calcolaPossibiliDate 
	 * @param nome
	 * @param datiTest
	 * @return
	 */
	public Date[] calcolaPossibiliDate(String nome,DTObject[] datiTest) {
		Date meseBersaglio=Calendario.getTargetMonth(2);
		ArrayList<Date[]> parziale=new ArrayList<>();
		DTObject[] tabella=datiTest;
		HashMap<Integer,ArrayList<DTObject>> map=new HashMap<>();

		for(DTObject entry:tabella){
			int codice=(Integer)(entry.getValoreCampo("Codice Tipo di Visita"));
			if(map.get(codice)==null){
				ArrayList<DTObject> aux=new ArrayList<>();
				aux.add(entry);
				map.put(codice,aux);
			}else{
				ArrayList<DTObject> aux=map.get(codice);
				aux.add(entry);
			}
		}

		for(ArrayList<DTObject> temp : map.values()){
			parziale.add(new TipoDiVisita(temp.toArray(new DTObject[0])).getDatePossibili(meseBersaglio));
		}

		return Calendario.scan(Calendario.getWholeMonth(meseBersaglio),(day) -> {
			for(Date[] array: parziale){
				if(Arrays.stream(datePrecluse.getDatePrecluse()).anyMatch((dataPreclusa) -> dataPreclusa.equals(day.getTime())))return false;
				if(Arrays.asList(array).contains(day.getTime())){
					return true;
				}
			}
			return false;
		});
	}

	public boolean giornoDiConfigurazione() throws ConfigFilesException {
		try {
			return calendario.aperturaGiornoDiConfigurazione() && !meseGiaConfigurato(fileManager.getPath());
		} catch (ParseException e) {
			throw new ConfigFilesException("Problemi con i files di configurazione", e);
		}
	}

	public void eliminaVolontario(String nickname) {
		fileManager.rimuoviDatiVolontario(nickname);
	}
    
}
