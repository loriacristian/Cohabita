package it.unical.ingsw.cohabita.domain;

public class Utente {

    private Integer id;
    private String username;
    private String password;
    private Integer ruolo;
    private Integer idCasa;


    public Utente(){}

    public Utente(Integer id, String username, String password, Integer ruolo, Integer idCasa) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
        this.idCasa = idCasa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRuolo() {
        return ruolo;
    }

    public void setRuolo(Integer ruolo) {
        this.ruolo = ruolo;
    }

    public Integer getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(Integer idCasa) {
        this.idCasa = idCasa;
    }
}
