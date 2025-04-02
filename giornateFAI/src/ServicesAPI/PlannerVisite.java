package ServicesAPI;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PlannerVisite {

	private RegistroDateDisponibili registroDateDisponibili;
	private VisualizzatoreConfiguratore visualizzatore;
	
	private List<String> volontari;
	private Map<String, Date[]> dateDisponibili;

	public PlannerVisite(RegistroDateDisponibili dateDisponibili, VisualizzatoreConfiguratore visualizzatore) {
		this.visualizzatore = visualizzatore;
		this.registroDateDisponibili = dateDisponibili;
	}

	public DTObject[] creaPianoVisite(List<String> volontari) {
		HashMap<String,RegistroDateDisponibili> rdd=new HashMap<>();
		HashMap<TipoDiVisita,String> appaiamenti=new HashMap<>();
		dateDisponibili=new HashMap<String,Date[]>();
		for(String vol:volontari){
			rdd.put(vol, new RegistroDateDisponibili(fileManager,datePrecluse,vol));
		}
		for(String vol:volontari){
			dateDisponibili.put(vol,rdd.get(vol).getDateDisponibili());
		}
		//TODO sbobba per capire le associazioni fra tipi di visita e volontari
		//Intenzione: raccogliere i tipi di visita e i volontari associati
		//	fare scan su tutti i giorni in cui si potrebbero piazzare visite
		//	in caso di giorno in cui è possibile assegnare una visita si assegna
		//Criteri:(da rivalutare a ogni assegnamento)
		//	Assegnare prima i volontari che possono fare meno tipi di visite nella data considerata
		//	Assegnare prima le istanze di visite con meno volontari disponibili
		//	Assegnare prima i volontari con meno disponibilità rimanenti
		//	Assegnare prima le visite con meno volontari associati
		//	Assegnare la visita a chi ha meno visite assegnate
		//	In caso di ulteriore pareggio si va un po' a caso (o si chiede?)
			
	}

	//TODO @Diego ricordati di pulire le date disponibili e precluse alla fine del piano visite
}
