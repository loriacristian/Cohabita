package it.unical.ingsw.cohabita.technicalservice.persistence.impl;

import it.unical.ingsw.cohabita.domain.CicloPulizie;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.CicloPulizieDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.db.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CicloPulizieDaoImpl implements CicloPulizieDao {
    private final Connection conn;

    public CicloPulizieDaoImpl() {
        this.conn = DBConnectionManager.getInstance().getConnection();
    }

    private static final String INSERISCI =
            "INSERT INTO cicli_pulizie (id_casa, data_inizio, cadenza, turni_cadauno) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String TROVA_ID=
            "SELECT id_ciclo, id_casa, data_inizio, cadenza, turni_cadauno " +
                    "FROM cicli_pulizie WHERE id_ciclo = ?";

    private static final String TROVA_CASA =
            "SELECT id_ciclo, id_casa, data_inizio, cadenza, turni_cadauno " +
                    "FROM cicli_pulizie WHERE id_casa = ? " +
                    "ORDER BY data_inizio DESC";

    private static final String TROVA_ULTIMO_CICLO =
            "SELECT id_ciclo, id_casa, data_inizio, cadenza, turni_cadauno " +
                    "FROM cicli_pulizie WHERE id_casa = ? AND attivo = true " +
                    "ORDER BY data_inizio DESC LIMIT 1";


    private static final String AGGIORNA =
            "UPDATE cicli_pulizie SET id_casa = ?, data_inizio = ?, cadenza = ?, turni_cadauno = ? " +
                    "WHERE id_ciclo = ?";

    private static final String DISATTIVA_CICLO =
            "UPDATE cicli_pulizie SET attivo = false " +
                    "WHERE id_casa = ? AND attivo = true";

    private static final String CANCELLA =
            "DELETE FROM cicli_pulizie WHERE id_casa = ?";

    @Override
    public void salvaCiclo(CicloPulizie ciclo){
        try (PreparedStatement ps = conn.prepareStatement(
                INSERISCI, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ciclo.getIdCasa());
            ps.setDate(2, Date.valueOf(ciclo.getDataCiclo()));
            ps.setShort(3, ciclo.getCadenza());
            ps.setShort(4, ciclo.getTurniCadauno());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ciclo.setIdCiclo(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel salvataggio del ciclo pulizie", e);
        }
    }


    @Override
    public CicloPulizie trovaID(Integer idCiclo){

        try(PreparedStatement ps = conn.prepareStatement(TROVA_ID)){
            ps.setInt(1, idCiclo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCiclo(rs);
                }
            }

        }catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero ultimo ciclo da ID", e);
        }

        return null;
    }

    @Override
    public List<CicloPulizie> trovaCasa(Integer idCasa){
        List<CicloPulizie> cicli = new ArrayList<CicloPulizie>();

        try(PreparedStatement ps= conn.prepareStatement(TROVA_CASA)){
            ps.setInt(1, idCasa);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cicli.add(mapCiclo(rs));
                }
            }

        }catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero cicli casa", e);
        }

        return cicli;
    }

    @Override
    public CicloPulizie trovaUltimoCicloCasa(Integer idCasa){

        try(PreparedStatement ps = conn.prepareStatement(TROVA_ULTIMO_CICLO)){
            ps.setInt(1, idCasa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCiclo(rs);
                }
            }

        }catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero ultimo ciclo", e);
        }

        return null;
    }

    @Override
    public void aggiornaCiclo(CicloPulizie ciclo){
        try(PreparedStatement ps = conn.prepareStatement(AGGIORNA)){
            ps.setInt(1, ciclo.getIdCasa());
            ps.setDate(2,Date.valueOf(ciclo.getDataCiclo()));
            ps.setShort(3,ciclo.getCadenza());
            ps.setShort(4,ciclo.getTurniCadauno());
            ps.setInt(5,ciclo.getIdCiclo());

            ps.executeUpdate();

        }catch (SQLException e) {
            throw new RuntimeException("Errore nell'aggiornamento ciclo", e);
        }
    }

    @Override
    public void cancellaCiclo(Integer idCasa){
        try(PreparedStatement ps = conn.prepareStatement(DISATTIVA_CICLO)){
            ps.setInt(1,idCasa);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore nella cancellazione ciclo", e);
        }
    }

    private CicloPulizie mapCiclo(ResultSet rs) throws SQLException {
        CicloPulizie ciclo = new CicloPulizie();
        ciclo.setIdCiclo(rs.getInt("id_ciclo"));
        ciclo.setIdCasa(rs.getInt("id_casa"));
        ciclo.setDataCiclo(rs.getDate("data_inizio").toLocalDate());
        ciclo.setCadenza(rs.getShort("cadenza"));
        ciclo.setTurniCadauno(rs.getShort("turni_cadauno"));
        return ciclo;
    }
}