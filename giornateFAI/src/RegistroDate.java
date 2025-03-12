package giornateFAI;

import giornateFAI.*;
import java.util.GregorianCalendar;
import java.util.Date;

public class RegistroDate extends GregorianCalendar{
	private Date[] datePrecluse;
	private String path;
	public RegistroDate(String path){
		super();
		try{
			this.path=path;
			if(XMLManager.fileExists(path)){
				if(getMonth()!=Integer.parseInt(XMLManager.leggiVariabile(path, "meseCorrente"))){
					if(getMonth()==Integer.parseInt(XMLManager.leggiVariabile(path, "meseCorrente"))+1){
						XMLManager.cambioMese(path);
					}else{
						XMLManager.cleanDates(path,getMonth());
					}
				}
				if(giornoDiConfigurazione()){
					System.out.println("Oggi è disponibile la configurazione delle date precluse");
				}
				this.datePrecluse=XMLManager.leggiDatePrecluse(path);
			}else{
				XMLManager.cleanDates(path, getMonth());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public RegistroDate(String path, int year, int month, int day){
		super(year,month,day);
		try{
			this.path=path;
			if(XMLManager.fileExists(path)){
				if(getMonth()!=Integer.parseInt(XMLManager.leggiVariabile(path, "meseCorrente"))){
					if(getMonth()==Integer.parseInt(XMLManager.leggiVariabile(path, "meseCorrente"))+1){
						System.out.println("Cambio mese");
						XMLManager.cambioMese(path);
					}else{
						System.out.println("Pulizia Date");
						XMLManager.cleanDates(path,getMonth());
					}
				}
			}else{
				XMLManager.cleanDates(path, getMonth());
			}
			if(giornoDiConfigurazione()){
				System.out.println("Oggi è disponibile la configurazione delle date precluse");
			}
			this.datePrecluse=XMLManager.leggiDatePrecluse(path);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public boolean giornoDiConfigurazione(){
		return getDay()>=16&&eLavorativo();
	}
	public boolean eLavorativo(){
		int today=getDOW();
		int saturday=this.SATURDAY;
		int sunday=this.SUNDAY;
		return today!=saturday&&today!=sunday;
	}
	public void registraDatePrecluse(Date[] input){
		XMLManager.scriviDatePrecluseFuture(this.path, input);
	}
	private int getDOW(){
		return this.get(this.DAY_OF_WEEK);
	}
	private int getDay(){
		return this.get(this.DAY_OF_MONTH);
	}
	private int getMonth(){
		return this.get(this.MONTH);
	}
	public static Date getDate(int year, int month, int day){
		return (new GregorianCalendar(year,month,day).getTime());
	}

	public Date[] getDatePrecluse(){
		System.out.println("Mandando date precluse in quantità di "+this.datePrecluse.length);
		return this.datePrecluse;
	}
}
