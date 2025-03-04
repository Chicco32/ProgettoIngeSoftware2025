public enum StatiVisite {
    PROPONIBILE,
    PROPOSTA,
    COMPLETA,
    ANNULLATA,
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
            case ANNULLATA:
                return "annullata";
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
            case "Proposta":
                return PROPOSTA;
            case "Completa":
                return COMPLETA;
            case "Annullata":
                return ANNULLATA;
            case "Confermata":
                return CONFERMATA;
            case "Effettuata":
                return EFFETTUATA;
            default:
                return null;
        }
    }
}
