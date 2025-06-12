package ServicesAPI;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collection;
import java.util.ArrayList;
import DataBaseImplementation.Tupla;
import java.util.Arrays;
import java.util.Comparator;

public class PlannerVisite {

	private final String[] campi={"codice visita","nickname volontario","data"};
	private class IstanzaDiVisita extends Tupla{

		private int codice;
		private String nome;
		IstanzaDiVisita(int codice,String nome,Date data){
			super("Visite programmabili",campi);
			this.codice=codice;
			this.nome=nome;
			impostaValore(codice,"codice visita");
			impostaValore(nome,"nickname volontario");
			impostaValore(data,"data");
		}
		public int getCodice(){
			return this.codice;
		}

		public String getNome(){
			return this.nome;
		}
	}
	private RegistroDateDisponibili registroDateDisponibili;
	private VisualizzatoreConfiguratore visualizzatore;
	
	private List<String> volontari;
	private Map<String, Queue<Date>> dateDisponibili;
	private HashMap<Integer,ArrayList<String>> appaiamentiTipoVol;
	private HashMap<Integer,TipoDiVisita> visite;
	private HashMap<Integer,Queue<Date>> programmabili;
	private ArrayList<IstanzaDiVisita> piano;

	public PlannerVisite(RegistroDateDisponibili dateDisponibili, VisualizzatoreConfiguratore visualizzatore) {
		this.visualizzatore = visualizzatore;
		this.registroDateDisponibili = dateDisponibili;
	}

