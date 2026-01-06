package it.unical.ingsw.cohabita.technicalservice.persistence.dao;

import it.unical.ingsw.cohabita.domain.Utente;

import java.util.List;

public interface UtenteDao {

    Utente trovaUtenteUsername(String username);

    Boolean esisteUsername(String username);

    void salvaUtente(Utente utente);

    Utente trovaUtenteId(Integer id);

    void aggiornaCasaERuolo(Integer idUtente, Integer idCasa, Integer ruolo);

    void aggiornaPassword(String hashPassword, Integer idUtente);

    List<Utente> trovaCoinquilini(Integer idCasa);

}
