package ServicesAPI;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

public class PlannerVisite {

	private RegistroDatePrecluse datePrecluse;
	private List<String> volontari;
	private Map<String, Date[]> dateDisponibili;
	

	public PlannerVisite(RegistroDatePrecluse datePrecluse) {
		this.datePrecluse = datePrecluse;

	}

	public DTObject[] creaPianoVisite(List<String> volontari) {
		return null; 
	}

	//TODO @Diego ricordati di pulire le date disponibili e precluse alla fine del piano visite
}