	/**
	 * Metodo che crea il piano delle visite per il mese successivo.
	 * @param volontari la lista dei nickname dei volontari su cui sviluppare il piano
	 * @return il piano delle visite sotto forma di array di DTObject contenenti in quest'ordine Tipo di visita, Volontario associato,Data. In caso di errore di connessione al DB stampa l'errore e ritorna null
	 */
	public DTObject[] creaPianoVisite(List<String> volontari) {
		this.volontari=volontari;
		appaiamentiTipoVol=new HashMap<>();
		visite=new HashMap<>();
		dateDisponibili=new HashMap<String,Queue<Date>>();
		for(String vol:volontari){
			Date[] date=registroDateDisponibili.getDateDisponibili(vol);
			if(date!=null)
			dateDisponibili.put(vol,new LinkedList<>(Arrays.asList(date)));
		}
		DTObject[] tipiVisiteVolontari;
		try{
			tipiVisiteVolontari=visualizzatore.estraiTipiDiVisiteVolontario();
		}catch(Eccezioni.DBConnectionException e){
			e.printStackTrace();
			return null;
		}
		for(DTObject tupla:tipiVisiteVolontari){
			List<Object> dati=tupla.getValori();
			int codice=(int)(dati.get(0));
			if(appaiamentiTipoVol.get(codice)==null){
				ArrayList<String> volontariTipoVisita=new ArrayList<>();
				volontariTipoVisita.add((String)(dati.get(1)));
				appaiamentiTipoVol.put(codice,volontariTipoVisita);
			}else{
				ArrayList<String>volontariTipoVisita=appaiamentiTipoVol.get(codice);
				volontariTipoVisita.add((String)(dati.get(1)));
			}
		}
		DTObject[] datiTipiVisita;
		try{
			datiTipiVisita=visualizzatore.estraiGiorniTipoDiVisita();
		}catch(Eccezioni.DBConnectionException e){
			e.printStackTrace();
			return null;
		}
		HashMap<Integer,ArrayList<DTObject>> tupleTipiDiVisita=new HashMap<>();
		for(DTObject tupla:datiTipiVisita){
			List<Object> dati=tupla.getValori();
			int codice=(int)(dati.get(0));
			if(tupleTipiDiVisita.get(codice)==null){
				ArrayList<DTObject> giorniTipoVisita=new ArrayList<>();
				giorniTipoVisita.add(tupla);
				tupleTipiDiVisita.put(codice,giorniTipoVisita);
			}else{
				ArrayList<DTObject>giorniTipoVisita=tupleTipiDiVisita.get(codice);
				giorniTipoVisita.add(tupla);
			}
		}
		for(int i:tupleTipiDiVisita.keySet()){
			visite.put(i,new TipoDiVisita(tupleTipiDiVisita.get(i).toArray(new DTObject[0])));
		}
		Date meseBersaglio=Calendario.getTargetMonth(1);
		programmabili=new HashMap<>();
		for(TipoDiVisita tdv:visite.values()){
			Date[] date=tdv.getDatePossibili(meseBersaglio);
			Queue<Date> queue = new LinkedList<>(Arrays.asList(date));
			programmabili.put(tdv.getCodice(),queue);
		}
		DateRange meseDaPianificare=Calendario.getWholeMonth(meseBersaglio);
		Date[] dateDaPiazzare=Calendario.scan(meseDaPianificare,(cal)->programmabili.values().stream().anyMatch((item)->contieneData(cal,item)));
		piano=new ArrayList<>();
		for(Date data:dateDaPiazzare){
			HashMap<TipoDiVisita,Integer> prioritàVisite=priorityMappingVisite(data);
			HashMap<String,Integer> prioritàVolontari=priorityMappingVolontari(data);
			HashMap<Object,Integer> priorità = new HashMap<>(prioritàVisite);
			priorità.putAll(prioritàVolontari);
			while(!prioritàVisite.isEmpty()&&!prioritàVolontari.isEmpty()){
				ArrayList<TipoDiVisita> visiteDaAppaiare = new ArrayList<>(prioritàVisite.keySet());
				ArrayList<String> volDaAppaiare = new ArrayList<>(prioritàVolontari.keySet());

				Comparator<Object> compPriorità=Comparator.comparingInt((obj)->priorità.get(obj));
				visiteDaAppaiare.sort(compPriorità);
				volDaAppaiare.sort(compPriorità);
				if(prioritàVisite.get(visiteDaAppaiare.get(0))>prioritàVolontari.get(volDaAppaiare.get(0))){
					appaiaVolontario(volDaAppaiare.get(0),visiteDaAppaiare,data);
				}else{
					appaiaVisita(visiteDaAppaiare.get(0),volDaAppaiare,data);
				}
				prioritàVisite=priorityMappingVisite(data);
				prioritàVolontari=priorityMappingVolontari(data);
			}
			for(TipoDiVisita visita:prioritàVisite.keySet()){
				purge(data,programmabili.get(visita.getCodice()));
			}
			for(String vol:prioritàVolontari.keySet()){
				purge(data,dateDisponibili.get(vol));
			}
		}
		for(String vol:volontari){
			registroDateDisponibili.cancellaDate(meseDaPianificare,vol);
		}
		return piano.toArray(new DTObject[0]);
	}

	/**
	 * Metodo che controlla se una data data(in formato di {@code ServicesAPI.Calendario}) è contenuta in una collezione di di date
	 * @param cal la data in formato calendario
	 * @param input la collezione di date
	 * @return una boolean che rappresenta se la data è contenuta
	 */
	private boolean contieneData(Calendario cal,Collection<Date> input){
		return input.contains(cal.getTime());
	}

