package ServicesAPI;

import java.util.Date;

public class RegistroDateDisponibili extends RegistroDate {

    public RegistroDateDisponibili(GestoreFilesConfigurazione fileManager, String nomeVolontario) {
        super(fileManager);
    }

    public void registraDateDisponibili(Date today, Date[] dateDisponibli, String nomeVolontario) {
        //TODO da fare @Diego
    }

	public Date[] calcolaPossibiliDate() {
		// TODO da fare@Diego
        return null;
	}
    
}
