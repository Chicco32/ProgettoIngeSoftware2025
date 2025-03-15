

import Controller.Avvio;

public class App {
    public static void main(String[] args) throws Exception {
	
        Avvio avvio;
        //Avvio diverso in base al tipo di server a cui accede
	    if(args.length==0){
		    avvio = new Avvio();
	    }else if(args.length>=3){
		    avvio=new Avvio(args[0],args[1],args[2]);
	    }else{
		    avvio = new Avvio();
	    }
        avvio.avviaApp();
    }
}
