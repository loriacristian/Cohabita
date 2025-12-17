package it.unical.ingsw.cohabita.technicalservice.persistence.dao;

import  it.unical.ingsw.cohabita.domain.Casa;

public interface CasaDao {

    Casa trovaCasaInvito(String codiceInvito);
    Casa trovaCasaId(Integer idCasa);
    Casa creaCasa(String nomeCasa, String codiceInvito);
}
