package it.unical.ingsw.cohabita.application.casa;

import it.unical.ingsw.cohabita.application.authentication.SessioneCorrente;
import it.unical.ingsw.cohabita.domain.Casa;
import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.CasaDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.UtenteDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.impl.CasaDaoImpl;
import it.unical.ingsw.cohabita.technicalservice.persistence.impl.UtenteDaoImpl;

import java.security.SecureRandom;

public class CasaService {

    private CasaDao casaDao=new CasaDaoImpl();
    private UtenteDao utenteDao=new UtenteDaoImpl();

    public Casa creaCasa(String nomeCasa){
        Utente utente = SessioneCorrente.getUtenteCorrente();

        if (utente == null) {
            throw new IllegalStateException("Nessun utente loggato");
        }

        String codice= generaCodiceInvito(8);

        Casa casa = casaDao.creaCasa(nomeCasa, codice);

        if(casa==null){
            throw new IllegalStateException("Impossibile creare una casa");
        }

        utente.setIdCasa(casa.getIdCasa());
        utente.setRuolo(0);

        utenteDao.aggiornaCasaERuolo(utente.getId(), casa.getIdCasa(), 0);

        SessioneCorrente.setUtenteCorrente(utente);

        return casa;
    }

    public boolean accediCasa(String codice){
        Utente utente = SessioneCorrente.getUtenteCorrente();

        if (utente == null) {
            throw new IllegalStateException("Nessun utente loggato");
        }

        Casa casa= casaDao.trovaCasaInvito(codice);

        if(casa==null){
            return false;
        }

        utente.setIdCasa(casa.getIdCasa());
        utente.setRuolo(1);

        utenteDao.aggiornaCasaERuolo(utente.getId(), casa.getIdCasa(), 1);

        SessioneCorrente.setUtenteCorrente(utente);

        return true;
    }

    public String generaCodiceInvito(int lunghezza){
        final String chars="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(lunghezza);
        for (int i = 0; i < lunghezza; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

}
