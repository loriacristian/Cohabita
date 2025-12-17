package it.unical.ingsw.cohabita.domain;

public class Casa {
    private Integer idCasa;
    private String nomeCasa;
    private String codiceInvito;

    public Casa(){}

    public Casa(Integer idCasa, String nomeCasa, String codiceInvito) {
        this.idCasa = idCasa;
        this.nomeCasa = nomeCasa;
        this.codiceInvito = codiceInvito;
    }

    public Integer getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(Integer idCasa) {
        this.idCasa = idCasa;
    }

    public String getNomeCasa() {
        return nomeCasa;
    }

    public void setNomeCasa(String nomeCasa) {
        this.nomeCasa = nomeCasa;
    }

    public String getCodiceInvito() {
        return codiceInvito;
    }

    public void setCodiceInvito(String codiceInvito) {
        this.codiceInvito = codiceInvito;
    }
}
