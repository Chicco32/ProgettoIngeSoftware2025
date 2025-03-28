package DataBaseImplementation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.DateFormatter;

import ServicesAPI.GestoreDateDisponibili;

public class XMLDateDisponibili extends XMLManager  implements GestoreDateDisponibili{

	public XMLDateDisponibili(String pathDateDisponibili) {
		super(pathDateDisponibili);
		this.path = pathDateDisponibili;
	}
	
	private static final DateFormat formatoData =new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormatter form = new DateFormatter(formatoData);

	public void registraDateDisponibili(Date today, Date[] dateDisponibili, String nomeVolontario) {
		String aux=this.path;
		this.path+=nomeVolontario + ".xml";
		inizializzaWriter();
		System.out.println("Inizio Scrittura");
		try{
			xmlw.writeStartDocument("utf-8", "1.0");
			xmlw.writeStartElement("registro");
			xmlw.writeStartElement("dataCorrente");
			xmlw.writeCharacters(form.valueToString(today));
			xmlw.writeEndElement();
			xmlw.writeStartElement("dateDisponibili");
			if(dateDisponibili.length!=0){
				xmlw.writeCharacters("[");
				for(int i=0;i<dateDisponibili.length-1;i++){
					xmlw.writeCharacters(form.valueToString(dateDisponibili[i])+",");
				}
				xmlw.writeCharacters(form.valueToString(dateDisponibili[dateDisponibili.length-1])+"]");
			}
			xmlw.writeEndElement();
			xmlw.writeEndElement();
		}catch(Exception e){
		    System.out.println("Errore nella scrittura del log del registro delle date:");
		}
		chiudiWriter();
		this.path=aux;
	}

	public Date[] leggiDateDisponibili(String nomeVolontario) {
		String store=this.path;
		this.path+=nomeVolontario;
		inzializzaReader();
		String[] aux={leggiVariabile("dateDisponibili")};
		Date[] res; 
		if(!aux[0].equals("")){aux=aux[0].substring(1,aux[0].length()-1).split(",");
			res=new Date[aux.length];
			for(int i=0;i<aux.length;i++){
				try{
					res[i]=formatoData.parse(aux[i]);
				}catch(Exception e){
					System.out.println("Errore nella lettura della data:");
				}
			}
		}else {
	    		res=new Date[0];
		}
		chiudiReader();
		this.path=store;
		return res;
	}

	public void cleanDates(Date data, String nome){
		registraDateDisponibili(data,new Date[0],nome);
	}
}
