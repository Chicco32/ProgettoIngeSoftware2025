package Services;

import java.util.Date;

/**
 * Struttura dati per passare due date consecutive. Attenzione che alla creazione se la data successiva non è strettamente
 * successiva a quella di inizio le date saranno settate a null
 * 
 * @see java.sql.Date
 */
public class DateRange {
    private Date startDate;
    private Date endDate;

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        if (endDate.after(startDate))
        this.endDate = endDate;
        else{
            this.startDate = null;
            this.endDate = null;
        }
    }

    // Getter e Setter
    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    /**
     * Controlla se la data è nel range dell'oggetto range. La data deve essere passata come un oggetto della classe Date
     * e ritorna true solo se contenuta strettamente all'interno del range
     * @param date la data da verificare
     * @return true se la data inserita è all'interno, false altrimenti
     * 
     */
    public boolean insideRange(Date date) {
        if (date == null) return false; // Se la data è null, non può essere nel range
        if (startDate != null && date.before(startDate)) return false; // Se è prima della data di inizio
        if (endDate != null && date.after(endDate)) return false; // Se è dopo la data di fine
        return true; // È nel range
    }

    @Override
    public String toString() {
        return "DateRange{startDate=" + startDate + ", endDate=" + endDate + "}";
    }

}
