package ServicesAPI;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PlannerVisite {

	private RegistroDatePrecluse datePrecluse;
	private List<String> volontari;
	private Map<String, Date[]> dateDisponibili;
	

	public PlannerVisite(FactoryServizi servizi) {
		this.datePrecluse = new RegistroDatePrecluse(servizi.inizializzaDatePrecluse());

	}

	public DTObject[] creaPianoVisite(List<String> volontari) {
		return null; //TODO @Diego
	}

	//TODO @Diego ricordati di pulire le date disponibili e precluse alla fine del piano visite
}
