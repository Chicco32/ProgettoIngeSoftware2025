import java.util.GregorianCalendar;
import java.util.Date;

public class RegistroDate extends GregorianCalendar{
	private Date[] datePrecluse;
	private String path;
	public RegistroDate(String path){
		super();
		this.path=path;
		if(this.MONTH!=Integer.parseInt(XMLManager.leggiVariabile(path, "meseCorrente"))){
			if(this.MONTH==Integer.parseInt(XMLManager.leggiVariabile(path, "meseCorrente"))+1){
				XMLManager.cambioMese(path);
			}else{
				XMLManager.cleanDates(path,this.MONTH);
			}
		}
		if(giornoDiConfigurazione()){
			System.out.println("Oggi è disponibile la configurazione delle date precluse");
		}
		this.datePrecluse=XMLManager.leggiDatePrecluse(path);
	}
	public boolean giornoDiConfigurazione(){
		return this.DATE>=16&&eLavorativo();
	}
	public boolean eLavorativo(){
		int today=this.DAY_OF_WEEK;
		int saturday=this.SATURDAY;
		int sunday=this.SUNDAY;
		return today!=saturday&&today!=sunday;
	}
	public void registraDatePrecluse(Date[] input){
		XMLManager.scriviDatePrecluseFuture(this.path, input);
	}
}
