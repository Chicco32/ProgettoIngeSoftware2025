package giornateFAI;

public class App {
    public static void main(String[] args) throws Exception {
	
        Login login;
        //login diverso in base al tipo di server a cui accede
	    if(args.length==0){
		    login= new Login();
	    }else if(args.length>=3){
		    login=new Login(args[0],args[1],args[2]);
	    }else{
		    login=new Login();
	    }

        //chiede il login finche non è valido
        Utente utente = login.loginUtente(CliUtente.chiediNickname(), CliUtente.chiediPassword());
        while (utente == null) {
            utente = login.loginUtente(CliUtente.chiediNickname(), CliUtente.chiediPassword()); 
        }
        
        if (utente.isPrimoAccesso()) {
           utente.registrati();
        }

        RegistroDate date = new RegistroDate(XMLManager.pathDatePrecluse, 2025, 2, 17);

        switch (utente.getRuolo()) {
            case "Configuratore":
            if (date.giornoDiConfigurazione()) {
                ((Configuratore) utente).aggiungiDatePrecluse(date);
            }
            CliUtente.benvenutoConfiguratore();
            BackEnd backEnd = new BackEnd(utente);
            backEnd.menuConfiguratore();
                break;
            default:
                break;
        }
    }
}
