package giornateFAI;

import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;

public class RegistroDate extends GregorianCalendar{

	private Date[] datePrecluse;
	private String path;

	public RegistroDate(String path){
		super();
		try{
			this.path=path;
			if(XMLManager.fileExists(path)){
				if(giornoDiConfigurazione()){
					System.out.println("Oggi è disponibile la configurazione delle date precluse");
				}
				this.datePrecluse=XMLManager.leggiDatePrecluse(path);
			}else{
				XMLManager.cleanDates(path, getTime());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//costruttore implementato per debugging
	public RegistroDate(String path, int year, int month, int day){
		super(year,month,day);
		try{
			this.path=path;
			if(XMLManager.fileExists(path)){
				if(giornoDiConfigurazione()){
					System.out.println("Oggi è disponibile la configurazione delle date precluse");
				}
				this.datePrecluse=XMLManager.leggiDatePrecluse(path);
			}else{
				XMLManager.cleanDates(path, getTime());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//di seguito metodi di utilità, creare classe calendairo per le loro responsablità

	public boolean giornoDiConfigurazione(){
		return getDay()>=16&&eLavorativo();
	}

	private boolean eLavorativo(){
		int today=getDOW();
		int saturday=SATURDAY;
		int sunday=SUNDAY;
		return today!=saturday&&today!=sunday;
	}

	public void registraDatePrecluse(Date[] input){
		ArrayList<Date> aux=new ArrayList<Date>(Arrays.asList(datePrecluse));
		aux.addAll(Arrays.asList(input));
		datePrecluse=aux.toArray(new Date[aux.size()]);
		XMLManager.scriviDatePrecluse(this.path,getTime(), datePrecluse);
	}

	private int getDOW(){
		return this.get(DAY_OF_WEEK);
	}

	public int getDay(){
		return this.get(DAY_OF_MONTH);
	}

	public int getMonth(){
		return this.get(MONTH);
	}
	
	public int getYear() {
		return this.get(YEAR);
	}

	public static Date getDate(int year, int month, int day){
		return (new GregorianCalendar(year,month,day).getTime());
	}

	public Date[] getDatePrecluse(){
		System.out.println("Mandando date precluse in quantità di "+this.datePrecluse.length);
		return this.datePrecluse;
	}
}
