package it.unical.ingsw.cohabita.application.utente;
import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.UtenteDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.impl.UtenteDaoImpl;

import java.util.List;

public class UtenteService {

    private final UtenteDao utenteDao;

    public UtenteService(UtenteDao utenteDao) {
        this.utenteDao = utenteDao;
    }

    public static UtenteService defaultInstance() {
        return new UtenteService(new UtenteDaoImpl());
    }

    public Utente getDaId(Integer id) {
        return utenteDao.trovaUtenteId(id);
    }

    public Utente getDaUsername(String username) {
        return utenteDao.trovaUtenteUsername(username);
    }

    public List<Utente> getDaCasa(Integer idCasa) {
        return utenteDao.trovaCoinquilini(idCasa);
    }

    public boolean isAdmin(Utente utente) {
        return utente != null && utente.getRuolo() != null && utente.getRuolo() == 0;
    }
}