package it.unical.ingsw.cohabita.application.classifica;

public class VoceClassifica {

    private Integer idUtente;
    private String nomeCompleto;
    private double mediaVoti;
    private int numeroVoti;

    public VoceClassifica() {}

    public VoceClassifica(Integer idUtente, String nomeCompleto, double mediaVoti, int numeroVoti) {
        this.idUtente = idUtente;
        this.nomeCompleto = nomeCompleto;
        this.mediaVoti = mediaVoti;
        this.numeroVoti = numeroVoti;
    }

    public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public double getMediaVoti() {
        return mediaVoti;
    }

    public void setMediaVoti(double mediaVoti) {
        this.mediaVoti = mediaVoti;
    }

    public int getNumeroVoti() {
        return numeroVoti;
    }

    public void setNumeroVoti(int numeroVoti) {
        this.numeroVoti = numeroVoti;
    }
}
