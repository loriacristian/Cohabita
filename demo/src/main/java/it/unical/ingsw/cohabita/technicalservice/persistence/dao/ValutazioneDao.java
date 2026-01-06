package it.unical.ingsw.cohabita.technicalservice.persistence.dao;

import it.unical.ingsw.cohabita.application.classifica.VoceClassifica;
import it.unical.ingsw.cohabita.domain.Valutazione;

import java.util.List;

public interface ValutazioneDao {

    void salvaValutazione(Valutazione valutazione);

    List<Valutazione> trovaTurno(Integer idTurno);

    List<VoceClassifica> trovaClassifica(Integer idCasa);

    Valutazione trovaId(Integer idValutazione);

}

