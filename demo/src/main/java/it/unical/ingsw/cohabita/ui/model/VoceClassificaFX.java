package it.unical.ingsw.cohabita.ui.model;

import javafx.beans.property.*;

public class VoceClassificaFX {

    private final IntegerProperty idUtente = new SimpleIntegerProperty();
    private final StringProperty nomeCompleto = new SimpleStringProperty();
    private final DoubleProperty mediaVoti = new SimpleDoubleProperty();
    private final IntegerProperty numeroVoti = new SimpleIntegerProperty();

    public VoceClassificaFX() {}

    public VoceClassificaFX(Integer idUtente, String nomeCompleto, double mediaVoti, int numeroVoti) {
        this.idUtente.set(idUtente);
        this.nomeCompleto.set(nomeCompleto);
        this.mediaVoti.set(mediaVoti);
        this.numeroVoti.set(numeroVoti);
    }

    public int getIdUtente() {
        return idUtente.get();
    }

    public void setIdUtente(int value) {
        idUtente.set(value);
    }

    public IntegerProperty idUtenteProperty() {
        return idUtente;
    }

    // Nome completo
    public String getNomeCompleto() {
        return nomeCompleto.get();
    }

    public void setNomeCompleto(String value) {
        nomeCompleto.set(value);
    }

    public StringProperty nomeCompletoProperty() {
        return nomeCompleto;
    }

    public double getMediaVoti() {
        return mediaVoti.get();
    }

    public void setMediaVoti(double value) {
        mediaVoti.set(value);
    }

    public DoubleProperty mediaVotiProperty() {
        return mediaVoti;
    }

    public int getNumeroVoti() {
        return numeroVoti.get();
    }

    public void setNumeroVoti(int value) {
        numeroVoti.set(value);
    }

    public IntegerProperty numeroVotiProperty() {
        return numeroVoti;
    }
}
