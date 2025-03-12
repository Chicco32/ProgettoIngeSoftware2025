package giornateFAI;

import giornateFAI.*;
import java.util.Date;
import static giornateFAI.RegistroDate.*;

public class ProvaRegistroDate{
	private static final String PATH="./Prova.xml";
	public static void main(String[] args){
		RegistroDate rd=new RegistroDate(PATH,2025,4,1);
		//Date[] provaDateDaAggiungere={getDate(2025,4,1),getDate(2025,4,8)};
		//rd.registraDatePrecluse(provaDateDaAggiungere);
		for(Date d:rd.getDatePrecluse()){
			System.out.println(d);
		}
	}
}
