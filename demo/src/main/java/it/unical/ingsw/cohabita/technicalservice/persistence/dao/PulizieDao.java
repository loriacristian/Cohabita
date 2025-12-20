package it.unical.ingsw.cohabita.technicalservice.persistence.dao;

import it.unical.ingsw.cohabita.domain.TurnoPulizie;

import java.time.LocalDate;
import java.util.List;

public interface PulizieDao {

    void salvaTurno(TurnoPulizie turno);

    TurnoPulizie trovaTurnoID(Integer id);

    List<TurnoPulizie> trovaTurnoDaCiclo(Integer idCiclo);

    List<TurnoPulizie> trovaDaUtente(Integer idUtente);

    TurnoPulizie trovaTurnoDaCasaData(Integer idCasa, LocalDate data);

    TurnoPulizie trovaProssimoTurnoUtente(Integer idUtente, LocalDate dataMinima);

    void aggiornaTurno(TurnoPulizie turno);

    void cancellaTurno(Integer idTurno);

}
