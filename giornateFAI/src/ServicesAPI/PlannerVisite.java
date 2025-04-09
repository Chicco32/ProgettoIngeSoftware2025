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
		private Date data;
		IstanzaDiVisita(int codice,String nome,Date data){
			super("Visite programmabili",campi);
			this.codice=codice;
			this.nome=nome;
			this.data=data;
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
			dateDisponibili.put(vol,new LinkedList(Arrays.asList(date)));
		}
		DTObject[] tipiVisiteVolontari;
		try{
			tipiVisiteVolontari=visualizzatore.estraiTipiDiVisiteVolontario();
		}catch(Exception e){
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
		}catch(Exception e){
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
			Queue<Date> queue=new LinkedList(Arrays.asList(date));
			programmabili.put(tdv.getCodice(),queue);
		}
		DateRange meseDaPianificare=Calendario.getWholeMonth(meseBersaglio);
		Date[] dateDaPiazzare=Calendario.scan(meseDaPianificare,(cal)->programmabili.values().stream().anyMatch((item)->contieneData(cal,item)));
		piano=new ArrayList<>();
		for(Date data:dateDaPiazzare){
			HashMap<TipoDiVisita,Integer> prioritàVisite=priorityMappingVisite(data);
			HashMap<String,Integer> prioritàVolontari=priorityMappingVolontari(data);
			HashMap<Object,Integer> priorità=new HashMap<>(prioritàVisite);
			priorità.putAll(prioritàVolontari);
			while(!prioritàVisite.isEmpty()&&!prioritàVolontari.isEmpty()){
				ArrayList<TipoDiVisita> visiteDaAppaiare=new ArrayList(prioritàVisite.keySet());
				ArrayList<String> volDaAppaiare=new ArrayList(volontari);

				Comparator<Object> compPriorità=Comparator.comparingInt((obj)->priorità.get(obj));
				visiteDaAppaiare.sort(compPriorità);
				volDaAppaiare.sort(compPriorità);
				if(prioritàVisite.get(visiteDaAppaiare.get(0))>prioritàVolontari.get(volDaAppaiare.get(0))){
					String vol=volDaAppaiare.get(0);
					for(TipoDiVisita v:visiteDaAppaiare){
						if(appaiamentiTipoVol.get(v.getCodice()).contains(vol)){
							piano.add(new IstanzaDiVisita(v.getCodice(),vol,data));
							programmabili.get(v.getCodice()).poll();
							break;
						}
					}
					dateDisponibili.get(vol).poll();
				}else{
					TipoDiVisita tdv=visiteDaAppaiare.get(0);
					for(String vol:volDaAppaiare){
						if(appaiamentiTipoVol.get(tdv).contains(vol)){
							piano.add(new IstanzaDiVisita(tdv.getCodice(),vol,data));
							dateDisponibili.get(vol).poll();
							break;
						}
					}
					programmabili.get(tdv.getCodice()).poll();
				}
				prioritàVisite=priorityMappingVisite(data);
				prioritàVolontari=priorityMappingVolontari(data);
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

	private HashMap<TipoDiVisita,Integer> priorityMappingVisite(Date data){
		int priorità;
		HashMap<TipoDiVisita,Integer> risultati=new HashMap<>();
		for(int codice:programmabili.keySet()){
			Queue<Date> visiteFuture=programmabili.get(codice);
			priorità=visiteFuture.size();
			if(visiteFuture.contains(data)){
				priorità+=numeroVisiteIstanziate(codice);
				for(String nickname:appaiamentiTipoVol.get(codice)){
					Queue<Date> dateVol=dateDisponibili.get(nickname);
					if(dateVol.contains(data)){
						priorità+=10;
					}
				risultati.put(visite.get(codice),priorità);
				}
			}
		}
		return risultati;
	}

	private HashMap<String,Integer> priorityMappingVolontari(Date data){	
		int priorità;
		HashMap<String,Integer> risultati=new HashMap<>();
		for(String nome:volontari){
			Queue<Date> disponibilità=dateDisponibili.get(nome);
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
	

}
