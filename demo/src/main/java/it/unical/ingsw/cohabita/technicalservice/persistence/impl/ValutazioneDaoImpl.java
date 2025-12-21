package it.unical.ingsw.cohabita.technicalservice.persistence.impl;

import it.unical.ingsw.cohabita.application.classifica.VoceClassifica;
import it.unical.ingsw.cohabita.domain.Valutazione;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.ValutazioneDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.db.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ValutazioneDaoImpl implements ValutazioneDao {

    private final Connection conn;

    public ValutazioneDaoImpl() {
        this.conn = DBConnectionManager.getInstance().getConnection();
    }

    private static final String INSERISCI =
            "INSERT INTO valutazioni (id_turno, id_utente_pulitore, id_utente_valutatore, valutazione) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String TROVA_TURNO =
            "SELECT id_valutazione, id_turno, id_utente_pulitore, id_utente_valutatore, valutazione " +
                    "FROM valutazioni WHERE id_turno = ?";

    private static final String TROVA_ID =
            "SELECT id_valutazione, id_turno, id_utente_pulitore, id_utente_valutatore, valutazione " +
                    "FROM valutazioni WHERE id_valutazione = ?";


    private static final String CLASSIFICA =
            "SELECT u.id AS id_utente, " +
                    "       u.username AS nome_completo, " +
                    "       AVG(v.valutazione)::numeric(10,2) AS media_voti, " +
                    "       COUNT(*) AS numero_voti " +
                    "FROM valutazioni v " +
                    "JOIN utenti u ON v.id_utente_pulitore = u.id " +
                    "WHERE u.id_casa = ? " +
                    "GROUP BY u.id, u.username " +
                    "ORDER BY media_voti DESC, numero_voti DESC";

    @Override
    public void salvaValutazione(Valutazione valutazione){
        try(PreparedStatement ps = conn.prepareStatement(INSERISCI, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, valutazione.getIdTurno());
            ps.setInt(2, valutazione.getIdUtentePulitore());
            ps.setInt(3, valutazione.getIdUtenteValutatore());
            ps.setShort(4, valutazione.getVoto());

            int righe = ps.executeUpdate();

            if (righe > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        valutazione.setIdValutazione(rs.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel salvataggio della valutazione", e);
        }
    }

    @Override
    public List<Valutazione> trovaTurno(Integer idTurno){

        List<Valutazione> valutazioni = new ArrayList<Valutazione>();

        try (PreparedStatement ps = conn.prepareStatement(TROVA_TURNO)) {

            ps.setInt(1, idTurno);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    valutazioni.add(mapValutazione(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero valutazioni per turno", e);
        }

        return valutazioni;
    }

    @Override
    public List<VoceClassifica> trovaClassifica(Integer idCasa){
        List<VoceClassifica> classifiche = new ArrayList<VoceClassifica>();

        try (PreparedStatement ps = conn.prepareStatement(CLASSIFICA)) {

            ps.setInt(1, idCasa);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer idUtente = rs.getInt("id_utente");
                    String nomeCompleto = rs.getString("nome_completo");
                    double mediaVoti = rs.getDouble("media_voti");
                    int numeroVoti = rs.getInt("numero_voti");

                    classifiche.add(new VoceClassifica(idUtente, nomeCompleto, mediaVoti, numeroVoti));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel calcolo della classifica", e);
        }

        return classifiche;
    }

    @Override
    public Valutazione trovaId(Integer idValutazione){

        try (PreparedStatement ps = conn.prepareStatement(TROVA_ID)) {

            ps.setInt(1, idValutazione);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapValutazione(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero valutazione per ID", e);
        }

        return null;
    }

    private Valutazione mapValutazione(ResultSet rs) throws SQLException {
        Valutazione v = new Valutazione();
        v.setIdValutazione(rs.getInt("id_valutazione"));
        v.setIdTurno(rs.getInt("id_turno"));
        v.setIdUtentePulitore(rs.getInt("id_utente_pulitore"));
        v.setIdUtenteValutatore(rs.getInt("id_utente_valutatore"));
        v.setVoto(rs.getShort("valutazione"));
        return v;
    }
}
