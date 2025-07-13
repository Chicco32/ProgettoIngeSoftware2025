package ServiceAPITest;

import org.junit.jupiter.api.*;

import ServicesAPI.Configuratore;
import ServicesAPI.DTObject;
import ServicesAPI.Eccezioni.CoerenzaException;
import ServicesAPI.Eccezioni.DBConnectionException;
import ServicesAPI.FactoryServizi;
import ServicesAPI.Fruitore;
import ServicesAPI.GestoreConfiguratore;
import ServicesAPI.GestoreDateDisponibili;
import ServicesAPI.GestoreDatePrecluse;
import ServicesAPI.GestoreFruitore;
import ServicesAPI.Registratore;
import ServicesAPI.RegistratoreIscrizioni;
import ServicesAPI.StatiVisite;
import ServicesAPI.VisualizzatoreConfiguratore;
import ServicesAPI.VisualizzatoreFruitore;
import ServicesAPI.VisualizzatoreVolontario;
import ServicesAPI.Volontario;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import java.util.Date;
import java.util.List;


public class UsersTest {

	private class GestoreConfiguratoreStub implements GestoreConfiguratore {
		private String path = "test_config.xml";
		private String area = "area-test";
		private int max = 5;
		private boolean fileExists = false;

		public void setFileExists(boolean fileExists) {
			this.fileExists = fileExists;
		}
		
		@Override
		public String getPath() {
			return path;
		}

		@Override
		public int leggiNumeroPartecipanti() {
			return max;
		}

		@Override
		public String leggiAreaCompetenza() {
			return area;
		}

		@Override
		public void modificaAreaCompetenza(String areaCompetenza) {
			this.area = areaCompetenza;
		}

		@Override
		public void modificaMaxPartecipanti(int maxPartecipanti) {
			this.max = maxPartecipanti;
		}

		@Override
		public void scriviRegistratoreDefault(String area, int maxPartecipanti) {
			this.area = area;
			this.max = maxPartecipanti;
		}

		@Override
		public String leggiVariabile(String nome) {
			if (nome.equals("maxPartecipanti")) return String.valueOf(max);
			if (nome.equals("areaCompetenza")) return area;
			return null;
		}

		@Override
		public boolean fileExists(String path) {
			return fileExists;
		}

		@Override
		public void creaFile(String path) {}

	}

	private class RegistratoreStub implements Registratore {

		@Override
		public boolean registraNuovoVolontario(DTObject volontario) throws DBConnectionException {
			throw new UnsupportedOperationException("Unimplemented method 'registraNuovoVolontario'"); }

		@Override
		public boolean nomeUtenteUnivoco(String nomeUtente) throws DBConnectionException {
			throw new UnsupportedOperationException("Unimplemented method 'nomeUtenteUnivoco'"); }

		@Override
		public boolean registraNuovoLuogo(DTObject luogo) throws CoerenzaException, DBConnectionException {
			throw new UnsupportedOperationException("Unimplemented method 'registraNuovoLuogo'");
		}

		@Override public boolean registraNuovoTipoVisita(DTObject tipoVisita) throws DBConnectionException {
			throw new UnsupportedOperationException("Unimplemented method 'registraNuovoTipoVisita'"); }

		@Override
		public boolean associaVolontarioVisita(DTObject associazione) throws CoerenzaException, DBConnectionException {
			throw new UnsupportedOperationException("Unimplemented method 'associaVolontarioVisita'");
		}

		@Override public boolean rimozioneLuogo(String nomeLuogo) throws DBConnectionException {
			throw new UnsupportedOperationException("Unimplemented method 'rimozioneLuogo'"); }

		@Override
		public boolean rimozioneVisita(String titoloVisita) throws DBConnectionException {
			throw new UnsupportedOperationException("Unimplemented method 'rimozioneVisita'"); }

