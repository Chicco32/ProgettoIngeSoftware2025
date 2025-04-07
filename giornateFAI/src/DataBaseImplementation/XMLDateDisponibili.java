package DataBaseImplementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.DateFormatter;
import ServicesAPI.GestoreDateDisponibili;

/**
 * Classe implementativa di XMLManager specifica per questo programma. Esso cerca i file di configurazione richiesti e li mostra all'applicazione.
 * In particolare implementa la versione XML dei metodi richiesti da Gestore delle Date Disponibili e per farlo si appoggia sulla creazione di
 * oggetti di lettura e scrittura xml forniti da XMLManager
 * 
 * @see XMLManager
 * @see GestoreDateDisponibili
 */
public class XMLDateDisponibili extends XMLManager  implements GestoreDateDisponibili{

	public XMLDateDisponibili(String pathDateDisponibili) {
		super(pathDateDisponibili);
		this.path = pathDateDisponibili;
	}
	
	private static final DateFormat formatoData =new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormatter form = new DateFormatter(formatoData);

	public void registraDateDisponibili(Date today, Date[] dateDisponibili, String nomeVolontario) throws FileNotFoundException {
		String pathCartella = this.path;
		this.path += nomeVolontario + ".xml";
		inizializzaWriter();

		//System.out.println("Inizio Scrittura");
		
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
			else {
				xmlw.writeCharacters("[]");
			}
			xmlw.writeEndElement();
			xmlw.writeEndElement();
		}catch(Exception e){
		    System.out.println("Errore nella scrittura del log del registro delle date:");
		}

		chiudiWriter();
		this.path = pathCartella;
	}

	public Date[] leggiDateDisponibili(String nomeVolontario) throws FileNotFoundException {
		String pathCartella = this.path;
		this.path += nomeVolontario + ".xml";
		Date[] res; 

		//appurata che la cartella esiste, bisogna verificare se il file del volontario esista a sua volta
		if (fileExists(this.path)) {
			
			inzializzaReader();
			String[] aux = {leggiVariabile("dateDisponibili")};
			if(!aux[0].equals("[]")){
				aux = aux[0].substring(1,aux[0].length()-1).split(",");
				res=new Date[aux.length];
				for(int i=0;i<aux.length;i++){
					try{
						res[i]=formatoData.parse(aux[i]);
					}catch(ParseException e){
						e.printStackTrace();
					}
				}
			}
			else {
				res=new Date[0];
			}
			chiudiReader();
		}
		else{
			this.path = pathCartella;
			throw new FileNotFoundException();
		}
		this.path = pathCartella;
		return res;
	}

	public void cleanDates(Date data, String nome){
		try {
			registraDateDisponibili(data,new Date[0],nome);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void rimuoviDatiVolontario(String nickname) {
		String pathCartella = this.path;
		this.path += nickname + ".xml";
		File file = new File(this.path);
		file.delete();
		this.path = pathCartella;
	}
}
