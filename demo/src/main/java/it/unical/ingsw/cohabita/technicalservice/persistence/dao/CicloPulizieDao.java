package it.unical.ingsw.cohabita.technicalservice.persistence.dao;

import it.unical.ingsw.cohabita.domain.CicloPulizie;

import java.util.List;

public interface CicloPulizieDao {

    void salvaCiclo(CicloPulizie ciclo);

    CicloPulizie trovaID(Integer idCiclo);

    List<CicloPulizie> trovaCasa(Integer idCasa);

    CicloPulizie trovaUltimoCicloCasa(Integer idCasa);

    void aggiornaCiclo(CicloPulizie ciclo);

    void cancellaCiclo(Integer idCiclo);
}
