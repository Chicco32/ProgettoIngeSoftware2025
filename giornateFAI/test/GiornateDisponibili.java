import ServicesAPI.*;
import java.util.Date;
import java.text.*;
import DataBaseImplementation.*;
import javax.swing.text.*;

public class GiornateDisponibili{
	public static void main(String[] args){
		DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
		DateFormatter form=new DateFormatter(df);
		Calendario cal= new Calendario();
		Date[] prova=new Date[5];
		try{
			prova[0]=df.parse("01/05/2025");
			prova[1]=df.parse("05/05/2025");
			prova[2]=df.parse("10/05/2025");
			prova[3]=df.parse("20/05/2025");
			prova[4]=df.parse("21/05/2025");
		}catch(Exception e){
			e.printStackTrace();
		}
		Tupla[] datiFinti=new Tupla[5];
		String[] colonne={"Codice Tipo di Visita","Giorno di Inizio (periodo anno)","Giorno di Fine (periodo anno)", "Giorno della Settimana"};
		for(int i=0;i<5;i++){
			datiFinti[i]=new Tupla("Tipo di Visita", colonne);
		}
		datiFinti[0].impostaValore(1, "Codice Tipo di Visita");
		datiFinti[0].impostaValore("2025-03-31", "Giorno di Inizio (periodo anno)");
		datiFinti[0].impostaValore("2025-07-02", "Giorno di Fine (periodo anno)");
		datiFinti[0].impostaValore("Mercoledì","Giorno della Settimana");
		datiFinti[1].impostaValore(2, "Codice Tipo di Visita");
		datiFinti[1].impostaValore("2025-03-31", "Giorno di Inizio (periodo anno)");
		datiFinti[1].impostaValore("2025-04-02", "Giorno di Fine (periodo anno)");
		datiFinti[1].impostaValore("Lunedì","Giorno della Settimana");
		datiFinti[2].impostaValore(3, "Codice Tipo di Visita");
		datiFinti[2].impostaValore("2025-05-12", "Giorno di Inizio (periodo anno)");
		datiFinti[2].impostaValore("2025-07-02", "Giorno di Fine (periodo anno)");
		datiFinti[2].impostaValore("Martedì","Giorno della Settimana");
		datiFinti[3].impostaValore(4, "Codice Tipo di Visita");
		datiFinti[3].impostaValore("2025-03-31", "Giorno di Inizio (periodo anno)");
		datiFinti[3].impostaValore("2025-07-02", "Giorno di Fine (periodo anno)");
		datiFinti[3].impostaValore("Sabato","Giorno della Settimana");
		datiFinti[4].impostaValore(4, "Codice Tipo di Visita");
		datiFinti[4].impostaValore("2025-03-31", "Giorno di Inizio (periodo anno)");
		datiFinti[4].impostaValore("2025-07-02", "Giorno di Fine (periodo anno)");
		datiFinti[4].impostaValore("Domenica","Giorno della Settimana");
		RegistroDatePrecluse rdp= new RegistroDatePrecluse( new XMLDatePrecluse("./giornateFAI/test/disponibili/datePrecluse.xml"));
		RegistroDateDisponibili rdd=new RegistroDateDisponibili(new XMLDateDisponibili("./giornateFAI/test/disponibili/dateDisponibili/"),rdp);
		rdp.registraDatePrecluse(prova);
		Date[] result=rdd.calcolaPossibiliDate("Prova", datiFinti);
		for(Date data:result){
			try{
				System.out.println(form.valueToString(data));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
