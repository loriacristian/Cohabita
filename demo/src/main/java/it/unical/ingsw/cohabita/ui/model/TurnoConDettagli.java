package it.unical.ingsw.cohabita.ui.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class TurnoConDettagli {

    private final IntegerProperty idTurno = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> data = new SimpleObjectProperty<>();
    private final IntegerProperty idUtente = new SimpleIntegerProperty();
    private final StringProperty nomeUtente = new SimpleStringProperty();
    private final StringProperty stato = new SimpleStringProperty();
    private final DoubleProperty valutazione = new SimpleDoubleProperty(-1);
    private final BooleanProperty valutato = new SimpleBooleanProperty(false);

    public TurnoConDettagli(Integer idTurno, LocalDate data, Integer idUtente, String nomeUtente,
                            String stato, Double valutazione, Boolean valutato) {
        this.idTurno.set(idTurno);
        this.data.set(data);
        this.idUtente.set(idUtente);
        this.nomeUtente.set(nomeUtente);
        this.stato.set(stato);
        this.valutazione.set(valutazione != null ? valutazione : -1);
        this.valutato.set(valutato != null && valutato);
    }

    public int getIdTurno() { return idTurno.get(); }
    public void setIdTurno(int value) { idTurno.set(value); }
    public IntegerProperty idTurnoProperty() { return idTurno; }

    public LocalDate getData() { return data.get(); }
    public void setData(LocalDate value) { data.set(value); }
    public ObjectProperty<LocalDate> dataProperty() { return data; }

    public int getIdUtente() { return idUtente.get(); }
    public void setIdUtente(int value) { idUtente.set(value); }
    public IntegerProperty idUtenteProperty() { return idUtente; }

    public String getNomeUtente() { return nomeUtente.get(); }
    public void setNomeUtente(String value) { nomeUtente.set(value); }
    public StringProperty nomeUtenteProperty() { return nomeUtente; }

    public String getStato() { return stato.get(); }
    public void setStato(String value) { stato.set(value); }
    public StringProperty statoProperty() { return stato; }

    public double getValutazione() { return valutazione.get(); }
    public void setValutazione(double value) { valutazione.set(value); }
    public DoubleProperty valutazioneProperty() { return valutazione; }

    public boolean isValutato() { return valutato.get(); }
    public void setValutato(boolean value) { valutato.set(value); }
    public BooleanProperty valutatoProperty() { return valutato; }
}