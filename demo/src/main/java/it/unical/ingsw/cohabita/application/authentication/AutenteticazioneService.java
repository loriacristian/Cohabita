package it.unical.ingsw.cohabita.application.authentication;

import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.UtenteDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.impl.UtenteDaoImpl;
import org.mindrot.jbcrypt.BCrypt;

public class AutenteticazioneService {

    private final UtenteDao utenteDao= new UtenteDaoImpl();

    public boolean login(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        Utente utente = utenteDao.trovaUtenteUsername(username);
        if (utente == null) {
            return false;
        }

        String passwordSalvata = utente.getPassword();
        if (passwordSalvata == null || passwordSalvata.trim().isEmpty()) {
            return false;
        }

        boolean ok = BCrypt.checkpw(password, passwordSalvata);

        if (!ok) {
            return false;
        }

        SessioneCorrente.setUtenteCorrente(utente);

        return true;
    }

    public boolean registrati(String username, String password, String confermaPassword) {

        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        if (confermaPassword == null || confermaPassword.trim().isEmpty()) {
            return false;
        }

        if(!password.equals(confermaPassword)) {
            return false;
        }

        if(utenteDao.trovaUtenteUsername(username) != null) {
            return false;
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        Utente utente= new Utente();
        utente.setUsername(username);
        utente.setPassword(passwordHash);
        utenteDao.salvaUtente(utente);

        return true;
    }

    public void lasciaCasa(Utente utente){
        if(utente!=null){
            utenteDao.aggiornaCasaERuolo(utente.getId(),null,null);
            utente.setIdCasa(null);
            utente.setId(null);
            SessioneCorrente.setUtenteCorrente(utente);
        }
    }

    public boolean CambiaPassword(Utente utente, String vecchiaPassword,String nuovaPassword,String confermaPassword){
        if(utente==null){
            return false;
        }
        if(vecchiaPassword == null || vecchiaPassword.trim().isEmpty()) {
            return false;
        }
        if(nuovaPassword == null || nuovaPassword.trim().isEmpty()) {
            return false;
        }
        if(confermaPassword == null || confermaPassword.trim().isEmpty()) {
            return false;
        }
        if(!BCrypt.checkpw(vecchiaPassword, utente.getPassword())) {
            return false;
        }

        String nuovaPasswordHash= BCrypt.hashpw(nuovaPassword,BCrypt.gensalt());


        utenteDao.aggiornaPassword(nuovaPasswordHash, utente.getId());


        utente.setPassword(nuovaPassword);
        SessioneCorrente.setUtenteCorrente(utente);

        return true;

    }

    public boolean passwordValida(String password){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(regex);
    }
}
