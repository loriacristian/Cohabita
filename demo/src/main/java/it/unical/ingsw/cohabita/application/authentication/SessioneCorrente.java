package it.unical.ingsw.cohabita.application.authentication;

import it.unical.ingsw.cohabita.domain.Casa;
import it.unical.ingsw.cohabita.domain.Utente;

public class SessioneCorrente {

    private static Utente utenteCorrente;

    public static Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    public static void setUtenteCorrente(Utente utenteCorrente) {
        SessioneCorrente.utenteCorrente = utenteCorrente;
    }

    public static boolean Loggato(){
        return utenteCorrente != null;
    }

    public static void rimuoviUtenteCorrente(){
        utenteCorrente = null;
    }
}
