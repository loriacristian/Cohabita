package it.unical.ingsw.cohabita.domain;

import java.time.LocalDate;


public class TurnoConDettagli {
    private Integer idTurno;
    private LocalDate data;
    private Integer idUtente;
    private String nomeUtente;
    private String stato;
    private Double valutazione;
    private boolean valutato;

    public TurnoConDettagli(Integer idTurno, LocalDate data, Integer idUtente,
                            String nomeUtente, String stato, Double valutazione, boolean valutato) {
        this.idTurno = idTurno;
        this.data = data;
        this.idUtente = idUtente;
        this.nomeUtente = nomeUtente;
        this.stato = stato;
        this.valutazione = valutazione;
        this.valutato = valutato;
    }

    public Integer getIdTurno() { return idTurno; }
    public LocalDate getData() { return data; }
    public Integer getIdUtente() { return idUtente; }
    public String getNomeUtente() { return nomeUtente; }
    public String getStato() { return stato; }
    public Double getValutazione() { return valutazione; }
    public boolean isValutato() { return valutato; }

    public void setStato(String stato) { this.stato = stato; }
    public void setNomeUtente(String nomeUtente) { this.nomeUtente = nomeUtente; }
    public void setValutazione(Double valutazione) { this.valutazione = valutazione; }
}