	/**
	 * Il priority mapping visite assegna a ogni tipo di visita da istanziare alla data in ingresso una priorità.
	 * la funzione ritorna una mappa che assegna a ogni tipo di visita una priorità espressa come intero.
	 * Per leggere la lista dei tipi di visite utilizza i campi dell'oggetto.
	 * @param data la data su cui valutare le differenti priorità
	 * @return un oggetto {@code HashMap} che contiene i tipi di viste come chiave e le priorità calcolate come valori
	 */
	private HashMap<TipoDiVisita,Integer> priorityMappingVisite(Date data){
		//priorità è un intero. Il valore di priorità è inversamente proporzionale al valore dell'intero
		int priorità;
		HashMap<TipoDiVisita,Integer> risultati = new HashMap<>();

		//prende da programmabili che associa i tipi di visite alle date in cui possono essere effettuate
		for(int codice:programmabili.keySet()){
			Queue<Date> visiteFuture = programmabili.get(codice);
			//se è possibile essere istanziata in futuro parte con un valore di priorità più alto
			priorità = visiteFuture.size();

			//prende la data tra quelle in coda e controlla se contiene la data passata in input
			if(visiteFuture.contains(data)){

				//se lo contiene procede a calcolarne la priorità associata.
				//le visite gia istanziate dello stesso tipo aumentano il valore priorità.
				priorità += numeroVisiteIstanziate(codice);

				//appaiamentiTipoVol associa a a ogni codice di tipo di visita l'arrayList di volontari che sono in grado di svolgerla
				for(String nickname:appaiamentiTipoVol.get(codice)){

					//se trova qualcuno che ha dato disponibilità nella data odierna aumenta il valore priorità
					Queue<Date> dateVol=dateDisponibili.get(nickname);
					if(dateVol != null && dateVol.contains(data)){
						priorità+=10;
					}
				
				//salva il valore di priorità
				risultati.put(visite.get(codice),priorità);
				}
			}
		}
		return risultati;
	}

	/**
	 * Il priority mapping visite assegna a ogni volontario che ha dato disponibilità nella data ingresso una priorità.
	 * La funzione ritorna una mappa che assegna a ogni volontario una priorità espressa come intero.
	 * Il comportamento è analogo a <pre> priorityMappingVisite(Date data) </pre>
	 * Per leggere la lista dei tipi di visite utilizza i campi dell'oggetto.
	 * @param data la data su cui valutare le differenti priorità
	 * @return un oggetto {@code HashMap} che contiene i tipi di viste come chiave e le priorità calcolate come valori
	 */
	private HashMap<String,Integer> priorityMappingVolontari(Date data){	
		int priorità;
		HashMap<String,Integer> risultati=new HashMap<>();
		for(String nome:volontari){
			Queue<Date> disponibilità=dateDisponibili.get(nome);
			if(disponibilità!=null){
				priorità=disponibilità.size();
				if(disponibilità.contains(data)){
					priorità+=numeroVisiteIstanziate(nome);
					for(int codice:programmabili.keySet()){
						if(appaiamentiTipoVol.get(codice).contains(nome)){
							Queue<Date> dateVisita=programmabili.get(codice);
							if(dateVisita.contains(data)){
								priorità+=10;
							}
						}
					risultati.put(nome,priorità);
					}
				}
			}
		}
		return risultati;
	}

	private int numeroVisiteIstanziate(int codice){
		int count=0;
		for(IstanzaDiVisita visita:piano){
			if(visita.getCodice()==codice)count++;
		}
		return count;
	}

	private int numeroVisiteIstanziate(String nome){
		int count=0;
		for(IstanzaDiVisita visita:piano){
			if(visita.getNome()==nome)count++;
		}
		return count;
	}
	
	private void appaiaVolontario(String vol,ArrayList<TipoDiVisita> visite,Date data){
		for(TipoDiVisita v:visite){
			if(appaiamentiTipoVol.get(v.getCodice()).contains(vol)){
				piano.add(new IstanzaDiVisita(v.getCodice(),vol,data));
				programmabili.get(v.getCodice()).poll();
				break;
			}
		}
		dateDisponibili.get(vol).poll();
	}

	private void appaiaVisita(TipoDiVisita tdv,ArrayList<String> volontari,Date data){
		int codice=tdv.getCodice();
		for(String vol:volontari){
			if(appaiamentiTipoVol.get(codice).contains(vol)){
				piano.add(new IstanzaDiVisita(codice,vol,data));
				dateDisponibili.get(vol).poll();
				break;
			}
		}
		programmabili.get(codice).poll();
	}

	private void purge(Date data,Queue<Date> lista){
		if(lista.contains(data)){
			lista.remove(data);
		}
	}
}
