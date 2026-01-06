package it.unical.ingsw.cohabita.technicalservice.persistence.impl;

import it.unical.ingsw.cohabita.domain.TurnoConDettagli;
import it.unical.ingsw.cohabita.domain.TurnoPulizie;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.PulizieDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.db.DBConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PulizieDaoImpl implements PulizieDao {

    private final Connection conn;

    public PulizieDaoImpl() {
        this.conn = DBConnectionManager.getInstance().getConnection();
    }

    private static final String INSERSCI=
            "INSERT INTO turni_pulizie (id_ciclo, id_utente, data_turno) " +
                    "VALUES (?, ?, ?)";
    private static final String TROVA_ID=
            "SELECT id_turno, id_ciclo, id_utente, data_turno " +
                    "FROM turni_pulizie WHERE id_turno = ?";
    private static final String TROVA_CICLO =
            "SELECT id_turno, id_ciclo, id_utente, data_turno " +
                    "FROM turni_pulizie WHERE id_ciclo = ? " +
                    "ORDER BY data_turno ASC";

    private static final String TROVA_DA_UTENTE =
            "SELECT id_turno, id_ciclo, id_utente, data_turno " +
                    "FROM turni_pulizie WHERE id_utente = ? " +
                    "ORDER BY data_turno ASC";

    private static final String TROVA_TURNO_DA_CASA_DATA =
            "SELECT t.id_turno, t.id_ciclo, t.id_utente, t.data_turno " +
                    "FROM turni_pulizie t " +
                    "JOIN cicli_pulizie c ON t.id_ciclo = c.id_ciclo " +
                    "WHERE c.id_casa = ? AND t.data_turno = ? " +
                    "LIMIT 1";

    private static final String TROVA_PROSSIMO_TURNO_UTENTE =
            "SELECT id_turno, id_ciclo, id_utente, data_turno " +
                    "FROM turni_pulizie " +
                    "WHERE id_utente = ? AND data_turno >= ? " +
                    "ORDER BY data_turno ASC " +
                    "LIMIT 1";

    private static final String AGGIORNA =
            "UPDATE turni_pulizie SET id_utente = ? WHERE id_turno = ?";

    private static final String CANCELLA =
            "DELETE FROM turni_pulizie WHERE id_turno = ?";

    private static final String CANCELLA_TURNI_FUTURI =
            "DELETE FROM turno_pulizie " +
                    "WHERE id_ciclo = ? AND data_turno >= CURRENT_DATE";


    private static final String TROVA_TURNI_DETTAGLIATI =
                    "SELECT t.id_turno, t.data_turno, t.id_utente, u.username, " +
                    "AVG(v.valutazione) as media_voto, " +
                    "COUNT(v.id_valutazione) > 0 as is_valutato " +
                    "FROM turni_pulizie t " +
                    "JOIN utenti u ON t.id_utente = u.id " +
                    "LEFT JOIN valutazioni v ON t.id_turno = v.id_turno " +
                    "WHERE t.id_ciclo = ? " +
                    "GROUP BY t.id_turno, u.username " +
                    "ORDER BY t.data_turno ASC";




    @Override
    public void salvaTurno(TurnoPulizie turno) {
        try (PreparedStatement ps = conn.prepareStatement(INSERSCI, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,turno.getIdCiclo());
            ps.setInt(2,turno.getIdUtente());
            ps.setDate(3, Date.valueOf(turno.getDataTurno()));

            int righe = ps.executeUpdate();

            if(righe>0){
                try(ResultSet rs = ps.getGeneratedKeys()) {
                    if(rs.next()){
                        turno.setIdTurno(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TurnoPulizie trovaTurnoID(Integer idTurno) {
        try (PreparedStatement stmt = conn.prepareStatement(TROVA_ID)) {

            stmt.setInt(1, idTurno);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapTurno(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero turno per ID", e);
        }

        return null;
    }
    @Override
    public List<TurnoPulizie> trovaTurnoDaCiclo(Integer idCiclo){
        List<TurnoPulizie> turni = new ArrayList<>();

        try(PreparedStatement ps= conn.prepareStatement(TROVA_CICLO)){
            ps.setInt(1, idCiclo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    turni.add(mapTurno(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero turni per ciclo", e);
        }

        return turni;
    }
    @Override
    public List<TurnoPulizie> trovaDaUtente(Integer idUtente){
        List<TurnoPulizie> turni = new ArrayList<>();

        try(PreparedStatement ps= conn.prepareStatement(TROVA_DA_UTENTE)){
            ps.setInt(1, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    turni.add(mapTurno(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero turni per utente", e);
        }

        return turni;
    }


    @Override
    public TurnoPulizie trovaTurnoDaCasaData(Integer idCasa, LocalDate data){

        try(PreparedStatement ps= conn.prepareStatement(TROVA_TURNO_DA_CASA_DATA)){
            ps.setInt(1, idCasa);
            ps.setDate(2, Date.valueOf(data));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTurno(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero turno per casa e data", e);
        }
        return null;
    }

    @Override
    public TurnoPulizie trovaProssimoTurnoUtente(Integer idUtente, LocalDate dataMinima) {
        try (PreparedStatement ps = conn.prepareStatement(TROVA_PROSSIMO_TURNO_UTENTE)) {
            ps.setInt(1, idUtente);
            ps.setDate(2, Date.valueOf(dataMinima));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTurno(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero prossimo turno utente", e);
        }
        return null;
    }

    @Override
    public void aggiornaTurno(TurnoPulizie turno) {
        try (PreparedStatement ps = conn.prepareStatement(AGGIORNA)) {
            ps.setInt(1,turno.getIdUtente());
            ps.setInt(2,turno.getIdTurno());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore nell'aggiornamento turno", e);
        }
    }

    @Override
    public void cancellaTurno(Integer idTurno){
        try(PreparedStatement ps= conn.prepareStatement(CANCELLA)){
            ps.setInt(1,idTurno);
            ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException("Errore nella cencellazione del turno ",e);
        }
    }

    @Override
    public void cancellaTurniFuturi(Integer idCiclo){
        try (PreparedStatement ps = conn.prepareStatement(CANCELLA_TURNI_FUTURI)) {
            ps.setInt(1, idCiclo);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore nella cancellazione turni futuri", e);
        }
    }


    private TurnoPulizie mapTurno(ResultSet rs) throws SQLException {
        TurnoPulizie turno = new TurnoPulizie();
        turno.setIdTurno(rs.getInt("id_turno"));
        turno.setIdCiclo(rs.getInt("id_ciclo"));
        turno.setIdUtente(rs.getInt("id_utente"));
        turno.setDataTurno(rs.getDate("data_turno").toLocalDate());
        return turno;
    }

    @Override
    public List<TurnoConDettagli> trovaTurniConDettagli(Integer idCiclo) {
        List<TurnoConDettagli> lista = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(TROVA_TURNI_DETTAGLIATI)) {
            ps.setInt(1, idCiclo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double media = rs.getDouble("media_voto");
                    if (rs.wasNull()) media = -1.0;

                    lista.add(new TurnoConDettagli(
                            rs.getInt("id_turno"),
                            rs.getDate("data_turno").toLocalDate(),
                            rs.getInt("id_utente"),
                            rs.getString("username"),
                            "",
                            media,
                            rs.getBoolean("is_valutato")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore nel caricamento ottimizzato turni", e);
        }
        return lista;
    }
}