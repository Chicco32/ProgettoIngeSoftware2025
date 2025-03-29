

import java.sql.Connection;

import Controller.Avvio;
import DataBaseImplementation.ConnessioneSQL;
import DataBaseImplementation.LoginSQL;
import Presentation.CliNotifiche;

public class App {
    public static void main(String[] args) throws Exception {
	
        Connection connection;
        //Crea connessioni diverse in base al tipo di server a cui accede, connection è un
	    if(args.length==0){
			connection = ConnessioneSQL.getConnection();
	    }else if(args.length>=3){
			connection = ConnessioneSQL.getConnection(args[0],args[1],args[2]);
	    }else{
		    connection = ConnessioneSQL.getConnection();
	    }

		if (notificaConnesione(connection)) {
			Avvio avvio = new Avvio(new LoginSQL());
			avvio.avviaApp();
		}
    }

	private static boolean notificaConnesione(Connection connection) {
	if (connection != null){
		CliNotifiche.avvisa(CliNotifiche.CONNESSIONE_RIUSCITA);
		return true;
	}
	else CliNotifiche.avvisa(CliNotifiche.ERRORE_CONNESSIONE);
	return false;
    }

}
