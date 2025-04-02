package ServicesAPI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Date;

public class TipoDiVisita{
	
	private int codice;
	private DateRange periodoProposta;
	private DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	private static HashMap<String,Integer> map;
	private int[] giorniSettimana;		

	public TipoDiVisita(DTObject[] input){
		if(map==null){
			map=new HashMap<String,Integer>(7);
			map.put("Domenica",Calendario.SUNDAY);
			map.put("Lunedì",Calendario.MONDAY);
			map.put("Martedì",Calendario.TUESDAY);
			map.put("Mercoledì",Calendario.WEDNESDAY);
			map.put("Giovedì",Calendario.THURSDAY);
			map.put("Venerdì",Calendario.FRIDAY);
			map.put("Sabato",Calendario.SATURDAY);
		}
		List<Object> dati=input[0].getValori();
		try{
			this.codice=(int)dati.get(0);
			this.periodoProposta=new DateRange((Date)(dati.get(1)),(Date)(dati.get(2)));
		}catch(Exception e){
			e.printStackTrace();
		}
		ArrayList<Integer> aux=new ArrayList<Integer>();
		for(DTObject entry: input){
			String giorno=(String)(entry.getValoreCampo("Giorno della Settimana"));
			System.out.println("Parsing "+giorno);
			aux.add(map.get(giorno));
		}
		Integer[] temp=aux.toArray(new Integer[aux.size()]);
		giorniSettimana=new int[temp.length];
		for(int i=0;i<temp.length;i++){
			giorniSettimana[i]=temp[i];
		}
	}
	
	public int getCodice(){
		return this.codice;
	}

	public DateRange getPeriodoProposta(){
		return this.periodoProposta;
	}

	
	public Date[] getDatePossibili(Date mese){
		DateRange meseIntero=Calendario.getWholeMonth(mese);
		return Calendario.scan(meseIntero,(calendario)-> periodoProposta.insideRange(calendario.getTime())&&Arrays.stream(giorniSettimana).anyMatch(num->num==calendario.getDOW()));
	}
}
