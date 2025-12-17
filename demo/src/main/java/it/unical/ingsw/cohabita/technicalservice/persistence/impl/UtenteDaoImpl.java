package it.unical.ingsw.cohabita.technicalservice.persistence.impl;

import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.UtenteDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.db.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDaoImpl implements UtenteDao {
    private final Connection conn;

    public UtenteDaoImpl() {
        this.conn = DBConnectionManager.getInstance().getConnection();
    }

    private static final String trovaUtenteConUsername=
            "SELECT u.id,u.username,c.password_hash, u.ruolo, u.id_casa" +
                    " FROM utenti u JOIN credenziali c ON u.id=c.id WHERE u.username=?";

    private static final String esisteUtenteConUsername=
            "SELECT 1 FROM utenti WHERE username = ? LIMIT 1";

    private static final String inserisciUtente=
            "INSERT INTO utenti (username) VALUES (?) RETURNING id";

    private static final String inserisciCredenziali=
            "INSERT INTO credenziali (id, password_hash) VALUES (?, ?)";

    private static final String aggiornaCasaRuolo=
            "INSERT INTO utenti SET id_casa=?, ruolo=?, WHERE id=?";

    private static final String aggiornaPassword=
            "INSERT INTO credenziali SET password_hash=?, WHERE id=?";

    private static final String trovaCoinquilini="SELECT id, username, id_casa, ruolo " +
            "FROM utenti WHERE id_casa = ? ORDER BY username ASC";


    private static final String trovaUtenteConId="SELECT u.id, u.username, c.password_hash, u.ruolo, u.id_casa " +
            "FROM utenti u JOIN credenziali c ON u.id = c.id WHERE u.id = ?";



    @Override
    public Utente trovaUtenteUsername(String username) {
        try (PreparedStatement ps = conn.prepareStatement(trovaUtenteConUsername)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return creaIstanzaUtente(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    @Override
    public Boolean esisteUsername(String username) {
        try (PreparedStatement ps = conn.prepareStatement(esisteUtenteConUsername)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void salvaUtente(Utente utente) {
            try {
                conn.setAutoCommit(false);
                int idUtente;
                try (PreparedStatement psUser = conn.prepareStatement(inserisciUtente, Statement.RETURN_GENERATED_KEYS)) {
                    psUser.setString(1, utente.getUsername());
                    psUser.executeUpdate();

                    try (ResultSet rs = psUser.getGeneratedKeys()) {
                        if (rs.next()) {
                            idUtente = rs.getInt(1);
                        } else {
                            throw new RuntimeException("Impossibile ottenere l'ID utente generato");
                        }
                    }
                }
                try (PreparedStatement psCred =
                             conn.prepareStatement(inserisciCredenziali)) {

                    psCred.setInt(1, idUtente);
                    psCred.setString(2, utente.getPassword());
                    psCred.executeUpdate();
                }
                conn.commit();
                utente.setId(idUtente);

            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                throw new RuntimeException("Errore nel salvataggio utente", e);
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

    }

    @Override
    public Utente trovaUtenteId(Integer id) {

        try (PreparedStatement ps = conn.prepareStatement(trovaUtenteConId)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return creaIstanzaUtente(rs);
            }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void aggiornaCasaERuolo(Integer idUtente, Integer idCasa, Integer ruolo) {
        try (PreparedStatement ps = conn.prepareStatement(aggiornaCasaRuolo)) {
            ps.setInt(1, idCasa);
            ps.setInt(2, ruolo);
            ps.setInt(3, idUtente);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void aggiornaPassword(String hashPassword, Integer idUtente) {
        try {
            PreparedStatement ps = conn.prepareStatement(aggiornaPassword);
            ps.setString(1, hashPassword);
            ps.setInt(2, idUtente);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
        }


    @Override
    public List<Utente> trovaCoinquilini(Integer idCasa) {
        List<Utente> coinquilini = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(trovaCoinquilini)) {
            ps.setInt(1, idCasa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Utente coinquilino = creaIstanzaUtente(rs);
                coinquilini.add(coinquilino);
            }
            return coinquilini;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }






    private Utente creaIstanzaUtente(ResultSet rs) throws SQLException {
        Integer id=rs.getInt("id");
        String username=rs.getString("username");
        String password=rs.getString("password_hash");
        Integer ruolo=rs.getInt("ruolo");
        Integer idCasa=rs.getInt("id_casa");
        return new Utente(id, username, password, ruolo, idCasa);
    }


}