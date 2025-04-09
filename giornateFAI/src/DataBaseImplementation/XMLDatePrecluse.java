package DataBaseImplementation;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.DateFormatter;
import ServicesAPI.GestoreDatePrecluse;

/**
 * Classe implementativa di XMLManager specifica per questo programma. Esso cerca i file di configuraizone richiesti e li mostra all'applicazione.
 * In particolare implementa la verisone XML dei metodi richiesti da Gestore delle Date precluse e per farlo si appoggia sulla creazione di
 * oggetti di lettura e scrittura xml forniti da XMLManager
 * 
 * @see XMLManager
 * @see GestoreDatePrecluse
 */
public class XMLDatePrecluse extends XMLManager implements GestoreDatePrecluse{

    public XMLDatePrecluse(String path) {
        super(path);
    }
    
    private static final DateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");

    public void scriviDatePrecluse(Date today, Date[] current) throws FileNotFoundException {
	    if(!fileExists(path)){
		    creaFile(path);
	    }
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
		else xmlw.writeCharacters("[]");
		xmlw.writeEndElement();
		xmlw.writeEndElement();
	    }catch(Exception e){
		    System.out.println("Errore nella scrittura del log del registro delle date:");
		    e.printStackTrace();
		}
		chiudiWriter();
	    //System.out.println("Fine scrittura");
    }

    public Date[] leggiDatePrecluse() throws FileNotFoundException{
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
		try {
			scriviDatePrecluse(data,new Date[0]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
