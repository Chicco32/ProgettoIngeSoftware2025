package DataBaseImplementation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.DateFormatter;

import ServicesAPI.GestoreFilesConfigurazione;

/**
 * Classe implementativa di XmlManager specifica per questo programma. Esso cerca i file di configuraizone richiesti e li mostra all'applicazione.
 * In particolare implementa la verisone XML dei mietodi richiesti da Gestore File di configurazione e per farlo si appoggia sulla creaiozne di
 * oggetti di lettura e scrittura xml forniti da XMLManagare
 * 
 * @see XMLManager
 * @see GestoreFilesConfigurazione
 */
public class XMLConfigurator extends XMLManager implements GestoreFilesConfigurazione {

    private static final DateFormat formatoData=new SimpleDateFormat("yyyy-MM-dd");

    public XMLConfigurator (String path) {
        super(path);
    }

    public void scriviRegistratoreDefault (String areaCompetenza, int maxPartecipanti) {
		inizializzaWriter();
    	try {
			xmlw.writeStartDocument("utf-8", "1.0");
        	xmlw.writeCharacters(System.getProperty("line.separator"));
        	xmlw.writeStartElement("registratore");
        	xmlw.writeCharacters(System.getProperty("line.separator"));
        	xmlw.writeStartElement("maxPartecipanti");
        	xmlw.writeCharacters(String.valueOf(maxPartecipanti));
        	xmlw.writeEndElement();
        	xmlw.writeCharacters(System.getProperty("line.separator"));
        	xmlw.writeStartElement("areaCompetenza");
        	xmlw.writeCharacters(areaCompetenza);
        	xmlw.writeEndElement();
        	xmlw.writeCharacters(System.getProperty("line.separator"));
        	xmlw.writeEndElement();
    	} catch (Exception e) {
        	System.out.println("Errore nella scrittura del file:");
		}
        chiudiWriter();
    }

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

	@Override
	public String getPath() {
		return this.path;
	}

}
