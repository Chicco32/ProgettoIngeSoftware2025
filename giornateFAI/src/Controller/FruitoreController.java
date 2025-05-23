package Controller;

import java.util.LinkedHashMap;
import java.util.Map;

import DataBaseImplementation.Tupla;
import Presentation.CliInput;
import Presentation.CliNotifiche;
import Presentation.CliVisualizzazione;
import ServicesAPI.Eccezioni.DBConnectionException;
import ServicesAPI.DTObject;
import ServicesAPI.Eccezioni;
import ServicesAPI.Fruitore;
import ServicesAPI.Login;
import ServicesAPI.Utente;

public class FruitoreController implements UtenteController {

	private Fruitore model;

	public FruitoreController(Fruitore model) {
		this.model = model;
	}

	public Utente getModel() {
		return this.model;
	}


	public void registrati(Login login) {
		CliNotifiche.avvisa(CliNotifiche.BENVENUTO_NUOVO_FRUITORE);
        boolean registrato = false;
        try {

            do {
                String nickname = CliInput.chiediConLunghezzaMax(CliVisualizzazione.VARIABILE_NICKNAME, CliInput.MAX_CARATTERI_NICKNAME);
                if (model.getRegistratoreIscrizioni().nomeUtenteUnivoco(nickname)) {
					model.setNickname(nickname);
                    DTObject dati = new Tupla("Fruitore", Tupla.FORMATO_UTENTE);
                    dati.impostaValore(nickname, "Nickname");
                    dati.impostaValore(CliInput.chiediConLunghezzaMax(
                        CliVisualizzazione.VARIABILE_PASSWORD, CliInput.MAX_CARATTERI_PASSWORD), "Password");
                    registrato = login.registraNuovoFruitore(dati);
                    if (registrato) CliNotifiche.avvisa(CliNotifiche.FRUITORE_CORRETTAMENTE_REGISTRATO);
                }
                else CliNotifiche.avvisa(CliNotifiche.NICKNAME_GIA_USATO);

            } while (!registrato);
        } catch (Eccezioni.DBConnectionException e) {
            //se c'è un errore salata la registrazione e va direttamente all'interno per non bloccare il flusso di esecuzione
            CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
        }  
	}

	public void accediSistema() {
		menuFruitore();
	}

	private void menuFruitore() {
        
        CliVisualizzazione.ingressoFrontendFruitore();

        //per nuove funzioni aggiungere nuove righe
        Map<String, Runnable> actions = new LinkedHashMap<>();
        actions.put("Visualizza le visite disponibili", this::visualizzaVisiteDisponibili);
		actions.put("Visualizza le visite a cui sei iscritto", this::visualizzaVisiteIscritte);
		actions.put("Visualizza le visite cancellate", this::visualizzaVisiteCancellate);
        actions.put("Iscriviti a una visita", this::iscrivitiVisita);
		actions.put("Disiscriviti da una visita", this::disiscrivitiVisita);  
        actions.put("Esci",() -> System.exit(0));

        //genero dinamicamente il menu in base alle azioni disponibili
        String[] opzioniConfiguratore = actions.keySet().toArray(new String[0]);

        while (true) {
            int scelta = CliInput.menuAzioni(getModel().getNickname(), opzioniConfiguratore);
            //scelta va da 1 a n+1, quindi se è uguale a n+1 esce
            actions.get(opzioniConfiguratore[scelta - 1]).run();
        }     
    }

	private void visualizzaVisiteDisponibili() {
		try {
			CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().VisualizzaIstanzeVisiteDisponibili(model.getNickname()), "Visite DIsponibili");
		} catch (DBConnectionException e) {
			CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
		}
	}

	private void visualizzaVisiteIscritte() {
		try {
			CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().VisualizzaIstanzeIscritte(model.getNickname()), "Iscrizioni fatte");
		} catch (DBConnectionException e) {
			CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
		}
	}

	private void visualizzaVisiteCancellate() {
		try {
			CliVisualizzazione.visualizzaRisultati(model.getVisualizzatore().VisualizzaIstanzeCancellate(model.getNickname()), "Visite cancellate");
		} catch (DBConnectionException e) {
			CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
		}
	}

	private void iscrivitiVisita() {

		CliVisualizzazione.intestazionePaginaInserimento("iscrizione");
		int maxIscrivibili, numPartecipanti, postiDisponibili;
		try {
			maxIscrivibili = model.getMaxPartecipanti(); 
			DTObject[] istanze = model.getVisualizzatore().VisualizzaIstanzeVisiteDisponibili(model.getNickname());
			CliVisualizzazione.visualizzaRisultati(istanze, "Visite Disponibili");
			boolean indietro = CliInput.tornareIndietro();
			if (indietro || istanze.length == 0) return;
			int codiceIstanza = CliInput.selezionaIstanza(istanze);
			postiDisponibili = model.getVisualizzatore().ottieniPostiDisponibili(codiceIstanza);
			do {
				numPartecipanti = CliInput.inserimentoPartecipantiVisita(1, "");
				if (numPartecipanti > maxIscrivibili || numPartecipanti > postiDisponibili) {
					CliNotifiche.avvisa(CliNotifiche.ERRORE_NUMERO_PARTECIPANTI);
				}
			} while (numPartecipanti > maxIscrivibili || numPartecipanti > postiDisponibili);
			String codice = model.getRegistratoreIscrizioni().iscrivitiVisita(codiceIstanza, model.getNickname(), numPartecipanti);

			//se il numero è esattamente quello dei posti rimasti
			if (numPartecipanti == postiDisponibili)
				model.getRegistratoreIscrizioni().aggiornaStatoPostIscrizione(codiceIstanza);
			CliNotifiche.avvisa(CliNotifiche.VISITA_CORRETTAMENTE_REGISTRATA);
			CliVisualizzazione.VisualizzaCodiceIscrizione(codice);
		} catch (DBConnectionException e) {
			CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
		} catch (Eccezioni.IscrizioneImpossibileException e) {
			CliNotifiche.avvisa(CliNotifiche.ISCRIZIONE_IMPOSSIBILE);
			CliInput.invioPerContinuare();
		}
		
	}

	private void disiscrivitiVisita() {
		
		try {
			CliVisualizzazione.intestazionePaginaRimozione("iscrizione");
			DTObject[] istanze = model.getVisualizzatore().VisualizzaIstanzeIscritte(model.getNickname());
			CliVisualizzazione.visualizzaRisultati(istanze, "Iscrizioni fatte");
			boolean indietro = CliInput.tornareIndietro();
			if (indietro || istanze.length == 0) return;
			int codiceIstanza = CliInput.selezionaIstanza(istanze);
			String codiceIscrizione = CliInput.chiediConConferma("Codice di iscrizione");
			model.getRegistratoreIscrizioni().rimuoviIscrizioneVisita(codiceIstanza, model.getNickname(), codiceIscrizione);
			model.getRegistratoreIscrizioni().aggiornaStatoPostRimozione(codiceIstanza);
			CliNotifiche.avvisa(CliNotifiche.VISITA_CORRETTAMENTE_RIMOSSA);
		}catch (DBConnectionException e) {
			CliNotifiche.avvisa(CliNotifiche.ERRORE_REGISTRAZIONE);
		} catch (Eccezioni.RimozioneIscrizioneImpossibileException e) {
			CliNotifiche.avvisa(CliNotifiche.RIMOZIONE_IMPOSSIBILE);
			CliInput.invioPerContinuare();
		}
		
	}

}
