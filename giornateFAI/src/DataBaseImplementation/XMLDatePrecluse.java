package DataBaseImplementation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.DateFormatter;

import ServicesAPI.GestoreDatePrecluse;

public class XMLDatePrecluse extends XMLManager implements GestoreDatePrecluse{

    public XMLDatePrecluse(String path) {
        super(path);
    }
    
    private static final DateFormat formatoData=new SimpleDateFormat("yyyy-MM-dd");

    public void scriviDatePrecluse(Date today, Date[] current) {
		inizializzaWriter();
	    System.out.println("Inizio Scrittura");
	    DateFormatter form=new DateFormatter(formatoData);
	    try{
		xmlw.writeStartDocument("utf-8", "1.0");
		xmlw.writeStartElement("registro");
		xmlw.writeStartElement("meseCorrente");
		xmlw.writeCharacters(form.valueToString(today));
		xmlw.writeEndElement();
		xmlw.writeStartElement("datePrecluse");
		if(current.length!=0){
			xmlw.writeCharacters("[");
			for(int i=0;i<current.length-1;i++){
				xmlw.writeCharacters(form.valueToString(current[i])+",");
			}
			xmlw.writeCharacters(form.valueToString(current[current.length-1])+"]");
		}
		xmlw.writeEndElement();
		xmlw.writeEndElement();
	    }catch(Exception e){
		    System.out.println("Errore nella scrittura del log del registro delle date:");
		}
		chiudiWriter();
	    //System.out.println("Fine scrittura");
    }

    public Date[] leggiDatePrecluse(){
		inzializzaReader();
		String[] aux={leggiVariabile("datePrecluse")};
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
		}		 
    	else {
	    	res=new Date[0];
    	}
		chiudiReader();
		return res;
    }

	public void cleanDates(Date data){
		scriviDatePrecluse(data,new Date[0]);
	}

}
