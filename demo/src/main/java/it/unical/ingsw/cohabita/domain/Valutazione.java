package it.unical.ingsw.cohabita.domain;

public class Valutazione {
    private Integer idValutazione;
    private Integer idTurno;
    private Integer idUtentePulitore;
    private Integer idUtenteValutatore;
    private Short voto;

    public Valutazione(){}

    public Valutazione(Integer idValutazione, Integer idTurno, Integer idUtentePulitore, Integer idUtenteValutatore, Short voto){
        this.idValutazione = idValutazione;
        this.idTurno = idTurno;
        this.idUtentePulitore = idUtentePulitore;
        this.idUtenteValutatore = idUtenteValutatore;
        this.voto = voto;
    }

    public Integer getIdValutazione() {
        return idValutazione;
    }

    public void setIdValutazione(Integer idValutazione) {
        this.idValutazione = idValutazione;
    }

    public Integer getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(Integer idTurno) {
        this.idTurno = idTurno;
    }

    public Integer getIdUtentePulitore() {
        return idUtentePulitore;
    }

    public void setIdUtentePulitore(Integer idUtentePulitore) {
        this.idUtentePulitore = idUtentePulitore;
    }

    public Integer getIdUtenteValutatore() {
        return idUtenteValutatore;
    }

    public void setIdUtenteValutatore(Integer idUtenteValutatore) {
        this.idUtenteValutatore = idUtenteValutatore;
    }

    public void setVoto(Short voto) {
        this.voto = voto;
    }


    public Short getVoto() {
        return voto;
    }



}
