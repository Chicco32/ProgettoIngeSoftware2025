package giornateFAI;

import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;

public class Calendario extends GregorianCalendar{

	public Calendario(){
		super();
	}

	public Calendario(int year,int month,int day){
		super(year,month,day);
	}
	
	private boolean eLavorativo(){
		int today=getDOW();
		int saturday=SATURDAY;
		int sunday=SUNDAY;
		return today!=saturday&&today!=sunday;
	}

	public boolean giornoDiConfigurazione(){
		return getDay()>=16&&eLavorativo();
	}

	public int getDay(){
		return this.get(DAY_OF_MONTH);
	}

	private int getDOW(){
		return this.get(DAY_OF_WEEK);
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
}
