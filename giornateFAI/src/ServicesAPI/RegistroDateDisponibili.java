package ServicesAPI;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class RegistroDateDisponibili extends RegistroDate {

	private Date[] dateDisponibili;
	private GestoreDateDisponibili fileManager;
	public RegistroDateDisponibili(GestoreDateDisponibili fileManager, String nome) {
        	super(fileManager);
		this.fileManager=fileManager;
		String path=fileManager.getPath();

		if(GestoreFilesConfigurazione.fileExists(path)){
			this.dateDisponibili=fileManager.leggiDateDisponibili(nome);
		}else{
			fileManager.cleanDates(calendario.getTime(),nome);
			this.dateDisponibili=new Date[0];
		}
	}

	public RegistroDateDisponibili(GestoreDateDisponibili fileManager){
		super(fileManager);
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
	
}
