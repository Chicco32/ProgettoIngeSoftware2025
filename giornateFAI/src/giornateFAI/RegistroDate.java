package giornateFAI;

import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;

public class RegistroDate{
	private Calendario cal;
	private Date[] datePrecluse;
	private String path;

	public RegistroDate(String path,Calendario cal){
		this.cal=cal;
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
	}

	public RegistroDate(String path){
		cal=new Calendario();
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
	}

	//costruttore implementato per debugging
	public RegistroDate(String path, int year, int month, int day){
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
	}


	public void registraDatePrecluse(Date[] input){
		ArrayList<Date> aux=new ArrayList<Date>(Arrays.asList(datePrecluse));
		aux.addAll(Arrays.asList(input));
		datePrecluse=aux.toArray(new Date[aux.size()]);
		XMLManager.scriviDatePrecluse(this.path,cal.getTime(), datePrecluse);
	}


	public Date[] getDatePrecluse(){
		System.out.println("Mandando date precluse in quantità di "+this.datePrecluse.length);
		return this.datePrecluse;
	}
}