		@Override
		public boolean rimozioneVolontario(String nickname) throws DBConnectionException {
			throw new UnsupportedOperationException("Unimplemented method 'rimozioneVolontario'"); }

		@Override
		public void verificaCoerenzaPostRimozione() throws DBConnectionException {
			throw new UnsupportedOperationException("Unimplemented method 'verificaCoerenzaPostRimozione'"); }

		@Override
		public boolean registraIstanzaDiVisita(DTObject istanza) throws DBConnectionException {
			throw new UnsupportedOperationException("Unimplemented method 'registraIstanzaDiVisita'"); }
	
	}
	
	private class VisualizzatoreConfiguratoreStub implements VisualizzatoreConfiguratore {

		@Override
		public DTObject[] visualizzaVisite(StatiVisite stato) throws DBConnectionException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'visualizzaVisite'");
		}

		@Override
		public boolean nonCisonoLuoghiRegistrati() throws DBConnectionException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'nonCisonoLuoghiRegistrati'");
		}

		@Override
		public DTObject[] visualizzaElencoVolontari() throws DBConnectionException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'visualizzaElencoVolontari'");
		}

		@Override
		public DTObject[] visualizzaElencoLuoghi() throws DBConnectionException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'visualizzaElencoLuoghi'");
		}

		@Override
		public List<String> listaLuoghiRegistrati() throws DBConnectionException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'listaLuoghiRegistrati'");
		}

		@Override
		public DTObject[] visualizzaElencoTipiDiVisite() throws DBConnectionException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'visualizzaElencoTipiDiVisite'");
		}

		@Override
		public DTObject[] estraiDOWPossibiliVolontario(String volontarioAssociato) throws DBConnectionException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'estraiDOWPossibiliVolontario'");
		}

		@Override
		public DTObject[] estraiTipiDiVisiteVolontario() throws DBConnectionException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'estraiTipiDiVisiteVolontario'");
		}

		@Override
		public DTObject[] estraiGiorniTipoDiVisita() throws DBConnectionException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'estraiGiorniTipoDiVisita'");
		}
	
	}

	private class GestoreDateDisponibiliStub implements GestoreDateDisponibili {

		@Override
		public boolean fileExists(String path) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'fileExists'");
		}

		@Override
		public void creaFile(String path) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'creaFile'");
		}

		@Override
		public String getPath() {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'getPath'");
		}

		@Override
		public String leggiVariabile(String tag) throws FileNotFoundException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'leggiVariabile'");
		}

		@Override
		public void registraDateDisponibili(Date today, Date[] dateDisponili, String nomeVolontario)
				throws FileNotFoundException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'registraDateDisponibili'");
		}

		@Override
		public Date[] leggiDateDisponibili(String nomeVolontario) throws FileNotFoundException {
			return new Date[0];
		}

		@Override
		public void cleanDates(Date data, String nomeVolontario) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'cleanDates'");
		}

		@Override
		public void rimuoviDatiVolontario(String nickname) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'rimuoviDatiVolontario'");
		}
	
	}

	private class RegistroDatePrecluseStub implements GestoreDatePrecluse{

		@Override
		public boolean fileExists(String path) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'fileExists'");
		}

		@Override
		public void creaFile(String path) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'creaFile'");
		}

		@Override
		public String getPath() {return "test";}

		@Override
		public String leggiVariabile(String tag) throws FileNotFoundException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'leggiVariabile'");
		}

		@Override
		public void scriviDatePrecluse(Date today, Date[] current) throws FileNotFoundException {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'scriviDatePrecluse'");
		}

		@Override
		public Date[] leggiDatePrecluse() throws FileNotFoundException {
			Date[] date = {};
			return date;
		}

		@Override
		public void cleanDates(Date data) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'cleanDates'");
		}
	
	}
	
	private class FactoryServiziStub implements FactoryServizi {
		private boolean fileExists;

		public FactoryServiziStub(boolean fileExists) {
			this.fileExists = fileExists;
		}

		@Override
    public GestoreConfiguratore creaGestoreConfiguratore() {
			GestoreConfiguratoreStub gestore = new GestoreConfiguratoreStub();
			gestore.setFileExists(fileExists);
      return gestore;
    }
		@Override public Registratore creaRegistratore() { return new RegistratoreStub() {}; }
		@Override public VisualizzatoreConfiguratore creaVisualizzatoreConfiguratore() { return new VisualizzatoreConfiguratoreStub() {}; }
		@Override public GestoreDateDisponibili inizializzaDateDisponibili() {
			return new GestoreDateDisponibiliStub();
		}
		@Override public GestoreDatePrecluse inizializzaDatePrecluse() {
			return new RegistroDatePrecluseStub();
		}
		@Override public RegistratoreIscrizioni creaRegistratoreIscrizioni() { return null; }
		@Override public VisualizzatoreVolontario creaVisualizzatoreVolontario() { return null; }
		@Override public VisualizzatoreFruitore creaVisualizzatoreFruitore() { return null; }
		@Override public GestoreFruitore creaGestoreFruitore() { return null; }
	}

	@Test
	void creaConfiguratoreInizializzatoConFileEsistente() {
		Configuratore configuratore = new Configuratore(
			false, "Prova",
			 new FactoryServiziStub(true));
		assertNotNull(configuratore);
	}

	@Test
	void creaConfiguratoreInizializzatoConFileNonEsistente() {
		Configuratore configuratore = new Configuratore(
			false, "Prova",
			 new FactoryServiziStub(false));
		assertNotNull(configuratore);
	}

		@Test
	void creaConfiguratoreNonInizializzatoConFileEsistente() {
		Configuratore configuratore = new Configuratore(
			true, "Prova",
			 new FactoryServiziStub(true));
		assertNotNull(configuratore);
	}

	@Test
	void creaConfiguratoreNonInizializzatoConFileNonEsistente() {
		Configuratore configuratore = new Configuratore(
			true, "Prova",
			 new FactoryServiziStub(false));
		assertNotNull(configuratore);
	}

		@Test
	void creaFruitoreInizializzatoConFileEsistente() {
		Fruitore fruitore = new Fruitore(
			false, "Prova",
			 new FactoryServiziStub(true));
		assertNotNull(fruitore);
	}

	@Test
	void creaFruitoreInizializzatoConFileNonEsistente() {
		Fruitore fruitore = new Fruitore(
			false, "Prova",
			 new FactoryServiziStub(false));
		assertNotNull(fruitore);
	}

		@Test
	void creaFruitoreNonInizializzatoConFileEsistente() {
		Fruitore fruitore = new Fruitore(
			true, "Prova",
			 new FactoryServiziStub(true));
		assertNotNull(fruitore);
	}

	@Test
	void creaFruitoreNonInizializzatoConFileNonEsistente() {
		Fruitore fruitore = new Fruitore(
			true, "Prova",
			 new FactoryServiziStub(false));
		assertNotNull(fruitore);
	}

		@Test
	void creaVolontarioInizializzatoConFileEsistente() {
		Volontario volontario = new Volontario(
			false, "Prova",
			 new FactoryServiziStub(true));
		assertNotNull(volontario);
	}

	@Test
	void creaVolontarioInizializzatoConFileNonEsistente() {
		Volontario volontario = new Volontario(
			false, "Prova",
			 new FactoryServiziStub(false));
		assertNotNull(volontario);
	}

		@Test
	void creaVolontarioNonInizializzatoConFileEsistente() {
		Volontario volontario = new Volontario(
			true, "Prova",
			 new FactoryServiziStub(true));
		assertNotNull(volontario);
	}

	@Test
	void creaVolontarioNonInizializzatoConFileNonEsistente() {
		Volontario volontario = new Volontario(
			true, "Prova",
			 new FactoryServiziStub(false));
		assertNotNull(volontario);
	}


}
