package it.unical.ingsw.cohabita.domain;

import java.time.LocalDate;

public class CicloPulizie {
    private Integer idCiclo;
    private LocalDate dataCiclo;
    private Integer idCasa;
    private Short cadenza;
    private Short turniCadauno;

    public CicloPulizie(){}

    public CicloPulizie(Integer idCiclo, LocalDate dataCiclo, Integer idCasa, Short cadenza, Short turniCadauno){
        this.idCiclo = idCiclo;
        this.dataCiclo = dataCiclo;
        this.idCasa = idCasa;
        this.cadenza = cadenza;
        this.turniCadauno = turniCadauno;
    }

    public Integer getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(Integer idCiclo) {
        this.idCiclo = idCiclo;
    }

    public LocalDate getDataCiclo() {
        return dataCiclo;
    }

    public void setDataCiclo(LocalDate dataCiclo) {
        this.dataCiclo = dataCiclo;
    }

    public Integer getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(Integer idCasa) {
        this.idCasa = idCasa;
    }

    public Short getCadenza() {
        return cadenza;
    }

    public void setCadenza(Short cadenza) {
        this.cadenza = cadenza;
    }

    public Short getTurniCadauno() {
        return turniCadauno;
    }

    public void setTurniCadauno(Short turniCadauno) {
        this.turniCadauno = turniCadauno;
    }


}
