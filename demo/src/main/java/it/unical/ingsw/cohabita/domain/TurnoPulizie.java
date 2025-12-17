package it.unical.ingsw.cohabita.domain;

import java.time.LocalDate;

public class TurnoPulizie {
    private Integer idTurno;
    private Integer idCiclo;
    private Integer idUtente;
    private LocalDate dataTurno;

    public TurnoPulizie(){}

    public TurnoPulizie(Integer idTurno, Integer idCiclo, Integer idUtente, LocalDate dataTurno) {
        this.idTurno = idTurno;
        this.idCiclo = idCiclo;
        this.idUtente = idUtente;
        this.dataTurno = dataTurno;
    }

    public Integer getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Integer idTurno) {
        this.idTurno = idTurno;
    }

    public Integer getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(Integer idCiclo) {
        this.idCiclo = idCiclo;
    }

    public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

    public LocalDate getDataTurno() {
        return dataTurno;
    }

    public void setDataTurno(LocalDate dataTurno) {
        this.dataTurno = dataTurno;
    }
}
