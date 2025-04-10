package ServicesAPI;

public enum StatiVisite {
    PROPONIBILE,
    PROPOSTA,
    COMPLETA,
    CANCELLATA,
    CONFERMATA,
    EFFETTUATA;

    public static String toString(StatiVisite stato) {
        switch (stato) {
            case PROPONIBILE:
                return "proponibile";
            case PROPOSTA:
                return "proposta";
            case COMPLETA:
                return "completa";
            case CANCELLATA:
                return "cancellata";
            case CONFERMATA:
                return "confermata";
            case EFFETTUATA:
                return "effettuata";
            default:
                return "Errore";
        }
    }

    public static StatiVisite fromString(String stato) {
        switch (stato) {
            case "proposta":
                return PROPOSTA;
            case "completa":
                return COMPLETA;
            case "cancellata":
                return CANCELLATA;
            case "confermata":
                return CONFERMATA;
            case "effettuata":
                return EFFETTUATA;
            default:
                return null;
        }
    }
}
