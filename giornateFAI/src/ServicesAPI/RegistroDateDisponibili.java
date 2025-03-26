package ServicesAPI;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import DataBaseImplementation.VisualizzatoreSQL;

public class RegistroDateDisponibili extends RegistroDate {
	private class TipoDiVisita{
		public DateRange periodoProposta;
		private DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		private static final HashMap<String,Integer> map=new HashMap<String,Integer>(7);
		public int[] giorniSettimana;
		public int codice;
		public TipoDiVisita(DTObject[] input){
			if(map==null){
				map.put("Domenica",calendario.SUNDAY);
				map.put("Lunedì",calendario.MONDAY);
				map.put("Martedì",calendario.TUESDAY);
				map.put("Mercoledì",calendario.WEDNESDAY);
				map.put("Giovedì",calendario.THURSDAY);
				map.put("Venerdì",calendario.FRIDAY);
				map.put("Sabato",calendario.SATURDAY);
			}
			List<Object> dati=input[0].getValori();
			this.codice=(int) dati.get(0);
			try{
				this.periodoProposta=new DateRange(df.parse((String)(dati.get(1))),df.parse((String)(dati.get(2))));
			}catch(Exception e){
				e.printStackTrace();
			}
			ArrayList<Integer> aux=new ArrayList<Integer>();
			for(DTObject entry: input){
				aux.add((Integer)(entry.getValoreCampo("Giorno della Settimana")));
			}
			Integer[] temp=aux.toArray(new Integer[aux.size()]);
			giorniSettimana=new int[temp.length];
			for(int i=0;i<temp.length;i++){
				giorniSettimana[i]=temp[i];
			}
		}
		public Date[] getDatePossibili(Date mese){
			Calendario inizioMese=new Calendario();
			inizioMese.setTime(mese);
			inizioMese.onlyDay();
			inizioMese.add(1-inizioMese.getDay(),Calendario.DATE);
			Calendario fineMese=(Calendario) (inizioMese.clone());
			fineMese.add(fineMese.getActualMaximum(Calendario.DATE)-1,Calendario.DATE);
			DateRange meseIntero=new DateRange(inizioMese.getTime(),fineMese.getTime());
			return Calendario.scan(meseIntero,(calendario)-> periodoProposta.insideRange(calendario.getTime())&&Arrays.stream(giorniSettimana).anyMatch(num->num==calendario.getDOW()));
		}
	}
	private RegistroDatePrecluse rdp;
	private Date[] dateDisponibili;
	private GestoreDateDisponibili fileManager;
	public RegistroDateDisponibili(GestoreDateDisponibili fileManager, RegistroDatePrecluse rdp, String nome) {
        	super(fileManager);
		this.rdp=rdp;
		this.fileManager=fileManager;
		String path=fileManager.getPath();
		if(GestoreFilesConfigurazione.fileExists(path)){
			this.dateDisponibili=fileManager.leggiDateDisponibili(nome);
		}else{
			fileManager.cleanDates(calendario.getTime(),nome);
			this.dateDisponibili=new Date[0];
		}
	}

	public RegistroDateDisponibili(GestoreDateDisponibili fileManager, RegistroDatePrecluse rdp){
		super(fileManager);
		this.rdp=rdp;
		this.fileManager=fileManager;
		this.dateDisponibili=new Date[0];
	}

	public void registraDateDisponibili(Date[] input,String nome){
		ArrayList<Date> aux=new ArrayList<Date>();
		aux.addAll(Arrays.asList(dateDisponibili));
		aux.addAll(Arrays.asList(input));
		dateDisponibili=aux.toArray(new Date[aux.size()]);
		this.fileManager.registraDateDisponibili(calendario.getTime(),dateDisponibili,nome);
	}

	public Date[] getDateDisponibili(){
		return this.dateDisponibili;
	}
	
	public void cancellaDate(DateRange periodo){
		ArrayList<Date> aux=new ArrayList<Date>();
		ArrayList<Date> toRem=new ArrayList<Date>();
		aux.addAll(Arrays.asList(dateDisponibili));
		for(Date data: aux){
			if(periodo.insideRange(data))toRem.add(data);
		}
		aux.removeAll(toRem);
	}
	

	public Date[] calcolaPossibiliDate(String nome) {
		Date meseBersaglio=Calendario.getTargetMonth(2);
		ArrayList<Date[]> parziale=new ArrayList<>();
		DTObject[] tabella=new VisualizzatoreSQL().estraiDOWPossibiliVolontario(nome);
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
				if(Arrays.stream(rdp.getDatePrecluse()).anyMatch((dataPreclusa)->dataPreclusa.equals(day.getTime())))return false;
				if(Arrays.asList(array).contains(day.getTime())){
					return true;
				}
			}
			return false;
		});
	}
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
		return Calendario.scan(Calendario.getWholeMonth(meseBersaglio),(day)->{
			for(Date[] array: parziale){
				if(Arrays.stream(rdp.getDatePrecluse()).anyMatch((dataPreclusa)->dataPreclusa.equals(day.getTime())))return false;
				if(Arrays.asList(array).contains(day.getTime())){
					return true;
				}
			}
			return false;
		});
	}
    
}
