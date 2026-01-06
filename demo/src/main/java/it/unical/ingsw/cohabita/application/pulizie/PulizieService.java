package it.unical.ingsw.cohabita.application.pulizie;

import it.unical.ingsw.cohabita.application.classifica.VoceClassifica;
import it.unical.ingsw.cohabita.domain.*;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.*;
import it.unical.ingsw.cohabita.technicalservice.persistence.impl.*;

import java.time.LocalDate;
import java.util.List;

public class PulizieService {

    private final CasaDao casaDao;
    private final UtenteDao utenteDao;
    private final CicloPulizieDao cicloDao;
    private final PulizieDao pulizieDao;
    private final ValutazioneDao valutazioneDao;

    public PulizieService(CasaDao casaDao,
                          UtenteDao utenteDao,
                          CicloPulizieDao cicloDao,
                          PulizieDao pulizieDao,
                          ValutazioneDao valutazioneDao) {
        this.casaDao = casaDao;
        this.utenteDao = utenteDao;
        this.cicloDao = cicloDao;
        this.pulizieDao = pulizieDao;
        this.valutazioneDao = valutazioneDao;
    }

    public static PulizieService defaultInstance() {
        return new PulizieService(
                new CasaDaoImpl(),
                new UtenteDaoImpl(),
                new CicloPulizieDaoImpl(),
                new PulizieDaoImpl(),
                new ValutazioneDaoImpl()
        );
    }

    public CicloPulizie creaCiclo(CicloPulizie ciclo) {
        cicloDao.salvaCiclo(ciclo);

        LocalDate date = ciclo.getDataCiclo();
        List<Utente> coinquilini= utenteDao.trovaCoinquilini(ciclo.getIdCasa());
        for(int i=0;i< ciclo.getTurniCadauno();i++){
            for(Utente utente : coinquilini){
                TurnoPulizie turnoPulizie = new TurnoPulizie();
                turnoPulizie.setIdCiclo(ciclo.getIdCiclo());
                turnoPulizie.setIdUtente(utente.getId());
                turnoPulizie.setDataTurno(date);
                pulizieDao.salvaTurno(turnoPulizie);
                date = date.plusDays(ciclo.getCadenza());
            }
        }
        return ciclo;
    }

    public Casa getCasa(Integer idCasa){
        return casaDao.trovaCasaId(idCasa);
    }

    public CicloPulizie getCiclo(Integer idCasa){
        return cicloDao.trovaUltimoCicloCasa(idCasa);
    }

    public List<TurnoPulizie> getTurniCiclo(Integer idCiclo) {
        return pulizieDao.trovaTurnoDaCiclo(idCiclo);
    }

    public TurnoPulizie getTurno(Integer idTurno) {
        return pulizieDao.trovaTurnoID(idTurno);
    }

    public void aggiornaTurno(TurnoPulizie turno) {
        pulizieDao.aggiornaTurno(turno);
    }

    public TurnoPulizie getTurnoOggi(Integer idCasa, LocalDate data) {
        return pulizieDao.trovaTurnoDaCasaData(idCasa, data);
    }

    public TurnoPulizie getProssimoTurnoUtente(Integer idUtente, LocalDate da) {
        return pulizieDao.trovaProssimoTurnoUtente(idUtente, da);
    }

    public Utente getUtente(Integer idUtente) {
        return utenteDao.trovaUtenteId(idUtente);
    }

    public List<Utente> getUtentiCasa(Integer idCasa) {
        return utenteDao.trovaCoinquilini(idCasa);
    }

    public List<Valutazione> getValutazioniTurno(Integer idTurno) {
        return valutazioneDao.trovaTurno(idTurno);
    }

    public void salvaValutazione(Valutazione v) {
        valutazioneDao.salvaValutazione(v);
    }

    public List<VoceClassifica> getClassificaCasa(Integer idCasa) {
        return valutazioneDao.trovaClassifica(idCasa);
    }

    public void cancellaCiclo(Integer idCasa,Integer idCiclo) {
        cicloDao.cancellaCiclo(idCasa);

        pulizieDao.cancellaTurno(idCiclo);
    }

    public List<TurnoConDettagli> getTurniDettagliati(Integer idCiclo) {
        return pulizieDao.trovaTurniConDettagli(idCiclo);
    }

}